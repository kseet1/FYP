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
public class Map {
    private static int[][] map;
    private static Node[][] nodeMap;
    private static int xDim, yDim;
    
    public Map(int xDimension, int yDimension) {
        map = new int[yDimension][xDimension];
        nodeMap = new Node[yDimension][xDimension];
        xDim = xDimension;
        yDim = yDimension;
        
        for(int x=0; x<xDim; x++) {
            for(int y=0; y<yDim; y++) {
                if((x==0)||(y==0)||(x==(xDim-1))||(y==(yDim-1))) {
                    map[y][x]=1;            //1 represents wall or obstacle in that node
                    nodeMap[y][x] = new Node(x,y,true,false,false);
                }
                else {
                    map[y][x]=0;            //0 represents empty node
                    nodeMap[y][x] = new Node(x,y,false,false,false);
                }
            }
        }
        for(int[] row : map) {
            for(int node : row) {
                System.out.print(node);
            }
            System.out.println();
        }
    }
    
    public void update(Vehicle vehicle) {
        int xCoordinate = vehicle.getXCoordinate();
        int yCoordinate = vehicle.getYCoordinate();
        int prev_XCoordinate = vehicle.getPrevXCoordinate();
        int prev_YCoordinate = vehicle.getPrevYCoordinate();
        
        //map[prev_YCoordinate][prev_XCoordinate] = 0;
        //map[yCoordinate][xCoordinate] = 2;  //2 represents a vehicle in that node
        //nodeMap[prev_YCoordinate][prev_XCoordinate].setOccupied(false,vehicle);
        //nodeMap[yCoordinate][xCoordinate].setOccupied(true,vehicle);
        nodeMap[prev_YCoordinate][prev_XCoordinate].remove(vehicle);
        nodeMap[yCoordinate][xCoordinate].add(vehicle);
        
        map[yCoordinate][xCoordinate] = 2;
        if(nodeMap[prev_YCoordinate][prev_XCoordinate].isOccupied())
            map[prev_YCoordinate][prev_XCoordinate] = 2;
        else
            map[prev_YCoordinate][prev_XCoordinate] = 0;
    }
    
    public Node getNode(int x, int y) {
        return nodeMap[y][x];
    }
    
    public int[][] getMap() {
        return this.map;
    }
    
    public void printMap() {
        for(int[] row : map) {
            for(int node : row) {
                System.out.print(node);
            }
            System.out.println();
        }
    }
    
    public int checkNode(int x, int y) {
        if(nodeMap[y][x].isWall())
            return 1;
        else if(nodeMap[y][x].isOccupied())
            return 2;
        else
            return 0;
    }
    
    public int getXDimension() {
        return this.xDim;
    }
    
    public int getYDimension() {
        return this.yDim;
    }
    
    public int size() {
        return (this.xDim-2)*(this.yDim-2);
    }
    
}
