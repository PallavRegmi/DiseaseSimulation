/***************************************************
 *CS351: Section 3SW
 *Project 4 - Disease Simulation Project
 *Authors : Ashmit Agrawal & Pallav Regmi
 *University of New Mexico - School of Engineering
 ***************************************************/

package Disease;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Agent class for a disease simulation. This class simulates the behavior
 * of an individual within a population during a disease outbreak, including
 * movement, infection, and potential death and ghost reanimation.
 */

public class Agent implements Runnable {
    private AgentHealthStateEnum agentHealthStateEnum;
    private long lastVisit, magicSuccessfulTime = 0, lastSeekTime = 0;

    private final int velocity;
    private final double recoveryP, reanimateP;
    private ArrayList<Agent> neighborList;
    private Point2D pointPosition;

    private final Random random;
    public final LinkedBlockingQueue<MessageUpdates> messageUpdatesLinkedBlockingQueue;
    private final Object lockHealthObject, lockPositionObject, lockNeighbourObject;
    private final int wrapXAxis, wrapYAxis;

    // all time periods stored in seconds.
    private final int incubationTimePeriod, sicknessTimePeriod, reanimateTimePeriod,
            starvationTimePeriod;
    private final boolean human, visit;
    static int agentNum = 0;


    /**
     * Constructs an Agent with a starting position and various disease parameters.
     * @param point2D         The starting location of the agent in the simulation area.
     * @param agentParameters A set of parameters defining agent behavior, such as disease incubation and infectious periods, probabilities of recovery and ghost states, etc.
     */
    public Agent(Point2D point2D, AgentParameters agentParameters) {
        agentHealthStateEnum = AgentHealthStateEnum.VULNERABLE;
        pointPosition = point2D;
        incubationTimePeriod = agentParameters.getIncTime();
        sicknessTimePeriod = agentParameters.getSickTime();
        velocity = agentParameters.getSpeed();
        recoveryP = agentParameters.getRecoveryProb();
        visit = agentParameters.getHaunt();

        neighborList = new ArrayList<>();
        random = new Random();
        lastVisit = System.currentTimeMillis();
        messageUpdatesLinkedBlockingQueue = new LinkedBlockingQueue<>();
        agentNum += 1;

        lockHealthObject = new Object();
        lockPositionObject = new Object();
        lockNeighbourObject = new Object();

        wrapXAxis = agentParameters.getX();
        wrapYAxis = agentParameters.getY();

        human = agentParameters.ghostEnabled();
        reanimateP = agentParameters.getReanimateProb();
        reanimateTimePeriod = agentParameters.getGhostTime();
        starvationTimePeriod = agentParameters.getGhostLife();
    }

    public boolean isSteady() {
        return messageUpdatesLinkedBlockingQueue.isEmpty();
    }

    public Point2D getPointOfPosition() {
        synchronized (lockPositionObject) {
            return pointPosition;
        }
    }

    private void setHealthStateOfAgent(AgentHealthStateEnum agentHealthStateEnum) {
        synchronized (lockHealthObject) {
            this.agentHealthStateEnum = agentHealthStateEnum;
        }
    }

    public AgentHealthStateEnum getHealthStateOfAgent() {
        synchronized (lockHealthObject) {
            return agentHealthStateEnum;
        }
    }


    public void setNeighborLists(ArrayList<Agent> neighborList) {
        synchronized (lockNeighbourObject) {
            this.neighborList = neighborList;
        }
    }


    /**
     * This method sends a request to all current neighbors of this agent, asking them
     * to transition to the INCUBATING state.
     */

    private void affectNeighbour() {
        synchronized (lockNeighbourObject) {
            for (Agent a : neighborList) {
                if (a.getHealthStateOfAgent() == AgentHealthStateEnum.VULNERABLE) {
                    a.sendMessages(new MessageUpdates(MessageUpdates.MessageType.TRANSITION,
                            AgentHealthStateEnum.INCUBATING));
                }
            }
        }
    }


