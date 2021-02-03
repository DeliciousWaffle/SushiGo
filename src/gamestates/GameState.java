package gamestates;

import java.awt.Graphics2D;
import java.awt.Point;

public abstract class GameState {
        
    protected GameStateManager gameStateManager;
    
    public abstract void update();
    
    public abstract void render(Graphics2D g2);
        
    public abstract void handleMouseClicked(Point p);
    
    public abstract void handleMouseReleased(Point p);
    
    public abstract void handleMouseMoved(Point p);
    
    public abstract void handleMouseDragged(Point p);
}