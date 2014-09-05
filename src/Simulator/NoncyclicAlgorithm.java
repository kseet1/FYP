/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import static Simulator.Vehicle.NORTH;
import static Simulator.Vehicle.SOUTH;
import static Simulator.Vehicle.EAST;
import static Simulator.Vehicle.WEST;
import java.util.LinkedList;

/**
 *
 * @author KSEET_000
 */
public class NoncyclicAlgorithm {

    private LinkedList<Vehicle> vehicles;
    private LinkedList<Node> visitedNodes = new LinkedList<>();
    private Map virtualMap;

    public NoncyclicAlgorithm(LinkedList<Vehicle> vehicles, Map current_map) {
        this.vehicles = vehicles;
        this.virtualMap = current_map;
        setupNoncyclicPath();
    }

    public void run() {

        for (Vehicle vehicle : vehicles) {
            Node currNode = virtualMap.getNode(vehicle.getXCoordinate(), vehicle.getYCoordinate());

            int xCoordinate = vehicle.getXCoordinate();
            int yCoordinate = vehicle.getYCoordinate();
            int direction = vehicle.getDirection();
            int currNodeIndex = visitedNodes.indexOf(currNode);
            Node nextNodeToVisit;
            if (!vehicle.isReversed()) {
                if (currNode == visitedNodes.peekLast()) {
                    nextNodeToVisit = visitedNodes.peekFirst();
                } else {
                    nextNodeToVisit = visitedNodes.get(currNodeIndex + 1);
                }
            } else {
                if (currNode == visitedNodes.peekFirst()) {
                    nextNodeToVisit = visitedNodes.peekLast();
                } else {
                    nextNodeToVisit = visitedNodes.get(currNodeIndex - 1);
                }
            }

            Node nextNode;
            Node leftNode;
            Node rightNode;

//            System.out.println("current node: ");
//            currNode.printCoordinates();
//            System.out.println("Next node to visit: ");
//            nextNodeToVisit.printCoordinates();
            switch (direction) {
                case NORTH:
                    nextNode = virtualMap.getNode(xCoordinate, yCoordinate - 1);
                    leftNode = virtualMap.getNode(xCoordinate - 1, yCoordinate);
                    rightNode = virtualMap.getNode(xCoordinate + 1, yCoordinate);
                    break;
                case EAST:
                    nextNode = virtualMap.getNode(xCoordinate + 1, yCoordinate);
                    leftNode = virtualMap.getNode(xCoordinate, yCoordinate - 1);
                    rightNode = virtualMap.getNode(xCoordinate, yCoordinate + 1);
                    break;
                case SOUTH:
                    nextNode = virtualMap.getNode(xCoordinate, yCoordinate + 1);
                    leftNode = virtualMap.getNode(xCoordinate + 1, yCoordinate);
                    rightNode = virtualMap.getNode(xCoordinate - 1, yCoordinate);
                    break;
                case WEST:
                    nextNode = virtualMap.getNode(xCoordinate - 1, yCoordinate);
                    leftNode = virtualMap.getNode(xCoordinate, yCoordinate + 1);
                    rightNode = virtualMap.getNode(xCoordinate, yCoordinate - 1);
                    break;
                default:
                    nextNode = null;
                    leftNode = null;
                    rightNode = null;
                    break;
            }

            if (nextNodeToVisit.isOccupied()) {
                vehicle.reverseDirection();
                if (currNode == visitedNodes.peekFirst()) {
                    vehicle.setDirection(EAST);
                }
            } else {
                if (nextNodeToVisit == nextNode) {
                    vehicle.move();
                    virtualMap.updateMovement(vehicle);
                } else if (leftNode == nextNodeToVisit) {
                    vehicle.turnLeft();
                } else if (rightNode == nextNodeToVisit) {
                    vehicle.turnRight();
                }
            }
        }
    }

