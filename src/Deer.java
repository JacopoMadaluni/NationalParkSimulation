import java.util.List;

/**
 * A class representing deers in the simulation. It defines deer species specific
 * constants that control deers' behaviour. It also defines deer specific behaviours.
 *
 * @author Jacopo Madaluni and Luka Kralj
 * @version February 2018
 */
public class Deer extends Animal implements Herbivore, Prey, Drawable{

    // Characteristics shared by all deers (class variables).

    // The age at which a deer can start to breed.
    private static final int BREEDING_AGE = 7;
    // The age to which a deer can live.
    private static final int MAX_AGE = 100;
    // The likelihood of a deer breeding.
    private static final double BREEDING_PROBABILITY = 0.41;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single deer.
    private static final int DEER_FOOD_VALUE = 9;
    // The probability of getting sick.
    private static final double DISEASE_PROBABILITY = 0.01;
    // Max food value of a deer.
    private static final int MAX_FOOD_VALUE = 12;
    // Steps a deer needs to wait before it can breed again.
    private static final int DAYS_TO_WAIT = 4;

    /**
     * Create a new deer. A deer may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the deer will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Deer(boolean randomAge, Field field, Location location, Environment environment) {
        super(field, location, randomAge, environment);
    }


    // GETTERS:

    /**
     * @see Animal
     */
    @Override
    public int getMaxAge(){
        return MAX_AGE;
    }

    /**
     * @see Animal
     */
    @Override
    protected double getBreedingProbability(){
        return BREEDING_PROBABILITY;
    }

    /**
     * @see Animal
     */
    @Override
    protected int getBreedingAge(){
        return BREEDING_AGE;
    }

    /**
     * @see Animal
     */
    @Override
    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }

    /**
     * @see Animal
     */
    @Override
    protected int getDaysToWait(){
        return DAYS_TO_WAIT;
    }

    /**
     * @see Animal
     */
    @Override
    protected int getMaxFoodValue(){
        return MAX_FOOD_VALUE;
    }

    /**
     * @see Animal
     */
    @Override
    protected double getDiseaseProbability() {
        return DISEASE_PROBABILITY;
    }

    /**
     * @see Animal
     */
    @Override
    protected int getFoodValue(){
        return DEER_FOOD_VALUE;
    }

    /**
     * A deer will first try to find a safe location to move to and then it
     * if the safe location is not found it will try to find plants to eat.
     *
     * @see Animal
     * @see Prey
     * @see Herbivore
     */
    @Override
    protected Location findFood(){
        List<Location> locations = getSafeLocations(this);
        if (locations.size() == 0){
            return findPlants(this);
        }
        return locations.get(0);
    }


    // OTHER METHODS:

    /**
     * Simulates giving births - creates new deers in some adjacent locations.
     *
     * @param newDeers A list to return newly born deers.
     */
    protected void giveBirth(List<Actor> newDeers) {
        if (!findMate()){
            return;
        }

        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Deer young = new Deer(false, field, loc, getEnvironment());
            newDeers.add(young);
        }
    }
}


