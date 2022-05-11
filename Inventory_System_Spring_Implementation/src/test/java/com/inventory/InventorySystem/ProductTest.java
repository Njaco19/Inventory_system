package com.inventory.InventorySystem;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    Product p;
    Product q;
    Product z;

    {
        try {
            p = new Product("Bazinga", 880);
            q = new Product("Bazinga", 880);
            z = new Product("Sheldon", 880);
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
    }

    @Test
    void setPrice() {
        assertEquals(880, p.getPrice());
        p.setPrice(200);
        assertEquals(200, p.getPrice());

        assertThrows(InvalidParameterException.class, () -> p.setPrice(-2));
    }

    @Test
    void getName() {
        assertThrows(InvalidNameException.class, () -> new Product("",0));
        assertEquals("Bazinga", p.getName());
    }

    @Test
    void testEquals() {
        assertEquals(p, q);
        assertNotEquals(p,z);
        q.setPrice(1);
        assertEquals(p,q);
    }
}