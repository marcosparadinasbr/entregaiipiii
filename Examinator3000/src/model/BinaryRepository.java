package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class BinaryRepository implements IRepository {
    Path ruta = Paths.get(System.getProperty("user.home"), "questions.bin");
    ArrayList<Question> questions;
    ArrayList<Question> exam;
    public BinaryRepository() {
        questions = new ArrayList<>();
    }
    public void saveQuestions() throws IRepositoryException {
        ObjectOutputStream oos = null;
        if (questions == null || questions.isEmpty()) {
            throw new IRepositoryException("No hay preguntas para guardar.");
        }
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
    public void loadQuestions() throws IRepositoryException {
        if (!Files.exists(ruta)) {
            questions = new ArrayList<>();
            return;
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(Files.newInputStream(ruta));
            questions = (ArrayList<Question>) ois.readObject();
            if (questions == null || questions.isEmpty()) {
                throw new IRepositoryException("No se encontraron preguntas en el archivo.");
            }
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
}