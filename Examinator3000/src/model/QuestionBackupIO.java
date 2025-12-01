package model;

import java.util.List;

public interface QuestionBackupIO{
    public void exportQuestions(List<Question> questions) throws QuestionBackupIOException;
    public List<Question> importQuestions() throws QuestionBackupIOException;
    public String getBackupIODescription() throws QuestionBackupIOException;
}