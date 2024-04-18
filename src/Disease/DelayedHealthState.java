package Disease;

import static java.lang.Thread.sleep;

/**
 * Lightweight helper class for delayed transition between health states. Runs
 * on its own thread.
 */
public class DelayedHealthState implements Runnable {
    private final Agent agt;
    private final HealthState hs;
    private final int ms;

    /**
     * @param agt agent whose health state will be updated
     * @param hs new health state
     * @param ms delay before sending health state message to agent, in ms
     */
    public DelayedHealthState(Agent agt, HealthState hs, int ms) {
        this.agt = agt;
        this.hs = hs;
        this.ms = ms;
    }

    /**
     * Sleeps for the specified amount of time, then sends a message to the
     * associated agent requesting that it change its health state
     */
    public void run() {
        try {
            sleep(ms);
        } catch (InterruptedException e) {return;}

        agt.sendMessage(new Message(Message.MessageType.TRANSITION, hs));
    }
}