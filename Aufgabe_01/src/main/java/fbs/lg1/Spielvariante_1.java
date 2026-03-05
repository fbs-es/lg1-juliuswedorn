package fbs.lg1;

public class Spielvariante_1 {
    private final int secretZahl;

    // Das ist ein Konstruktor. Man muss mit this.secretZahl die Variable
    // initialisieren,
    // um den Wert auch in der Klasse zuzuweisen zu köömem.
    public Spielvariante_1(int u, int o) {
        this.secretZahl = Utility.Random(u, o);
    }

    public String Check(int guess) {
        if (guess == secretZahl) {
            return "Gewonnen!";
        }
        String richtung;
        if (guess < secretZahl) {
            richtung = "größer";
        } else {
            richtung = "kleiner";
        }
        int distanz = Math.abs(guess - secretZahl);

        if (distanz <= 5) {
            return "Die Zahl ist etwas " + richtung + "!";
        } else {
            return "Die Zahl ist viel " + richtung + "!";
        }
    }
}