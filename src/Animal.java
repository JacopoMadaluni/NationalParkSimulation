import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A class representing shared characteristics of animals.
 * Some common behaviours are also specified here (e.g. findMate, act...).
 *
 * (Main framework of the class given by David J. Barnes and Michael Kölling.)
 *
 * @author Jacopo Madaluni and Luka Kralj
 * @version February 2018
 */
public abstract class Animal implements Actor {
    // Whether the animal is alive or not.
    private boolean alive;
    // Age of the animal.
    private int age;
    // 'F' for female, 'M' for male
    private char gender;
    // Counts the steps from the last breeding onwards.
    private int breedCounter;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    // The amount of food the animal has eaten.
    // N.B. this counter cannot increase forever as each animal has its own maximal food level.
    private int foodLevel;
    // The disease the animal currently has. Null if none.
    private Disease disease;
    // The animal's environment.
    private Environment environment;
    // Random generator.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a new animal at location in field and certain environment.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param randomAge If set the false the animal was just born. When populating this should be
     *                  true as we want animals of different age and different food level.
     * @param environment Environment in which the animal is.
     */
    public Animal(Field field, Location location, boolean randomAge, Environment environment) {
        alive = true;
        this.field = field;
        setLocation(location); // Places the animal on the field.
        age = 0;
        this.environment = environment;
        breedCounter = getDaysToWait();
        disease = null; // At the creation every animal is healthy.
        gender = getRandomGender(); // Whenever we create an animal the gender is random.
        if(randomAge) {
            // This is executed when populating the field.
            age = rand.nextInt(getMaxAge());
            foodLevel = rand.nextInt(getMaxFoodValue());
        }
        else {
            // When new animal is born the food value is maximal.
            foodLevel = getMaxFoodValue();
        }
    }


    // GETTERS:

    /**
     *
     * @return True if animal is sick, otherwise returns false.
     */
    protected  boolean hasDisease() {
        if (disease != null){
            return true;
        }
        return false;
    }

    /**
     * Returns the gender of the animal.
     *
     * @return 'F' if animal is female, 'M' otherwise.
     */
    private char getGender() {
        return gender;
    }

    /**
     *
     * @return Age of the animal.
     */
    private int getAge() {
        return age;
    }

    /**
     * Check whether the animal is alive or not.
     *
     * @return True if the animal is still alive, false if not.
     */
    protected boolean isAlive() {
        return alive;
    }

    /**
     * Return the animal's location.
     *
     * @return The animal's location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @return The animal's environment.
     */
    protected Environment getEnvironment(){
        return environment;
    }

    /**
     *
     * @return The disease the animal has, null if it is healthy.
     */
    private Disease getDisease() {
        return disease;
    }

    /**
     * Return the animal's field.
     *
     * @return The animal's field.
     */
    public Field getField() {
        return field;
    }

    /**
     * Check if the animal is still active.
     *
     * @return True if animal is still alive, false if not.
     * @see Actor
     */
    @Override
    public boolean isActive() {
        return isAlive();
    }

    /**
     * This function determines whether an animal currently sleeps.
     * By default all animals sleep at night, but the method can be overridden
     * in subclasses to reflect species' specific behaviour.
     *
     * @return True if animal currently sleeps, false if not.
     */
    protected boolean sleeps(){
        return environment.isNight();
    }


    // SETTERS:

    /**
     * Animal gets sick with certain probability which is different for every species.
     */
    private void setDisease() {
        if (rand.nextDouble() <= getDiseaseProbability()) {
            disease = new Disease(10);
        }
    }

    /**
     * Increments breed counter for female subjects only.
     * The counter never exceeds the days needed to wait until next breeding.
     */
    private void incrementBreedCounter() {
        if(breedCounter < getDaysToWait() && getGender() == 'F'){
            breedCounter++;
        }
    }

