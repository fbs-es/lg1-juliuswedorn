package fbs.lg1;

public class Spielvariante_2 {

    public int u = 1;
    public int o = 100;
    private int letzterTipp1;
    private int ratenzahle = 50;
    private double divider = 25;

    public int Guess1() {
        letzterTipp1 = Utility.Random(u, o);
        return letzterTipp1;
    }

    public int Guess4(int ratenzahle) {
        return ratenzahle;
    }

    public void Guess2(String feedback) {
        if (feedback.contains("viel größer")) {
            u = letzterTipp1 + 6;
        } else if (feedback.contains("etwas größer")) {
            u = letzterTipp1 + 1;
            o = letzterTipp1 + 5;
        } else if (feedback.contains("viel kleiner")) {
            o = letzterTipp1 - 6;
        } else if (feedback.contains("etwas kleiner")) {
            o = letzterTipp1 - 1;
            u = letzterTipp1 - 5;
        }
    }

    // ICH HABE GEFÜLT FÜR 2 STUNDEN GEBUCKFIXET FÜR EINE MATH.MAX
    public int Guess3(String feedback) {
        if (feedback.contains("viel größer")) {
            ratenzahle += Math.max(1, (int) Math.round(divider));
        } else if (feedback.contains("etwas größer")) {
            ratenzahle += Math.max(1, (int) Math.round(divider / 4));
        } else if (feedback.contains("viel kleiner")) {
            ratenzahle -= Math.max(1, (int) Math.round(divider));
        } else if (feedback.contains("etwas kleiner")) {
            ratenzahle -= Math.max(1, (int) Math.round(divider / 4));
        }

        divider /= 2;
        return ratenzahle;
    }
}