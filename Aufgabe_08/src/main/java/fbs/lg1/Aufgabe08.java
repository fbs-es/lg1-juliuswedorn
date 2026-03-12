package fbs.lg1;

public class Aufgabe08 {
    public static void main(String[] args) {
        System.out.println(check(false, 20.0, true, true));
        System.out.println("Sie müssen " + check2(1, false, false) + "€ zahlen");
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

    public static int check2(int size, boolean ausland, boolean express) {
        int geld = switch (size) {
            case 1 -> 5;
            case 2 -> 10;
            case 3 -> 15;
            default -> 0;
        };

        if (ausland) {
            geld += 10;
            if (express)
                geld += 15;
        } else if (express) {
            geld += 10;
        }

        return geld;
    }
}
