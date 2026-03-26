package fbs.lg1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Aufgabe08Test {

    @Test
    void testCheck1() {
        assertEquals("Aufnahme verweigern.", Aufgabe08.check(true, 10.0, true, false));

        assertEquals("Annahme & Druck. Vermerk 'Vorsicht'", Aufgabe08.check(false, 20.0, true, true));

        assertEquals("Annahme & Druck.", Aufgabe08.check(false, 10.0, true, false));

        assertEquals("Verweis an Schalter.", Aufgabe08.check(false, 15.0, false, false));

        assertEquals("Verweis an Schalter.", Aufgabe08.check(false, 40.0, true, false));
    }
}
