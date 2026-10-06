package fbs.lg1;

public class OrderPosition {
    private int orderPositionId;
    private int amount;
    private float price;
    private Product product;

    public OrderPosition(int orderPositionId, int amount, float price, Product product) {
        initialize(orderPositionId, amount, price, product);
    }

    private void initialize(int orderPositionId, int amount, float price, Product product) {
        this.orderPositionId = orderPositionId;
        this.amount = amount;
        this.price = price;
        this.product = product;
    }

    public float computeSubtotal() {
        return this.amount * this.price;
    }

    public int getOrderPositionId() { return orderPositionId; }
    public int getAmount() { return amount; }
    public float getPrice() { return price; }
    public Product getProduct() { return product; }
}