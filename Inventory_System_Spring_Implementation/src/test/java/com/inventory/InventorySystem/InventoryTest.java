package com.inventory.InventorySystem;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    Inventory i = Inventory.getInstance();

    @Test
    void getInstance() {
        assertEquals(i, Inventory.getInstance());
    }

    @Test
    void addItem() {
        try {
            Product p;
            p = new Product("product1", 9000);
            Integer qty1 = 1;
            Integer qty2 = 2;
            assertNotNull(p);
            assertThrows(InvalidParameterException.class, () -> i.addItem(p, -1));
            i.addItem(p, qty1);
            i.addItem(p, qty2);
            assertTrue(i.isInStock(p.getName()));
            assertEquals(3, (int) i.getStock().get(p));
            i.deleteItem(p);
        } catch (InvalidNameException | InvalidParameterException | TooFewProductsException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteItem() {
        Product p;
        try {
            p = new Product("product2", 100);
            i.addItem(p,100);
            assertThrows(NoSuchElementException.class, () ->
                    i.deleteItem(new Product("FakeProduct", 100)));
            assertTrue(i.getStock().containsKey(p));
            i.deleteItem(p);
            assertFalse(i.getStock().containsKey(p));
        } catch (InvalidNameException | TooFewProductsException e) {
            e.printStackTrace();
        }
    }

    @Test
    void removeItem() {
        Product p;
        try {
            p = new Product("product3", 100);
            assertThrows(NoSuchElementException.class, () -> i.removeItem(p, 100));
            i.addItem(p,100);
            assertThrows(TooFewProductsException.class, () -> i.removeItem(p, 200));
            i.removeItem(p, 25);
            assertEquals(75, (int) i.getStock().get(p));
            i.deleteItem(p);
        } catch (InvalidNameException | TooFewProductsException e) {
            e.printStackTrace();
        }
    }

    @Test
    void isInStock() {
        Product p;
        try {
            p = new Product("product4", 100);
            i.addItem(p,100);
            assertTrue(i.isInStock(p.getName()));
            assertFalse(i.isInStock(new Product("FakeProduct", 10).getName()));
            i.deleteItem(p);
            assertFalse(i.isInStock(p.getName()));
        } catch (InvalidNameException | TooFewProductsException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addOrder() {
        try {
            Order o1 = new Order(
                    new Store("store1"),
                    new Salesman("sales1"));
            Order o2 = new Order(
                    new Store("store2"),
                    new Salesman("sales2"));
            assertTrue(i.getOrders().isEmpty());
            i.addOrder(o1);
            i.addOrder(o2);
            assertEquals(2, i.getOrders().size());
            i.deleteOrder(o1.getOrderID());
            i.deleteOrder(o2.getOrderID());
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteOrder() {
        try {
            Order o1 = new Order(
                    new Store("store1"),
                    new Salesman("sales1"));
            Order o2 = new Order(
                    new Store("store2"),
                    new Salesman("sales2"));
            assertThrows(NoSuchElementException.class, () ->
                    i.deleteOrder(o1.getOrderID()));
            i.addOrder(o1);
            i.addOrder(o2);
            assertEquals(2, i.getOrders().size());
            i.deleteOrder(o1.getOrderID());
            i.deleteOrder(o2.getOrderID());
            assertTrue(i.getOrders().isEmpty());
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }

    }

    @Test
    void getStock() {
        Product p = null;
        Product q = null;
        try {
            p = new Product("p", 100);
            q = new Product("q", 100);
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
        i.addItem(p,100);
        i.addItem(q, 50);
        HashMap<Product,Integer> prodList = new HashMap<>(2);
        prodList.put(p, 100);
        prodList.put(q, 50);
        assertEquals(prodList, i.getStock());
        try {
            i.deleteItem(p);
            i.deleteItem(q);
        } catch (TooFewProductsException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getOrder() {
        ArrayList<Order> testOrders = new ArrayList<>(2);
        try {
            Order o1 = new Order(
                    new Store("store1"),
                    new Salesman("sales1"));
            Order o2 = new Order(
                    new Store("store2"),
                    new Salesman("sales2"));
            testOrders.add(o1);
            testOrders.add(o2);
            i.addOrder(o1);
            i.addOrder(o2);
            assertEquals(testOrders, i.getOrders());
            i.deleteOrder(o1.getOrderID());
            i.deleteOrder(o2.getOrderID());
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
     }
    @Test
    void newSalesman(){
        try{
            new Salesman("salesman1");
        } catch (InvalidNameException e){
            System.out.println();
        } finally {
            assertThrows(InvalidNameException.class, () -> new Salesman(""));
            assertTrue(i.getEmployees()
                    .stream()
                    .anyMatch(s -> s.getUsername()
                            .equals("salesman1")));
            assertFalse(i.getEmployees()
                    .stream()
                    .anyMatch(s -> s.getUsername()
                            .equals("fake salesman")));
        }
    }

    @Test
    void newWarehouseWorker(){
        try {
            i.newWarehouseWorker("warehouse man");
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
    }
/*
    @Test
    void getEmployees() {
        ArrayList<Employee> tests = new ArrayList<>();
        try {
            new Salesman("sales man");
            i.newWarehouseWorker("warehouse man");
            tests.add(new Salesman("sales man"));
            tests.add(new WarehouseWorker("warehouse man"));
        }catch (InvalidNameException e){
            e.printStackTrace();
        } finally {
            assertEquals(tests, i.getEmployees());
            assertEquals(i.getEmployees().size(), 2);
        }
    }
*/
}