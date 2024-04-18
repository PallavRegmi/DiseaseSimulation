package Disease;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * Centralized class responsible for setting up, managing, and ending the
 * simulation. In addition to instantiating agents at the start of the
 * simulation, the AgentManager's primary role is repeatedly determining
 * which agents are neighbors with which agents and updating agents' neighbors
 * lists accordingly. The AgentManager also tracks whether or not the simulation
 * has reached a stable state
 *
 */
public class AgentManager implements Runnable {
    private int width, height;
    private int infectDist;
    private final ArrayList<Agent> agents;

    /**
     * @param width width of simulation area in pixels
     * @param height height of simulation area in pixels
     */
    public AgentManager(int width, int height) {
        this.width = width;
        this.height = height;
        this.agents = new ArrayList<>();
    }

    public AgentManager() {
        this(200, 200);
    }

    public ArrayList<Agent> getAgents() {
        // Return a shallow copy
        return new ArrayList<>(agents);
    }

    /**
     * Instantiate a specified number of agents, at positions according to
     * layout type, with identical parameters. Also set initial immune and
     * initial sick message. Does not start the simulation.
     *
     * @param layout layout parameters for simulation
     * @param params collection of parameters for each agent
     */
    public void makeAgents(AgentLayout layout, AgentParameters params) {
        int cols = layout.getColumns();
        int rows = layout.getRows();
        infectDist = params.getInfectDistance();

        if (agents.size() > 0) {
            agents.clear();
        }

        switch (layout.getType()) {
            case GRID -> makeAgentGrid(cols, rows, params);
            case RANDOMGRID -> makeRandomAgentGrid(cols, rows,
                    layout.getNumAgents(), params);
            case RANDOM -> makeRandomAgents(layout.getNumAgents(), params);
        }

        int i = 0;

        for (; i<layout.getInitialSick() && i < layout.getNumAgents(); i++) {
            Message m = new Message(Message.MessageType.TRANSITION,
                    HealthState.SICK);
            agents.get(i).sendMessage(m);
        }

        for (; i<layout.getInitialSick() + layout.getInitialImmune() &&
                i < layout.getNumAgents(); i++) {
            System.out.println("i: " + i);
            Message m = new Message(Message.MessageType.TRANSITION,
                    HealthState.IMMUNE);
            agents.get(i).sendMessage(m);
        }
    }

    /**
     * Make grid of agents with specified number of rows and columns. Helper
     * function for makeAgents.
     *
     * @param columns number of columns
     * @param rows number of rows
     * @param params collection of parameters for each agent
     */
    private void makeAgentGrid(int columns, int rows, AgentParameters params) {
        int x, y;

        params.setWanderEnabled(false);
        params.becomeGhost(false);

        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                x = width/(columns + 1) * (j + 1);
                y = height/(rows + 1) * (i + 1);
                agents.add(new Agent(new Point2D(x, y), params));
            }
        }
    }

    /**
     * Make grid of agents with specified number of rows and columns, but with
     * a specified number of agents removed at random. Helper function for
     * makeAgents.
     *
     * @param columns number of columns
     * @param rows number of rows
     * @param numAgents number of agents remaining after removal
     * @param params collection of parameters for each agent
     */
    private void makeRandomAgentGrid(int columns, int rows, int numAgents,
                                     AgentParameters params) {
        Random r = new Random();
        int x, y;
        ArrayList<Agent> possibleAgents = new ArrayList<>();

        params.setWanderEnabled(false);
        params.becomeGhost(false);

        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                x = width/(columns + 1) * (j + 1);
                y = height/(rows + 1) * (i + 1);
                possibleAgents.add(new Agent(new Point2D(x, y), params));
            }
        }

        int idx;

        for (int i=0; i<numAgents; i++) {
            idx = r.nextInt(possibleAgents.size());
            agents.add(possibleAgents.get(idx));
            possibleAgents.remove(idx);
        }
    }

    /**
     * Make set of fixed number of ungridded agents, each of which performs a
     * random walk. Helper function for makeAgents.
     *
     * @param numAgents number of agents remaining after removal
     * @param params collection of parameters for each agent
     */
    private void makeRandomAgents(int numAgents, AgentParameters params) {
        Random r = new Random();
        int x, y;

        params.setWanderEnabled(true);

        for (int i=0; i<numAgents; i++) {
            x = (int) (r.nextDouble() * width);
            y = (int) (r.nextDouble() * height);
            agents.add(new Agent(new Point2D(x, y), params));
        }
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Start the AgentManager on dedicated thread, and start each agent's
     * thread, thereby starting the simulation.
     */
    public void start() {
        new Thread(this).start();
        for (Agent a: agents) {
            a.start();
        }
    }

    /**
     * Checks whether the simulation has reached a stable state (i.e. that no
     * further transitions of state are possible). Used to determine when to
     * end the simulation.
     *
     * @return true if no further transitions can occur; else false
     */
    public boolean isStableState() {
        HealthState hs;

        for (Agent a: agents) {
            hs = a.getHealth();
            if (!(hs == HealthState.PERMADEAD || hs == HealthState.IMMUNE ||
                    hs == HealthState.VULNERABLE)) {
                return false;
            } else if (hs == HealthState.VULNERABLE && !a.isIdle()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Repeatedly check the distances between pairs of agents, and reassign
     * neighbors based on the infection distance. Once simulation has reached
     * a stable state, send STOP message to all agents.
     */
    public void run() {
        Point2D pos;
        ArrayList<Agent> neighbors;

        while (!isStableState()) {
            for (Agent a: agents) {
                pos = a.getPosition();
                neighbors = new ArrayList<>();

                for (Agent other: agents) {
                    if (!a.equals(other) &&
                            pos.distance(other.getPosition()) < infectDist) {
                        neighbors.add(other);
                    }
                }
                a.setNeighbors(neighbors);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
        }

        for (Agent a: agents) {
            a.sendMessage(new Message(Message.MessageType.STOP));
        }

        //System.out.println("Simulation ended");
    }
}
