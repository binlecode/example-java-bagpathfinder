package bcus.bag;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ble on 2015/4/17.
 */
public class BagShortestPathCache {

    private Map<String, BagShortestPath> cache = new HashMap<String, BagShortestPath>();

    public boolean hasKey(String id) {
        return cache.containsKey(id);
    }

    public BagShortestPath get(String id) {
        return cache.get(id);
    }

    public BagShortestPath put(BagShortestPath bagPath) {
        return cache.put(bagPath.getId(), bagPath);
    }

    public int getSize() {
        return cache.size();
    }


}
