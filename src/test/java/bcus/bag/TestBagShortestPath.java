package bcus.bag;

import bcus.dijkstra.Vertex;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ble on 2015/4/17.
 */
public class TestBagShortestPath {

    @Test
    public void testBuilder() {
        Vertex sourceNode = new Vertex("A1", "A1");
        Vertex destinationNode = new Vertex("A2", "A2");

        List<Vertex> path = new ArrayList<Vertex>();
        path.add(sourceNode);
        path.add(destinationNode);

        int totalTravelTime = 10;

        BagRoute bagRoute = new BagRoute("0001", sourceNode, destinationNode, "AAA");
        bagRoute.setPath(path);
        bagRoute.setTotalTravelTime(totalTravelTime);

        BagShortestPath bagPath = BagShortestPath.buildBagPath(bagRoute);

        Assert.assertEquals("A1,A2", bagPath.getId());
        Assert.assertEquals(10, bagPath.getTravelTime());
        Assert.assertEquals(2, bagPath.getPath().size());
    }
}
