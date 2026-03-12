package fbs.lg1;

public class Spielvariante_1 {
    private final int secretZahl;

    // Das ist ein Konstruktor. Man muss mit this.secretZahl die Variable
    // initialisieren,
    // um den Wert auch in der Klasse zuzuweisen zu köömem.
    public Spielvariante_1(int u, int o) {
        this.secretZahl = Utility.Random(u, o);
    }

    public String check(int guess) {
        if (guess == secretZahl) {
            return "Gewonnen!";
        }

        String richtung = (guess < secretZahl) ? "größer" : "kleiner";
        String intensitaet = (Math.abs(guess - secretZahl) <= 5) ? "etwas" : "viel";

        return String.format("Die Zahl ist %s %s!", intensitaet, richtung);
    }
}