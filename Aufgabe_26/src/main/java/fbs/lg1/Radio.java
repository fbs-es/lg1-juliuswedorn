package fbs.lg1;

import fbs.lg1.Enums.MusicGenre;
import fbs.lg1.Services.Categorys.AbstractCategory;
import fbs.lg1.Services.Categorys.AdCategory;
import fbs.lg1.Services.Categorys.MusicCategory;
import fbs.lg1.Services.Categorys.NewsCategory;
import fbs.lg1.Services.Categorys.PresenterCategory;
import fbs.lg1.Services.Moderator;
import fbs.lg1.Services.RadioBroadcast;
import fbs.lg1.Services.RadioProgram;
import fbs.lg1.Services.RadioStation;
import fbs.lg1.UI.Ui;
import fbs.lg1.UI.UiElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static fbs.lg1.UI.UiElement.Step.step;

public class Radio {
    private final RadioStation station;
    private RadioProgram currentProgram;
    private RadioBroadcast currentBroadcast;
    private final Ui ui = new Ui();

    public Radio() {
        this.station = new RadioStation("Cloud-FM", 1045, null);
    }

    public RadioStation getStation() {
        return station;
    }

    public RadioBroadcast getBroadcast() {
        if (currentBroadcast == null && !station.getPrograms().isEmpty()) {
            currentProgram = station.getPrograms().get(0);
            currentBroadcast = currentProgram.getRadioBroadcast();
        }
        return currentBroadcast;
    }

    public void genApp() {
        if (currentProgram == null && !station.getPrograms().isEmpty()) {
            currentProgram = station.getPrograms().get(0);
            currentBroadcast = currentProgram.getRadioBroadcast();
        }

        List<UiElement<?>> mainMenuItems = new ArrayList<>();

        mainMenuItems.add(new UiElement<>("Sender- & Programm-Informationen", "1",
                List.of(new UiElement<>("Senderdetails:", null, step(() -> {
                    long total = currentBroadcast != null ? currentBroadcast.getTotalTime() : 0;
                    long max = currentBroadcast != null ? currentBroadcast.getMaxTime() : 0;
                    long remaining = Math.max(0, max - total);
                    return "Sendername: " + station.getName() +
                            "\nFrequenz: " + station.getFreq() + " MHz" +
                            "\nAktuelles Programm: " + (currentProgram != null ? currentProgram.getName() : "Kein Programm") +
                            "\nAktuelle Sendung: " + (currentBroadcast != null ? currentBroadcast.GetName() : "Keine") +
                            "\nVerplante Zeit: " + total + "s / " + max + "s" +
                            "\nVerbleibende Restzeit: " + remaining + "s";
                })))
        ));

        mainMenuItems.add(Ui.buildDynamicMenu(
                "Programme verwalten",
                "2",
                station::getPrograms,
                (prog, pIndex) -> createProgramMenu(prog, pIndex)
        ));

        List<Function<String, ?>> newProgramParsers = List.of(
                s -> s,
                s -> s,
                s -> s,
                s -> safeLong(s, 3600L)
        );

        mainMenuItems.add(new UiElement<>("Neues Programm & Sendung erstellen", "3",
                List.of(new UiElement<>("Programm- & Sendungsdaten eingeben:", null,
                        step(newProgramParsers, (List<Object> args) -> {
                            String progName = parseString((String) args.get(0), "Neues Programm");
                            String broadcastName = parseString((String) args.get(1), "Spezialsendung");
                            String modName = parseString((String) args.get(2), "Moderator Team");
                            Long maxTime = (Long) args.get(3);

                            RadioBroadcast newBroadcast = new RadioBroadcast(broadcastName, new Moderator(modName), 0, maxTime);
                            RadioProgram newProgram = new RadioProgram(progName, newBroadcast);

                            station.getPrograms().add(newProgram);
                            currentProgram = newProgram;
                            currentBroadcast = newBroadcast;

                            return "Neues Programm '" + progName + "' mit Sendung '" + broadcastName + "' (Max: " + maxTime + "s) erfolgreich erstellt!";
                        })
                ))
        ));

        if (mainMenuItems.isEmpty()) {
            System.out.println("Keine Daten zum Aufbauen der UI vorhanden.");
            return;
        }

        ui.selectValue(mainMenuItems);
    }

