package bcus.dijkstra;

import java.util.List;
import java.util.Set;

/**
 * Created by ble on 4/15/15.
 */
public class DijkstraAlgorithm {
    private final List<Vertex> nodes;
    private final List<Edge> edges;
    private Set<Vertex> settledNodes;
    private

    public DijkstraAlgorithm(List<Vertex> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }
}
