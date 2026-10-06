package fbs.lg1;

public class Delivery {
    private int deliveryId;
    private String name;

    public Delivery(String name) {
        this.deliveryId = Main.getApp().getDeliveries().size() + 1;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}