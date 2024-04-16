package tyg.tradinggame.tradinggame.application.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class PublicEntityNotFoundException extends EntityNotFoundException {
    public PublicEntityNotFoundException(String message) {
        super(message);
    }
}
