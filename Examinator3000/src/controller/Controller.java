package controller;
import model.Model;
import view.BaseView;
public class Controller {
    private Model model;
    private BaseView view;

    public Controller(Model model, BaseView view) {
        this.model = model;
        this.view = view;
        view.setController(this);
    }
    public void start() {
        // Initialization logic here
    }
    public void end() {
        // Cleanup logic here
    }
}
