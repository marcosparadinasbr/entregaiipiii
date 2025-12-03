package model;

import java.util.ArrayList;

public class Model {
    IRepository repository;
    QuestionBackupIO backupHandler;
    ArrayList<QuestionCreator> questionCreators;

    public Model (IRepository repository, QuestionBackupIO backupHandler, ArrayList<QuestionCreator> questionCreators) {
        this.repository = repository;
        this.backupHandler = backupHandler;
        this.questionCreators = questionCreators;
    }

    public Question addQuestion(Question newQuestion) throws IRepositoryException {
        return repository.addQuestion(newQuestion);
    }

    public ArrayList<Question> getAllQuestions() throws IRepositoryException {
        return repository.getAllQuestions();
    }
    public void modifyQuestion(Question modifiedQuestion) throws IRepositoryException {
        repository.modifyQuestion(modifiedQuestion);
    }

    public void removeQuestion(Question q) throws IRepositoryException {
        repository.removeQuestion(q);
    }
    public void exportQuestions() throws QuestionBackupIOException, IRepositoryException {
        backupHandler.exportQuestions(repository.getAllQuestions());
    }
    public void importQuestions() throws QuestionBackupIOException {
        backupHandler.importQuestions();
    }
}