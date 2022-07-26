package graph;

import java.util.*;

public class DijkstraTraversal extends TraversalStrategy{

    private List<Integer> indexesToVisit;
    public DijkstraTraversal(AbstractGraph graph)
    {
        super(graph);
        indexesToVisit = new ArrayList<>();
        for(int i = 0; i < graph.getNumberOfVertices(); i++){
            indexesToVisit.add(i);
        }
    }

    @Override
    void traverseGraph(Vertex source, Vertex destination)
    {
        traverseGraph(source);
    }

    public void traverseGraph(Vertex source) {

        int sourceIndex = getGraph().getVertices().indexOf(source);
        markVertexAsVisited(sourceIndex);
        setDistanceToVertex(sourceIndex, 0);
        setPredecessorVertexIndex(sourceIndex, -1);

        List<Vertex> visitedVertices = new LinkedList<>();
        visitedVertices.add(source);

        var currentVisitedVertex = source;
        int currentVisitedVertexIndex = getGraph().getVertices().indexOf(currentVisitedVertex);
        while(indexesToVisit.size() > 0)
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
                        }
                    }
                    adjacentVertex = getGraph().getNextConnectedVertex(currentVisitedVertex, adjacentVertex);
                }
            }
            currentVisitedVertexIndex = getLowestDistance();
            if(currentVisitedVertexIndex != -1){
                currentVisitedVertex = getGraph().getVertices().get(currentVisitedVertexIndex);
                visitedVertices.add(currentVisitedVertex);
            }
        }
        printDistances();
    }

    private int getLowestDistance()
    {
        int closestVertexIndex = indexesToVisit.stream()
                                               .min((a, b) -> {
                                                    float distanceA = getDistanceToVertex(a);
                                                    float distanceB = getDistanceToVertex(b);

                                                    return (int)(distanceA - distanceB);
                                                })
                                               .orElse(-1);

        indexesToVisit.remove(Integer.valueOf(closestVertexIndex));
        return closestVertexIndex;
    }
}
