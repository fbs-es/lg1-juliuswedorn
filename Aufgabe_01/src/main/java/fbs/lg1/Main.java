package fbs.lg1;

import java.util.Scanner;

public class Main {
    public static final String win = "Gewonnen!";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String menuChoice;

        do {
            System.out.println("1: Ich rate (Mensch)");
            System.out.println("2: Computer rät");
            System.out.println("67: Testmodus (Pc)");
            System.out.println("Q: Beenden");
            menuChoice = scanner.nextLine().toLowerCase();

            if (menuChoice.equals("1")) {
                startHumanMode(scanner, false, new int[1], 0);
            } else if (menuChoice.equals("2")) {
                startComputerMode(scanner, true, null, "2", 0, 0);
            } else if (menuChoice.equals("67")) {
                Utility.tester(scanner);
            }

        } while (!menuChoice.equals("q"));

        scanner.close();
    }

    private static void startHumanMode(Scanner sc, Boolean log, int[] values, int j) {
        // Hier wird ein neues Objekt der Klasse Spielvariante_1 mit dem Namen game
        // erstellt.
        // Dabei werden die Werte 1 und 100 an den Konstruktor geschikt.
        Spielvariante_1 game = new Spielvariante_1(1, 100);
        String status = "";
        int i = 0;

        while (!status.equals(win)) {
            System.out.print("Dein Tipp (1-100): ");
            String input = sc.nextLine();
            if (Utility.isInputValid(input, 1, 100)) {
                i++;
                status = game.check(Integer.parseInt(input));
                System.out.println(status);
            }
        }
        j++;
        System.out.println("Versuche: " + i);
        if (log) {
            values[j] = j;
        }
    }

    public static void startComputerMode(Scanner sc, Boolean log, int[] values, String KIChoice, int ziel, int index) {
        Spielvariante_2 ki = new Spielvariante_2();
        String status = "";
        int i = 0;
        int tipp;
        if (!log) {
            System.out.print("Gib eine Zahl (1-100) vor, die der PC raten soll: ");
            String goalInput = sc.nextLine();
            if (!Utility.isInputValid(goalInput, 1, 100))
                return;
            ziel = Integer.parseInt(goalInput);
            System.out.println("1: Random KI");
            System.out.println("2: Smart KI");
            KIChoice = sc.nextLine().toLowerCase();
        }
        if (KIChoice.equals("1")) {
            tipp = ki.Guess1();
        } else if (KIChoice.equals("2")) {
            tipp = 50;
        } else {
            return;
        }
        while (!status.equals(win)) {
            i++;
            System.out.println("PC tippt: " + tipp);

            status = Utility.check(tipp, ziel);
            System.out.println("Feedback: " + status);

            if (status.equals(win)) {
                if (log) {
                    values[index] = i;
                }
                break;
            }
            if (KIChoice.equals("1")) {
                ki.Guess2(status);
                tipp = ki.Guess1();
            } else {
                tipp = ki.Guess3(status);
            }
        }
        System.out.println("PC fertig in " + i + " Versuchen.");
    }
}