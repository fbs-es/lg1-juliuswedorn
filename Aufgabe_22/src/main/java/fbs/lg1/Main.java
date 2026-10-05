package fbs.lg1;

public class Main {

    private static ECommercePortal ECommercePortal;

    public static void main(String[] args) {
        startApp();
    }

    private static void startApp() {
        newApp();
        ECommercePortal.addAccount(new Customer("Manfred"));
        ECommercePortal.addAccount(new Customer("Udolf"));
        ECommercePortal.addManufacturer(new Manufacturer());
        ECommercePortal.addManufacturer(new Manufacturer());
        ECommercePortal.addProduct(new Product("Gun",3));
        ECommercePortal.addProduct(new Product("Bread",8));
        ECommercePortal.genApp();
    }

    public static ECommercePortal newApp() {
        return ECommercePortal = new ECommercePortal();
    }

    public static ECommercePortal getApp() {
        return ECommercePortal;
    }
}
