package fbs.lg1;

import fbs.lg1.Enums.MusicGenre;
import fbs.lg1.Services.Categorys.AdCategory;
import fbs.lg1.Services.Categorys.MusicCategory;
import fbs.lg1.Services.Categorys.NewsCategory;
import fbs.lg1.Services.Categorys.PresenterCategory;
import fbs.lg1.Services.Moderator;
import fbs.lg1.Services.RadioBroadcast;
import fbs.lg1.Services.RadioProgram;
import fbs.lg1.Services.RadioStation;

public class Main {
    private static Radio radio;

    public static void main(String[] args) {
        startApp();
    }

    public static void startApp() {
        newApp();

        RadioStation station = radio.getStation();

        RadioBroadcast broadcast1 = new RadioBroadcast("Morgen-Chaos mit Kratky-Fake", new Moderator("Robert Kratzkopf"), 0, 3600);
        broadcast1.addReports(new MusicCategory("Der Wecker tickt falsch", 180, 0, "Die Sound-Knödel", "Frühstücks-Beat", MusicGenre.Pop));
        broadcast1.addReports(new NewsCategory("ZOB - Zeit Ohne Durchblick", 120, 0, "Österreich", 2));
        broadcast1.addReports(new AdCategory("McDampf Pommes-Shake", 30, 0, "McDampf Inc.", 1500.0f));

        RadioProgram program1 = new RadioProgram("Ö66 Wecker-Programm", broadcast1);
        station.getPrograms().add(program1);

        RadioBroadcast broadcast2 = new RadioBroadcast("Spät-Schicht Extrem", new Moderator("DJ Sockenbrand"), 0, 7200);
        broadcast2.addReports(new PresenterCategory("Anrufer-Lounge", 60, 0, "Warum haben Pinguine keine Ohren?"));
        broadcast2.addReports(new MusicCategory("Oma deine oma geht mit dem flow ja", 240, 0, "DJ Flauschball", "Bass-Gewitter", MusicGenre.Rap));
        broadcast2.addReports(new AdCategory("RotBulle Verleiht Flossen", 45, 0, "RotBulle GmbH", 4500.0f));

        RadioProgram program2 = new RadioProgram("KnorpelHit Spätprogramm", broadcast2);
        station.getPrograms().add(program2);

        radio.genApp();
    }

    public static void newApp() {
        radio = new Radio();
    }

    public static Radio getApp() {
        return radio;
    }
}