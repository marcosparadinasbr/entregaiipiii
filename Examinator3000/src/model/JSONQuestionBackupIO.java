package model;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONQuestionBackupIO implements QuestionBackupIO {
    @Override
    public void exportQuestions(List<Question> questions) throws QuestionBackupIOException {
        Path ruta = Paths.get(System.getProperty("user.home"), "backup.json");
        Gson gson = new Gson();
        String json = gson.toJson(questions);
        try {
            Files.write(ruta, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new QuestionBackupIOException("Error exporting questions to JSON", e);
        }
    }

    @Override
    public List<Question> importQuestions() throws QuestionBackupIOException {
        Path ruta = Paths.get(System.getProperty("user.home"), "backup.json");
        try {
            String json = new String (Files.readAllBytes(ruta), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            Type questionListType = new TypeToken<List<Question>>(){}.getType();
            List<Question> questions = gson.fromJson(json, questionListType);
            return questions;
        } catch (IOException e) {
            throw new QuestionBackupIOException("Error importing questions from JSON", e);
        }
    }

    @Override
    public String getBackupIODescription() throws QuestionBackupIOException {
        return "JSON Backup Handler";
    }
}
