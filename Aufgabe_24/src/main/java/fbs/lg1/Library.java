package fbs.lg1;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private String name;
    private String address;
    private List<Book> books;
    private List<User> users;

    public Library(String name, String address) {
        this.name = name;
        this.address = address;
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public void addBook(Book book) {
        this.books.add(book);
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public Borrow startBorrowing(User user, Book book, long startDate, long durationMillis) {
        if (!book.isOnHand()) {
            System.out.println("Das Buch '" + book.getTitle() + "' ist derzeit vergriffen.");
            return null;
        }

        book.setOnHand(false);
        Borrow borrow = new Borrow(user, book, startDate, startDate + durationMillis);
        user.addBorrowing(borrow);
        return borrow;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public List<Book> getBooks() { return books; }
    public List<User> getUsers() { return users; }
}