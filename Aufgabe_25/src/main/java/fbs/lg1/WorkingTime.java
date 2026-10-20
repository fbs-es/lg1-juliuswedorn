package fbs.lg1;

/**
 * Represents a record of working time, tracking the date, duration in minutes,
 * and whether the time has already been charged or processed.
 */
public class WorkingTime {

    private long Date;
    private long TimeInMinutes;
    private boolean Charged;

    /**
     * Constructs a new WorkingTime record with the specified date and duration in minutes.
     * The charged status is initialized to {@code false} by default.
     *
     * @param Date          the date of the working time record (typically as a timestamp or numerical format)
     * @param TimeInMinutes the duration of work in minutes
     */
    WorkingTime(long Date, long TimeInMinutes) {
        this.Date = Date;
        this.TimeInMinutes = TimeInMinutes;
        this.Charged = false;
    }
}