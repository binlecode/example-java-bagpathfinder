package bcus.dijkstra;

/**
 * Created by ble on 4/15/15.
 */
public class Edge {
    // immutable fields
    private final String id;
    private final Vertex sourceVertex;
    private final Vertex destinationVertex;
    private final int weight;

    public Edge(String id, Vertex sourceVertex, Vertex destinationVertex, int weight) {
        this.id = id;
        this.sourceVertex = sourceVertex;
        this.destinationVertex = destinationVertex;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public Vertex getSourceVertex() {
        return sourceVertex;
    }

    public Vertex getDestinationVertex() {
        return destinationVertex;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "id='" + id + '\'' +
                ", sourceVertex=" + sourceVertex +
                ", destinationVertex=" + destinationVertex +
                ", weight=" + weight +
                '}';
    }


}
