package Disease;

import static java.lang.Thread.sleep;

/**
 * A helper class designed for managing delayed transitions between health states.
 * It operates independently on its own thread.
 */

public class SlowedHealthState implements Runnable {
    private final Agent agent;
    private final AgentHealthStateEnum stateOfAgent;
    private final int time;

    /**
     * @param agent the agent whose health state is to be updated
     * @param stateOfAgent the new health state to be applied to the agent
     * @param time the delay, in milliseconds, before the health state update message is sent to the agent
     */

    public SlowedHealthState(Agent agent, AgentHealthStateEnum stateOfAgent, int time) {
        this.agent = agent;
        this.stateOfAgent = stateOfAgent;
        this.time = time;
    }

    /**
     * Pauses execution for a designated period before sending a message to the associated agent,
     * instructing it to change its health state.
     */

    public void run() {
        try {
            sleep(time);
        } catch (Exception e) {return;}

        agent.sendMessages(new MessageUpdates(MessageUpdates.MessageType.TRANSITION, stateOfAgent));
    }
}