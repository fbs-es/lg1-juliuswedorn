package fbs.lg1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Testklasse3 {

    @Test
    void testRandomRange() {
        int min = 5;
        int max = 10;
        for (int i = 0; i < 100; i++) {
            int result = Utility.Random(min, max);
            assertTrue(result >= min && result <= max);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "50, 1, 100, true",
            "1, 1, 100, true",
            "100, 1, 100, true",
            "0, 1, 100, false",
            "101, 1, 100, false",
            "abc, 1, 100, false",
            "'', 1, 100, false"
    })
    void testIsInputValid(String input, int min, int max, boolean expected) {
        assertEquals(expected, Utility.isInputValid(input, min, max));
    }

    @ParameterizedTest
    @CsvSource({
            "50, 50, 'Gewonnen!'",
            "46, 50, 'Die Zahl ist etwas größer!'",
            "45, 50, 'Die Zahl ist etwas größer!'",
            "40, 50, 'Die Zahl ist viel größer!'",
            "54, 50, 'Die Zahl ist etwas kleiner!'",
            "55, 50, 'Die Zahl ist etwas kleiner!'",
            "60, 50, 'Die Zahl ist viel kleiner!'"
    })
    void testCheckLogic(int input, int ziel, String expected) {
        assertEquals(expected, Utility.check(input, ziel));
    }
}
