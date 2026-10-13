package fbs.lg1;

public class BasePaymentService implements PaymentService {

    public boolean processPayment(double amount) {
        if (amount <= 0) {
            return false;
        }
        System.out.println("Zahlung von " + amount + " erfolgreich verarbeitet.");
        return true;
    }

    public boolean cancelPayment(String transactionId) {
        System.out.println("Transaktion " + transactionId + " abgebrochen.");
        return true;
    }
}