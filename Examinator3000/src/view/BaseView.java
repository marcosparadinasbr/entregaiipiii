package view;
import controller.Controller;
public abstract class BaseView {
    private Controller controller;
    public void setController(Controller controller) {
        this.controller = controller;
    }
    public abstract void init();
    public abstract void end();
    public abstract void showMessage(String message);
    public abstract void showErrorMessage(String errorMessage);
}
