package gamemanager;

import cards.Card;
import cards.Deck;
import guielements.GameBoard;
import entities.*;
import misc.RandomNames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Random;

public class GameManager {
    
    private int numberOfPlayers, difficulty;
    private Deck deck;
    private Player player;
    private ArrayList<AI> AIs;
    private int turnNumber, roundNumber;
    private boolean roundOver, gameOver;
    private int[] makiBonuses, puddingBonuses;
    private GameBoard playersGameBoardCopy;
    private ArrayList<GameBoard> AIsGameBoardCopies;
      
    public GameManager() {
        
        this.numberOfPlayers = 4;
        this.difficulty = 2;
        this.turnNumber = 0;
        this.roundNumber = 1;
    }
   
    /*
        Starts up the game when the player selects the start game button in the main menu.
    */
    public void init() {
        
        deck = new Deck();
        
        // create the player, the AIs, and deal a hand to each one
        player = new Player();
        player.setPlayersHand(deck.dealHand(numberOfPlayers));
        
        AIs = new ArrayList<>();
        
        // numberOfPlayers includes the player, prevent creating an additional AI
        for(int i = 0; i < numberOfPlayers - 1; i++) {
            
            AIs.add(new AI(new RandomNames().getRandomName(), difficulty));
            AIs.get(i).setAIsHand(deck.dealHand(numberOfPlayers));
        }
        
        // tracking all the player's maki bonuses and pudding bonuses
        makiBonuses = new int[numberOfPlayers];
        Arrays.fill(makiBonuses, 0);
        puddingBonuses = new int[numberOfPlayers];
        Arrays.fill(puddingBonuses, 0);
        
        // used to display everyone's boards before starting a new round
        playersGameBoardCopy = new GameBoard(player.getPlayersGameBoard());
        
        AIsGameBoardCopies = new ArrayList<>();
        for(int i = 0; i < AIs.size(); i++)
            AIsGameBoardCopies.add(new GameBoard(AIs.get(i).getAIsGameBoard()));
    }
    
    public int getNumberOfPlayers() { return numberOfPlayers; }
    public int getDifficulty() { return difficulty; }
    
    public Player getPlayer() { return player; }
    public ArrayList<AI> getAIs() { return AIs; }
    public AI getAI(int index) { return AIs.get(index); }
    
    public ArrayList<String> getAIsNames() {
    
        ArrayList<String> AIsNames = new ArrayList<>();
        for(AI ai : AIs)
            AIsNames.add(ai.getName());
        
        return AIsNames;
    }
    
    public int[] getMakiBonuses() { return makiBonuses; }
    public int[] getPuddingBonuses() { return puddingBonuses; }
    
    public int getTurnNumber() { return turnNumber; }
    public int getRoundNumber() { return roundNumber; }
    
    public boolean getRoundOver() { return roundOver; }
    public boolean getGameOver() { return gameOver; }
    
    public GameBoard getPlayersGameBoardCopy() { return playersGameBoardCopy; }
    public ArrayList<GameBoard> getAIsGameBoardCopies() { return AIsGameBoardCopies; }
    
