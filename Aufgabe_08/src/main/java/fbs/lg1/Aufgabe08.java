package fbs.lg1;

public class Aufgabe08 {

    public static void main(String[] args) {
        check(false, 20.0, true, true);
    }

    public static void check(boolean gefahr, double gewicht, boolean masseKorrekt, boolean zerbrechlich) {

        if (gefahr) {
            System.out.println("Annahme verweigern");
        } else if (gewicht >= 31.5 || !masseKorrekt) {
            System.out.println("Schalter");
        } else {
            System.out.println("Annahme & Druck");

            if (zerbrechlich) {
                System.out.println("Vermerk 'Vorsicht'");
            }
        }
    }
}
