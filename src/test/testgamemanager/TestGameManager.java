package test.testgamemanager;

import org.junit.*;
import static org.junit.Assert.*;

import gamemanager.GameManager;
import entities.*;
import cards.Card;
import guielements.GameBoard;

import java.util.ArrayList;

public class TestGameManager {
    
    private GameManager gm;
    
    @Before
    public void init() {
        
        // for most tests, need to include these last two lines of code to 
        // prevent weird behavior (don't know what test will be executed first at runtime)
        gm = new GameManager();
        gm.init();
    }
    
    @Test
    public void testPlayerGameBoardCopy() {
        
        System.out.println("In testPlayerGameBoardCopy()");
        
        Player p1 = gm.getPlayer();
        GameBoard p1GameBoard = p1.getPlayersGameBoard();
        GameBoard p1GameBoardCopy = gm.getPlayersGameBoardCopy();
        assertNotSame(p1GameBoard, p1GameBoardCopy);
    }
    
    @Test
    public void testAIGameBoardCopies() {
        
        System.out.println("In testAIGameBoardCopies())");
        
        ArrayList<AI> ais = gm.getAIs();
        ArrayList<GameBoard> aisGameBoards = new ArrayList<>();
        for(int i = 0; i < ais.size(); i++)
            aisGameBoards.add(ais.get(i).getAIsGameBoard());
        
        ArrayList<GameBoard> aisGameBoardCopies = gm.getAIsGameBoardCopies();
        for(int i = 0; i < ais.size(); i++)
            assertNotSame(aisGameBoards.get(i), aisGameBoardCopies.get(i));
    }
    
    // used for testCalculateScores() to help properly init everything
    // just know that the way this is set up prevents weird stuff from happening
    private void setEveryonesCard(GameManager gumbo) {
        
        // note: when gamemanager inits, it gives all players cards, when continueTurn is 
        // called everyone plays a card which could change the score, fixing that
        ArrayList<AI> ais = gumbo.getAIs();
        
        for(AI ai : ais) {
            
            // don't want the round to end prematurely, so adding 2 cards worth 0 pts as a control
            ArrayList<Card> oneCard = new ArrayList<>();
            oneCard.add(new Card("Chopsticks"));
            oneCard.add(new Card("Chopsticks")); 
            ai.setAIsHand(oneCard);
        }
    }
    
    @Test
    public void testCalculcateScores() {
        
        System.out.println("In testCalculateScores()");
        
        setEveryonesCard(gm);
        Card c1 = new Card("SquidNigiri");  
        Player p1 = gm.getPlayer();
        GameBoard g1 = p1.getPlayersGameBoard();
        g1.playCard(c1);
        
        gm.continueTurn();
        int score = p1.getScore();
        assertEquals(3, score);
        
        gm = new GameManager();     
        gm.init();
        setEveryonesCard(gm);
        
        c1 = new Card("SalmonNigiri");
        AI ai = gm.getAI(0);
        g1 = ai.getAIsGameBoard();
        g1.playCard(c1);
        gm.continueTurn();
        score = ai.getScore();
        assertEquals(2, score);
        
        gm = new GameManager();
        gm.init();
        setEveryonesCard(gm);
        
        c1 = new Card("Tempura");
        Card c2 = new Card("Tempura");
        Card c3 = new Card("SquidNigiri");
        p1 = gm.getPlayer();
        g1 = p1.getPlayersGameBoard();
        g1.playCard(c1);
        g1.playCard(c2);
        g1.playCard(c3);
        gm.continueTurn();
        score = p1.getScore();
        assertEquals(8, score);
        
        gm = new GameManager();
        gm.init();
        setEveryonesCard(gm);
        
        c1 = new Card("Sashimi");
        c2 = new Card("Sashimi");
        c3 = new Card("Sashimi");
        Card c4 = new Card("Wasabi");
        Card c5 = new Card("SquidNigiri");
        ai = gm.getAI(2);
        g1 = ai.getAIsGameBoard();
        g1.playCard(c1, 0);
        g1.playCard(c2, 0);
        g1.playCard(c3, 0);
        g1.playCard(c4, 1);
        g1.playCard(c5, 1);
        gm.continueTurn();
        score = ai.getScore();
        assertEquals(19, score);
    }
    
