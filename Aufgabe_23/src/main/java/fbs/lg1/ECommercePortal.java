package fbs.lg1;

import fbs.lg1.UI.Ui;
import fbs.lg1.UI.UiElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static fbs.lg1.UI.UiElement.Step.step;

public class ECommercePortal {
    private final List<Customer> customers = new ArrayList<>();
    private final List<Delivery> deliveries = new ArrayList<>();
    private final List<Product> products = new ArrayList<>();
    private final List<DeliveryOrder> deliveryOrders = new ArrayList<>();
    private final Ui ui = new Ui();

    public void addCustomer(Customer customer) {
        if (customer != null) customers.add(customer);
    }

    public void addDelivery(Delivery delivery) {
        if (delivery != null) deliveries.add(delivery);
    }

    public void addProduct(Product product) {
        if (product != null) products.add(product);
    }

    public List<Delivery> getDeliveries() { return deliveries; }
    public List<Customer> getCustomers() { return customers; }
    public List<Product> getProducts() { return products; }
    public List<DeliveryOrder> getDeliveryOrders() { return deliveryOrders; }

    public void genApp() {
        List<UiElement<?>> mainMenuItems = new ArrayList<>();

        List<UiElement<?>> accountMenuItems = Ui.buildMenu(customers, this::createAppMenu);
        if (!accountMenuItems.isEmpty()) {
            mainMenuItems.add(new UiElement<>("Kundenkonten", "1", accountMenuItems));
        }

        mainMenuItems.add(new UiElement<>("Lieferantenbestellungen (Nachschub-Historie)", "2",
                List.of(new UiElement<>("Alle Lieferantenbestellungen anzeigen", "1",
                        List.of(new UiElement<>("Historie:", null, step(this::formatDeliveryOrders)))
                ))
        ));

        if (mainMenuItems.isEmpty()) {
            System.out.println("Keine Daten zum Aufbauen der UI vorhanden.");
            return;
        }

        ui.selectValue(mainMenuItems);
    }

    private UiElement<?> createAppMenu(Customer customer, int index) {
        return new UiElement<>(
                () -> customer.getName() + " (Kunden-ID: " + customer.getUserId() + ")",
                String.valueOf(index + 1),
                List.of(
                        new UiElement<>("Profil-Informationen drucken", "1",
                                List.of(new UiElement<>("Kundendetails:", null, step(() ->
                                        "Kundennummer: " + customer.getUserId() +
                                                "\nName: " + customer.getName() +
                                                "\nE-Mail: " + customer.getEmail() +
                                                "\nAnzahl Bestellungen: " + customer.getOrders().size()
                                )))
                        ),
                        Ui.<Product>buildDynamicMenu(
                                "Produkte & Einkauf",
                                "2",
                                this::getProducts,
                                (product, pIndex) -> createProductMenu(customer, product, pIndex)
                        ),
                        Ui.<Order>buildDynamicMenu(
                                "Meine Bestellungen & Stornierung",
                                "3",
                                customer::getOrders,
                                (order, oIndex) -> createOrderMenu(customer, order, oIndex)
                        )
                )
        );
    }

    private UiElement<?> createProductMenu(Customer customer, Product product, int index) {
        return new UiElement<>(
                () -> product.getName() + " [" + product.getPrice() + " € | Bestand: " + product.getQuantity() + " | Mindestbestand: " + product.getMinQuantity() + " | Lieferant: " + (product.getDelivery() != null ? product.getDelivery().getName() : "Keiner") + "]",
                String.valueOf(index + 1),
                List.of(
                        new UiElement<>("Produkt kaufen", "1",
                                List.of(new UiElement<>("Kaufpreis & Ergebnis:", null, step(() -> {
                                    try {
                                        boolean success = customer.buyProduct(product);
                                        if (success) {
                                            return "Erfolgreich gekauft: " + product.getName() + "!\nRestlicher Lagerbestand: " + product.getQuantity();
                                        }
                                        return "Kauf fehlgeschlagen.";
                                    } catch (Exception e) {
                                        return "Fehler beim Kauf: " + e.getMessage();
                                    }
                                })))
                        ),
                        new UiElement<>("Nachschub prüfen & auslösen", "2",
                                List.of(new UiElement<>("Status Nachschubprüfung:", null, step(() -> {
                                    boolean triggered = product.checkResupply();
                                    return triggered
                                            ? "Mindestbestand unterschritten/erreicht! Automatische Lieferantenbestellung ausgelöst.\nNeuer Lagerbestand: " + product.getQuantity()
                                            : "Lagerbestand ausreichend (" + product.getQuantity() + " > Mindestbestand " + product.getMinQuantity() + "). Kein Nachschub erforderlich.";
                                })))
                        )
                )
        );
    }

