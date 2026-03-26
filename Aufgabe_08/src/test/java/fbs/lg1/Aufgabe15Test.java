package fbs.lg1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Aufgabe15Test {

    @Test
    void testCheck1() {
        Aufgabe08 aufgabe = new Aufgabe08();
        for (int i = 0; i <= 126; i += 10) {
            int j = (i > 100) ? 20 : (i > 50) ? 10 : (i >= 11) ? 5 : 0;
            assertEquals(j, Aufgabe08.check3(i));
        }
    }
}