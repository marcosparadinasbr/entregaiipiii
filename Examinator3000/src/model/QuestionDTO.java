package model;

import java.util.List;

public class QuestionDTO {
    public String author;
    public List<String> topics;
    public String statement;
    public List<OptionDTO> options;

    public String getAuthor() {
        return author;
    }
    public String getStatement() {
        return statement;
    }
    public List<OptionDTO> getOptions() {
        return options;
    }
    public List<String> getTopics() {
        return topics;
    }
}
