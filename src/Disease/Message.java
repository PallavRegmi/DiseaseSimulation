package Disease;

/**
 * Simple class for sending messages between agents.
 *
 */
public class Message {
    private HealthState state = HealthState.VULNERABLE;
    private MessageType type;

    public enum MessageType {
        TRANSITION,
        STOP,
    }

    public Message(MessageType t) {type = t;}

    public Message(MessageType t, HealthState hs) {
        type = t;
        state = hs;
    }

    public HealthState getState() {return state;}

    public MessageType getType() {return type;}
}