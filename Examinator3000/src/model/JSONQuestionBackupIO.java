package model;

import java.io.IOException;
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
    
    @Override
    public void exportQuestions(List<Question> questions, String filename) throws QuestionBackupIOException {
        Path ruta = Paths.get(System.getProperty("user.home"), filename);
        Gson gson = new Gson();
        String json = gson.toJson(questions);
        if (questions == null || questions.isEmpty()) {
            throw new QuestionBackupIOException("No hay preguntas para exportar.");
        }
        try {
            Files.write(ruta, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new QuestionBackupIOException("Error exportando preguntas a JSON", e);
        }
    }

    @Override
    public ArrayList<Question> importQuestions(String filename) throws QuestionBackupIOException {
        ArrayList<Question> questions = new ArrayList<>();
        Gson gson = new Gson();
        Path ruta = Paths.get(System.getProperty("user.home"), filename);
        if (!Files.exists(ruta)) {
            throw new QuestionBackupIOException("No se encontró el archivo de respaldo JSON.");
        }
        try {
            String json = new String (Files.readAllBytes(ruta), StandardCharsets.UTF_8);
            Type questionListType = new TypeToken<List<Question>>(){}.getType();
            List<Question> lista = gson.fromJson(json, questionListType);
            if (lista == null || lista.isEmpty()) {
                throw new QuestionBackupIOException("No se encontraron preguntas en el archivo JSON.");
            }
            if (lista != null) {
                questions.addAll(lista);
            }
        } catch (IOException e) {
            throw new QuestionBackupIOException("Error importando preguntas desde JSON", e);
        }
        return questions;
    }

    @Override
    public String getBackupIODescription() throws QuestionBackupIOException {
        return "JSON Backup Handler";
    }
}
