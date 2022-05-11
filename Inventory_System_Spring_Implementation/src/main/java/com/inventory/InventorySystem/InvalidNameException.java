package com.inventory.InventorySystem;

public class InvalidNameException extends Exception{

    public InvalidNameException(String message){
        super(message);
    }

    public InvalidNameException(){
        this("Name not valid!");
    }

}
