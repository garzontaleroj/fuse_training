package com.confianza.modelo;

public class StoreWrapper {
    private Store store;

    public StoreWrapper() {}

    public StoreWrapper(Store store) {
        this.store = store;
    }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }

    @Override
    public String toString() {
        return "StoreWrapper{store=" + store + "}";
    }
}
