/**
 * A class representing diseases that can affect animals behaviour.
 * Currently there is only one class for diseases which can be later extended
 * to become a superclass for different kinds of diseases that can affect different species
 * differently.
 *
 * @author Jacopo Madaluni and Luka Kralj
 * @version February 2018
 */
public class Disease {
    private int agingFactor; // The smaller the factor the more deadly the disease is.

    /**
     * Create new disease with certain aging factor. Currently the sick animals age more quickly.
     * More precisely, their age increases by MaxSpeciesAge/AgingFactor rather than by one.
     * Sick animals also cannot breed in this version.
     *
     * @param agingFactor Factor that affects by how much the animals are aging. Must be at least 1.
     */
    public Disease(int agingFactor){
        if (agingFactor < 1) { // Because of the current usage of this factor it cannot be less than 1.
            System.out.println("Aging factor must be at least one.");
            System.out.println("Using default value (1).");
            agingFactor = 1;
        }
        this.agingFactor = agingFactor;
    }

    /**
     *
     * @return Aging factor of the disease.
     */
    public int getAgingFactor(){
        return agingFactor;
    }
}
