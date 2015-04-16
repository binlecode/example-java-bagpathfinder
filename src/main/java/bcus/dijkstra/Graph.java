package bcus.dijkstra;

import java.util.List;

/**
 * Created by ble on 4/15/15.
 */
public class Graph {
    private final List<Vertex> vertexes;
    private final List<Edge> edges;


    public Graph(List<Vertex> vertexes, List<Edge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public List<Vertex> getVertexes() {
        return vertexes;
    }

    public List<Edge> getEdges() {
        return edges;
    }


}
