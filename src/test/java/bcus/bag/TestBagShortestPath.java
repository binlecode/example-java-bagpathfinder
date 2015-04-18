package bcus.bag;

import bcus.dijkstra.Vertex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ble on 2015/4/17.
 */
public class TestBagShortestPath {

    Vertex sourceNode = new Vertex("A1", "A1");
    Vertex destinationNode = new Vertex("A2", "A2");
    BagRoute bagRoute = new BagRoute("0001", sourceNode, destinationNode, "AAA");
    List<Vertex> path = new ArrayList<Vertex>();

    int totalTravelTime = 10;

    @Before
    public void beforeEach() {
        path.add(sourceNode);
        path.add(destinationNode);
    }

    @Test
    public void testBuildPathIdFromRoute() {
        Assert.assertEquals("A1,A2", BagShortestPath.buildPathIdFromRoute(bagRoute));
    }

    @Test
    public void testBuilder() {
        bagRoute.setPath(path);
        bagRoute.setTotalTravelTime(totalTravelTime);

        BagShortestPath bagPath = BagShortestPath.buildPathFromRoute(bagRoute);

        Assert.assertEquals("A1,A2", bagPath.getId());
        Assert.assertEquals(10, bagPath.getTravelTime());
        Assert.assertEquals(2, bagPath.getPath().size());
    }
}
