package gamestates;

import assets.AssetLoader;
import guielements.Button;
import guielements.Text;
import gamemanager.GameManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/*
    Pretty much a copy of the MakiBonusState, but for puddings.
    Both classes could have gone into a single state, but this
    makes life easier.
*/
public class PuddingBonusState extends GameState {
    
    private GameManager gameManager; 
    
    private Text puddingBonusText;
    private ArrayList<Text> playerInfo;
    private Text roundNumber;
    private Button continueButton;
    private float rotate;
    
    public PuddingBonusState(GameStateManager gameStateManager, GameManager gameManager) {
        
        this.gameStateManager = gameStateManager;
        this.gameManager = gameManager;
        
        this.puddingBonusText = new Text("Pudding Bonus!", AssetLoader.font, 60f, Color.WHITE, 340, 80);
        this.continueButton = new Button("Continue", 60f, 450, 700);
    }
    
    public void refreshPuddingBonusState() {
        
        gameManager.getPlayersGameBoardCopy().setEffects(false);
        gameManager.getPlayersGameBoardCopy().setRaveMode(false);
        gameManager.getPlayersGameBoardCopy().setColor(Color.WHITE);
        
        for(int i = 0; i < gameManager.getAIsGameBoardCopies().size(); i++) {
            
            gameManager.getAIsGameBoardCopies().get(i).setEffects(false);
            gameManager.getAIsGameBoardCopies().get(i).setColor(Color.WHITE);
        }
        
        // set the colors of the card slots to green if a player got the most points, yellow if they got something, red if they got negative points, and white if nothing
        int[] puddingBonuses = gameManager.getPuddingBonuses();
        Color slotColor;
        int puddingSlot;
        
        // doing the player first
        if(puddingBonuses[0] == 6)
            slotColor = Color.GREEN;
        else if(puddingBonuses[0] > 0)
            slotColor = new Color(230, 126, 0);
        else if(puddingBonuses[0] < 0)
            slotColor = Color.RED;
        else
            slotColor = Color.WHITE;
        
        gameManager.getPlayersGameBoardCopy().setColor(slotColor);
        
        // now do all the AI players
        for(int i = 0; i < gameManager.getAIsGameBoardCopies().size(); i++) {
            
            if(puddingBonuses[i + 1] == 6)
                slotColor = Color.GREEN;
            else if(puddingBonuses[i + 1] > 0)
                slotColor = new Color(230, 126, 0);
            else if(puddingBonuses[i + 1] < 0)
                slotColor = Color.RED;
            else
                slotColor = Color.WHITE;
            
            gameManager.getAIsGameBoardCopies().get(i).setColor(slotColor);
        }
         
        // showing player's names, their scores before getting a pudding bonus, their pudding bonus, and grand total
        playerInfo = new ArrayList<>();
        Font font = AssetLoader.font;  
        
        roundNumber = new Text("Round " + (gameManager.getRoundNumber() == 3 ? 3 : (gameManager.getRoundNumber() - 1)), font, 25f, Color.WHITE, 1140, 710);
        int scoreBeforePuddings;
        
        switch(gameManager.getNumberOfPlayers()) {
            case 2:
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getPlayer().getScore(), puddingBonuses[0]);
                playerInfo.add(new Text("You     Score: " + scoreBeforePuddings + plusOrMinus(puddingBonuses[0]) + Math.abs(puddingBonuses[0]) + " = " + gameManager.getPlayer().getScore(), font, 40f, Color.WHITE, 280, 130));
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(0).getScore(), puddingBonuses[1]);
                playerInfo.add(new Text(gameManager.getAI(0).getName() + "     Score: " + scoreBeforePuddings + plusOrMinus(puddingBonuses[1]) + Math.abs(puddingBonuses[1]) + " = " + gameManager.getAIs().get(0).getScore(), font, 40f, Color.WHITE, 280, 430));
                gameManager.getPlayersGameBoardCopy().formatBoard(280, 150, 0.75);     
                gameManager.getAIsGameBoardCopies().get(0).formatBoard(280, 450, 0.75);
                break;
            case 3:
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getPlayer().getScore(), puddingBonuses[0]);
                playerInfo.add(new Text("You     Score: " + scoreBeforePuddings + plusOrMinus(puddingBonuses[0]) + Math.abs(puddingBonuses[0]) + " = " + gameManager.getPlayer().getScore(), font, 35f, Color.WHITE, 360, 150));                                 
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(0).getScore(), puddingBonuses[1]);
                playerInfo.add(new Text(gameManager.getAI(0).getName() + "     Score: " + scoreBeforePuddings + plusOrMinus(puddingBonuses[1]) + Math.abs(puddingBonuses[1]) + " = " + gameManager.getAIs().get(0).getScore(), font, 35f, Color.WHITE, 30, 430)); 
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(1).getScore(), puddingBonuses[2]);
                playerInfo.add(new Text(gameManager.getAI(1).getName() + "     Score: " + scoreBeforePuddings + plusOrMinus(puddingBonuses[2]) + Math.abs(puddingBonuses[2]) + " = " + gameManager.getAIs().get(1).getScore(), font, 35f, Color.WHITE, 660, 430));              
                gameManager.getPlayersGameBoardCopy().formatBoard(360, 170, 0.6);
                gameManager.getAIsGameBoardCopies().get(0).formatBoard(30, 450, 0.6);
                gameManager.getAIsGameBoardCopies().get(1).formatBoard(660, 450, 0.6);
                break;
            case 4:
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getPlayer().getScore(), puddingBonuses[0]);
                playerInfo.add(new Text("You     Score: " + scoreBeforePuddings + plusOrMinus(puddingBonuses[0]) + Math.abs(puddingBonuses[0]) + " = " + gameManager.getPlayer().getScore(), font, 30f, Color.WHITE, 60, 170));          
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(0).getScore(), puddingBonuses[1]);
                playerInfo.add(new Text(gameManager.getAI(0).getName() + "     Score: " + scoreBeforePuddings + plusOrMinus(puddingBonuses[1]) + Math.abs(puddingBonuses[1]) + " = " + gameManager.getAIs().get(0).getScore(), font, 30f, Color.WHITE, 660, 170));                
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(1).getScore(), puddingBonuses[2]);
                playerInfo.add(new Text(gameManager.getAI(1).getName() + "     Score: " + scoreBeforePuddings + plusOrMinus(puddingBonuses[2]) + Math.abs(puddingBonuses[2]) + " = " + gameManager.getAIs().get(1).getScore(), font, 30f, Color.WHITE, 60, 440));               
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(2).getScore(), puddingBonuses[3]);
                playerInfo.add(new Text(gameManager.getAI(2).getName() + "     Score: " + scoreBeforePuddings + plusOrMinus(puddingBonuses[3]) + Math.abs(puddingBonuses[3]) + " = " + gameManager.getAIs().get(2).getScore(), font, 30f, Color.WHITE, 660, 440));                            
                gameManager.getPlayersGameBoardCopy().formatBoard(60, 180, 0.58);
                gameManager.getAIsGameBoardCopies().get(0).formatBoard(660, 180, 0.58);
                gameManager.getAIsGameBoardCopies().get(1).formatBoard(60, 450, 0.58);
                gameManager.getAIsGameBoardCopies().get(2).formatBoard(660, 450, 0.58);
                break;
            case 5:
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getPlayer().getScore(), puddingBonuses[0]);
                playerInfo.add(new Text("You     Score: " + scoreBeforePuddings + plusOrMinus(puddingBonuses[0]) + Math.abs(puddingBonuses[0]) + " = " + gameManager.getPlayer().getScore(), font, 25f, Color.WHITE, 400, 310));                    
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(0).getScore(), puddingBonuses[1]);
                playerInfo.add(new Text(gameManager.getAI(0).getName() + "     Score: " + scoreBeforePuddings + plusOrMinus(puddingBonuses[1]) + Math.abs(puddingBonuses[1]) + " = " + gameManager.getAIs().get(0).getScore(), font, 25f, Color.WHITE, 30, 110));                 
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(1).getScore(), puddingBonuses[2]);
                playerInfo.add(new Text(gameManager.getAI(1).getName() + "     Score: " + scoreBeforePuddings + plusOrMinus(puddingBonuses[2]) + Math.abs(puddingBonuses[2]) + " = " + gameManager.getAIs().get(1).getScore(), font, 25f, Color.WHITE, 770, 110));                
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(2).getScore(), puddingBonuses[3]);
                playerInfo.add(new Text(gameManager.getAI(2).getName() + "     Score: " + scoreBeforePuddings + plusOrMinus(puddingBonuses[3]) + Math.abs(puddingBonuses[3]) + " = " + gameManager.getAIs().get(2).getScore(), font, 25f, Color.WHITE, 30, 510)); 
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(3).getScore(), puddingBonuses[4]);
                playerInfo.add(new Text(gameManager.getAI(3).getName() + "     Score: " + scoreBeforePuddings + plusOrMinus(puddingBonuses[4]) + Math.abs(puddingBonuses[4]) + " = " + gameManager.getAIs().get(3).getScore(), font, 25f, Color.WHITE, 770, 510)); 
                gameManager.getPlayersGameBoardCopy().formatBoard(400, 320, 0.5);
                gameManager.getAIsGameBoardCopies().get(0).formatBoard(30, 120, 0.5);
                gameManager.getAIsGameBoardCopies().get(1).formatBoard(770, 120, 0.5);
                gameManager.getAIsGameBoardCopies().get(2).formatBoard(30, 520, 0.5);
                gameManager.getAIsGameBoardCopies().get(3).formatBoard(770, 520, 0.5);
                this.roundNumber.setVisible(false);
                break;
        }
    }
 
    private int getScoreBeforePuddings(int totalScore, int puddingBonus) {
        
        return totalScore -= puddingBonus;
    }
    
    private String plusOrMinus(int val) { 
        
        return val >= 0 ? " + " : " - ";
    }
    
    @Override
    public void update() {
    
        continueButton.update();
        
        rotate += 1.2f;
        
        if(rotate >= 10000)
            rotate = 0;
    }
    
    @Override
    public void render(Graphics2D g2) {
        
        g2.drawImage(AssetLoader.carpetBackground, 0, 0, 1280, 720, null);
        
        puddingBonusText.render(g2);
        
        // pudding swag
        AffineTransform restoreTransform = g2.getTransform();
        g2.rotate(Math.toRadians(rotate), 232, 82);
        g2.drawImage(AssetLoader.puddingIcon, 170, 20, 124, 124, null);
        g2.setTransform(restoreTransform);
        g2.rotate(Math.toRadians(-rotate), 1032, 82);
        g2.drawImage(AssetLoader.puddingIcon, 970, 20, 124, 124, null);   
        g2.setTransform(restoreTransform);
        
        // render all game boards before clearing them to see how many makis were scored
        gameManager.getPlayersGameBoardCopy().render(g2);
        
        for(int i = 0; i < gameManager.getAIsGameBoardCopies().size(); i++)
            gameManager.getAIsGameBoardCopies().get(i).render(g2);
        
        // render all the player names, scores, and how many points they got from makis
        for(Text t : playerInfo)
            t.render(g2);
        
        roundNumber.render(g2);
        
        continueButton.render(g2);
    }
        
    @Override
    public void handleMouseClicked(Point p) {
    
        if(continueButton.contains(p)) {
            
            gameStateManager.endGame();
            gameStateManager.setCurrentState(GameStateManager.SCORE_SCREEN_STATE);
        }
    }
    
    @Override
    public void handleMouseReleased(Point p) {}
    
    @Override
    public void handleMouseMoved(Point p) {
    
        if(continueButton.contains(p));
    }
    
    @Override
    public void handleMouseDragged(Point p) {}
}