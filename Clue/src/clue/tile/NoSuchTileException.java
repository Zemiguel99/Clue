/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clue.tile;

/**
 *
 * @author Malter
 */
public class NoSuchTileException extends Exception {

    /**
     * Creates a noSuchTileException
     * @param message the exception message
     */
    public NoSuchTileException(String message) {
        super(message);
    }
        
    public NoSuchTileException() {
    }
    
}
