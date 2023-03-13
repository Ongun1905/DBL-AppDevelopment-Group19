package com.appdev.terra.services.helpers;

public class Container<T> {
    private T val;
    public Container(){    }
    public Container(T val) {
        this.val = val;
    }
    public T getVal() {
        return val;
    }
    public void setVal(T val) {
        this.val = val;
    }
}
