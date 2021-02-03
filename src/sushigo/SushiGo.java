package sushigo;

import assets.AssetLoader;

import javax.swing.JFrame;
import java.awt.EventQueue;

/*
    Main class that launches the entire game. Sushi Go is a card game in which
    the goal is to get the most points. The game is 2 to 5 players, but is
    set up to be single player. The player is up against AIs whose difficulties
    can be changed. There are 3 rounds and points get added cumulatively between rounds.
    Authors: Jake Bussa    Alex Dermer
*/
public class SushiGo extends JFrame {
    
    /*
        Called by the main method, this pretty much gets the game going. Starts by
        loading in all the assets to be used throughout the game. Afterwards,
        the panel, which is responsible for showing the gui and allowing for user
        interactions, gets created. Boilerplate code to set up the frame comes next.
        Lastly, the panel gets added to the frame.
    */    
    public void init() {
        
        new AssetLoader();
        
        SushiGoPanel panel = new SushiGoPanel();
        
        this.setTitle("SushiGo");
        this.setUndecorated(true);                              
        this.setDefaultLookAndFeelDecorated(true);
        this.setContentPane(panel);
        this.pack();  
        this.setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel.setFrame(this);
    }

    public static void main(String[] args) {
        
        // safe way to launch the game
        EventQueue.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                
                new SushiGo().init();
            }
        });
    }
}