    private UiElement<?> createProgramMenu(RadioProgram prog, int index) {
        RadioBroadcast broadcastOfProg = prog.getRadioBroadcast();
        List<UiElement<?>> options = new ArrayList<>();

        options.add(new UiElement<>("Als aktives Programm & Sendung setzen", "1",
                List.of(new UiElement<>("Ergebnis:", null, step(() -> {
                    currentProgram = prog;
                    currentBroadcast = prog.getRadioBroadcast();
                    return "Programm '" + prog.getName() + "' ist jetzt aktiv!";
                })))
        ));

        options.add(new UiElement<>("Programm löschen", "2",
                List.of(new UiElement<>("Ergebnis:", null, step(() -> {
                    if (station.getPrograms().size() <= 1) {
                        return "Fehler: Das letzte Programm kann nicht gelöscht werden.";
                    }
                    station.getPrograms().remove(prog);
                    if (currentProgram == prog) {
                        currentProgram = station.getPrograms().get(0);
                        currentBroadcast = currentProgram.getRadioBroadcast();
                    }
                    return "Programm erfolgreich gelöscht.";
                })))
        ));

        if (broadcastOfProg != null) {
            List<Function<String, ?>> musicParsers = List.of(
                    s -> s,
                    s -> safeLong(s, 180L),
                    s -> s,
                    s -> s,
                    this::parseGenre
            );

            List<Function<String, ?>> adParsers = List.of(
                    s -> s,
                    s -> safeLong(s, 30L),
                    s -> s,
                    s -> safeFloat(s, 1000.0f)
            );

            List<Function<String, ?>> presenterParsers = List.of(
                    s -> s,
                    s -> safeLong(s, 45L),
                    s -> s
            );

            List<Function<String, ?>> newsParsers = List.of(
                    s -> s,
                    s -> safeLong(s, 60L),
                    s -> s,
                    s -> Math.min(5, Math.max(1, (int) safeLong(s, 5L)))
            );

            options.add(new UiElement<>("Radiosendung verwalten (" + broadcastOfProg.GetName() + ")", "3",
                    List.of(
                            Ui.buildDynamicMenu(
                                    "Beitragsliste anzeigen & verwalten",
                                    "1",
                                    broadcastOfProg::getCategorys,
                                    (cat, cIndex) -> createCategoryMenu(broadcastOfProg, cIndex)
                            ),
                            new UiElement<>("Neuen Beitrag hinzufügen", "2",
                                    List.of(
                                            new UiElement<>("Musikbeitrag hinzufügen", "1",
                                                    List.of(new UiElement<>("Eingaben für Musikbeitrag:", null,
                                                            step(musicParsers, (List<Object> args) -> {
                                                                String title = parseString((String) args.get(0), "Neuer Song");
                                                                Long duration = (Long) args.get(1);
                                                                String artist = parseString((String) args.get(2), "Unbekannter Künstler");
                                                                String album = parseString((String) args.get(3), "Unbekanntes Album");
                                                                MusicGenre genre = (MusicGenre) args.get(4);

                                                                MusicCategory music = new MusicCategory(title, duration, 0, artist, album, genre);

                                                                if (broadcastOfProg.getTotalTime() + duration > broadcastOfProg.getMaxTime()) {
                                                                    return "WARNUNG: Hinzufügen abgelehnt! Sendungslimit von " + broadcastOfProg.getMaxTime() + "s würde überschritten werden!";
                                                                }

                                                                broadcastOfProg.addReports(music);
                                                                return "Musikbeitrag '" + title + "' von " + artist + " (" + duration + "s) erfolgreich hinzugefügt!";
                                                            })
                                                    ))
                                            ),
                                            new UiElement<>("Werbebeitrag hinzufügen", "2",
                                                    List.of(new UiElement<>("Eingaben für Werbebeitrag:", null,
                                                            step(adParsers, (List<Object> args) -> {
                                                                String title = parseString((String) args.get(0), "Werbespot");
                                                                Long duration = (Long) args.get(1);
                                                                String customer = parseString((String) args.get(2), "Anonymer Kunde");
                                                                Float budget = (Float) args.get(3);

                                                                AdCategory ad = new AdCategory(title, duration, 0, customer, budget);

                                                                if (broadcastOfProg.getTotalTime() + duration > broadcastOfProg.getMaxTime()) {
                                                                    return "WARNUNG: Hinzufügen abgelehnt! Sendungslimit von " + broadcastOfProg.getMaxTime() + "s würde überschritten werden!";
                                                                }

                                                                broadcastOfProg.addReports(ad);
                                                                return "Werbebeitrag '" + title + "' für " + customer + " (" + duration + "s) erfolgreich hinzugefügt!";
                                                            })
                                                    ))
                                            ),
                                            new UiElement<>("Moderation hinzufügen", "3",
                                                    List.of(new UiElement<>("Eingaben für Moderation:", null,
                                                            step(presenterParsers, (List<Object> args) -> {
                                                                String title = parseString((String) args.get(0), "Moderationsblock");
                                                                Long duration = (Long) args.get(1);
                                                                String text = parseString((String) args.get(2), "Herzlich willkommen beim Radio!");

                                                                PresenterCategory presenter = new PresenterCategory(title, duration, 0, text);

                                                                if (broadcastOfProg.getTotalTime() + duration > broadcastOfProg.getMaxTime()) {
                                                                    return "WARNUNG: Hinzufügen abgelehnt! Sendungslimit von " + broadcastOfProg.getMaxTime() + "s würde überschritten werden!";
                                                                }

                                                                broadcastOfProg.addReports(presenter);
                                                                return "Moderation '" + title + "' (" + duration + "s) erfolgreich hinzugefügt!";
                                                            })
                                                    ))
                                            )
                                    )
                            ),
                            new UiElement<>("Eilmeldung (Breaking News) einfügen", "3",
                                    List.of(new UiElement<>("Eingaben für Eilmeldung:", null,
                                            step(newsParsers, (List<Object> args) -> {
                                                String title = parseString((String) args.get(0), "ZOB Eilmeldung");
                                                Long duration = (Long) args.get(1);
                                                String region = parseString((String) args.get(2), "Inland");
                                                Integer priority = (Integer) args.get(3);

                                                NewsCategory news = new NewsCategory(title, duration, 0, region, priority);
                                                broadcastOfProg.insertBreakingNews(news);

                                                if (broadcastOfProg.getTotalTime() > broadcastOfProg.getMaxTime()) {
                                                    return "ACHTUNG WARNUNG: Durch den spontanen Einschub der Eilmeldung hat die Sendung ihr Zeitlimit von " + broadcastOfProg.getMaxTime() + "s überschritten (Aktuell: " + broadcastOfProg.getTotalTime() + "s)! Bitte Beiträge kürzen.";
                                                }

                                                return "Eilmeldung '" + title + "' (" + duration + "s, Prio " + priority + ") direkt in den Plan geschoben!";
                                            })
                                    ))
                            )
                    )
            ));
        }

        return new UiElement<>(
                () -> (index + 1) + ". " + prog.getName() + " [Sendung: " + (broadcastOfProg != null ? broadcastOfProg.GetName() : "Keine") + "]",
                String.valueOf(index + 1),
                options
        );
    }

