import java.util.List;

/**
 * This interface defines all actors that can be used in the simulation.
 * By actors it is meant all the items in the simulation that change their state
 * according to certain conditions in the simulation process.
 *
 * @author Jacopo Madaluni and Luka Kralj
 * @version February 2018
 */
public interface Actor {

    /**
     * All actors need to have this method defined since they all have some kind
     * of behaviour.
     *
     * @param newActors List of all newly created actors at each step. These actors are added to
     *                  the simulation after each step.
     */
    void act(List<Actor> newActors);

    /**
     * Some actors may become inactive during the simulation for various reasons.
     * The most common reason, however, is death.
     *
     * @return True if actor is still active in the simulation.
     */
    boolean isActive();
}