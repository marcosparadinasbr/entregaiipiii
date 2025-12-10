package view;
import static com.coti.tools.Esdia.*;

import java.util.HashSet;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

import model.Option;
import model.Question;
import model.ExamResult;
import model.IRepositoryException;
import model.QuestionBackupIOException;
import model.QuestionCreatorException;

public class InteractiveView extends BaseView {
    String[] opciones = {"a)", "b)", "c)", "d)"};
    ArrayList<Question> preguntas;
    private HashSet<String> temasDisponibles;
    int questionNumber = 0;
    public void init() {
        showMenu();
    }
    public void showMenu() {
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
        showMessage("Saliendo de Examinator 3000. ¡Hasta luego!");
    }
    public void showMessage(String message) {
        System.out.println(message);
    }
    public void showErrorMessage(String errorMessage) {
        System.err.println("Error: " + errorMessage);
    }
    private void addQuestion() {
        String author = readString_ne("Ingrese el autor de la pregunta: ");
        HashSet<String> topics = new HashSet<>();
        String topicsInput = readString_ne("Ingrese los temas separados por comas: ");
        for (String topic : topicsInput.split(",")) {
            topics.add(topic.trim().toUpperCase());
        }
        String statement = readString("Ingrese el enunciado de la pregunta: ");
        ArrayList<Option> options = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String optionText = readString_ne("Ingrese el texto de la opción " + opciones[i] + ": ");
            String rationale = readString_ne("Ingrese la justificación de la opción " + opciones[i] + ": ");
            boolean correct = yesOrNo("¿Es correcta la opción " + opciones[i] + "? ");
            options.add(new Option(optionText, rationale, correct));
        }
        Question newQuestion = new Question(author, topics, statement, options);
        try {
            controller.addQuestion(newQuestion);
            showMessage("Pregunta añadida al repositorio exitosamente.");
        } catch (IRepositoryException e) {
            showErrorMessage("Error al añadir la pregunta al repositorio: " + e.getMessage());
        }
    }
    private void listarPreguntas() {
        preguntas = new ArrayList<>();
        int numPregunta = 0;
        try {
            preguntas = controller.getAllQuestions();
        } catch (IRepositoryException e) {
            showErrorMessage("Error al obtener las preguntas del repositorio: " + e.getMessage());
        }
        if (preguntas.isEmpty()) {
                showMessage("No hay preguntas en el repositorio.");
        } else {
            showMessage("¿Desea filtrar por tema o ver por fecha de creación?");
            showMessage("1. Filtrar por tema");
            showMessage("2. Ver por fecha de creación");
            int filtro = readInt("Seleccione una opción: ", 1, 2);

            if (filtro == 1) {
                HashSet<String> temas = controller.getTopicsSet();
                showMessage("Temas disponibles:");
                for (String t : temas) {
                    showMessage("- " + t);
                }
                String temaFiltro = readString_ne("Ingrese uno de los temas anteriores: ").toUpperCase();
                ArrayList<Question> preguntasFiltradas = new ArrayList<>();
                for (Question q : preguntas) {
                    if (q.getTopics().contains(temaFiltro)) {
                        preguntasFiltradas.add(q);
                    }
                }
                if (preguntasFiltradas.isEmpty()) {
                    showMessage("No hay preguntas para el tema seleccionado.");
                    return;
                }
                preguntas = preguntasFiltradas;
            } else if (filtro == 2) {
                preguntas.sort(Comparator.comparingLong(Question::getCreatedAt));
            }
            for (int i = 0; i < preguntas.size(); i++) {
                showMessage("Pregunta " + (i + 1) + ": " + preguntas.get(i).getStatement());
                showMessage("Opciones:");
                ArrayList<Option> opts = (ArrayList<Option>) preguntas.get(i).getOptions();
                for (int j = 0; j < opts.size(); j++) {
                    showMessage("  " + opciones[j] + " " + opts.get(j).getText());
                }
                showMessage("Temas: " + String.join(", ", preguntas.get(i).getTopics()));
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                Instant instant = Instant.ofEpochMilli(preguntas.get(i).getCreatedAt());
                String fecha = fmt.format(instant.atZone(ZoneId.systemDefault()));
                showMessage("Fecha de creación: " + fecha);
                showMessage("-----------------------");
            }
            boolean resp=yesOrNo("Desea ver detalle de alguna de las preguntas");
            if (resp) {
                if (preguntas.size()>1) {
                    numPregunta = readInt("Ingrese el número de la pregunta a detallar: ", 1, preguntas.size());
                } else {
                    numPregunta = 1;
                }
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
        try {
            String topic = readString("Ingrese el tema para la pregunta automática: ");
            showMessage("Creando pregunta automáticamente...");
            controller.crearPreguntaAutomaticamente(topic.toUpperCase());
            showMessage("Pregunta creada y añadida al repositorio exitosamente.");
        } catch (QuestionCreatorException e) {
            showErrorMessage("Error al crear la pregunta automáticamente: " + e.getMessage());
        }
    }
    private void modoExamen() {
        controller.startExamMode();
    }
    public void showTopics(HashSet<String> topics) {
        this.temasDisponibles=topics;
        showMessage("Temas disponibles:");
        for (String topic : temasDisponibles) {
            showMessage("- " + topic);
        }
        showMessage("- TODOS");
        String temaSeleccionado = readString("Seleccione un tema de los anteriores o elija la opción TODOS: ");
        if (temaSeleccionado.equalsIgnoreCase("todos")) {
            showMessage("Ha seleccionado todos los temas.");
        } else if (temasDisponibles.contains(temaSeleccionado.toUpperCase())) {
            showMessage("Ha seleccionado el tema: " + temaSeleccionado.toUpperCase());
        } else {
            showErrorMessage("Tema no válido. Volviendo al menú principal.");
            return;
        }
        controller.topicSelected(temaSeleccionado.toUpperCase());
    }
    public void askNumQuestions(int maxQuestions) {
        if (maxQuestions == 0) {
            showErrorMessage("No hay preguntas disponibles para el tema seleccionado. Volviendo al menú principal.");
            return;
        }
        int numPreguntas = readInt("Ingrese el número de preguntas para el examen (máximo " + maxQuestions + "): ", 1, maxQuestions);
        try {
            controller.numQuestionsSelected(numPreguntas);
        } catch (IRepositoryException e) {
            showErrorMessage("Error al configurar el examen: " + e.getMessage());
        }
    }
    public void showQuestion(Question q) {
        questionNumber = questionNumber + 1;
        showMessage("Pregunta " + questionNumber + ":");
        showMessage("Enunciado: " + q.getStatement());
        ArrayList<Option> opts = (ArrayList<Option>) q.getOptions();
        for (int j = 0; j < opts.size(); j++) {
            Option opt = opts.get(j);
            showMessage(opciones[j] + " " + opt.getText());
        }
    }
    public int getUserAnswer() {
        String respuesta = readString("Ingrese la letra de su respuesta (a, b, c, d) o presione Enter para omitir: ");
        int answerIndex = -1;
        if (!respuesta.isEmpty()) {
            switch (respuesta.toLowerCase()) {
                case "a":
                    answerIndex = 0;
                    break;
                case "b":
                    answerIndex = 1;
                    break;
                case "c":
                    answerIndex = 2;
                    break;
                case "d":
                    answerIndex = 3;
                    break;
                default:
                    showErrorMessage("Respuesta no válida. Se considerará como omitida.");
            }
        }
        return answerIndex;
    }
    private void exportarPreguntas() {
        String nombreFichero= readString_ne("Ingrese el nombre del fichero para exportar las preguntas: ");
        if (!nombreFichero.endsWith(".json")) {
            nombreFichero += ".json";
        }
        showMessage("Exportando preguntas...");
        try {
            controller.exportQuestions(nombreFichero);
            showMessage("Preguntas exportadas exitosamente.");
        } catch (QuestionBackupIOException | IRepositoryException e) {
            showErrorMessage("Error al exportar las preguntas: " + e.getMessage());
        }
    }
    public void showFeedback(String result) {
        showMessage(result);
    }
    public void showExamResult(ExamResult result) {
        showMessage("Resultado del examen:");
        showMessage("Correctas: " + result.getCorrect());
        showMessage("Incorrectas: " + result.getWrong());
        showMessage("Omitidas: " + result.getSkipped());
        showMessage("Total de preguntas: " + result.getTotal());
        showMessage("Tiempo empleado (s): " + result.getTimeMillis() / 1000);
        showMessage("Nota final: " + String.format("%.2f", result.getGrade()));
    }
    private void importarPreguntas() {
        String nombreFichero= readString_ne("Ingrese el nombre del fichero para importar las preguntas: ");
        if (!nombreFichero.endsWith(".json")) {
            nombreFichero += ".json";
        }
        try {
            controller.importQuestions(nombreFichero);
            showMessage("Preguntas importadas exitosamente.");
        } catch (QuestionBackupIOException | IRepositoryException e) {
            showErrorMessage("Error al importar las preguntas: " + e.getMessage());
        }
    }
    private void modificarPregunta(Question q) {
        String author = readString_ne("Ingrese el nuevo autor de la pregunta: ");
        HashSet<String> topics = new HashSet<>();
        String topicsInput = readString_ne("Ingrese los nuevos temas separados por comas: ");
        for (String topic : topicsInput.split(",")) {
            topics.add(topic.trim().toUpperCase());
        }
        String statement = readString_ne("Ingrese el nuevo enunciado de la pregunta: ");
        ArrayList<Option> options = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String optionText = readString_ne("Ingrese el texto de la opción " + opciones[i] + ": ");
            String rationale = readString_ne("Ingrese la justificación de la opción " + opciones[i] + ": ");
            boolean correct = yesOrNo("¿Es correcta la opción " + opciones[i] + "");
            options.add(new Option(optionText, rationale, correct));
        }
        q.setAuthor(author);
        q.setTopics(topics);
        q.setStatement(statement);
        q.setOptions(options);
        try {
            controller.modifyQuestion(q);
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