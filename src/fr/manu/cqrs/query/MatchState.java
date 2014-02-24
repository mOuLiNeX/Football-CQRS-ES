package fr.manu.cqrs.query;

import com.google.common.base.Objects;

import fr.manu.cqrs.domain.MatchId;

public class MatchState {

    public final MatchId id;
    public Status status;

    private MatchState(MatchId id, Status status) {
        this.id = id;
        this.status = status;
    }

    public MatchState(MatchId id) {
        this(id, Status.PLANNED);
    }

    static enum Status {
        PLANNED, STARTED;
    }

    // For test use
    static MatchState createPlannedMatch(MatchId id) {
        return new MatchState(id);
    }

    // For test use
    static MatchState createStartedMatch(MatchId id) {
        return new MatchState(id, Status.STARTED);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MatchState other = (MatchState) obj;
        return Objects.equal(id, other.id) && Objects.equal(status, other.status);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, status);
    }

}
