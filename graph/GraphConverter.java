package graph;

import java.util.logging.Logger;

class GraphConverter 
{
  private static final Logger LOGGER = Logger.getLogger("GraphConverter.class");

  public static AbstractGraph predecessorListToGraph(AbstractGraph graph, int[] predecessorArray) 
  {
    AbstractGraph newGraph = null;
    try{
      newGraph = graph.clone();
      newGraph.removeAllEdges();

      for (int i = 1; i < predecessorArray.length; i++){
        int destinationIndex = i;
        int sourceIndex = predecessorArray[i];

        Vertex destination = newGraph.getVertices().get(destinationIndex);
        Vertex source = newGraph.getVertices().get(sourceIndex);
        float distance = graph.getDistance(source, destination);

        newGraph.addEdge(source, destination, distance);
      }
    }catch(Exception e){
      LOGGER.info("Erro ao gerar novo grafo\nErro: "+e.getMessage());
    }
    return newGraph;
  }
}