package com.test;

import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;

import com.test.model.Root;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Test extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		JacksonDataFormat format = new JacksonDataFormat(Root.class);

//		from("timer://json-once?repeatCount=1").setBody(constant(
//				"{\"store\":{\"book\":[{\"title\":\"Book 1\",\"price\":15},{\"title\":\"Book 2\",\"price\":25}]}}"))
//
//				.setProperty("original").simple("${body}").setProperty("b1").jsonpath("$.store.book[0].price")
//				.setProperty("b3").jsonpath("$.store.book[1].price").setProperty("test").simple("valor")
//				.setProperty("test2").constant("valor2").unmarshal(format)
//				.bean("util", "counterRowsManifest")
//				.log("${body}")
//				.marshal().jacksonXml().log("${body}")
////				.bean("util", "counter")
//				.log("${body}");
//		
		
		
//		from("timer://json-once?repeatCount=1")
//	    .setBody(constant("{\"store\":{\"book\":[{\"title\":\"Book 1\",\"price\":14},{\"title\":\"Book 2\",\"price\":15}]}}"))
//	    .unmarshal(format)
//	    .bean("util", "counterRowsManifest")
////	    .unmarshal().json(JsonLibrary.Jackson, Root.class)
//
//	    .marshal().jacksonXml()
//	    .log("${body}")
//
//	    .convertBodyTo(String.class)
//
//	    .split().xpath("/Root/store/book/book").streaming()
//	        .log("Fragmento book:\n${body}") 
//	        .toD("file:target/books?fileName=book-${exchangeProperty.CamelSplitIndex}.xml")
//	    .end();
//		
		
		
		
		
		from("timer://json-once?repeatCount=1")
	    .setBody(constant("{\"store\":{\"book\":[{\"title\":\"Book 1\",\"price\":14},{\"title\":\"Book 2\",\"price\":15}]}}"))
	    .unmarshal(format)
	    .bean("util", "counterRowsManifest")
//	    .unmarshal().json(JsonLibrary.Jackson, Root.class)

	    .marshal().jacksonXml()
	    .log("${body}")

	    .convertBodyTo(String.class)

//	    .split().xpath("/Root/store/book/book").streaming()
//	        .log("Fragmento book:\n${body}") 
//	        .toD("file:target/books?fileName=book-${exchangeProperty.CamelSplitIndex}.xml")
	    .toD("direct:xml")
	    .end();
		
		
		
		from("direct:xml")
		.log("Recibo ${body}");
		
		
		from("timer://json-once?repeatCount=1")
	    .setBody(constant("{\"store\":{\"book\":[{\"title\":\"Book 1\",\"price\":14},{\"title\":\"Book 2\",\"price\":15},{\"title\":\"Free Book\",\"price\":0}]}}"))
	    .unmarshal(format)
	    .bean("util", "counterRowsManifest")

	    .marshal().jacksonXml()
	    .convertBodyTo(String.class)
	    .log("${body}")

	    .split().xpath("/Root/store/book/book", org.w3c.dom.NodeList.class).streaming()
	        .setHeader("price").xpath("number(./price)", java.lang.Double.class)
	        .setHeader("title").xpath("string(./title)", java.lang.String.class)
	        .log("Book ${header.CamelSplitIndex}: title='${header.title}', price=${header.price}")
	        .choice()
	            .when().simple("${header.price} > 0")
	                .toD("file:target/books/priciomayor?fileName=book-${header.CamelSplitIndex}-${date:now:yyyyMMddHHmmssSSS}.xml")
	            .otherwise()
	                .toD("file:target/books/pricemenor?fileName=book-${header.CamelSplitIndex}-${date:now:yyyyMMddHHmmssSSS}.xml")
	        .end()
	    .end();

		
		
		

	}
	
	
	
	
	
}
