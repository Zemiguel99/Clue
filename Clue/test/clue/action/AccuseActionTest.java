/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clue.action;

import clue.GameController;
import clue.MissingRoomDuringCreationException;
import clue.card.PersonCard;
import clue.card.RoomCard;
import clue.card.WeaponCard;
import clue.player.Player;
import clue.tile.NoSuchRoomException;
import clue.tile.NoSuchTileException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author steve
 */
public class AccuseActionTest {
    
    public AccuseActionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of execute method, of class AccuseAction.
     */
    @Test
    public void testExecute() throws InterruptedException, UnknownActionException, NoSuchRoomException, NoSuchTileException, MissingRoomDuringCreationException, GameController.TooManyPlayersException {
        System.out.println("execute");
        PersonCard person = new PersonCard(1);
        RoomCard room = new RoomCard(1);
        WeaponCard weapon = new WeaponCard(1);
        Player player = new Player(1);
        ArrayList<Player> players = new ArrayList();
        players.add(player);
        GameController game = new GameController(1,1,"testCsv/tiles1.csv", "testCsv/doors1.csv");
        AccuseAction instance = new AccuseAction(player, person, room, weapon, true);
        instance.execute();
        // TODO review the generated test code and remove the default call to fail.
        boolean expResult = true;
        boolean result = instance.result;
        assertEquals(expResult, result);
        assertEquals(false,player.isActive());
    }
    
}
