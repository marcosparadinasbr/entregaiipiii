package model;

import java.util.List;

public interface QuestionBackupIO{
    public void exportQuestions(List<Question> questions, String filename) throws QuestionBackupIOException;
    public List<Question> importQuestions(String filename) throws QuestionBackupIOException;
    public String getBackupIODescription() throws QuestionBackupIOException;
}