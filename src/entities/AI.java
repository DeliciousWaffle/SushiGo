package entities;

import cards.*;
import guielements.GameBoard;

import java.util.ArrayList;
import java.awt.Color;

public class AI {
    
    private String name;
    private int difficulty;
    private GameBoard AIsGameBoard;
    private ArrayList<Card> AIsHand;
    private int score;
    
    public AI(String name, int difficulty) {
        
        this.name = name;
        this.difficulty = difficulty;
        this.AIsGameBoard = new GameBoard(0, 0, 0);
    }
    
    public String getName() { return name; }
    public int getDifficulty() { return difficulty; }
    public GameBoard getAIsGameBoard() { return AIsGameBoard; }
    public ArrayList<Card> getAIsHand() { return AIsHand; }
    public int getScore() { return score; }

    public void setName(String name) { this.name = name; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
    public void setAIsGameBoard(GameBoard AIsGameBoard) { this.AIsGameBoard = AIsGameBoard; }
    public void setAIsHand(ArrayList<Card> AIsHand) { this.AIsHand = AIsHand; }
    public void setScore(int score) { this.score = score; }
    
    public void playCard(int index) {
        
        AIsGameBoard.playCard(AIsHand.remove(index));
        AIsGameBoard.setColor(Color.WHITE);
    }
}