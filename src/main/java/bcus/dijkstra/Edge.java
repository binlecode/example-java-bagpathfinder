package bcus.dijkstra;

/**
 * Created by ble on 4/15/15.
 */
public class Edge {
    // immutable fields
    private final String id;
    private final Vertex source;
    private final Vertex destination;
    private final int weight;

    public Edge(String id, Vertex sourceVertex, Vertex destinationVertex, int weight) {
        this.id = id;
        this.source = sourceVertex;
        this.destination = destinationVertex;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "id='" + id + '\'' +
                ", source=" + source +
                ", destination=" + destination +
                ", weight=" + weight +
                '}';
    }


}
