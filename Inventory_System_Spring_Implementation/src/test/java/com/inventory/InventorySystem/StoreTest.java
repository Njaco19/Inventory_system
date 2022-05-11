package com.inventory.InventorySystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

    Store s1;
    Store s2;

    @BeforeEach
    public void startUp(){
        s1 = new Store("Imerco");
        s2 = new Store("Bahne");
    }
    @Test
    void setName() {
        assertThrows(InvalidNameException.class, () -> s1.setName(""));
        try {
            s1.setName("Store1");
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
        assertTrue(s1.toString().contains("Store1"));
    }

    @Test
    void setAddress() {
        assertThrows(InvalidNameException.class, () -> s1.setAddress(""));
        try {
            s1.setAddress("Istedvænget 11.3 tv, 5000 Odense C");
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
        assertTrue(s1.toString().contains("Istedvænget 11.3 tv, 5000 Odense C"));

    }

    @Test
    void setPhoneNr() {
        assertThrows(InvalidNameException.class, () -> s1.setPhoneNr(-1));
        try {
            s1.setPhoneNr(12345678);
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
        assertTrue(s1.toString().contains("12345678"));
    }
}