package gamestates;

import assets.AssetLoader;
import guielements.Button;
import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.Point;

public class QuickGameSummaryState extends GameState {

    private Button backButton;
    
    public QuickGameSummaryState(GameStateManager gsm) {
        
        this.gameStateManager = gsm;
        this.backButton = new Button("Back", 70f, 50, 100);
    }
    
    @Override
    public void update() {

        backButton.update();
    }

    @Override
    public void render(Graphics2D g2) {
        
        g2.drawImage(AssetLoader.tableBackground, 0, 0, 1280, 720, null);
        g2.setColor(Color.BLACK);
        g2.fillRect(270, 0, 730, 720);
        g2.drawImage(AssetLoader.quickGameSummary, 280, 0, 710, 720, null);
        backButton.render(g2);
    }

    @Override
    public void handleMouseClicked(Point p) {

        if(backButton.contains(p)) {
            
            backButton.reset();
                        
            if(gameStateManager.getPreviousState() == GameStateManager.PLAYERS_HAND_STATE)
                gameStateManager.setCurrentState(GameStateManager.PLAYERS_HAND_STATE);
            else
                gameStateManager.setCurrentState(GameStateManager.GAME_BOARD_STATE);
        }
    }
    
    @Override
    public void handleMouseReleased(Point p) {}

    @Override
    public void handleMouseMoved(Point p) {

        backButton.contains(p);
    }

    @Override
    public void handleMouseDragged(Point p) {}
}