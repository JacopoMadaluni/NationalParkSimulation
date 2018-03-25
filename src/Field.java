import java.util.Stack;
import java.util.Random;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

/**
 * Represent a rectangular grid of field positions.
 * Each position is able to store at most a plant and an animal.
 * Implementation is using stacks that can simulate animals stepping on the
 * plants without killing/removing them from the field.
 * 
 * @author David J. Barnes and Michael Kölling (modified to use stacks by: Jacopo Madaluni and Luka Kralj)
 * @version February 2018
 */
public class Field {
    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();
    // The depth and width of the field.
    private int depth, width;
    // Storage for the plants and animals.
    private Stack[][] field;

    /**
     * Represent a field of the given dimensions.
     *
     * @param depth The depth of the field.
     * @param width The width of the field.
     */
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
        field = new Stack[depth][width];

        initialiseGrid();
    }

    /**
     * The 2-dimensional array of stack needs to be initialised with new stacks.
     */
    private void initialiseGrid(){
        for(int row = 0; row < depth; row++) {
            for(int col = 0; col < width; col++) {
                field[row][col] = new Stack<>();
            }
        }
    }
    
    /**
     * Empty the field.
     */
    public void clear() {
        initialiseGrid();
    }
    
    /**
     * Clear the top element of the given location.
     *
     * @param location The location to clear.
     */
    public void clear(Location location) {
        field[location.getRow()][location.getCol()].pop();
    }
    
    /**
     * Place an object at the given location.
     *
     * @param object The object to be placed.
     * @param row Row coordinate of the location.
     * @param col Column coordinate of the location.
     */
    public void place(Object object, int row, int col) {
        place(object, new Location(row, col));
    }

    /**
     * Place an object at the given location.
     *
     * @param object The object to be placed.
     * @param location Where to place the object.
     */
    public void place(Object object, Location location) {
        field[location.getRow()][location.getCol()].push(object);
    }

    /**
     * Return the top object at the given location, if any.
     *
     * @param location Where in the field.
     * @return The top object at the given location, or null if there is none.
     */
    public Object getObjectAt(Location location) {
        return getObjectAt(location.getRow(), location.getCol());
    }
    
    /**
     * Return the object at the given location, if any.
     *
     * @param row The desired row.
     * @param col The desired column.
     * @return The top object at the given location, or null if there is none.
     */
    public Object getObjectAt(int row, int col) {
        if (field[row][col].isEmpty()) {
            return null;
        }
        return field[row][col].peek();
    }

    /**
     * It returns the field as a 2-dimensional array that is needed for some operations.
     *
     * @return 2D array of stacks that is currently used as a field.
     */
    public Stack[][] getField(){
        return field;
    }
    
    /**
     * Generate a random location that is adjacent to the
     * given location, or is the same location.
     * The returned location will be within the valid bounds
     * of the field.
     *
     * @param location The location from which to generate an adjacency.
     * @return A valid location within the grid area.
     */
    public Location randomAdjacentLocation(Location location) {
        List<Location> adjacent = adjacentLocations(location);
        return adjacent.get(0);
    }
    
    /**
     * Get a shuffled list of the free adjacent locations.
     * As a free location it is meant a location with no other objects but Plants.
     *
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location) {
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = adjacentLocations(location);
        for(Location next : adjacent) {
            if(getObjectAt(next) == null || getObjectAt(next) instanceof Plant) {
                free.add(next);
            }
        }
        return free;
    }
    
    /**
     * Try to find a free location that is adjacent to the
     * given location. As a free location it is meant a location with
     * no other objects but Plants. If there is no such location, return null.
     * The returned location will be within the valid bounds
     * of the field.
     *
     * @param location The location from which to generate an adjacency.
     * @return A valid location within the grid area.
     */
    public Location freeAdjacentLocation(Location location) {
        // The available free ones.
        List<Location> free = getFreeAdjacentLocations(location);
        if(free.size() > 0) {
            return free.get(0);
        }
        else {
            return null;
        }
    }

    /**
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     *
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> adjacentLocations(Location location) {
        assert location != null : "Null location passed to adjacentLocations";
        // The list of locations to be returned.
        List<Location> locations = new LinkedList<>();
        if(location != null) {
            int row = location.getRow();
            int col = location.getCol();
            for(int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth) {
                    for(int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }
            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Return the depth of the field.
     *
     * @return The depth of the field.
     */
    public int getDepth() {
        return depth;
    }
    
    /**
     * Return the width of the field.
     *
     * @return The width of the field.
     */
    public int getWidth() {
        return width;
    }
}