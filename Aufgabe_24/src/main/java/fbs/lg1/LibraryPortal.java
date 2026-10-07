package fbs.lg1;

import fbs.lg1.UI.Ui;
import fbs.lg1.UI.UiElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static fbs.lg1.UI.UiElement.Step.step;

public class LibraryPortal {
    private final Library library = new Library("Städtische Bibliothek", "Bibliotheksplatz 1");
    private final Ui ui = new Ui();

    public Library getLibrary() {
        return library;
    }

    public void genApp() {
        List<UiElement<?>> mainMenuItems = new ArrayList<>();

        List<UiElement<?>> accountMenuItems = Ui.buildMenu(library.getUsers(), this::createUserMenu);
        if (!accountMenuItems.isEmpty()) {
            mainMenuItems.add(new UiElement<>("Registrierte Nutzer & Konten", "1", accountMenuItems));
        }

        mainMenuItems.add(Ui.<Book>buildDynamicMenu(
                "Gesamter Medienbestand (Bücher)",
                "2",
                library::getBooks,
                this::createBookMenu
        ));

        mainMenuItems.add(new UiElement<>("Bibliotheksverwaltung", "3",
                List.of(
                        new UiElement<>("Neues Buch hinzufügen", "1",
                                List.of(new UiElement<>("Status Bucherfassung:", null, step(() -> {
                                    int nextId = library.getBooks().size() + 1;
                                    Book newBook = new Book("978-3-00-00" + nextId, "Neues Buch #" + nextId, "Autor " + nextId);
                                    library.addBook(newBook);
                                    return "Neues Buch erfolgreich zum Bestand hinzugefügt:\n'" + newBook.getTitle() + "' (ISBN: " + newBook.getIsbn() + ")";
                                })))
                        ),
                        new UiElement<>("Neuen Nutzer registrieren", "2",
                                List.of(new UiElement<>("Status Registrierung:", null, step(() -> {
                                    int nextId = library.getUsers().size() + 1;
                                    User newUser = new User(nextId, "Nutzer " + nextId, "user" + nextId + "@bibliothek.de");
                                    library.addUser(newUser);
                                    return "Neuer Nutzer erfolgreich registriert:\n" + newUser.getName() + " (ID: " + newUser.getId() + ")";
                                })))
                        )
                )
        ));

        if (mainMenuItems.isEmpty()) {
            System.out.println("Keine Daten zum Aufbauen der UI vorhanden.");
            return;
        }

        ui.selectValue(mainMenuItems);
    }

    private UiElement<?> createUserMenu(User user, int index) {
        return new UiElement<>(
                () -> user.getName() + " (Nutzer-ID: " + user.getId() + ")",
                String.valueOf(index + 1),
                List.of(
                        new UiElement<>("Profil-Informationen anzeigen", "1",
                                List.of(new UiElement<>("Nutzerdetails:", null, step(() ->
                                        "Nutzer-ID: " + user.getId() +
                                                "\nName: " + user.getName() +
                                                "\nE-Mail: " + user.getEmail() +
                                                "\nAnzahl Ausleihen: " + user.getBorrowings().size()
                                )))
                        ),
                        Ui.<Book>buildDynamicMenu(
                                "Buch ausleihen (Verfügbare Medien)",
                                "2",
                                () -> library.getBooks().stream().filter(Book::isOnHand).toList(),
                                (book, bIndex) -> createBorrowActionMenu(user, book, bIndex)
                        ),
                        Ui.<Borrow>buildDynamicMenu(
                                "Meine Ausleihen (Verlängern & Strafe prüfen)",
                                "3",
                                user::getBorrowings,
                                (borrow, brIndex) -> createBorrowMenu(user, borrow, brIndex)
                        )
                )
        );
    }

