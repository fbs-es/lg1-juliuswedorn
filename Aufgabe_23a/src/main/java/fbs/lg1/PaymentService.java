package fbs.lg1;

public interface PaymentService {
    boolean processPayment(double amount);

    boolean cancelPayment(String transactionId);
}
