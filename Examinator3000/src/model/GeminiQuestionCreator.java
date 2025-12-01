package model;

public class GeminiQuestionCreator implements QuestionCreator {
    String questionCreatorDescription;
    String API_KEY;
    public GeminiQuestionCreator(String API_KEY) {
        this.API_KEY = API_KEY;
    }
    public String getQuestionCreatorDescription(){
        return questionCreatorDescription;
    }
    public Question createQuestion(String topic){

    }
}
