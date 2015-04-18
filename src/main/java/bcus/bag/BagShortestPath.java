package bcus.bag;

import bcus.dijkstra.Vertex;

import java.util.List;

/**
 * Created by ble on 2015/4/17.
 */
public class BagShortestPath {

    private final String id;
    private final List<Vertex> path;
    private final int travelTime;

    static public BagShortestPath buildBagPath(BagRoute bagRoute) {
        String id = bagRoute.getSourceNode().getId() + "," + bagRoute.getDestinationNode().getId();
        return new BagShortestPath(id, bagRoute.getPath(), bagRoute.getTotalTravelTime());
    }

    public BagShortestPath(String id, List<Vertex> path, int travelTime) {
        this.id = id;
        this.path = path;
        this.travelTime = travelTime;
    }

    public String getId() {
        return id;
    }

    public List<Vertex> getPath() {
        return path;
    }

    public int getTravelTime() {
        return travelTime;
    }
}
