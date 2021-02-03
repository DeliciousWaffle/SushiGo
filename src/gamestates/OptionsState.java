package gamestates;

import gamemanager.GameManager;
import assets.AssetLoader;
import guielements.Button;

import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class OptionsState extends GameState {
    
    private GameManager gm;
    private Button numberOfPlayersButton, difficultyButton, fullscreenButton, backButton;
    private int numberOfPlayers, difficulty;
    private ArrayList<BufferedImage> numberOfPlayersIcons;
    private ArrayList<BufferedImage> difficultyLevelIcons;
    private float rotateRight;
    
    public OptionsState(GameStateManager gsm, GameManager gm) {
        
        this.gameStateManager = gsm;
        this.gm = gm;
        
        numberOfPlayers = gm.getNumberOfPlayers();
        difficulty = gm.getDifficulty();
        
        numberOfPlayersButton = new Button("Players:", 80f, 80, 300);
        difficultyButton = new Button("Difficulty:", 80f, 80, 450);
        fullscreenButton = new Button("Fullscreen: On", 80f, 80, 600);
        backButton = new Button("Back", 70f, 50, 100);
        
        numberOfPlayersIcons = new ArrayList<>();
        numberOfPlayersIcons.add(AssetLoader.dumplingIcon);
        numberOfPlayersIcons.add(AssetLoader.wasabiIcon);
        numberOfPlayersIcons.add(AssetLoader.chopsticksIcon);
        numberOfPlayersIcons.add(AssetLoader.sashimiIcon);
        numberOfPlayersIcons.add(AssetLoader.tempuraIcon);
        
        difficultyLevelIcons = new ArrayList<>();
        
        for(int i = 0; i < 3; i++)
            difficultyLevelIcons.add(AssetLoader.makiRollIcon);
    }
    
    // you can't just simply change the size to fullscreen, gotta go through a lot of bs
    private void changeWindowType() {
        
        JFrame frame = gameStateManager.getFrame();
        frame.dispose();
        
        // fullscreen off
        if(gameStateManager.getFrame().isDefaultLookAndFeelDecorated()) {
            
            frame.setExtendedState(JFrame.NORMAL);
            frame.setSize(gameStateManager.getFrame().getContentPane().getPreferredSize());
            frame.setUndecorated(false);
            frame.setDefaultLookAndFeelDecorated(false);
            frame.setLocationRelativeTo(null);
            fullscreenButton.setText("Fullscreen: Off");
            
        // fullscreen on
        } else {
            
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
            frame.setDefaultLookAndFeelDecorated(true);
            fullscreenButton.setText("Fullscreen: On");
        }
        
        frame.setVisible(true);
    }
    
    @Override
    public void update() {
        
        numberOfPlayersButton.update();
        difficultyButton.update();
        fullscreenButton.update();
        backButton.update();
        
        rotateRight += 1.2f;
        
        if(rotateRight >= 10000)
            rotateRight = 0;
    }

    @Override
    public void render(Graphics2D g2) {
        
        g2.drawImage(AssetLoader.menuBackground, 0, 0, 1280, 720, null);
        
        numberOfPlayersButton.render(g2);
        difficultyButton.render(g2);
        fullscreenButton.render(g2);
        backButton.render(g2);
        
        // drawing corresponding number of sushis to the screen, rotation is overally complicated and stupid
        // if you don't understand what's going on, that's ok, neither do I
        int offsetX = 0;
        for(int i = 0; i < numberOfPlayers; i++) {
            
            AffineTransform restoreAT = g2.getTransform();
            
            g2.rotate(Math.toRadians(rotateRight), 630 + offsetX, 220);
            g2.drawImage(numberOfPlayersIcons.get(i), 530 + offsetX, 110, 200, 200, null);
            
            offsetX += 130;
            
            g2.setTransform(restoreAT);
        }
        
        offsetX = 0;
        for(int i = 0; i < difficulty; i++) {
            
            AffineTransform restoreAT = g2.getTransform();
            int anotherOffsetX = 0;
            
            for(int j = 0; j <= i; j++) {
                
                AffineTransform restoreAT2 = g2.getTransform();
                
                g2.rotate(Math.toRadians(rotateRight), 730 + offsetX + anotherOffsetX, 450);
                g2.drawImage(difficultyLevelIcons.get(i), 630 + offsetX + anotherOffsetX, 350, 200, 200, null);
                
                anotherOffsetX += 50;
                
                g2.setTransform(restoreAT2);
            }
            
            offsetX += 80;
            offsetX += anotherOffsetX;
            
            g2.setTransform(restoreAT);
        }
    }

    @Override
    public void handleMouseClicked(Point p) {
        
        if(numberOfPlayersButton.contains(p)) {
            
            numberOfPlayers++;
            
            if(numberOfPlayers > 5)
                numberOfPlayers = 2;
        }
        
        if(difficultyButton.contains(p)) {
            
            difficulty++;
            
            if(difficulty > 3)
                difficulty = 1;
        }
        
        if(fullscreenButton.contains(p))
            changeWindowType();
        
        if(backButton.contains(p)) {
            
            numberOfPlayersButton.reset();
            difficultyButton.reset();
            fullscreenButton.reset();
            backButton.reset();
            
            gm.setNumberOfPlayers(numberOfPlayers);
            gm.setDifficulty(difficulty);
            
            gameStateManager.setCurrentState(GameStateManager.MAIN_MENU_STATE);
        }
    }
    
    @Override
    public void handleMouseReleased(Point p) {}

    @Override
    public void handleMouseMoved(Point p) {
        
        numberOfPlayersButton.contains(p);
        difficultyButton.contains(p);
        fullscreenButton.contains(p);
        backButton.contains(p);
    }

    @Override
    public void handleMouseDragged(Point p) {}
}