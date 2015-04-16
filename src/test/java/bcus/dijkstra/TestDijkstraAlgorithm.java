package bcus.dijkstra;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ble on 4/16/15.
 */
public class TestDijkstraAlgorithm {

    private List<Vertex> nodes;
    private List<Edge> edges;

    @Test
    public void testAlgorithmConstruction() {


        nodes = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();

        // we have the following nodes: Concourse_A_Ticketing, BaggageClaim, A1, A2, ... A10
        /* the distances between nodes are
            Concourse_A_Ticketing A5 5
            A5 BaggageClaim 5
            A5 A10 4
            A5 A1 6
            A1 A2 1
            A2 A3 1
            A3 A4 1
            A10 A9 1
            A9 A8 1
            A8 A7 1
            A7 A6 1
        */

        List<String> nodeNames = new ArrayList<String>();

        nodes.add(new Vertex("Concourse_A_Ticketing", "Concourse_A_Ticketing"));
        nodeNames.add("Concourse_A_Ticketing");
        nodes.add(new Vertex("BaggageClaim", "BaggageClaim"));
        nodeNames.add("BaggageClaim");
        for (int i = 1; i <= 10; i++) {
            nodes.add(new Vertex("A" + i, "A" + i));
            nodeNames.add("A" + i);
        }

        edges.add(new Edge("Concourse_A_Ticketing - A5", nodes.get(nodeNames.indexOf("Concourse_A_Ticketing")), nodes.get(nodeNames.indexOf("A5")), 5));
        edges.add(new Edge("A5 - BaggageClaim", nodes.get(nodeNames.indexOf("A5")), nodes.get(nodeNames.indexOf("BaggageClaim")), 5));
        edges.add(new Edge("A5 - A10", nodes.get(nodeNames.indexOf("A5")), nodes.get(nodeNames.indexOf("A10")), 4));
        edges.add(new Edge("A5 - A1", nodes.get(nodeNames.indexOf("A5")), nodes.get(nodeNames.indexOf("A1")), 6));
        edges.add(new Edge("A1 - A2", nodes.get(nodeNames.indexOf("A1")), nodes.get(nodeNames.indexOf("A2")), 1));
        edges.add(new Edge("A2 - A3", nodes.get(nodeNames.indexOf("A2")), nodes.get(nodeNames.indexOf("A3")), 1));
        edges.add(new Edge("A3 - A4", nodes.get(nodeNames.indexOf("A3")), nodes.get(nodeNames.indexOf("A4")), 1));
        edges.add(new Edge("A10 - A9", nodes.get(nodeNames.indexOf("A10")), nodes.get(nodeNames.indexOf("A9")), 1));
        edges.add(new Edge("A9 - A8", nodes.get(nodeNames.indexOf("A9")), nodes.get(nodeNames.indexOf("A8")), 1));
        edges.add(new Edge("A8 - A7", nodes.get(nodeNames.indexOf("A8")), nodes.get(nodeNames.indexOf("A7")), 1));
        edges.add(new Edge("A7 - A6", nodes.get(nodeNames.indexOf("A7")), nodes.get(nodeNames.indexOf("A6")), 1));


        Graph graph = new Graph(nodes, edges);

        // check hashCode for Set support
        Assert.assertEquals(12, graph.getVertexes().size());
        Assert.assertEquals(11, graph.getEdges().size());

        DijkstraAlgorithm algorithm = new DijkstraAlgorithm(graph);
        algorithm.execute(nodes.get(nodeNames.indexOf("Concourse_A_Ticketing")));
        List<Vertex> path = algorithm.getPath(nodes.get(nodeNames.indexOf("A1")));

        for (Vertex node : path) {
            System.out.println(node);
        }

        System.out.println("total travel time: " + algorithm.getPathDistance(path));



    }

}
