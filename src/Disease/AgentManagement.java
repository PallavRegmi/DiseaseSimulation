package Disease;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Random;

/**
 * This centralized class handles the setup, management, and termination of the simulation.
 * It begins by creating agents and primarily focuses on continuously identifying neighboring
 * agents, updating their neighbor lists as needed. The AgentManager also monitors the simulation
 * to determine if it has reached a stable state.
 */

public class AgentManagement implements Runnable {
    private final ArrayList<Agent> agentList;
    private int distanceInfection;
    private int breadth, depth;


    /**
     * @param breadth the width of the simulation area in pixels
     * @param depth the height of the simulation area in pixels
     */

    public AgentManagement(int breadth, int depth) {
        this.breadth = breadth;
        this.depth = depth;
        this.agentList = new ArrayList<>();
    }

    public AgentManagement() {
        this(200, 200);
    }

    public ArrayList<Agent> getAgentList() {
        // Return a shallow copy
        return new ArrayList<>(agentList);
    }

    /**
     * Creates a specified number of agents, positioning them based on the agentDetail type and
     * assigning the same parameters to each. Additionally, it sets the initial immune and
     * initial sick messages.
     *
     * @param agentDetail configuration specifying the parameters for agent placement in the simulation
     * @param agentParameter collection of common parameters applied to each agent
     */

    public void createAgents(AgentInfoLayout agentDetail, AgentParameters agentParameter) {
        int columns = agentDetail.getCols();
        int rows = agentDetail.getRws();
        distanceInfection = agentParameter.getVulnDist()
        ;

        if (agentList.size() > 0) {
            agentList.clear();
        }

        switch (agentDetail.getLayoutType()) {
            case GRID -> makeAgentGridChart(columns, rows, agentParameter);
            case RANDOMGRID -> makeRandomAgentGridChart(columns, rows,
                    agentDetail.getAgentNumber(), agentParameter);
            case RANDOM -> createRandomAgents(agentDetail.getAgentNumber(), agentParameter);
        }


        for (int i = 0; i < agentDetail.getInitSick() && i < agentDetail.getAgentNumber(); i++) {
            MessageUpdates messages = new MessageUpdates(MessageUpdates.MessageType.TRANSITION,
                    AgentHealthStateEnum.SICK);
            agentList.get(i).sendMessages(messages);
        }

        for (int i = 0; i < agentDetail.getInitSick() + agentDetail.getInitImmune() &&
                i < agentDetail.getAgentNumber(); i++) {
            System.out.printf("i: %d \n ", i);
            MessageUpdates messages = new MessageUpdates(MessageUpdates.MessageType.TRANSITION,
                    AgentHealthStateEnum.IMMUNE);
            agentList.get(i).sendMessages(messages);
        }
    }

    /**
     * Constructs a grid of agents arranged into specified numbers of horizontal and vertical lines,
     * but randomly removes a certain number of agents to achieve a desired total count. This method
     * serves as a helper function for makeAgents to set up an initially populated but partially
     * thinned-out grid.
     *
     * @param verticalLine     the number of vertical lines in the grid
     * @param horizontalLine   the number of horizontal lines in the grid
     * @param agent            collection of parameters for each agent
     */

    private void makeAgentGridChart(int verticalLine, int horizontalLine, AgentParameters agent) {
        int a, b;

        agent.setWanderEnabled(false);
        agent.becomeGhost(false);
        int i;
        for (i = 0; i < horizontalLine; i++) {
            for (int j = 0; j < verticalLine; j++) {
                a = breadth / (verticalLine + 1) * (j + 1);
                b = depth / (horizontalLine + 1) * (i + 1);
                agentList.add(new Agent(new Point2D(a, b), agent));
            }
        }
    }

