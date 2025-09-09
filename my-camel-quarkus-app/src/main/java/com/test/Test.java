package com.test;

import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;

import com.confianza.modelo.Book;
import com.confianza.modelo.StoreWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class Test extends RouteBuilder {

	@Override
	public void configure() throws Exception {
	
		// Primer ruta que se ejecuta una sola vez
		from("timer://once?repeatCount=1").routeId("hello-once")
        .log("Hola Camel Quarkus");		


        // Segunda ruta que se ejecuta una sola vez y procesa JSON
        Predicate isTypeA = jsonpath("$.type", String.class).isEqualTo("A");
        Predicate isTypeB = jsonpath("$.type", String.class).isEqualTo("B");

        from("timer://json-once?repeatCount=1")
            .routeId("json-choice-once")
            .setBody().constant("{\"type\":\"B\",\"message\":\"Hola desde JSON\"}")
            .setHeader("Content-Type", constant("application/json"))
            .choice()
                .when(isTypeA)
                    .log("[JSON] Tipo A -> ${body}")
                .when(isTypeB)
                    .log("[JSON] Tipo B -> ${body}")
                .otherwise()
                    .log("[JSON] Tipo desconocido -> ${body}")
            .end();



        // Tercera ruta que se ejecuta una sola vez y extrae datos de JSON
        from("timer://json-oncerepeatCount=1")
        .setBody(constant("{\"store\":{\"book\":[{\"title\":\"Book 1\",\"price\":15},{\"title\":\"Book 2\",\"price\":25}]}}"))
        .log("Original JSON: ${body}")
        .setHeader("b1").jsonpath("$.store.book[1].title") 
//	        .transform().jsonpath("$.store.book[1].title") 
        .log("Extracted title: ${headers}")
        .end();

        // Cuarta ruta que se ejecuta una sola vez y procesa XML
        Predicate isXmlA = xpath("/root/type = 'A'");
        Predicate isXmlB = xpath("/root/type = 'B'");

        from("timer://xml-once?repeatCount=1")
            .routeId("xml-choice-once")
            .setBody().constant("<root><type>B</type><message>Hola desde XML</message></root>")
            .setHeader("Content-Type", constant("application/xml"))
            .choice()
                .when(isXmlA)
                    .log("[XML] Tipo A -> ${body}")
                .when(isXmlB)
                    .log("[XML] Tipo B -> ${body}")
                .otherwise()
                    .log("[XML] Tipo desconocido -> ${body}")
            .end();	 

        // Quinta ruta que se ejecuta una sola vez y suma precios en JSON        
        from("timer://json-once?repeatCount=1")
            .routeId("json-sumar-precios")
            .setBody().constant("{\"store\":{\"book\":[{\"title\":\"Book 1\",\"price\":15},{\"title\":\"Book 2\",\"price\":25}]}}")
            .log("JSON original: ${body}")
            .setHeader("price1", jsonpath("$.store.book[0].price"))
            .setHeader("price2", jsonpath("$.store.book[1].price"))
            .process(exchange -> {
                int p1 = exchange.getIn().getHeader("price1", Integer.class);
                int p2 = exchange.getIn().getHeader("price2", Integer.class);
                int total = p1 + p2;
                exchange.getIn().setBody("Suma de precios: " + total);
            })
            .log("${body}");


        // Sexta ruta que se ejecuta una sola vez y procesa XML con choice
        from("timer://xml-twice?repeatCount=1")
            .routeId("xml-choice-twice")
            .setBody(constant("<root><type>B</type><message>Hola desde XML B</message></root>"))
            .setHeader("Content-Type", constant("application/xml"))
            .choice()
                .when(xpath("/root/type = 'B'"))
                    .log("[XML] Tipo B detectado -> ${body}")
                    .setBody(constant("<root><type>B</type><message>Transformado desde B</message></root>"))
                .otherwise()
                    .log("[XML] Tipo desconocido -> ${body}")
                    .setBody(constant("<root><type>?</type><message>Tipo desconocido</message></root>"))
            .end()
            .log("XML final: ${body}");


        // Septima ruta que se ejecuta una sola vez y deserializa JSON a objetos Java
        from("timer://json-java-once?repeatCount=1")
        .routeId("json-java-once")
        .setBody().constant("{\"store\":{\"book\":[{\"title\":\"Book 1\",\"price\":15},{\"title\":\"Book 2\",\"price\":25}]}}")
        .log("JSON original: ${body}")
        .process(exchange -> {
            String json = exchange.getIn().getBody(String.class);
            ObjectMapper mapper = new ObjectMapper();
            StoreWrapper wrapper = mapper.readValue(json, StoreWrapper.class);
            int total = wrapper.getStore().getBook().stream().mapToInt(Book::getPrice).sum();
            System.out.println("Total: " + total);
        });

	}
}