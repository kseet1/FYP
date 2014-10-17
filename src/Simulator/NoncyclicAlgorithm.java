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
import java.awt.Color;
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
                    // reverse the direction of the other vehicle as well
                    // reverse the direction if and only if that vehicle is facing the current vehicle
                    Vehicle tmp = nextNodeToVisit.getVehicles().peekFirst();
                    if (currNode == getNextNode(tmp, virtualMap.getNode(tmp.getXCoordinate(), tmp.getYCoordinate()))) {
                        tmp.reverseDirection();
                    }
                } else if ((currNode == visitedNodes.peekFirst() || currNode == visitedNodes.peekLast()) && nextNodeToVisit.isWall()) {
                    vehicle.reverseDirection();
                    vehicle.setDirection(SOUTH);
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

    private void setupNoncyclicPath() {

        Vehicle setupVehicle = new Vehicle(1, 1, 1, SOUTH, Color.BLUE);
        virtualMap.updateMovement(setupVehicle);
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

            if (visitedNodes.contains(currNode)) {
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
                    visitedNodes.add(currNode);
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
                            visitedNodes.add(currNode);
                        }
                    } else if (rightNode.isWall()) {
                        //compare the left node
                        if ((currentTime - leftNode.lastVisited()) >= (currentTime - nextNode.lastVisited())) {
                            //turn left as left node is of higher priority
                            setupVehicle.turnLeft();
                        } else {
                            setupVehicle.move();
                            virtualMap.updateMovement(setupVehicle);
                            visitedNodes.add(currNode);
                        }
                    }
                }
            }
            //virtualMap.printMap();
            //currNode.printCoordinates();
            //currNode = virtualMap.getNode(setupVehicle.getXCoordinate(), setupVehicle.getYCoordinate());
        } while (visitedNodes.size() != virtualMap.size());
        int x = setupVehicle.getXCoordinate();
        int y = setupVehicle.getYCoordinate();
        virtualMap.getNode(x, y).remove(setupVehicle);
    }

    private Node getNextNode(Vehicle vehicle, Node currNode) {
        if (!vehicle.isReversed()) {
            if (visitedNodes.indexOf(currNode) + 1 >= virtualMap.size()) {
                vehicle.reverseDirection();
                return visitedNodes.get(visitedNodes.indexOf(currNode) - 1);
            }
            else
                return visitedNodes.get(visitedNodes.indexOf(currNode) + 1);
        } else {
            if (visitedNodes.indexOf(currNode) - 1 < 0) {
                vehicle.reverseDirection();
                return visitedNodes.get(visitedNodes.indexOf(currNode) + 1);
            }
            return visitedNodes.get(visitedNodes.indexOf(currNode) - 1);
        }
    }
}