    /**
     * Moves the agent randomly, basing the distance on how long it's been since we last roamed around.
     * Like a spontaneous stroll that varies in length depending on the time elapsed.
     *
     * @param present the timestamp in milliseconds when this wanderlust hits
     * @param velocity how fast we're going in pixels per second
     */

    private void visit(long present, int velocity) {
        double timeSinceLastVisit = (present - lastVisit) / 1000.0;
        double directions = 2 * Math.PI * random.nextDouble();

        double x = velocity * timeSinceLastVisit * Math.cos(directions);
        double y = velocity * timeSinceLastVisit * Math.sin(directions);

        synchronized (lockPositionObject) {

            x += this.pointPosition.getX();
            y += this.pointPosition.getY();

            // Ensures agents wrap around to the opposite side of the simulation area
            // both horizontally and vertically when they exit the boundaries.

            while (x > wrapXAxis) {
                x -= wrapXAxis;
            }
            while (x < 0) {
                x += wrapXAxis;
            }

            while (y > wrapYAxis) {
                y -= wrapYAxis;
            }
            while (y < 0) {
                y += wrapYAxis;
            }

            this.pointPosition = new Point2D(x, y);
        }

        lastVisit = present;
    }


    /**
     * Finds the nearest neighbor to the current agent, skipping any that are in DEAD,
     * PERMADEAD, or ghost states. This helper function is used primarily for checking
     * conditions related to isBlackMagic.
     *
     * @return the agent closest to the current one
     */

    private Agent getNearestNeighbor() {
        AgentHealthStateEnum stateOfAgent;
        Agent nearestAgt = null;
        double distancr, nearestDistance = -1;

        synchronized (lockNeighbourObject) {
            for (Agent agent : neighborList) {
                stateOfAgent = agent.getHealthStateOfAgent();
                if (!(stateOfAgent == AgentHealthStateEnum.DEAD || stateOfAgent == AgentHealthStateEnum.PERMADEAD ||
                        stateOfAgent == AgentHealthStateEnum.GHOST)) {
                    distancr = pointPosition.distance(agent.getPointOfPosition());
                    if (nearestAgt == null || distancr < nearestDistance) {
                        nearestAgt = agent;
                        nearestDistance = distancr;
                    }
                }
            }

            return nearestAgt;
        }
    }

    /**
     * Directs this agent to approach the nearest living agent, avoiding those in DEAD,
     * PERMADEAD, or ghost states. If the agent closes within 10 pixels of its target,
     * it eliminates the target, which transition to a ghost state.
     *
     * @param present the current time in milliseconds when isBlackMagic is invoked
     */

    private void darkMagic(long present, int velocity, Agent nearestNeighbour) {
        if (lastSeekTime == 0) {
            lastSeekTime = present;
        }

        if (nearestNeighbour == null) {
            lastSeekTime = present;
            return;
        }

        synchronized (lockPositionObject) {
            Point2D point2DHeading;
            point2DHeading = nearestNeighbour.getPointOfPosition().subtract(getPointOfPosition());
            double secsSinceCheck = (present - this.lastSeekTime) / 1000.0;
            point2DHeading = point2DHeading.normalize().multiply(velocity * secsSinceCheck);
            pointPosition = pointPosition.add(point2DHeading);

            if (pointPosition.distance(nearestNeighbour.getPointOfPosition()) < 10) {
                MessageUpdates m = new MessageUpdates(MessageUpdates.MessageType.TRANSITION,
                        AgentHealthStateEnum.DEAD);
                nearestNeighbour.sendMessages(m);
                magicSuccessfulTime = present;
            }
        }

        lastSeekTime = present;
    }

    /**
     * Starts the agent's life cycle, where it wanders around, stays on top of incoming messages,
     * and potentially infects neighbors if it's sick. The agent comes back as a ghost. This whole
     * routine keeps going until the agent hits the PERMADEAD state, at which
     * point it's really, truly game over.
     */

