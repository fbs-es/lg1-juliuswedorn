package fbs.lg1;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private long orderDate;
    private DeliveryStatus deliveryStatus;
    private Customer customer;
    private List<OrderPosition> orderPositions = new ArrayList<>();

    public Order(DeliveryStatus deliveryStatus, int orderId, Customer customer) {
        this.deliveryStatus = deliveryStatus;
        this.orderId = orderId;
        this.customer = customer;
        this.orderDate = System.currentTimeMillis();
        this.orderPositions = new ArrayList<>();
    }

    public void addProduct(Product product, int amount) {
        if (product != null && amount > 0) {
            int posId = this.orderPositions.size() + 1;
            OrderPosition position = new OrderPosition(posId, amount, product.getPrice(), product);
            this.orderPositions.add(position);
        }
    }

    public float computeTotalPrice() {
        float total = 0;
        if (orderPositions != null) {
            for (OrderPosition pos : orderPositions) {
                total += pos.computeSubtotal();
            }
        }
        return total;
    }

    public boolean cancel() {
        if (this.deliveryStatus == DeliveryStatus.Open) {
            this.deliveryStatus = DeliveryStatus.Canceled;
            return true;
        }
        return false;
    }

    public int getOrderId() { return orderId; }
    public long getOrderDate() { return orderDate; }
    public DeliveryStatus getDeliveryStatus() { return deliveryStatus; }
    public Customer getCustomer() { return customer; }
    public List<OrderPosition> getOrderPositions() { return orderPositions; }
}