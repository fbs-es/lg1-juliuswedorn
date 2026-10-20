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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Aufgabe26Test {

    private Radio radio;
    private RadioStation station;
    private RadioProgram program;
    private RadioBroadcast broadcast;
    private Moderator moderator;
    private MusicCategory musicCategory;
    private AdCategory adCategory;
    private NewsCategory newsCategory;
    private PresenterCategory presenterCategory;

    @BeforeEach
    void setUp() {
        Main.newApp();
        radio = Main.getApp();

        station = radio.getStation();
        moderator = new Moderator("DJ Schnarchnase");
        broadcast = new RadioBroadcast("Morgenshow", moderator, 0, 3600);
        program = new RadioProgram("Frühstück in der antarktis", broadcast);

        if (station.getPrograms().isEmpty()) {
            station.addProgram(program);
        }

        musicCategory = new MusicCategory("Crazy Hamster in da Hood", 180, 0, "DJ Flauschball", "Paws & Beats", MusicGenre.Rap);
        adCategory = new AdCategory("Sprunk", 30, 0, "Sprunk Corp.", 9999.0f);
        newsCategory = new NewsCategory("ZOB Eilmeldung", 60, 0, "Inland", 5);
        presenterCategory = new PresenterCategory("Live-Talk", 45, 0, "Sprechertext hier!? ömm ja  also eeee");

        broadcast.addReports(musicCategory);
        broadcast.addReports(adCategory);
    }

    @Test
    @DisplayName("Radiosender speichert Namen und Frequenz korrekt")
    void checkRadioStationMetadata() {
        assertThat(station.getName()).isEqualTo("Cloud-FM");
        assertThat(station.getFreq()).isEqualTo(1045);
        assertThat(station.getPrograms()).isNotEmpty();
    }

    @Test
    @DisplayName("Radioprogramm & Radiosendung (Zeitblock, Moderator, Maximaldauer) werden korrekt verwaltet")
    void checkProgramAndBroadcastInit() {
        assertThat(broadcast.GetName()).isEqualTo("Morgenshow");
        assertThat(broadcast.getMaxTime()).isEqualTo(3600);
        assertThat(broadcast.getStartTime()).isEqualTo(0);
        assertThat(broadcast.getRestTime(1000)).isEqualTo(2600);
    }

    @Test
    @DisplayName("Musikbeitrag speichert Titel, Dauer, Künstler, Album und Genre fehlerfrei")
    void checkMusicCategorySpecifics() {
        assertThat(musicCategory.getTitle()).isEqualTo("Crazy Hamster in da Hood");
        assertThat(musicCategory.getPlannedTimeSec()).isEqualTo(180);
        MusicCategory rock = new MusicCategory("Rock", 100, 0, "Artist", "Album", MusicGenre.Rock);
        MusicCategory rap = new MusicCategory("Rap", 100, 0, "Artist", "Album", MusicGenre.Rap);
        MusicCategory jazz = new MusicCategory("Jazz", 100, 0, "Artist", "Album", MusicGenre.Jazz);
        assertThat(rock.getTitle()).isEqualTo("Rock");
        assertThat(rap.getTitle()).isEqualTo("Rap");
        assertThat(jazz.getTitle()).isEqualTo("Jazz");
    }

    @Test
    @DisplayName("Werbebeitrag erfasst Kundenname und Werbebudget korrekt")
    void checkAdCategorySpecifics() {
        assertThat(adCategory.getTitle()).isEqualTo("Sprunk");
        assertThat(adCategory.getPlannedTimeSec()).isEqualTo(30);
    }

    @Test
    @DisplayName("Moderation enthält den konkreten Sprechertext")
    void checkPresenterCategorySpecifics() {
        assertThat(presenterCategory.getTitle()).isEqualTo("Live-Talk");
        assertThat(presenterCategory.getPlannedTimeSec()).isEqualTo(45);
    }

    @Test
    @DisplayName("Eilmeldung speichert Dringlichkeitsstufe (1-5) und Region")
    void checkNewsCategorySpecifics() {
        assertThat(newsCategory.getTitle()).isEqualTo("ZOB Eilmeldung");
        assertThat(newsCategory.getPlannedTimeSec()).isEqualTo(60);
    }

    @Test
    @DisplayName("Gesamtzeit und Restzeit werden präzise berechnet")
    void checkTotalTimeCalculation() {
        assertThat(broadcast.getTotalTime()).isEqualTo(210);
        assertThat(broadcast.getRestTime(broadcast.getTotalTime())).isEqualTo(3390);
    }

    @Test
    @DisplayName("Das Hinzufügen von Beiträgen wird bei Überschreitung der Maximaldauer abgelehnt")
    void reportExceedingMaxTimeIsRejected() {
        RadioBroadcast shortShow = new RadioBroadcast("Short", moderator, 0, 100);
        MusicCategory bigSong = new MusicCategory("Epic", 150, 0, "Artist", "Album", MusicGenre.Rock);

        shortShow.addReports(bigSong);
        assertThat(shortShow.getCategorys()).doesNotContain(bigSong);
        assertThat(shortShow.getTotalTime()).isEqualTo(0);
    }

    @Test
    @DisplayName("Startzeitpunkte errechnen sich automatisch aus der Summe der vorherigen Beiträge")
    void automaticStartTimesCalculation() {
        assertThat(musicCategory.getStartTimeSec()).isEqualTo(0);
        assertThat(adCategory.getStartTimeSec()).isEqualTo(180);

        broadcast.addReports(presenterCategory);
        assertThat(presenterCategory.getStartTimeSec()).isEqualTo(210);
    }

    @Test
    @DisplayName("Eilmeldung schiebt sich direkt vor den aktuellen Beitrag und verschiebt nachfolgende")
    void breakingNewsInsertsAtTopAndShiftsOthers() {
        assertThat(broadcast.getCategorys().get(0)).isEqualTo(musicCategory);
        assertThat(musicCategory.getStartTimeSec()).isEqualTo(0);

        broadcast.insertBreakingNews(newsCategory);

        assertThat(broadcast.getCategorys().get(0)).isEqualTo(newsCategory);
        assertThat(newsCategory.getStartTimeSec()).isEqualTo(0);
        assertThat(musicCategory.getStartTimeSec()).isEqualTo(60);
        assertThat(adCategory.getStartTimeSec()).isEqualTo(240);
    }

    @Test
    @DisplayName("Beiträge können im Sendeplan nachträglich gekürzt oder komplett gelöscht werden")
    void removingAndUpdatingReportsWorks() {
        broadcast.updateReportsTime(musicCategory, 90);
        assertThat(musicCategory.getPlannedTimeSec()).isEqualTo(90);
        assertThat(broadcast.getTotalTime()).isEqualTo(120);

        broadcast.removeReports(musicCategory);
        assertThat(broadcast.getCategorys()).doesNotContain(musicCategory);
        assertThat(adCategory.getStartTimeSec()).isEqualTo(0);
    }

    @Test
    @DisplayName("Notfall-Splitting unterbricht einen laufenden Beitrag und platziert die Eilmeldung dazwischen")
    void emergencySplittingDividesReport() {
        MusicCategory targetSong = new MusicCategory("Target", 200, 0, "A", "B", MusicGenre.Pop);
        RadioBroadcast splitShow = new RadioBroadcast("Split", moderator, 0, 3600);
        splitShow.addReports(targetSong);

        splitShow.splitReports(targetSong, newsCategory, 100);

        assertThat(targetSong.getPlannedTimeSec()).isEqualTo(100);
        assertThat(splitShow.getCategorys()).hasSize(3);
        assertThat(splitShow.getCategorys().get(1)).isEqualTo(newsCategory);
    }
}