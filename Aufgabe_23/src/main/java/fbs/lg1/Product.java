package fbs.lg1;

import java.util.List;

public class Product {
    private int productId;
    private String name;
    private float price;
    private int quantity;
    private int minQuantity;
    private Delivery delivery;

    public Product(String name, float price, int quantity, int minQuantity, Delivery delivery) {
        initialize(name, price, quantity, minQuantity, delivery);
    }

    private void initialize(String name, float price, int quantity, int minQuantity, Delivery delivery) {
        List<Product> existingProducts = Main.getApp() != null ? Main.getApp().getProducts() : null;
        this.productId = (existingProducts != null) ? existingProducts.size() + 1 : 1;

        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
        this.delivery = delivery;
    }

    public boolean checkResupply() {
        if (this.quantity <= this.minQuantity) {
            try {
                return sendResupplyMessage();
            } catch (Exception e) {
                System.err.println("Resupply failed: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    private boolean sendResupplyMessage() throws Exception {
        System.out.println("Sending resupply message to supplier...");

        int resupplyAmount = 10;

        if (!this.buyProduct(resupplyAmount)) {
            throw new RuntimeException("Buying new product failed");
        }

        DeliveryOrder deliveryOrder = new DeliveryOrder(resupplyAmount, this, this.delivery);
        Main.getApp().getDeliveryOrders().add(deliveryOrder);

        System.out.println("Resupply accepted! " + resupplyAmount + " items added to stock.");
        return true;
    }

    private boolean buyProduct(int amount) {
        int startQuantity = this.quantity;
        this.quantity += amount;
        return this.quantity == startQuantity + amount;
    }

    public boolean sellProduct(int quantity) {
        if (this.quantity < quantity) {
            return false;
        }
        this.quantity -= quantity;
        return true;
    }

    public String getName() { return name; }
    public float getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getMinQuantity() { return minQuantity; }
    public Delivery getDelivery() { return delivery; }
}