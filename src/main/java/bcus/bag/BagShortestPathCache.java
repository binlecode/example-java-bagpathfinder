package bcus.bag;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides a cache for already calculated shortest paths.
 * Each cached element is a {@link BagShortestPath} object that contains shortest path
 * and total travel time for a given (source, destination) pair.
 *
 * Created by ble on 2015/4/17.
 */
public class BagShortestPathCache {

    private Map<String, BagShortestPath> cache = new HashMap<String, BagShortestPath>();

//    public boolean containsPathForRoute(BagRoute bagRoute) {
//        return cache.containsKey(BagShortestPath.buildPathIdFromRoute(bagRoute));
//    }

    public boolean containsPathForId(String id) {
        return cache.containsKey(id);
    }

    public boolean containsPath(BagShortestPath path) {
        return cache.containsKey(path.getId());
    }

    public BagShortestPath getPathById(String id) {
        return cache.get(id);
    }

//    public BagShortestPath getPathByRoute(BagRoute bagRoute) {
//        return cache.get(BagShortestPath.buildPathIdFromRoute(bagRoute));
//    }

    public BagShortestPath putPath(BagShortestPath bagPath) {
        return cache.put(bagPath.getId(), bagPath);
    }

    public int size() {
        return cache.size();
    }



}
