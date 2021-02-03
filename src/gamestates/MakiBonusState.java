package gamestates;

import gamemanager.GameManager;
import assets.AssetLoader;
import guielements.Button;
import guielements.Text;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/*
    After every round, all player's makis are added up to see
    who got a bonus. This state displays this information.
*/
public class MakiBonusState extends GameState {
    
    private GameManager gameManager; 
    
    private Text makiBonusText;
    private ArrayList<Text> playerInfo;
    private Text roundNumber;
    private Button continueButton;
    private float rotate;
    
    public MakiBonusState(GameStateManager gameStateManager, GameManager gameManager) {
        
        this.gameStateManager = gameStateManager;
        this.gameManager = gameManager;
        
        this.makiBonusText = new Text("Maki Bonus!", AssetLoader.font, 60f, Color.WHITE, 400, 80);
        this.continueButton = new Button("Continue", 60f, 450, 700);
    }
    
    /*
        Updates everything within this state to the most recent changes.
    */
    public void refreshMakiBonusState() {
        
        // disable any effects of everyones game boards
        gameManager.getPlayersGameBoardCopy().setEffects(false);
        gameManager.getPlayersGameBoardCopy().setRaveMode(false);
        gameManager.getPlayersGameBoardCopy().setColor(Color.WHITE);
        
        for(int i = 0; i < gameManager.getAIsGameBoardCopies().size(); i++)
            gameManager.getAIsGameBoardCopies().get(i).setEffects(false);
        
        // set the colors of the card slots to green if a player got the most points, yellow if they got something, white if nothing
        int makiBonuses[] = gameManager.getMakiBonuses();
        Color slotColor;
        
        // doing the player first
        if(makiBonuses[0] == 6)
            slotColor = Color.GREEN;
        else if(makiBonuses[0] > 0)
            slotColor = new Color(230, 126, 0);
        else
            slotColor = Color.WHITE;
        
        gameManager.getPlayersGameBoardCopy().setColor(slotColor);
        
        // now do all the AI players
        for(int i = 0; i < gameManager.getAIsGameBoardCopies().size(); i++) {
            
            if(makiBonuses[i + 1] == 6)
                slotColor = Color.GREEN;
            else if(makiBonuses[i + 1] > 0)
                slotColor = new Color(230, 126, 0);
            else
                slotColor = Color.WHITE;
            
            gameManager.getAIsGameBoardCopies().get(i).setColor(slotColor);
        }
         
        // showing player's names, their scores before getting a maki bonus, their maki bonus, and grand total
        playerInfo = new ArrayList<>();
        Font font = AssetLoader.font;  
        
        roundNumber = new Text("Round " + (gameManager.getGameOver() ? 3 : (gameManager.getRoundNumber() - 1)), font, 25f, Color.WHITE, 1140, 710);
        
        // need this because when you get the final score for the players, it takes into account the pudding bonuses already which we don't want to show until the PuddingBonusState
        int puddingBonuses[] = gameManager.getPuddingBonuses();
        int scoreBeforeBonuses, scoreBeforePuddings;
        
        // need to re-format everyone's boards to properly fit on screen too (using the copies!) 
        switch(gameManager.getNumberOfPlayers()) {
            case 2:
                scoreBeforeBonuses = getScoreBeforeBonuses(gameManager.getPlayer().getScore(), makiBonuses[0], puddingBonuses[0]);
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getPlayer().getScore(), puddingBonuses[0]);
                playerInfo.add(new Text("You     Score: " + scoreBeforeBonuses + " + " + makiBonuses[0] + " = " + scoreBeforePuddings, font, 40f, Color.WHITE, 280, 130));
                scoreBeforeBonuses = getScoreBeforeBonuses(gameManager.getAIs().get(0).getScore(), makiBonuses[1], puddingBonuses[1]);
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(0).getScore(), puddingBonuses[1]);
                playerInfo.add(new Text(gameManager.getAI(0).getName() + "     Score: " + scoreBeforeBonuses + " + " + makiBonuses[1] + " = " + scoreBeforePuddings, font, 40f, Color.WHITE, 280, 430));
                gameManager.getPlayersGameBoardCopy().formatBoard(280, 150, 0.75);     
                gameManager.getAIsGameBoardCopies().get(0).formatBoard(280, 450, 0.75);
                break;
            case 3:
                scoreBeforeBonuses = getScoreBeforeBonuses(gameManager.getPlayer().getScore(), makiBonuses[0], puddingBonuses[0]);
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getPlayer().getScore(), puddingBonuses[0]);
                playerInfo.add(new Text("You     Score: " + scoreBeforeBonuses + " + " + makiBonuses[0] + " = " + scoreBeforePuddings, font, 35f, Color.WHITE, 360, 150));                                 
                scoreBeforeBonuses = getScoreBeforeBonuses(gameManager.getAIs().get(0).getScore(), makiBonuses[1], puddingBonuses[1]);
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(0).getScore(), puddingBonuses[1]);
                playerInfo.add(new Text(gameManager.getAI(0).getName() + "     Score: " + scoreBeforeBonuses + " + " + makiBonuses[1] + " = " + scoreBeforePuddings, font, 35f, Color.WHITE, 30, 430)); 
                scoreBeforeBonuses = getScoreBeforeBonuses(gameManager.getAIs().get(1).getScore(), makiBonuses[2], puddingBonuses[2]);
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(1).getScore(), puddingBonuses[2]);
                playerInfo.add(new Text(gameManager.getAI(1).getName() + "     Score: " + scoreBeforeBonuses + " + " + makiBonuses[2] + " = " + scoreBeforePuddings, font, 35f, Color.WHITE, 660, 430));              
                gameManager.getPlayersGameBoardCopy().formatBoard(360, 170, 0.6);
                gameManager.getAIsGameBoardCopies().get(0).formatBoard(30, 450, 0.6);
                gameManager.getAIsGameBoardCopies().get(1).formatBoard(660, 450, 0.6);
                break;
            case 4:
                scoreBeforeBonuses = getScoreBeforeBonuses(gameManager.getPlayer().getScore(), makiBonuses[0], puddingBonuses[0]);
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getPlayer().getScore(), puddingBonuses[0]);
                playerInfo.add(new Text("You     Score: " + scoreBeforeBonuses + " + " + makiBonuses[0] + " = " + scoreBeforePuddings, font, 30f, Color.WHITE, 60, 170));               
                scoreBeforeBonuses = getScoreBeforeBonuses(gameManager.getAIs().get(0).getScore(), makiBonuses[1], puddingBonuses[1]);
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(0).getScore(), puddingBonuses[1]);
                playerInfo.add(new Text(gameManager.getAI(0).getName() + "     Score: " + scoreBeforeBonuses + " + " + makiBonuses[1] + " = " + scoreBeforePuddings, font, 30f, Color.WHITE, 660, 170));                 
                scoreBeforeBonuses = getScoreBeforeBonuses(gameManager.getAIs().get(1).getScore(), makiBonuses[2], puddingBonuses[2]);
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(1).getScore(), puddingBonuses[2]);
                playerInfo.add(new Text(gameManager.getAI(1).getName() + "     Score: " + scoreBeforeBonuses + " + " + makiBonuses[2] + " = " + scoreBeforePuddings, font, 30f, Color.WHITE, 60, 440));          
                scoreBeforeBonuses = getScoreBeforeBonuses(gameManager.getAIs().get(2).getScore(), makiBonuses[3], puddingBonuses[3]);
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(2).getScore(), puddingBonuses[3]);
                playerInfo.add(new Text(gameManager.getAI(2).getName() + "     Score: " + scoreBeforeBonuses + " + " + makiBonuses[3] + " = " + scoreBeforePuddings, font, 30f, Color.WHITE, 660, 440));           
                gameManager.getPlayersGameBoardCopy().formatBoard(60, 180, 0.58);
                gameManager.getAIsGameBoardCopies().get(0).formatBoard(660, 180, 0.58);
                gameManager.getAIsGameBoardCopies().get(1).formatBoard(60, 450, 0.58);
                gameManager.getAIsGameBoardCopies().get(2).formatBoard(660, 450, 0.58);
                break;
            case 5:
                scoreBeforeBonuses = getScoreBeforeBonuses(gameManager.getPlayer().getScore(), makiBonuses[0], puddingBonuses[0]);
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getPlayer().getScore(), puddingBonuses[0]);
                playerInfo.add(new Text("You     Score: " + scoreBeforeBonuses + " + " + makiBonuses[0] + " = " + scoreBeforePuddings, font, 25f, Color.WHITE, 400, 310));     
                scoreBeforeBonuses = getScoreBeforeBonuses(gameManager.getAIs().get(0).getScore(), makiBonuses[1], puddingBonuses[1]);
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(0).getScore(), puddingBonuses[1]);
                playerInfo.add(new Text(gameManager.getAI(0).getName() + "     Score: " + scoreBeforeBonuses + " + " + makiBonuses[1] + " = " + scoreBeforePuddings, font, 25f, Color.WHITE, 30, 110)); 
                scoreBeforeBonuses = getScoreBeforeBonuses(gameManager.getAIs().get(1).getScore(), makiBonuses[2], puddingBonuses[2]);
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(1).getScore(), puddingBonuses[2]);
                playerInfo.add(new Text(gameManager.getAI(1).getName() + "     Score: " + scoreBeforeBonuses + " + " + makiBonuses[2] + " = " + scoreBeforePuddings, font, 25f, Color.WHITE, 770, 110));
                scoreBeforeBonuses = getScoreBeforeBonuses(gameManager.getAIs().get(2).getScore(), makiBonuses[3], puddingBonuses[3]);
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(2).getScore(), puddingBonuses[3]);
                playerInfo.add(new Text(gameManager.getAI(2).getName() + "     Score: " + scoreBeforeBonuses + " + " + makiBonuses[3] + " = " + scoreBeforePuddings, font, 25f, Color.WHITE, 30, 510)); 
                scoreBeforeBonuses = getScoreBeforeBonuses(gameManager.getAIs().get(3).getScore(), makiBonuses[4], puddingBonuses[4]);
                scoreBeforePuddings = getScoreBeforePuddings(gameManager.getAIs().get(3).getScore(), puddingBonuses[4]);
                playerInfo.add(new Text(gameManager.getAI(3).getName() + "     Score: " + scoreBeforeBonuses + " + " + makiBonuses[4] + " = " + scoreBeforePuddings, font, 25f, Color.WHITE, 770, 510)); 
                gameManager.getPlayersGameBoardCopy().formatBoard(400, 320, 0.5);
                gameManager.getAIsGameBoardCopies().get(0).formatBoard(30, 120, 0.5);
                gameManager.getAIsGameBoardCopies().get(1).formatBoard(770, 120, 0.5);
                gameManager.getAIsGameBoardCopies().get(2).formatBoard(30, 520, 0.5);
                gameManager.getAIsGameBoardCopies().get(3).formatBoard(770, 520, 0.5);
                this.roundNumber.setVisible(false);
                break;
        }
    }
 
    private int getScoreBeforeBonuses(int totalScore, int makiBonus, int puddingBonus) {
        
        return totalScore -= (makiBonus + puddingBonus);
    }
    
    private int getScoreBeforePuddings(int totalScore, int puddingBonus) {
        
        return totalScore -= puddingBonus;
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
        
        makiBonusText.render(g2);
        
        // maki swag
        AffineTransform restoreTransform = g2.getTransform();
        g2.rotate(Math.toRadians(rotate), 312, 82);
        g2.drawImage(AssetLoader.makiRollIcon, 250, 20, 124, 124, null);
        g2.setTransform(restoreTransform);
        g2.rotate(Math.toRadians(-rotate), 962, 82);
        g2.drawImage(AssetLoader.makiRollIcon, 900, 20, 124, 124, null);   
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
            
            if(gameManager.getGameOver()) {
                
                gameStateManager.refreshPuddingBonusState();
                gameStateManager.setCurrentState(GameStateManager.PUDDING_BONUS_STATE);
            
            } else {
                
                continueButton.reset();
                gameManager.setRoundOver(false);
                gameStateManager.refreshPlayersHandState();
                gameStateManager.setCurrentState(GameStateManager.PLAYERS_HAND_STATE);
            }
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