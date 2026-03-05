package fbs.lg1;

import java.util.Random;
import java.util.Scanner;

public class Utility {

    private static final Random RANDOM = new Random();

    private Utility() {

    }// ich verste wirklich nicht warum mann das machen solte aber Sonar sag das ich
     // das machen soll

    public static int Random(int u, int o) {
        return RANDOM.nextInt(o - u + 1) + u;
    }

    public static boolean isInputValid(String input, int min, int max) {
        try {
            int value = Integer.parseInt(input);
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String check(int input, int randomZahl) {
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

        if (distanz <= 5) {
            return "Die Zahl ist etwas " + richtung + "!";
        } else {
            return "Die Zahl ist viel " + richtung + "!";
        }
    }

    public static void tester(Scanner scanner) {
        System.out.print("Gib eine Zahl (1-100) vor, die der PC raten soll: ");
        String input = scanner.nextLine();

        if (!isInputValid(input, 1, 100)) {
            System.out.println("Ungültige Eingabe.");
            return;
        }

        int zielZahl = Integer.parseInt(input);
        int[] values1 = new int[10];
        int[] values2 = new int[10];

        for (int i = 0; i < 10; i++) {
            Main.startComputerMode(scanner, true, values1, "1", zielZahl, i);
            Main.startComputerMode(scanner, true, values2, "2", zielZahl, i);
        }

        printStats("Random KI (Strategie 1)", values1);
        printStats("Smart KI (Strategie 2)", values2);
    }

    private static void printStats(String name, int[] versuche) {
        double summe = 0;
        for (int wert : versuche) {
            summe += wert;
        }
        double avg = summe / versuche.length;
        System.out.println(name + " - Durchschnittliche Versuche: " + avg);
    }
}