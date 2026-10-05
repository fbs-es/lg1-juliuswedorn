package fbs.lg1;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewAnswer {
    private String comment;
    private long date;

    public ReviewAnswer(String comment) {
        initialize(comment);
    }

    private void initialize(String comment) {
        this.comment = comment;
        this.date = System.currentTimeMillis();
    }

    public String getComment() {
        return comment;
    }

    public void writeComment(String comment) {
        this.comment = comment;
        this.date = System.currentTimeMillis();
    }

    public long getDate() {
        return date;
    }

    public String formatReviewAnswer() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return String.format("Answer: \"%s\" [%s]", comment, sdf.format(new Date(date)));
    }
}