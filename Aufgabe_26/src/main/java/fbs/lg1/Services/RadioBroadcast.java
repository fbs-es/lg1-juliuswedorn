package fbs.lg1.Services;

import fbs.lg1.Services.Categorys.AbstractCategory;
import fbs.lg1.Services.Categorys.NewsCategory;
import fbs.lg1.Services.Categorys.PresenterCategory;

import java.util.ArrayList;

public class RadioBroadcast {
    String Name;
    Moderator Moderator;
    long StartTime;
    long EndTime;
    long MaxTime;
    ArrayList<AbstractCategory> Categories = new ArrayList<AbstractCategory>();

    public RadioBroadcast (String name, Moderator moderator, long startTime, long endTime) {
        initialize(name, moderator, startTime, endTime);
    }

    private void initialize(String name, Moderator moderator, long startTime, long endTime) {
        this.Name = name;
        this.Moderator = moderator;
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.MaxTime = endTime - startTime;
    }

    public long getRestTime(long CurrentTime) {
        return EndTime - CurrentTime;
    }

    public long getStartTime() {
        return StartTime;
    }

    public String GetName() {
        return Name;
    }

    public long getMaxTime() {
        return MaxTime;
    }

    public ArrayList<AbstractCategory> getCategorys() {
        return Categories;
    }

    public long getTotalTime() {
        long sum = 0;
        for (AbstractCategory category : Categories) {
            sum += category.getPlannedTimeSec();
        }
        return sum;
    }

    public void recalculateStartTimes() {
        long currentOffset = 0;
        for (AbstractCategory category : Categories) {
            category.setStartTimeSec(currentOffset);
            currentOffset += category.getPlannedTimeSec();
        }
    }

    public void addReports(AbstractCategory category) {
        if (getTotalTime() + category.getPlannedTimeSec() > MaxTime) {
            System.out.println("WARNUNG: Hinzufügen nicht möglich! Das Zeitlimit der Sendung wird überschritten.");
            return;
        }
        Categories.add(category);
        recalculateStartTimes();
    }

    public void removeReports(AbstractCategory category) {
        Categories.remove(category);
        recalculateStartTimes();
    }

    public void updateReportsTime(AbstractCategory category, long newTime) {
        long difference = newTime - category.getPlannedTimeSec();
        if (getTotalTime() + difference > MaxTime) {
            System.out.println("WARNUNG: Zeitänderung überschreitet das Sende-Limit!");
        }
        category.setPlannedTimeSec(newTime);
        recalculateStartTimes();
    }

    public void insertBreakingNews(NewsCategory news) {
        if (getTotalTime() + news.getPlannedTimeSec() > MaxTime) {
            System.out.println("WARNUNG: Eilmeldung sprengt das Zeitlimit der Sendung!");
        }
        if (!Categories.isEmpty()) {
            Categories.add(0, news);
        } else {
            Categories.add(news);
        }
        recalculateStartTimes();
    }

    public void splitReports(AbstractCategory categoryToInterrupt, NewsCategory news, long splitPointSec) {
        if (!Categories.contains(categoryToInterrupt)) {
            System.out.println("Fehler: Beitrag existiert nicht in dieser Sendung.");
            return;
        }

        if (getTotalTime() + news.getPlannedTimeSec() > MaxTime) {
            System.out.println("WARNUNG: Sendezeitlimit durch Eilmeldung überschritten!");
        }

        int index = Categories.indexOf(categoryToInterrupt);
        long originalTotalTime = categoryToInterrupt.getPlannedTimeSec();

        if (splitPointSec >= originalTotalTime || splitPointSec <= 0) {
            Categories.add(index, news);
        } else {
            categoryToInterrupt.setPlannedTimeSec(splitPointSec);

            Categories.add(index + 1, news);

            AbstractCategory remainingPart = new PresenterCategory(
                    categoryToInterrupt.getTitle() + " (Rest)",
                    originalTotalTime - splitPointSec,
                    0,
                    "Restlaufzeit nach Unterbrechung"
            );
            Categories.add(index + 2, remainingPart);
        }
        recalculateStartTimes();
    }

    public void splitReports(AbstractCategory category) {
        if (category != null && Categories.contains(category)) {
            NewsCategory defaultNews = new NewsCategory("Eilmeldung", 60, 0, "Allgemein", 3);
            splitReports(category, defaultNews, category.getPlannedTimeSec() / 2);
        }
    }
}