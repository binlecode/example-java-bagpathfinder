package bcus.bag;

import bcus.dijkstra.DijkstraAlgorithm;
import bcus.dijkstra.Edge;
import bcus.dijkstra.Graph;
import bcus.dijkstra.Vertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Airport Baggage - Pathfinding utility main class
 *
 * Denver International Airport has decided to give an automated baggage system another shot.
 * The hardware and tracking systems from the previous attempt are still in place, they just
 * need a system to route the baggage.  The system will route baggage checked, connecting,
 * and terminating in Denver.
 *
 * This path finding utility will route bags to their flights or the proper baggage claim.
 * The input describes the airport conveyor system, the departing flights, and the bags to be routed.
 * The output is the optimal routing to get bags to their destinations.
 * Bags with a flight id of “ARRIVAL” are terminating in Denver are routed to Baggage Claim.
 *
 * Input: The input consists of several sections.  The beginning of each section is marked by a line starting: “# Section:”
 * Section 1: A weighted bi-directional graph describing the conveyor system.
 * Format: <Node 1> <Node 2> <travel_time>
 * Section 2: Departure list
 * Format: <flight_id> <flight_gate> <destination> <flight_time>
 * Section 3: Bag list
 * Format: <bag_number> <entry_point> <flight_id>
 *
 * Output: The optimized route for each bag
 * <Bag_Number> <point_1> <point_2> [<point_3>, …] : <total_travel_time>
 * The output should be in the same order as the Bag list section of the input.
 *
 * Example Input:
 * # Section: Conveyor System
 * Concourse_A_Ticketing A5 5
 * A5 BaggageClaim 5
 * A5 A10 4
 * A5 A1 6
 * A1 A2 1
 * A2 A3 1
 * A3 A4 1
 * A10 A9 1
 * A9 A8 1
 * A8 A7 1
 * A7 A6 1
 * # Section: Departures
 * UA10 A1 MIA 08:00
 * UA11 A1 LAX 09:00
 * UA12 A1 JFK 09:45
 * UA13 A2 JFK 08:30
 * UA14 A2 JFK 09:45
 * UA15 A2 JFK 10:00
 * UA16 A3 JFK 09:00
 * UA17 A4 MHT 09:15
 * UA18 A5 LAX 10:15
 * # Section: Bags
 * 0001 Concourse_A_Ticketing UA12
 * 0002 A5 UA17
 * 0003 A2 UA10
 * 0004 A8 UA18
 * 0005 A7 ARRIVAL
 * Example Output:
 * 0001 Concourse_A_Ticketing A5 A1 : 11
 * 0002 A5 A1 A2 A3 A4 : 9
 * 0003 A2 A1 : 1
 * 0004 A8 A9 A10 A5 : 6
 * 0005 A7 A8 A9 A10 A5 BaggageClaim : 12
 *
 * Created by Bin Le (binle2002@hotmail.com) on 4/15/15.
 *
 */
public class BagPathFinder {
    public static final String KEYWORD_ARRIVAL = "ARRIVAL";
    public static final String KEYWORD_BAGGAGE_CLAIM = "BaggageClaim";

    List<String[]> conveyorPathList = new ArrayList<String[]>();
    Map<String, String> flightDepartureMap = new HashMap<String, String>();
    List<String[]> bagFlightList = new ArrayList<String[]>();

    // we use map to keep non-duplicate objects before filling into graph
    Map<String, Vertex> nodeMap = new HashMap<String, Vertex>();
    Map<String, Edge> edgeMap = new HashMap<String, Edge>();

    BagShortestPathCache bagShortestPathCache = new BagShortestPathCache();

