package fbs.lg1;

import java.util.Date;

public class Ride {
    private int Scooter_ID;
    private int User_ID;
    private long startDate;
    private Long endDate;

    Ride(int Scooter_ID, int User_ID){
        initialize(Scooter_ID, User_ID);
    }

    private void initialize(int Scooter_ID, int User_ID){
        this.Scooter_ID = Scooter_ID;
        this.User_ID = User_ID;
        this.startDate = new Date().getTime();
        this.endDate = null;
    }

    public void stopTime(){
        this.endDate = new Date().getTime();
    }

    public int getScooterId() {
        return this.Scooter_ID;
    }

    public int getUserId() {
        return this.User_ID;
    }

    public Long getEndDate() {
        return this.endDate;
    }

    public long getStartDate() {
        return this.startDate;
    }
}