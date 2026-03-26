package fbs.lg1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;

class Aufgabe16Test {
    private Kinosaal meinSaal;
    private final int REIHE = 10;
    private final int SITZE = 15;

    @BeforeEach
    void setUp() {
        meinSaal = new Kinosaal(REIHE, SITZE);
    }

    @Test
    void testInitialisierungFrei() throws Exception {
        SitzStatus[][] plan = getSaalPlan(meinSaal);
        assertThat(plan[0][0]).isEqualTo(SitzStatus.FREI);
        assertThat(plan[REIHE - 2][0]).isEqualTo(SitzStatus.FREI);
    }

    @Test
    void testLetzteReiheIstReserviert() throws Exception {
        SitzStatus[][] plan = getSaalPlan(meinSaal);
        for (int s = 0; s < SITZE; s++) {
            assertThat(plan[REIHE - 1][s]).isEqualTo(SitzStatus.RESERVIERT);
        }
    }

    @Test
    void testDefaultConstructor() throws Exception {
        Kinosaal defaultSaal = new Kinosaal();
        SitzStatus[][] plan = getSaalPlan(defaultSaal);
        assertThat(plan.length).isEqualTo(13);
        assertThat(plan[0].length).isEqualTo(9);
    }

    @Test
    void testReservierePlatzErfolgreich() throws Exception {
        meinSaal.reservierePlatz(5, 5);
        SitzStatus[][] plan = getSaalPlan(meinSaal);
        assertThat(plan[5][5]).isEqualTo(SitzStatus.RESERVIERT);
    }

    @Test
    void testCheckVorgemerktPlatzErfolgreich() throws Exception {
        meinSaal.findePlätze(2, 2, 1);
        SitzStatus[][] plan = getSaalPlan(meinSaal);
        assertThat(plan[1][1]).isNotEqualTo(SitzStatus.VORGEMERKT);
        assertThat(plan[1][2]).isEqualTo(SitzStatus.VORGEMERKT);
        assertThat(plan[1][3]).isEqualTo(SitzStatus.VORGEMERKT);
        assertThat(plan[1][4]).isNotEqualTo(SitzStatus.VORGEMERKT);
    }

    private SitzStatus[][] getSaalPlan(Kinosaal kino) throws Exception {
        Field field = Kinosaal.class.getDeclaredField("saalPlan");
        field.setAccessible(true);
        return (SitzStatus[][]) field.get(kino);
    }
}
