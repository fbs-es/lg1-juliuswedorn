package fbs.lg1;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Kinosaal {
    private static final int DEFAULT_REIHEN = 13;
    private static final int DEFAULT_SITZE = 9;

    private final int reihen;
    private final int sitze;
    private final SitzStatus[][] saalPlan;
    private int gefundeneReihe = -1;
    private int startSitz = -1;
    Scanner scanner = new Scanner(System.in);

    public Kinosaal(int reihen, int sitze) {
        this.reihen = reihen;
        this.sitze = sitze;
        this.saalPlan = new SitzStatus[reihen][sitze];
        initSaal();
    }

    public Kinosaal() {
        this(DEFAULT_REIHEN, DEFAULT_SITZE);
    }

    private void initSaal() {
        for (int r = 0; r < reihen; r++) {
            for (int s = 0; s < sitze; s++) {
                saalPlan[r][s] = (r == saalPlan.length - 1) ? SitzStatus.RESERVIERT : SitzStatus.FREI;
            }
        }
    }

    private void printKino() {
        System.out.println("  Aktueller Saalplan");
        System.out.print("  ");
        for (int s = 1; s <= sitze; s++) {
            System.out.printf("%2d ", s);
        }
        System.out.println();

        for (int r = 0; r < reihen; r++) {
            System.out.printf("%2d ", r + 1);
            for (int s = 0; s < sitze; s++) {
                char symbol = switch (saalPlan[r][s]) {
                    case FREI -> 'F';
                    case RESERVIERT -> 'R';
                    case VORGEMERKT -> 'V';
                };
                System.out.print(symbol + "  ");
            }
            System.out.println();
        }
    }

    public boolean reservierePlatz(int r, int s) {
        if (saalPlan[r][s] == SitzStatus.FREI) {
            saalPlan[r][s] = SitzStatus.RESERVIERT;
            return true;
        }
        return false;
    }

    public boolean findePlätze(int anzahl, int wunschSitz, int wunschReihe) {
        for (int r = wunschReihe; r < reihen; r++) {
            int anfang = (r == wunschReihe) ? wunschSitz : 0;
            int freiZaehler = 0;

            for (int s = anfang; s < sitze; s++) {
                freiZaehler = (saalPlan[r][s] == SitzStatus.FREI) ? freiZaehler++ : 0;

                if (freiZaehler == anzahl) {
                    this.startSitz = s - anzahl + 1;
                    this.gefundeneReihe = r;

                    for (int i = 0; i < anzahl; i++) {
                        saalPlan[r][this.startSitz + i] = SitzStatus.VORGEMERKT;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void bestätigeVormerkung(boolean reservieren, int anzahl, int startSitz, int reihe) {
        SitzStatus neuerStatus = reservieren ? SitzStatus.RESERVIERT : SitzStatus.FREI;
        for (int i = 0; i < anzahl; i++) {
            saalPlan[reihe][startSitz + i] = neuerStatus;
        }
    }

    private void Statistik() {
        int F = 0;
        int R = 0;
        int V = 0;
        for (int r = 0; r < reihen; r++) {
            for (int s = 0; s < sitze; s++) {
                switch (saalPlan[r][s]) {
                    case SitzStatus.RESERVIERT:
                        R++;
                        break;
                    case SitzStatus.FREI:
                        F++;
                        break;
                    case SitzStatus.VORGEMERKT:
                        V++;
                        break;
                    default:
                        break;
                }
            }
        }
        System.out.println("    Sitzplan");
        System.out.println("Frei:       " + F);
        System.out.println("Reserviert: " + R);
        System.out.println("Vorgemerkt: " + V);
    }

    public boolean menu() {

        System.out.println("Was möchtest du tun?");
        System.out.println("[1] Reservieren");
        System.out.println("[2] Vormerken");
        System.out.println("[3] Kino-Saal anzeigen");
        System.out.println("[4] Statistiken aufrufen");
        System.out.println("[5] Beenden");
        int auswahl = input(6, 1);

        switch (auswahl) {
            case 1:
                System.out.print("Reihe: ");
                int r = input(reihen, 1) - 1;
                System.out.print("Sitz: ");
                int s = input(sitze, 1) - 1;

                System.out.println(reservierePlatz(r, s) ? "Erfolg!" : "Besetzt!");

                return true;
            case 2:
                System.out.print("Wie viele Sitze: ");
                int anzahl = input(sitze, 1);
                System.out.print("Ab Sitz: ");
                int startSitz = input(sitze, 1) - 1;
                System.out.print("Ab Reihe: ");
                int startReihe = input(reihen, 1) - 1;
                if (findePlätze(anzahl, startSitz, startReihe)) {
                    System.out.println(anzahl + " Plätze gefunden: Reihe " + (gefundeneReihe + 1) +
                            ", Sitze " + (startSitz + 1) + " bis " + (startSitz + anzahl));
                    System.out.println("Reservieren? (1 = Ja, 0 = Nein)");
                    int wahl = input(1, 0);
                    bestätigeVormerkung(wahl == 1, anzahl, startSitz, gefundeneReihe);
                } else {
                    System.out.println("Keine zusammenhängenden Plätze frei.");
                }
                return true;
            case 3:
                printKino();
                return true;
            case 4:
                Statistik();
                return true;
            default:
                return false;
        }
    }

    private int input(int max, int min) {
        int s = -1;
        while (s == -1) {
            try {
                s = Math.clamp(scanner.nextInt(), min, max);
            } catch (InputMismatchException e) {
                System.out.println("Bitte eine Zahl eingeben");
                scanner.next();
            }
        }
        return s;
    }
}
