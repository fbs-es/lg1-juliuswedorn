package fbs.lg1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Aufgabe23Test {

    private Customer customer;
    private Product product;
    private Delivery delivery;

    @BeforeEach
    void setUp() {
        Main.newApp();
        delivery = new Delivery("DHL Express");
        Main.getApp().addDelivery(delivery);

        product = new Product("Test Product", 10.00f, 5, 2, delivery);
        Main.getApp().addProduct(product);

        customer = new Customer("Max Mustermann", "max@example.com");
        Main.getApp().addCustomer(customer);
    }

    @Test
    @DisplayName("Should correctly initialize Customer with constructor")
    void testCustomerConstructor() throws Exception {
        Customer newCustomer = new Customer("Anna", "anna@example.com");

        assertThat((int) getFieldValue("userId", newCustomer)).isEqualTo(2);
        assertThat((String) getFieldValue("name", newCustomer)).isEqualTo("Anna");
        assertThat((String) getFieldValue("email", newCustomer)).isEqualTo("anna@example.com");
        assertThat(newCustomer.getOrders()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should correctly return values from Customer getters")
    void testCustomerGetters() throws Exception {
        assertThat(customer.getUserId()).isEqualTo((int) getFieldValue("userId", customer));
        assertThat(customer.getName()).isEqualTo("Max Mustermann");
        assertThat(customer.getEmail()).isEqualTo("max@example.com");
        assertThat(customer.getOrders()).isNotNull();
    }

    @Test
    @DisplayName("Should correctly initialize Customer via private initialize method")
    void testCustomerInitialize() throws Exception {
        Method initializeMethod = Customer.class.getDeclaredMethod("initialise", String.class, String.class);
        initializeMethod.setAccessible(true);

        initializeMethod.invoke(customer, "New Name", "new@example.com");

        assertThat((String) getFieldValue("name", customer)).isEqualTo("New Name");
        assertThat((String) getFieldValue("email", customer)).isEqualTo("new@example.com");
    }

    @Test
    @DisplayName("Should successfully buy product when stock is available")
    void testBuyProductSuccess() throws Exception {
        boolean result = customer.buyProduct(product);

        assertThat(result).isTrue();
        assertThat(product.getQuantity()).isEqualTo(4);
        assertThat(customer.getOrders()).hasSize(1);

        Order createdOrder = customer.getOrders().get(0);
        assertThat(createdOrder.getDeliveryStatus()).isEqualTo(DeliveryStatus.Open);
        assertThat(createdOrder.getOrderPositions()).hasSize(1);
    }

    @Test
    @DisplayName("Should throw Exception when buying product with zero quantity")
    void testBuyProductOutOfStockThrowsException() throws Exception {
        setFieldValue("quantity", product, 0);

        assertThatThrownBy(() -> customer.buyProduct(product))
                .isInstanceOf(Exception.class)
                .hasMessage("No more products available");
    }

    @Test
    @DisplayName("Should correctly initialize Product constructor")
    void testProductConstructor() throws Exception {
        Product p = new Product("Laptop", 999.99f, 10, 3, delivery);

        assertThat((int) getFieldValue("productId", p)).isEqualTo(2);
        assertThat((String) getFieldValue("name", p)).isEqualTo("Laptop");
        assertThat((float) getFieldValue("price", p)).isEqualTo(999.99f);
        assertThat((int) getFieldValue("quantity", p)).isEqualTo(10);
        assertThat((int) getFieldValue("minQuantity", p)).isEqualTo(3);
    }

    @Test
    @DisplayName("Should correctly sell product and reduce stock quantity")
    void testSellProductSuccess() {
        boolean result = product.sellProduct(3);

        assertThat(result).isTrue();
        assertThat(product.getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should fail to sell product when requested quantity exceeds available stock")
    void testSellProductExceedsQuantity() {
        boolean result = product.sellProduct(10);

        assertThat(result).isFalse();
        assertThat(product.getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should trigger automatic resupply when product quantity falls to or below minQuantity")
    void testCheckResupplyTriggered() throws Exception {
        setFieldValue("quantity", product, 2);

        boolean resupplied = product.checkResupply();

        assertThat(resupplied).isTrue();
        assertThat(product.getQuantity()).isEqualTo(12); // 2 + 10 resupplied
        assertThat(Main.getApp().getDeliveryOrders()).hasSize(1);
    }

    @Test
    @DisplayName("Should not trigger resupply when product quantity is above minQuantity")
    void testCheckResupplyNotTriggered() {
        boolean resupplied = product.checkResupply();

        assertThat(resupplied).isFalse();
        assertThat(product.getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should correctly initialize Order and compute total price")
    void testOrderAndOrderPosition() throws Exception {
        Order order = new Order(DeliveryStatus.Open, 1, customer);

        order.addProduct(product, 2);

        assertThat(order.getOrderId()).isEqualTo(1);
        assertThat(order.getDeliveryStatus()).isEqualTo(DeliveryStatus.Open);
        assertThat(order.getCustomer()).isEqualTo(customer);
        assertThat(order.getOrderPositions()).hasSize(1);

        OrderPosition pos = order.getOrderPositions().get(0);
        assertThat(pos.getOrderPositionId()).isEqualTo(1);
        assertThat(pos.getAmount()).isEqualTo(2);
        assertThat(pos.getPrice()).isEqualTo(10.00f);
        assertThat(pos.computeSubtotal()).isEqualTo(20.00f);
        assertThat(order.computeTotalPrice()).isEqualTo(20.00f);
    }

    @Test
    @DisplayName("Should successfully cancel an open order")
    void testCancelOpenOrderSuccess() {
        Order order = new Order(DeliveryStatus.Open, 1, customer);

        boolean canceled = order.cancel();

        assertThat(canceled).isTrue();
        assertThat(order.getDeliveryStatus()).isEqualTo(DeliveryStatus.Canceled);
    }

    @Test
    @DisplayName("Should fail to cancel an order that is already canceled or sent")
    void testCancelNonOpenOrderFails() throws Exception {
        Order order = new Order(DeliveryStatus.Send, 1, customer);

        boolean canceled = order.cancel();

        assertThat(canceled).isFalse();
        assertThat(order.getDeliveryStatus()).isEqualTo(DeliveryStatus.Send);
    }

    @Test
    @DisplayName("Should correctly initialize Delivery and DeliveryOrder")
    void testDeliveryAndDeliveryOrder() throws Exception {
        DeliveryOrder deliveryOrder = new DeliveryOrder(10, product, delivery);

        assertThat((int) getFieldValue("deliveryOrderId", deliveryOrder)).isEqualTo(1);
        assertThat(deliveryOrder.getAmount()).isEqualTo(10);
        assertThat(deliveryOrder.getProduct()).isEqualTo(product);
        assertThat(deliveryOrder.getDelivery()).isEqualTo(delivery);
        assertThat(deliveryOrder.getDate()).isGreaterThan(0);
    }

    private Object getFieldValue(String fieldName, Object target) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    private void setFieldValue(String fieldName, Object target, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}