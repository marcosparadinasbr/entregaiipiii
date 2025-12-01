package model;

public interface QuestionCreator {
    public Question createQuestion(String topic) throws QuestionCreatorException;
    public String getQuestionCreatorDescription();
}
