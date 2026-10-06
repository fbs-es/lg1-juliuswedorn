package fbs.lg1;

import java.util.Date;

public class DeliveryOrder {
    private int deliveryOrderId;
    private long date;
    private int amount;
    private Product product;
    private Delivery delivery;

    public DeliveryOrder(int amount, Product product, Delivery delivery) {
        this.deliveryOrderId = Main.getApp().getDeliveryOrders().size() + 1;
        this.date = new Date().getTime();
        this.amount = amount;
        this.product = product;
        this.delivery = delivery;
    }

    public int getDeliveryOrderId() { return deliveryOrderId; }
    public long getDate() { return date; }
    public int getAmount() { return amount; }
    public Product getProduct() { return product; }
    public Delivery getDelivery() { return delivery; }
}