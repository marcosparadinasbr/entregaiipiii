package model;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONQuestionBackupIO implements QuestionBackupIO {
    Path ruta = Paths.get(System.getProperty("user.home"), "backup.json");
    @Override
    public void exportQuestions(List<Question> questions) throws QuestionBackupIOException {
        Gson gson = new Gson();
        String json = gson.toJson(questions);
        try {
            Files.write(ruta, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new QuestionBackupIOException("Error exporting questions to JSON", e);
        }
    }

    @Override
    public ArrayList<Question> importQuestions() throws QuestionBackupIOException {
        ArrayList<Question> questions = new ArrayList<>();
        Gson gson = new Gson();
        if (!Files.exists(ruta)) {
            return questions;
        }
        try {
            String json = new String (Files.readAllBytes(ruta), StandardCharsets.UTF_8);
            Type questionListType = new TypeToken<List<Question>>(){}.getType();
            List<Question> lista = gson.fromJson(json, questionListType);
            if (lista != null) {
                questions.addAll(lista);
            }
        } catch (IOException e) {
            throw new QuestionBackupIOException("Error importing questions from JSON", e);
        }
        return questions;
    }

    @Override
    public String getBackupIODescription() throws QuestionBackupIOException {
        return "JSON Backup Handler";
    }
}
