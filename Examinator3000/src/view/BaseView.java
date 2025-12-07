package view;
import java.util.HashSet;

import model.ExamResult;
import model.Question;

import controller.Controller;
public abstract class BaseView {
    protected Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }
    public abstract void init();
    public abstract void end();
    public abstract void showMessage(String message);
    public abstract void showErrorMessage(String errorMessage);
    public abstract void showTopics(HashSet<String> topics);
    public abstract void askNumQuestions(int maxQuestions);
    public abstract int getUserAnswer();
    public abstract void showQuestion(Question q);
    public abstract void showFeedback(String result);
    public abstract void showExamResult(ExamResult result);
}
