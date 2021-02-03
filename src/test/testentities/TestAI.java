package test.testentities;

import org.junit.*;
import static org.junit.Assert.*;

import entities.AI;
import guielements.GameBoard;
import cards.Card;

import java.util.ArrayList;

public class TestAI {
    
    private AI ai;
    
    @Before
    public void init() {
        
        ai = new AI("Big Boi", 3);
    }
    
    @Test
    public void testDifficulty() {
        
        System.out.println("In testDifficulty()");
        
        int diff = 2;
        ai.setDifficulty(diff);
        assertEquals(2, ai.getDifficulty());
    }
    
    @Test
    public void testPlayCard() {
        
        System.out.println("In testPlayCard()");
        
        ArrayList<Card> hand = new ArrayList<>();
        Card c1 = new Card("Tempura");
        hand.add(c1);
        ai.setAIsHand(hand);
        ai.playCard(0);
        GameBoard g1 = ai.getAIsGameBoard();
        Card c2 = g1.getBoardCards().get(0);
        assertEquals(c1, c2);
    }
    
    @Test
    public void testCopyAIsGameBoard() {
        
        System.out.println("In testCopyAIsGameBoard()");
        
        GameBoard g1 = ai.getAIsGameBoard();
        GameBoard copy = new GameBoard(g1);
        
        assertNotSame(g1, copy);
    }
    
    @Test
    public void testCopyAIsHand() {
        
        System.out.println("In testCopyAIsHand()");
        
        ArrayList<Card> hand = new ArrayList<>();
        Card c1 = new Card("Pudding");
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
        cards.add(new Card("Sashimi"));
        cards.add(new Card("DoubleMakiRoll"));
        cards.add(new Card("Tempura"));
        ai.setAIsHand(cards);
        ArrayList<Card> hand = ai.getAIsHand();
        assertEquals(cards, hand);
    }
    
    @Test
    public void testGetPlayersGameBoard() {
        
        System.out.println("In testGetPlayerGameBoard()");
        
        GameBoard g1 = new GameBoard(0, 0, 0);
        ai.setAIsGameBoard(g1);
        GameBoard g2 = ai.getAIsGameBoard();
        assertEquals(g1, g2);
    }
    
    @Test
    public void testGetScore() {
        
        System.out.println("In testGetScore()");
        
        int inputScore = 10;
        ai.setScore(inputScore);
        int outputScore = ai.getScore();
        assertEquals(10, outputScore);
    }
}