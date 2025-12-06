import java.util.ArrayList;

import model.BinaryRepository;
import model.GeminiQuestionCreator;
import model.IRepository;
import model.Model;
import model.JSONQuestionBackupIO;
import model.QuestionBackupIO;
import model.QuestionCreator;
import controller.Controller;
import view.BaseView;
import view.InteractiveView;

public class App {
    public static void main(String[] args) throws Exception {
        String modelId = null;
        String apiKey = null;
        IRepository repository = new BinaryRepository();
        QuestionBackupIO questionBackupIO = new JSONQuestionBackupIO();
        ArrayList<QuestionCreator> questionCreators = new ArrayList<>();
        if (args.length == 3 && args[0].equals("-question-creator")) {
            modelId = args[1];
            apiKey = args[2];
        }
        if (apiKey != null) {
            questionCreators.add(new GeminiQuestionCreator(modelId, apiKey));
        }
        Model model = new Model(repository, questionBackupIO, questionCreators);
        BaseView view = new InteractiveView();
        Controller controller = new Controller(model, view);
        controller.start();
        controller.end();
    }
}
