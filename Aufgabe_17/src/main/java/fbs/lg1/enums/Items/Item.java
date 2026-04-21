package fbs.lg1.enums.Items;

public enum Item {
    WATER("Spritziges Mineralwasser", 180, 10),
    COFFEE("Heißer Kaffee aus der Antarktis", 300, 5),
    COLA("Koffeinhaltige Limonade", 150, 12),
    MATE("Wachmacher aus Südamerika", 220, 8),
    TEA("Beruhigender Kräutertee", 120, 5),
    VODKA("FlowBerry Fizz", 5000, 1);

    private final String information;
    private final int verkaufspreis;
    private final int mindestbestandsmenge;

    Item(String information, int verkaufspreis, int mindestbestandsmenge) {
        this.information = information;
        this.verkaufspreis = verkaufspreis;
        this.mindestbestandsmenge = mindestbestandsmenge;
    }

    public int getMindestbestandsmenge() {
        return mindestbestandsmenge;
    }

    public int getVerkaufspreis() {
        return verkaufspreis;
    }
}
