package model;

import java.util.ArrayList;
import java.util.HashSet;

public interface IRepository {
    public Question addQuestion(Question q) throws IRepositoryException;
    public void removeQuestion(Question q) throws IRepositoryException;
    public Question modifyQuestion(Question q) throws IRepositoryException;
    public ArrayList<Question> getAllQuestions() throws IRepositoryException;
    public HashSet<String> getAvailableTopics();
    public int getMaxQuestions(String temaSeleccionado);
    public void configureExam(String topic, int numQuestions);
    public void saveQuestions() throws IRepositoryException;
    public void loadQuestions() throws IRepositoryException;
}
