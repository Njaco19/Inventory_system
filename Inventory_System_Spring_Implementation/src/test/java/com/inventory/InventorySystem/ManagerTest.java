package com.inventory.InventorySystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {
    Manager m;
    Manager m1;
    Manager m2;
    Manager m3;

    @BeforeEach
    void setUp(){
        try{
            m = new Manager("managerman");
            m1 = new Manager("1");
            m2 = new Manager("2");
            m3 = new Manager("2");
        }catch(InvalidNameException e){
            e.printStackTrace();
        }
    }
    @Test
    void getUsername(){
        Manager nullman = null;
        try{
            nullman = new Manager("");
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
        finally {
            assertEquals("managerman", m.getUsername());
            assertNull(nullman);
        }
    }



    //@Test
    //void orderProduct() {}
}