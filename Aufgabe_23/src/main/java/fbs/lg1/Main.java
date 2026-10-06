package fbs.lg1;

public class Main {
    private static ECommercePortal portal;

    public static void main(String[] args) {
        startApp();
    }

    public static void startApp() {
        newApp();
        portal.addDelivery(new Delivery("DHLSlow"));
        portal.addDelivery(new Delivery("FailEx"));
        portal.addCustomer(new Customer("Manfred", "manfred@gmail.com"));
        portal.addCustomer(new Customer("Hildegard", "hildegard@gmail.com"));
        portal.addProduct(new Product("Smartphone", 499.99f, 10, 2, portal.getDeliveries().get(0)));
        portal.addProduct(new Product("Headphones", 89.99f, 25, 5, portal.getDeliveries().get(1)));
        portal.genApp();
    }

    public static void newApp() {
        portal = new ECommercePortal();
    }

    public static ECommercePortal getApp() {
        return portal;
    }
}