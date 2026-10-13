package fbs.lg1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Aufgabe24Test {

    private static final long DAY = 24L * 60 * 60 * 1000;

    private LibraryPortal LP;
    private Library library;
    private Book book;
    private User user;

    @BeforeEach
    void setUp() {
        Main.newApp();
        LP = Main.getApp();
        library = LP.getLibrary();

        book = new Book("978-3-16-148410-0", "Der Herr der Ringe", "J.R.R. Tolkien");
        library.addBook(book);

        user = new User(1, "Max Mustermann", "max@example.com");
        library.addUser(user);
    }

    @Test
    @DisplayName("Should correctly initialize Library via LibraryPortal")
    void testLibraryConstructor() throws Exception {
        assertThat((String) getFieldValue("name", library)).isEqualTo("Städtische Bibliothek");
        assertThat((String) getFieldValue("address", library)).isEqualTo("Bibliotheksplatz 1");
        assertThat(library.getName()).isEqualTo("Städtische Bibliothek");
        assertThat(library.getAddress()).isEqualTo("Bibliotheksplatz 1");
    }

    @Test
    @DisplayName("Should start with empty books and users after newApp")
    void testNewAppIsEmpty() {
        Main.newApp();
        Library fresh = Main.getApp().getLibrary();

        assertThat(fresh.getBooks()).isNotNull().isEmpty();
        assertThat(fresh.getUsers()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should correctly initialize Book with constructor")
    void testBookConstructor() throws Exception {
        Book b = new Book("978-0-13-235088-4", "Clean Code", "Robert C. Martin");

        assertThat((String) getFieldValue("isbn", b)).isEqualTo("978-0-13-235088-4");
        assertThat((String) getFieldValue("title", b)).isEqualTo("Clean Code");
        assertThat((String) getFieldValue("author", b)).isEqualTo("Robert C. Martin");
        assertThat((boolean) getFieldValue("onHand", b)).isTrue();
    }

    @Test
    @DisplayName("Should correctly return values from Book getters and setter")
    void testBookGettersAndSetter() {
        assertThat(book.getIsbn()).isEqualTo("978-3-16-148410-0");
        assertThat(book.getTitle()).isEqualTo("Der Herr der Ringe");
        assertThat(book.getAuthor()).isEqualTo("J.R.R. Tolkien");
        assertThat(book.isOnHand()).isTrue();

        book.setOnHand(false);

        assertThat(book.isOnHand()).isFalse();
    }

    @Test
    @DisplayName("Should correctly initialize User with constructor")
    void testUserConstructor() throws Exception {
        User u = new User(2, "Anna Schmidt", "anna@example.com");

        assertThat((int) getFieldValue("id", u)).isEqualTo(2);
        assertThat((String) getFieldValue("name", u)).isEqualTo("Anna Schmidt");
        assertThat((String) getFieldValue("email", u)).isEqualTo("anna@example.com");
        assertThat(u.getBorrowings()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should correctly return values from User getters")
    void testUserGetters() {
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getName()).isEqualTo("Max Mustermann");
        assertThat(user.getEmail()).isEqualTo("max@example.com");
        assertThat(user.getBorrowings()).isNotNull();
    }

    @Test
    @DisplayName("Should successfully start borrowing when book is on hand")
    void testStartBorrowingSuccess() throws Exception {
        Borrow borrow = library.startBorrowing(user, book, 1000L, 14 * DAY);

        assertThat(borrow).isNotNull();
        assertThat(book.isOnHand()).isFalse();
        assertThat(user.getBorrowings()).hasSize(1).containsExactly(borrow);

        assertThat((long) getFieldValue("startDate", borrow)).isEqualTo(1000L);
        assertThat((long) getFieldValue("endDate", borrow)).isEqualTo(1000L + 14 * DAY);
        assertThat(borrow.getStatus()).isEqualTo(borrowStatus.Active);
        assertThat(borrow.isExtended()).isFalse();
        assertThat(borrow.getPenalty()).isEqualTo(0.0);
        assertThat(borrow.getBook()).isEqualTo(book);
        assertThat(borrow.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Should return null when borrowing a book that is not on hand")
    void testStartBorrowingNotAvailable() throws Exception {
        setFieldValue("onHand", book, false);

        Borrow borrow = library.startBorrowing(user, book, 0L, 14 * DAY);

        assertThat(borrow).isNull();
        assertThat(user.getBorrowings()).isEmpty();
    }

    @Test
    @DisplayName("Should not allow the same book to be borrowed twice")
    void testStartBorrowingTwice() {
        User anna = new User(2, "Anna Schmidt", "anna@example.com");
        library.addUser(anna);

        Borrow first = library.startBorrowing(user, book, 0L, 14 * DAY);
        Borrow second = library.startBorrowing(anna, book, 0L, 14 * DAY);

        assertThat(first).isNotNull();
        assertThat(second).isNull();
        assertThat(anna.getBorrowings()).isEmpty();
    }

    @Test
    @DisplayName("Should extend loan exactly once")
    void testExtendLoan() throws Exception {
        Borrow borrow = library.startBorrowing(user, book, 0L, 14 * DAY);

        boolean first = borrow.extendLoan(14 * DAY);
        boolean second = borrow.extendLoan(14 * DAY);

        assertThat(first).isTrue();
        assertThat(second).isFalse();
        assertThat((long) getFieldValue("endDate", borrow)).isEqualTo(28 * DAY);
        assertThat((boolean) getFieldValue("extended", borrow)).isTrue();
    }

    @Test
    @DisplayName("Should fail to extend loan when borrow is already returned")
    void testExtendLoanReturnedFails() {
        Borrow borrow = library.startBorrowing(user, book, 0L, 14 * DAY);
        borrow.setStatus(borrowStatus.Returned);

        boolean extended = borrow.extendLoan(14 * DAY);

        assertThat(extended).isFalse();
        assertThat(borrow.getEndDate()).isEqualTo(14 * DAY);
        assertThat(borrow.isExtended()).isFalse();
    }

    @Test
    @DisplayName("Should fail to extend loan when borrow is overdue")
    void testExtendLoanOverdueFails() {
        Borrow borrow = library.startBorrowing(user, book, 0L, 14 * DAY);
        borrow.computePenalty(20 * DAY);

        boolean extended = borrow.extendLoan(14 * DAY);

        assertThat(borrow.getStatus()).isEqualTo(borrowStatus.Overdue);
        assertThat(extended).isFalse();
    }

    @Test
    @DisplayName("Should not compute penalty before or exactly at end date")
    void testComputePenaltyNotOverdue() {
        Borrow borrow = library.startBorrowing(user, book, 0L, 14 * DAY);

        assertThat(borrow.computePenalty(10 * DAY)).isEqualTo(0.0);
        assertThat(borrow.computePenalty(14 * DAY)).isEqualTo(0.0);
        assertThat(borrow.getStatus()).isEqualTo(borrowStatus.Active);
    }

    @Test
    @DisplayName("Should charge one day penalty when overdue by less than a day")
    void testComputePenaltyLessThanOneDay() {
        Borrow borrow = library.startBorrowing(user, book, 0L, 14 * DAY);

        double penalty = borrow.computePenalty(14 * DAY + 1000);

        assertThat(penalty).isEqualTo(1.50);
        assertThat(borrow.getStatus()).isEqualTo(borrowStatus.Overdue);
    }

    @Test
    @DisplayName("Should compute penalty per overdue day")
    void testComputePenaltyMultipleDays() throws Exception {
        Borrow borrow = library.startBorrowing(user, book, 0L, 14 * DAY);

        double penalty = borrow.computePenalty(14 * DAY + 3 * DAY);

        assertThat(penalty).isEqualTo(4.50);
        assertThat((double) getFieldValue("penalty", borrow)).isEqualTo(4.50);
        assertThat(borrow.getStatus()).isEqualTo(borrowStatus.Overdue);
    }

    @Test
    @DisplayName("Should not compute penalty for returned borrow")
    void testComputePenaltyReturned() {
        Borrow borrow = library.startBorrowing(user, book, 0L, 14 * DAY);
        borrow.setStatus(borrowStatus.Returned);

        double penalty = borrow.computePenalty(30 * DAY);

        assertThat(penalty).isEqualTo(0.0);
        assertThat(borrow.getStatus()).isEqualTo(borrowStatus.Returned);
    }

    @Test
    @DisplayName("Should not charge penalty within extended period")
    void testExtensionPreventsPenalty() {
        Borrow borrow = library.startBorrowing(user, book, 0L, 14 * DAY);
        borrow.extendLoan(14 * DAY);

        assertThat(borrow.computePenalty(20 * DAY)).isEqualTo(0.0);
        assertThat(borrow.getStatus()).isEqualTo(borrowStatus.Active);
    }

    @Test
    @DisplayName("Should contain all borrowStatus values")
    void testBorrowStatusValues() {
        assertThat(borrowStatus.values())
                .containsExactly(borrowStatus.Active, borrowStatus.Returned, borrowStatus.Overdue);
    }

    @Test
    @DisplayName("Should format borrow details via private formatBorrowDetails method")
    void testFormatBorrowDetails() throws Exception {
        Borrow borrow = library.startBorrowing(user, book, 0L, 14 * DAY);

        Method method = LibraryPortal.class.getDeclaredMethod("formatBorrowDetails", Borrow.class);
        method.setAccessible(true);
        String result = (String) method.invoke(LP, borrow);

        assertThat(result)
                .contains("Buch: Der Herr der Ringe (ISBN: 978-3-16-148410-0)")
                .contains("Nutzer: Max Mustermann")
                .contains("Status: Active")
                .contains("Einmalig verlängert: Nein")
                .contains("Verspätungsstrafe: 0.0 €");
    }

    @Test
    @DisplayName("Should throw when invoking formatBorrowDetails with null")
    void testFormatBorrowDetailsNullThrows() throws Exception {
        Method method = LibraryPortal.class.getDeclaredMethod("formatBorrowDetails", Borrow.class);
        method.setAccessible(true);

        assertThatThrownBy(() -> method.invoke(LP, (Borrow) null))
                .hasCauseInstanceOf(NullPointerException.class);
    }

    private Object getFieldValue(String fieldName, Object target) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    private void setFieldValue(String fieldName, Object target, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}