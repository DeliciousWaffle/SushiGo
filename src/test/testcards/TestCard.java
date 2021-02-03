package test.testcards;

import org.junit.*;
import static org.junit.Assert.*;

import cards.Card;
import java.awt.Point;

public class TestCard {
    
    @Test
    public void testSafeCopy() {
        
        System.out.println("In testSafeCopy()");
        Card c1 = new Card("Tempura");
        Card c2 = new Card(c1);
        assertNotSame(c1, c2);
    }
    
    @Test
    public void testSameCard() {
        
        System.out.println("In testSameCard()");
        Card c1 = new Card("Tempura");
        Card c2 = new Card("Tempura");
        assertEquals(c1, c2);
    }
    
    @Test
    public void testDifferentCard() {
        
        System.out.println("In testDifferentCard()");
        Card c1 = new Card("Tempura");
        Card c2 = new Card("Sashimi");
        assertFalse(c1.equals(c2));
    }
    
    @Test
    public void testMoveLeft() {
        
        System.out.println("In testMoveLeft()");
        Card c1 = new Card("Pudding");
        c1.setX(100);
        c1.moveLeft();
        assertEquals(70, c1.getX());
    }
    
    @Test
    public void testMoveUp() {
        
        System.out.println("In testMoveUp()");
        Card c1 = new Card("Wasabi");
        c1.setY(500);
        c1.moveUp();
        assertEquals(490, c1.getY());
    }
    
    @Test
    public void testGetBounds() {
        
        System.out.println("In testGetBounds()");
        Card c1 = new Card("Sashimi");
        c1.setX(100);
        c1.setY(100);
        c1.setWidth(100);
        c1.setHeight(100);
        assertTrue(c1.getBounds().getX() == 99 && c1.getBounds().getY() == 99
            && c1.getBounds().getWidth() == 102 && c1.getBounds().getHeight() == 102);
    }
    
    @Test
    public void testContains() {
        
        System.out.println("In testContains()");
        Card c1 = new Card("Sashimi");
        c1.setX(0);
        c1.setY(0);
        c1.setWidth(100);
        c1.setHeight(100);
        assertTrue(c1.contains(new Point(50, 50)));
    }
    
    @Test
    public void testReset() {
        
        System.out.println("In testReset()");
        Card c1 = new Card("Sashimi");
        c1.setStartX(0);
        c1.setStartY(0);
        c1.setX(100);
        c1.setY(100);
        c1.setDragged(true);
        c1.reset();
        assertTrue(c1.getX() == 0 && c1.getY() == 0 && c1.getDragged() == false);
    }
    
    @Test
    public void testOffscreenLeft() {
        
        System.out.println("In testOffscreenLeft()");
        Card c1 = new Card("EggNigiri");
        c1.setX(-100);
        c1.update();
        assertTrue(c1.getX() == 0);
    }
    
    @Test
    public void testOffscreenRight() {
        
        System.out.println("In testOffscreenRight()");
        Card c1 = new Card("EggNigiri");
        c1.setX(1500);
        c1.setWidth(100);
        c1.update();
        assertTrue(c1.getX() == 1180);
    }
    
    @Test
    public void testOffscreenTop() {
        
        System.out.println("In testOffscreenTop)");
        Card c1 = new Card("SalmonNigiri");
        c1.setY(-100);
        c1.update();
        assertTrue(c1.getY() == 0);
    }
    
    @Test
    public void testOffScreenBottom() {
        
        System.out.println("In testOffscreenBottom()");
        Card c1 = new Card("EggNigiri");
        c1.setY(1000);
        c1.setHeight(100);
        c1.update();
        assertTrue(c1.getY() == 620);
    }
}