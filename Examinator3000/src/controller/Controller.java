package controller;
import java.util.ArrayList;
import java.util.HashSet;

import model.IRepositoryException;
import model.QuestionBackupIOException;
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
        view.init();
    }
    public void end() {
        view.end();
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
    public void exportQuestions() throws QuestionBackupIOException, IRepositoryException {
        model.exportQuestions();
    }
    public void importQuestions() throws QuestionBackupIOException {
        model.importQuestions();
    }
    public HashSet<String> startExamMode() {
        return model.getAvailableTopics();
    }
    public int topicSelected(String temaSeleccionado) {
        return model.getMaxQuestions(temaSeleccionado);
    }
    public void numQuestionsSelected(String temaSeleccionado, int numQuestions) {
        model.configureExam(temaSeleccionado, numQuestions);
        
    }
}