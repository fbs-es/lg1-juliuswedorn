package fbs.lg1;

import java.util.ArrayList;
import java.util.Scanner;

import fbs.lg1.enums.Items.Item;

public class Utils extends Vendingmachine {
    private Scanner scanner = new Scanner(System.in);
    protected Vendingmachine vendingmachine1 = new Vendingmachine();
    protected Vendingmachine vendingmachine2 = new Vendingmachine();
    protected ArrayList<Vendingmachine> vendingmachineList = new ArrayList<>();

    protected ArrayList<Item> restockItems = new ArrayList<>();

    public Utils() {
        vendingmachineList.add(vendingmachine1);
        vendingmachineList.add(vendingmachine2);
    }

    protected String input() {
        return scanner.nextLine();
    }

    protected int intInput() {
        while (true) {
            try {
                return Integer.parseInt(input());
            } catch (NumberFormatException e) {
                System.out.println("Fehler: Bitte gib eine Zahl.");
            }
        }
    }

    // was eine banger class sehr wichtig (wollt eigentlich mehr reinpacken aber hab
    // nix gefunden das hir reinpassen würde)
}
