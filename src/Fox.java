import java.util.*;

/**
 * A class representing foxes in the simulation. It defines fox species specific
 * constants that control foxes' behaviour. It also defines fox specific behaviours.
 *
 * @author Jacopo Madaluni and Luka Kralj
 * @version February 2018
 */
public class Fox extends Animal implements Predator, Prey, Drawable
{
    // Characteristics shared by all foxes (class variables).

    // The age at which a fox can start to breed.
    private static final int BREEDING_AGE = 6;
    // The age to which a fox can live.
    private static final int MAX_AGE = 100;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.85;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // The food value of a single fox.
    private static final int FOX_FOOD_VALUE = 7;
    // The probability of getting sick.
    private static final double DISEASE_PROBABILITY = 0.009;
    // Max food value of a fox.
    private static final int MAX_FOOD_VALUE = 11;
    // Steps a fox needs to wait before it can breed again.
    private static final int DAYS_TO_WAIT = 4;
    // List of species of animals that are hunted by foxes.
    private static final Class[] huntedAnimals = { Rabbit.class};

    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Fox(boolean randomAge, Field field, Location location, Environment environment) {
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
        return FOX_FOOD_VALUE;
    }

    /**
     * A fox will first try to find a safe location to move to and then it
     * if the safe location is not found it will try to hunt.
     *
     * @see Animal
     * @see Prey
     * @see Predator
     */
    @Override
    protected Location findFood(){
        List<Location> locations = getSafeLocations(this);
        if (locations.size() == 0){
            return hunt(this);
        }
        return locations.get(0);
    }

    /**
     * @see Predator
     */
    @Override
    public Class[] getHuntedAnimals(){
        return huntedAnimals;
    }

    /**
     * Foxes are only active during the night.
     *
     * @see Animal
     */
    @Override
    protected boolean sleeps(){
        return getEnvironment().isDay();
    }


    // OTHER METHODS:

    /**
     * Simulates giving births - creates new foxes in some adjacent locations.
     *
     * @param newFoxes A list to return newly born foxes.
     */
    protected void giveBirth(List<Actor> newFoxes) {
        if (!findMate()){
            return;
        }

        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Fox young = new Fox(false, field, loc, getEnvironment());
            newFoxes.add(young);
        }
    }
}
