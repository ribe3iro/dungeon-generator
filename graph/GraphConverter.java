package graph;

import java.util.logging.Logger;

class GraphConverter 
{
  private static final Logger LOGGER = Logger.getLogger("GraphConverter.class");

  public static AbstractGraph predecessorListToGraph(AbstractGraph dungeon, int[] predecessorArray) 
  {
    AbstractGraph newDungeon = null;
    try{
      newDungeon = dungeon.clone();
      newDungeon.removeAllEdges();

      for (int i = 1; i < predecessorArray.length; i++){
        int destinationIndex = i;
        int sourceIndex = predecessorArray[i];
  
        Vertex destination = dungeon.getVertices().get(destinationIndex);
        Vertex source = dungeon.getVertices().get(sourceIndex);
        float distance = dungeon.getDistance(source, destination);
  
        System.out.println(distance);
      }

      for (int i = 1; i < predecessorArray.length; i++){
        int destinationIndex = i;
        int sourceIndex = predecessorArray[i];

        Vertex destination = newDungeon.getVertices().get(destinationIndex);
        Vertex source = newDungeon.getVertices().get(sourceIndex);
        float distance = dungeon.getDistance(source, destination);

        newDungeon.addEdge(source, destination, distance);
      }
    }catch(Exception e){
      LOGGER.info("Erro ao clonar dungeon\nErro: "+e.getMessage());
    }
    return newDungeon;
  }
}