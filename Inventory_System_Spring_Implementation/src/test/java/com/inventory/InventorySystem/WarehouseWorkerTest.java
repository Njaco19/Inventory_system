package com.inventory.InventorySystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.CannotProceedException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseWorkerTest {
    WarehouseWorker w1;
    WarehouseWorker w2;
    WarehouseWorker w3;
    WarehouseWorker w4;
    WarehouseWorker nullman;
    @BeforeEach
    void setUp(){
        try {
            w1 = new WarehouseWorker("WarehouseWorker1");
            w2 = new WarehouseWorker("WarehouseWorker2");
            w3 = new WarehouseWorker("WarehouseWorker3");
            w4 = new WarehouseWorker("WarehouseWorker4");

        }catch(InvalidNameException e){
            e.printStackTrace();
        }
        try{
            nullman = new WarehouseWorker("");
        }catch(InvalidNameException e){
            nullman = null;
        }

    }

    @Test
    void getUsername() {
        assertEquals("WarehouseWorker1", w1.getUsername());
        assertEquals("WarehouseWorker2", w2.getUsername());
        assertEquals("WarehouseWorker3", w3.getUsername());
        assertEquals("WarehouseWorker4", w4.getUsername());
        assertNull(nullman);
    }

    @Test
    void beginOrder() {
        try {
            Order o = new Order(new Store("imerco"), new Salesman("Sale man"));
            w1.beginOrder(o);
            assertEquals(OrderStatus.PROCESSING, o.getOrderStatus());
            assertEquals(w1.getWorkingOn(), o);
            assertThrows(CannotProceedException.class, () -> w2.beginOrder(o));
        } catch(InvalidNameException | CannotProceedException e){
            e.printStackTrace();
        }
    }

    @Test
    void completeOrder() {
        try {
            Order o = new Order(new Store("imerco"), new Salesman("Sale man"));
            w1.beginOrder(o);
            assertEquals(OrderStatus.PROCESSING, o.getOrderStatus());
            w1.completeOrder(o);
            assertEquals(o.getOrderStatus(),OrderStatus.COMPLETED);
            assertThrows(CannotProceedException.class, () -> w2.completeOrder(o));
        } catch(InvalidNameException | CannotProceedException e) {
            e.printStackTrace();
        }
    }
}