    @Test
    public void testPassHand() {
        
        System.out.println("In testPassHand()");
        
        Player p1 = gm.getPlayer();
        ArrayList<Card> p1Hand = p1.getPlayersHand();
        AI ai0 = gm.getAI(0);
        ArrayList<Card> ai0Hand = ai0.getAIsHand();
        AI ai1 = gm.getAI(1);
        ArrayList<Card> ai1Hand = ai1.getAIsHand();
        AI ai2 = gm.getAI(2);
        ArrayList<Card> ai2Hand = ai2.getAIsHand();
        gm.continueTurn();
        
        ArrayList<Card> p1NewHand = p1.getPlayersHand();
        ArrayList<Card> ai0NewHand = ai0.getAIsHand();
        ArrayList<Card> ai1NewHand = ai1.getAIsHand();
        ArrayList<Card> ai2NewHand = ai2.getAIsHand();
        assertNotSame(p1Hand, p1NewHand);
        assertNotSame(ai0Hand, ai0NewHand);
        assertNotSame(ai1Hand, ai1NewHand);
        assertNotSame(ai2Hand, ai2NewHand);
        gm = new GameManager();
        gm.init();
    }
    
    @Test
    public void testCheckRoundOver() {
        
        System.out.println("In testCheckRoundOver()");
        
        Player p1 = gm.getPlayer();
        ArrayList<Card> oneCard = new ArrayList<>();
        oneCard.add(new Card("Sashimi"));
        p1.setPlayersHand(oneCard);
        ArrayList<AI> ais = gm.getAIs();
        
        for(int i = 0; i < ais.size(); i++) {
            
            ArrayList<Card> aiOneCard = new ArrayList<>();
            aiOneCard.add(new Card("Sashimi"));
            ais.get(i).setAIsHand(aiOneCard);
        }
        
        gm.continueTurn();
        boolean roundOver = gm.getRoundOver();
        assertTrue(roundOver);
        gm = new GameManager();
        gm.init();
    }
    
    @Test
    public void testCheckGameOver() {
        
        System.out.println("In testCheckGameOver()");
        
        for(int i = 0; i < 3; i++) {
            
            Player p1 = gm.getPlayer();
            ArrayList<Card> oneCard = new ArrayList<>();
            oneCard.add(new Card("Sashimi"));
            p1.setPlayersHand(oneCard);
            ArrayList<AI> ais = gm.getAIs();
            
            for(int j = 0; j < ais.size(); j++) {
                
                ArrayList<Card> aiOneCard = new ArrayList<>();
                aiOneCard.add(new Card("Sashimi"));
                ais.get(j).setAIsHand(aiOneCard);
            }
            
            gm.continueTurn();
        }
        
        boolean gameOver = gm.getGameOver();
        assertTrue(gameOver);
        gm = new GameManager();
        gm.init();
    }
    
