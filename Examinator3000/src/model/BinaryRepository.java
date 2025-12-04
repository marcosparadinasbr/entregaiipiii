package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class BinaryRepository implements IRepository {
    Path ruta = Paths.get(System.getProperty("user.home"), "questions.bin");
    ArrayList<Question> questions;
    ArrayList<Question> exam;
    public BinaryRepository() {
        questions = new ArrayList<>();
    }
    @Override
    public void saveQuestions() throws IRepositoryException {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(Files.newOutputStream(ruta));
            oos.writeObject(questions);
        } catch (IOException e) {
            throw new IRepositoryException("Error al guardar las preguntas: " + e.getMessage());
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (java.io.IOException e) {
                    throw new IRepositoryException("Error al cerrar el flujo de salida: " + e.getMessage());
                }
            }
        }
    }
    @SuppressWarnings("unchecked")
    @Override
    public void loadQuestions() throws IRepositoryException {
        if (!Files.exists(ruta)) {
            questions = new ArrayList<>();
            return;
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(Files.newInputStream(ruta));
            questions = (ArrayList<Question>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IRepositoryException("Error al cargar las preguntas: " + e.getMessage());
        }
        finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public Question addQuestion(Question q) throws IRepositoryException {
        try {
            questions.add(q);
            return q;
        } catch (Exception e) {
            throw new IRepositoryException("No se pudo añadir la pregunta: " + e.getMessage());
        }
    }
    @Override
    public void removeQuestion(Question q) throws IRepositoryException {
        try {
            questions.remove(q);
        } catch (Exception e) {
            throw new IRepositoryException("No se pudo eliminar la pregunta: " + e.getMessage());
        }
    }
    @Override
    public Question modifyQuestion(Question q) throws IRepositoryException {

        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getId().equals(q.getId())) {
                questions.set(i, q);
                return q;
            }
        }

        throw new IRepositoryException("Pregunta no encontrada para modificar (ID: " + q.getId() + ")");
    }
    @Override
    public ArrayList<Question> getAllQuestions() throws IRepositoryException {
        try {
            return new ArrayList<>(questions);
        } catch (Exception e) {
            throw new IRepositoryException("No se pudieron obtener las preguntas: " + e.getMessage());
        }
    }
    @Override
    public HashSet<String> getAvailableTopics() {
        HashSet<String> topics = new HashSet<>();
        for (Question q : questions) {
            topics.addAll(q.getTopics());
        }
        return topics;
    }
    @Override
    public int getMaxQuestions(String temaSeleccionado) {
        int maxQuestions=0;
        for (Question q : questions) {
            if (q.getTopics().contains(temaSeleccionado)) {
                maxQuestions++;
            }
        }
        return maxQuestions;
    }
    @Override
    public void configureExam(String topic, int numQuestions) {
        int count=0;
        exam = new ArrayList<>();
        if (topic.equalsIgnoreCase("todos")) {
            while (count < numQuestions) {
                exam.add(questions.get(count));
                count++;
            }
        }
        else {
            for (Question q : questions) {
                if (q.getTopics().contains(topic) && count < numQuestions) {
                    exam.add(q);
                    count++;
                }
            }
        }

    }
    public Question getCurrentQuestion() {
        // This method can be expanded to return questions from the exam list
    }
}