    /**
     * Constructs a grid of agents arranged into specified numbers of horizontal and vertical lines,
     * but with a specified number of agents randomly removed to achieve the desired count. This
     * function serves as a helper for makeAgents, setting up an initial agent grid that's partially
     * depleted.
     *
     * @param verticalLine     the number of vertical lines in the grid
     * @param horizontalLine   the number of horizontal lines in the grid
     * @param agentNum         the target number of agents remaining after random removal
     * @param agent            collection of parameters for each agent
     */

    private void makeRandomAgentGridChart(int verticalLine, int horizontalLine, int agentNum,
                                          AgentParameters agent) {
        Random random = new Random();
        int a, b;
        ArrayList<Agent> possibleAgentList = new ArrayList<>();

        agent.setWanderEnabled(false);
        agent.becomeGhost(false);
        int i;
        for (i = 0; i < horizontalLine; i++) {
            for (int j = 0; j < verticalLine; j++) {
                a = breadth / (verticalLine + 1) * (j + 1);
                b = depth / (horizontalLine + 1) * (i + 1);
                possibleAgentList.add(new Agent(new Point2D(a, b), agent));
            }
        }

        int num;
        int j;
        for (j = 0; j < agentNum; j++) {
            num = random.nextInt(possibleAgentList.size());
            agentList.add(possibleAgentList.get(num));
            possibleAgentList.remove(num);
        }
    }

    /**
     * Creates a specified number of ungridded agents, each programmed to perform a random walk.
     * This method is a helper function for makeAgents, designed to initialize agents that do not
     * adhere to a grid layout.
     *
     * @param agentNum the number of agents to be created
     * @param agent    collection of parameters for each agent
     */

    private void createRandomAgents(int agentNum, AgentParameters agent) {
        Random random = new Random();
        int a, b;

        agent.setWanderEnabled(true);
        int j;
        for (j = 0; j < agentNum; j++) {
            a = (int) (random.nextDouble() * breadth);
            b = (int) (random.nextDouble() * depth);
            agentList.add(new Agent(new Point2D(a, b), agent));
        }
    }

    public void setProportions(int breadth, int width) {
        this.breadth = breadth;
        this.depth = width;
    }

    /**
     * Launches the AgentManager on its own dedicated thread and starts the threads for each agent,
     * effectively kicking off the simulation.
     */

    public void start() {
        new Thread(this).start();
        for (Agent agent : agentList) {
            agent.start();
        }
    }

    /**
     * Evaluates if the simulation has settled into a stable state,
     * meaning no more state transitions are possible.
     * This check is crucial for deciding when to conclude the simulation.
     *
     * @return true if the simulation is stable and no further transitions are expected, otherwise false
     */

    public boolean isInStableState() {
        AgentHealthStateEnum stateOfAgent;

        for (Agent agent : agentList) {
            stateOfAgent = agent.getHealthStateOfAgent();
            if (!(stateOfAgent == AgentHealthStateEnum.PERMADEAD || stateOfAgent == AgentHealthStateEnum.IMMUNE ||
                    stateOfAgent == AgentHealthStateEnum.VULNERABLE)) {
                return false;
            } else if (stateOfAgent == AgentHealthStateEnum.VULNERABLE && !agent.isSteady()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Continuously monitors the distances between pairs of agents, updating their neighbor assignments
     * based on a predefined infection distance. Once the simulation stabilizes and no further changes
     * are expected, it sends a STOP message to all agents to end their activity.
     */

    public void run() {
        Point2D point2D;
        ArrayList<Agent> neighborList;

        while (!isInStableState()) {
            for (Agent agent : agentList) {
                point2D = agent.getPointOfPosition();
                neighborList = new ArrayList<>();

                for (Agent otherAgents : agentList) {
                    if (!agent.equals(otherAgents) &&
                            point2D.distance(otherAgents.getPointOfPosition()) < distanceInfection) {
                        neighborList.add(otherAgents);
                    }
                }
                agent.setNeighborLists(neighborList);
            }
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }

        for (Agent agent : agentList) {
            agent.sendMessages(new MessageUpdates(MessageUpdates.MessageType.STOP));
        }

    }
}