    @Test
    public void testScoreMakis() {
        
        System.out.println("In testScoreMakis()");
       
        // 1 person got 1st 1 person got 2nd
        Player p1 = gm.getPlayer();
        GameBoard p1gb = p1.getPlayersGameBoard();
        ArrayList<AI> ais = gm.getAIs();
        ArrayList<GameBoard> aisgb = new ArrayList<>();
        for(int i = 0; i < ais.size(); i++)
            aisgb.add(ais.get(i).getAIsGameBoard());
        
        p1gb.playCard(new Card("TripleMakiRoll"));
        aisgb.get(0).playCard(new Card("SingleMakiRoll"));
        ArrayList<Card> oneCard = new ArrayList<>();
        oneCard.add(new Card("Sashimi"));
        p1.setPlayersHand(oneCard);
        
        for(int j = 0; j < ais.size(); j++) {
                
            ArrayList<Card> aiOneCard = new ArrayList<>();
            aiOneCard.add(new Card("Sashimi"));
            ais.get(j).setAIsHand(aiOneCard);
        }
            
        gm.continueTurn();
        int playerScore = gm.getPlayer().getScore();
        int ai0Score = gm.getAI(0).getScore();
        assertEquals(playerScore, 6);
        assertEquals(ai0Score, 3);
        gm = new GameManager();
        gm.init();
        
        // 2 people got 1st
        p1 = gm.getPlayer();
        p1gb = p1.getPlayersGameBoard();
        ais = gm.getAIs();
        aisgb = new ArrayList<>();
        for(int i = 0; i < ais.size(); i++)
            aisgb.add(ais.get(i).getAIsGameBoard());
        
        p1gb.playCard(new Card("TripleMakiRoll"));
        aisgb.get(0).playCard(new Card("TripleMakiRoll"));
        oneCard = new ArrayList<>();
        oneCard.add(new Card("Sashimi"));
        p1.setPlayersHand(oneCard);
        
        for(int j = 0; j < ais.size(); j++) {
                
            ArrayList<Card> aiOneCard = new ArrayList<>();
            aiOneCard.add(new Card("Sashimi"));
            ais.get(j).setAIsHand(aiOneCard);
        }
            
        gm.continueTurn();
        playerScore = gm.getPlayer().getScore();
        ai0Score = gm.getAI(0).getScore();
        assertEquals(playerScore, 3);
        assertEquals(ai0Score, 3);
        gm = new GameManager();
        gm.init();
        
        // 1 person got 1st, 2 people got 2nd
        p1 = gm.getPlayer();
        p1gb = p1.getPlayersGameBoard();
        ais = gm.getAIs();
        aisgb = new ArrayList<>();
        for(int i = 0; i < ais.size(); i++)
            aisgb.add(ais.get(i).getAIsGameBoard());
        
        p1gb.playCard(new Card("TripleMakiRoll"));
        aisgb.get(0).playCard(new Card("SingleMakiRoll"));
        aisgb.get(1).playCard(new Card("SingleMakiRoll"));
        oneCard = new ArrayList<>();
        oneCard.add(new Card("Sashimi"));
        p1.setPlayersHand(oneCard);
        
        for(int j = 0; j < ais.size(); j++) {
                
            ArrayList<Card> aiOneCard = new ArrayList<>();
            aiOneCard.add(new Card("Sashimi"));
            ais.get(j).setAIsHand(aiOneCard);
        }
            
        gm.continueTurn();
        playerScore = gm.getPlayer().getScore();
        ai0Score = gm.getAI(0).getScore();
        int ai1Score = gm.getAI(1).getScore();
        assertEquals(playerScore, 6);
        assertEquals(ai0Score, 1);
        assertEquals(ai1Score, 1);
        gm = new GameManager();
        gm.init();
    }
    
