package controller;
import java.util.ArrayList;
import java.util.HashSet;

import model.IRepositoryException;
import model.QuestionBackupIOException;
import model.QuestionCreatorException;
import model.Model;
import model.Question;
import view.BaseView;

public class Controller {
    private Model model;
    private BaseView view;
    private HashSet<String> topics;
    private int maxQuestions=0;
    private String temaSeleccionado;

    public Controller(Model model, BaseView view) {
        this.model = model;
        this.view = view;
        view.setController(this);
    }
    public void start() {
            try {
                model.loadState();
                view.showMessage("Preguntas cargadas correctamente.");
            } catch (Exception e) {
                view.showErrorMessage("No se pudieron cargar las preguntas: " + e.getMessage());
            }
        view.init();
    }
    public void end() {
        try {
            model.saveState();
            view.showMessage("Preguntas guardadas correctamente.");
        } catch (Exception e) {
            view.showErrorMessage("No se pudieron guardar las preguntas: " + e.getMessage());
        }
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
    public void importQuestions() throws QuestionBackupIOException, IRepositoryException {
        model.importQuestions();
    }
    public void startExamMode() {
        topics=model.getAvailableTopics();
        view.showTopics(topics);
    }
    public void topicSelected(String temaSeleccionado) {
        this.temaSeleccionado=temaSeleccionado;
        maxQuestions = model.getMaxQuestions(temaSeleccionado);
        view.askNumQuestions(maxQuestions);
    }
    public void numQuestionsSelected(int numQuestions) {
        model.configureExam(temaSeleccionado, numQuestions);
        questionLoop();
    }
    private void questionLoop() {
        Question currentQuestion = model.getCurrentQuestion();
        while (currentQuestion != null) {
            view.showQuestion(currentQuestion);
            int userAnswer = view.getUserAnswer();
            int result = model.answerCurrentQuestion(userAnswer);
            view.showFeedback(result);
            currentQuestion = model.getCurrentQuestion();
        }
        view.showExamResult(model.getExamResult());
    }
    public void crearPreguntaAutomaticamente(String topic) throws QuestionCreatorException {
        model.crearPreguntaAutomaticamente(topic);
    }
}