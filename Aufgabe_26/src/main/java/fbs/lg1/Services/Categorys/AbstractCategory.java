package fbs.lg1.Services.Categorys;

public abstract class AbstractCategory {
    String Title;
    long PlannedTimeSec;
    long StartTimeSec;

    public AbstractCategory(String title, long plannedTimeSec, long startTimeSec) {
        initialize(title, plannedTimeSec, startTimeSec);
    }

    private void initialize(String title, long plannedTimeSec, long startTimeSec) {
        this.Title = title;
        this.PlannedTimeSec = plannedTimeSec;
        this.StartTimeSec = startTimeSec;
    }

    public long getPlannedTimeSec() {
        return PlannedTimeSec;
    }

    public void setPlannedTimeSec(long plannedTimeSec) {
        this.PlannedTimeSec = plannedTimeSec;
    }

    public long getStartTimeSec() {
        return StartTimeSec;
    }

    public void setStartTimeSec(long startTimeSec) {
        this.StartTimeSec = startTimeSec;
    }

    public String getTitle() {
        return Title;
    }
}