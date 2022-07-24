package graph;

import javax.swing.*;
import java.util.List;
import java.util.Scanner;

import static java.awt.geom.Point2D.distance;

public class DungeonController {

    private AbstractGraph dungeon;

    private static final int TOTAL_LOCKS = 3;
    private Room entrance;
    private Room exit;

    public static void main(String[] args) {
        DungeonController dungeonController = new DungeonController();
        createRandomDungeon(dungeonController);
        DelaunayTriangulation.triangulateGraphVertices(dungeonController.dungeon);
        ReplaceDungeonWithMST(dungeonController);
        setSpecialRooms(dungeonController);
        List<Vertex> traversalPath = getPathFromEntranceToExit(dungeonController);
        setLocksAndKeys(dungeonController);
        // printDungeonPath(dungeonController);
        SwingUtilities.invokeLater(() -> new DungeonGraphic(dungeonController.dungeon, traversalPath).setVisible(true));
    }

    private static void printDungeonPath(DungeonController dungeonController) {
        AbstractGraph dungeon = dungeonController.dungeon;
        TraversalStrategy traversalStrategy;
        traversalStrategy = new BreadthFirstTraversal(dungeon);
        Vertex source = dungeon.getVertices().get(0);
        traversalStrategy.traverseGraph(source);
    }

    private static void setLocksAndKeys(DungeonController dungeonController) {
        AbstractGraph dungeon = dungeonController.dungeon;
        TraversalStrategy traversalStrategy;
        traversalStrategy = new KeyLockGenerator(dungeon, TOTAL_LOCKS);
        traversalStrategy.traverseGraph(dungeonController.entrance);
    }

    private static void createRandomDungeon(DungeonController dungeonController) {
        Scanner scanner = new Scanner(System.in);
        int seed = Integer.parseInt(scanner.nextLine());
        RandomSingleton.getInstance(seed);
        int nRooms = Integer.parseInt(scanner.nextLine());
        var randomDungeonGenerator = new DungeonGenerator(nRooms);
        dungeonController.dungeon = randomDungeonGenerator.getDungeon();
    }

    private static void ReplaceDungeonWithMST(DungeonController dungeonController) {
        AbstractGraph dungeon = dungeonController.dungeon;
        TraversalStrategy traversalStrategy;
        traversalStrategy = new PrimMSTTraversal(dungeon);
        traversalStrategy.traverseGraph(dungeon.getVertices().get(0));
        dungeonController.dungeon = GraphConverter.predecessorListToGraph(dungeon,
                traversalStrategy.getPredecessorArray());
    }

    private static void setSpecialRooms(DungeonController dungeonController) {
        AbstractGraph dungeon = dungeonController.dungeon;
        TraversalStrategy traversalStrategy = new FloydWarshallTraversal(dungeon);
        traversalStrategy.traverseGraph(dungeon.getVertices().get(0));
        Room center = (Room) dungeon
                .getCentermostVertex(((FloydWarshallTraversal) traversalStrategy).getDistanceMatrix());
        center.setCheckPoint(true);
        Room entrance = (Room) dungeon
                .getOuterMostVertex(((FloydWarshallTraversal) traversalStrategy).getDistanceMatrix());
        entrance.setEntrance(true);
        dungeonController.entrance = entrance;
        Room exit = (Room) dungeon
                .getMostDistantVertex(((FloydWarshallTraversal) traversalStrategy).getDistanceMatrix(), entrance);
        exit.setExit(true);
        dungeonController.exit = exit;
    }

    private static List<Vertex> getPathFromEntranceToExit(DungeonController dungeonController) {
        AbstractGraph dungeon = dungeonController.dungeon;

        int numOfVertices = dungeon.getNumberOfVertices();
        float[] heuristic = new float[numOfVertices];
        var p1 = dungeonController.exit.getPoint();
        for(int i = 0; i < numOfVertices; i++){
            var p2 = ((Room)dungeon.getVertices().get(i)).getPoint();
            heuristic[i] = (float)distance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            // heuristic[i] *= heuristic[i];
            // heuristic[i] *= 2;
        }

        TraversalStrategy traversalStrategy = new AStarPathFind(dungeon, heuristic);
        //TraversalStrategy traversalStrategy = new DepthFirstTraversal(dungeon);
        //TraversalStrategy traversalStrategy = new BreadthFirstTraversal(dungeon);
        traversalStrategy.traverseGraph(dungeonController.entrance, dungeonController.exit);
        return traversalStrategy.getShortestPath(dungeonController.entrance, dungeonController.exit);
        //return traversalStrategy.getTraversalPath();
    }
}
