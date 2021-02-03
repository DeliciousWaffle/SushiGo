package gamestates;

import gamemanager.GameManager;
import assets.AssetLoader;
import guielements.Button;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class MainMenuState extends GameState {
    
    private GameManager gm;
    private Button startButton, rulesButton, optionsButton, creditsButton, exitButton;
    private float theta, county = 0.1f;
    
    public MainMenuState(GameStateManager gsm, GameManager gm) {
        
        this.gameStateManager = gsm;
        this.gm = gm;
        
        startButton = new Button("Start Game", 90f, 330, 290);
        rulesButton = new Button("Rules", 90f, 490, 390);
        optionsButton = new Button("Options", 90f, 430, 490);
        creditsButton = new Button("Credits", 90f, 440, 590);
        exitButton = new Button("Exit", 90f, 540, 690);
    }
    
    @Override
    public void update() {
        
        startButton.update();
        rulesButton.update();
        optionsButton.update();
        creditsButton.update();
        exitButton.update();
        
        theta += county;
        if(theta >= 1.5 || theta <= -1.5)
            county = -county;
    }
    
    @Override
    public void render(Graphics2D g2) {
        
        g2.drawImage(AssetLoader.menuBackground, 0, 0, 1280, 720, null);
        
        startButton.render(g2);                                             
        rulesButton.render(g2);
        optionsButton.render(g2);
        creditsButton.render(g2);
        exitButton.render(g2);
        
        g2.rotate(Math.toRadians(theta), 640, 180);
        g2.setFont(AssetLoader.font.deriveFont(160f));
        g2.setColor(Color.BLACK);
        g2.drawString("Sushi Go!", 170, 180);                               
        g2.setColor(Color.WHITE);
        g2.drawString("Sushi Go!", 160, 170);
    }

    @Override
    public void handleMouseClicked(Point p) {
        
        if(startButton.contains(p)) {
            
            gameStateManager.startGame();
            gameStateManager.setCurrentState(GameStateManager.PLAYERS_HAND_STATE);
        }
        
        if(rulesButton.contains(p)) {
            
            startButton.reset();
            rulesButton.reset();
            optionsButton.reset();
            creditsButton.reset();
            exitButton.reset();
            gameStateManager.setCurrentState(GameStateManager.RULES_STATE);
        }
        
        if(optionsButton.contains(p)) {
            
            startButton.reset();
            rulesButton.reset();
            optionsButton.reset();
            creditsButton.reset();
            exitButton.reset();
            gameStateManager.setCurrentState(GameStateManager.OPTIONS_STATE);
        }
        
        if(creditsButton.contains(p)) {
            
            startButton.reset();
            rulesButton.reset();
            optionsButton.reset();
            creditsButton.reset();
            exitButton.reset();
            gameStateManager.setCurrentState(GameStateManager.CREDITS_STATE);
        }
        
        if(exitButton.contains(p))
            System.exit(0);
    }
    
    @Override
    public void handleMouseReleased(Point p) {}
    
    @Override
    public void handleMouseMoved(Point p) {
    
        startButton.contains(p);
        rulesButton.contains(p);
        optionsButton.contains(p);
        creditsButton.contains(p);
        exitButton.contains(p);
    }

    @Override
    public void handleMouseDragged(Point p) {}
}
