
BagPathFinder - airport bag routing shortest path finding utility

Version: 1.0 (2015-04-18)
Author: Bin Le (binle2002@hotmail.com)


Coding problem: Airport Baggage Path Finding

Denver International Airport has decided to give an automated baggage system another shot. The hardware and tracking systems from the previous attempt are still in place, they just need a system to route the baggage.  The system will route baggage checked, connecting, and terminating in Denver.

You have been asked to implement a system that will route bags to their flights or the proper baggage claim.  The input describes the airport conveyor system, the departing flights, and the bags to be routed.  The output is the optimal routing to get bags to their destinations.  Bags with a flight id of “ARRIVAL” are terminating in Denver are routed to Baggage Claim.

Input: The input consists of several sections.  The beginning of each section is marked by a line starting: “# Section:”
Section 1: A weighted bi-directional graph describing the conveyor system.
Format: <Node 1> <Node 2> <travel_time>
Section 2: Departure list
           Format: <flight_id> <flight_gate> <destination> <flight_time>
Section 3: Bag list
           Format: <bag_number> <entry_point> <flight_id>

Output: The optimized route for each bag
<Bag_Number> <point_1> <point_2> [<point_3>, …] : <total_travel_time>
The output should be in the same order as the Bag list section of the input.

Example Input:
# Section: Conveyor System
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
# Section: Departures
UA10 A1 MIA 08:00
UA11 A1 LAX 09:00
UA12 A1 JFK 09:45
UA13 A2 JFK 08:30
UA14 A2 JFK 09:45
UA15 A2 JFK 10:00
UA16 A3 JFK 09:00
UA17 A4 MHT 09:15
UA18 A5 LAX 10:15
# Section: Bags
0001 Concourse_A_Ticketing UA12
0002 A5 UA17
0003 A2 UA10
0004 A8 UA18
0005 A7 ARRIVAL
Example Output:
0001 Concourse_A_Ticketing A5 A1 : 11
0002 A5 A1 A2 A3 A4 : 9
0003 A2 A1 : 1
0004 A8 A9 A10 A5 : 6
0005 A7 A8 A9 A10 A5 BaggageClaim : 12


Working Process

Assumptions
- oracle jdk 1.7 is ok
- using maven for build and packaging is ok
- using junit for unit testing is ok
- a typical java software utility should have log support like log4j to support development and trouble shooting. Since the exercise request not using external libraries for development, log4j is not included in this programming exercise
- each node has no limitation of number of bag conveyors starting/passing at the same time, in other words, no concurrent capacity limit
- there are bag conveyors at each starting node for bag transfer, in other words, there's no additional waiting time cost from one bag routing request to another
- flight departure time in departure section is not directly used in path finding, the possible use of it is to take current system time into account to check if remaining time window is enough for the travel time of even the shortest path found, but this is not in scope of this exercise
- the data provided from the data section file is correct, and always holds the same data format, and the path finder program does not try to guess or fix the input data

Thinking Process
- this is basically a shortest path problem on a weighted bidirectional graph
- the graph is G(V, E) where V(ertex) is the node, and E(dge) is the path with weight (travel time)
- graph is bidirectional, so each input edge should be duplicated as a pair with same distance (weight)
- use Dijkstra algorithm to calculate the shortest path for a given pair of source and destination node
    - Floyd-Warshall algorithm (http://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm) is also popular for similar problem, but its up-front computational complexity O(N^3) is higher than Dijkstra, also it seems to me its result data structure is more difficult to cache and lookup.
    - Dijkstra and Floyd-Warshal converge to same computational complexity when shortest paths are needed for all the nodes being source and destination, aka the path finding is exhaustive
    - I personally like just-in-time type of performance design better, Dijkstra algorithm is easier to work with to cache its calculation results
- since Dijkstra algorithm calculates shortest path from one source node at a time and result in shortest paths from source node to all other nodes
    - it should run according to each bag conveyor route request’s source node
    - for performance, all calculated shortest paths are cached with key like (source_node, destination_code) for future routing requests
    - shortest path cache is growing with new routing requests, until all the possible shortest paths are covered

Data Model and Bag Path Finding Logic Design
- G(graph) has a list of V(ertex)/node and a list of E(edges)
- Vertex represents the node with id and name, it should have custom equals and hashCode to support Set operation in Dijkstra algoirthm
- Edge has sourceVertex and destinationVertex, and a weight (travel time)
- input file conveyor section data list is used to build graph
- input file departure and bag section data lists together are used to build route request:
    - bag.startNode => sourceVertex
    - bag.flightLabel => departure.flightLabel => departure.destinationNode => destinationVertex
    - when bag.flightLabel is 'ARRIVAL', the destinationNode is set to 'BaggageClaim'
- BagRoute model to hold route request (source, destination), resulting shortest path (list of node steps) and total travel time.
- BagShortestPath model to hold calculated shortest path for future requests
- BagShortestPathCache to hold a lookup map/dictionary of BagShortestPath records
- logic of performance improvement: for a given graph with fixed number of nodes (N), there are at most (N-1)*N possible combination of (sourceNode, destinationNode). For bidirectional graph there are (N-1)*N/2 unique paths. When Djikstra algorithm calculates for one source node, it results in all N-1 shortest paths from this source node to all the rest of the nodes. If all N source nodes are run there will be N*(N-1) shortest paths covering all routing request combinations.
- logic of adaptive performance improvement: as from (8) we know there only needs to be up to N algorithm runs for N-node graph to cache all shortest paths from any source node to any destination node. The paths could be cached as just-in-time: for any new routing request, check if the (source, destination) pair is already in the cache, if not, run the algorithm and cache all the N-1 paths, otherwise just look up the cache for the path.
- some file IO and formatting utility methods, basic exception handling

System Work Flow Design
- need two group of classes, Dijkstra algorithm package and bag routing package
- need file reader to read in three sections of data: conveyor, departure and bag
- use conveyor section data to build the graph and initialize Dijkstra algorithm with graph
- use departure and bag sections to build bag (source, destination) routing requests
- loop through requests and lookup the shortest path cache, for a cache miss call algorithm to calculate the paths and cache them, otherwise get path from cache
- format path results and print them out

Packaging
- compile code in jar package with entry of bcus.bag.BagPathFinder.main() method
- taking one argument to specify input file with required format
- result is printed out to the terminal console with required format
- usage example: java -jar bagpathfinder-1.0.jar 'sample_data.txt'