    private UiElement<?> createOrderMenu(Customer customer, Order order, int index) {
        return new UiElement<>(
                () -> "Bestellung #" + order.getOrderId() + " [" + order.getDeliveryStatus() + "] - Gesamt: " + order.computeTotalPrice() + " €",
                String.valueOf(index + 1),
                List.of(
                        new UiElement<>("Bestelldetails anzeigen", "1",
                                List.of(new UiElement<>("Details:", null, step(() -> formatOrderDetails(order))))
                        ),
                        new UiElement<>("Bestellung stornieren", "2",
                                List.of(new UiElement<>("Stornierungsstatus:", null, step(() -> {
                                    if (order.getDeliveryStatus() != DeliveryStatus.Open) {
                                        return "Bestellung kann nicht storniert werden. Aktueller Status: " + order.getDeliveryStatus() + ". Nur OFFENE Bestellungen können storniert werden.";
                                    }
                                    boolean canceled = order.cancel();
                                    if (canceled) {
                                        return "Bestellung #" + order.getOrderId() + " erfolgreich storniert! Automatische Rückerstattung an " + customer.getName() + " wurde veranlasst.";
                                    }
                                    return "Stornierung der Bestellung fehlgeschlagen.";
                                })))
                        )
                )
        );
    }

    private String formatOrderDetails(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bestellnummer: ").append(order.getOrderId())
                .append("\nKunde: ").append(order.getCustomer() != null ? order.getCustomer().getName() : "Unbekannt")
                .append("\nDatum: ").append(new Date(order.getOrderDate()))
                .append("\nStatus: ").append(order.getDeliveryStatus())
                .append("\n\nBestellpositionen:");

        if (order.getOrderPositions() == null || order.getOrderPositions().isEmpty()) {
            sb.append("\n  Keine Positionen vorhanden.");
        } else {
            for (OrderPosition pos : order.getOrderPositions()) {
                sb.append("\n  - Pos #").append(pos.getOrderPositionId())
                        .append(" | Produkt: ").append(pos.getProduct() != null ? pos.getProduct().getName() : "Unbekannt")
                        .append(" | Menge: ").append(pos.getAmount())
                        .append(" | Historischer Einzelpreis: ").append(pos.getPrice()).append(" €")
                        .append(" | Zwischensumme: ").append(pos.computeSubtotal()).append(" €");
            }
        }
        sb.append("\n\nGesamtwert aller Positionen: ").append(order.computeTotalPrice()).append(" €");
        return sb.toString();
    }

    private String formatDeliveryOrders() {
        if (deliveryOrders.isEmpty()) {
            return "Bisher keine Lieferantenbestellungen erfasst.";
        }
        StringBuilder sb = new StringBuilder();
        for (DeliveryOrder order : deliveryOrders) {
            sb.append("Lieferantenbestellung #").append(order.getDeliveryOrderId())
                    .append(" | Produkt: ").append(order.getProduct() != null ? order.getProduct().getName() : "Unbekannt")
                    .append(" | Lieferant: ").append(order.getDelivery() != null ? order.getDelivery().getName() : "Unbekannt")
                    .append(" | Menge: ").append(order.getAmount())
                    .append(" | Datum: ").append(new Date(order.getDate()))
                    .append("\n");
        }
        return sb.toString().trim();
    }
}