    public void setNumberOfPlayers(int numberOfPlayers) { this.numberOfPlayers = numberOfPlayers; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    public void setAIsNames(ArrayList<String> AIsNames) {
    
        for(int i = 0; i < AIs.size(); i++)
            AIs.get(i).setName(AIsNames.get(i));
    }    
    
    public void setRoundOver(boolean roundOver) { this.roundOver = roundOver; }
    
    /*
        Called after the player has played a card from PlayersHandState. 
    */
    public void continueTurn() {
        
        AIPlayCard();
        calculateScores();
        passHand();
        checkGameOver();
        checkRoundOver();
    }
    
    /*
        TODO: Make it better
        The AI plays a card depending on the current difficulty level.  (kinda)  
    */
    private void AIPlayCard() {
        int cardToPlay = 0;
        int heuristicValue = 0;
        HashMap<Integer, Integer> cardValues = new HashMap<>();

        for(AI ai : AIs){
            // making a safe copy to prevent weird issues
            ArrayList<Card> hand = new ArrayList<>();
            for(int i = 0; i < ai.getAIsHand().size(); i++)
                hand.add(new Card(ai.getAIsHand().get(i)));
            if(difficulty != 1){
                for(Card card : hand){
                    if(difficulty == 3) { //if you are palying on hard, use additional heuristics 
                        cardValues.put(hand.indexOf(card), calculateAITestScore(ai, card) + aiHeuristics(ai, card));
                    }
                    else if(difficulty == 2) { //no additional heuristics
                        cardValues.put(hand.indexOf(card), calculateAITestScore(ai, card));
                    }
                }
                cardToPlay = bestCard(cardValues);
            }else{
                int handSize = ai.getAIsHand().size();
                cardToPlay = new Random().nextInt(handSize);
            }
            ai.playCard(cardToPlay);
        }
        
        // quick fix to a big problem of cards appearing on screen where they are not supposed to be
        int x = player.getPlayersGameBoard().getX();
        int y = player.getPlayersGameBoard().getY();
        double scale = player.getPlayersGameBoard().getScale();
        
        player.getPlayersGameBoard().formatBoard(x, y, scale);
        
        for(AI ai : AIs) {
            
            x = ai.getAIsGameBoard().getX();
            y = ai.getAIsGameBoard().getY();
            scale = ai.getAIsGameBoard().getScale();
            ai.getAIsGameBoard().formatBoard(x, y, scale);
        }
    }
    
    /*
    Heuristics in an attempt to make the AI more difficult
    */
    private int aiHeuristics(AI ai, Card card){
        int value = 0;
        String cardName = card.getName();
        // making a safe copy to prevent issues
        GameBoard board = new GameBoard(ai.getAIsGameBoard());
        // getBoardCards() now returns a copy of everything on the board to prevent issues
        ArrayList<Card> tmp = ai.getAIsGameBoard().getBoardCards();
        ArrayList<String> cardsOnBoard = new ArrayList<>();
        tmp.forEach(c -> cardsOnBoard.add(c.getName()));
        
        // safe copy again
        ArrayList<Card> hand = new ArrayList<>();
        for(int i = 0; i < ai.getAIsHand().size(); i++)
            hand.add(new Card(ai.getAIsHand().get(i)));
        
        if(cardName.equals("SquidNigiri") || cardName.equals("SalmonNigiri") || cardName.equals("EggNigiri")) {
            if(board.hasUsableWasabi()){
                switch(cardName) {
                    case "SquidNigiri":
                        value = 6;
                        break;
                    case "SalmonNigiri":
                        value = 4;
                        break;
                    case "EggNigiri":
                        value = 2;
                        break;
                }                       
            }
        } 
        else if(cardName.equals("Wasabi") && hand.size() > 3) {
            value = 4;
        }
        else if(cardName.equals("Pudding") && hand.size() < 3) {
           value = 5;
        }
        else if(cardName.equals("Tempura") && hand.size() > 3) {
           value = 1;              
        }
        else if(cardName.equals("Sashimi") && hand.size() > 5) {
           value = 1;
           int sashimiCount = 0;
           
           for(String c : cardsOnBoard) {
                if(c.equals("Sashimi"))
                    sashimiCount++;
           }
           
           if(sashimiCount%3 == 1)
                value +=4;
           
           if(hand.size() > 5)
               value++;
        }
        else if(cardName.equals("Dumpling") && hand.size() > 3) {
            value += 2;
        }
        else if(cardName.equals("SingleMakiRoll") || cardName.equals("DoubleMakiRoll") ||cardName.equals("TripleMakiRoll")) {
            value += 3;
            for(String name : cardsOnBoard) {
                if(name.equals("SingleMakiRoll") || name.equals("DoubleMakiRoll") ||name.equals("TripleMakiRoll"))
                    switch(name) {
                        case "SingleMakiRoll":
                            value += 1;
                            break;
                        case "DoubleMakiRoll":
                            value += 2;
                            break;
                        case "TripleMakiRoll":
                            value += 3;
                            break;
                    }
           }
        }
        else if(cardName.equals("Chopsticks")) {//we do NOT want the AI using chopsticks
            value--;
        }
        
        return value;
    }
    
    public int bestCard(HashMap<Integer, Integer> hand){
        //sort the hand based on what is best for the ai based on the Integer value
        //find that cards name in the AIs hand and return that index
        int max = 0;
        int maxIndex = 0;
        for(Map.Entry<Integer, Integer> entry : hand.entrySet()) {
            
            int indexOf = entry.getKey();
            int value = entry.getValue();
            
            if(value > max) {
                maxIndex = indexOf;
            }
        }
        
        return maxIndex;
    }
    
    private int calculateAITestScore(AI ai, Card card) {
        return ai.getAIsGameBoard().calculateAITestScore(card);
    }
    
    /*
        Calculates and sets the scores for each player. After each turn, tempura, sashimi, dumplings, and 
        nigiris are scored. After each round, makis are scored. Once the game is over with, puddings
        are scored. Note: This method will NOT score the makis or puddings, that's done elsewhere.
    */
    private void calculateScores() {
    
        // calculating the score for each player (scores tempura, sashimi, dumplings, and nigiris)
        player.setScore(player.getPlayersGameBoard().calculateScore());
        
        for(int i = 0; i < AIs.size(); i++)
            AIs.get(i).setScore(AIs.get(i).getAIsGameBoard().calculateScore()); 
    }
    
    private void passHand() {
        
        // adding the player and all the AI's hands to a single array list in order to perform a cycle (this is more complicated than it needs to be)
        ArrayList<ArrayList<Card>> allHands = new ArrayList<>();
        
        allHands.add(player.getPlayersHand());
        
        for(int i = 0; i < AIs.size(); i++)
            allHands.add(AIs.get(i).getAIsHand());
        
        // this part does the cycle
        ArrayList<Card> temp = allHands.get(0);
        
        for(int i = 0; i < allHands.size() - 1; i++)
            allHands.set(i, allHands.get(i + 1));
        
        allHands.set(allHands.size() - 1, temp);
        
        // now extracting the newly swapped hands and giving them to the players
        player.setPlayersHand(allHands.get(0));
        
        for(int i = 1; i < allHands.size(); i++)
            AIs.get(i - 1).setAIsHand(allHands.get(i));
        
        turnNumber++;
    }
    
    private void checkGameOver() {
        
        // the game is not over, don't execute any code
        if(! player.getPlayersHand().isEmpty() || roundNumber != 3)
            return;
        
        System.out.println("Before maki bonus: " + Arrays.toString(getNamesAndScores()));
        
        // the game is over, gotta do a bunch of stuff and end the game
        gameOver = true;
        
        // some player's might have gotten a maki bonus in a previous round, clear that
        Arrays.fill(makiBonuses, 0);
        
        // score the makis and puddings
        scoreMakis();         
        scorePuddings();
        
        // before clearing everyone's boards, copy them to display how each player did for MakiBonusState/PuddingBonusState
        copyGameBoards();
        
        // re-calculate the scores to account for the maki and pudding bonuses
        calculateScores();
        
        System.out.println("After bonuses: " + Arrays.toString(getNamesAndScores()));
    }
    
    private void checkRoundOver() {
        
        // the round is not over or the game is over, don't execute any code
        if(! player.getPlayersHand().isEmpty() || gameOver)
            return;
           
        // round is over, gotta do a bunch of stuff and start a new round
        roundOver = true;
            
        roundNumber++;
        turnNumber = 0;
          
        // resetting the maki bonuses that players might have gotten from previous rounds
        Arrays.fill(makiBonuses, 0);
        
        // once the round is over, score the makis and add it the cumulative score within the board
        scoreMakis();
        
        // before clearing everyone's boards, copy them to display how each player did for MakiBonusState/PuddingBonusState
        copyGameBoards();
        
        // wipe everyone's boards except for puddings
        player.getPlayersGameBoard().clear();
        
        for(AI ai : AIs)
            ai.getAIsGameBoard().clear();
        
        // need to re-calculate the scores to take into account maki bonuses
        calculateScores();
        
        // give each player a new hand
        player.setPlayersHand(deck.dealHand(numberOfPlayers));
        
        for(int i = 0; i < numberOfPlayers - 1; i++)
            AIs.get(i).setAIsHand(deck.dealHand(numberOfPlayers));
    }
    
    /*
        Called by the player's hand state when the round is over, this scores makis.
        Person with most makis scores 6 points, second place gets 3 points. If there is
        a tie for first, split evenly amongst players with no remainder and reward no points
        to second place. If ties for second, split evenly amongst players with no remainder.
    */
    private void scoreMakis() {
        
        // mapping player names to the number of makis they have
        HashMap<String, Integer> makiAmounts = new HashMap<>();
        
        makiAmounts.put(player.getName(), player.getPlayersGameBoard().getMakiAmount());
        
        for(int i = 0; i < AIs.size(); i++)
            makiAmounts.put(AIs.get(i).getName(), AIs.get(i).getAIsGameBoard().getMakiAmount());
        
        // finding the player with the most amount of maki rolls
        int mostMakis = 0;
        String mostMakisPlayer = null;
        
        for(Map.Entry<String, Integer> entry : makiAmounts.entrySet()) {
            
            String playerName = entry.getKey();
            int makiAmount = entry.getValue();
            
            // keep track of the player with the most makis
            if(makiAmount >= mostMakis) {
                
                mostMakis = makiAmount;
                mostMakisPlayer = playerName;
            }
        }
        
        // now creating a new hashmap that's a subset of the first one and placing in possible ties with first
        HashMap<String, Integer> tiesForFirst = new HashMap<>();
        
        tiesForFirst.put(mostMakisPlayer, mostMakis);
        
        // if there are ties for first, don't reward points to players in second place
        boolean tiedForFirst = false;
        
        for(Map.Entry<String, Integer> entry : makiAmounts.entrySet()) {
            
            String playerName = entry.getKey();
            int makiAmount = entry.getValue();
            
            // check if another player tied for first and put them into the subset hashmap,
            // skip over the player that previously had the most to avoid duplicates
            if(makiAmount == mostMakis && ! playerName.equals(mostMakisPlayer)) {
                
                tiesForFirst.put(playerName, makiAmount);
                tiedForFirst = true;
            }
        }
        
        // add 6 points to the player's score if they got first place
        if(tiesForFirst.size() == 1) {
            
            if(mostMakisPlayer.equals(player.getName())) {
                
                player.getPlayersGameBoard().setCumulativeScore(player.getPlayersGameBoard().getCumulativeScore() + 6);
                makiBonuses[0] = 6;
            }
            
            for(int i = 0; i < AIs.size(); i++) {
                
                if(mostMakisPlayer.equals(AIs.get(i).getName())) {
                    
                    AIs.get(i).getAIsGameBoard().setCumulativeScore(AIs.get(i).getAIsGameBoard().getCumulativeScore() + 6);
                    makiBonuses[i + 1] = 6;
                }
            }
            
        } else {
            
            if(tiesForFirst.containsKey(player.getName())) {
                
                player.getPlayersGameBoard().setCumulativeScore(player.getPlayersGameBoard().getCumulativeScore() + (6 / tiesForFirst.size()));
                makiBonuses[0] = 6 / tiesForFirst.size();
            }
            
            for(int i = 0; i < AIs.size(); i++) {
                
                if(tiesForFirst.containsKey(AIs.get(i).getName())) {
                    
                    AIs.get(i).getAIsGameBoard().setCumulativeScore(AIs.get(i).getAIsGameBoard().getCumulativeScore() + (6 / tiesForFirst.size()));
                    makiBonuses[i + 1] = 6 / tiesForFirst.size();
                }
            }
        }
        
        // after that whole mess, have to repeat it for second place if there were not any ties for first
        if(! tiedForFirst) {
            
            int secondMostMakis = 0;
            String secondMostMakisPlayer = null;
        
            for(Map.Entry<String, Integer> entry : makiAmounts.entrySet()) {
            
                String playerName = entry.getKey();
                int makiAmount = entry.getValue();
            
                // keep track of the player with the second most makis, skip over player(s) that got first place
                if(makiAmount >= secondMostMakis && makiAmount != mostMakis) {
                
                    secondMostMakis = makiAmount;
                    secondMostMakisPlayer = playerName;
                }
            }
            
            // now placing in possible ties for second
            HashMap<String, Integer> tiesForSecond = new HashMap<>();
        
            tiesForSecond.put(secondMostMakisPlayer, secondMostMakis);
            
            for(Map.Entry<String, Integer> entry : makiAmounts.entrySet()) {
            
                String playerName = entry.getKey();
                int makiAmount = entry.getValue();
            
                // check if another player tied for second and put them into the subset hashmap,
                // skip over the player that previously had the second most to avoid duplicates
                if(makiAmount == secondMostMakis && ! playerName.equals(secondMostMakisPlayer))
                    tiesForSecond.put(playerName, makiAmount);
            }
            
            // add the scores to the player(s) that got second for makis
            // if there was only 1 player that got second, give them 3 points, else split them evenly
            if(tiesForSecond.size() == 1) {
            
                if(secondMostMakisPlayer.equals(player.getName())) {
                    
                    player.getPlayersGameBoard().setCumulativeScore(player.getPlayersGameBoard().getCumulativeScore() + 3);
                    makiBonuses[0] = 3;
                }
            
                for(int i = 0; i < AIs.size(); i++) {
                    
                    if(secondMostMakisPlayer.equals(AIs.get(i).getName())) {
                        
                        AIs.get(i).getAIsGameBoard().setCumulativeScore(AIs.get(i).getAIsGameBoard().getCumulativeScore() + 3);
                        makiBonuses[i + 1] = 3;
                    }
                }

            } else {
            
                if(tiesForSecond.containsKey(player.getName())) {
                    
                    player.getPlayersGameBoard().setCumulativeScore(player.getPlayersGameBoard().getCumulativeScore() + (3 / tiesForSecond.size()));
                    makiBonuses[0] = 3 / tiesForSecond.size();
                }
                    
                for(int i = 0; i < AIs.size(); i++) {
                    
                    if(tiesForSecond.containsKey(AIs.get(i).getName())) {
                        
                        AIs.get(i).getAIsGameBoard().setCumulativeScore(AIs.get(i).getAIsGameBoard().getCumulativeScore() + (3 / tiesForSecond.size()));     
                        makiBonuses[i + 1] = 3 / tiesForSecond.size();
                    }
                }
            }
        }
        
        System.out.println("Maki bonuses: " + Arrays.toString(makiBonuses));
    }
    
    /*
        Called by the player's hand state when the game is over with. Scores the number of
        puddings. Person with the most gets 6 points, person with the least gets -6 points.
        Ties for first or last split the points gained and lost evenly, ignoring the remainder.
    */
    public void scorePuddings() {
        
        HashMap<String, Integer> puddingAmounts = new HashMap<>();
        
        puddingAmounts.put(player.getName(), player.getPlayersGameBoard().getPuddingAmount());
        
        for(int i = 0; i < AIs.size(); i++)
            puddingAmounts.put(AIs.get(i).getName(), AIs.get(i).getAIsGameBoard().getPuddingAmount());
        
        int mostPuddings = 0;
        String mostPuddingsPlayer = null;
        
        for(Map.Entry<String, Integer> entry : puddingAmounts.entrySet()) {
            
            String playerName = entry.getKey();
            int puddingAmount = entry.getValue();
            
            if(puddingAmount >= mostPuddings) {
                
                mostPuddings = puddingAmount;
                mostPuddingsPlayer = playerName;
            }
        }
        
        // giving 6 points to the player who got first, ties for first split the points
        HashMap<String, Integer> tiesForFirst = new HashMap<>();
        
        tiesForFirst.put(mostPuddingsPlayer, mostPuddings);
                
        for(Map.Entry<String, Integer> entry : puddingAmounts.entrySet()) {
            
            String playerName = entry.getKey();
            int puddingAmount = entry.getValue();
            
            if(puddingAmount == mostPuddings && ! playerName.equals(mostPuddingsPlayer)) {
                
                tiesForFirst.put(playerName, puddingAmount);
            }
        }
        
        if(tiesForFirst.size() == 1) {
            
            if(mostPuddingsPlayer.equals(player.getName())) {
                
                player.getPlayersGameBoard().setCumulativeScore(player.getPlayersGameBoard().getCumulativeScore() + 6);
                puddingBonuses[0] = 6;
            }
            
            for(int i = 0; i < AIs.size(); i++) {
                
                if(mostPuddingsPlayer.equals(AIs.get(i).getName())) {
                    
                    AIs.get(i).getAIsGameBoard().setCumulativeScore(AIs.get(i).getAIsGameBoard().getCumulativeScore() + 6);
                    puddingBonuses[i + 1] = 6;
                }
            }
            
        } else {
            
            if(tiesForFirst.containsKey(player.getName())) {
                
                player.getPlayersGameBoard().setCumulativeScore(player.getPlayersGameBoard().getCumulativeScore() + (6 / tiesForFirst.size()));
                puddingBonuses[0] = 6 / tiesForFirst.size();
            }
            
            for(int i = 0; i < AIs.size(); i++) {
                
                if(tiesForFirst.containsKey(AIs.get(i).getName())) {
                    
                    AIs.get(i).getAIsGameBoard().setCumulativeScore(AIs.get(i).getAIsGameBoard().getCumulativeScore() + (6 / tiesForFirst.size()));
                    puddingBonuses[i + 1] = 6 / tiesForFirst.size();
                }
            }
        }
        
        // giving -6 points to the person who got last, ties for last split the loss of points evenly
        int leastPuddings = mostPuddings;
        String leastPuddingsPlayer = null;
        
        for(Map.Entry<String, Integer> entry : puddingAmounts.entrySet()) {
            
            String playerName = entry.getKey();
            int puddingAmount = entry.getValue();
            
            if(puddingAmount < leastPuddings) {
                
                leastPuddings = puddingAmount;
                leastPuddingsPlayer = playerName;
            }
        }
        
        HashMap<String, Integer> tiesForLast = new HashMap<>();
        
        tiesForLast.put(leastPuddingsPlayer, leastPuddings);
                
        for(Map.Entry<String, Integer> entry : puddingAmounts.entrySet()) {
            
            String playerName = entry.getKey();
            int puddingAmount = entry.getValue();
            
            if(puddingAmount == leastPuddings && ! playerName.equals(leastPuddingsPlayer)) {
                
                tiesForLast.put(playerName, puddingAmount);
            }
        }
        
        // give -6 points to the player with the least puddings
        if(tiesForLast.size() == 1) {
            
            if(leastPuddingsPlayer.equals(player.getName())) {
                
                player.getPlayersGameBoard().setCumulativeScore(player.getPlayersGameBoard().getCumulativeScore() - 6);
                puddingBonuses[0] = -6;
            }
            
            for(int i = 0; i < AIs.size(); i++) {
                
                if(leastPuddingsPlayer.equals(AIs.get(i).getName())) {
                    
                    AIs.get(i).getAIsGameBoard().setCumulativeScore(AIs.get(i).getAIsGameBoard().getCumulativeScore() - 6);
                    puddingBonuses[i + 1] = -6;
                }
            }
        
        // split the loss of points amongst the losers
        } else {
            
            if(tiesForLast.containsKey(player.getName())) {
                
                player.getPlayersGameBoard().setCumulativeScore(player.getPlayersGameBoard().getCumulativeScore() - (6 / tiesForLast.size()));
                puddingBonuses[0] = -(6 / tiesForLast.size());
            }
            
            for(int i = 0; i < AIs.size(); i++) {
                
                if(tiesForLast.containsKey(AIs.get(i).getName())) {
                    
                    AIs.get(i).getAIsGameBoard().setCumulativeScore(AIs.get(i).getAIsGameBoard().getCumulativeScore() - (6 / tiesForLast.size()));     
                    puddingBonuses[i + 1] = -(6 / tiesForLast.size());
                }
            }
        }
        
        System.out.println("Pudding bonuses: " + Arrays.toString(puddingBonuses));
    }
    
    private void copyGameBoards() {
        
        playersGameBoardCopy = new GameBoard(player.getPlayersGameBoard());
        
        for(int i = 0; i < AIsGameBoardCopies.size(); i++)
            AIsGameBoardCopies.set(i, new GameBoard(AIs.get(i).getAIsGameBoard()));
    }
    
    public String[] getNamesAndScores() {
        
        ArrayList<String> names = new ArrayList<>(); 
        ArrayList<Integer> scores = new ArrayList<>();
        
        names.add(player.getName());
        scores.add(player.getScore());
        
        for(AI ai : AIs) {
            
            names.add(ai.getName());
            scores.add(ai.getScore());    
        }
        
        String[] namesAndScores = new String[names.size()];
        
        for(int i = 0; i < namesAndScores.length; i++)
            namesAndScores[i] = names.get(i) + ": " + scores.get(i);
        
        return namesAndScores;
    }
    
    /*
        Returns how the player's were placed, their names, and their scores in a string format
        to later be displayed in the ScoreScreenState.
    */
    public String[] getPlacesNamesAndScores() {
        
        ArrayList<Integer> scores = new ArrayList<>();
        
        scores.add(player.getScore());
        
        for(AI ai : AIs)
            scores.add(ai.getScore());
        
        Collections.sort(scores);
        Collections.reverse(scores);
        
        ArrayList<String> names = new ArrayList<>();        
        
        for(Integer score : scores) {
            
            if(score == player.getScore() && ! names.contains(player.getName()))
                names.add("You");
            
            for(AI ai : AIs) 
                if(score == ai.getScore() && ! names.contains(ai.getName()))
                    names.add(ai.getName());          
        }
        
        String[] namesAndScores = new String[names.size()];
        int placement = 1;
        String placementString = "1st ";
        
        for(int i = 0; i < names.size(); i++) {
            
            // checking for ties amongst players (e.g. 1st John, 2nd Blub, 2nd Bungus, 3rd Ralph)
            if(i != 0)
                if(! scores.get(i - 1).equals(scores.get(i)))
                    placement++;
            
            switch(placement) {
                case 1:
                    placementString = "1st ";
                    break;
                case 2:
                    placementString = "2nd ";
                    break;
                case 3:
                    placementString = "3rd ";
                    break;
                case 4:
                    placementString = "4th ";
                    break;
                case 5:
                    placementString = "5th ";
                    break;
            }
            
            namesAndScores[i] = placementString + names.get(i) + ": " + scores.get(i);
        }
        
        return namesAndScores;
    }
    
    /*
        Returns the name of the player that won. If there is a tie, returns all names
        that tied for first. TODO: Maybe get rid of the second part and just
        say that there was a tie.
    */
    public String getWinner() {
        
        HashMap<String, Integer> namesAndScores = new HashMap<>();
       
        namesAndScores.put(player.getName(), player.getScore());
        
        for(AI ai : AIs)
            namesAndScores.put(ai.getName(), ai.getScore());
        
        HashMap<String, Integer> tiesForFirst = new HashMap<>();
        String winningPlayer = null;
        int winningScore = 0;
        
        for(Map.Entry<String, Integer> entry : namesAndScores.entrySet()) {
            
            String player = entry.getKey();
            int score = entry.getValue();
            
            if(score >= winningScore) {
                
                winningPlayer = player;
                winningScore = score;
            }
        }
        
        // checking for and adding duplicate winners
        tiesForFirst.put(winningPlayer, winningScore);
                
        for(Map.Entry<String, Integer> entry : namesAndScores.entrySet()) {
            
            String playerName = entry.getKey();
            int score = entry.getValue();
            
            if(score == winningScore && ! playerName.equals(winningPlayer))
                tiesForFirst.put(playerName, score);
        }
        
        // only a single person won, return their name
        if(tiesForFirst.size() == 1) {
            
            if(winningPlayer.equals(player.getName()))
                return "You Won!";
            else
                return winningPlayer + " Won!";
        
        // either 2 or more people tied for first
        } else {
            
            ArrayList<String> winners = new ArrayList<>();
            
            for(String name : tiesForFirst.keySet())
                winners.add(name);
            
            switch(winners.size()) {
                case 2:
                    return winners.get(0) + " and " + winners.get(1) + " tied!";
                case 3:
                    return winners.get(0) + ", " + winners.get(1) + ",and " + winners.get(2) + " tied!";
                case 4:
                    return winners.get(0) + ", " + winners.get(1) + ", " + winners.get(2) + ",and " + winners.get(3) + " tied!";
                case 5:
                    return winners.get(0) + ", " + winners.get(1) + ", " + winners.get(2) + ", " + winners.get(3) + ",and " + winners.get(4) + " tied!";
            }
        }
        
        return null;
    }
    
    /************************************************
     * For now I am working on a different approach to the one below, using the new State class and ChoiceAlgorithm
     * which can be found under misc.
     * aiPlayCard will need to be updated if this new method works out
     * Keeping this code as a fall back plan for now
    *************************************************/
    
//    public HashMap<String, Integer> cardValueDifficulty(Card card){
//        HashMap<String, Integer> tmp = new HashMap<>();
//        
//        switch(getDifficulty()){
//            case 1:
//                //tmp = getAIEasy(card);//for when a better AI is implemented
//                break;
//            case 2:
//                tmp = getAIMedium(card);
//                break;
//            case 3:
//                tmp = getAIHard(card);
//                break;
//            default:
//                tmp = getAIHard(card);
//                break;
//        }
//        
//        return tmp;
//    }
//    
//    public HashMap<String, Integer> getAIEasy(Card card){//Implement with better AI, currently picks random card on easy
//        HashMap<String, Integer> tmp = new HashMap<>();
//        return tmp;
//    }
//    
//    public HashMap<String, Integer> getAIMedium(Card card){
//        HashMap<String, Integer> tmp = new HashMap<>();
//        
//        String cardName = card.getName();
//        switch(cardName) {
//            
//            case "Tempura":
//                
//                break;              
//            case "Sashimi":
//                
//                break;              
//            case "Dumpling":
//                
//                break;          
//            case "SingleMakiRoll":
//                
//                break;         
//            case "DoubleMakiRoll":
//                
//                break;                
//            case "TripleMakiRoll":
//                
//                break;
//            case "SalmonNigiri":
//                
//                break;                
//            case "SquidNigiri":
//                
//                break;               
//            case "EggNigiri":
//                
//                break;               
//            case "Pudding":
//                tmp.put("Pudding", 0);
//                break;               
//            case "Wasabi":
//                tmp.put("Wasabi", 0);
//                break;               
//            case "Chopsticks":
//                tmp.put("Chopsticks", 0);
//                break;
//        }
//        
//        return tmp;
//    }
//    
//    public HashMap<String, Integer> getAIHard(Card card){
//        HashMap<String, Integer> tmp = new HashMap<>();
//        
//        String cardName = card.getName();
//        switch(cardName) {
//            
//            case "Tempura":
//                
//                break;              
//            case "Sashimi":
//                
//                break;              
//            case "Dumpling":
//                
//                break;          
//            case "SingleMakiRoll":
//                
//                break;         
//            case "DoubleMakiRoll":
//                
//                break;                
//            case "TripleMakiRoll":
//                
//                break;
//            case "SalmonNigiri":
//                
//                break;                
//            case "SquidNigiri":
//                
//                break;               
//            case "EggNigiri":
//                
//                break;               
//            case "Pudding":
//                
//                break;               
//            case "Wasabi":
//                
//                break;               
//            case "Chopsticks":
//                
//                break;
//        }
//        
//        return tmp;
//    }
}