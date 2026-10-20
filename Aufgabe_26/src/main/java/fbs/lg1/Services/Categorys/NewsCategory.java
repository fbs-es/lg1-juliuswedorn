package fbs.lg1.Services.Categorys;

public class NewsCategory extends AbstractCategory{

    String Region;
    int Urgency;

    public NewsCategory(String title, long plannedTimeSec, long startTimeSec, String region, int urgency) {
        super(title, plannedTimeSec, startTimeSec);
        initialize(region, urgency);
    }

    protected void initialize(String region, int urgency) {
        this.Region = region;
        this.Urgency = urgency;
    }


}
