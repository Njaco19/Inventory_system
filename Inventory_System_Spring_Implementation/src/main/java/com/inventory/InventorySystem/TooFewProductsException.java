package com.inventory.InventorySystem;

public class TooFewProductsException extends Exception{

    public TooFewProductsException(String errormessage){
        super(errormessage);
    }

    public TooFewProductsException(){
        this("There were not enough products to perform that instruction");
    }
}
