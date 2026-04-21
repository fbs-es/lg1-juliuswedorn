package fbs.lg1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fbs.lg1.enums.Coins.MuenzTyp;
import fbs.lg1.enums.Items.Item;

class Aufgabe17Test {
    private Vendingmachine vm;

    @BeforeEach
    void setUp() {
        vm = new Vendingmachine();
    }

    @Test
    void testInventoryInitializesWithZero() throws Exception {
        Field field = Vendingmachine.class.getDeclaredField("inventory");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        EnumMap<?, Integer> inventory = (EnumMap<?, Integer>) field.get(vm);

        assertNotNull(inventory);
        assertFalse(inventory.isEmpty());

        for (Integer count : inventory.values()) {
            assertEquals(0, count);
        }
    }

    @Test
    void testCoinBoxInitializesWithZero() throws Exception {
        Field field = Vendingmachine.class.getDeclaredField("coinBox");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        EnumMap<?, Integer> coinBox = (EnumMap<?, Integer>) field.get(vm);

        assertNotNull(coinBox);
        assertFalse(coinBox.isEmpty());

        for (Integer count : coinBox.values()) {
            assertEquals(0, count);
        }
    }

    @Test
    void testformatToEuro() {
        assertEquals("1.50 €", vm.formatToEuro(150));
        assertEquals("0.05 €", vm.formatToEuro(5));
        assertEquals("0.26 €", vm.formatToEuro(26));
        assertEquals("2.00 €", vm.formatToEuro(200));
    }

    @Test
    void testgetCoinValues() {
        Collection<MuenzTyp> keys = vm.getCoindKeys();

        assertNotNull(keys);
        assertTrue(keys.contains(MuenzTyp.ZWEI_EURO));
        assertTrue(keys.contains(MuenzTyp.EIN_EURO));
        assertTrue(keys.contains(MuenzTyp.ZWANZIG_CENT));
        assertTrue(keys.contains(MuenzTyp.FUENFZIG_CENT));
    }

    @Test
    void testgetItemKeys() {
        Collection<Item> keys = vm.getItemKeys();

        assertNotNull(keys);
        assertTrue(keys.contains(Item.COFFEE));
        assertTrue(keys.contains(Item.COLA));
        assertTrue(keys.contains(Item.MATE));
        assertTrue(keys.contains(Item.VODKA));
    }

    @Test
    void testcheckBefuellStant() {
        ArrayList<Item> checklist = new ArrayList<>();
        vm.checkBefuellStant(checklist);
        assertEquals(6, checklist.size());

    }

    @Test
    void testaddBefuellStamm() {
        ArrayList<Item> checklist = new ArrayList<>(List.of(Item.COFFEE, Item.VODKA));
        vm.addBefuellStamm(checklist);
        assertThat(vm.getItemValues()).containsExactly(0, 10, 0, 0, 0, 10);
    }

    @Test
    void testCashcollapse() throws Exception {
        Field field = Vendingmachine.class.getDeclaredField("coinBox");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        EnumMap<MuenzTyp, Integer> coinBox = (EnumMap<MuenzTyp, Integer>) field.get(vm);

        coinBox.clear();
        coinBox.put(MuenzTyp.EIN_EURO, 1);
        coinBox.put(MuenzTyp.FUENFZIG_CENT, 1);

        int gesamtSumme = vm.Cashcollapse();

        assertEquals(150, gesamtSumme);
    }

    @Test
    void testprintchashtopay() throws Exception {
        assertEquals(0, vm.printchashtopay("WATER", 1));

        Field field = Vendingmachine.class.getDeclaredField("inventory");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        EnumMap<Item, Integer> inventory = (EnumMap<Item, Integer>) field.get(vm);

        inventory.put(Item.WATER, 10);
        assertEquals(180, vm.printchashtopay("WATER", 1));
    }

