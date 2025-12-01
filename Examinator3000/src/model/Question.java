package model;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class Question {
    UUID id; // Question identifier
    String author; // Human or AI model
    HashSet<String> topics; // One or more topics
    String statement; // Question statement
    List<Option> options; // 4 options

}
