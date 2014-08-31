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

/**
 *
 * @author KSEET_000
 */
public class CyclicAlgorithm {

    private LinkedList<Vehicle> vehicles;
    private LinkedList<Node> visitedNodes = new LinkedList<Node>();
    //private Node keyNode;
    private Node startNode;
    private Map virtualMap;

    public CyclicAlgorithm(LinkedList<Vehicle> vehicles, Map current_map) {
        this.vehicles = vehicles;
        this.virtualMap = current_map;
        //setReturnPath(1, 1);
        //this.keyNode = virtualMap.getNode(virtualMap.getXDimension() - 2, 2);
        startNode = virtualMap.getNode(1, 1);
        setupCyclicPath();
    }

    public void run() {

        //if (visitedNodes.size() == virtualMap.size()) {
        //vehicle have traversed the entire map
        //use the recorded traversed nodes as the path for the algorithm
        for (Vehicle vehicle : vehicles) {
            Node currNode = virtualMap.getNode(vehicle.getXCoordinate(), vehicle.getYCoordinate());

            int xCoordinate = vehicle.getXCoordinate();
            int yCoordinate = vehicle.getYCoordinate();
            int direction = vehicle.getDirection();
            int currNodeIndex = visitedNodes.indexOf(currNode);
            Node nextNodeToVisit;
            if (currNode == visitedNodes.peekLast()) {
                nextNodeToVisit = virtualMap.getNode(startNode.getX(), startNode.getY());
            } else {
                nextNodeToVisit = visitedNodes.get(currNodeIndex + 1);
            }
            Node nextNode;
            Node leftNode;
            Node rightNode;

            System.out.println("current node: ");
            currNode.printCoordinates();
            System.out.println("Next node to visit: ");
            nextNodeToVisit.printCoordinates();
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

            if (nextNodeToVisit == nextNode) {
                vehicle.move();
                virtualMap.updateMovement(vehicle);
            } else if (leftNode == nextNodeToVisit) {
                vehicle.turnLeft();
            } else if (rightNode == nextNodeToVisit) {
                vehicle.turnRight();
            }

        }

//        } else {
//
//            if (this.keyNode.isOccupied()) {
//                clearReturnPath(1, 1);
//            }
//
//            for (Vehicle vehicle : vehicles) {
//                int xCoordinate = vehicle.getXCoordinate();
//                int yCoordinate = vehicle.getYCoordinate();
//                int direction = vehicle.getDirection();
//                Node currNode = virtualMap.getNode(xCoordinate, yCoordinate);
//                Node leftNode;
//                Node rightNode;
//                Node nextNode;
//
//                if (!visitedNodes.contains(currNode)) {
//                    visitedNodes.add(currNode);
//                }
//
//                switch (direction) {
//                    case NORTH:
//                        nextNode = virtualMap.getNode(xCoordinate, yCoordinate - 1);
//                        leftNode = virtualMap.getNode(xCoordinate - 1, yCoordinate);
//                        rightNode = virtualMap.getNode(xCoordinate + 1, yCoordinate);
//                        break;
//                    case EAST:
//                        nextNode = virtualMap.getNode(xCoordinate + 1, yCoordinate);
//                        leftNode = virtualMap.getNode(xCoordinate, yCoordinate - 1);
//                        rightNode = virtualMap.getNode(xCoordinate, yCoordinate + 1);
//                        break;
//                    case SOUTH:
//                        nextNode = virtualMap.getNode(xCoordinate, yCoordinate + 1);
//                        leftNode = virtualMap.getNode(xCoordinate + 1, yCoordinate);
//                        rightNode = virtualMap.getNode(xCoordinate - 1, yCoordinate);
//                        break;
//                    case WEST:
//                        nextNode = virtualMap.getNode(xCoordinate - 1, yCoordinate);
//                        leftNode = virtualMap.getNode(xCoordinate, yCoordinate + 1);
//                        rightNode = virtualMap.getNode(xCoordinate, yCoordinate - 1);
//                        break;
//                    default:
//                        nextNode = null;
//                        leftNode = null;
//                        rightNode = null;
//                        break;
//                }
//
//                if (nextNode.isOccupied()) {
//                    //for the bump and reverse algorithm - if the vehicle meets another vehicle, reverse direction
//                    vehicle.reverseDirection();
//                } else if (nextNode.isWall() || nextNode.isReturnNode()) {    //next node is a wall 
//                    if (leftNode.isWall()) {
//                        vehicle.turnRight();
//                    } else if (rightNode.isWall()) {
//                        vehicle.turnLeft();
//                    } else {  //both left and right node is not a wall
//                        //turn the vehicle's direction to the next highest priority node
//                        //compare the last visited timestamp for each node
//
//                        double currentTime = System.currentTimeMillis();
//
//                        if ((currentTime - leftNode.lastVisited()) > (currentTime - rightNode.lastVisited())) {
//                            vehicle.turnLeft();
//                        } else {
//                            vehicle.turnRight();
//                        }
//                    }
//                } else {    //nextNode is empty, not a wall or a returnNode
//                    if (vehicle.getDirection() == NORTH || vehicle.getDirection() == SOUTH) {
//                        vehicle.move();
//                        virtualMap.update(vehicle);
//                    } else {  //vehicle is facing EAST or WEST
//                        double currentTime = System.currentTimeMillis();
//
//                        //compare the priorities of nextNode with left and right nodes
//                        //this is done by comparing the timestamps of the visits on each nodes
//                        if (leftNode.isWall() || leftNode.isReturnNode()) {
//                            if ((currentTime - rightNode.lastVisited()) >= (currentTime - nextNode.lastVisited())) {
//                                vehicle.turnRight();
//                            } else {
//                                vehicle.move();
//                                virtualMap.update(vehicle);
//                            }
//                        } else if (rightNode.isWall() || rightNode.isReturnNode()) {
//                            if ((currentTime - leftNode.lastVisited()) >= (currentTime - nextNode.lastVisited())) {
//                                vehicle.turnLeft();
//                            } else {
//                                vehicle.move();
//                                virtualMap.update(vehicle);
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    //a private method to setup the path of the cyclic algorithm
    private void setupCyclicPath() {

        Vehicle setupVehicle = new Vehicle(1, 1, 1, SOUTH);
        Node currNode;
        Node startNode = virtualMap.getNode(1, 1);
        Node keyNode = virtualMap.getNode(virtualMap.getXDimension() - 2, 2);
        setReturnPath(1, 1);

        do {
            System.out.println("Setting up");
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
            currNode.printCoordinates();
        } while (currNode != startNode);
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
}
