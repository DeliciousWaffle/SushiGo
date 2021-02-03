package gamestates;

import assets.AssetLoader;
import gamemanager.GameManager;
import guielements.*;
import misc.Skittles;
import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/*
    Once all 3 rounds are over with, the player is automatically taken to this state which
    shows the winner and how everyone scored. The player can decide whether to restart the
    game, go back to the main menu, or quit the game.
*/

public class ScoreScreenState extends GameState {

    private GameManager gm;
    private Text winnerText;
    private Text[] formattedNamesAndScores;
    private Button mainMenuButton, quitButton, restartButton;
    private ArrayList<Icon> fallingSushis;
    private Random randy;
    ArrayList<Color> colors;
    int colorIterator;
    
    public ScoreScreenState(GameStateManager gsm, GameManager gm) {
        
        this.gameStateManager = gsm;
        this.gm = gm;
        
        this.randy = new Random();
        this.colors = new Skittles().tasteTheRainbow();
    }
    
    /*
        Creates all the variables that will be used within this class.
    */
    public void init() {
        
        // get the winner(s) and display their name(s)
        winnerText = new Text(gm.getWinner(), AssetLoader.font, 120f, colors.get(colorIterator), 300, 150);
        
        // depending on the size of the string, may need to resize, compare to "You Won!" which is right in the middle of the screen
        int numChars = winnerText.getText().length();
        
        if(numChars > 8) {
            
            winnerText.setX(winnerText.getX() - (numChars * 10));
            winnerText.setFontSize((float) (120 - (numChars * 2)));
        }

        // create text entries for the player's names and scores, need to adjust location based on number of players
        String[] namesAndScores = gm.getPlacesNamesAndScores();
        int numPlayers = namesAndScores.length;
        formattedNamesAndScores = new Text[numPlayers];
        
        // setting the locations of the text, names will be compared to "1st You: Score XX" which is in the middle of the screen
        switch(numPlayers) {
            case 2:
                for(int i = 0, offSetY = 0; i < formattedNamesAndScores.length; i++, offSetY += 100) {
                    numChars = namesAndScores[i].length();
                    int x = numChars > 11 ? numChars * 5 : 0;
                    formattedNamesAndScores[i] = new Text(namesAndScores[i], AssetLoader.font, 70f, Color.WHITE, 400 - x, 350 + offSetY);
                }
                break;
            case 3:
                for(int i = 0, offSetY = 0; i < formattedNamesAndScores.length; i++, offSetY += 100) {
                    numChars = namesAndScores[i].length();
                    int x = numChars > 11 ? numChars * 5 : 0;
                    formattedNamesAndScores[i] = new Text(namesAndScores[i], AssetLoader.font, 70f, Color.WHITE, 400 - x, 300 + offSetY);
                }
                break;
            case 4:
                for(int i = 0, offSetY = 0; i < formattedNamesAndScores.length; i++, offSetY += 100) {
                    numChars = namesAndScores[i].length();
                    int x = numChars > 11 ? numChars * 5 : 0;
                    formattedNamesAndScores[i] = new Text(namesAndScores[i], AssetLoader.font, 70f, Color.WHITE, 400 - x, 250 + offSetY);
                }
                break;
            case 5:
                for(int i = 0, offSetY = 0; i < formattedNamesAndScores.length; i++, offSetY += 75) {
                    numChars = namesAndScores[i].length();
                    int x = numChars > 11 ? numChars * 5 : 0;
                    formattedNamesAndScores[i] = new Text(namesAndScores[i], AssetLoader.font, 70f, Color.WHITE, 400 - x, 250 + offSetY);
                }
                break;
        }
        
        // creating the buttons
        restartButton = new Button("Restart", 70f, 50, 680);
        quitButton = new Button("Quit", 70f, 500, 680);
        mainMenuButton = new Button("Main Menu", 70f, 750, 680);
        
        // create falling background sushis because why not?
        fallingSushis = new ArrayList<>();
        
        for(int i = 0; i < 35; i++)
            fallingSushis.add(getRandomSushi(false));
    }
    
