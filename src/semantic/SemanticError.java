package semantic;

public class SemanticError extends RuntimeException {
    public SemanticError(String message) {
        super(message);
    }

    public SemanticError(String message, Throwable cause) {
        super(message, cause);
    }
}
