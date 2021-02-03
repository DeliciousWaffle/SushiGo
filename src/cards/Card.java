package cards;

import assets.AssetLoader;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Card {

    private String cardName;
    private BufferedImage cardImage;
    private double scale;
    private int x, y;
    private int startX, startY;
    private int xVel, yVel;
    private int width, height;
    private Color color;
    private boolean dragged;
    
    public Card(String cardName) {
        
        this.cardName = cardName;
        
        switch(cardName) {
            
            case "Tempura":
                cardImage = AssetLoader.tempura;
                break;              
            case "Sashimi":
                cardImage = AssetLoader.sashimi;
                break;              
            case "Dumpling":
                cardImage = AssetLoader.dumpling;
                break;          
            case "SingleMakiRoll":
                cardImage = AssetLoader.singleMakiRoll;
                break;         
            case "DoubleMakiRoll":
                cardImage = AssetLoader.doubleMakiRoll;
                break;                
            case "TripleMakiRoll":
                cardImage = AssetLoader.tripleMakiRoll;
                break;
            case "SalmonNigiri":
                cardImage = AssetLoader.salmonNigiri;
                break;                
            case "SquidNigiri":
                cardImage = AssetLoader.squidNigiri;
                break;               
            case "EggNigiri":
                cardImage = AssetLoader.eggNigiri;
                break;               
            case "Pudding":
                cardImage = AssetLoader.pudding;
                break;               
            case "Wasabi":
                cardImage = AssetLoader.wasabi;
                break;               
            case "Chopsticks":
                cardImage = AssetLoader.chopsticks;
                break;
        }
        
        // default size including the black/green outline, will be 100 x 150
        this.width = 99;
        this.height = 149; 
        this.color = Color.BLACK;
        this.scale = 1.0;
    }    
    
    /*
        A copy constructor used to make a deep copy of the card object. Atleast
        that's what I hope is happening. This is to avoid using the cloneable
        interface which according to the StackOverflow community is hella jank.
    */
    public Card(Card copyCard) {
        
        this.cardName = copyCard.cardName;
        this.cardImage = copyCard.cardImage;
        this.scale = copyCard.scale;
        this.x = copyCard.x;
        this.y = copyCard.y;
        this.startX = copyCard.startX;
        this.startY = copyCard.startY;
        this.xVel = copyCard.xVel;
        this.yVel = copyCard.yVel;
        this.width = copyCard.width;
        this.height = copyCard.height;
        this.color = copyCard.color;
        this.dragged = copyCard.dragged;
    }
    
    public void moveLeft() {
        
        x -= 30;
    }
    
    public void moveUp() {
        
        y -= 10;
    }
    
    public String getName() { return cardName; }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    public int getStartX() { return startX; }
    public int getStartY() { return startY; }
    
    public int getXVel() { return xVel; }
    public int getYVel() { return yVel; }
    
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public Rectangle getBounds() { 
        
        return dragged ? new Rectangle(0, 0, 1280, 720) : new Rectangle(x - 1, y - 1, width + 2, height + 2); 
    }

    public boolean getDragged() { return dragged; }
    
    public void setScale(double scale) { this.scale = scale; }
    
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    
    public void setStartX(int startX) {
        
        this.startX = startX;
        x = startX;
    }
    
    public void setStartY(int startY) {
        
        this.startY = startY;
        y = startY;
    }
    
    public void setXVel(int xVel) { this.xVel = xVel; }
    public void setYVel(int yVel) { this.yVel = yVel; }
    
    public void setWidth(int width) { this.width = width; }        
    public void setHeight(int height) { this.height = height; }
    
    public void setColor(Color color) { this.color = color; }
    
    public void setDragged(boolean dragged) { this.dragged = dragged; }
    
    public void formatCard(int x, int y, double scale) {
        
        this.x = x;
        this.y = y;
        this.scale = scale;
    }
    
    public boolean contains(Point p) {
        
        if(getBounds().contains(p)) {    
            
            color = Color.GREEN;
            y--;
    
            if(y <= startY - 5)
                y = startY - 5;
            
            return true;
        
        } else {
            
            color = Color.BLACK;
            y++;
            
            if(y >= startY)
                y = startY;
            
            return false;
        }
    }
    
    // TODO, change both contains to make betterer
    public boolean contains(Point p, boolean b) {
        
        dragged = false;
        return(getBounds().contains(p));
    }
    
    public void reset() {
        
        x = startX;
        y = startY;
        color = Color.BLACK; 
        dragged = false;
    }
    
    public void update() {
    
        x += xVel;
        y += yVel;
        
        // don't move card off the screen
        if(x < 0)
            x = 0;
        
        if(x + width > 1280)
            x = 1280 - width;
        
        if(y < 0)
            y = 0;
        
        if(y + height > 720)
            y = 720 - height;
    }
    
    public void render(Graphics2D g2) {
        
        // indicates whether the card is being held
        g2.setColor(color);
        g2.fillRect(x - 1, y - 1, (int) ((width + 2) * scale), (int) ((height + 2) * scale));
        
        g2.drawImage(cardImage, x, y, (int) (width * scale), (int) (height * scale), null);
    }
    
    @Override
    public boolean equals(Object o) { 
  
        if (o == this)
            return true; 
  
        if (! (o instanceof Card))
            return false; 
          
        Card c = (Card) o;  
        
        return cardName.equals(c.cardName);
    } 
    
    @Override
    public String toString() {
        
        return cardName;
    }
}