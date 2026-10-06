package fbs.lg1;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int userId;
    private String name;
    private String email;
    private List<Order> orders;

    public Customer(String name, String email) {
        initialise(name, email);
    }

    private void initialise(String name, String email) {
        this.userId = Main.getApp().getCustomers().size() + 1;
        this.name = name;
        this.email = email;
        this.orders = new ArrayList<>();
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Order> getOrders() { return orders; }

    public boolean buyProduct(Product product) throws Exception {
        if (product.getQuantity() <= 0) {
            throw new Exception("No more products available");
        }

        if (!product.sellProduct(1)) {
            throw new Exception("Product sale failed");
        }

        int nextId = this.orders.size() + 1;
        Order order = new Order(DeliveryStatus.Open, nextId, this);
        order.addProduct(product, 1);

        this.addOrder(order);

        product.checkResupply();
        return true;
    }
}