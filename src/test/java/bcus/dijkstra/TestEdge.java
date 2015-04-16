package bcus.dijkstra;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ble on 4/16/15.
 */
public class TestEdge {

    private List<Vertex> nodes;

    @Test
    public void testConstructor() {
        nodes = new ArrayList<Vertex>();
        nodes.add(new Vertex("0", "Concourse_A_Ticketing"));
        nodes.add(new Vertex("999", "BaggageClaim"));

        Edge edge = new Edge("edge_0", nodes.get(0), nodes.get(1), 5);

        Assert.assertEquals(edge.getSource(), new Vertex("0", "Concourse_A_Ticketing"));
        Assert.assertEquals(edge.getDestination(), new Vertex("999", "BaggageClaim"));
        Assert.assertEquals(edge.getWeight(), 5);

    }


}
