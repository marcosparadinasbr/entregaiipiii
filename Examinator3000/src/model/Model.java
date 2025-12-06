package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class Model {
    IRepository repository;
    QuestionBackupIO backupHandler;
    ArrayList<Question> questions;
    ArrayList<QuestionCreator> questionCreators;
    ArrayList<Question> exam;
    int currentQuestionIndex=0;
    int correct=0;
    int wrong=0;
    int skipped=0;
    long startTime=0;

    public Model (IRepository repository, QuestionBackupIO backupHandler, ArrayList<QuestionCreator> questionCreators) {
        this.repository = repository;
        this.backupHandler = backupHandler;
        this.questionCreators = questionCreators;
    }

    public Question addQuestion(Question newQuestion) throws IRepositoryException {
        return repository.addQuestion(newQuestion);
    }

    public ArrayList<Question> getAllQuestions() throws IRepositoryException {
        return repository.getAllQuestions();
    }
    public void modifyQuestion(Question modifiedQuestion) throws IRepositoryException {
        repository.modifyQuestion(modifiedQuestion);
    }

    public void removeQuestion(Question q) throws IRepositoryException {
        repository.removeQuestion(q);
    }
    public void exportQuestions() throws QuestionBackupIOException, IRepositoryException {
        backupHandler.exportQuestions(repository.getAllQuestions());
    }
    public void importQuestions() throws QuestionBackupIOException, IRepositoryException {
        ArrayList<Question> importedQuestions = (ArrayList<Question>) backupHandler.importQuestions();
        List<Question> existing = repository.getAllQuestions();
        HashSet<UUID> existingIds = new HashSet<>();
        for (Question q : existing) {
            existingIds.add(q.getId());
        }

        for (Question q : importedQuestions) {
            if (!existingIds.contains(q.getId())) {
                repository.addQuestion(q);
            }
        } 
    }

    public HashSet<String> getAvailableTopics() {
        HashSet<String> topics = new HashSet<>();
        ArrayList<Question> questions;
        try {
            questions = repository.getAllQuestions();
        } catch (IRepositoryException e) {
            return topics;
        }
        for (Question q : questions) {
            topics.addAll(q.getTopics());
        }
        return topics;
    }
    public int getMaxQuestions(String temaSeleccionado) {
        int maxQuestions=0;
        ArrayList<Question> questions;
        try {
            questions = repository.getAllQuestions();
        } catch (IRepositoryException e) {
            return 0;
        }
        for (Question q : questions) {
            if (q.getTopics().contains(temaSeleccionado)) {
                maxQuestions++;
            }
        }
        return maxQuestions;
    }
    public void configureExam(String topic, int numQuestions) {
        int count=0;
        exam = new ArrayList<>();
        currentQuestionIndex=0;
        correct=0;
        wrong=0;
        skipped=0;
        startTime=System.currentTimeMillis();
        ArrayList<Question> questions;
        try {
            questions = repository.getAllQuestions();
        } catch (IRepositoryException e) {
            return;
        }
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
        if (exam == null || exam.isEmpty() || currentQuestionIndex >= exam.size()) {
            return null;
        }
        return exam.get(currentQuestionIndex);
    }
    public int answerCurrentQuestion(Integer answerIndex) {
        Question q = exam.get(currentQuestionIndex);
        if (answerIndex == null) {
            skipped++;
            currentQuestionIndex++;
            return 0;
        } else if (q.getOptions().get(answerIndex).isCorrect()) {
            correct++;
            currentQuestionIndex++;
            return 1;
        } else {
            wrong++;
            currentQuestionIndex++;
            return -1;
        }
        
    }
    public ExamResult getExamResult() {
        long timeMillis = System.currentTimeMillis() - startTime;
        double valorPregunta = 10.0 / (correct + wrong + skipped);
        double penalizacion = valorPregunta * 0.33;
        double nota = correct * valorPregunta - wrong * penalizacion;
        if (nota < 0) {
            nota = 0;
        }
        return new ExamResult(correct, wrong, skipped, timeMillis, nota);
    }
    public void crearPreguntaAutomaticamente(String topic) throws QuestionCreatorException {
        if (questionCreators.isEmpty()) {
            throw new QuestionCreatorException("No hay creadores de preguntas disponibles.");
        }
        QuestionCreator creator = questionCreators.get(0);
        Question nuevaPregunta = creator.createQuestion(topic);
        try {
            repository.addQuestion(nuevaPregunta);
        } catch (IRepositoryException e) {
            throw new QuestionCreatorException("Error al añadir la pregunta al repositorio: " + e.getMessage(), e);
        }
    }
}