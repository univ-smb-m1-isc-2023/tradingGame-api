package tyg.tradinggame.tradinggame.application.exceptions;

public class PublicIllegalArgumentException extends IllegalArgumentException {
    public PublicIllegalArgumentException(String message) {
        super(message);
    }

    public PublicIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
