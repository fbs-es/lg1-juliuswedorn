package fbs.lg1;

public class WorkingTime {
    private long Date;
    private long TimeInMinutes;
    private boolean Charged;

    WorkingTime(long Date, long TimeInMinutes) {
        this.Date = Date;
        this.TimeInMinutes = TimeInMinutes;
        this.Charged = false;
    }
}
