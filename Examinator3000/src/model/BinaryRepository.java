package model;

import java.util.ArrayList;
import java.util.UUID;

public class BinaryRepository implements IRepository {
    ArrayList<Question> questions;
    public BinaryRepository() {
        questions = new ArrayList<>();
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