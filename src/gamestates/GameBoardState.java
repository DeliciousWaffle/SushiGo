package gamestates;

import assets.AssetLoader;
import gamemanager.GameManager;
import guielements.Button;
import guielements.Text;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;

/*
    Purpose of this class is to show the scores of all the players, what
    everyone has played, and who is currently winning.
*/
public class GameBoardState extends GameState {

    private GameManager gameManager;
    private ArrayList<Text> playerInfo;
    private Text roundNumber;
    private Button playersHandButton, quickGameSummaryButton, quitButton;
    
    public GameBoardState(GameStateManager gameStateManager, GameManager gameManager) {
        
        this.gameStateManager = gameStateManager;
        this.gameManager = gameManager;
        
        this.playersHandButton = new Button("Hand", 60f, 50, 80);
        this.quickGameSummaryButton = new Button("Quick Guide", 60f, 450, 80);
        this.quitButton = new Button("Quit", 60f, 1080, 80);
    }
    
    public ArrayList<Text> getPlayerInfo() { return playerInfo; }
    public Text getRoundNumber() { return roundNumber; }
    
    public void init() {
        
        this.playerInfo = new ArrayList<>();
        Font font = AssetLoader.font;
        this.roundNumber = new Text("Round " + Integer.toString(gameManager.getRoundNumber()), font, 25f, Color.WHITE, 1140, 710);
        
        // depending on the number of players, information and game boards get placed accordingly
        switch(gameManager.getNumberOfPlayers()) {
            case 2:
                playerInfo.add(new Text("You     Score: 0", font, 40f, Color.WHITE, 280, 130));                                  // top
                playerInfo.add(new Text(gameManager.getAI(0).getName() + "     Score: 0", font, 40f, Color.WHITE, 280, 430));    // bottom
                gameManager.getAI(0).getAIsGameBoard().formatBoard(280, 450, 0.75);
                break;
            case 3:
                playerInfo.add(new Text("You     Score: 0", font, 35f, Color.WHITE, 360, 150));                                  // top center
                playerInfo.add(new Text(gameManager.getAI(0).getName() + "     Score: 0", font, 35f, Color.WHITE, 30, 430));     // bottom left
                gameManager.getAI(0).getAIsGameBoard().formatBoard(30, 450, 0.6);
                playerInfo.add(new Text(gameManager.getAI(1).getName() + "     Score: 0", font, 35f, Color.WHITE, 660, 430));    // bottom right
                gameManager.getAI(1).getAIsGameBoard().formatBoard(660, 450, 0.6); 
                break;
            case 4:
                playerInfo.add(new Text("You     Score: 0", font, 30f, Color.WHITE, 60, 170));                                   // top left
                playerInfo.add(new Text(gameManager.getAI(0).getName() + "     Score: 0", font, 30f, Color.WHITE, 660, 170));    // top right
                gameManager.getAI(0).getAIsGameBoard().formatBoard(660, 180, 0.58);
                playerInfo.add(new Text(gameManager.getAI(1).getName() + "     Score: 0", font, 30f, Color.WHITE, 60, 440));     // bottom left
                gameManager.getAI(1).getAIsGameBoard().formatBoard(60, 450, 0.58);
                playerInfo.add(new Text(gameManager.getAI(2).getName() + "     Score: 0", font, 30f, Color.WHITE, 660, 440));    // bottom right
                gameManager.getAI(2).getAIsGameBoard().formatBoard(660, 450, 0.58);
                break;
            case 5:
                playerInfo.add(new Text("You     Score: 0", font, 25f, Color.WHITE, 400, 310));                                  // center
                playerInfo.add(new Text(gameManager.getAI(0).getName() + "     Score: 0", font, 25f, Color.WHITE, 30, 110));     // top left
                gameManager.getAI(0).getAIsGameBoard().formatBoard(30, 120, 0.5);
                playerInfo.add(new Text(gameManager.getAI(1).getName() + "     Score: 0", font, 25f, Color.WHITE, 770, 110));    // top right
                gameManager.getAI(1).getAIsGameBoard().formatBoard(770, 120, 0.5);
                playerInfo.add(new Text(gameManager.getAI(2).getName() + "     Score: 0", font, 25f, Color.WHITE, 30, 510));     // bottom left
                gameManager.getAI(2).getAIsGameBoard().formatBoard(30, 520, 0.5);
                playerInfo.add(new Text(gameManager.getAI(3).getName() + "     Score: 0", font, 25f, Color.WHITE, 770, 510));    // bottom right
                gameManager.getAI(3).getAIsGameBoard().formatBoard(770, 520, 0.5);
                // need to change roundNumber's position to fit better when there is 5 players
                this.roundNumber.setX(580);
                break;
        }
        
        // get rid of all blinking effects for the ai boards
        for(int i = 0; i < gameManager.getAIs().size(); i++)
            gameManager.getAIs().get(i).getAIsGameBoard().setEffects(false);
    }
    
