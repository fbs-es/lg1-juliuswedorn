package fbs.lg1;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class Aufgabe23aTest {
    @Test
    void testSagHallo() {
        Aufgabe23a aufgabe = new Aufgabe23a();
        assertThat(aufgabe.sagHallo()).contains("Hallo");
    }
}
