package com.inventory.InventorySystem;

import java.util.ArrayList;

/**
 * A singleton representing the manager of the company
 */
public class Manager extends Employee{

    /**
     * Attributes.
     */
    private String username;

    /**
     * Constructor.
     * @param username
     * @throws InvalidNameException
     */
    public Manager(String username) throws InvalidNameException{
        if(usernameValid(username)) {
            this.username = username;
            Inventory.getInstance().addEmployee(this);
        }else
            throw new InvalidNameException();
    }

    /**
     * Returns the username of this manager.
     * @return username
     */
    @Override
    public String getUsername() {
        return username;
    }


    /**
     * Orders a new product from a supplier.
     * @param product
     * @param qty
     * @param supplier
     */
    //public void orderProduct(String product, int qty, String supplier){}



}
