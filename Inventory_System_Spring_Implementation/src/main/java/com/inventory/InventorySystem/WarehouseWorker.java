package com.inventory.InventorySystem;

import javax.naming.CannotProceedException;
import java.util.Objects;

/**
 * Class representing an employee of type warehouse worker.
 */
public class WarehouseWorker extends Employee{

    /**
     * Attributes.
     */
    private String username;
    private Order workingOn;

    /**
     * Constructor.
     * @param username
     */
    public WarehouseWorker(String username) throws InvalidNameException{
        if(usernameValid(username)) {
            this.username = username;
            this.workingOn = null;
            Inventory.getInstance().addEmployee(this);
        }else
            throw new InvalidNameException();
    }

    /**
     * Returns the username of this warehouse worker.
     * @return username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Returns the order this warehouse worker is working on.
     * @return Order workingOn
     */
    public Order getWorkingOn(){return workingOn;}

    /**
     * Adds this order to this warehouse workers list of orders,
     * and sets the status of the specified order to "Processing"
     * @param order
     */
    public void beginOrder(Order order) throws CannotProceedException{
        if(!order.getOrderStatus().equals(OrderStatus.PENDING)){
            throw new CannotProceedException("Cannot start order that are not PENDING");
        }
        this.workingOn = order;
        this.workingOn.setWwUsername(this.username);
        order.setOrderStatus(OrderStatus.PROCESSING);
    }

    /**
     * Removes order from this warehouse workers list of orders,
     * and sets the status of the specified order to "Completed"
     * @param order Order to complete.
     */
    public void completeOrder(Order order) throws CannotProceedException{
        if(!order.getOrderStatus().equals(OrderStatus.PROCESSING)){
            throw new CannotProceedException("Cannot complete an order that has not been processed");
        }
        this.workingOn = null;
        order.setOrderStatus(OrderStatus.COMPLETED);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WarehouseWorker)) return false;
        WarehouseWorker that = (WarehouseWorker) o;
        return username.equals(that.username) && Objects.equals(workingOn, that.workingOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, workingOn);
    }

    @Override
    public String toString(){
        return username;
    }

}