    /*
        Method is called when the Players Hand State switches to this state. This method takes care of updating
        variables and values that need to get updated like scores or formatting of the everyone's boards.
        Purpose is so that this state is updated only once when called.
    */
    public void refreshGameBoardState() {
        
        // update the scores for each player
        playerInfo.get(0).setText("You     Score: " + gameManager.getPlayer().getScore()); 
        for(int i = 1; i < playerInfo.size(); i++)
            playerInfo.get(i).setText(gameManager.getAI(i - 1).getName() + "     Score: " + gameManager.getAI(i - 1).getScore());
        
        // update the round number
        roundNumber.setText("Round " + gameManager.getRoundNumber());
        
        // format each player's board        
        switch(gameManager.getNumberOfPlayers()) {
            case 2:
                gameManager.getPlayer().getPlayersGameBoard().formatBoard(280, 150, 0.75);     
                gameManager.getAI(0).getAIsGameBoard().formatBoard(280, 450, 0.75);
                break;
            case 3:
                gameManager.getPlayer().getPlayersGameBoard().formatBoard(360, 170, 0.6);
                gameManager.getAI(0).getAIsGameBoard().formatBoard(30, 450, 0.6);
                gameManager.getAI(1).getAIsGameBoard().formatBoard(660, 450, 0.6);
                break;
            case 4:
                gameManager.getPlayer().getPlayersGameBoard().formatBoard(60, 180, 0.58);
                gameManager.getAI(0).getAIsGameBoard().formatBoard(660, 180, 0.58);
                gameManager.getAI(1).getAIsGameBoard().formatBoard(60, 450, 0.58);
                gameManager.getAI(2).getAIsGameBoard().formatBoard(660, 450, 0.58);
                break;
            case 5:
                gameManager.getPlayer().getPlayersGameBoard().formatBoard(400, 320, 0.5);
                gameManager.getAI(0).getAIsGameBoard().formatBoard(30, 120, 0.5);
                gameManager.getAI(1).getAIsGameBoard().formatBoard(770, 120, 0.5);
                gameManager.getAI(2).getAIsGameBoard().formatBoard(30, 520, 0.5);
                gameManager.getAI(3).getAIsGameBoard().formatBoard(770, 520, 0.5);
                break;
        }
        
        // disable all the effects for each player
        gameManager.getPlayer().getPlayersGameBoard().setEffects(false);
        
        for(int i = 0; i < gameManager.getAIs().size(); i++)
            gameManager.getAI(i).getAIsGameBoard().setEffects(false);
    }
    
    @Override
    public void update() {
        
        playersHandButton.update();
        quickGameSummaryButton.update();
        quitButton.update();
    }

    @Override
    public void render(Graphics2D g2) {
        
        g2.drawImage(AssetLoader.tableBackground, 0, 0, 1280, 720, null);
        
        playersHandButton.render(g2);
        quickGameSummaryButton.render(g2);
        quitButton.render(g2);
        
        for(Text t : playerInfo)
            t.render(g2);
        
        roundNumber.render(g2);
        
        gameManager.getPlayer().getPlayersGameBoard().render(g2);
        for(int i = 0; i < gameManager.getAIs().size(); i++)
            gameManager.getAIs().get(i).getAIsGameBoard().render(g2);   
    }

    @Override
    public void handleMouseClicked(Point p) {
        
        if(playersHandButton.contains(p)) {
            
            playersHandButton.reset();
            quickGameSummaryButton.reset();
            quitButton.reset();
            gameStateManager.refreshPlayersHandState();
            gameStateManager.setCurrentState(GameStateManager.PLAYERS_HAND_STATE);
        }
        
        if(quickGameSummaryButton.contains(p)) {
            
            playersHandButton.reset();
            quickGameSummaryButton.reset();
            quitButton.reset();
            gameStateManager.setPreviousState(GameStateManager.GAME_BOARD_STATE);
            gameStateManager.setCurrentState(GameStateManager.QUICK_GAME_SUMMARY_STATE);
        }
        
        if(quitButton.contains(p))
            System.exit(0);
    }
    
    @Override
    public void handleMouseReleased(Point p) {}

    @Override
    public void handleMouseMoved(Point p) {
        
        playersHandButton.contains(p);
        quickGameSummaryButton.contains(p);
        quitButton.contains(p);
    }

    @Override
    public void handleMouseDragged(Point p) {}
}