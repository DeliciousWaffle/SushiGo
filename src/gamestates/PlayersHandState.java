package gamestates;

import gamemanager.GameManager;
import assets.AssetLoader;
import cards.Card;
import guielements.*;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

/*
    Interface in which the player selects and plays cards. Player clicks
    on a card and drags it the appropriate destination. The card organizer
    determines whether the spot is valid or not. If the card is valid, the
    player drops the card in a slot and gets scored. The player can choose
    to go the game board (to see what other cards have been played as well as
    scores), the quick game summary page (to get an idea of how cards net points),
    or just simply quit the game. Uses the GameManger class as a backbone to
    retrieve info on scores, cards in hand, round number, etc. This class
    just allows the user to interact with the game and doesn't perform
    any related calculations.
*/
public class PlayersHandState extends GameState {

    private GameManager gameManager;
    private Text playersScoreText, changeInScoreText, sushiGoFlagText, roundNumberText, roundOverText;
    
    // allows the played to play 2 cards in a turn instead of 1
    // [0] = played chopsticks this turn, [1] = prevent playing multiple chopsticks this turn
    private boolean[] playedChopsticks;     
    private Card chopsticks;
    
    private Button boardButton, quickGameSummaryButton, quitButton, roundSummaryButton;
    
    // used to allow a selected card to overlap other cards when it's being dragged 
    private String dragId = null;   
    
    // when the cards transition off/on screen, prevent player from interacting with anything
    private boolean turnTransition, roundTransition, roundOver;
    
    // copies of the player's current hand and incomming hand from another player, used
    // for the sake of turnTransition and will get deleted once the new turn starts
    private ArrayList<Card> playersHandCopy, opponentsHandCopy;
    private long startTime;
    
    public PlayersHandState(GameStateManager gameStateManager, GameManager gameManager) {
        
        this.gameStateManager = gameStateManager;
        this.gameManager = gameManager;
       
        this.boardButton = new Button("Board", 60f, 50, 80);
        this.quickGameSummaryButton = new Button("Quick Guide", 60f, 450, 80);
        this.quitButton = new Button("Quit", 60f, 1080, 80);
        
        // will only appear at the end of the round to show maki/pudding bonuses
        this.roundSummaryButton = new Button("Round Summary", 80f, 240, 620);
        this.roundSummaryButton.setVisible(false);
        this.roundSummaryButton.setCanActivate(false);  
        
        this.playersScoreText = new Text("Score: 0", AssetLoader.font, 25f, Color.WHITE, 20, 710);
        this.changeInScoreText = new Text("+ 0", AssetLoader.font, 50f, Color.GREEN, 40, 670);       
        this.changeInScoreText.setVisible(false);
        this.sushiGoFlagText = new Text("Sushi Go! Play 2 Cards!", AssetLoader.font, 25f, Color.GREEN, 450, 710);
        this.roundNumberText = new Text("Round 1", AssetLoader.font, 25f, Color.WHITE, 1140, 710); 
        
        // only appears at the end of the round
        this.roundOverText = new Text("Round is Over!", AssetLoader.font, 60f, Color.WHITE, 360, 80);
        this.roundOverText.setVisible(false);
    }  
    
    /*
        Called when the player hits the start button in the MainMenuState.
        Solves a concurrency issue, so that's why this method exists.
    */
    public void init() {
        
        gameManager.getPlayer().getPlayersGameBoard().formatBoard(150, 150, 1.0);
        
        // since less players = bigger starting hand, must position cards accordingly
        int startX = 0;
        int offsetX = 0;
        
        for(int i = 2; i <= gameManager.getNumberOfPlayers(); i++)
            startX += 55;
        
        for(Card c : gameManager.getPlayer().getPlayersHand()) {
            
            c.setStartX(startX + offsetX);
            c.setStartY(520);
            offsetX += 120;
        }
        
        playedChopsticks = new boolean[]{false, false};
    }
    