    /**
     * Make this animal more hungry. This could result in the animal's death.
     */
    private void incrementHunger() {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Sets food level of the animal. If after eating the food level would exceed the
     * maximum food level of the animal the food level is set to maximum instead.
     *
     * @param foodValue Value by which we need to increase the food level.
     */
    protected void setFoodLevel(int foodValue) {
        if (foodLevel + foodValue > getMaxFoodValue()){
            foodLevel = getMaxFoodValue();
        }else {
            foodLevel += foodValue;
        }
    }

    /**
     * Increase the age.
     * This could result in the animal's death.
     */
    private void incrementAge() {
        if (disease != null){
            // If animal is sick it ages more quickly.
            age = age + getMaxAge()/disease.getAgingFactor();
        }
        else {
            age++;
        }
        if(age > getMaxAge()) {
            setDead();
        }
    }

    /**
     * Place the animal at the new location in the given field.
     *
     * @param newLocation The animal's new location.
     */
    public void setLocation(Location newLocation) {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Indicates that the animal is no longer alive.
     * Animal is removed from the field.
     */
    protected void setDead() {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }


    // OTHER METHODS:

    /**
     * Generates random gender.
     *
     * @return 'F' for female, 'M' for male.
     */
    private char getRandomGender(){
        if (rand.nextBoolean()) {
            return 'F';
        }
        return 'M';
    }

    /**
     * This method checks if there is an animal of the same species in any of the adjacent
     * locations of the current position of the animal the is currently acting.
     * If such animal is found then we check if that animal is actually a mate:
     * we check if it is alive, if it is of the opposite gender etc.
     *
     * @return True if appropriate mate is found, false otherwise.
     */
    protected boolean findMate(){
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Animal) {
                Animal mate = (Animal) animal;
                if (mate.getClass().equals(getClass())
                        && mate.isAlive() && isMate(mate)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if mate is of the opposite gender and if it is actually ready to breed.
     *
     * @param mate Animal we compare the current animal with.
     * @return True if mate can breed with the animal currently acting, false if not.
     */
    private boolean isMate(Animal mate){
        return mate.canBreed() && gender != mate.getGender();
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     *
     * @return The number of births (may be zero).
     */
    protected int breed() {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
            breedCounter = 0; // The animal just bred. Now it needs to wait some time before it can breed again.
        }
        return births;
    }

    /**
     * An animal can breed if it reached the certain age, enough days have passed since the last breeding,
     * the animal is not sick and it if it is not too hungry.
     *
     * @return True if the animal can breed, false otherwise.
     */
    private boolean canBreed() {
        return getAge() >= getBreedingAge()
                && breedCounter == getDaysToWait()
                && !hasDisease()
                && foodLevel > getMaxFoodValue()/4;
    }

    /**
     * When an animal meets a sick animal it can get the disease of that animal (with certain probability).
     */
    private void checkForDisease(){
        if (rand.nextDouble() > getDiseaseProbability()) {
            return;
        }
        List<Location> locations = field.adjacentLocations(location);
        for(Location l : locations) {
            Object obj = field.getObjectAt(l);
            if (obj instanceof Animal){
                Animal animal = (Animal) obj;
                if(animal.hasDisease()){
                    disease = animal.getDisease();
                    return;
                }
            }
        }
    }


    // OVERRIDDEN METHODS FROM SUPERCLASSES AND INTERFACES:

    /**
     * Some animals' behaviour is common to all animal species: they breed, they get more hungry at each step,
     * they get older and they need to find food. Some of these behaviours need to be defined in each
     * species' class separately.
     *
     * @param newAnimals List of all newly created animals at each step. These animals are added to
     *                   the simulation after each step.
     * @see Actor
     */
    @Override
    public void act(List<Actor> newAnimals) {
        if (sleeps()) { // If the animal sleeps its state won't change.
            return;
        }

        incrementAge();
        incrementBreedCounter();
        incrementHunger();

        if(isAlive()) {
            setDisease(); // Animals can randomly get sick.
            checkForDisease(); // Check if any of the animals nearby is sick.
            giveBirth(newAnimals);
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }


    // ABSTRACT METHODS THAT NEED TO BE IMPLEMENTED IN SUBCLASSES:

    /**
     *
     * @return Breeding probability of the animal's species.
     */
    protected abstract double getBreedingProbability();

    /**
     *
     * @return Breeding age of the animal's species.
     */
    protected abstract int getBreedingAge();

    /**
     *
     * @return Maximum litter size of the animal's species.
     */
    protected abstract int getMaxLitterSize();

    /**
     *
     * @return Steps to wait until next breeding of the animal's species.
     */
    protected  abstract int getDaysToWait();

    /**
     *
     * @return Food value that a specimen of the animal's species gives when eaten.
     */
    protected abstract int getFoodValue();

    /**
     *
     * @return Disease probability of the animal's species.
     */
    protected abstract double getDiseaseProbability();

    /**
     *
     * @return Maximum food value of the animal's species.
     */
    protected abstract int getMaxFoodValue();

    /**
     *
     * @return Max age of the animal's species.
     */
    public abstract int getMaxAge();

    /**
     * Specifies special behaviour for every animal when giving birth since each species
     * gives birth to animals of their own species.
     *
     * @param newAnimals List of all newly created animals at each step. These animals are added to
     *                   the simulation after each step.
     */
    protected abstract void giveBirth(List<Actor> newAnimals);

    /**
     * Each animal has its own way of finding food. It can either be a predator, herbalist, prey or any other
     * combination.
     *
     * @return Location where food source was found or null if no such location was found.
     */
    protected abstract Location findFood();
}