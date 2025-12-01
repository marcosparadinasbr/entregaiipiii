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
}