    private void cyclePlayersCards() {
        
        // before swapping the cards, make a deep copy of the player's hand and the hand that is comming in to display an animation
        turnTransition = true;
        playersHandCopy = new ArrayList<>();
        opponentsHandCopy = new ArrayList<>();
        
        // copying the contents of the player's hand before swapping
        for(int i = 0; i < gameManager.getPlayer().getPlayersHand().size(); i++)
            playersHandCopy.add(new Card(gameManager.getPlayer().getPlayersHand().get(i)));   
        
        // if chopsticks were played this turn, add them to the player's hand before cycling cards
        if(chopsticks != null) {
            
            gameManager.getPlayer().getPlayersHand().add(chopsticks);
            chopsticks = null;
        }
         
        int scoreBeforeSwapping = gameManager.getPlayer().getScore();
        
        // allows the AIs to play a card, scores get updated, etc.
        gameManager.continueTurn();
        // after the turn is over with, update variables to reflect changes made
        
        // getting the change in score for the player
        int changeInScoreInt = gameManager.getPlayer().getScore() - scoreBeforeSwapping;
        String changeInScoreString = "+ " + changeInScoreInt; 
        Color changeInScoreColor = changeInScoreInt == 0 ? Color.WHITE : Color.GREEN;
        changeInScoreText.setText(changeInScoreString);
        changeInScoreText.setColor(changeInScoreColor);
        changeInScoreText.setVisible(true);
        
        // used to help show for a little while how much the player scored
        startTime = System.currentTimeMillis();
        
        // retrieve the actual score for the player
        playersScoreText.setText("Score: " + Integer.toString(gameManager.getPlayer().getScore()));
        
        // stop blinking effect of chopsticks when the player only has 1 card left to signify that they can't be played
        if(gameManager.getPlayer().getPlayersHand().size() == 1)
            gameManager.getPlayer().getPlayersGameBoard().stopChopsticksEffects();
        
        // after the turn is over, check to see if the round is over 
        roundOver = gameManager.getRoundOver();
        
        if(roundOver || gameManager.getGameOver()) {
            
            // need to subtract maki/pudding bonus because we'll show the player that info in MakiBonusState and PuddingBonusState
            changeInScoreInt -= (gameManager.getMakiBonuses()[0] + gameManager.getPuddingBonuses()[0]);
            
            changeInScoreString = "+ " + changeInScoreInt;
            changeInScoreColor = changeInScoreInt == 0 ? Color.WHITE : Color.GREEN;
            
            changeInScoreText.setText(changeInScoreString);
            changeInScoreText.setColor(changeInScoreColor);
            
            // also need to set the scores so that maki and pudding bonuses don't get added
            int totalScore = gameManager.getPlayer().getScore();
            totalScore -= (gameManager.getMakiBonuses()[0] + gameManager.getPuddingBonuses()[0]);
            
            playersScoreText.setText("Score: " + totalScore);
            
            turnTransition = false; 
            
            roundSummaryButton.setVisible(true);
            roundSummaryButton.setCanActivate(true);     
            roundOverText.setVisible(true);
            boardButton.setVisible(false);
            boardButton.setCanActivate(false);
            quickGameSummaryButton.setVisible(false);
            quickGameSummaryButton.setCanActivate(false);
            quitButton.setVisible(false);
            quitButton.setCanActivate(false);
            
            gameManager.getPlayersGameBoardCopy().setEffects(false);
            gameManager.getPlayersGameBoardCopy().setRaveMode(true);
        
        // after cycling cards, we now have the opponent's cards, copying for the animation when the cards come on screen
        } else {          
        
            for(int i = 0; i < gameManager.getPlayer().getPlayersHand().size(); i++)
                opponentsHandCopy.add(new Card(gameManager.getPlayer().getPlayersHand().get(i)));
        }
        
        // need to re-position all the cards
        positionCards();
    }
    
