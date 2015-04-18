package bcus.dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ble on 4/15/15.
 */
public class DijkstraAlgorithm {
    private final List<Vertex> nodes;
    private final List<Edge> edges;
    private Set<Vertex> settledNodes;
    private Set<Vertex> unSettledNodes;
    private Map<Vertex, Vertex> predecessors;
    private Map<Vertex, Integer> distance;

    /**
     * constructor to initailize algorithm with a Graph object
     */
    public DijkstraAlgorithm(Graph graph) {
        this.nodes = graph.getVertexes();
        this.edges = graph.getEdges();
    }

    /**
     * run the initialized algorithm to calculate all shortest paths from the given source node
     * @param source  the source node (vertex)
     */
    public void execute(Vertex source) {
        settledNodes = new HashSet<Vertex>();
        unSettledNodes = new HashSet<Vertex>();
        distance = new HashMap<Vertex, Integer>();
        predecessors = new HashMap<Vertex, Vertex>();
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Vertex node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Vertex node) {
        List<Vertex> adjacentNodes = getNeighbors(node);
        for (Vertex target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }
    }

    /**
     * Find the distance via the edge with given start and end nodes.
     * There must be such an edge available
     * @throws RuntimeException when the matching edge is not found
     */
    private int getDistance(Vertex node, Vertex target) {
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && edge.getDestination().equals(target)) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Data error: matching edge not found from: " + node + " to: " + target);
    }

    /**
     * Gather neighbors of the given node from the unSettled node set
     */
    private List<Vertex> getNeighbors(Vertex node) {
        List<Vertex> neighbors = new ArrayList<Vertex>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && !isSettled(edge.getDestination())) {  // only from unsettled node set
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }

    /**
     * Finds the node with the shortest distance to the source node
     * @param vertexes  the node set
     * @return  node with shortest distance to the source node
     */
    private Vertex getMinimum(Set<Vertex> vertexes) {
        Vertex minimum = null;
        for (Vertex vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Vertex vertex) {
        return settledNodes.contains(vertex);
    }

    /**
     * Look up the recorded shortest distance for given destination, if not found, return Infinity
     */
    private int getShortestDistance(Vertex destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    /**
     * Calculate the path from the source node to the given target node.
     * Path is calculated by back tracing the steps from the target node.
     * @param target
     * @return  a list of nodes from source node to target node node
     */
    public LinkedList<Vertex> getPath(Vertex target) {
        LinkedList<Vertex> path = new LinkedList<Vertex>();
        Vertex step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // reverse the steps to give a path from source to target
        Collections.reverse(path);
        return path;
    }

    public int getPathDistance(List<Vertex> path) {
        if (path == null) {
            return 0;   // there's no path, source and destination must be the same in this case
        }

        Iterator<Vertex> pathIterator = path.iterator();
        Vertex fromNode = pathIterator.next();
        Vertex toNode;
        int pathDistance = 0;
        while (pathIterator.hasNext()) {
            toNode = pathIterator.next();
            pathDistance += getDistance(fromNode, toNode);
            fromNode = toNode;
        }
        return pathDistance;
    }






}
