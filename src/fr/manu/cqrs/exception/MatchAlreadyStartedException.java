package fr.manu.cqrs.exception;

@SuppressWarnings("serial")
public class MatchAlreadyStartedException extends RuntimeException {

    public MatchAlreadyStartedException() {
        super();
    }

    public MatchAlreadyStartedException(String message) {
        super(message);
    }

    public MatchAlreadyStartedException(Throwable cause) {
        super(cause);
    }

    public MatchAlreadyStartedException(String message, Throwable cause) {
        super(message, cause);
    }

}
