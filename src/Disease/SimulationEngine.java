package Disease;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationEngine {
    private List<Agent> agents = new ArrayList<>();
    private double exposureDistance = 20;

    public SimulationEngine() {

        agents.add(new Agent(Agent.State.SICK, 100, 100));
        for (int i = 1; i <= 100; i++) {
            agents.add(new Agent(Agent.State.VULNERABLE, Math.random() * 200, Math.random() * 200));
        }
    }

    public void update() {
        // Example update - you might include more logic for state changes and interactions
        agents.forEach(agent -> {
            if (agent.getState() != Agent.State.DEAD) {
                agent.moveRandomly(5); // Move agents randomly
            }
        });

        // Here, implement your logic for checking if agents become sick, recover, etc.
    }

    public List<Agent> getAgents() {
        return agents;
    }
}
