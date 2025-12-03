import java.util.ResourceBundle.Control;
import model.Model;
import controller.Controller;
import view.BaseView;
import view.InteractiveView;

public class App {
    public static void main(String[] args) throws Exception {
        Model model = new Model();
        BaseView view = new InteractiveView();
        Controller controller = new Controller(model, view);
        controller.start();
        controller.end();
    }
}
