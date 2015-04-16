package bcus.bag;

import bcus.dijkstra.Vertex;

import java.util.List;

/**
 * Created by ble on 4/16/15.
 */
public class BagRoute {

    private final String requestIndex;
    private final Vertex sourceNode;
    private final Vertex destinationNode;
    private final String flightLabel;

    List<Vertex> path;
    int totalTravelTime;

    BagRoute(String requestIndex, Vertex sourceNode, Vertex destinationNode, String flightLabel) {
        this.requestIndex = requestIndex;
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.flightLabel = flightLabel;
    }

    public String getRequestIndex() {
        return requestIndex;
    }

    public Vertex getSourceNode() {
        return sourceNode;
    }

    public Vertex getDestinationNode() {
        return destinationNode;
    }

    public String getFlightLabel() {
        return flightLabel;
    }

    public List<Vertex> getPath() {
        return path;
    }

    public void setPath(List<Vertex> path) {
        this.path = path;
    }

    public int getTotalTravelTime() {
        return totalTravelTime;
    }

    public void setTotalTravelTime(int totalTravelTime) {
        this.totalTravelTime = totalTravelTime;
    }

    @Override
    public String toString() {
        return "BagRoute{" +
                "requestIndex='" + requestIndex + '\'' +
                ", sourceNode=" + sourceNode +
                ", destinationNode=" + destinationNode +
                ", flightLabel='" + flightLabel + '\'' +
                ", path=" + path +
                ", totalTravelTime=" + totalTravelTime +
                '}';
    }
}