    /*
        Creates a bunch of spinning and falling sushis in random positions on screen.
        Falling from ceiling is false if you're just trying to populate the screen
        with sushis.
    */
    private Icon getRandomSushi(boolean fallingFromCeiling) {
        
        int randomIconChoice = randy.nextInt(10);
        BufferedImage randomIcon = null;
            
        switch(randomIconChoice) {
            case 0:
                randomIcon = AssetLoader.dumplingIcon;
                break;
            case 1:
                randomIcon = AssetLoader.makiRollIcon;
                break;
            case 2:
                randomIcon = AssetLoader.eggNigiriIcon;
                break;
            case 3:
                randomIcon = AssetLoader.salmonNigiriIcon;
                break;
            case 4:
                randomIcon = AssetLoader.squidNigiriIcon;
                break;
            case 5:
                randomIcon = AssetLoader.sashimiIcon;
                break;
            case 6:
                randomIcon = AssetLoader.tempuraIcon;
                break;
            case 7:
                randomIcon = AssetLoader.wasabiIcon;
                break;
            case 8:
                randomIcon = AssetLoader.chopsticksIcon;
                break;
            case 9:
                randomIcon = AssetLoader.puddingIcon;
                break;
        }
        
        // make sushis fall within the designated range so it's easier to see names and scores
        int randX = randy.nextInt(1280);
        
        // don't spawn sushis in the middle of the screen
        while(randX >=300 && randX <= 900)
            randX = randy.nextInt(1280);
        
        Icon randomSushi = new Icon(randomIcon, randX, fallingFromCeiling ? -125 : randy.nextInt(720), 125, 125);
        randomSushi.setYVel(randy.nextInt(2) + 1);
        randomSushi.setRotationVel(randy.nextBoolean() ? .03f : -.03f);
            
        return randomSushi;
    }
    
    @Override
    public void update() {
        
        restartButton.update();
        quitButton.update();
        mainMenuButton.update();
        
        // once a falling sushi goes offscreen, remove it from the list and add another one that falls from the top
        boolean sushisOffScreen = true;
        
        while(sushisOffScreen) {            
    
            for(int i = 0; i < fallingSushis.size(); i++) {
    
                int yPos = fallingSushis.get(i).getY();
                
                if(yPos > 720) {
                    
                    fallingSushis.remove(i);
                    break;
                }
                
                if(i >= fallingSushis.size() - 1)
                    sushisOffScreen = false;
            }
        }
        
        while(fallingSushis.size() < 35)
            fallingSushis.add(getRandomSushi(true));
          
        for(Icon i : fallingSushis)
            i.update();
        
        colorIterator += 2;
        
        if(colorIterator >= colors.size() - 1)
            colorIterator = 0;
        
        winnerText.setColor(colors.get(colorIterator));
    }

    @Override
    public void render(Graphics2D g2) {
        
        g2.drawImage(AssetLoader.tableBackground, 0, 0, 1280, 720, null);
        
        // using a for-each causes concurrent exceptions to be thrown
        for(int i = 0; i < fallingSushis.size(); i++)
            fallingSushis.get(i).render(g2);
        
        winnerText.render(g2);
        
        for(Text t : formattedNamesAndScores)
            t.render(g2);
        
        restartButton.render(g2);
        quitButton.render(g2);
        mainMenuButton.render(g2);
    }

    @Override
    public void handleMouseClicked(Point p) {
        
        if(restartButton.contains(p)) {
            
            gameStateManager.restartGame();
            gameStateManager.setCurrentState(GameStateManager.PLAYERS_HAND_STATE);
        }
        
        if(quitButton.contains(p))
            System.exit(0);
        
        if(mainMenuButton.contains(p)) {
            
            gameStateManager.restartGameAndGoToMainMenu();
            gameStateManager.setCurrentState(GameStateManager.MAIN_MENU_STATE);
        }
    }
    
    @Override
    public void handleMouseReleased(Point p) {}

    @Override
    public void handleMouseMoved(Point p) {
        
        restartButton.contains(p);
        mainMenuButton.contains(p);
        quitButton.contains(p);
    }

    @Override
    public void handleMouseDragged(Point p) {}
}