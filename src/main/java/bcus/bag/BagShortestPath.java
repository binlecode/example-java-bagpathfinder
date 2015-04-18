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

    static public BagShortestPath buildPathFromRoute(BagRoute bagRoute) {
        String id = buildPathIdFromRoute(bagRoute);
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

    /**
     * Builds the cache key as {@link BagShortestPath} object id to be cached
     * @param bagRoute  {@link BagRoute} object
     * @return  cache key (id) string
     */
    static public String buildPathIdFromRoute(BagRoute bagRoute) {
        return buildPathIdFromSourceAndDestinationNodes(bagRoute.getSourceNode(), bagRoute.getDestinationNode());
    }

    /**
     * Builds the cache key as {@link BagShortestPath} object id to be cached
     * @param sourceNode   {@link Vertex} object as source node
     * @param destinationNode  {@link Vertex} object as destination node
     * @return   cache key (id) string
     */
    static public String buildPathIdFromSourceAndDestinationNodes(Vertex sourceNode, Vertex destinationNode) {
        return sourceNode.getId() + "," + destinationNode.getId();
    }

    @Override
    public String toString() {
        return "BagShortestPath{" +
                "id='" + id + '\'' +
                ", path=" + path +
                ", travelTime=" + travelTime +
                '}';
    }
}
