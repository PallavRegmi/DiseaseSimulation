//package Disease;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class SimulationEngine {
//    private List<Agent> agents = new ArrayList<>();
//    private double infectionDistance = 10;
//    private double moveDistance = 5;
//    private double infectionProbability = 0.1;
//
//    public SimulationEngine() {
//        initializeAgents(100); // Initialized with 100 agents
//    }
//
//    private void initializeAgents(int numberOfAgents) {
//        // Initialize agents with random positions and states for the simulation
//        for (int i = 0; i < numberOfAgents; i++) {
//            double x = Math.random() * 400; // Assuming a 400x400 canvas for simplicity
//            double y = Math.random() * 400;
//            Agent.State state = i < 10 ? Agent.State.SICK : Agent.State.VULNERABLE; // First 10 agents are sick
//            agents.add(new Agent(state, x, y));
//        }
//    }
//
//    public void update() {
//        // Update agents' states and positions here
//        for (Agent agent : agents) {
//            if (agent.getState() == Agent.State.SICK) {
//                // Spread infection
//                for (Agent other : agents) {
//                    if (agent != other && agent.isWithinDistance(other, infectionDistance) && other.getState() == Agent.State.VULNERABLE) {
//                        if (Math.random() < infectionProbability) {
//                            other.setState(Agent.State.SICK);
//                        }
//                    }
//                }
//            }
//            // Move randomly
//            agent.moveRandomly(moveDistance);
//        }
//
//        // Example logic for recovering or dying, adjust as necessary
//        for (Agent agent : agents) {
//            if (agent.getState() == Agent.State.SICK && Math.random() < 0.05) { // 5% chance to recover or die
//                agent.setState(Math.random() < 0.5 ? Agent.State.IMMUNE : Agent.State.DEAD);
//            }
//        }
//    }
//
//    public Map<Agent.State, Integer> getAgentCounts() {
//        // Count the number of agents in each state
//        Map<Agent.State, Integer> counts = new HashMap<>();
//        for (Agent agent : agents) {
//            counts.putIfAbsent(agent.getState(), 0);
//            counts.put(agent.getState(), counts.get(agent.getState()) + 1);
//        }
//        return counts;
//    }
//
//    public List<Agent> getAgents() {
//        return agents;
//    }
//}
