/**
 * This interface defines all drawable objects in the simulation.
 * If we want the object to be drawable we need to have access to its field and
 * also its location on that field.
 *
 * @author Jacopo Madaluni and Luka Kralj
 * @version February 2018
 */
public interface Drawable {

    /**
     *
     * @return The field of the drawable object.
     */
    Field getField();

    /**
     *
     * @return Location of the drawable object.
     */
    Location getLocation();

    /**
     * Place the drawable object to new location.
     * @param location Location to witch we want to place the drawable object.
     */
    void setLocation(Location location);
}
