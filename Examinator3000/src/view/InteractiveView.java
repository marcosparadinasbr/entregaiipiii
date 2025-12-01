package view;
import static com.coti.tools.Esdia.*;
public class InteractiveView extends BaseView {
    public void init() {
        boolean salir = false;
        int option;
        do {
            showMessage("===================");
            showMessage("==EXAMINATOR 3000==");
            showMessage("===================");
            showMessage("1. Crear nuevas preguntas");
            showMessage("2. Listar preguntas");
            showMessage("3. Crear pregunta mautomáticamente");
            showMessage("4. Modo examen");
            showMessage("5. Exportar preguntas");
            showMessage("6. Importar preguntas");
            showMessage("7. Salir");
            option = readInt("Seleccione una opción: ");
            switch (option) {
                case 1:
                    // Logic for creating new questions
                    break;
                case 2:
                    // Logic for listing questions
                    break;
                case 3:
                    // Logic for auto-generating a question
                    break;
                case 4:
                    // Logic for exam mode
                    break;
                case 5:
                    // Logic for exporting questions
                    break;
                case 6:
                    // Logic for importing questions
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
}
