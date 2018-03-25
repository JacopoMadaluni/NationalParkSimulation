/**
 * A class representing environment in which we are simulating.
 * This class keep the track of time of the day in terms of steps (24 steps = 1 day).
 * Each environment can be assigned it's own weather.
 *
 * @author Jacopo Madaluni and Luka Kralj
 * @version February 2018
 */
public class Environment{
    private int hourOfDay;
    private Weather weather;

    /**
     * Create new environment.
     *
     * @param weather Weather that we want to add to the simulation.
     */
    public Environment (Weather weather){
        hourOfDay = 0;
        this.weather = weather;
    }


    // GETTERS:

    /**
     * As "night" it is considered hours between 0 and 7 (including).
     *
     * @return True if it is night, false otherwise.
     */
    public boolean isNight(){
        return hourOfDay < 8;
    }

    /**
     * As "day" it is considered hours between 8 and 23 (including).
     *
     * @return True if it is day, false otherwise.
     */
    public boolean isDay(){
        return !isNight();
    }

    /**
     * As "afternoon" it is considered hours between 12 and 19 (including).
     *
     * @return True if it is afternoon, false otherwise.
     */
    public boolean isAfternoon(){
        return hourOfDay > 11 && hourOfDay < 20;
    }

    /**
     *
     * @return Weather of the environment.
     */
    public Weather getWeather() {
        return weather;
    }

    /**
     * This method is used to obtain the string of the daytime. As the time of the
     * day changes very quickly, only day or night are displayed.
     *
     * @return "day" if it is day, "night" if it is night.
     */
    public String getTimeString(){
        if(isNight()){
            return "night";
        }
        return "day";
    }


    // SETTERS:

    /**
     * Increment time of day for one hour. Time is increased at the beginning of each step.
     */
    public void incrementHourOfDay(){
        hourOfDay = (hourOfDay + 1)% 23  ;
    }
}
