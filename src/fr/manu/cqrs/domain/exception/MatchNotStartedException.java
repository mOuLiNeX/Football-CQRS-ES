package fr.manu.cqrs.domain.exception;

@SuppressWarnings("serial")
public class MatchNotStartedException extends RuntimeException {

    public MatchNotStartedException(String message) {
        super(message);
    }
}