    private UiElement<?> createBorrowActionMenu(User user, Book book, int index) {
        return new UiElement<>(
                () -> book.getTitle() + " [ISBN: " + book.getIsbn() + " | Autor: " + book.getAuthor() + "]",
                String.valueOf(index + 1),
                List.of(
                        new UiElement<>("Ausleihprozess starten", "1",
                                List.of(new UiElement<>("Ergebnis Ausleihe:", null, step(() -> {
                                    long now = System.currentTimeMillis();
                                    long fourteenDays = 14L * 24 * 60 * 60 * 1000;
                                    Borrow borrow = library.startBorrowing(user, book, now, fourteenDays);

                                    if (borrow != null) {
                                        return "Ausleihe erfolgreich gestartet!" +
                                                "\nBuch: " + book.getTitle() +
                                                "\nGeplantes Rückgabedatum: " + new Date(borrow.getEndDate());
                                    }
                                    return "Ausleihe fehlgeschlagen. Buch ist zurzeit nicht verfügbar.";
                                })))
                        )
                )
        );
    }

    private UiElement<?> createBookMenu(Book book, int index) {
        return new UiElement<>(
                () -> book.getTitle() + " | Autor: " + book.getAuthor() + " | Status: " + (book.isOnHand() ? "Verfügbar" : "Ausgeliehen"),
                String.valueOf(index + 1),
                List.of(
                        new UiElement<>("Buchdetails anzeigen", "1",
                                List.of(new UiElement<>("Details:", null, step(() ->
                                        "ISBN: " + book.getIsbn() +
                                                "\nTitel: " + book.getTitle() +
                                                "\nAutor: " + book.getAuthor() +
                                                "\nVerfügbarkeit: " + (book.isOnHand() ? "Im Bestand" : "Ausgeliehen")
                                )))
                        )
                )
        );
    }

    private UiElement<?> createBorrowMenu(User user, Borrow borrow, int index) {
        return new UiElement<>(
                () -> "Ausleihe #" + (index + 1) + " [" + borrow.getBook().getTitle() + "] - Status: " + borrow.getStatus(),
                String.valueOf(index + 1),
                List.of(
                        new UiElement<>("Ausleihdetails anzeigen", "1",
                                List.of(new UiElement<>("Details:", null, step(() -> formatBorrowDetails(borrow))))
                        ),
                        new UiElement<>("Leihfrist einmalig verlängern", "2",
                                List.of(new UiElement<>("Verlängerungsstatus:", null, step(() -> {
                                    long fourteenDays = 14L * 24 * 60 * 60 * 1000;
                                    boolean success = borrow.extendLoan(fourteenDays);
                                    if (success) {
                                        return "Leihfrist erfolgreich um 14 Tage verlängert!\nNeues Rückgabedatum: " + new Date(borrow.getEndDate());
                                    }
                                    return "Verlängerung fehlgeschlagen. (Bereits einmalig verlängert oder Ausleihe beendet).";
                                })))
                        ),
                        new UiElement<>("Verspätungsstrafe berechnen", "3",
                                List.of(new UiElement<>("Strafberechnung:", null, step(() -> {
                                    long currentTime = System.currentTimeMillis();
                                    double penalty = borrow.computePenalty(currentTime);
                                    return "Aktueller Status: " + borrow.getStatus() +
                                            "\nBerechnete Verspätungsstrafe: " + penalty + " €";
                                })))
                        )
                )
        );
    }

    private String formatBorrowDetails(Borrow borrow) {
        StringBuilder sb = new StringBuilder();
        sb.append("Buch: ").append(borrow.getBook().getTitle())
                .append(" (ISBN: ").append(borrow.getBook().getIsbn()).append(")")
                .append("\nNutzer: ").append(borrow.getUser().getName())
                .append("\nAusleihdatum: ").append(new Date(borrow.getStartDate()))
                .append("\nGeplantes Rückgabedatum: ").append(new Date(borrow.getEndDate()))
                .append("\nStatus: ").append(borrow.getStatus())
                .append("\nEinmalig verlängert: ").append(borrow.isExtended() ? "Ja" : "Nein")
                .append("\nVerspätungsstrafe: ").append(borrow.getPenalty()).append(" €");
        return sb.toString();
    }
}