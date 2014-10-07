/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import java.util.LinkedList;
import static Simulator.Vehicle.EAST;
import static Simulator.Vehicle.NORTH;
import static Simulator.Vehicle.SOUTH;
import static Simulator.Vehicle.WEST;
import java.awt.Color;

/**
 *
 * @author KSEET_000
 */
public class CyclicAlgorithm {

    private LinkedList<Vehicle> vehicles;
    private LinkedList<Node> visitedNodes = new LinkedList<>();
    //private Node keyNode;
    private Node startNode;
    private Map virtualMap;

    public CyclicAlgorithm(LinkedList<Vehicle> vehicles, Map current_map) {
        this.vehicles = vehicles;
        this.virtualMap = current_map;
        startNode = virtualMap.getNode(1, 1);
        setupCyclicPath();
    }

    public void run() {

        for (Vehicle vehicle : vehicles) {
            int turns = vehicle.getSpeed(); //the vehicle speed determines the number of turns it has to move.
            while (turns != 0) {
                Node currNode = virtualMap.getNode(vehicle.getXCoordinate(), vehicle.getYCoordinate());

                int xCoordinate = vehicle.getXCoordinate();
                int yCoordinate = vehicle.getYCoordinate();
                int direction = vehicle.getDirection();
                Node nextNodeToVisit = getNextNode(vehicle, currNode);
                
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
                    // reverse the direction of the other vehicle as well
                    // reverse the direction if and only if that vehicle is facing the current vehicle
                    Vehicle tmp = nextNodeToVisit.getVehicles().peekFirst();
                    if (currNode == getNextNode(tmp, virtualMap.getNode(tmp.getXCoordinate(), tmp.getYCoordinate()))) {
                        tmp.reverseDirection();
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
                turns--;
            }
        }
    }

    //a private method to setup the path of the cyclic algorithm (forming a hamiltonian path)
    private void setupCyclicPath() {

        Vehicle setupVehicle = new Vehicle(1, 1, 1, SOUTH, Color.BLUE);
        Node currNode;
        Node startNode = virtualMap.getNode(1, 1);
        Node keyNode = virtualMap.getNode(virtualMap.getXDimension() - 2, 2);
        setReturnPath(1, 1);

        do {
            //System.out.println("Setting up");
            if (keyNode.isVisited()) {
                clearReturnPath(1, 1);
            }

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

            if (nextNode.isWall() || nextNode.isReturnNode()) {    //next node is a wall 
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
            } else {    //nextNode is empty, not a wall or a returnNode
                if (setupVehicle.getDirection() == NORTH || setupVehicle.getDirection() == SOUTH) {
                    setupVehicle.move();
                    virtualMap.updateMovement(setupVehicle);
                } else {  //vehicle is facing EAST or WEST
                    double currentTime = System.currentTimeMillis();

                    //compare the priorities of nextNode with left and right nodes
                    //this is done by comparing the timestamps of the visits on each nodes
                    if (leftNode.isWall() || leftNode.isReturnNode()) {
                        if ((currentTime - rightNode.lastVisited()) >= (currentTime - nextNode.lastVisited())) {
                            setupVehicle.turnRight();
                        } else {
                            setupVehicle.move();
                            virtualMap.updateMovement(setupVehicle);
                        }
                    } else if (rightNode.isWall() || rightNode.isReturnNode()) {
                        if ((currentTime - leftNode.lastVisited()) >= (currentTime - nextNode.lastVisited())) {
                            setupVehicle.turnLeft();
                        } else {
                            setupVehicle.move();
                            virtualMap.updateMovement(setupVehicle);
                        }
                    }
                }
            }
            currNode = virtualMap.getNode(setupVehicle.getXCoordinate(), setupVehicle.getYCoordinate());
            //currNode.printCoordinates();
        } while (currNode != startNode);
        //remove setupVehicle
        virtualMap.getNode(1, 1).remove(setupVehicle);
    }

    //a private method to allocate the path of the vehicles to return.
    //this is to prevent the vehicles from traversing to the return path
    //necessary for cyclic algorithm
    private void setReturnPath(int startX, int startY) {
        for (int i = startX; i < virtualMap.getXDimension() - 2; i++) {
            virtualMap.getNode(i, startY).setReturnNode(true);
        }
    }

    private void clearReturnPath(int startX, int startY) {
        for (int i = startX; i < virtualMap.getXDimension() - 2; i++) {
            virtualMap.getNode(i, startY).setReturnNode(false);
        }
    }
    
    private Node getNextNode(Vehicle vehicle, Node currNode) {
         if (!vehicle.isReversed()) {
                    if (currNode == visitedNodes.peekLast()) {
                        return visitedNodes.peekFirst();
                    } else {
                        return visitedNodes.get(visitedNodes.indexOf(currNode) + 1);
                    }
                } else {
                    if (currNode == visitedNodes.peekFirst()) {
                        return visitedNodes.peekLast();
                    } else {
                        return visitedNodes.get(visitedNodes.indexOf(currNode) - 1);
                    }
                }
    }
}
