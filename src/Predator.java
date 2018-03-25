import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This interface contains all the common behaviours that predators share.
 * A predator can hunt determined species.
 * @author Jacopo Madaluni & Luka Kralj
 * @version 2018.02 (2)
 */
public interface Predator  extends Actor{

    /**
     * Default method to hunt and eat animals.
     * Predators will call this method instead of findFood()
     * @param animal The animal that hunts.
     * @return The location of the prey (where the predator is going to move)
     */
    default Location hunt(Animal animal)
    {
        Field field = animal.getField();
        List<Location> adjacent = field.adjacentLocations(animal.getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext() && animal.getEnvironment().getWeather().getVision() >= Randomizer.getRandom().nextDouble()) {
            Location where = it.next();
            Object an = field.getObjectAt(where);
            if(an != null && Arrays.asList(getHuntedAnimals()).contains(an.getClass())) {
                Animal prey = (Animal) an;
                if(prey.isAlive()) {
                    prey.setDead();
                    animal.setFoodLevel(prey.getFoodValue());
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * @return The list of animals that every predator specie hunts.
     */
    Class[] getHuntedAnimals();

}
