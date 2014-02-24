package fr.manu.cqrs.exception;

@SuppressWarnings("serial")
public class MatchAlreadyStartedException extends RuntimeException {

    public MatchAlreadyStartedException(String message) {
        super(message);
    }
}
