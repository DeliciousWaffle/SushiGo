package test.testguielements;

import org.junit.*;
import static org.junit.Assert.*;

import assets.AssetLoader;
import guielements.Button;

import java.awt.Point;

public class TestButton {
    
    private Button textButton, imageButton;
    
    @Before
    public void init() {
        
        new AssetLoader();
        textButton = new Button("Blah", 50f, 100, 100);
        imageButton = new Button(null, 100, 100, 100, 100);
    }
    
    @Test
    public void testButtonActivation() {
        
        System.out.println("In testButtonActivation");
        
        textButton.setCanActivate(false);
        boolean canActivate = textButton.getCanActivate();
        assertFalse(canActivate);
        
        textButton.setCanActivate(true);
        canActivate = textButton.getCanActivate();
        assertTrue(canActivate);
        
        imageButton.setCanActivate(false);
        canActivate = imageButton.getCanActivate();
        assertFalse(canActivate);
        
        imageButton.setCanActivate(true);
        canActivate = imageButton.getCanActivate();
        assertTrue(canActivate);
    }
    
    @Test
    public void testButtonVisibility() {
        
        System.out.println("In testButtonVisibility");
        
        textButton.setVisible(false);
        boolean visible = textButton.getVisibility();
        assertFalse(visible);
        
        textButton.setVisible(true);
        visible = textButton.getVisibility();
        assertTrue(visible);
        
        imageButton.setVisible(false);
        visible = imageButton.getVisibility();
        assertFalse(visible);
        
        imageButton.setVisible(true);
        visible = imageButton.getVisibility();
        assertTrue(visible);
    }
    
    @Test
    public void testContainsText() {

        System.out.println("In testContainsText()");
        
        boolean contains = textButton.contains(new Point(100, 150));
        assertFalse(contains);
    }
    
    @Test
    public void testContainsImage() {
        
        System.out.println("In testContainsImage()");
        
        boolean contains = imageButton.contains(new Point(150, 150));
        assertTrue(contains);
        
        contains = imageButton.contains(new Point(950, 150));
        assertFalse(contains);
    }
    
    @Test
    public void testReset() {
        
        System.out.println("In testReset()");
        
        textButton.reset();
        boolean selected = textButton.getSelected();
        assertFalse(selected);
        
        imageButton.reset();
        selected = imageButton.getSelected();
        assertFalse(selected);
    }
}