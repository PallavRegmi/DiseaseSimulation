package Disease;
public class Agent {
    public enum State { VULNERABLE, SICK, IMMUNE, DEAD }

    private State state;
    private double x, y; // Position

    public Agent(State state, double x, double y) {
        this.state = state;
        this.x = x;
        this.y = y;
    }

    // Getters and setters
    public State getState() { return state; }
    public void setState(State state) { this.state = state; }
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    // Check if another agent is within exposure distance
    public boolean isWithinDistance(Agent other, double distance) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy) <= distance;
    }

    // Move the agent randomly
    public void moveRandomly(double distance) {
        double angle = Math.random() * 2 * Math.PI;
        this.x += Math.cos(angle) * distance;
        this.y += Math.sin(angle) * distance;
    }
}