    @Test
    public void testScorePuddings() {
        
        System.out.println("In testScorePuddings()");
        
        // 1 person got 1st 1 person got last
        Player p1 = gm.getPlayer();
        GameBoard p1gb = p1.getPlayersGameBoard();
        ArrayList<AI> ais = gm.getAIs();
        ArrayList<GameBoard> aisgb = new ArrayList<>();
        for(int i = 0; i < ais.size(); i++)
            aisgb.add(ais.get(i).getAIsGameBoard());
        
        p1gb.playCard(new Card("Pudding"));
        p1gb.playCard(new Card("Pudding"));
        aisgb.get(0).playCard(new Card("Pudding"));
        aisgb.get(1).playCard(new Card("Pudding"));
        // last ai player gets no puddings
        
        for(int i = 0; i < 3; i++) {
            
            ArrayList<Card> oneCard = new ArrayList<>();
            oneCard.add(new Card("Chopsticks"));
            p1.setPlayersHand(oneCard);
            p1gb.playCard(oneCard.remove(0));
            
            for(int j = 0; j < ais.size(); j++) {
                
                ArrayList<Card> aiOneCard = new ArrayList<>();
                aiOneCard.add(new Card("Chopsticks"));
                ais.get(j).setAIsHand(aiOneCard);
            }
            
            gm.continueTurn();
        }
        
        // interesting find: at the end of every round, everyone will be awarded 1 point because
        // they all tied for first for makis which is weird; need to account for that below
        int weirdMakiBonus = 3;
        int playerScore = gm.getPlayer().getScore();
        int ai2Score = gm.getAI(2).getScore();
        assertEquals(6 + weirdMakiBonus, playerScore);
        assertEquals(-6 + weirdMakiBonus, ai2Score);
        gm = new GameManager();
        gm.init();
        
        // 2 people got 1st, 2 people got last
        p1 = gm.getPlayer();
        p1gb = p1.getPlayersGameBoard();
        ais = gm.getAIs();
        aisgb = new ArrayList<>();
        for(int i = 0; i < ais.size(); i++)
            aisgb.add(ais.get(i).getAIsGameBoard());
        
        p1gb.playCard(new Card("Pudding"));
        aisgb.get(0).playCard(new Card("Pudding"));
        // last 2 ai players get no puddings
        
        for(int i = 0; i < 3; i++) {
            
            ArrayList<Card> oneCard = new ArrayList<>();
            oneCard.add(new Card("Chopsticks"));
            p1.setPlayersHand(oneCard);
            p1gb.playCard(oneCard.remove(0));
            
            for(int j = 0; j < ais.size(); j++) {
                
                ArrayList<Card> aiOneCard = new ArrayList<>();
                aiOneCard.add(new Card("Chopsticks"));
                ais.get(j).setAIsHand(aiOneCard);
            }
            
            gm.continueTurn();
        }
        
        weirdMakiBonus = 3;
        playerScore = gm.getPlayer().getScore();
        int ai0Score = gm.getAI(0).getScore();
        int ai1Score = gm.getAI(1).getScore();
        ai2Score = gm.getAI(2).getScore();
        assertEquals(3 + weirdMakiBonus, playerScore);
        assertEquals(3 + weirdMakiBonus, ai0Score);
        assertEquals(-3 + weirdMakiBonus, ai1Score);
        assertEquals(-3 + weirdMakiBonus, ai2Score);
        gm = new GameManager();
        gm.init();
    }
    
    @Test
    public void testGetNamesAndScores() {
     
        System.out.println("In testGetNamesAndScores()");
        
        ArrayList<String> actualNames = gm.getAIsNames();
        actualNames.add(0, "You");
        
        String[] namesAndScores = gm.getNamesAndScores();
        String[] names = new String[namesAndScores.length];
        String[] scores = new String[namesAndScores.length];
        for(int i = 0; i < names.length; i++) {
            
            String[] temp = namesAndScores[i].split(": ");
            names[i] = temp[0];
            scores[i] = temp[1];
        }
        
        for(int i = 0; i < names.length; i++) {
         
            assertEquals(actualNames.get(i), names[i]);
            assertEquals(0, Integer.parseInt(scores[i]));
        }
    }
    
    @Test
    public void testGetWinner() {
        
        System.out.println("In testGetWinner()");
        
        // testing player won
        Player p1 = gm.getPlayer();
        GameBoard p1gb = p1.getPlayersGameBoard();
        ArrayList<AI> ais = gm.getAIs();
        
        p1gb.playCard(new Card("SquidNigiri"));
        
        for(int i = 0; i < 3; i++) {
            
            ArrayList<Card> oneCard = new ArrayList<>();
            oneCard.add(new Card("Chopsticks"));
            p1.setPlayersHand(oneCard);
            p1gb.playCard(oneCard.remove(0));
            
            for(int j = 0; j < ais.size(); j++) {
                
                ArrayList<Card> aiOneCard = new ArrayList<>();
                aiOneCard.add(new Card("Chopsticks"));
                ais.get(j).setAIsHand(aiOneCard);
            }
            
            gm.continueTurn();
        }
        
        String winner = gm.getWinner();
        assertEquals("You Won!", winner);
        gm = new GameManager();
        gm.init();
    }
    
