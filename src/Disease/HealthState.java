package Disease;

/**
 * Possible states of agent health.
 **/
public enum HealthState {
    VULNERABLE,
    INCUBATING,
    SICK,
    IMMUNE,
    DEAD, // still may transition to ZOMBIE
    GHOST,
    PERMADEAD // cannot transition to ZOMBIE
}