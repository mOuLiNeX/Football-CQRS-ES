package fr.manu.cqrs.domain;

import java.util.Objects;

public class Team {

    public final String name;

    public Team(String name) {
        super();
        this.name = name;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		return Objects.equals(this.name, other.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}

}
