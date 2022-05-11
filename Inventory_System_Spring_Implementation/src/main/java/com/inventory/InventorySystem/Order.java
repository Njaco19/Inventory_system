package com.inventory.InventorySystem;

import javax.naming.CannotProceedException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Order
 */
public class Order {
    private static int firstAvailableID = 0;
    private final int orderID;
    private HashMap<Product,Integer> orderedProducts;
    private Store store;
    private Salesman salesman;
    private OrderStatus orderStatus;
    private String wwUsername;

    /**
     * Constructs a new order
     * @param store
     * @param salesman
     */
    public Order(Store store, Salesman salesman){
        this.store = store;
        this.salesman = salesman;
        this.orderedProducts = new HashMap<>();
        this.wwUsername = "None";
        orderStatus = OrderStatus.PENDING;
        orderID = firstAvailableID;
        firstAvailableID++;

    }
    public String getWwUsername() {
        return wwUsername;
    }

    public void setWwUsername(String wwUsername) {
        this.wwUsername = wwUsername;
    }

    /**
     * Sets order status
     * @param orderStatus
     */
    public void setOrderStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

    /**
     * Gets order status
     * @return
     */
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    /**
     * Gets store
     * @return
     */
    public Store getStore() {
        return store;
    }

    /**
     * Gets salesman
     * @return
     */
    public Salesman getSalesman() {
        return salesman;
    }

    public int getOrderID(){
        return orderID;
    }

    /**
     * Gets a hashmap of the ordered products
     * @return The products in this order
     */
    public HashMap<Product, Integer> getOrderedProducts() {
        return orderedProducts;
    }

    /**
     * add a new product to this order
     * If the product is already in the map, it will replace it with an incremented value
     * @param product
     * @param amount
     */
    public void addProduct(Product product, int amount){
        if (amount <= 0){
            throw new IllegalArgumentException("Cannot add negative amount of " + product.getName() + ", use deleteProduct instead");
        }
        if(orderedProducts.containsKey(product))
            orderedProducts.replace(product, orderedProducts.get(product) + amount);
        else
            orderedProducts.put(product,amount);
    }

    /**
     * Delete a product from this order
     * @param product
     * @throws NoSuchElementException If the product is not in the order
     */
    public void deleteProduct(String product) throws NoSuchElementException {
        if(orderedProducts.keySet().stream().noneMatch(s -> s.getName().equals(product) )) //product not contained in orderedProducts
            throw new NoSuchElementException();
        else {
            Product p = getProduct(product);
            orderedProducts.remove(p);
        }
    }

    private Product getProduct(String name) throws NoSuchElementException{
        if (this.orderedProducts.keySet()
                .stream()
                .noneMatch(s ->
                        s.getName().equals(name))
        )
            throw new NoSuchElementException("Cannot remove product " + name + ", on account of it not existing.");
        else {
            return this.orderedProducts
                    .keySet()
                    .stream()
                    .filter(s -> s.getName().equals(name))
                    .findFirst()
                    .get();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return orderID == order.orderID && orderedProducts.equals(order.orderedProducts) && store.equals(order.store) && salesman.equals(order.salesman) && orderStatus.equals(order.orderStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderID, orderedProducts, store, salesman, orderStatus);
    }

    @Override
    public String toString(){
        return "Store: " + store +
                "\nSalesman: " + salesman +
                "\nOrder Status: " + orderStatus +
                "\nOrdered products: " + orderedProducts +
                "\nOrderID: " + orderID;
    }

    public void updateOrderStatus() throws CannotProceedException {
        switch (this.orderStatus) {
            case PENDING:
                this.orderStatus = OrderStatus.PROCESSING;
                break;
            case PROCESSING:
                Inventory.getInstance().getEmployees().stream()
                        .filter(s->s.getUsername().equals(this.getWwUsername()))
                        .map(s->(WarehouseWorker) s)
                        .findFirst()
                        .get().completeOrder(this);
                break;
            default:
                throw new UnsupportedOperationException("Cannot advance a completed order!");
        }
    }
}
