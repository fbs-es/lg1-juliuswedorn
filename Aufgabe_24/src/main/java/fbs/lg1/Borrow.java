package fbs.lg1;

public class Borrow {
    private long startDate;
    private long endDate;
    private borrowStatus status;
    private boolean extended;
    private double penalty;
    private Book book;
    private User user;

    public Borrow(User user, Book book, long startDate, long endDate) {
        this.user = user;
        this.book = book;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = borrowStatus.Active;
        this.extended = false;
        this.penalty = 0.0;
    }

    public boolean extendLoan(long extraMillis) {
        if (!this.extended && this.status == borrowStatus.Active) {
            this.endDate += extraMillis;
            this.extended = true;
            return true;
        }
        return false;
    }

    public double computePenalty(long currentDate) {
        if (currentDate > this.endDate && this.status != borrowStatus.Returned) {
            this.status = borrowStatus.Overdue;
            long overdueMillis = currentDate - this.endDate;
            long daysOverdue = overdueMillis / (1000 * 60 * 60 * 24);
            if (daysOverdue == 0) {
                daysOverdue = 1;
            }
            this.penalty = daysOverdue * 1.50;
        }
        return this.penalty;
    }

    public long getStartDate() { return startDate; }
    public long getEndDate() { return endDate; }
    public borrowStatus getStatus() { return status; }
    public void setStatus(borrowStatus status) { this.status = status; }
    public boolean isExtended() { return extended; }
    public double getPenalty() { return penalty; }
    public Book getBook() { return book; }
    public User getUser() { return user; }
}