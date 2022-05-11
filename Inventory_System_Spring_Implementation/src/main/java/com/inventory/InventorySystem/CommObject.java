package com.inventory.InventorySystem;

public class CommObject {
    private String store;
    private String name;
    private String string3;
    private int amount;
    private int id;

    public void setString3(String string){
        this.string3 = string;
    }

    public String getString3(){
        return this.string3;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public int getAmount(){return amount;}

    public void setAmount(int amount){this.amount = amount;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}