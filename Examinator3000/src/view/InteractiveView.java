package view;
import static com.coti.tools.Esdia.*;

import java.util.HashSet;
import java.util.ArrayList;
import model.Option;
import model.Question;
import model.IRepositoryException;

public class InteractiveView extends BaseView {
    String[] opciones = {"a)", "b)", "c)", "d)"};
    ArrayList<Question> preguntas;
    public void init() {
        boolean salir = false;
        int option;
        do {
            showMessage("=======================");
            showMessage("====EXAMINATOR 3000====");
            showMessage("=======================");
            showMessage("1. Crear nueva pregunta");
            showMessage("2. Listar preguntas");
            showMessage("3. Crear pregunta automáticamente");
            showMessage("4. Modo examen");
            showMessage("5. Exportar preguntas");
            showMessage("6. Importar preguntas");
            showMessage("7. Salir");
            option = readInt("Seleccione una opción: ");
            switch (option) {
                case 1:
                    addQuestion();
                    break;
                case 2:
                    listarPreguntas();
                    break;
                case 3:
                    crearPreguntaAutomaticamente();
                    break;
                case 4:
                    modoExamen();
                    break;
                case 5:
                    exportarPreguntas();
                    break;
                case 6:
                    importarPreguntas();
                    break;
                case 7:
                    salir = true;
                    break;
                default:
                    showErrorMessage("Opción no válida. Inténtelo de nuevo.");
            }

        } while (!salir); // Replace with actual condition
    }
    public void end() {
        showMessage("Exiting Interactive View. Goodbye!");
    }
    public void showMessage(String message) {
        System.out.println(message);
    }
    public void showErrorMessage(String errorMessage) {
        System.err.println("Error: " + errorMessage);
    }
    private void addQuestion() {
        String author = readString("Ingrese el autor de la pregunta: ");
        HashSet<String> topics = new HashSet<>();
        String topicsInput = readString("Ingrese los temas separados por comas: ");
        for (String topic : topicsInput.split(",")) {
            topics.add(topic.trim().toUpperCase());
        }
        String statement = readString("Ingrese el enunciado de la pregunta: ");
        ArrayList<Option> options = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String optionText = readString("Ingrese el texto de la opción " + opciones[i] + ": ");
            String rationale = readString("Ingrese la justificación de la opción " + opciones[i] + ": ");
            boolean correct = yesOrNo("¿Es correcta la opción " + opciones[i] + "? (y/n): ");
            options.add(new Option(optionText, rationale, correct));
        }
        Question newQuestion = new Question(author, topics, statement, options);
        showMessage("Pregunta creada exitosamente.");
        try {
            controller.addQuestion(newQuestion);
            showMessage("Pregunta añadida al repositorio exitosamente.");
        } catch (IRepositoryException e) {
            showErrorMessage("Error al añadir la pregunta al repositorio: " + e.getMessage());
        }
    }
    private void listarPreguntas() {
        preguntas = new ArrayList<>();
        try {
            preguntas = controller.getAllQuestions();
        } catch (IRepositoryException e) {
            showErrorMessage("Error al obtener las preguntas del repositorio: " + e.getMessage());
        }
        if (preguntas.isEmpty()) {
                showMessage("No hay preguntas en el repositorio.");
        } else {
            for (int i = 0; i < preguntas.size(); i++) {
                showMessage("Pregunta " + (i + 1) + ": " + preguntas.get(i).getStatement());
            }
            boolean resp=yesOrNo("Desea ver detalle de alguna de las preguntas? (y/n): ");
            if (resp) {
                int numPregunta = readInt("Ingrese el número de la pregunta a detallar: ", 1, preguntas.size());
                Question q = preguntas.get(numPregunta - 1);
                showMessage("Autor: " + q.getAuthor());
                showMessage("Temas: " + String.join(", ", q.getTopics()));
                showMessage("Enunciado: " + q.getStatement());
                ArrayList<Option> opts = (ArrayList<Option>) q.getOptions();
                for (int j = 0; j < opts.size(); j++) {
                    Option opt = opts.get(j);
                    showMessage(opciones[j] + " " + opt.getText() + " (Correcta: " + opt.isCorrect() + ")");
                    showMessage("   Justificación: " + opt.getRationale());
                }
                showMessage("=======================");
                showMessage("1. Modificar pregunta");
                showMessage("2. Eliminar pregunta");
                showMessage("3. Volver al menú principal");
                int accion = readInt("Seleccione una opción: ", 1, 3);
                switch (accion) {
                    case 1:
                        modificarPregunta(q);
                        break;
                    case 2:
                        eliminarPregunta(q);
                        break;
                    case 3:
                        break;  
                }
            }
        }
    }
    private void crearPreguntaAutomaticamente() {
        showMessage("Funcionalidad para crear una pregunta automáticamente.");
        // Implementación pendiente
    }
    private void modoExamen() {
        showMessage("Funcionalidad para modo examen.");
        // Implementación pendiente
    }
    private void exportarPreguntas() {
        showMessage("Funcionalidad para exportar preguntas.");
        // Implementación pendiente
    }
    private void importarPreguntas() {
        showMessage("Funcionalidad para importar preguntas.");
        // Implementación pendiente
    }
    private void modificarPregunta(Question q) {
        String author = readString("Ingrese el nuevo autor de la pregunta: ");
        HashSet<String> topics = new HashSet<>();
        String topicsInput = readString("Ingrese los nuevos temas separados por comas: ");
        for (String topic : topicsInput.split(",")) {
            topics.add(topic.trim().toUpperCase());
        }
        String statement = readString("Ingrese el nuevo enunciado de la pregunta: ");
        ArrayList<Option> options = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String optionText = readString("Ingrese el texto de la opción " + opciones[i] + ": ");
            String rationale = readString("Ingrese la justificación de la opción " + opciones[i] + ": ");
            boolean correct = yesOrNo("¿Es correcta la opción " + opciones[i] + "? (y/n): ");
            options.add(new Option(optionText, rationale, correct));
        }
        Question modifiedQuestion = new Question(author, topics, statement, options);
        try {
            controller.modifyQuestion(modifiedQuestion);
            showMessage("Pregunta modificada exitosamente.");
        } catch (IRepositoryException e) {
            showErrorMessage("Error al modificar la pregunta: " + e.getMessage());
        }
    }
    private void eliminarPregunta(Question q) {
        try {
            controller.removeQuestion(q);
            showMessage("Pregunta eliminada exitosamente.");
        } catch (IRepositoryException e) {
            showErrorMessage("Error al eliminar la pregunta: " + e.getMessage());
        }
    }
}