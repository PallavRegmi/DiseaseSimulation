package Disease;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Agent class for plague simulation. Encapsulates all agent behavior, including
 * getting sick, wandering, and infecting other agents. Also includes death and
 * optional reanimation as ghosts.
 *
 */

public class Agent implements Runnable {
    static int agentNumber = 0;
    private HealthState health;
    private Point2D position;
    // all times specified in seconds
    private final int incubationTime, sicknessTime, reanimateTime,
            starvationTime;
    private final int speed, num;
    private final double recoveryProb, reanimateProb;
    private ArrayList<Agent> neighbors;
    private long lastWander, magicSuccessful =0, lastSeek=0;
    private final Random rand;
    private final String name;
    public final LinkedBlockingQueue<Message> messages;
    private final Object healthlock, positionlock, neighborlock;
    private final int wrapX, wrapY;
    private final boolean ghost, wander;

    /**
     * @param pos initial position of the agent
     * @param param collection of agent parameters, including incubation period,
     *              sickness length, probability of recovery, ghost parameters,
     *              etc.
     */
    public Agent(Point2D pos, AgentParameters param) {
        health = HealthState.VULNERABLE;
        position = pos;
        incubationTime = param.getIncubationPeriod();
        sicknessTime = param.getSicknessTime();
        speed = param.getSpeed();
        recoveryProb = param.getRecoveryProb();
        wander = param.getWander();

        neighbors = new ArrayList<>();
        rand = new Random();
        lastWander = System.currentTimeMillis();
        messages = new LinkedBlockingQueue();
        agentNumber += 1;
        num = agentNumber;
        name = "Agent " + agentNumber;

        healthlock = new Object();
        positionlock = new Object();
        neighborlock = new Object();

        wrapX = param.getWrapX();
        wrapY = param.getWrapY();

        ghost = param.ghostEnabled();
        reanimateProb = param.getReanimateProb();
        reanimateTime = param.getReanimateTime();
        starvationTime = param.getStarvationTime();
    }

    public Point2D getPosition() {
        synchronized (positionlock) {
            return position;
        }
    }

    public HealthState getHealth() {
        synchronized (healthlock) {
            return health;
        }
    }

    public String getName() {
        return name;
    }

    public int getNumber() {return num;}

    private void setHealth(HealthState health) {
        synchronized (healthlock) {
            this.health = health;
        }
    }

    public void setNeighbors(ArrayList<Agent> neighbors) {
        synchronized (neighborlock) {
            this.neighbors = neighbors;
        }
    }

    /**
     * Send a message to this agent requesting that it either transition to
     * another health state or stop its thread. Certain health states may cause
     * the agent to create a health state timer  (DelayedHealtstate) on a new
     * thread.
     *
     * @param msg requested action
     */
    public void sendMessage(Message msg) {
        try {
            messages.put(msg);
        } catch (InterruptedException e) {};
    }

    /**
     * Send a message to each of this agent's current neighbors requesting that
     * they transition to the INCUBATING state.
     */
    private void infectNeighbors() {
        synchronized (neighborlock) {
            for (Agent a : neighbors) {
                if (a.getHealth() == HealthState.VULNERABLE) {
                    a.sendMessage(new Message(Message.MessageType.TRANSITION,
                            HealthState.INCUBATING));
                }
            }
        }
    }

    /**
     * Move in a random direction with distance proportional to the amount of
     * time that has passed since the last time wander was called.
     *
     * @param now time in ms at which wander is called
     * @param speed movement speed in pixels per second
     */
    private void wander(long now, int speed) {
        double secsSinceCheck = (now - lastWander) / 1000.0;
        double dir = 2 * Math.PI * rand.nextDouble();

        double x = speed * secsSinceCheck * Math.cos(dir);
        double y = speed * secsSinceCheck * Math.sin(dir);

        synchronized (positionlock) {

            x += this.position.getX();
            y += this.position.getY();

            // Wrap positions horizontally and vertically when agents leave
            // simulation area
            while (x > wrapX) {
                x -= wrapX;
            }
            while (x < 0) {
                x += wrapX;
            }

            while (y > wrapY) {
                y -= wrapY;
            }
            while (y < 0) {
                y += wrapY;
            }

            this.position = new Point2D(x, y);
        }

        lastWander = now;
    }

