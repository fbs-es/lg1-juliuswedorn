package fbs.lg1;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int productId;
    private String name;
    private float price;
    private ArrayList<Review> reviews;

    Product(String name,float price){
        initialize(name,price);
    }

    private void initialize(String name,float price)
    {
        this.productId = Main.getApp().getProducts().size()+1;
        this.name = name;
        this.price = price;
        this.reviews = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public boolean addReview(int stars, String comment, Customer customer) {
        if (customer == null) throw new IllegalArgumentException("Customer cannot be null");

        if ((stars < 1) || (stars > 5)) throw  new IllegalArgumentException("Stars must be between 1 and 5");
        boolean customerHasBought = customer.getBoughtProducts().contains(this);
        Review review = new Review(stars, comment, customerHasBought);
        this.reviews.add(review);
        customer.addReview(review);
        return true;
    }

    public float getAvgRating() {
        if (this.reviews == null || this.reviews.isEmpty()) {
            throw new IllegalStateException("No reviews available to calculate average rating.");
        }

        int totalStars = 0;
        for (Review review : this.reviews) {
            totalStars += review.getStars();
        }

        return (float) totalStars / this.reviews.size();
    }

    public List<Review> getReviews() {
        return this.reviews;
    }
}
