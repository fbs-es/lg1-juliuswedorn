package fbs.lg1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Aufgabe22Test {

    private Product product;
    private Customer customer;

    @BeforeEach
    void setUp() {
        ECommercePortal ecp = Main.newApp();

        product = new Product("Smartphone", 599.99f);
        ecp.addProduct(product);

        customer = new Customer("Anna Muster");
        ecp.addAccount(customer);
    }

    @Test
    @DisplayName("Sterne unter 1 müssen abgelehnt werden (0 Sterne)")
    void testAddReviewInvalidStarsTooLow() {
        assertThrows(IllegalArgumentException.class, () -> {
            product.addReview(0, "Sehr schlecht", customer);
        }, "Sterne < 1 sollten eine IllegalArgumentException werfen!");
    }

    @Test
    @DisplayName("Sterne über 5 müssen abgelehnt werden (6 Sterne)")
    void testAddReviewInvalidStarsTooHigh() {
        assertThrows(IllegalArgumentException.class, () -> {
            product.addReview(6, "Überragend!", customer);
        }, "Sterne > 5 sollten eine IllegalArgumentException werfen!");
    }

    @Test
    @DisplayName("FAILING TEST: getAvgRating wirft IllegalStateException bei leerer Bewertungsliste")
    void testGetAvgRatingWithoutReviews() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> product.getAvgRating()
        );

        assertEquals("No reviews available to calculate average rating.", exception.getMessage());
    }
    @Test
    @DisplayName("PASSING TEST: Korrekte Berechnung der Durchschnittssterne")
    void testGetAvgRatingSuccess() {
        product.addReview(5, "Super", customer);
        product.addReview(1, "Schlecht", customer);

        assertEquals(3.0f, product.getAvgRating(), 0.001f);
    }

    @Test
    @DisplayName("PASSING TEST: Maximale Anzahl von 1 Antwort pro Bewertung")
    void testReviewCannotHaveMultipleAnswers() {
        product.addReview(4, "Gutes Produkt", customer);
        Review review = product.getReviews().get(0);

        review.addAnswer("Vielen Dank für das Feedback!");

        assertThrows(IllegalStateException.class, () -> {
            review.addAnswer("Zweite Antwort versucht");
        });
    }

    @Test
    @DisplayName("PASSING TEST: Antwortdatum wird beim Erstellen gesetzt")
    void testReviewAnswerHasDate() {
        product.addReview(5, "Top!", customer);
        Review review = product.getReviews().get(0);

        long beforeTimestamp = System.currentTimeMillis();
        review.addAnswer("Danke!");
        long afterTimestamp = System.currentTimeMillis();

        assertNotNull(review.getAnswer());
        assertTrue(review.getAnswer().getDate() >= beforeTimestamp);
        assertTrue(review.getAnswer().getDate() <= afterTimestamp);
    }

    @Test
    @DisplayName("PASSING TEST: Nicht-Käufer erhält gekaufte-Flag = false")
    void testReviewHasBoughtFlagFalseForNonBuyer() {
        product.addReview(3, "Durchschnitt", customer);

        Review review = product.getReviews().get(0);
        assertFalse(review.isHasBought(), "Flag für gekaufte Produkte muss false sein!");
    }

    @Test
    @DisplayName("PASSING TEST: Käufer erhält gekaufte-Flag = true")
    void testReviewHasBoughtFlagTrueForBuyer() {
        customer.buyProduct(product);
        product.addReview(5, "Super Kauf!", customer);

        Review review = product.getReviews().get(0);
        assertTrue(review.isHasBought(), "Flag für gekaufte Produkte muss true sein!");
    }

    @Test
    @DisplayName("PASSING TEST: addReview wirft Exception bei null Customer")
    void testAddReviewNullCustomer() {
        assertThrows(IllegalArgumentException.class, () -> {
            product.addReview(4, "Top", null);
        });
    }
}