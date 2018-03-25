import sun.corba.EncapsInputStreamFactory;

import java.util.EventListener;
import java.util.List;
import java.util.Stack;

/**
 * A simple model of a plant.
 * Plants age, and die.
 * Plants can spawn around other plants at a give rate (depending on the weather)
 *
 * @author Jacopo Madaluni & Luka Kralj
 * @version 2018.02 (2)
 */
public class Plant implements Actor, Drawable{

    private Field field;
    private Location location;
    private Environment environment;
    private boolean alive;

    private int age;
    private int foodValue;

    private static final int MAX_AGE = 100;

    /**
     * Create a new plant.
     * @param field
     * @param location
     * @param environment
     */
    public Plant(Field field , Location location, Environment environment){
        this.field = field;
        this.location = location;
        setLocation(location);
        this.environment = environment;
        foodValue = Randomizer.getRandom().nextInt(5);


        age = Randomizer.getRandom().nextInt(500);
        alive = true;

    }

    /**
     * @return True if the plant is alive
     */
    @Override
    public boolean isActive(){
        return alive;
    }
    /**
     * @return The value the plant is going to give to the animals who eat it.
     */

    public int getFoodValue(){
        return foodValue;
    }
    /**
     * The following 3 methods are described in the drawable interface.
     */
    @Override
    public Location getLocation() {
        return location;
    }
    @Override
    public Field getField() {
        return field;
    }
    /**
     * Place the plant at the new location in the given field.
     * @param newLocation The plant's new location.
     */
    @Override
    public void setLocation(Location newLocation)
    {
        location = newLocation;
        field.place(this, newLocation);
    }





    /**
     * Plants age and spawn new plants.
     * If the plant dies it is removed from the field.
     * @param newPlants
     */
    @Override
    public void act(List<Actor> newPlants) {
        if (!isActive()){
            return;
        }
        spawnPlants(newPlants);
        spawnPlants(newPlants);
        grow();

        if (age > MAX_AGE){
            setDead();
        }

    }

    /**
     * Removes a plant from the field.
     * If there is an animal on the plant, the animal will be popped out the stack to be replaced after
     * the removal of the plant.
     */
    public void setDead(){
        alive = false;
        Stack<Object> stack = field.getField()[location.getRow()][location.getCol()];
        if(location != null && stack.size() == 2) {
            Object obj = stack.pop();
            field.clear(location);
            field.place(obj, location);
            location = null;
            field = null;
        }else if (location != null && stack.size() == 1){
            field.clear(location);
            location = null;
            field = null;
        }

    }

    /**
     * Depending on the weather, this plant spawns a number of new plants around itself.
     * If a position is already taken by a plant, a new plant cannot be spawned.
     * If a position is taken by an animal, the new plant is placed under the animal.
     * (See field stack implementation)
     * @param newPlants
     */
    public void spawnPlants(List<Actor> newPlants){
        List<Location> locations = field.adjacentLocations(location);
        int noOfPlant = environment.getWeather().getConditions();
        for (int i = 0; i < locations.size() && noOfPlant > 0 ; i++, noOfPlant--){
            Location l = locations.get(i);
            if (field.getField()[l.getRow()][l.getCol()].empty()){
                Plant newPlant = new Plant(field, l, environment);
                newPlants.add(newPlant);
            }
            else if (field.getField()[l.getRow()][l.getCol()].size() == 1 && !(field.getObjectAt(l) instanceof Plant)) {
                Object obj = field.getField()[l.getRow()][l.getCol()].pop();
                Plant newPlant = new Plant(field, l, environment);
                newPlants.add(newPlant);
                field.place(obj , l);
            }

        }
    }

    /**
     * Makes the plant grow.
     * The more grown is the plant, the more value it will give to animals who eat it.
     */
    private void grow(){
        if (environment.isDay()) {
            incrementFoodValue();
        }
        age++;
        if (age == MAX_AGE) { setDead(); }


    }

    /**
     * Increments the food value that the plant is going to give to the animal who eats it.
     * If it's raining the value is increased by 2.
     */
    private void incrementFoodValue(){
        Weather weather = environment.getWeather();
        if (weather.getConditions() == 4){ // if it's raining
            foodValue++;
        }
        foodValue++;
    }
}
