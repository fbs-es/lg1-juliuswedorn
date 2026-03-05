package fbs.lg1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Testklasse2 {

    private Spielvariante_2 spiel;

    @BeforeEach
    void setUp() {
        spiel = new Spielvariante_2();
    }

    @Test
    void testGuess2_GrenzenAnpassung() {
        spiel.u = 50;
        spiel.o = 50;
        spiel.Guess1();
        spiel.Guess2("viel größer");
        assertEquals(56, spiel.u, "Untergrenze sollte bei 'viel größer' auf Tipp + 6 steigen");
        spiel.u = 1;
        spiel.o = 100;
        spiel.Guess1();
        int tipp = spiel.Guess1();
        spiel.Guess2("etwas kleiner");
        assertEquals(tipp - 1, spiel.o);
        assertEquals(tipp - 5, spiel.u);
    }

    @Test
    void testGuess3_Berechnung() {
        int ersterTipp = spiel.Guess3("viel größer");
        assertEquals(75, ersterTipp);
        int zweiterTipp = spiel.Guess3("etwas kleiner");
        assertEquals(72, zweiterTipp);
    }

    @Test
    void testGuess3_MathMaxVerhindertStillstand() {
        for (int i = 0; i < 10; i++) {
            spiel.Guess3("viel größer");
        }

        int wertVorher = spiel.Guess3("viel größer");
        int wertNachher = spiel.Guess3("viel größer");

        assertTrue(wertNachher > wertVorher, "Der Wert muss sich um mindestens 1 erhöhen");
    }
}
