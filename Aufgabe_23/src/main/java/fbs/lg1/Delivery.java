package fbs.lg1;

public class Delivery {
    private int deliveryId;
    private String name;

    public Delivery(String name) {
        initialize(name);
    }

    private void initialize(String name) {
        this.name = name;
        this.deliveryId = Main.getApp().getDeliveries().size() +1;
    }

    public String getName() {
        return name;
    }
}