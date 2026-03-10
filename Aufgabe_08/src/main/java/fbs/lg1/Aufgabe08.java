package fbs.lg1;

public class Aufgabe08 {
    public static void main(String[] args) {
        System.out.println(check(false, 20.0, true, true));
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
}
