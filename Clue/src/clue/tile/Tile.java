/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clue.tile;

import java.util.ArrayList;
import java.util.List;

/**
 *Represents a tile on the game board.
 * @author slb35
 */
public class Tile {
    public boolean special;
    private List<Tile> adjacentTiles;
    public boolean room;
    private boolean occupied;
    private int x;
    private int y;
    
    
    /**
     * Creates a new Tile.
     */
    public Tile(int x, int y) {
        special = false;
        room = false;
        adjacentTiles = new ArrayList<>();
        this.x = x;
        this.y = y;
        occupied = false;
    }
    
    /**
     * Adds Tiles to the list of adjacent tiles
     * @param adjacentTiles list of tiles to add
     */
    public void setAdjacent(List<Tile> adjacentTiles){
        this.adjacentTiles.addAll(adjacentTiles);
    }

    /**
     * Gets whether or not a Tile is adjacent to this one.
     * @param tile Tile to check
     * @return adjacency
     */
    public boolean isAdjacent(Tile tile){
        if(adjacentTiles.contains(tile)){
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Returns true if the tile is room, false otherwise.
     * @return room
     */
    public boolean isRoom(){
        return room;
    }
    
    
    /**
     * @returns false if the tile is a non room tile and someone is already in the tile;
     * 
     */
    public boolean isFull(){
        return occupied && !isRoom();
    }
    
    /**
     * sets the occupied state of the tile
     * @param occupied the new occupied state of the square
     * 
     */
    public void setOccupied(boolean occupied){
        this.occupied = occupied;
    }
    
    /**
     * gets the occupied state of the tile
     * @return occupied the occupied state of the square
     * 
     */
    public boolean setOccupied(){
        return occupied;
    }

    /**
     * Adds Tile to the list of adjacent tiles for the current tile, the tile being added will have this tile added to its adjacency list
     * @param adjacentTile tile to add
     */
    public void addAdjacentBoth(Tile adjacentTile) {
        this.addAdjacent(adjacentTile);
        if (adjacentTile.isAdjacent(this)){
            System.out.println("[Tile.addAdjacentBoth]target tile allready knowns about this adjacency, dont add both");                    
        }
        else{
                adjacentTile.addAdjacent(this);//non recursive add to prevent them keep on adding and calling each other
        }  
    }
   
    /**
     * Adds Tile to the list of adjacent tiles for the current tile 
     * @param adjacentTile tile to add
     */
    public void addAdjacent(Tile adjacentTile) {
        if (isAdjacent(adjacentTile)){
            System.out.println("[Tile.addAdjacent]this tile allready knows about this adjacency!");
        }
        else{
            this.adjacentTiles.add(adjacentTile);
        }
    }
    
    @Override
    public String toString(){
        String res = "";
        res+= "spec:"+isSpecial()+", room:"+isRoom() + ", xy:" + getX() + "," + getY()+ " \n";
        
        for(Tile adjacent : getAdjacent()){
            res += "    "+adjacent.getX()+ ", "+adjacent.getY()+ " \n";
        }
        
        return res;
    }
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public List<Tile> getAdjacent(){
        return adjacentTiles;
    }
    
    public boolean isSpecial(){
        return special;
    }
        
}
