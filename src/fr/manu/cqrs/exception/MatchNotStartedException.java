package fr.manu.cqrs.exception;

@SuppressWarnings("serial")
public class MatchNotStartedException extends RuntimeException {

    public MatchNotStartedException() {
        super();
    }

    public MatchNotStartedException(String message) {
        super(message);
    }

    public MatchNotStartedException(Throwable cause) {
        super(cause);
    }

    public MatchNotStartedException(String message, Throwable cause) {
        super(message, cause);
    }

}
