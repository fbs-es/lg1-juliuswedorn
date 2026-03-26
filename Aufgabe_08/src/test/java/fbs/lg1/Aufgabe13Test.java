package fbs.lg1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Aufgabe13Test {

    @ParameterizedTest
    @CsvSource({
            "1, false, false, 5",
            "1, true,  false, 15",
            "1, true,  true,  30",
            "1, false, true,  15",
            "2, false, false, 10",
            "2, true,  true,  35",
            "3, false, false, 15",
            "3, true,  true,  40",
            "0, false, false, 0",
            "4, true,  true,  25"
    })
    void testCheck2Combined(int size, boolean ausland, boolean express, int expected) {
        int baseGeld = Aufgabe08.check2_base(size);
        int totalGeld = Aufgabe08.check2_add(baseGeld, ausland, express);

        assertEquals(expected, totalGeld);
    }
}
