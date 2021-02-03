package test.testcards;

import org.junit.*;
import static org.junit.Assert.*;

import cards.Card;
import cards.Deck;

import java.util.ArrayList;

public class TestDeck {
    
    @Test
    public void testSize() {
        
        System.out.println("In testSize()");
        Deck deck = new Deck();
        assertEquals(108, deck.getSize());
        deck.drawCard();
        assertEquals(107, deck.getSize());
    }
    
    @Test
    public void testIsEmpty() {
        
        System.out.println("In testSize()");
        Deck deck = new Deck();
        assertFalse(deck.isEmpty());
        while(! deck.isEmpty())
            deck.drawCard();
        assertTrue(deck.isEmpty());
    }
    
    @Test
    public void testDealHand() {
        
        System.out.println("In testDealHand()");
        Deck deck = new Deck();
        ArrayList<Card> twoPlayers = deck.dealHand(2);
        assertTrue(twoPlayers.size() == 10);
        ArrayList<Card> threePlayers = deck.dealHand(3);
        assertTrue(threePlayers.size() == 9);
        ArrayList<Card> fourPlayers = deck.dealHand(4);
        assertTrue(fourPlayers.size() == 8);
        ArrayList<Card> fivePlayers = deck.dealHand(5);
        assertTrue(fivePlayers.size() == 7);
    }
    
    @Test
    public void testDrawCard() {
        
        System.out.println("In testDrawCard()");
        Deck deck = new Deck();
        Card c1 = deck.drawCard();
        assertNotNull(c1);
    }
}