package Disease;

/**
 * Consolidates all the parameters specific to individual agents.
 * */

public class AgentParameters {
    private int vulnDist;
    private int sick, immune;
    private double HealthyChance, GhostChance;
    private int speed;
    private int x, y;
    private boolean haunt, ghosts;
    private int incTime, sickTime, ghostTime, ghostLife;


    /**
     * Initializes all parameters to their default values.
     **/

    public AgentParameters() {
        incTime = 5;
        sickTime = 10;
        HealthyChance = 0.95;
        speed = 100;
        vulnDist = 20;
        x = 500;
        y = 500;
        haunt = false;

        // Zombie parameters
        ghosts = false;
        ghostTime = 10;
        GhostChance = 0;
        ghostLife = 10;
    }

    public void setIncTime(int t) {
        incTime = t;
    }

    public void setSickTime(int t) {
        sickTime = t;
    }

    public void setHealthyChance(double p) {
        HealthyChance = p;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSick(int n) {
        sick = n;
    }

    public void setImmune(int n) {
        immune = n;
    }

    public void setVulnDist(int dist) {
        vulnDist = dist;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setWanderEnabled(boolean wander) {
        this.haunt = wander;
    }

    public void becomeGhost(boolean zombies) {
        this.ghosts = zombies;
    }

    public void setReanimateProb(double p) {
        GhostChance = p;
    }

    public void setGhostTime(int t) {
        ghostTime = t;
    }

    public void setGhostLife(int t) {
        ghostLife = t;
    }

    public int getIncTime() {
        return incTime;
    }

    public int getSick() {
        return sick;
    }

    public int getImmune() {
        return immune;
    }

    public int getSickTime() {
        return sickTime;
    }

    public double getRecoveryProb() {
        return HealthyChance;
    }

    public int getVulnDist() {
        return vulnDist;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean getHaunt() {
        return haunt;
    }

    public boolean ghostEnabled() {
        return ghosts;
    }

    public double getReanimateProb() {
        return GhostChance;
    }

    public int getGhostTime() {
        return ghostTime;
    }

    public int getGhostLife() {
        return ghostLife;
    }

}