package com.inventory.InventorySystem;

/**
 * Superclass for all employee types
 */
public abstract class Employee {

    private String username;

    public abstract String getUsername();
    public static Boolean usernameValid(String username){
        return !username.equals("");
    }

}
