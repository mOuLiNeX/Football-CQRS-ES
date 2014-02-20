package fr.manu.cqrs.query;

import com.google.common.base.Objects;

public class TeamState {

    public final String name;
    private int victoryCounter;
    private int defeatCounter;

    public TeamState(String name) {
        this.name = name;
        this.victoryCounter = 0;
        this.defeatCounter = 0;
    }

    private TeamState(String name, int victoryCounter, int defeatCounter) {
        this(name);
        this.victoryCounter = victoryCounter;
        this.defeatCounter = defeatCounter;
    }

    // For test use
    static TeamState createVictoryStat(String teamName) {
        return new TeamState(teamName, 1, 0);
    }

    // For test use
    static TeamState createDefeatStat(String teamName) {
        return new TeamState(teamName, 0, 1);
    }

    public void addVictory() {
        victoryCounter++;
    }

    public void addDefeat() {
        defeatCounter++;
    }

    public int getVictoryCounter() {
        return victoryCounter;
    }

    public int getDefeatCounter() {
        return defeatCounter;
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
        TeamState other = (TeamState) obj;
        return Objects.equal(name, other.name) && Objects.equal(victoryCounter, other.victoryCounter)
            && Objects.equal(defeatCounter, other.defeatCounter);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, victoryCounter, defeatCounter);
    }
}
