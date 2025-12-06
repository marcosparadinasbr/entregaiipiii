package model;

public class ExamResult {
    private int correct;
    private int wrong;
    private int skipped;
    private long timeMillis;
    private double grade;

    public ExamResult(int correct, int wrong, int skipped, long timeMillis, double grade) {
        this.correct = correct;
        this.wrong = wrong;
        this.skipped = skipped;
        this.timeMillis = timeMillis;
        this.grade = grade;
    }

    public int getCorrect() {
        return correct;
    }

    public int getWrong() {
        return wrong;
    }

    public int getSkipped() {
        return skipped;
    }

    public long getTimeMillis() {
        return timeMillis;
    }
    public int getTotal() {
        return correct + wrong + skipped;
    }
    public double getGrade() {
        return grade;
    }
}