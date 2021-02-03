package guielements;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;

public class Text {
    
    private String text;
    private Font font;
    private Color color;
    private int x, y, xVel, yVel;
    private boolean visible;
    
    public Text(String text, Font font, float fontSize, Color color, int x, int y) {
        
        this.text = text;
        this.font = font.deriveFont(fontSize);
        this.color = color;
        this.x = x;
        this.y = y;
        this.visible = true;
    }
    
    public void setText(String text) { this.text = text; }
    
    public void setFontSize(float fontSize) { this.font = font.deriveFont(fontSize); }
    
    public void setColor(Color color) { this.color = color; }
    
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    
    public void setXVel(int xVel) { this.xVel = xVel; }
    public void setYVel(int yVel) { this.yVel = yVel; }
    
    public void setVisible(boolean visible) { this.visible = visible; }
    
    public String getText() { return text; }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    public int getXVel() { return xVel; }
    public int getYVel() { return yVel; }
    
    public void update() {
        
        x += xVel;
        y += yVel;
    }
    
    public void render(Graphics2D g2) {
        
        if(! visible) return;
        
        g2.setFont(font);
        
        g2.setColor(Color.BLACK);
        g2.drawString(text, x, y);
        
        g2.setColor(color);
        g2.drawString(text, x - (font.getSize() / 10), y - (font.getSize() / 10));
    }
}