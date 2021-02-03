package gamestates;

import gamemanager.GameManager;

import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class GameStateManager {
    
    private GameManager gameManager;
    private ArrayList<GameState> gameStates;
    public static final int MAIN_MENU_STATE = 0;
    public static final int RULES_STATE = 1;
    public static final int OPTIONS_STATE = 2;
    public static final int CREDITS_STATE = 3;  
    public static final int PLAYERS_HAND_STATE = 4;
    public static final int GAME_BOARD_STATE = 5;
    public static final int QUICK_GAME_SUMMARY_STATE = 6;
    public static final int MAKI_BONUS_STATE = 7;
    public static final int PUDDING_BONUS_STATE = 8;
    public static final int SCORE_SCREEN_STATE = 9;
    private int currentState, previousState;
    private JFrame frame;
    
    public GameStateManager(GameManager gameManager) {
        
        this.gameManager = gameManager;
        
        gameStates = new ArrayList<>();
        gameStates.add(new MainMenuState(this, gameManager));
        gameStates.add(new RulesState(this));
        gameStates.add(new OptionsState(this, gameManager));
        gameStates.add(new CreditsState(this));
        gameStates.add(new PlayersHandState(this, gameManager));
        gameStates.add(new GameBoardState(this, gameManager));
        gameStates.add(new QuickGameSummaryState(this));
        gameStates.add(new MakiBonusState(this, gameManager));
        gameStates.add(new PuddingBonusState(this, gameManager));
        gameStates.add(new ScoreScreenState(this, gameManager)); 
        
        currentState = MAIN_MENU_STATE;
    }
    
    public void setCurrentState(int currentState) { this.currentState = currentState; }
    public void setPreviousState(int previousState) { this.previousState = previousState; }
    public void setFrame(JFrame frame) { this.frame = frame; }
    
    public int getCurrentState() { return currentState; }
    public int getPreviousState() { return previousState; }
    public JFrame getFrame() { return frame; }
    
    /*
        Once the user finished selecting their options and hits the start button, 
        this initializes all the variables within the GameManager, PlayersHandState,
        and GameBoardState.
    */
    public void startGame() {
        
        gameManager.init();
        PlayersHandState phs = (PlayersHandState) gameStates.get(GameStateManager.PLAYERS_HAND_STATE);
        phs.init();
        GameBoardState gbs = (GameBoardState) gameStates.get(GameStateManager.GAME_BOARD_STATE);
        gbs.init();
    }
    
    /*
        Takes the player to the ScoreScreenState once the game ends to see who won and everyone's scores.
    */
    public void endGame() {
        
        ScoreScreenState sss = (ScoreScreenState) gameStates.get(GameStateManager.SCORE_SCREEN_STATE);
        sss.init();
    }
    
    /*
        Restarts the game, but with the same players and difficulty level. The order of
        operations is confusing here, but works.
    */
    public void restartGame() {
        
        int numberOfPlayers = gameManager.getNumberOfPlayers();
        int difficulty = gameManager.getDifficulty();
        ArrayList<String> AIsNames = gameManager.getAIsNames();
        
        gameManager = new GameManager();
        gameManager.setNumberOfPlayers(numberOfPlayers);
        gameManager.setDifficulty(difficulty);
        
        gameStates.set(GameStateManager.PLAYERS_HAND_STATE, new PlayersHandState(this, gameManager));
        gameStates.set(GameStateManager.GAME_BOARD_STATE, new GameBoardState(this, gameManager));
        gameStates.set(GameStateManager.SCORE_SCREEN_STATE, new ScoreScreenState(this, gameManager));
        gameStates.set(GameStateManager.MAKI_BONUS_STATE, new MakiBonusState(this, gameManager));
        gameStates.set(GameStateManager.PUDDING_BONUS_STATE, new PuddingBonusState(this, gameManager));
        
        startGame();
        
        gameManager.setAIsNames(AIsNames);
    }
    
    /*
        Takes the player to the main menu. Also restarts the game with new players, more/less players,
        and a different difficulty level depending on what/if the user changed any options.
    */
    public void restartGameAndGoToMainMenu() {
        
        gameManager = new GameManager();
        
        gameStates = new ArrayList<>();
        gameStates.add(new MainMenuState(this, gameManager));
        gameStates.add(new RulesState(this));
        gameStates.add(new OptionsState(this, gameManager));
        gameStates.add(new CreditsState(this));
        gameStates.add(new PlayersHandState(this, gameManager));
        gameStates.add(new GameBoardState(this, gameManager));
        gameStates.add(new QuickGameSummaryState(this));
        gameStates.add(new MakiBonusState(this, gameManager));
        gameStates.add(new PuddingBonusState(this, gameManager));
        gameStates.add(new ScoreScreenState(this, gameManager)); 
        
        currentState = MAIN_MENU_STATE;
    }
    
    public void refreshPlayersHandState() {
        
        PlayersHandState phs = (PlayersHandState) gameStates.get(GameStateManager.PLAYERS_HAND_STATE);
        phs.refreshPlayersHandState();
    }
    
    public void refreshGameBoardState() {
        
        GameBoardState gbs = (GameBoardState) gameStates.get(GameStateManager.GAME_BOARD_STATE);
        gbs.refreshGameBoardState();
    }
    
    public void refreshMakiBonusState() {
     
        MakiBonusState mbs = (MakiBonusState) gameStates.get(GameStateManager.MAKI_BONUS_STATE);
        mbs.refreshMakiBonusState();
    }
    
    public void refreshPuddingBonusState() {
     
        PuddingBonusState pbs = (PuddingBonusState) gameStates.get(GameStateManager.PUDDING_BONUS_STATE);
        pbs.refreshPuddingBonusState();
    }
    
    public void resetCreditsState() {
        
        gameStates.set(GameStateManager.CREDITS_STATE, new CreditsState(this));
    }
    
    public void update() { 
        
        gameStates.get(currentState).update(); 
    }
    
    public void render(Graphics2D g2) { 
        
        gameStates.get(currentState).render(g2); 
    }

    public void handleMouseClicked(Point p) { 
        
        gameStates.get(currentState).handleMouseClicked(p); 
    }
    
    public void handleMouseReleased(Point p) { 
        
        gameStates.get(currentState).handleMouseReleased(p); 
    }

    public void handleMouseMoved(Point p) { 
        
        gameStates.get(currentState).handleMouseMoved(p); 
    }

    public void handleMouseDragged(Point p) { 
        
        gameStates.get(currentState).handleMouseDragged(p); 
    }
}