    @Test
    public void testRoundOver() {
     
        System.out.println("In testRoundOver()");
        
        // round over
        Player p1 = gm.getPlayer();
        ArrayList<Card> oneCard = new ArrayList<>();
        oneCard.add(new Card("Chopsticks"));
        p1.setPlayersHand(oneCard);
        GameBoard p1gb = p1.getPlayersGameBoard();
        p1gb.playCard(oneCard.remove(0));
           
        ArrayList<AI> ais = gm.getAIs();
        
        for(int j = 0; j < ais.size(); j++) {
                
            ArrayList<Card> aiOneCard = new ArrayList<>();
            aiOneCard.add(new Card("Chopsticks"));
            ais.get(j).setAIsHand(aiOneCard);
        }
            
        gm.continueTurn();
        
        boolean roundOver = gm.getRoundOver();
        assertTrue(roundOver);
        gm = new GameManager();
        gm.init();
        
        // not round over
        p1 = gm.getPlayer();
        ArrayList<Card> twoCard = new ArrayList<>();
        twoCard.add(new Card("Chopsticks"));
        twoCard.add(new Card("Chopsticks"));
        p1.setPlayersHand(twoCard);
        p1gb = p1.getPlayersGameBoard();
        p1gb.playCard(twoCard.remove(0));
           
        ais = gm.getAIs();
        
        for(int j = 0; j < ais.size(); j++) {
                
            ArrayList<Card> aiTwoCard = new ArrayList<>();
            aiTwoCard.add(new Card("Chopsticks"));
            aiTwoCard.add(new Card("Chopsticks"));
            ais.get(j).setAIsHand(aiTwoCard);
        }
            
        gm.continueTurn();
        
        roundOver = gm.getRoundOver();
        assertFalse(roundOver);
        gm = new GameManager();
        gm.init();
    }
    
    @Test
    public void testGameOver() {
        
        System.out.println("In testGameOver()");
        
        // not game over
        Player p1 = gm.getPlayer();
        ArrayList<Card> oneCard = new ArrayList<>();
        oneCard.add(new Card("Chopsticks"));
        p1.setPlayersHand(oneCard);
        GameBoard p1gb = p1.getPlayersGameBoard();
        p1gb.playCard(oneCard.remove(0));
           
        ArrayList<AI> ais = gm.getAIs();
        
        for(int j = 0; j < ais.size(); j++) {
                
            ArrayList<Card> aiOneCard = new ArrayList<>();
            aiOneCard.add(new Card("Chopsticks"));
            ais.get(j).setAIsHand(aiOneCard);
        }
            
        gm.continueTurn();
        
        boolean gameOver = gm.getGameOver();
        assertFalse(gameOver);
        
        gm = new GameManager();
        gm.init();
        
        // game over
        p1 = gm.getPlayer();
        ais = gm.getAIs();
        
        for(int i = 0; i < 3; i++) {
            
            oneCard = new ArrayList<>();
            oneCard.add(new Card("Chopsticks"));
            p1.setPlayersHand(oneCard);
            p1gb.playCard(oneCard.remove(0));
            
            for(int j = 0; j < ais.size(); j++) {
                
                ArrayList<Card> aiOneCard = new ArrayList<>();
                aiOneCard.add(new Card("Chopsticks"));
                ais.get(j).setAIsHand(aiOneCard);
            }
            
            gm.continueTurn();
        } 
        
        gameOver = gm.getGameOver();
        assertTrue(gameOver);
        gm = new GameManager();
        gm.init();
    }
}