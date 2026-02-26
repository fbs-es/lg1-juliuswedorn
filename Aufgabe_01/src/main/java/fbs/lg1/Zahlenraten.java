package fbs.lg1;

import java.util.Random;
import java.util.Scanner;

public class Zahlenraten {

    public static void main(String[] args) {
        Zahlenraten spiel = new Zahlenraten();
        spiel.starten();
    }

    public int random(int r) {
        Random random = new Random();
        int randomZahl = random.nextInt(r) + 1;
        return randomZahl;
    }

    public String check(int input, int randomZahl) {
        if (input == randomZahl) {
            return "Gewonnen!";
        }
        String richtung;

        if (input < randomZahl) {
            richtung = "größer";
        } else {
            richtung = "kleiner";
        }

        int distanz = Math.abs(input - randomZahl);

        if (distanz <= 4) {
            return "Die Zahl ist etwas " + richtung + "!";
        } else {
            return "Die Zahl ist viel " + richtung + "!";
        }
    }

    public boolean isInputValid(String input) {
        try {
            int value = Integer.parseInt(input);
            return value >= 1 && value <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void starten() {

        int randomZahl = random(100);
        Scanner scanner = new Scanner(System.in);

        boolean valid_input;
        String status = "";
        int i = 0;
        System.out.println("Guten Tag, Sie können eine zahl von 1-100 eingeben um meine geheimzahl zu eraten");

        do {
            System.out.print("Versuch " + (i + 1) + " - Zahl eingeben: ");
            String input = scanner.nextLine();

            valid_input = isInputValid(input);
            if (valid_input) {
                i++;
                int inputInt = Integer.parseInt(input);
                status = check(inputInt, randomZahl);
                System.out.println(status);
            } else {
                System.out.println("Bitte gib eine gültige Zahl ein!(1-100)");
            }

        } while (!status.equals("Gewonnen!"));
        System.out.println("Glückwunsch " + i + " Versuche benötigt.");

        scanner.close();
    }
}