    /*
        When a player plays a card, their hand size decreases. Need to re-adjust the cards' positions
        so they don't slide offscreen or do other wonkey stuff.
    */
    private void positionCards() {
        
        int startX = 0;
        int offsetX = 0;
        int turnNumber = gameManager.getTurnNumber();
        
        for(int i = 2; i <= gameManager.getNumberOfPlayers() + turnNumber; i++)
            startX += 55;
        
        // also need to adjust the location of the cards transtitioning in
        int i = 0;
        for(Card c : gameManager.getPlayer().getPlayersHand()) {
            
            // adjusting the real cards
            c.setStartX(startX + offsetX);
            c.setStartY(520);
            
            // adjusting the copies, but moving them offscreen
            if(turnTransition && opponentsHandCopy != null) {
                
                opponentsHandCopy.get(i).setStartX(startX + offsetX + 1280);
                opponentsHandCopy.get(i).setStartY(520);
            }
            
            if(roundTransition && opponentsHandCopy != null) {
                
                opponentsHandCopy.get(i).setStartX(startX + offsetX);
                opponentsHandCopy.get(i).setStartY(720);
            }
            
            offsetX += 120;
            i++;
        } 
    }
    
    /*
        MakiBonusState -> PlayersHandState: need to obtain the player's new hand
        and upate some variables.
        GameBoardState -> PlayersHandState: need to re-size the player's board because
        its size and position were changed in the GameBoardState.
    */
    public void refreshPlayersHandState() {
        
        if(roundOver) {
            
            roundOver = false;
            roundTransition = true;
            
            playersScoreText.setText("Score: " + gameManager.getPlayer().getScore());
            roundNumberText.setText("Round " + Integer.toString(gameManager.getRoundNumber())); 
            
            // your new hand that animates upwards on screen
            for(int i = 0; i < gameManager.getPlayer().getPlayersHand().size(); i++)
                opponentsHandCopy.add(new Card(gameManager.getPlayer().getPlayersHand().get(i)));

            positionCards();
            gameManager.getPlayer().getPlayersGameBoard().formatBoard(150, 150, 1.0);
            gameManager.getPlayer().getPlayersGameBoard().setEffects(true);
            
            roundOverText.setVisible(false);
            
            roundSummaryButton.setVisible(false);
            roundSummaryButton.setCanActivate(false);
            boardButton.setVisible(true);
            boardButton.setCanActivate(true);
            quickGameSummaryButton.setVisible(true);
            quickGameSummaryButton.setCanActivate(true);
            quitButton.setVisible(true);
            quitButton.setCanActivate(true);
            
        } else {
            
            gameManager.getPlayer().getPlayersGameBoard().formatBoard(150, 150, 1.0);
            gameManager.getPlayer().getPlayersGameBoard().setEffects(true);
        }
    }
    
    @Override
    public void update() {
        
        boardButton.update();
        quickGameSummaryButton.update();
        quitButton.update();
        roundSummaryButton.update();
        
        // allows the copy of the game board to taste the rainbow
        if(roundOver || gameManager.getGameOver())
            gameManager.getPlayersGameBoardCopy().update();
        
        // moving cards to the left, check the last card to see if it's fully offscreen
        if(turnTransition) {
            
            if(playersHandCopy != null) {
                
                for(int i = 0; i < playersHandCopy.size(); i++) {
                    
                    playersHandCopy.get(i).moveLeft();
                
                    // stop turnTransition once the player's cards are offscreen and the new cards are on screen
                    if(i == playersHandCopy.size() - 1) {
                        
                        if(playersHandCopy.get(i).getX() + 100 <= 0) {
                            
                            playersHandCopy = null;
                            break;
                        }   
                    }
                }
                
                // because of the way this is set up, if chopsticks are played when there are only 2 cards left, the size of
                // playersHandCopy will be 0, but won't be null and can't be set to null, this fixes this
                if(playersHandCopy != null && playersHandCopy.isEmpty())
                    playersHandCopy = null;
            }
            
            if(opponentsHandCopy != null) {
                
                for(int i = 0; i < opponentsHandCopy.size(); i++) {
                    
                    opponentsHandCopy.get(i).moveLeft();
                    
                    // check to see when the incomming hand makes it to the leftmost position
                    if(i == 0) {
                        
                        if(opponentsHandCopy.get(i).getX() <= gameManager.getPlayer().getPlayersHand().get(i).getX()) {
                            
                            opponentsHandCopy = null;
                            break;
                        }
                    }
                }
            }
            
            // all the turn transition stuff has completed
            if(playersHandCopy == null && opponentsHandCopy == null)
                turnTransition = false;
        
        // new round started move the new hand upwards offscreen
        } else if(roundTransition) {
            
            if(opponentsHandCopy != null) {
               
                for(int i = 0; i < opponentsHandCopy.size(); i++) {
                
                    opponentsHandCopy.get(i).moveUp();
                
                    if(opponentsHandCopy.get(i).getY() <= gameManager.getPlayer().getPlayersHand().get(i).getY()) {
                    
                        roundTransition = false;
                        opponentsHandCopy = null;
                        break;
                    }
                }
            }
        
        // update the cards normally if the round is not over
        } else if(! roundOver) {
            
            for(Card c : gameManager.getPlayer().getPlayersHand())
                c.update();
        }
        
        // show what the player scored for a little while at the end of the round
        if(System.currentTimeMillis() >= startTime + 1500)
            changeInScoreText.setVisible(false);
    }

