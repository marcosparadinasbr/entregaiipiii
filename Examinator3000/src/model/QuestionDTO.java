package model;

import java.util.HashSet;
import java.util.List;

public class QuestionDTO {
    public String author;
    public String statement;
    public List<OptionDTO> options;
    public HashSet<String> topics;

    public String getAuthor() {
        return author;
    }
    public String getStatement() {
        return statement;
    }
    public List<OptionDTO> getOptions() {
        return options;
    }
    public HashSet<String> getTopics() {
        return topics;
    }
}
