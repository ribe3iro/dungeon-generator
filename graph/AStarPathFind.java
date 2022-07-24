package graph;

import java.util.*;

public class AStarPathFind extends TraversalStrategy{

    private List<Integer> indexesToVisit;
    private float[] heuristic;

    public AStarPathFind(AbstractGraph graph, float[] heuristic)
    {
        super(graph);
        this.heuristic = heuristic;
        indexesToVisit = new ArrayList<>();
        for(int i = 0; i < graph.getNumberOfVertices(); i++){
            indexesToVisit.add(i);
        }
    }

    public void traverseGraph(Vertex source) throws IllegalArgumentException{
        throw new IllegalArgumentException("Illegal argument: Provide at least 2 vertex for this method!");
    }

    public void traverseGraph(Vertex source, Vertex destination) {

        int sourceIndex = getGraph().getVertices().indexOf(source);
        markVertexAsVisited(sourceIndex);
        setDistanceToVertex(sourceIndex, 0);
        setPredecessorVertexIndex(sourceIndex, -1);

        List<Vertex> visitedVertices = new LinkedList<>();
        visitedVertices.add(source);

        var currentVisitedVertex = source;
        int currentVisitedVertexIndex = getGraph().getVertices().indexOf(currentVisitedVertex);
        while(currentVisitedVertex != destination && indexesToVisit.size() > 0)
        {
            if (currentVisitedVertex != null)
            {
                var adjacentVertex = getGraph().getFirstConnectedVertex(currentVisitedVertex);
                while (adjacentVertex != null)
                {
                    int adjacentVertexIndex = getGraph().getVertices().indexOf(adjacentVertex);
                    if (!visitedVertices.contains(adjacentVertex))
                    {
                        float newDistance = getGraph().getDistance(currentVisitedVertex, adjacentVertex) + getDistanceToVertex(currentVisitedVertexIndex);
                        if(newDistance < getDistanceToVertex(adjacentVertexIndex))
                        {
                            setDistanceToVertex(adjacentVertexIndex, newDistance);
                            setPredecessorVertexIndex(adjacentVertexIndex, currentVisitedVertexIndex);
                        }
                    }
                    adjacentVertex = getGraph().getNextConnectedVertex(currentVisitedVertex, adjacentVertex);
                }
            }
            currentVisitedVertexIndex = getLowestDistance();
            if(currentVisitedVertexIndex != -1){
                currentVisitedVertex = getGraph().getVertices().get(currentVisitedVertexIndex);
                addToPath(currentVisitedVertex);
                visitedVertices.add(currentVisitedVertex);
            }
        }

        printPath();

        if(currentVisitedVertex != destination){
            printMessage("Destination is unreachable!");
        }
    }

    private int getLowestDistance()
    {
        int closestVertexIndex = indexesToVisit.stream()
                                               .min((a, b) -> {
                                                    float distanceA = getDistanceToVertex(a) + heuristic[a];
                                                    float distanceB = getDistanceToVertex(b) + heuristic[b];

                                                    return (int)(distanceA - distanceB);
                                                })
                                               .orElse(-1);

        indexesToVisit.remove(Integer.valueOf(closestVertexIndex));
        return closestVertexIndex;
    }
}