    @Override
    public void render(Graphics2D g2) {
        
        g2.drawImage(AssetLoader.tableBackground, 0, 0, 1280, 720, null);
        
        boardButton.render(g2);
        quickGameSummaryButton.render(g2);
        quitButton.render(g2);
        roundSummaryButton.render(g2);
        
        playersScoreText.render(g2);   
        roundNumberText.render(g2);
        roundOverText.render(g2);
        
        // only show the sushi go! text when player has played chopsticks
        if(playedChopsticks[1])
            sushiGoFlagText.render(g2);
        
        // keep what's on the gameboard until the player finishes viewing the MakiBonusState
        if(roundOver)
            gameManager.getPlayersGameBoardCopy().render(g2);   
        
        else
            gameManager.getPlayer().getPlayersGameBoard().render(g2); 
        
        // render the incomming and outgoing cards if transitioning turns
        if(turnTransition) {
            
            if(playersHandCopy != null)
                for(Card c : playersHandCopy)
                    c.render(g2);
            
            if(opponentsHandCopy != null)
                for(Card c : opponentsHandCopy)
                    c.render(g2);
            
        } else if(roundTransition) {
            
            if(opponentsHandCopy != null)
                for(Card c : opponentsHandCopy)
                    c.render(g2);
            
        // render all the other cards normally if the round is not over
        } else if(! roundOver) {
            
            for(Card c : gameManager.getPlayer().getPlayersHand())
                c.render(g2);
        }
        
        // if a player is currently dragging a card, render it such that it "overlaps" the other cards
        for(Card c : gameManager.getPlayer().getPlayersHand())
            if(dragId != null && Integer.toString(c.hashCode()).equals(dragId))
                c.render(g2);
        
        changeInScoreText.render(g2);
    }

    @Override
    public void handleMouseClicked(Point p) {
        
        // don't accept user input during transitions
        if(turnTransition)
            return;
        
        // player selected the chopsticks on the board and can play 2 cards this turn
        // add the chopsticks back into the player's hand; prevent using 2 chopsticks at same time
        if(gameManager.getPlayer().getPlayersGameBoard().hasChopsticks(p)
            && gameManager.getPlayer().getPlayersHand().size() >= 2 
            && ! playedChopsticks[0] && ! playedChopsticks[1]) {
            
                chopsticks = gameManager.getPlayer().getPlayersGameBoard().playChopsticks();
                playedChopsticks[0] = true;
                playedChopsticks[1] = true;
        }
        
        // player wants to go to either the game board, quick game summary, or just quit
        if(boardButton.contains(p)) {
            
            boardButton.reset();
            quickGameSummaryButton.reset();
            quitButton.reset();
            
            gameStateManager.refreshGameBoardState();
            gameStateManager.setCurrentState(GameStateManager.GAME_BOARD_STATE);
        }
        
        if(quickGameSummaryButton.contains(p)) {
            
            boardButton.reset();
            quickGameSummaryButton.reset();
            quitButton.reset();
            
            gameStateManager.setPreviousState(GameStateManager.PLAYERS_HAND_STATE);
            gameStateManager.setCurrentState(GameStateManager.QUICK_GAME_SUMMARY_STATE);
        }
        
        if(quitButton.contains(p))
            System.exit(0);
        
        if(roundSummaryButton.contains(p)) {
            
            roundSummaryButton.reset();
            gameStateManager.refreshMakiBonusState();
            gameStateManager.setCurrentState(GameStateManager.MAKI_BONUS_STATE);
        }
    }
    
