package controller;
import java.util.ArrayList;

import model.IRepositoryException;
import model.Model;
import model.Question;
import view.BaseView;
public class Controller {
    private Model model;
    private BaseView view;

    public Controller(Model model, BaseView view) {
        this.model = model;
        this.view = view;
        view.setController(this);
    }
    public void start() {
        // Initialization logic here
    }
    public void end() {
        // Cleanup logic here
    }
    public void addQuestion(Question newQuestion) throws IRepositoryException {
        model.addQuestion(newQuestion);
    }
    public ArrayList<Question> getAllQuestions() throws IRepositoryException {
        return model.getAllQuestions();
    }
    public void modifyQuestion(Question modifiedQuestion) throws IRepositoryException {
        model.modifyQuestion(modifiedQuestion);
    }
    public void removeQuestion(Question q) throws IRepositoryException {
        model.removeQuestion(q);
    }
}
