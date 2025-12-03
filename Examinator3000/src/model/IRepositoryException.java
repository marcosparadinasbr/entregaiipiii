package model;

public class IRepositoryException extends Exception {
    public IRepositoryException(String message) {
        super(message);
    }
    public IRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
