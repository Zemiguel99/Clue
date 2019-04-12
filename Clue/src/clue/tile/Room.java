/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clue.tile;

import clue.card.RoomCard;
import java.util.ArrayList;

/**
 * Represents a room on the board. Each room should have an associated RoomCard.
 * @author slb35
 */
public class Room extends Tile{
    private RoomCard card;
    private ArrayList<int[]> locations;
    /**
     * Creates a new Room
     * @param card the RoomCard associated with this room.
     */
    public Room(RoomCard card) {
        super(-1,card.getid());
        this.card = card;
        room = true;
        locations = new ArrayList<>();
    }
    /**
     * Creates a new Room
     * @param x
     * @param y
     */
    public Room(){
        super(-1,-1);
        this.card = card;
        room = true;
        locations = new ArrayList<>();
    }
    
    /**
     * Adds a x y location to the room location
     * @param x the x coordinate of the location to be added
     * @param y the y coordinate of the location to be added
     */
    public void addLocation(int x, int y){
        int[] loc = new int[2];
        loc[0] = x;
        loc[1] = y;
        locations.add(loc);
    }
    
    /**
     * Returns the physical locations (x,y coordinates) of the room
     * @return 
     */
    public ArrayList<int[]> getLocations(){
        return locations;
    }
    
    
    
    /**
     * Gets the room id of the room
     * @return the room id
     */
    public int getId(){
        return getY();
    
    }

    
    /**
     * Placeholder method from legacy version, currently still here so it is not re implemented without notice to mw434
     * 
     */
    final public void setCard(RoomCard card){
        System.err.println("[Room.setCard] //DO NOT IMPLEMENT, IF YOU MUST SPEAK TO MW434"); 
    }
    
    /**
     * Gets the RoomCard associated with this room
     * @return the RoomCard associated with this room
     * @throws NoSuchRoomException thrown when room has no associated card
     */
    public RoomCard getCard() throws NoSuchRoomException{
        if (getY() == -1){
            throw new NoSuchRoomException("room was not created correctly, does not contain a valid roomCard");
        }
        return card;
    }
}
