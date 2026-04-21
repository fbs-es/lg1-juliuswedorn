package fbs.lg1;

public class Ui extends Utils {

    public Ui() {
        VendorMenu();
    }

    private boolean VendorAdminMenu(int index) {
        Vendingmachine vm = vendingmachineList.get(index);
        boolean zurück = true;
        while (zurück) {
            System.out.println("[Admin-Menü " + (index + 1) + "]");
            System.out.println("[1] Alle Gegenstände ausgeben");
            System.out.println("[2] Alle Münzen ausgeben");
            System.out.println("[3] Cent in Euro formatieren");
            System.out.println("[4] Bestandsprüfung ausfüren");
            System.out.println("[5] Bestand auffüllen");
            System.out.println("[6] Kassensturz machen");
            System.out.println("[0] Close Menu");
            System.out.print("Ihre Eingabe: ");
            int choice = intInput();
            switch (choice) {
                case 1:
                    vm.printItem();
                    break;
                case 2:
                    vm.printCoins();
                    break;
                case 3:
                    System.out.println("Betrag in Cent eingeben:");
                    System.out.println(vm.formatToEuro(intInput()));
                    break;
                case 4:
                    vm.checkBefuellStant(restockItems);
                    break;
                case 5:
                    if (restockItems.isEmpty()) {
                        System.out
                                .println("Bitte zuerst Punkt 4 ausführen (Bedarf prüfen), sonst könte es falsch sein!");
                    } else {
                        vm.addBefuellStamm(restockItems);
                        restockItems.clear();
                    }
                    break;
                case 6:
                    System.out.println("In der Kasse sind " + vm.formatToEuro(vm.Cashcollapse()) + " .");
                    break;
                default:
                    zurück = false;
                    break;
            }
        }
        return zurück;
    }

    private boolean Mainmenu(int index) {
        boolean zurück = true;
        String Julius_Wedorn = "Me";
        while (zurück) {
            System.out.println("[Automaten-Menü " + (index + 1) + "]");
            System.out.println("[1] Produkt eingeben");
            System.out.println("[2] Produkte ausgeben");
            System.out.println("[0] Close Menu");
            System.out.print("Ihre Eingabe: ");
            Vendingmachine vm = vendingmachineList.get(index);
            int choice = intInput();
            switch (choice) {
                case 1:
                    System.out.println("Produktname eingeben:");
                    String name = input().toUpperCase();
                    System.out.println("Menge eingeben:");
                    int menge = intInput();
                    int preis = vm.printchashtopay(name, menge);
                    if (preis == 0) {
                        System.out.println("Nicht genug Bestand!");
                    } else if (preis == -5000) {
                        System.out.println(
                                "Ich bin ein Secred falls Sie dieses fidnen vertraun Sie nächster test wird besser (hoff ich) (:");
                        System.out.println("Made by " + Julius_Wedorn);
                    } else if (preis == -1) {
                        System.out.println("Produkt existiert nicht!");
                    } else {
                        System.out.println("Zu zahlen: " + vm.formatToEuro(preis));
                        int currentTotal = 0;
                        while (currentTotal < preis) {
                            System.out.println("Münze einwerfen [0 für Abbruch] :");
                            int coinFromUser = intInput();
                            if (coinFromUser == 0) {
                                vm.refundInput(virtualCoinBox);
                                System.out.println("Abbruch. Geld zurück: " + vm.formatToEuro(currentTotal));
                                currentTotal = 0;
                                break;
                            }
                            int result = vm.findcoin(coinFromUser, currentTotal);
                            if (result != -1) {
                                currentTotal = result;
                            }
                            if (result == -1) {
                                System.out.println("Ungültige Münze!");
                            } else {
                                currentTotal = result;
                                if (currentTotal < preis) {
                                    System.out.println("Noch zu zahlen: " + vm.formatToEuro(preis - currentTotal));
                                }
                            }
                        }
                        if (currentTotal >= preis) {
                            int wechselgeld = currentTotal - preis;
                            if (vm.giveChange(wechselgeld)) {
                                vm.addInput(vm.virtualCoinBox);
                                vm.virtualCoinBox.clear();
                                vm.reduceStock(name, menge);
                                System.out.println("Hier ist dein Produkt!");
                                
                            } else {
                                System.out.println("Automat kann nicht wechseln. Geld wird erstattet: "
                                        + vm.formatToEuro(currentTotal));
                                vm.refundInput(virtualCoinBox);
                            }
                        }
                    }
                    break;
                case 2:
                    vm.printasIteamlist();
                    break;
                default:
                    zurück = false;
                    break;
            }
        }
        return zurück;
    }

    private boolean VendorMenu() {
        boolean zurück = true;
        while (zurück) {
            System.out.println("Verfügbare Automaten: 1 bis " + vendingmachineList.size());
            System.out.println("[Q] Gib den Index der gewünschten Maschine ein:");
            System.out.println("[0] Close Menu");
            System.out.print("Ihre Eingabe: ");

            try {
                int index = intInput();
                if (index >= 1 && index <= vendingmachineList.size() + 1) {
                    int listIndex = index - 1;
                    while (ChoseMenu(listIndex)) {
                    }
                } else {
                    System.out.println("Index existiert nicht.");
                    System.out.println("Tschüsssssssssssssssss");
                    zurück = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe. Bitte eine Zahl eingeben.");
            }
        }
        return zurück;
    }

    private boolean ChoseMenu(int index) {
        boolean zurück = true;
        while (zurück) {
            System.out.println("[Menü Auswal-Menü "+ (index + 1) + "]");
            System.out.println("[1] Normales Menu");
            System.out.println("[2] Admin Menu");
            System.out.println("[0] Close Menu");
            System.out.print("Ihre Eingabe: ");

            int choice = intInput();
            switch (choice) {
                case 1:
                    Mainmenu(index);
                    break;
                case 2:
                    VendorAdminMenu(index);
                    break;
                default:
                    zurück = false;
                    break;
            }
        }
        return zurück;
    }
}
