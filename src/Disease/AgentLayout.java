package Disease;
import Disease.LayoutType;

/**
 * Encapsulates all parameters related to layout and configuration of Agents
 * collectively. All parameters related to individual agents are located in the
 * AgentParameters class.
 *
 */
public class AgentLayout {

    private LayoutType type;
    private int columns;
    private int rows;
    private int numAgents;
    private int initialSick = 1;
    private int initialImmune = 0;

    public AgentLayout(String layout) {
        switch (layout) {
            case "grid" -> type = LayoutType.GRID;
            case "randomgrid" -> { type = LayoutType.RANDOMGRID; }
            case "random" -> { type = LayoutType.RANDOM; }
            default -> { type = LayoutType.GRID; }
        }
    }

    public AgentLayout() {
        type = LayoutType.GRID;
    }

    public void setType(String layout) {
        switch (layout) {
            case "grid" -> { type = LayoutType.GRID; }
            case "randomgrid" -> { type = LayoutType.RANDOMGRID; }
            case "random" -> { type = LayoutType.RANDOM; }
            default -> { type = LayoutType.GRID; }
        }
    }

    public void setColumns(int width) {
        columns = width;
    }

    public void setRows(int height) {
        rows = height;
    }

    public void setNumAgents(int n) {
        numAgents = n;
    }

    public void setInitialSick(int n) { initialSick = n;}

    public void setInitialImmune(int n) {initialImmune = n;}

    public LayoutType getType() {
        return this.type;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getNumAgents() {
        return numAgents;
    }

    public int getInitialSick() {return initialSick;}

    public int getInitialImmune() {return initialImmune;}

}