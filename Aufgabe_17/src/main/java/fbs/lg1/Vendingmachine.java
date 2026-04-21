package fbs.lg1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

import fbs.lg1.enums.Coins.MuenzTyp;
import fbs.lg1.enums.Items.Item;

public class Vendingmachine {

    protected Vendingmachine() {
        initializeInventory();
        initializeCoins();
    }

    protected EnumMap<Item, Integer> inventory = new EnumMap<>(Item.class);
    protected EnumMap<MuenzTyp, Integer> coinBox = new EnumMap<>(MuenzTyp.class);
    protected EnumMap<MuenzTyp, Integer> virtualCoinBox = new EnumMap<>(MuenzTyp.class);

    private void initializeInventory() {
        for (Item item : Item.values()) {
            inventory.put(item, 0);
        }
    }

    private void initializeCoins() {
        for (MuenzTyp coin : MuenzTyp.values()) {
            coinBox.put(coin, 0);
        }
    }

    public String formatToEuro(int cents) {
        return String.format("%.2f €", cents / 100.0);
    }

    public Collection<Integer> getCoinValues() {
        return coinBox.values();
    }

    public Collection<MuenzTyp> getCoindKeys() {
        return coinBox.keySet();
    }

    public void printCoins() {
        for (MuenzTyp typ : getCoindKeys()) {
            Integer anzahl = coinBox.get(typ);
            System.out.println(typ + ": " + anzahl + " Stück");
        }
    }

    public Collection<Integer> getItemValues() {
        return inventory.values();
    }

    public Collection<Item> getItemKeys() {
        return inventory.keySet();
    }

    public void printItem() {
        for (Item typ : getItemKeys()) {
            Integer anzahl = inventory.get(typ);
            System.out.println(typ + ": " + anzahl + "Stück");
        }
    }

    public void printasIteamlist() {
        for (java.util.Map.Entry<Item, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " Stück");
        }
    }

    public void checkBefuellStant(ArrayList<Item> liste) {
        for (Map.Entry<Item, Integer> entry : inventory.entrySet()) {
            if (entry.getKey().getMindestbestandsmenge() > entry.getValue()) {
                liste.add(entry.getKey());
                System.out.println(entry.getKey() + " muss nachgefült werden!");
            }
        }
    }

    public void addBefuellStamm(ArrayList<Item> liste) {
        int gelieferteItems = 10;
        for (Item item : liste) {
            int aktuellerBestand = inventory.getOrDefault(item, 0);
            inventory.put(item, aktuellerBestand + gelieferteItems);
            System.out.println(item + " wurde aufgefüllt. Neuer Bestand: " + (aktuellerBestand + gelieferteItems));
        }
    }

    public int Cashcollapse() {
        int allcoin = 0;
        for (java.util.Map.Entry<MuenzTyp, Integer> entry : coinBox.entrySet()) {
            int muenzWert = entry.getKey().getcoinworth();
            int anzahl = entry.getValue();
            allcoin += (muenzWert * anzahl);
        }
        return allcoin;
    }

    public int printchashtopay(String valueEnum, Integer amount) {
        try {
            Item item = Item.valueOf(valueEnum);
            Integer amountInv = inventory.get(item);
            if (amountInv == null || amountInv < amount) {
                return 0;
            }
            return item.getVerkaufspreis() * amount;

        } catch (IllegalArgumentException e) {
            return -1;
        }
    }

    public void reduceStock(String name, int menge) {
        Item item = Item.valueOf(name);
        inventory.put(item, inventory.get(item) - menge);
    }

    public int findcoin(int coininput, int coinstotal) {
        MuenzTyp typ = findTyp(coininput);
        if (typ != null) {
            System.out.println("Match gefunden: " + typ.name());
            virtualCoinBox.put(typ, virtualCoinBox.getOrDefault(typ, 0) + 1);
            return coinstotal + coininput;
        }
        return -1;
    }

    private MuenzTyp findTyp(int value) {
        for (MuenzTyp typ : MuenzTyp.values()) {
            if (typ.getcoinworth() == value) {
                return typ;
            }
        }
        return null;
    }

    public void addInput(EnumMap<MuenzTyp, Integer> coins) {
        coins.forEach((k, v) -> this.coinBox.merge(k, v, (old, change) -> Math.max(0, old + change)));
    }


    public void refundInput(EnumMap<MuenzTyp, Integer> coins) {
        coins.forEach((k, v) -> this.coinBox.merge(k, v * (-1), (old, change) -> Math.max(0, old + change)));
    }

    public boolean giveChange(int changeAmount) {
        if (changeAmount == 0)
            return true;

        Map<MuenzTyp, Integer> toGiveBack = new LinkedHashMap<>();
        int remaining = changeAmount;
        MuenzTyp[] typen = MuenzTyp.values();

        for (int i = 0; i < typen.length; i++) {
            int anzahl = Math.min(remaining / typen[i].getcoinworth(), coinBox.getOrDefault(typen[i], 0));
            if (anzahl > 0) {
                toGiveBack.put(typen[i], anzahl);
                remaining -= anzahl * typen[i].getcoinworth();
            }
        }

        if (remaining == 0) {
            for (Map.Entry<MuenzTyp, Integer> entry : toGiveBack.entrySet()) {
                coinBox.put(entry.getKey(), coinBox.get(entry.getKey()) - entry.getValue());
                System.out.println(entry.getValue() + "x " + entry.getKey().name() + " zurück.");
            }
            return true;
        }
        return false;
    }
}