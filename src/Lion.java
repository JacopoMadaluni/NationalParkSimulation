import java.util.List;


/**
 * A simple model of a lion.
 * Lions age, move, hunt other animals, breed, and die.
 * They have all functionality of Animals and Predators
 *
 * @author Jacopo Madaluni & Luka Kralj
 * @version 2018.02 (2)
 */
public class Lion extends Animal implements Predator, Drawable  {

    // Characteristics shared by all foxes (class variables).

    // The age at which a fox can start to breed.
    private static final int BREEDING_AGE = 8;
    // The age to which a fox can live.
    private static final int MAX_AGE = 250;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.5; //0,08 default
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;  //2 default
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int LION_FOOD_VALUE = 20;

    private static final double DISEASE_PROBABILITY = 0.001;

    private static final int MAX_FOOD_VALUE = 13;

    private static final int DAYS_TO_WAIT = 5;

    private static final Class[] huntedAnimals = { Rabbit.class, Fox.class, Deer.class };
    // A shared random number generator to control breeding.
    //private static final Random rand = Randomizer.getRandom();

    // The fox's food level, which is increased by eating rabbits.


    /**
     * Create a lion. A lion can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param environment The environment the new lion is put.
     */
    public Lion(boolean randomAge, Field field, Location location, Environment environment)
    {
        super(field, location, randomAge, environment);

    }



    /**
     * All the following overrided methods are described in the Animal Class.
     *
     */
    /**
     * @return The max age of a lion.
     */
    @Override
    public int getMaxAge(){
        return MAX_AGE;
    }
    /**
     * @return The breeding probablity of a lion.
     */
    @Override
    protected double getBreedingProbability(){
        return BREEDING_PROBABILITY;
    }
    /**
     * @return The breeding age of a lion.
     */
    @Override
    protected int getBreedingAge(){
        return BREEDING_AGE;
    }
    /**
     * @return The max litter size of a lion.
     */
    @Override
    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }
    /**
     * @return The days to wait after breeding to breed again (applies to females only).
     */
    @Override
    protected int getDaysToWait(){
        return DAYS_TO_WAIT;
    }
    /**
     * @return The maximum food value of a lion.
     */
    @Override
    protected int getMaxFoodValue(){
        return MAX_FOOD_VALUE;
    }
    /**
     * @return The value a lion is going to give to the animal who eats it.
     */
    @Override
    protected int getFoodValue(){
        return LION_FOOD_VALUE;
    }
    /**
     * @return The probability of a lion of getting a desease.
     */
    @Override
    protected double getDiseaseProbability(){ return DISEASE_PROBABILITY; }

    /**
     * The following method overrides animal.findFood().
     * All predators will call hunt() from the predator interface to find food.
     * @return The location of the prey to eat.
     */
    @Override
    protected Location findFood(){
        return hunt(this);
    }

    @Override
    public boolean sleeps(){
        return !getEnvironment().isAfternoon();
    }

    /**
     * The following method is described in the predator interface.
     * @return A list of the hunted species.
     */
    @Override
    public Class[] getHuntedAnimals(){
        return huntedAnimals;
    }



    /**
     * This lion gives birth to new lions.
     * New births will be made into free adjacent locations.
     * @param newLions A list to return newly born lions.
     */
    @Override
    protected void giveBirth(List<Actor> newLions)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        if (!findMate()){
            return;
        }
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Lion young = new Lion(false, field, loc, getEnvironment());
            newLions.add(young);
        }
    }



}

