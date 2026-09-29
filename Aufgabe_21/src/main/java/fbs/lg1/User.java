package fbs.lg1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {
    private int userId;
    private String name;
    private String email;
    private float credit;
    private boolean isUserLocked;
    private boolean isReminderPending;
    private Ride currentRide;
    private List<Ride> rideHistory;

    public User(String name, String email, float credit) {
        initialize(name, email, credit);
    }

    private void initialize(String name, String email, float credit) {
        if (credit < 10.00f) {
            throw new IllegalArgumentException("The starting balance must be at least 10.00€");
        }

        this.userId = Main.getCityGlideApp().getUserCount() + 1;
        this.name = name;
        this.email = email;
        this.credit = credit;
        this.isUserLocked = false;
        this.isReminderPending = false;
        this.rideHistory = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }


    public float getCredit() {
        return credit;
    }

    public boolean isUserLocked() {
        return isUserLocked;
    }

    public void setCurrentRide(Ride currentRide) {
        this.currentRide = currentRide;
    }

    public boolean hasActiveRide() {
        return this.currentRide != null;
    }

    public String getRideHistoryString() {
        if (rideHistory.isEmpty()) {
            return "No rides in history.";
        }
        StringBuilder sb = new StringBuilder();
        for (Ride ride : rideHistory) {
            sb.append("Scooter ID: ").append(ride.getScooterId())
                    .append(" | Start: ").append((ride.getStartDate() / 60000)+" min")
                    .append(" | End: ").append((ride.getEndDate() / 60000)+" min");
        }
        return sb.toString();
    }

    public void addRideToHistory(Ride ride) {
        this.rideHistory.add(ride);
    }

    public void payOutCredit(float amount) {
        this.credit -= amount;

        if (this.credit < 0.00f) {
            lockUser();
            sendReminder();
        }
    }

    public boolean payInCredit(float amount) {
        this.credit += amount;

        if (this.credit > 0.00f) {
            unlockUser();
            this.isReminderPending = false;
        }
        return true;
    }

    private void lockUser() {
        this.isUserLocked = true;
    }

    private void unlockUser() {
        this.isUserLocked = false;
    }

    private void sendReminder() {
        this.isReminderPending = true;
        System.out.println("Automatic payment reminder sent via email to: " + this.email);
    }
}