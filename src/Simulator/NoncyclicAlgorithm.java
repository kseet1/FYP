/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import java.util.LinkedList;

/**
 *
 * @author KSEET_000
 */
public class NoncyclicAlgorithm {

    static final int NORTH = 1;
    static final int EAST = 2;
    static final int SOUTH = 3;
    static final int WEST = 4;

    private LinkedList<Vehicle> vehicles;
    private LinkedList<Node> visitedNodes = new LinkedList<Node>();
    private Map virtualMap;

    public NoncyclicAlgorithm(LinkedList<Vehicle> vehicles, Map current_map) {
        this.vehicles = vehicles;
        this.virtualMap = current_map;
    }

    public void run() {

        for (Vehicle vehicle : vehicles) {
            int xCoordinate = vehicle.getXCoordinate();
            int yCoordinate = vehicle.getYCoordinate();
            int direction = vehicle.getDirection();
            Node currNode = virtualMap.getNode(xCoordinate, yCoordinate);
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

            if (nextNode.isOccupied()) {
                //for the bump and reverse algorithm - if the vehicle meets another vehicle, reverse direction
                vehicle.reverseDirection();
            } else if (nextNode.isWall()) {    //next node is a wall 
                if (leftNode.isWall()) {
                    vehicle.turnRight();
                } else if (rightNode.isWall()) {
                    vehicle.turnLeft();
                } else {  //both left and right node is not a wall
                    //turn the vehicle's direction to the next highest priority node
                    //compare the last visited timestamp for each node

                    double currentTime = System.currentTimeMillis();

                    if ((currentTime - leftNode.lastVisited()) > (currentTime - rightNode.lastVisited())) {
                        vehicle.turnLeft();
                    } else {
                        vehicle.turnRight();
                    }
                }
            } else {    //nextNode is empty, not a wall or a returnNode
                if (vehicle.getDirection() == NORTH || vehicle.getDirection() == SOUTH) {
                    //vehicle is facing NORTH or SOUTH
                    vehicle.move();
                    virtualMap.update(vehicle);
                } else {  //vehicle is facing EAST or WEST
                    //compare if NORTH or SOUTH node is high priority
                    double currentTime = System.currentTimeMillis();

                    if (leftNode.isWall()) {
                        //compare the right node
                        if ((currentTime - rightNode.lastVisited()) >= (currentTime - nextNode.lastVisited())) {
                            vehicle.turnRight();
                        } else {
                            vehicle.move();
                            virtualMap.update(vehicle);
                        }
                    } else if (rightNode.isWall()) {
                        //compare the left node
                        if ((currentTime - leftNode.lastVisited()) >= (currentTime - nextNode.lastVisited())) {
                            //turn left as left node is of higher priority
                            vehicle.turnLeft();
                        } else {
                            vehicle.move();
                            virtualMap.update(vehicle);
                        }
                    }
                }
            }
        }
    }
}
