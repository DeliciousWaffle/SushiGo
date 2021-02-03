package guielements;

import assets.AssetLoader;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Button {
    
    private String text;
    private Font font;
    private BufferedImage image;
    private boolean isText, visible, canActivate;
    private int x, y;
    private int startY;
    private int width, height;
    private Color color;
    private boolean selected;
    
    // text button
    public Button(String text, float fontSize, int x, int y) {
        
        this.text = text;
        this.font = AssetLoader.font.deriveFont(fontSize);
        this.x = x;
        this.y = y;
        this.startY = y;
        this.color = Color.WHITE;
        this.isText = true;
        this.visible = true;
        this.canActivate = true;
    }
    
    // image button
    public Button(BufferedImage image, int x, int y, int width, int height) {
        
        this.image = image;
        this.x = x;
        this.y = y;
        this.startY = y;
        this.width = width;
        this.height = height;
        this.isText = false;
        this.visible = true;
        this.canActivate = true;
    }
    
    public void setText(String text) { this.text = text; }
    
    public void setVisible(boolean visible) { this.visible = visible; }
    public void setCanActivate(boolean canActivate) { this.canActivate = canActivate; }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    public int getWidth() { return width; }
    
    public boolean getVisibility() { return visible; }
    public boolean getCanActivate() { return canActivate; }
    public boolean getSelected() { return selected; }
    
    public boolean contains(Point p) { 

        if(! canActivate)
            return false;
        
        if(isText)
            return containsText(p);
        else
            return containsImage(p);
    }
    
    private boolean containsText(Point p) {
       
        // this is the area that works for some reason
        if(new Rectangle(x, y - height - 20, width, height + 25 + (startY - y)).contains(p)){
            
            color = Color.GREEN;
            selected = true;
            return true;
            
        } else {
            
            color = Color.WHITE;
            selected = false;
            return false;
        }
    }  
    
    private boolean containsImage(Point p) {
        
        if(new Rectangle(x, y, width, height + (startY - y)).contains(p)) {
            
            selected = true;
            return true;
        
        } else {
            
            selected = false;
            return false;
        }
    }

    public void reset() {
        
        y = startY;
        selected = false;
        color = Color.WHITE;
    }
    
    public void update() {
    
        // make the text/image rise up and fall a little bit to add pizzaz
        if(selected) {
            
            y--;
            
            if(y <= startY - 3)
                y = startY - 3;
            
        } else {    
            
            y++;
            
            if(y >= startY + 3)
                y = startY + 3;
        }
    }
    
    public void render(Graphics2D g2) {
        
        if(! visible)
            return;
        
        if(isText)
            renderText(g2);
        else
            renderImage(g2);
    }
    
    private void renderText(Graphics2D g2) {
            
        // for debugging purposes
        //g2.setColor(Color.CYAN);
        //g2.fill(new Rectangle(x, y - height - 20, width, height + 25 +(startY - y)));
        
        g2.setFont(font);
        
        g2.setColor(Color.BLACK);
        g2.drawString(text, x, y);
        
        g2.setColor(color);
        g2.drawString(text, x - (font.getSize() / 10), y - (font.getSize() / 10));
        
        // used to calculate whether the player's mouse is within this button
        width = g2.getFontMetrics().stringWidth(text);
        height = g2.getFontMetrics().getHeight() / 2;
    }  
    
    private void renderImage(Graphics2D g2) {
            
        g2.drawImage(image, x, y, width, height, null);
    }
}