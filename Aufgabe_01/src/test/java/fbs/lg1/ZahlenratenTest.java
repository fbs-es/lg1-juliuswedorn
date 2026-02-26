package fbs.lg1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ZahlenratenTest {
    private final Zahlenraten game = new Zahlenraten();

    @Test
    void testRandomRange() {
        for (int i = 0; i < 100; i++) {
            int res = game.random(100);
            assertTrue(res >= 1 && res <= 100, "Zahl " + res + " out of bounds");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = { "1", "50", "100" })
    void testValidInputs(String input) {
        assertTrue(game.isInputValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = { "0", "101", "-5", "abc", "", " " })
    void testInvalidInputs(String input) {
        assertFalse(game.isInputValid(input));
    }

    @Test
    public void testCheckLogic() {
        Zahlenraten check = new Zahlenraten();
        int testzahl = 50;
        assertEquals("Gewonnen!", check.check(50, testzahl));
        assertEquals("Die Zahl ist viel größer!", check.check(10, testzahl));
        assertEquals("Die Zahl ist etwas größer!", check.check(45, testzahl));
        assertEquals("Die Zahl ist viel kleiner!", check.check(110, testzahl));
        assertEquals("Die Zahl ist etwas kleiner!", check.check(55, testzahl));
    }
}
