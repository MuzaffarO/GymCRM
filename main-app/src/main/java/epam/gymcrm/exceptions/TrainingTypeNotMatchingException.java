package epam.gymcrm.exceptions;

public class TrainingTypeNotMatchingException extends RuntimeException{
    public TrainingTypeNotMatchingException(String message) {
        super(message);
    }
}
