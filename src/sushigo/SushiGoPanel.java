package sushigo;

import gamemanager.GameManager;
import gamestates.GameStateManager;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

/*
    Class is responsible for rendering all game objects and allowing the user to
    interact with these objects. Basically allows the player to play the game.
*/
public class SushiGoPanel extends JPanel implements Runnable, MouseListener, MouseMotionListener {

    private GameManager gm;
    private GameStateManager gsm;
    private double scaleX, scaleY;    

    public SushiGoPanel() {
        
        gm = new GameManager();
        gsm = new GameStateManager(gm);
        
        this.setPreferredSize(new Dimension(1280, 720));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        new Thread(this).start();
    }
    
    public void setFrame(JFrame frame) { 
        
        gsm.setFrame(frame); 
    }
    
    @Override
    public void run() {
   
        while(true) {
            
            long startTime = System.currentTimeMillis();
            update();
            render();
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            sleep(elapsedTime);
        }
    }
    
    private void update() { 
        
        gsm.update(); 
    }
    
    private void render() { 
        
        this.repaint(); 
    }
    
    private void sleep(long elapsedTime) {
        
        double updatesPerSec = 1000 / 60;
        int sleepTime = (int) (updatesPerSec - elapsedTime);
        
        try {
            
            Thread.sleep(Math.abs(sleepTime));
            
        } catch(Exception e) {
            
            e.printStackTrace();
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);                                                    // house keeping
        Graphics2D g2 = (Graphics2D) g;                                             // Graphics2D provides more uses over just regular Graphics
        
        g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,    // add anti-aliasing
            RenderingHints.VALUE_ANTIALIAS_ON));
        g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION,   // makes images used look slightly less bad
            RenderingHints.VALUE_INTERPOLATION_BILINEAR));
        
        // scale objects drawn to the screen with screen size
        scaleX = this.getWidth() / 100.0;
        scaleY = this.getHeight() / 100.0;
        scaleX /= 12.8;
        scaleY /= 7.2;
        
        g2.scale(scaleX, scaleY);
        
        // let the GameStateManager handle all the rendering
        gsm.render(g2);
    }

    /*  following methods send mouse events to game state manager to be later processed.
        the location of the point needs to be scaled whenever the screen size changes
        so that the mouse's location is accurate. 
    */
    @Override
    public void mouseClicked(MouseEvent e) {
    
        Point p = e.getPoint();
        Point scaled = new Point((int) (p.x / scaleX), (int) (p.y / scaleY));
        gsm.handleMouseClicked(scaled);
    }
    
    @Override
    public void mouseReleased(MouseEvent e) { 
    
        Point p = e.getPoint();
        Point scaled = new Point((int) (p.x / scaleX), (int) (p.y / scaleY));
        gsm.handleMouseReleased(scaled);
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
    
        Point p = e.getPoint();
        Point scaled = new Point((int) (p.x / scaleX), (int) (p.y / scaleY));
        gsm.handleMouseDragged(scaled);
    }
            
    @Override
    public void mouseMoved(MouseEvent e) {
    
        Point p = e.getPoint();
        Point scaled = new Point((int) (p.x / scaleX), (int) (p.y / scaleY));
        gsm.handleMouseMoved(scaled);
    }
    
    // following have to be implemented, however they are not going to be used
    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}