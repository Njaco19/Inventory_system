package com.inventory.InventorySystem;

import java.util.Objects;

public class Store {

    private static int firstAvailableID = 0;
    private int id;
    private String name;
    private String address;
    private int phoneNr;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getPhoneNr() {
        return phoneNr;
    }

    public Store(String name, String address, int phoneNr){
        this.id = firstAvailableID;
        firstAvailableID++;
        this.name = name;
        this.address = address;
        this.phoneNr = phoneNr;
    }

    public Store(String name){
        this(name, "", -1);
    }

    public void setName(String name) throws InvalidNameException {
        if (isValidString(name)){
            this.name = name;
        } else {
            throw new InvalidNameException();
        }
    }

    public void setAddress(String address) throws InvalidNameException{
        if(isValidString(address)){
            this.address = address;
        } else {
            throw new InvalidNameException();
        }
    }

    public void setPhoneNr(int phoneNr) throws InvalidNameException{
        if(phoneNr >= 0) {
            this.phoneNr = phoneNr;
        } else {
            throw new InvalidNameException();
        }
    }

    public Boolean isValidString(String s){
        return !s.equals("");
    }

    @Override
    public String toString(){
        return  "Name: " + this.name + "\n" +
                "Address: " + this.address + "\n" +
                "Phone: " + this.phoneNr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Store)) return false;
        Store store = (Store) o;
        return id == store.id && phoneNr == store.phoneNr && name.equals(store.name) && address.equals(store.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, phoneNr);
    }
}
