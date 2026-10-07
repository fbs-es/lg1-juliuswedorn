package fbs.lg1;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private String email;
    private List<Borrow> borrowings;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.borrowings = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Borrow> getBorrowings() {
        return borrowings;
    }

    public void addBorrowing(Borrow borrow) {
        this.borrowings.add(borrow);
    }
}