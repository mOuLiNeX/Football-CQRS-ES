package fr.manu.cqrs.domain;

import java.util.UUID;

import com.google.common.base.Objects;

public class MatchId {
    private final UUID id;

    private MatchId(UUID id) {
        this.id = id;
    }

    public static final MatchId newMatchId() {
        return new MatchId(UUID.randomUUID());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MatchId other = (MatchId) obj;
        return Objects.equal(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(this.id).toString();
    }
}
