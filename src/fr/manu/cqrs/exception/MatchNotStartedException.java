package fr.manu.cqrs.exception;

@SuppressWarnings("serial")
public class MatchNotStartedException extends RuntimeException {

    public MatchNotStartedException(String message) {
        super(message);
    }
}
