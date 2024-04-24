package Disease;

/**
 * A class designed to facilitate the sending of messages between agents.
 */

public class MessageUpdates {
    private AgentHealthStateEnum stateOfAgent = AgentHealthStateEnum.VULNERABLE;
    private final MessageType messageType;

    public enum MessageType {
        TRANSITION,
        STOP,
    }

    public MessageUpdates(MessageType messageType) {
        this.messageType = messageType;}

    public MessageUpdates(MessageType messageType, AgentHealthStateEnum stateOfAgent) {
        this.messageType = messageType;
        this.stateOfAgent = stateOfAgent;
    }

    public AgentHealthStateEnum getStateOfAgent() {return stateOfAgent;}

    public MessageType getMessageType() {return messageType;}
}