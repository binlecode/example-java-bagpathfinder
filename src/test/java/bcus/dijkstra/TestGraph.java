package bcus.dijkstra;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ble on 4/16/15.
 */
public class TestGraph {

    private List<Vertex> nodes;
    private List<Edge> edges;


    @Test
    public void testConstructor() {
        nodes = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();

        nodes.add(new Vertex("0", "Concourse_A_Ticketing"));
        nodes.add(new Vertex("999", "BaggageClaim"));

        edges.add(new Edge("edge_0", nodes.get(0), nodes.get(1), 5));

        Graph graph = new Graph(nodes, edges);

        Assert.assertNotNull(graph);
        Assert.assertEquals(graph.getVertexes().size(), 2);
        Assert.assertEquals(graph.getEdges().size(), 1);

        // check hashCode for Set support
        Assert.assertEquals(graph.getVertexes().get(0).hashCode(), new Vertex("0", "Concourse_A_Ticketing").hashCode());

    }

}