    // determine whether the player actually played a card or not
    @Override
    public void handleMouseReleased(Point p) {
        
        if(turnTransition)
            return;
        
        // card is no longer being dragged, so it's not overlapping other cards anymore
        dragId = null;
        
        // player played a card in a card slot, check to see if it's a valid choice
        outer:
        for(int i = 0; i < gameManager.getPlayer().getPlayersHand().size(); i++) {
            
            // first check to make sure the card is actually within the board to begin with
            if(gameManager.getPlayer().getPlayersGameBoard().intersects(p, gameManager.getPlayer().getPlayersHand().get(i))) {
                
                // loop through all the card slots within the game board
                for(int j = 0; j < gameManager.getPlayer().getPlayersGameBoard().getSize(); j++) {
                    
                    // now check to see if the card is within a card slot
                    if(gameManager.getPlayer().getPlayersGameBoard().intersectsCardSlot(p, gameManager.getPlayer().getPlayersHand().get(i), j)) {
                        
                        // make sure this is a valid card slot
                        if(gameManager.getPlayer().getPlayersGameBoard().validCardSlot(gameManager.getPlayer().getPlayersHand().get(i), j)) {
                            
                            gameManager.getPlayer().getPlayersGameBoard().playCard(gameManager.getPlayer().getPlayersHand().remove(i), j);
                            
                            // prevents player from playing multiple chopsticks per turn
                            if(! playedChopsticks[0])
                                playedChopsticks[1] = false;
                            
                            // if the played played chopsticks, they can play 2 cards instead of 1, if not, normally pass hand
                            if(! playedChopsticks[0] && ! playedChopsticks[1])
                                cyclePlayersCards();
                            
                            playedChopsticks[0] = false;
                            break outer;
                        }
                    }
                }
            }
        }
        
        // player didn't play a card, return that card's position back to their hand
        for(Card c : gameManager.getPlayer().getPlayersHand())
            c.reset();
        
        gameManager.getPlayer().getPlayersGameBoard().reset();
        gameManager.getPlayer().getPlayersGameBoard().setColor(Color.WHITE);
    }

    @Override
    public void handleMouseMoved(Point p) {
        
        if(turnTransition)
            return;
        
        boardButton.contains(p);
        quickGameSummaryButton.contains(p);
        quitButton.contains(p);
        roundSummaryButton.contains(p);
        
        // if chopsticks are on the board, highlight them to signal that they can be played
        gameManager.getPlayer().getPlayersGameBoard().hasChopsticks(p); 
        
        // for hovering over/selecting a card
        for(Card c : gameManager.getPlayer().getPlayersHand())
            c.contains(p);         
    }

    @Override
    public void handleMouseDragged(Point p) {

        // roundOver is here because of a fun error
        if(turnTransition || roundOver)
            return;
        
        for(Card c : gameManager.getPlayer().getPlayersHand()) {
            
            if(c.contains(p)) {
                
                // prevents dragging multiple cards at a time
                if(dragId == null)
                    dragId = Integer.toString(c.hashCode());
                
                // prevents dragging multiple cards of the same type at the same time
                if(! Integer.toString(c.hashCode()).equals(dragId)) {
                    
                    c.reset();
                    continue;
                }
                
                // if moving the mouse too fast, the card will reset, this prevents losing the card by accident
                c.setDragged(true);

                // set the card's position to where the mouse is located
                c.setX((int) (p.getX() - (c.getWidth() / 2)));
                c.setY((int) (p.getY() - (c.getHeight() / 2)));  
               
                // simply highlights whether or not the card can be played in a slot
                for(int i = 0; i < gameManager.getPlayer().getPlayersGameBoard().getSize(); i++)
                    if(gameManager.getPlayer().getPlayersGameBoard().intersectsCardSlot(p, c, i))
                        gameManager.getPlayer().getPlayersGameBoard().validCardSlot(c, i);
                
                break;
                
            // reset the card's position and the board
            } else {
                
                c.reset();
                gameManager.getPlayer().getPlayersGameBoard().reset();
                gameManager.getPlayer().getPlayersGameBoard().setColor(Color.WHITE);
            }
        } 
    }
}