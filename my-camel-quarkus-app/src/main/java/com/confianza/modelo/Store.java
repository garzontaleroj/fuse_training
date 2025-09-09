package com.confianza.modelo;

import java.util.List;

public class Store {
    private List<Book> book;

    public Store() {}

    public Store(List<Book> book) {
        this.book = book;
    }

    public List<Book> getBook() { return book; }
    public void setBook(List<Book> book) { this.book = book; }

    @Override
    public String toString() {
        return "Store{book=" + book + "}";
    }
}
