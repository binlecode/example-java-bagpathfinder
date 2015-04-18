package bcus.bag;


import bcus.dijkstra.Edge;
import bcus.dijkstra.Graph;
import bcus.dijkstra.Vertex;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by ble on 4/15/15.
 */
public class TestBagPathFinder extends TestCase {
    private BagPathFinder bagPathFinder = new BagPathFinder();

    @Before
    public void beforeEach() {
        // place holder
    }

    @Test
    public void testReadSections() throws IOException {
        System.out.println(">> running test: " + getName());

        bagPathFinder.readSections(getClass().getClassLoader().getResource("sample_data.txt").getFile());

        Assert.assertTrue(bagPathFinder.conveyorPathList.size() > 0);
        for (String[] conveyorPathArr : bagPathFinder.conveyorPathList) {
            System.out.println(conveyorPathArr[0] + " : " + conveyorPathArr[1]);
        }

        Assert.assertTrue(bagPathFinder.flightDepartureMap.size() > 0);
        for (String key : bagPathFinder.flightDepartureMap.keySet()) {
            System.out.println(key + " : " + bagPathFinder.flightDepartureMap.get(key));
        }

        Assert.assertTrue(bagPathFinder.bagFlightList.size() > 0);
        for (String[] bagFlightArr : bagPathFinder.bagFlightList) {
            System.out.println(bagFlightArr[0] + " : " + bagFlightArr[1] + " : " + bagFlightArr[2]);
        }
    }

    @Test
    public void testBuildConveyorGraph() throws IOException {
        System.out.println(">> running test: " + getName());

        bagPathFinder.readSections(getClass().getClassLoader().getResource("sample_data.txt").getFile());
        Graph conveyorGraph = bagPathFinder.buildConveyorGraph();

        Assert.assertEquals(12, conveyorGraph.getVertexes().size());
        Assert.assertTrue(conveyorGraph.getEdges().size() == 22);

        for (Vertex node : conveyorGraph.getVertexes()) {
            System.out.println(node);
        }
        for (Edge edge : conveyorGraph.getEdges()) {
            System.out.println(edge);
        }

        Assert.assertEquals(BagPathFinder.KEYWORD_BAGGAGE_CLAIM, bagPathFinder.nodeMap.get("BaggageClaim").getName());
    }

    @Test
    public void testBuildBagRouteRequests() throws IOException {
        System.out.println(">> running test: " + getName());

        bagPathFinder.readSections(getClass().getClassLoader().getResource("sample_data.txt").getFile());
        Graph conveyorGraph = bagPathFinder.buildConveyorGraph();
        List<BagRoute> bagRouteRequests = bagPathFinder.buildBagRouteRequests();

        Assert.assertTrue(bagRouteRequests.size() > 0);
        for (BagRoute bagRoute : bagRouteRequests) {
            System.out.println(bagRoute);
        }
    }

    @Test
    public void testRunBagPathFindingForFile() {
        System.out.println(">> running test: " + getName());

        bagPathFinder.runBagPathFindingForFile(getClass().getClassLoader().getResource("sample_data.txt").getFile());
    }





}
