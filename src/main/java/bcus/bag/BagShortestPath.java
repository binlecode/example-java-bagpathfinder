package bcus.bag;

import bcus.dijkstra.Vertex;

import java.util.List;

/**
 * Class used to contain a shortest path for a bag routing request.
 *
 * Created by ble on 2015/4/17.
 */
public class BagShortestPath {

    /**
     * identifies the path for each source and destination node pair
     * id string has a format of: "<sourceNode.id>,<destinationNode.id>"
     * @see {@link #buildPathIdFromSourceAndDestinationNodes(Vertex, Vertex)}
     */
    private final String id;
    /**
     * a list contains the calculated shortest path from source node to destination node.
     */
    private final List<Vertex> path;
    /**
     * the travel time of the shortest path
     */
    private final int travelTime;

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
