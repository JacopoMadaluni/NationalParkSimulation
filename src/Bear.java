import java.util.List;

/**
 * A class representing bears in the simulation. It defines bear species specific
 * constants that control bears' behaviour. It also defines bear specific behaviours.
 *
 * @author Jacopo Madaluni and Luka Kralj
 * @version February 2018
 */
public class Bear extends Animal implements Predator, Herbivore, Drawable  {

    // Characteristics shared by all bears (class variables).

    // The age at which a bear can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a bear can live.
    private static final int MAX_AGE = 200;
    // The likelihood of a bear breeding.
    private static final double BREEDING_PROBABILITY = 0.59;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single bear.
    private static final int BEAR_FOOD_VALUE = 23;
    // The probability of getting sick.
    private static final double DISEASE_PROBABILITY = 0.009;
    // Max food value of a bear.
    private static final int MAX_FOOD_VALUE = 18;
    // Steps a bear needs to wait before it can breed again.
    private static final int DAYS_TO_WAIT = 8;
    // List of species of animals that are hunted by bears.
    private static final Class[] huntedAnimals = { Fox.class, Rabbit.class };

    /**
     * Create a bear. A bear can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     *
     * @see Animal
     */
    public Bear(boolean randomAge, Field field, Location location, Environment environment) {
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
        return BEAR_FOOD_VALUE;
    }

    /**
     * A bear will first hunt. If unsuccessful it will try to find plants.
     *
     * @see Animal
     * @see Predator
     * @see Herbivore
     */
    @Override
    protected Location findFood(){
        Location loc = hunt(this);
        if (loc == null){
            loc = findPlants(this);
        }
        return loc;
    }

    /**
     * @see Predator
     */
    @Override
    public Class[] getHuntedAnimals(){
        return huntedAnimals;
    }


    // OTHER METHODS:

    /**
     * Simulates giving births - creates new bears in some adjacent locations.
     *
     * @param newBears A list to return newly born bears.
     */
    protected void giveBirth(List<Actor> newBears) {
        if (!findMate()){
            return;
        }

        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Bear young = new Bear(false, field, loc, getEnvironment());
            newBears.add(young);
        }
    }
}