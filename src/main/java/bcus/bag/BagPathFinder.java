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
 * Created by ble on 4/15/15.
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
        new BagPathFinder().runBagPathFindingForFile(file);
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
            e.printStackTrace();
            return;
        }

        // initialize algorithm with graph
        DijkstraAlgorithm algorithm = new DijkstraAlgorithm(graph);

        // initialize bag route best path request list
        List<BagRoute> bagRoutes = buildBagRouteRequests();

        // execute algorithm for each bag route request
        BagShortestPath bagShortestPath;
        String bagShortestPathId;
        List<Vertex> algorithmPath;
        int totalTravelTime;
        for (BagRoute bagRoute : bagRoutes) {

            System.out.println(">> bag route request: " + bagRoute);
            bagShortestPathId = BagShortestPath.buildPathIdFromRoute(bagRoute);

            if (bagShortestPathCache.containsPathForId(bagShortestPathId)) {
                bagShortestPath = bagShortestPathCache.getPathById(bagShortestPathId);
                System.out.println("cache hit for bag shortest path: " + bagShortestPath);
                bagRoute.setPath(bagShortestPath.getPath());
                bagRoute.setTotalTravelTime(bagShortestPath.getTravelTime());

            } else {
                System.out.println("cache miss, execute algorithm ...");
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
                    System.out.println("caching shortest path: " + bagShortestPath);
                    bagShortestPathCache.putPath(bagShortestPath);

                    if (node.getId().equals(bagRoute.getDestinationNode().getId())) {
                        bagRoute.setPath(algorithmPath);
                        bagRoute.setTotalTravelTime(totalTravelTime);
                        System.out.println("  -> recording path and travel time for current bag route request: " + bagRoute);
                    }
                }
            }

        }

        //todo: post-processing for bag route path result list
        System.out.println("Calculated bag routes:");
        for (BagRoute bagRoute : bagRoutes) {
            System.out.println(formatBagRoute(bagRoute));
        }
    }

    private boolean isFoundInCache(BagRoute bagRoute) {
        return false;
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
            nodeMap.put(nodeName, node);
        }
        return node;
    }

    private static void usage() {
        System.out.println("Usage: java -jar bagpathfinder-<version>.jar <data_file_name>");
    }


}
