package graph;

import java.util.*;

public class DepthFirstTraversal extends TraversalStrategy {
    public DepthFirstTraversal(AbstractGraph graph) {
        super(graph);
    }

    @Override
    public void traverseGraph(Vertex source, Vertex destination) {
        traverseGraph(source);
    }

    public void traverseGraph(Vertex source) {
        int sourceIndex = getGraph().getVertices().indexOf(source);
        addToPath(source);
        markVertexAsVisited(sourceIndex);
        setDistanceToVertex(sourceIndex, 0);
        setPredecessorVertexIndex(sourceIndex, -1);

        visitVertex(source);

        //printPath();
    }

    private void visitVertex(Vertex currentVertex) {
        int currentVertexIndex = getGraph().getVertices().indexOf(currentVertex);

        Vertex adjacentVertex = getGraph().getFirstConnectedVertex(currentVertex);
        while (adjacentVertex != null) {
            int adjacentVertexIndex = getGraph().getVertices().indexOf(adjacentVertex);
            if (!hasVertexBeenVisited(adjacentVertexIndex)) {
                updateTraversalInfoForVertex(adjacentVertexIndex, currentVertexIndex);
                visitVertex(adjacentVertex);
            }
            adjacentVertex = getGraph().getNextConnectedVertex(currentVertex, adjacentVertex);
        }
    }

    private void updateTraversalInfoForVertex(int newVertexIndex, int previousVertexIndex) {
        var newVertex = getGraph().getVertices().get(newVertexIndex);
        var oldVertex = getGraph().getVertices().get(previousVertexIndex);
        float newDistance = getGraph().getDistance(oldVertex, newVertex);
        float distance = getDistanceToVertex(previousVertexIndex) + newDistance;
        addToPath(newVertex);
        markVertexAsVisited(newVertexIndex);
        setDistanceToVertex(newVertexIndex, distance);
        setPredecessorVertexIndex(newVertexIndex, previousVertexIndex);
        setSuccessorVertexIndex(previousVertexIndex, newVertexIndex);
    }
}
