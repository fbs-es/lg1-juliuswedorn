package fbs.lg1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Testklasse1 {
    private Spielvariante_1 game;

    @BeforeEach
    void setUp() {
        game = new Spielvariante_1(50, 50);
    }

    @Test
    void testCheckLogic() {
        assertEquals("Gewonnen!", game.check(50));
        assertEquals("Die Zahl ist viel größer!", game.check(10));
        assertEquals("Die Zahl ist etwas größer!", game.check(45));
        assertEquals("Die Zahl ist viel kleiner!", game.check(110));
        assertEquals("Die Zahl ist etwas kleiner!", game.check(55));
    }
}