    private void setupNoncyclicPath() {

        Vehicle setupVehicle = new Vehicle(1, 1, 1, SOUTH);
        Node currNode;
        Node startNode = virtualMap.getNode(1, 1);

        do {
            int xCoordinate = setupVehicle.getXCoordinate();
            int yCoordinate = setupVehicle.getYCoordinate();
            int direction = setupVehicle.getDirection();
            currNode = virtualMap.getNode(xCoordinate, yCoordinate);
            Node leftNode;
            Node rightNode;
            Node nextNode;

            if (!visitedNodes.contains(currNode)) {
                visitedNodes.add(currNode);
            }

            switch (direction) {
                case NORTH:
                    nextNode = virtualMap.getNode(xCoordinate, yCoordinate - 1);
                    leftNode = virtualMap.getNode(xCoordinate - 1, yCoordinate);
                    rightNode = virtualMap.getNode(xCoordinate + 1, yCoordinate);
                    break;
                case EAST:
                    nextNode = virtualMap.getNode(xCoordinate + 1, yCoordinate);
                    leftNode = virtualMap.getNode(xCoordinate, yCoordinate - 1);
                    rightNode = virtualMap.getNode(xCoordinate, yCoordinate + 1);
                    break;
                case SOUTH:
                    nextNode = virtualMap.getNode(xCoordinate, yCoordinate + 1);
                    leftNode = virtualMap.getNode(xCoordinate + 1, yCoordinate);
                    rightNode = virtualMap.getNode(xCoordinate - 1, yCoordinate);
                    break;
                case WEST:
                    nextNode = virtualMap.getNode(xCoordinate - 1, yCoordinate);
                    leftNode = virtualMap.getNode(xCoordinate, yCoordinate + 1);
                    rightNode = virtualMap.getNode(xCoordinate, yCoordinate - 1);
                    break;
                default:
                    nextNode = null;
                    leftNode = null;
                    rightNode = null;
                    break;
            }

            if (nextNode.isWall()) {    //next node is a wall 
                if (leftNode.isWall()) {
                    setupVehicle.turnRight();
                } else if (rightNode.isWall()) {
                    setupVehicle.turnLeft();
                } else {  //both left and right node is not a wall
                    //turn the vehicle's direction to the next highest priority node
                    //compare the last visited timestamp for each node

                    double currentTime = System.currentTimeMillis();

                    if ((currentTime - leftNode.lastVisited()) > (currentTime - rightNode.lastVisited())) {
                        setupVehicle.turnLeft();
                    } else {
                        setupVehicle.turnRight();
                    }
                }
            } else {    //nextNode is empty and not a wall
                if (setupVehicle.getDirection() == NORTH || setupVehicle.getDirection() == SOUTH) {
                    //vehicle is facing NORTH or SOUTH
                    setupVehicle.move();
                    virtualMap.updateMovement(setupVehicle);
                } else {  //vehicle is facing EAST or WEST
                    //compare if NORTH or SOUTH node is high priority
                    double currentTime = System.currentTimeMillis();

                    if (leftNode.isWall()) {
                        //compare the right node
                        if ((currentTime - rightNode.lastVisited()) >= (currentTime - nextNode.lastVisited())) {
                            setupVehicle.turnRight();
                        } else {
                            setupVehicle.move();
                            virtualMap.updateMovement(setupVehicle);
                        }
                    } else if (rightNode.isWall()) {
                        //compare the left node
                        if ((currentTime - leftNode.lastVisited()) >= (currentTime - nextNode.lastVisited())) {
                            //turn left as left node is of higher priority
                            setupVehicle.turnLeft();
                        } else {
                            setupVehicle.move();
                            virtualMap.updateMovement(setupVehicle);
                        }
                    }
                }
            }
            //virtualMap.printMap();
            currNode = virtualMap.getNode(setupVehicle.getXCoordinate(), setupVehicle.getYCoordinate());
        } while (currNode != startNode);
        virtualMap.getNode(1, 1).remove(setupVehicle);
    }
}
