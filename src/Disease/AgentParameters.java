package Disease;

/**
 * Encapsulates all parameters related to individual agents. All parameters
 * related to the collection of agents as a whole are located in the
 * AgentLayout class.
 **/
public class AgentParameters {
    // all times in seconds
    private int incubationPeriod, sicknessTime, reanimateTime, starvationTime;
    private int infectDistance;
    private double recoveryProbability, reanimateProbability;
    private int wrapX, wrapY;
    private int initialSick, initialImmune;
    private int speed;
    private boolean wander, zombies;

    /**
     * Set default values for all parameters, according to project
     * specifications.
     */
    public AgentParameters() {
        incubationPeriod = 5;
        sicknessTime = 10;
        recoveryProbability = 0.95;
        speed = 100;
        infectDistance = 20;
        wrapX = 500;
        wrapY = 500;
        wander = false;

        // Zombie parameters
        zombies = false;
        reanimateTime = 10;
        reanimateProbability = 0;
        starvationTime  = 10;
    }

    public void setIncubationPeriod(int t) {incubationPeriod = t;}

    public void setSicknessTime(int t) {sicknessTime = t;}

    public void setRecoveryProbability(double p) {recoveryProbability = p;}

    public void setWrapX(int x) {wrapX = x;}

    public void setWrapY(int y) {wrapY = y;}

    public void setInitialSick(int n) {initialSick = n;}

    public void setInitialImmune(int n) {initialImmune = n;}

    public void setInfectDistance(int dist) {infectDistance = dist;}

    public void setSpeed(int speed) {this.speed = speed;}

    public void setWanderEnabled(boolean wander) {this.wander = wander;}

    public void becomeGhost(boolean zombies) {this.zombies = zombies;}

    public void setReanimateProb(double p) {reanimateProbability = p;}

    public void setReanimateTime(int t) {reanimateTime = t;}

    public void setStarvationTime(int t) {starvationTime = t;}

    public int getIncubationPeriod() {return incubationPeriod;}

    public int getInitialSick() {return initialSick;}

    public int getInitialImmune() {return initialImmune;}

    public int getSicknessTime() {return sicknessTime;}

    public double getRecoveryProb() {return recoveryProbability;}

    public int getInfectDistance() {return infectDistance;}

    public int getSpeed() {return speed;}

    public int getWrapX() {return wrapX;}

    public int getWrapY() {return wrapY;}

    public boolean getWander() {return wander;}

    public boolean ghostEnabled() {return zombies;}

    public double getReanimateProb() {return reanimateProbability;}

    public int getReanimateTime() {return reanimateTime;}

    public int getStarvationTime() {return starvationTime;}

}