    private UiElement<?> createCategoryMenu(RadioBroadcast broadcast, int index) {
        List<Function<String, ?>> splitParsers = List.of(
                s -> s,
                s -> safeLong(s, 90L),
                s -> s
        );

        return new UiElement<>(
                () -> {
                    List<AbstractCategory> cats = broadcast.getCategorys();
                    if (index >= cats.size()) return "Eintrag nicht mehr verfügbar";
                    AbstractCategory cat = cats.get(index);
                    return (index + 1) + ". " + cat.getTitle() + " [Dauer: " + cat.getPlannedTimeSec() + "s | Start: " + cat.getStartTimeSec() + "s]";
                },
                String.valueOf(index + 1),
                List.of(
                        new UiElement<>("Beitrag löschen", "1",
                                List.of(new UiElement<>("Ergebnis:", null, step(() -> {
                                    List<AbstractCategory> cats = broadcast.getCategorys();
                                    if (index < cats.size()) {
                                        AbstractCategory target = cats.get(index);
                                        broadcast.removeReports(target);
                                        return "Beitrag '" + target.getTitle() + "' gelöscht und Startzeiten neu berechnet.";
                                    }
                                    return "Fehler: Beitrag nicht gefunden.";
                                })))
                        ),
                        new UiElement<>("Beitrag kürzen", "2",
                                List.of(new UiElement<>("Neue Dauer in Sekunden eingeben:", null,
                                        step(s -> safeLong(s, 60L), (Long newDuration) -> {
                                            List<AbstractCategory> cats = broadcast.getCategorys();
                                            if (index < cats.size()) {
                                                AbstractCategory target = cats.get(index);
                                                broadcast.updateReportsTime(target, newDuration);
                                                return "Beitrag '" + target.getTitle() + "' auf " + newDuration + " Sekunden gekürzt.";
                                            }
                                            return "Fehler: Beitrag nicht gefunden.";
                                        })
                                ))
                        ),
                        new UiElement<>("Notfall-Splitting durchführen", "3",
                                List.of(new UiElement<>("Eingaben für Notfall-Splitting:", null,
                                        step(splitParsers, (List<Object> args) -> {
                                            String newsTitle = parseString((String) args.get(0), "Spezial-ZOB");
                                            Long newsDuration = (Long) args.get(1);
                                            String region = parseString((String) args.get(2), "Regional");

                                            List<AbstractCategory> cats = broadcast.getCategorys();
                                            if (index < cats.size()) {
                                                AbstractCategory target = cats.get(index);
                                                long splitPoint = target.getPlannedTimeSec() / 2;
                                                NewsCategory emergencyNews = new NewsCategory(newsTitle, newsDuration, 0, region, 4);
                                                broadcast.splitReports(target, emergencyNews, splitPoint);
                                                return "Beitrag gesplittet und ZOB-Eilmeldung '" + newsTitle + "' (" + newsDuration + "s) eingefügt!";
                                            }
                                            return "Fehler: Beitrag nicht gefunden.";
                                        })
                                ))
                        )
                )
        );
    }

    private String parseString(String s, String fallback) {
        if (s != null && !s.trim().isEmpty()) {
            return s.trim();
        }
        return fallback;
    }

    private MusicGenre parseGenre(String input) {
        if (input == null) return MusicGenre.Pop;
        String normalized = input.trim().toLowerCase();
        if (normalized.contains("rock")) return MusicGenre.Rock;
        if (normalized.contains("rap")) return MusicGenre.Rap;
        if (normalized.contains("jazz")) return MusicGenre.Jazz;
        return MusicGenre.Pop;
    }

    private long safeLong(String s, long fallback) {
        try {
            return Long.parseLong(s.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    private float safeFloat(String s, float fallback) {
        try {
            return Float.parseFloat(s.trim());
        } catch (Exception e) {
            return fallback;
        }
    }
}