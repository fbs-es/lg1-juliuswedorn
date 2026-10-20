package fbs.lg1.Services.Categorys;

public class PresenterCategory extends AbstractCategory {

    private String Script;

    public PresenterCategory(String title, long plannedTimeSec, long startTimeSec,String script) {
        super(title, plannedTimeSec, startTimeSec);
        initialize(script);
    }

    private void initialize(String script) {
        this.Script = script;
    }
}
