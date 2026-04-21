package fbs.lg1.enums.Coins;

public enum MuenzTyp {
    ZWEI_EURO(200, "2 Euro"),
    EIN_EURO(100, "1 Euro"),
    FUENFZIG_CENT(50, "50 Cent"),
    ZWANZIG_CENT(20, "20 Cent"),
    ZEHN_CENT(10, "10 Cent"),
    FUENF_CENT(5, "5 Cent"),
    ZWEI_CENT(2, "2 Cent"),
    EIN_CENT(1, "1 Cent");

    private final int wertInCent;
    private final String beschreibung;

    MuenzTyp(int wertInCent, String beschreibung) {
        this.wertInCent = wertInCent;
        this.beschreibung = beschreibung;
    }

    public int getcoinworth() {
        return wertInCent;
    }
}
