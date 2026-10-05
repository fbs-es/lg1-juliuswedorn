package fbs.lg1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Aufgabe21Test {

    private Scooter scooter;
    private User user;

    @BeforeEach
    void setUp() {
        CityGlideApp CGA = Main.newCityGlideApp();
        scooter = new Scooter(80, true);
        CGA.addScooter(scooter);
        user = new User("Sigbert", "sigber@sigma.ru", 30.20f);
        CGA.addAccount(user);
    }

    @Test
    @DisplayName("Should correctly initialize scooter with default constructor")
    void testDefaultConstructor() throws Exception {
        Scooter defaultScooter = new Scooter();

        assertThat((int) getFieldValue("charge", defaultScooter)).isEqualTo(100);
        assertThat((boolean) getFieldValue("locked", defaultScooter)).isTrue();
        assertThat(defaultScooter.getRideHistory()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should correctly initialize scooter with custom constructor parameters")
    void testCustomConstructor() throws Exception {
        Scooter customScooter = new Scooter(50, false);

        assertThat((int) getFieldValue("charge", customScooter)).isEqualTo(50);
        assertThat((boolean) getFieldValue("locked", customScooter)).isFalse();
    }

    @Test
    @DisplayName("Should correctly return values from getters")
    void testGetters() throws Exception {
        assertThat(scooter.getScooterId()).isEqualTo((int) getFieldValue("scooterId", scooter));
        assertThat(scooter.getCharge()).isEqualTo(80);
        assertThat(scooter.isLocked()).isTrue();
        assertThat(scooter.getRideHistory()).isNotNull();
    }

    @Test
    @DisplayName("Should correctly set fields when initialize is called via reflection")
    void testInitialize() throws Exception {
        Method initializeMethod = Scooter.class.getDeclaredMethod("initialize", int.class, boolean.class);
        initializeMethod.setAccessible(true);

        initializeMethod.invoke(scooter, 75, false);

        assertThat((int) getFieldValue("charge", scooter)).isEqualTo(75);
        assertThat((boolean) getFieldValue("locked", scooter)).isFalse();
        assertThat(scooter.getRideHistory()).isNotNull();
    }

    @Test
    @DisplayName("Should successfully update charge when valid value is provided")
    void testSetChargeSuccess() {
        scooter.setCharge(50);
        assertThat(scooter.getCharge()).isEqualTo(50);

        scooter.setCharge(0);
        assertThat(scooter.getCharge()).isEqualTo(0);

        scooter.setCharge(100);
        assertThat(scooter.getCharge()).isEqualTo(100);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when charge is less than 0")
    void testSetChargeBelowZeroThrowsException() {
        assertThatThrownBy(() -> scooter.setCharge(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Battery charge must be between 0% and 100%.");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when charge is greater than 100")
    void testSetChargeAboveHundredThrowsException() {
        assertThatThrownBy(() -> scooter.setCharge(101))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Battery charge must be between 0% and 100%.");
    }

    @Test
    @DisplayName("Should throw Exception when startScooter is called on an unlocked scooter")
    void testStartScooterAlreadyUnlockedThrowsException() throws Exception {
        Scooter unlockedScooter = new Scooter(80, false);

        assertThatThrownBy(() -> unlockedScooter.startScooter(user))
                .isInstanceOf(Exception.class)
                .hasMessage("Scooter is already in use.");
    }

    @Test
    @DisplayName("Should throw Exception when startScooter is called with battery charge at or below 15%")
    void testStartScooterLowBatteryThrowsException() {
        Scooter lowBatteryScooter = new Scooter(15, true);

        assertThatThrownBy(() -> lowBatteryScooter.startScooter(user))
                .isInstanceOf(Exception.class)
                .hasMessage("Battery charge must be above 15%.");
    }

    @Test
    @DisplayName("Should throw Exception when startScooter is called and user credit is less than 1.00")
    void testStartScooterInsufficientUserCreditThrowsException() throws Exception {
        setFieldValue("credit", user, 0.50f);

        assertThatThrownBy(() -> scooter.startScooter(user))
                .isInstanceOf(Exception.class)
                .hasMessage("User credit must be at least €1.00.");
    }

    @Test
    @DisplayName("Should throw Exception when startScooter is called and user account is locked")
    void testStartScooterLockedUserThrowsException() throws Exception {
        setFieldValue("credit", user, 5.00f);
        setFieldValue("isUserLocked", user, true);

        assertThatThrownBy(() -> scooter.startScooter(user))
                .isInstanceOf(Exception.class)
                .hasMessage("User account is locked.");
    }

    @Test
    @DisplayName("Should throw Exception when startScooter is called and user already has an active ride")
    void testStartScooterUserHasActiveRideThrowsException() throws Exception {
        setFieldValue("credit", user, 5.00f);
        setFieldValue("isUserLocked", user, false);
        setFieldValue("currentRide", user, new Ride(1, user.getUserId()));

        assertThatThrownBy(() -> scooter.startScooter(user))
                .isInstanceOf(Exception.class)
                .hasMessage("User already has an active ride.");
    }

    @Test
    @DisplayName("Should successfully start scooter with valid user and state")
    void testStartScooterSuccess() throws Exception {
        setFieldValue("credit", user, 5.00f);
        setFieldValue("isUserLocked", user, false);

        boolean result = scooter.startScooter(user);

        assertThat(result).isTrue();
        assertThat(scooter.isLocked()).isFalse();
        assertThat(getFieldValue("currentRide", scooter)).isNotNull();
    }

    @Test
    @DisplayName("Should throw Exception when stopScooter is called with no active ride")
    void testStopScooterNoActiveRideThrowsException() {
        assertThatThrownBy(() -> scooter.stopScooter(user))
                .isInstanceOf(Exception.class)
                .hasMessage("No active ride found for this user.");
    }

    @Test
    @DisplayName("Should throw Exception when stopScooter is called by a different user")
    void testStopScooterDifferentUserThrowsException() throws Exception {
        User otherUser = new User("Other", "other@sigma.ru", 20.00f);
        setFieldValue("credit", user, 5.00f);
        scooter.startScooter(user);

        assertThatThrownBy(() -> scooter.stopScooter(otherUser))
                .isInstanceOf(Exception.class)
                .hasMessage("No active ride found for this user.");
    }

    @Test
    @DisplayName("Should throw Exception when stopScooter duration is invalid (<= 0 minutes)")
    void testStopScooterInvalidDurationThrowsException() throws Exception {
        setFieldValue("credit", user, 5.00f);

        scooter.startScooter(user);

        assertThatThrownBy(() -> scooter.stopScooter(user))
                .isInstanceOf(Exception.class)
                .hasMessage("Invalid ride duration.");
    }

    @Test
    @DisplayName("Should successfully stop scooter and deduct cost when duration is valid")
    void testStopScooterSuccess() throws Exception {
        setFieldValue("credit", user, 10.00f);

        scooter.startScooter(user);
        Ride currentRide = (Ride) getFieldValue("currentRide", scooter);

        long startTime = System.currentTimeMillis() - 600000; // 10 minutes ago
        setFieldValue("startDate", currentRide, startTime);

        boolean result = scooter.stopScooter(user);

        assertThat(result).isTrue();
        assertThat(scooter.isLocked()).isTrue();
        assertThat(scooter.getCharge()).isEqualTo(70); // 80 - 10 minutes
        assertThat(getFieldValue("currentRide", scooter)).isNull();
        assertThat(scooter.getRideHistory()).hasSize(1);
    }

    @Test
    @DisplayName("Should correctly reduce charge when private discharge method is invoked")
    void testDischarge() throws Exception {
        Method dischargeMethod = Scooter.class.getDeclaredMethod("discharge", int.class);
        dischargeMethod.setAccessible(true);

        dischargeMethod.invoke(scooter, 30);
        assertThat(scooter.getCharge()).isEqualTo(50);

        dischargeMethod.invoke(scooter, 100);
        assertThat(scooter.getCharge()).isEqualTo(0);
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