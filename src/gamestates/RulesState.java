package gamestates;

import assets.AssetLoader;
import guielements.Button;
import guielements.Text;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class RulesState extends GameState {

    private BufferedImage[] pages;
    private int pageNumber;
    private Button leftButton, rightButton, backButton;
    private Text pageNumberText;
    
    public RulesState(GameStateManager gsm) {
        
        this.gameStateManager = gsm;
        
        pages = AssetLoader.rulesPages;
        
        leftButton = new Button(AssetLoader.arrowIconLeft, 10, 200, 250, 400);
        rightButton = new Button(AssetLoader.arrowIconRight, 1020, 200, 250, 400);
        backButton = new Button("Back", 60f, 30, 80);
        
        pageNumberText = new Text("Page " + Integer.toString(pageNumber + 1), AssetLoader.font, 60f, Color.WHITE, 1020, 80);
    }
    
    private void cycleLeft() {
        
        pageNumber--;
        
        if(pageNumber < 0)
            pageNumber = pages.length - 1;
        
        pageNumberText.setText("Page " + Integer.toString(pageNumber + 1));
    }
    
    private void cycleRight() {
        
        pageNumber++;
        
        if(pageNumber > pages.length - 1)
            pageNumber = 0;
        
        pageNumberText.setText("Page " + Integer.toString(pageNumber + 1));
    }
        
    @Override
    public void update() {
        
        leftButton.update();
        rightButton.update();
        backButton.update();
    }

    @Override
    public void render(Graphics2D g2) {
        
        g2.drawImage(AssetLoader.menuBackground, 0, 0, 1280, 720, null);
        g2.setColor(Color.BLACK);
        g2.fillRect(270, 0, 730, 720);
        
        g2.drawImage(pages[pageNumber], 280, 0, 710, 720, null);
        
        leftButton.render(g2);
        rightButton.render(g2);
        backButton.render(g2);
        
        pageNumberText.render(g2);
    }

    @Override
    public void handleMouseClicked(Point p) {
        
        if(leftButton.contains(p))
            cycleLeft();
        
        if(rightButton.contains(p))
            cycleRight();
        
        if(backButton.contains(p)) {
            
            leftButton.reset();
            rightButton.reset();
            backButton.reset();
                        
            gameStateManager.setCurrentState(GameStateManager.MAIN_MENU_STATE);
        }
    }
    
    @Override
    public void handleMouseReleased(Point p) {}

    @Override
    public void handleMouseMoved(Point p) {
        
        leftButton.contains(p);
        rightButton.contains(p);
        backButton.contains(p);
    }

    @Override
    public void handleMouseDragged(Point p) {}
}