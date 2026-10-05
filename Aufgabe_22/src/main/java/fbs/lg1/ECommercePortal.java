package fbs.lg1;

import fbs.lg1.UI.Ui;
import fbs.lg1.UI.UiElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static fbs.lg1.UI.UiElement.Step.step;

public class ECommercePortal {
    private final List<Customer> customers = new ArrayList<>();
    private final List<Manufacturer> manufacturers = new ArrayList<>();
    private final List<Product> products = new ArrayList<>();
    private final Ui ui = new Ui();

    public void addAccount(Customer customer) {
        if (customer != null) customers.add(customer);
    }

    public void addManufacturer(Manufacturer manufacturer) {
        if (manufacturer != null) manufacturers.add(manufacturer);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void addProduct(Product product) {
        if (product != null) products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void genApp() {
        List<UiElement<?>> accountMenuItems = Ui.buildMenu(customers, this::createAppMenu);

        if (accountMenuItems.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        ui.selectValue(accountMenuItems);
    }

    private UiElement<?> createAppMenu(Customer customer, int index) {
        return new UiElement<>(
                customer.getName(),
                String.valueOf(index + 1),
                List.of(
                        new UiElement<>("Print Name", "1",
                                List.of(new UiElement<>("Your Name:", null, step(customer::getName)))
                        ),
                        new UiElement<>("Products", "2",
                                Ui.buildMenu(products, (product, pIndex) -> createProductMenu(customer, product, pIndex))
                        ),
                        Ui.buildDynamicMenu(
                                "Show bought Products",
                                "3",
                                customer::getBoughtProducts,
                                Product::getName
                        )
                )
        );
    }

    private UiElement<?> createProductMenu(Customer customer, Product product, int index) {
        List<Review> reviews = product.getReviews();

        return new UiElement<>(
                product.getName(),
                String.valueOf(index + 1),
                List.of(
                        new UiElement<>("Buy Product", "1",
                                List.of(new UiElement<>("Purchased Product:", null, step(() -> customer.buyProduct(product))))
                        ),
                        new UiElement<>("Review Product", "2",
                                List.of(new UiElement<>("Review Result:", null, step(
                                        Integer::parseInt,
                                        Function.identity(),
                                        (stars, comment) -> {
                                            try {
                                                product.addReview(stars, comment, customer);
                                                return "Review added successfully!";
                                            } catch (Exception e) {
                                                return e.getMessage();
                                            }
                                        }
                                )))
                        ),
                        new UiElement<>("Show Average Rating", "3",
                                List.of(new UiElement<>("Average Rating:", null, step(product::getAvgRating)))
                        ),
                        new UiElement<>(
                                "Show Reviews",
                                "4",
                                List.of(new UiElement<>(
                                        "Reviews:",
                                        null,
                                        step(() -> String.join("\n", Review.formatReviews(reviews, customers)))
                                ))
                        ),
                        new UiElement<>(
                                "Answer a Review",
                                "5",
                                Ui.buildListMenu(
                                        reviews,
                                        review -> Review.formatReview(review, customers),
                                        "Enter your answer:",
                                        review -> step((String answerText) -> {
                                            return "Answer saved!";
                                        })
                                )
                        )
                )
        );
    }
}