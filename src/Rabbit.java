import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, eat plants, and die.
 * 
 * @author David J. Barnes and Michael Kölling, Jacopo Madaluni & Luka Kralj
 * @version 2018.02 (2)
 */
public class Rabbit extends Animal implements Prey, Herbivore, Drawable
{
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 100;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.80; //default 0.12
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;

    private static final int RABBIT_FOOD_VALUE = 2;

    private static final double DISEASE_PROBABILITY = 0.01;

    private static final int MAX_FOOD_VALUE = 10;

    private static final int DAYS_TO_WAIT = 6;
    // A shared random number generator to control breeding.
    //private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param environment The environment the new rabbit is put
     */
    public Rabbit(boolean randomAge, Field field, Location location, Environment environment)
    {
        super(field, location, randomAge, environment);
    
    }


    /**
     * All the following Overrided methods are described in the animal class.
     */


    @Override
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    @Override
    protected double getBreedingProbability(){
        return BREEDING_PROBABILITY;
    }
    @Override
    protected int getBreedingAge(){
        return BREEDING_AGE;
    }
    @Override
    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }
    @Override
    protected int getDaysToWait(){
        return DAYS_TO_WAIT;
    }
    @Override
    protected int getFoodValue(){
        return RABBIT_FOOD_VALUE;
    }
    @Override
    protected int getMaxFoodValue(){
        return MAX_FOOD_VALUE;
    }
    @Override
    protected double getDiseaseProbability(){ return DISEASE_PROBABILITY; }

    /**
     * @see Herbivore
     * Find plants is described in the Herbivore interface
     */
    @Override
    protected Location findFood(){
        return findPlants(this);
    }

    
    /**
     * This rabbit gives births to new rabbits.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    protected void giveBirth(List<Actor> newRabbits)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        if (!findMate()){
            return;
        }
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Rabbit young = new Rabbit(false, field, loc, getEnvironment());
            newRabbits.add(young);
        }
    }

}
