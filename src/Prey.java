import java.util.*;

public interface Prey extends Actor{


    default List<Location> getSafeLocations(Animal animal){
        return new ArrayList<>();
    }


}