    @Test
    void testreduceStock() throws Exception {
        assertThat(vm.getItemValues()).containsExactly(0, 0, 0, 0, 0, 0);

        Field field = Vendingmachine.class.getDeclaredField("inventory");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        EnumMap<Item, Integer> inventory = (EnumMap<Item, Integer>) field.get(vm);

        inventory.put(Item.WATER, 26);

        vm.reduceStock("WATER", 25);

        assertThat(vm.getItemValues()).containsExactly(1, 0, 0, 0, 0, 0);
    }

    @Test
    void testFindCoin_Logic() {
        assertEquals(-1, vm.findcoin(26, 0));

        assertEquals(46, vm.findcoin(20, 26));

        assertEquals(-1, vm.findcoin(0, 0));
        assertEquals(-1, vm.findcoin(-1, 10));
    }

    @Test
    void testrefundInput() throws Exception {
        Field vBoxField = Vendingmachine.class.getDeclaredField("virtualCoinBox");
        vBoxField.setAccessible(true);
        @SuppressWarnings("unchecked")
        EnumMap<MuenzTyp, Integer> virtualCoinBox = (EnumMap<MuenzTyp, Integer>) vBoxField.get(vm);

        Field cBoxField = Vendingmachine.class.getDeclaredField("coinBox");
        cBoxField.setAccessible(true);
        @SuppressWarnings("unchecked")
        EnumMap<MuenzTyp, Integer> coinBox = (EnumMap<MuenzTyp, Integer>) cBoxField.get(vm);

        coinBox.put(MuenzTyp.EIN_EURO, 5);
        coinBox.put(MuenzTyp.FUENFZIG_CENT, 2);

        virtualCoinBox.put(MuenzTyp.EIN_EURO, 1);
        virtualCoinBox.put(MuenzTyp.FUENFZIG_CENT, 1);

        vm.refundInput(virtualCoinBox);

        assertEquals(4, coinBox.get(MuenzTyp.EIN_EURO));
        assertEquals(1, coinBox.get(MuenzTyp.FUENFZIG_CENT));
    }

    @Test
    void testaddInput() throws Exception {
        Field vBoxField = Vendingmachine.class.getDeclaredField("virtualCoinBox");
        vBoxField.setAccessible(true);
        @SuppressWarnings("unchecked")
        EnumMap<MuenzTyp, Integer> virtualCoinBox = (EnumMap<MuenzTyp, Integer>) vBoxField.get(vm);

        Field cBoxField = Vendingmachine.class.getDeclaredField("coinBox");
        cBoxField.setAccessible(true);
        @SuppressWarnings("unchecked")
        EnumMap<MuenzTyp, Integer> coinBox = (EnumMap<MuenzTyp, Integer>) cBoxField.get(vm);

        coinBox.put(MuenzTyp.EIN_EURO, 5);
        coinBox.put(MuenzTyp.FUENFZIG_CENT, 2);

        virtualCoinBox.put(MuenzTyp.EIN_EURO, 1);
        virtualCoinBox.put(MuenzTyp.FUENFZIG_CENT, 1);

        vm.addInput(virtualCoinBox);

        assertEquals(6, coinBox.get(MuenzTyp.EIN_EURO));
        assertEquals(3, coinBox.get(MuenzTyp.FUENFZIG_CENT));
    }

    @Test
    void testgiveChange() throws Exception {
        Field field = Vendingmachine.class.getDeclaredField("coinBox");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        EnumMap<MuenzTyp, Integer> coinBox = (EnumMap<MuenzTyp, Integer>) field.get(vm);

        coinBox.put(MuenzTyp.EIN_EURO, 3);
        coinBox.put(MuenzTyp.FUENFZIG_CENT, 2);

        boolean success = vm.giveChange(150);

        assertTrue(success);
        assertEquals(2, coinBox.get(MuenzTyp.EIN_EURO));
        assertEquals(1, coinBox.get(MuenzTyp.FUENFZIG_CENT));

        boolean fail = vm.giveChange(500);

        assertFalse(fail);
        assertEquals(2, coinBox.get(MuenzTyp.EIN_EURO));
    }

}
