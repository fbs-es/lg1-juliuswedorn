package fbs.lg1.Services.Categorys;

public class AdCategory extends AbstractCategory{

    private String CustomerName;
    private float AdBudget;

    public AdCategory(String title, long plannedTimeSec, long startTimeSec,String CustomerName,float AdBudget) {
        super(title, plannedTimeSec, startTimeSec);
        initialize(CustomerName, AdBudget);
    }

    private void initialize(String CustomerName, float AdBudget) {
        this.CustomerName = CustomerName;
        this.AdBudget = AdBudget;
    }

}