    public void run() {
        MessageUpdates messages;
        int timeSinceAte;

        while (this.getHealthStateOfAgent() != AgentHealthStateEnum.PERMADEAD) {
            if (this.getHealthStateOfAgent() == AgentHealthStateEnum.GHOST) {
                timeSinceAte = (int) ((System.currentTimeMillis()
                        - magicSuccessfulTime) / 1000);
                if (timeSinceAte >= starvationTimePeriod) {
                    setHealthStateOfAgent(AgentHealthStateEnum.PERMADEAD);
                } else {
                    darkMagic(System.currentTimeMillis(), (int) (0.25 * velocity),
                            getNearestNeighbor());
                }
            } else if (visit && getHealthStateOfAgent() != AgentHealthStateEnum.DEAD) {
                visit(System.currentTimeMillis(), velocity);
            }

            if (getHealthStateOfAgent() == AgentHealthStateEnum.SICK) {
                affectNeighbour();
            }

            try {
                messages = messageUpdatesLinkedBlockingQueue.poll(100, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                continue;
            }

            if (messages == null) {
                continue;
            }

            switch (messages.getMessageType()) {
                case TRANSITION:
                    switch (messages.getStateOfAgent()) {
                        case INCUBATING:
                            if (getHealthStateOfAgent() == AgentHealthStateEnum.VULNERABLE) {
                                new Thread(new SlowedHealthState(this,
                                        AgentHealthStateEnum.SICK,
                                        incubationTimePeriod * 1000)).start();
                                setHealthStateOfAgent(messages.getStateOfAgent());
                            }
                            break;
                        case SICK:
                            if (getHealthStateOfAgent() == AgentHealthStateEnum.VULNERABLE ||
                                    getHealthStateOfAgent() == AgentHealthStateEnum.INCUBATING) {
                                if (random.nextDouble() < recoveryP) {
                                    new Thread(new SlowedHealthState(this,
                                            AgentHealthStateEnum.IMMUNE,
                                            sicknessTimePeriod * 1000)).start();
                                } else {
                                    new Thread(new SlowedHealthState(this,
                                            AgentHealthStateEnum.DEAD,
                                            sicknessTimePeriod * 1000)).start();
                                }
                                setHealthStateOfAgent(messages.getStateOfAgent());
                            }
                            break;
                        case DEAD:
                            if (human) {
                                setHealthStateOfAgent(AgentHealthStateEnum.DEAD);

                                if (random.nextDouble() < reanimateP) {
                                    new Thread(new SlowedHealthState(this,
                                            AgentHealthStateEnum.GHOST,
                                            reanimateTimePeriod * 1000)).start();
                                } else {
                                    setHealthStateOfAgent(AgentHealthStateEnum.PERMADEAD);
                                }
                            } else {
                                setHealthStateOfAgent(AgentHealthStateEnum.PERMADEAD);
                            }
                            break;
                        case GHOST:
                            magicSuccessfulTime = System.currentTimeMillis();
                            setHealthStateOfAgent(messages.getStateOfAgent());
                            break;
                        default:
                            setHealthStateOfAgent(messages.getStateOfAgent());
                            break;
                    }
                    break;
                case STOP:
                    return;
            }
        }
    }


    public void start() {
        new Thread(this).start();
    }


    /**
     * Sends a directive to this agent, asking it to either switch health states or halt its thread.
     * Depending on the health state it transitions to, the agent also kicks off a
     * DelayedHealthState timer on a separate thread to manage the timing of state changes.
     *
     * @param messageUpdates the action being requested of the agent
     */

    public void sendMessages(MessageUpdates messageUpdates) {
        try {
            messageUpdatesLinkedBlockingQueue.put(messageUpdates);
        } catch (Exception e) {
        }

    }
}