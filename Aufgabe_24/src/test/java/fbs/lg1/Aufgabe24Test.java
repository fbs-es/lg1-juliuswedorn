package fbs.lg1;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class Aufgabe24Test {
    @Test
    void testSagHallo() {
        Main aufgabe = new Main();
        assertThat(aufgabe.sagHallo()).contains("Hallo");
    }
}
