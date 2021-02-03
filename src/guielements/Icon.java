package guielements;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/*
    Basically an image with a positon, velocity, and other stuff.
*/
public class Icon {
    
    private BufferedImage iconImage;
    private int x, y;
    private float rotation;
    private int width, height;
    private int xVel, yVel;
    private float rotationVel;
    
    public Icon(BufferedImage iconImage, int x, int y, int width, int height) {
        
        this.iconImage = iconImage;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    public void setXVel(int xVel) { this.xVel = xVel; }
    public void setYVel(int yVel) { this.yVel = yVel; }
    
    public void setRotationVel(float rotationVel) { this.rotationVel = rotationVel; }
    
    public void update() {
        
        x += xVel;
        y += yVel;
        rotation += rotationVel;
    }
    
    public void render(Graphics2D g2) {
    
        AffineTransform restoreTransform = g2.getTransform();
        g2.rotate(rotation, x + (width / 2), y + (height / 2));
        g2.drawImage(iconImage, x, y, width, height, null);
        g2.setTransform(restoreTransform);
    }
}