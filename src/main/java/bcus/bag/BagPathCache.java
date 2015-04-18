package bcus.bag;

import bcus.dijkstra.Vertex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ble on 2015/4/17.
 */
public class BagPathCache {

    private Map<String, BagPath> cache = new HashMap<String, BagPath>();

    public boolean hasKey(String id) {
        return cache.containsKey(id);
    }

    public BagPath get(String id) {
        return cache.get(id);
    }

    public BagPath put(BagPath bagPath) {
        return cache.put(bagPath.getId(), bagPath);
    }

    public int getSize() {
        return cache.size();
    }


}
