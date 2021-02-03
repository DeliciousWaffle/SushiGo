package test.testguielements;

import org.junit.*;
import static org.junit.Assert.*;

import guielements.GameBoard;
import cards.Card;

import java.util.ArrayList;
import java.awt.Point;

public class TestGameBoard {
 
    private GameBoard gameBoard;
    
    @Before
    public void init() {
        
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testSafeCopy() {
        
        System.out.println("In testSafeCopy()");
        
        GameBoard deepCopy = new GameBoard(gameBoard);
        assertNotSame(gameBoard, deepCopy);
    }
    
    @Test
    public void testSameGameBoard() {
        
        System.out.println("In testSameGameBoard()");
        
        GameBoard shallowCopy = gameBoard;
        assertSame(shallowCopy, gameBoard);
    }
    
    @Test
    public void testCardSlotIndex() {
        
        System.out.println("In testCardSlotIndex()");
        
        gameBoard.playCard(new Card("Sashimi"), 1);
        int cardSlotIndex = gameBoard.getCardSlotIndex("Sashimi");
        assertEquals(1, cardSlotIndex);
        
        gameBoard.playCard(new Card("SingleMakiRoll"), 2);
        cardSlotIndex = gameBoard.getCardSlotIndex("Makis");
        assertEquals(2, cardSlotIndex);
        
        cardSlotIndex = gameBoard.getCardSlotIndex("Not a Card");
        assertEquals(-1, cardSlotIndex);
        gameBoard.clear();
    }
    
    @Test
    public void testClear() {
        
        System.out.println("In testClear()");
        
        GameBoard g1 = new GameBoard(0, 0, 0);
        g1.playCard(new Card("Sashimi"));
        g1.playCard(new Card("SingleMakiRoll"));
        g1.playCard(new Card("SalmonNigiri"));
        g1.playCard(new Card("Wasabi"));
        g1.clear();
        ArrayList<Card> noCards = g1.getBoardCards();
        assertEquals(0, noCards.size());
        
        g1.playCard(new Card("Sashimi"));
        g1.playCard(new Card("SingleMakiRoll"));
        g1.playCard(new Card("SalmonNigiri"));
        g1.playCard(new Card("Pudding"));
        int puddingAmount = g1.getPuddingAmount();
        assertEquals(1, puddingAmount);
    }
    
    @Test
    public void testHasChopsticks() {
        
        System.out.println("In testHasChopsticks()");
        
        GameBoard g1 = new GameBoard(150, 150, 1.0);
        g1.playCard(new Card("Chopsticks"), 0);
        boolean hasChopsticks = g1.hasChopsticks(new Point(155, 155));
        assertTrue(hasChopsticks);
        g1.clear();
        
        g1.formatBoard(0, 0, 0);
        g1.playCard(new Card("Chopsticks"), 0);
        hasChopsticks = g1.hasChopsticks(new Point(155, 155));
        assertFalse(hasChopsticks);
        g1.clear();
        
        g1.formatBoard(150, 150, 1.0);
        g1.playCard(new Card("Chopsticks"), 5);
        hasChopsticks = g1.hasChopsticks(new Point(155, 155));
        assertFalse(hasChopsticks);
        g1.clear();
    }
    
    @Test
    public void testPlayChopsticks() {
        
        System.out.println("In testPlayChopsticks()");
        
        GameBoard g1 = new GameBoard(150, 150, 1.0);
        g1.playCard(new Card("Chopsticks"), 0);
        Card c1 = g1.playChopsticks();
        assertNotNull(c1);
        
        g1.playCard(new Card("Pudding"), 0);
        c1 = g1.playChopsticks();
        assertNull(c1);
    }
    
    @Test
    public void testIntersects() {
        
        System.out.println("In testIntersects()");
        
        GameBoard g1 = new GameBoard(150, 150, 1.0);
        Card c1 = new Card("Sashimi");
        g1.playCard(c1, 0);
        boolean intersects = g1.intersects(new Point(155, 155), c1);
        assertTrue(intersects);
        
        Card c2 = new Card("Sashimi");
        g1.playCard(c2, 0);
        intersects = g1.intersects(new Point(300, 500), c2);
        assertFalse(intersects);
    }
    
    @Test
    public void testIntersectsCardSlot() {
        
        System.out.println("In testIntersectsCardSlot()");
        
        GameBoard g1 = new GameBoard(150, 150, 1.0);
        Card c1 = new Card("Pudding");
        g1.playCard(c1, 0);
        boolean intersects = g1.intersectsCardSlot(new Point(155, 155), c1, 0);
        assertTrue(intersects);
        
        Card c2 = new Card("Sashimi");
        g1.playCard(c2, 0);
        intersects = g1.intersectsCardSlot(new Point(300, 500), c2, 5);
        assertFalse(intersects);
    }
    
    @Test
    public void testValidCardSlotPreviouslyPlayedCard() {
        
        System.out.println("In testValidCardSlotPreviouslyPlayedCard()");
        
        Card c1 = new Card("Sashimi");
        gameBoard.playCard(c1, 0);
        Card c2 = new Card("Sashimi");
        boolean validSlot = gameBoard.validCardSlot(c2, 0);
        assertTrue(validSlot);
        
        Card c3 = new Card("SingleMakiRoll");
        validSlot = gameBoard.validCardSlot(c3, 0);
        assertFalse(validSlot);
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testValidCardSlotEmptyCheck() {
        
        System.out.println("In testValidCardSlotEmptyCheck()");
        
        Card c1 = new Card("Sashimi");
        boolean validSlot = gameBoard.validCardSlot(c1, 0);
        assertTrue(validSlot);
        
        Card c2 = new Card("SingleMakiRoll");
        gameBoard.playCard(c2, 0);
        validSlot = gameBoard.validCardSlot(c1, 0);
        assertFalse(validSlot);
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testValidCardSlotMakiCheck() {
        
        System.out.println("In testValidCardSlotMakiCheck()");
        
        Card c1 = new Card("SingleMakiRoll");
        gameBoard.playCard(c1, 0);
        Card c2 = new Card("DoubleMakiRoll");
        boolean validSlot = gameBoard.validCardSlot(c2, 0);
        assertTrue(validSlot);
        
        Card c3 = new Card("Pudding");
        validSlot = gameBoard.validCardSlot(c3, 0);
        assertFalse(validSlot);
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testValidCardSlotWasabiCheck() {
        
        System.out.println("In testValidCardSlotWasabiCheck()");
        
        Card c1 = new Card("Wasabi");
        gameBoard.playCard(c1, 0);
        Card c2 = new Card("SalmonNigiri");
        boolean validSlot = gameBoard.validCardSlot(c2, 0);
        assertTrue(validSlot);
        gameBoard.clear();
        
        Card c3 = new Card("Wasabi");
        gameBoard.playCard(c3, 0);
        Card c4 = new Card("Pudding");
        validSlot = gameBoard.validCardSlot(c4, 0);
        assertFalse(validSlot);
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testValidCardSlotNigiriCheck() {
        
        System.out.println("In testValidCardSlotNigiriCheck()");
        
        Card c1 = new Card("SalmonNigiri");
        gameBoard.playCard(c1, 0);
        Card c2 = new Card("Wasabi");
        boolean validSlot = gameBoard.validCardSlot(c2, 0);
        assertTrue(validSlot);
        gameBoard.clear();
        
        Card c3 = new Card("SalmonNigiri");
        gameBoard.playCard(c3, 0);
        Card c4 = new Card("Pudding");
        validSlot = gameBoard.validCardSlot(c4, 0);
        assertFalse(validSlot);
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testValidCardSlotNameCheck() {
        
        System.out.println("In testValidCardSlotNameCheck()");
        
        Card c1 = new Card("Tempura");
        gameBoard.playCard(c1, 0);
        Card c2 = new Card("Tempura");
        boolean validSlot = gameBoard.validCardSlot(c2, 0);
        assertTrue(validSlot);
        gameBoard.clear();
        
        Card c3 = new Card("Tempura");
        gameBoard.playCard(c3, 0);
        Card c4 = new Card("Sashimi");
        validSlot = gameBoard.validCardSlot(c4, 0);
        assertFalse(validSlot);
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testPlayCard() {
        
        System.out.println("In testPlayCard()");
        
        Card c1 = new Card("TripleMakiRoll");
        gameBoard.playCard(c1, 5);
        ArrayList<Card> cards = gameBoard.getBoardCards();
        assertFalse(cards.isEmpty());
        
        Card c3 = new Card("TripleMakiRoll");
        gameBoard.playCard(c3);
        cards = gameBoard.getBoardCards();
        assertFalse(cards.isEmpty());
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testScoreTempura() {
    
        System.out.println("In testScoreTempura()");
        
        Card c1 = new Card("Tempura");
        gameBoard.playCard(c1, 0);
        int score = gameBoard.calculateScore();
        assertEquals(0, score);
        
        Card c2 = new Card("Tempura");
        gameBoard.playCard(c2, 0);
        score = gameBoard.calculateScore();
        assertEquals(5, score);
        
        Card c3 = new Card("Tempura");
        gameBoard.playCard(c3, 0);
        score = gameBoard.calculateScore();
        assertEquals(5, score);
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testScoreSashimi() {
    
        System.out.println("In testScoreSashimi()");
                
        Card c1 = new Card("Sashimi");
        gameBoard.playCard(c1, 0);
        int score = gameBoard.calculateScore();
        assertEquals(0, score);
        
        Card c2 = new Card("Sashimi");
        gameBoard.playCard(c2, 0);
        score = gameBoard.calculateScore();
        assertEquals(0, score);
        
        Card c3 = new Card("Sashimi");
        gameBoard.playCard(c3, 0);
        score = gameBoard.calculateScore();
        assertEquals(10, score);
        
        Card c4 = new Card("Sashimi");
        gameBoard.playCard(c4, 0);
        score = gameBoard.calculateScore();
        assertEquals(10, score);
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testScoreDumpling() {
    
        System.out.println("In testScoreDumpling()");
        
        Card c1 = new Card("Dumpling");
        gameBoard.playCard(c1, 0);
        int score = gameBoard.calculateScore();
        assertEquals(1, score);
        
        Card c2 = new Card("Dumpling");
        gameBoard.playCard(c2, 0);
        score = gameBoard.calculateScore();
        assertEquals(3, score);
        
        Card c3 = new Card("Dumpling");
        gameBoard.playCard(c3, 0);
        score = gameBoard.calculateScore();
        assertEquals(6, score);
        
        Card c4 = new Card("Dumpling");
        gameBoard.playCard(c4, 0);
        score = gameBoard.calculateScore();
        assertEquals(10, score);
        
        Card c5 = new Card("Dumpling");
        gameBoard.playCard(c5, 0);
        score = gameBoard.calculateScore();
        assertEquals(15, score);
        
        Card c6 = new Card("Dumpling");
        gameBoard.playCard(c6, 0);
        score = gameBoard.calculateScore();
        assertEquals(15, score);
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testScoreEggNigiri() {
    
        System.out.println("In testScoreEggNigiri()");
        
        Card c1 = new Card("EggNigiri");
        gameBoard.playCard(c1, 0);
        int score = gameBoard.calculateScore();
        assertEquals(1, score);
        
        Card c2 = new Card("Wasabi");
        gameBoard.playCard(c2, 0);
        score = gameBoard.calculateScore();
        assertEquals(3, score);
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testScoreSquidNigiri() {
    
        System.out.println("In testScoreSquidNigiri()");
        
        Card c1 = new Card("SquidNigiri");
        gameBoard.playCard(c1, 0);
        int score = gameBoard.calculateScore();
        assertEquals(3, score);
        
        Card c2 = new Card("Wasabi");
        gameBoard.playCard(c2, 0);
        score = gameBoard.calculateScore();
        assertEquals(9, score);
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testScoreSalmonNigiri() {
    
        System.out.println("In testScoreSalmonNigiri()");
        
        Card c1 = new Card("SalmonNigiri");
        gameBoard.playCard(c1, 0);
        int score = gameBoard.calculateScore();
        assertEquals(2, score);
        
        Card c2 = new Card("Wasabi");
        gameBoard.playCard(c2, 0);
        score = gameBoard.calculateScore();
        assertEquals(6, score);
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testCumulativeScore() {
    
        System.out.println("In testCumulativeScore()");
        
        Card c1 = new Card("SingleMakiRoll");
        Card c2 = new Card("Pudding");
        Card c3 = new Card("SquidNigiri");
        Card c4 = new Card("Sashimi");
        Card c5 = new Card("Tempura");
        Card c6 = new Card("Tempura");
        Card c7 = new Card("Chopsticks");
        gameBoard.playCard(c1);
        gameBoard.playCard(c2);
        gameBoard.playCard(c3);
        gameBoard.playCard(c4);
        gameBoard.playCard(c5);
        gameBoard.playCard(c6);
        gameBoard.playCard(c7);
        int score = gameBoard.calculateScore();
        assertEquals(8, score);
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testGetMakiAmount() {
    
        System.out.println("In testGetMakiAmount()");
        
        Card c1 = new Card("SingleMakiRoll");
        Card c2 = new Card("DoubleMakiRoll");
        Card c3 = new Card("TripleMakiRoll");
        gameBoard.playCard(c1);
        gameBoard.playCard(c2);
        gameBoard.playCard(c3);
        int makis = gameBoard.getMakiAmount();
        assertEquals(6, makis);
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testGetPuddingAmount() {
    
        System.out.println("In testGetPuddingAmount()");
        
        Card c1 = new Card("Pudding");
        Card c2 = new Card("Pudding");
        Card c3 = new Card("Pudding");
        gameBoard.playCard(c1);
        gameBoard.playCard(c2);
        gameBoard.playCard(c3);
        int puddings = gameBoard.getPuddingAmount();
        assertEquals(3, puddings);
        gameBoard = new GameBoard(0, 0, 0);
    }
    
    @Test
    public void testUsableWasabi() {
        
        System.out.println("In testUsableWasabi()");
        
        Card c1 = new Card("Wasabi");
        gameBoard.playCard(c1);
        boolean hasWasab = gameBoard.hasUsableWasabi();
        assertTrue(hasWasab);
        gameBoard.clear();
        
        hasWasab = gameBoard.hasUsableWasabi();
        assertFalse(hasWasab);
    }
}