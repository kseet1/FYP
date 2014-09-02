/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Simulator;

/**
 *
 * @author KSEET_000
 */
public class Vehicle {
    static final int NORTH = 1;
    static final int EAST = 2;
    static final int SOUTH = 3;
    static final int WEST = 4;
    
    private int curr_x;  //current x-coordinate of the vehicle
    private int curr_y;  //current y-coordinate of the vehicle
    private int prev_x;  //previous x-coordinate of the vehicle
    private int prev_y;  //previous y-coordinate of the vehicle
    private int direction; //direction of the vehicle facing
    private final int speed; //Speed of vehicle (variable performance between different vehicles)
    
    public Vehicle(int x, int y, int speed, int direction) {
        //initiate the starting point of the Vehicle
        this.curr_x = x;
        this.curr_y = y;
        this.prev_x = x;
        this.prev_y = y;
        this.speed = speed;
        this.direction = direction;
    }
    
    public void move() {
        this.prev_x = curr_x;
        this.prev_y = curr_y;
        
        if(direction==NORTH) {
            this.curr_y--;
        }
        else if(direction==SOUTH) {
            this.curr_y++;
        }
        else if(direction==EAST) {
            this.curr_x++;
        }
        else if(direction==WEST) {
            this.curr_x--;
        }
    }
    
    public void reverseDirection() {
        if(direction==NORTH) {
            this.direction = SOUTH;
        }
        else if(direction==SOUTH) {
            this.direction = NORTH;
        }
        else if(direction==EAST) {
            this.direction = WEST;
        }
        else if(direction==WEST) {
            this.direction = EAST;
        }
    }

    public void turnLeft() {
        if(direction==NORTH) {
            this.direction = WEST;
        }
        else if(direction==SOUTH) {
            this.direction = EAST;
        }
        else if(direction==EAST) {
            this.direction = NORTH;
        }
        else if(direction==WEST) {
            this.direction = SOUTH;
        }
    }
    
    public void turnRight() {
        if(direction==NORTH) {
            this.direction = EAST;
        }
        else if(direction==SOUTH) {
            this.direction = WEST;
        }
        else if(direction==EAST) {
            this.direction = SOUTH;
        }
        else if(direction==WEST) {
            this.direction = NORTH;
        }
    }
    
    public void setDirection(int new_direction) {
        this.direction = new_direction;
    }
    
    public int getXCoordinate() {
        return this.curr_x;
    }
    
    public int getYCoordinate() {
        return this.curr_y;
    }
    
    public int getPrevXCoordinate() {
        return this.prev_x;
    }
    
    public int getPrevYCoordinate() {
        return this.prev_y;
    }
    
    public int getDirection() {
        return this.direction;
    }
}
