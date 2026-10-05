package fbs.lg1;

import java.util.ArrayList;

public class Customer {
    private int userId;
    private String name;
    private ArrayList<Review> reviews;
    private ArrayList<Product> boughtProducts;

    Customer(String name) {
        initialise(name);
    }

    private void initialise(String name) {
        this.userId = Main.getApp().getCustomers().size() + 1;
        this.name = name;
        this.boughtProducts = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public void addReview(Review review) {
        if (review != null) {
            this.reviews.add(review);
        }
    }

    public String getName() {
        return name;
    }

    public boolean buyProduct(Product product) {
        this.boughtProducts.add(product);
        return true;
    }

    public ArrayList<Product> getBoughtProducts() {
        return boughtProducts;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }
}