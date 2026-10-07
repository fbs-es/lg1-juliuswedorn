package fbs.lg1;

public class Main {
    private static LibraryPortal portal;

    public static void main(String[] args) {
        startApp();
    }

    public static void startApp() {
        newApp();

        portal.getLibrary().addBook(new Book("978-3-16-148410-0", "Der Herr der Ringe", "J.R.R. Tolkien"));
        portal.getLibrary().addBook(new Book("978-3-83-627282-7", "Java ist auch eine Insel", "Christian Ullenboom"));
        portal.getLibrary().addBook(new Book("978-0-13-235088-4", "Clean Code", "Robert C. Martin"));

        portal.getLibrary().addUser(new User(1, "Max Mustermann", "max@example.com"));
        portal.getLibrary().addUser(new User(2, "Anna Schmidt", "anna@example.com"));

        portal.genApp();
    }

    public static void newApp() {
        portal = new LibraryPortal();
    }

    public static LibraryPortal getApp() {
        return portal;
    }
}