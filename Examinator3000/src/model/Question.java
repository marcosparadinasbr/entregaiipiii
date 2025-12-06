package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class Question implements Serializable{
    UUID id; // Question identifier
    String author; // Human or AI model
    HashSet<String> topics; // One or more topics
    String statement; // Question statement
    List<Option> options; // 4 options
    private static final long serialVersionUID = 1L;

    public Question(String author, HashSet<String> topics, String statement, List<Option> options) {
        this.id = UUID.randomUUID();
        this.author = author;
        this.topics = topics;
        this.statement = statement;
        this.options = options;
    }
    public UUID getId() {
        return id;
    }
    public String getAuthor() {
        return author;
    }
    public HashSet<String> getTopics() {
        return topics;
    }
    public String getStatement() {
        return statement;
    }
    public List<Option> getOptions() {
        return options;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setTopics(HashSet<String> topics) {
        this.topics = topics;
    }
    public void setStatement(String statement) {
        this.statement = statement;
    }
    public void setOptions(List<Option> options) {
        this.options = options;
    }
}