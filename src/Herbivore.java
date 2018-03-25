import java.util.Iterator;
import java.util.List;


/**
 * The Interface of an Herbivore animal.
 * @author Jacopo Madaluni & Luka Kralj
 * @version 2018.02 (2)
 */
public interface Herbivore extends Actor {

    /**
     * Default method to eat plands around an herbivore location.
     * @param animal
     * @return The location of the plant to eat. (null if there is no plant)
     */
    default Location findPlants(Animal animal){
        Field field = animal.getField();
        List<Location> adjacent = field.adjacentLocations(animal.getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object an = field.getObjectAt(where);
            if(an != null && an instanceof Plant) {
                Plant plant = (Plant) an;
                if(plant.isActive()) {
                    plant.setDead();
                    animal.setFoodLevel(plant.getFoodValue());
                    return where;
                }
            }
        }
        return null;

    }
}