    /**
     * Begin the agent's life, which involves repeatedly wandering, checking
     * and responding to new messages, and infecting neighbors (if the agent is
     * sick). Also includes potential reanimation as ghost if enabled. The loop
     * ends when the agent's health state transitions to PERMADEAD.
     */
    public void run() {
        Message msg;
        int secsSinceAte;

        while (this.getHealth() != HealthState.PERMADEAD) {
            if (this.getHealth() == HealthState.GHOST) {
                secsSinceAte = (int) ((System.currentTimeMillis()
                        - magicSuccessful) / 1000);
                if (secsSinceAte >= starvationTime) {
                    setHealth(HealthState.PERMADEAD);
                } else {
                    blackMagic(System.currentTimeMillis(), (int) (0.25 * speed),
                            getClosestNeighbor());
                }
            } else if (wander && getHealth() != HealthState.DEAD) {
                wander(System.currentTimeMillis(), speed);
            }

            if (getHealth() == HealthState.SICK) {
                infectNeighbors();
            }

            try {
                msg = messages.poll(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {continue;}

            if (msg == null) {
                continue;
            }

            switch (msg.getType()) {
                case TRANSITION:
                    switch (msg.getState()) {
                        case INCUBATING:
                            if (getHealth() == HealthState.VULNERABLE) {
                                new Thread(new DelayedHealthstate(this,
                                        HealthState.SICK,
                                        incubationTime * 1000)).start();
                                setHealth(msg.getState());
                            }
                            break;
                        case SICK:
                            if (getHealth() == HealthState.VULNERABLE ||
                                    getHealth() == HealthState.INCUBATING) {
                                if (rand.nextDouble() < recoveryProb) {
                                    new Thread(new DelayedHealthstate(this,
                                            HealthState.IMMUNE,
                                            sicknessTime * 1000)).start();
                                } else {
                                    new Thread(new DelayedHealthstate(this,
                                            HealthState.DEAD,
                                            sicknessTime * 1000)).start();
                                }
                                setHealth(msg.getState());
                            }
                            break;
                        case DEAD:
                            if (ghost) {
                                setHealth(HealthState.DEAD);

                                if (rand.nextDouble() < reanimateProb) {
                                    new Thread(new DelayedHealthstate(this,
                                            HealthState.GHOST,
                                            reanimateTime*1000)).start();
                                } else {
                                    setHealth(HealthState.PERMADEAD);
                                }
                            } else {
                                setHealth(HealthState.PERMADEAD);
                            }
                            break;
                        case GHOST:
                            magicSuccessful = System.currentTimeMillis();
                            setHealth(msg.getState());
                            break;
                        default:
                            setHealth(msg.getState());
                            break;
                    }
                    break;
                case STOP:
                    return;
            }
        }
    }

    /**
     * Get the neighbor presently closest to the current agent, ignoring agents
     * in DEAD, PERMADEAD, or ghost states. Helper function for isBlackMagic.
     *
     * @return closest agent
     */
    private Agent getClosestNeighbor() {
        HealthState hs;
        Agent closestAgt = null;
        double dist, closestDist = -1;

        synchronized (neighborlock) {
            for (Agent a : neighbors) {
                hs = a.getHealth();
                if (!(hs == HealthState.DEAD || hs == HealthState.PERMADEAD ||
                        hs == HealthState.GHOST)) {
                    dist = position.distance(a.getPosition());
                    if (closestAgt == null || dist < closestDist) {
                        closestAgt = a;
                        closestDist = dist;
                    }
                }
            }

            return closestAgt;
        }
    }

    /**
     * Move towards the closest agent not in DEAD, PERMADEAD, or ghost state.
     * If the agent "catches" the target (i.e. comes within 10 pixels), kill
     * the target agent, which has a chance of then transition to ghost state.
     *
     * @param now time in ms at which isBlackMagic is called
     * @param speed
     * @param closestNeighbor
     */
    private void blackMagic(long now, int speed, Agent closestNeighbor) {
        if (lastSeek == 0) {
            lastSeek = now;
        }

        if (closestNeighbor == null) {
            lastSeek = now;
            return;
        }

        synchronized (positionlock) {
            Point2D heading;
            heading = closestNeighbor.getPosition().subtract(getPosition());
            double secsSinceCheck = (now - this.lastSeek) / 1000.0;
            heading = heading.normalize().multiply(speed * secsSinceCheck);
            position = position.add(heading);

            if (position.distance(closestNeighbor.getPosition()) < 10) {
                Message m = new Message(Message.MessageType.TRANSITION,
                        HealthState.DEAD);
                closestNeighbor.sendMessage(m);
                magicSuccessful = now;
            }
        }

        lastSeek = now;
    }

    public boolean isIdle() {
        return messages.isEmpty();
    }

    /**
     * Convenience method for running agent without explicitly wrapping it in
     * a new Thread object. Idiom borrowed from Brooke Chenoweth Creel
     */
    public void start() {
        // Idiom borrowed from Brooke
        new Thread(this).start();
    }

}