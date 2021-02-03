package test.testentities;

import org.junit.*;
import static org.junit.Assert.*;

import entities.Player;
import guielements.GameBoard;
import cards.Card;

import java.util.ArrayList;

public class TestPlayer {
    
    private Player player;
    
    @Before
    public void init() {
        
        player = new Player();
    }
    
    @Test
    public void testCopyGameBoard() {
        
        System.out.println("In testCopyGameBoard()");
        
        GameBoard g1 = player.getPlayersGameBoard();
        GameBoard copy = new GameBoard(g1);
        
        assertNotSame(g1, copy);
    }
    
    @Test
    public void testCopyPlayersHand() {
        
        System.out.println("In testCopyPlayersHand()");
        
        ArrayList<Card> hand = new ArrayList<>();
        Card c1 = new Card("Wasabi");
        hand.add(new Card(c1));
        ArrayList<Card> copy = new ArrayList<>();
        for(int i = 0; i < hand.size(); i++)
            copy.add(new Card(c1));
        
        assertNotSame(hand, copy);
    }
    
    @Test
    public void testGetPlayersHand() {
        
        System.out.println("In testGetPlayersHand()");
        
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card("Pudding"));
        cards.add(new Card("SingleMakiRoll"));
        cards.add(new Card("Sashimi"));
        player.setPlayersHand(cards);
        ArrayList<Card> hand = player.getPlayersHand();
        assertEquals(cards, hand);
    }
    
    @Test
    public void testGetPlayersGameBoard() {
        
        System.out.println("In testGetPlayerGameBoard()");
        
        GameBoard g1 = new GameBoard(0, 0, 0);
        player.setPlayersBoard(g1);
        GameBoard g2 = player.getPlayersGameBoard();
        assertEquals(g1, g2);
    }
    
    @Test
    public void testGetScore() {
        
        System.out.println("In testGetScore()");
        
        int inputScore = 50;
        player.setScore(inputScore);
        int outputScore = player.getScore();
        assertEquals(50, outputScore);
    }
}