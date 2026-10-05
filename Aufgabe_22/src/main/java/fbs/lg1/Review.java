package fbs.lg1;

import java.util.List;
import java.util.stream.Collectors;

public class Review {
    private int stars;
    private long date;
    private String comment;
    private boolean hasBought;
    private ReviewAnswer answer;

    public Review(int stars, String comment, boolean hasBought) {
        initialize(stars, comment, hasBought);
    }

    private void initialize(int stars, String comment, boolean hasBought) {
        this.stars = stars;
        this.comment = comment;
        this.hasBought = hasBought;
        this.date = System.currentTimeMillis();
        this.answer = null;
    }

    public int getStars() {
        return stars;
    }

    public String getComment() {
        return comment;
    }

    public ReviewAnswer getAnswer() {
        return answer;
    }

    public void addAnswer(String comment) {
        if (this.answer != null) throw  new IllegalStateException("An answer already exists");
        this.answer = new ReviewAnswer(comment);
    }

    public String getUsername(List<Customer> customers) {
        if (customers == null) return "Unknown User";

        for (Customer customer : customers) {
            List<Review> customerReviews = customer.getReviews();
            if (customerReviews != null && customerReviews.contains(this)) {
                return customer.getName();
            }
        }
        return "Unknown User";
    }

    public boolean isHasBought() {
        return hasBought;
    }

    public static String formatReview(Review r, List<Customer> customers) {
        if (r == null) return "";
        return String.format(
                "%d/5 Stars by %s%s: \"%s\"%s",
                r.getStars(),
                r.getUsername(customers),
                r.isHasBought() ? " [Verified Buyer]" : "",
                r.getComment(),
                (r.getAnswer() != null && r.getAnswer().getComment() != null)
                        ? "\n  -> Answer: " + r.getAnswer().getComment()
                        : ""
        );
    }

    public static List<String> formatReviews(List<Review> reviews, List<Customer> customers) {
        if (reviews == null || reviews.isEmpty()) {
            return List.of("No reviews found.");
        }
        return reviews.stream()
                .map(r -> "- " + formatReview(r, customers))
                .collect(Collectors.toList());
    }
}