    /**
     * Main entry for command line execution
     * @param args takes only 1 argument for data file name
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
            return;
        }

        String file = args[0];
        try {
            new BagPathFinder().runBagPathFindingForFile(file);
        } catch (Exception e) {
            System.out.println("oops, something went wrong: " + e.getMessage());
        }
    }

    /**
     * Main public API method for path finding
     * @param file  input file that contains the section data
     */
    public void runBagPathFindingForFile(String file) {
        Graph graph;
        try {
            readSections(file);
            graph = buildConveyorGraph();
        } catch (IOException e) {
            System.out.println("oops, something wen wrong: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // initialize algorithm with graph
        DijkstraAlgorithm algorithm = new DijkstraAlgorithm(graph);

        // initialize bag route best path request list
        List<BagRoute> bagRoutes = buildBagRouteRequests();

        // calculate shortest path for each bag route request
        for (BagRoute bagRoute : bagRoutes) {
            calculateAndSaveShortestPathToBagRouteWithCache(bagRoute, algorithm);
        }

        // print out results
        System.out.println("Calculated bag routes:");
        for (BagRoute bagRoute : bagRoutes) {
            System.out.println(formatBagRoute(bagRoute));
        }
    }

    /**
     * Calculates the shortest path for given bag route request, and saves back to it.
     * A {@link BagShortestPathCache} cache is used for shortest path caching and look-up to avoid redundant algorithm
     * executions.
     * @param bagRoute  the {@link BagRoute} object having the route request
     * @param algorithm  the initialized Dijkstra algorithm (already loaded with graph)
     */
    protected void calculateAndSaveShortestPathToBagRouteWithCache(BagRoute bagRoute, DijkstraAlgorithm algorithm) {
        BagShortestPath bagShortestPath;
        List<Vertex> algorithmPath;
        int totalTravelTime;

        String bagShortestPathId = BagShortestPath.buildPathIdFromRoute(bagRoute);
        // check cache for shortest path record
        if (bagShortestPathCache.containsPathForId(bagShortestPathId)) {
            bagShortestPath = bagShortestPathCache.getPathById(bagShortestPathId);
            // cache hit, save to bag route record
            bagRoute.setPath(bagShortestPath.getPath());
            bagRoute.setTotalTravelTime(bagShortestPath.getTravelTime());

        } else {  // cache miss, run the algorithm
            algorithm.execute(bagRoute.getSourceNode());
            // we want to cache all paths calculated from the algorithm execution
            for (Vertex node : nodeMap.values()) {
                if (node.getId().equals(bagRoute.getSourceNode().getId())) {
                    continue;   // exclude source node itself
                }

                algorithmPath = algorithm.getPath(node);
                totalTravelTime = algorithm.getPathDistance(algorithmPath);
                bagShortestPath = new BagShortestPath(
                        BagShortestPath.buildPathIdFromSourceAndDestinationNodes(bagRoute.getSourceNode(), node),
                        algorithmPath, totalTravelTime);
                bagShortestPathCache.putPath(bagShortestPath);

                // save destination node related shortest path to bag route record
                if (node.getId().equals(bagRoute.getDestinationNode().getId())) {
                    bagRoute.setPath(algorithmPath);
                    bagRoute.setTotalTravelTime(totalTravelTime);
                }
            }
        }
    }

    /**
     * Reads section data from given file
     * @param file
     * @throws IOException
     */
    protected void readSections(String file) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        // skip first # section comment
        br.readLine();

        // read conveyor section
        while (!(line = br.readLine()).startsWith("#")) {
            conveyorPathList.add(line.split(" "));
        }

        // read departure section
        while (!(line = br.readLine()).startsWith("#")) {
            String[] departureArr = line.split(" ");
            flightDepartureMap.put(departureArr[0], departureArr[1]);  // only use first and second column for flight -> destination node mapping
        }

        // read bags section
        while ((line = br.readLine()) != null) {
            bagFlightList.add(line.split(" "));  // only use first and second column for flight -> destination node mapping
        }

        br.close();
    }

    /**
     * Builds the bidirectional conveyor routes graph from data loaded from file sections
     * @return
     * @throws NumberFormatException
     */
    protected Graph buildConveyorGraph() throws NumberFormatException {
        // build node list
        Vertex fromNode;
        Vertex toNode;
        String edgeKey;
        for (String[] conveyorPath : conveyorPathList) {
            fromNode = checkOrAddNodeWithName(nodeMap, conveyorPath[0]);
            toNode = checkOrAddNodeWithName(nodeMap, conveyorPath[1]);
            edgeKey = fromNode.getId() + " - " + toNode.getId();
            edgeMap.put(edgeKey, new Edge(edgeKey, fromNode, toNode, Integer.parseInt(conveyorPath[2])));
            // because we are building a bidirectional graph, we need to build reverse edge with same weight value
            edgeKey = toNode.getId() + " - " + fromNode.getId();
            edgeMap.put(edgeKey, new Edge(edgeKey, toNode, fromNode, Integer.parseInt(conveyorPath[2])));
        }
        return new Graph(new ArrayList<Vertex>(nodeMap.values()), new ArrayList<Edge>(edgeMap.values()));
    }

    protected List<BagRoute> buildBagRouteRequests() {
        List<BagRoute> bagRouteRequests = new ArrayList<BagRoute>();

        String flightNum;
        Vertex fromNode;
        Vertex toNode;
        for (String[] bagFlightArr : bagFlightList) {
            fromNode = nodeMap.get(bagFlightArr[1]);

            // conduct needed keyword translation from flight to destination node
            flightNum = bagFlightArr[2];
            if (KEYWORD_ARRIVAL.equals(flightNum)) {
                toNode = nodeMap.get(KEYWORD_BAGGAGE_CLAIM);
            } else {
                toNode = nodeMap.get(flightDepartureMap.get(flightNum));
            }

            bagRouteRequests.add(new BagRoute(bagFlightArr[0], fromNode, toNode, flightNum));
        }

        return bagRouteRequests;
    }

    private String formatBagRoute(BagRoute bagRoute) {
        StringBuilder sb = new StringBuilder(bagRoute.getRequestIndex());
        if (bagRoute.getPath() != null) {
            for (Vertex node : bagRoute.getPath()) {
                sb.append(" ").append(node.getName());
            }
        }
        sb.append(" : ").append(bagRoute.getTotalTravelTime());
        return sb.toString();
    }

    private Vertex checkOrAddNodeWithName(Map<String, Vertex> nodeMap, String nodeName) {
        Vertex node;
        if (nodeMap.containsKey(nodeName)) {
            node = nodeMap.get(nodeName);
        } else {
            node = new Vertex(nodeName, nodeName);
            nodeMap.put(nodeName, node);  // assume node name is unique so can be used as key/id
        }
        return node;
    }

    private static void usage() {
        System.out.println("Usage: java -jar bagpathfinder-<version>.jar <data_file_name>");
    }


}
