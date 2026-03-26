package fbs.lg1;

public class Aufgabe08 {
    public static void main(String[] args) {
        System.out.println(check(false, 20.0, true, true));
        System.out.println("Sie müssen " + check2_add(check2_base(1), false, false) + "€ zahlen");
        System.out.println("Du hast 26 Pakete bestelt und somit " + check3(26) + "% rabatt bekommen");
    }

    public static String check(boolean gefahr, double gewicht, boolean masseKorrekt, boolean zerbrechlich) {
        if (gefahr) {
            return "Aufnahme verweigern.";
        }
        if (gewicht >= 31.5 || !masseKorrekt) {
            return "Verweis an Schalter.";
        }
        if (zerbrechlich) {
            return "Annahme & Druck. Vermerk 'Vorsicht'";
        }
        return "Annahme & Druck.";
    }

    public static int check2_base(int size) {
        return switch (size) {
            case 1 -> 5;
            case 2 -> 10;
            case 3 -> 15;
            default -> 0;
        };
    }

    public static int check2_add(int geld, boolean ausland, boolean express) {
        if (ausland) {
            geld += 10;
            if (express)
                geld += 15;
        } else if (express) {
            geld += 10;
        }

        return geld;
    }

    public static int check3(int Pakete) {
        int[] rabatt = new int[102];
        int[] schwellen = { 1, 11, 51, 101 };
        int[] werte = { 0, 5, 10, 20 };
        for (int i = 1; i < rabatt.length; i++) {
            for (int j = 0; j < schwellen.length; j++) {
                if (i >= schwellen[j]) {
                    rabatt[i] = werte[j];
                }
            }
        }

        int index = Math.clamp(Pakete, 0, 101);
        return rabatt[index];
    }
}
