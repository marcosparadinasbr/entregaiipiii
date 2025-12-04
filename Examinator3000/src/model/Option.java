package model;

import java.io.Serializable;

public class Option implements Serializable{
    String text; // Answewr text
    String rationale; // Why it is right/wrong
    boolean correct; // Is this option correct?

    public Option(String text, String rationale, boolean correct) {
        this.text = text;
        this.rationale = rationale;
        this.correct = correct;
    }
    public String getText() {
        return text;
    }
    public String getRationale() {
        return rationale;
    }
    public boolean isCorrect() {
        return correct;
    }
}
