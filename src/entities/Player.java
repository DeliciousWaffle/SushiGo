package entities;

import cards.*;
import guielements.GameBoard;

import java.util.ArrayList;

public class Player {
    
    private String name;
    private GameBoard playersGameBoard;
    private ArrayList<Card> playersHand;
    private int score;
    
    public Player() {
    
        this.name = "You";
        this.playersGameBoard = new GameBoard(0, 0, 0);
    }
    
    public String getName() { return name; }
    public GameBoard getPlayersGameBoard() { return playersGameBoard; }
    public ArrayList<Card> getPlayersHand() { return playersHand; }
    public int getScore() { return score; }
    
    public void setName(String name) { this.name = name; }
    public void setPlayersBoard(GameBoard playersGameBoard) { this.playersGameBoard = playersGameBoard; }
    public void setPlayersHand(ArrayList<Card> playersHand) { this.playersHand = playersHand; }
    public void setScore(int score) { this.score = score; }
}