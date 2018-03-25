import java.util.List;
import java.util.Random;

/**
 * The model class of the weather.
 * The hierarchy system can be implemented to have a subclass for every different weather.
 * For now the weather can either be sunny, rainy, snowy or foggy.
 *
 * The weather affects the probability for predators to hunt and for plants to grow.
 *
 * @author Jacopo Madaluni & Luka Kralj
 * @version 2018.02 (2)
 */
public class Weather implements Actor {
    private boolean sunny;
    private boolean rainy;
    private boolean snowy;
    private boolean foggy;

    private final double SUN_PROBABILITY= 0.8;
    private final double RAIN_PROBABILITY= 0.7;
    private final double SNOW_PROBABILITY= 0.1;
    private final double FOG_PROBABILITY= 0.2;

    private Random rand = Randomizer.getRandom();

    /**
     * Create a new weather.
     */
    public Weather(){
        reset();
        sunny = true; // default value
    }

    /**
     * @return True, weather is always active.
     */
    @Override
    public boolean isActive() {
        return true;
    }

    /**
     * The weather changes with his own probabilty.
     * @param newActors
     */
    @Override
    public void act(List<Actor> newActors) {
        reset();
        if (Randomizer.getRandom().nextDouble() <= SNOW_PROBABILITY){
            snowy = true;
        }
        else if (Randomizer.getRandom().nextDouble() <= FOG_PROBABILITY){
            foggy = true;
        }
        else if (Randomizer.getRandom().nextDouble() <= RAIN_PROBABILITY){
            rainy = true;
        }
        else{
            sunny = true;
        }
    }

    /**
     * Returns a integer that represents the conditions for a plant to grow and spread.
     * Higher the value, better the condition.
     * @return An integer representing the actual conditions.
     */
    public int getConditions(){
        if (sunny){
            return 3;
        }
        if (rainy){
            return 4;
        }
        if (snowy){
            return 0;
        }
        else{  //foggy
            return 2;
        }
    }

    /**
     * @return The probability for a predator to see a prey.
     */
    public double getVision(){
        if (sunny){
            return 1.0;
        }
        if (rainy){
            return 0.9;
        }
        if (snowy){
            return 0.8;
        }
        else{  //foggy
            return 0.8;
        }
    }

    /**
     * Resets the weather.
     */
    private void reset(){
        sunny = false;
        rainy = false;
        foggy = false;
        snowy = false;
    }

    /**
     * @return The string to be printed in the GUI.
     */
    public String getWeatherString(){
        if (sunny){
            return "sunny";
        }
        if (rainy){
            return "rainy";
        }
        if (snowy){
            return "snowy";
        }
        else if (foggy){  //foggy
            return "foggy";
        }else{
            return "Error: no weather (?)";
        }
    }
}
