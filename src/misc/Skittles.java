package misc;

import java.awt.Color;
import java.util.ArrayList;

/*
    Super important class that is paramount to the overall functioning
    of the game. Without this class, our project will explode
    into a million pieces.
*/
public class Skittles {
    
    private ArrayList<Color> skittles;
    
    public Skittles() {
        
        // credit: https://stackoverflow.com/questions/22973532/java-creating-a-discrete-rainbow-colour-array
        skittles = new ArrayList<>();
        
        for(int r = 0; r < 100; r++) 
            skittles.add(new Color(r * 255 / 100, 255, 0));
        
        for(int g = 100; g > 0; g--) 
            skittles.add(new Color(255, g * 255 / 100, 0));
        
        for(int b = 0; b < 100; b++) 
            skittles.add(new Color(255, 0, b * 255 / 100));
        
        for(int r = 100; r > 0; r--) 
            skittles.add(new Color(r * 255 / 100, 0, 255));
        
        for(int g = 0; g < 100; g++) 
            skittles.add(new Color(0, g * 255 / 100, 255));
        
        for(int b = 100; b > 0; b--) 
            skittles.add(new Color(0, 255, b * 255 / 100));
    }
    
    public ArrayList<Color> tasteTheRainbow() { return skittles; } 
}