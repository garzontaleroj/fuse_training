package com.test.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;

import com.test.model.Book;
import com.test.model.Root;
import com.test.model.Store;

import io.quarkus.logging.Log;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("util")
@RegisterForReflection
public class Util {

	public void counterRowsManifest(Exchange exchange) {
		Book b1 = new Book();
		b1.setTitle("Book 0");
		b1.setPrice(3);
		Book b2 = new Book();
		b2.setTitle("Book 2");
		b2.setPrice(15);
		Book b3 = new Book();
		b2.setTitle("Book 3");
		b2.setPrice(3);

		Store store = new Store();
		store.setBook(new ArrayList<>(List.of(b1, b2, b3)));

		Root root = new Root();
		root.setStore(store);

		exchange.getIn().setBody(root);
//
//	    Integer title0 = root.getStore().getBook().get(0).getPrice();
//	    Log.error("@@@ " + title0);

	}

	@Named("counter")
	public void counter(Exchange exchange) {

		String original = (String) exchange.getProperty("original");

		original = original.replace("Book", "libro");
		exchange.getIn().setBody(original);

	}

}
