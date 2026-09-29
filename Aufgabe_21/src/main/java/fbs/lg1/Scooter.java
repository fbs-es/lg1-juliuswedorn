package fbs.lg1;

import java.util.ArrayList;
import java.util.List;

public class Scooter {
    private int scooterId;
    private int charge;
    private boolean locked;
    private Ride currentRide;
    private List<Ride> rideHistory;

    public Scooter() {
        initialize(100, true);
    }

    public Scooter(int charge, boolean locked) {
        initialize(charge, locked);
    }

    private void initialize(int charge, boolean locked) {
        this.scooterId = Main.getCityGlideApp().getScooterCount() + 1;
        setCharge(charge);
        this.locked = locked;
        this.rideHistory = new ArrayList<>();
    }

    public int getScooterId() {
        return scooterId;
    }

    public int getCharge() {
        return charge;
    }

    public boolean isLocked() {
        return locked;
    }

    public List<Ride> getRideHistory() {
        return rideHistory;
    }

    public void setCharge(int charge) {
        if (charge < 0 || charge > 100) throw new IllegalArgumentException("Battery charge must be between 0% and 100%.");

        this.charge = charge;
    }

    public boolean startScooter(User user) throws Exception {
        if (!this.locked) throw new Exception("Scooter is already in use.");

        if (this.charge <= 15) throw new Exception("Battery charge must be above 15%.");

        if (user.getCredit() < 1.0f) throw new Exception("User credit must be at least €1.00.");

        if (user.isUserLocked()) throw new Exception("User account is locked.");

        if (user.hasActiveRide()) throw new Exception("User already has an active ride.");


        this.locked = false;
        this.currentRide = new Ride(this.scooterId, user.getUserId());
        user.setCurrentRide(this.currentRide);

        return true;
    }

    public boolean stopScooter(User user) throws Exception {
        if (this.currentRide == null || this.currentRide.getUserId() != user.getUserId()) throw new Exception("No active ride found for this user.");


        this.currentRide.stopTime();
        long durationMs = this.currentRide.getEndDate() - this.currentRide.getStartDate();
        int minutes = (int) durationMs / 60000;

        if (minutes <= 0) throw new Exception("Invalid ride duration.");


        float cost = minutes * 0.20f;
        user.payOutCredit(cost);
        discharge(minutes);

        this.locked = true;
        this.rideHistory.add(this.currentRide);
        user.addRideToHistory(this.currentRide);

        this.currentRide = null;
        user.setCurrentRide(null);

        return true;
    }

    private void discharge(int amount) {
        int newCharge = this.charge - amount;
        this.charge = Math.max(0, newCharge);
    }
}