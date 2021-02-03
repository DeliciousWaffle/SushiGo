package cards;

import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Collections;

public class Deck {
    
    private Stack<Card> deck;
    private int size;
    
    public Deck() {
        
        deck = new Stack<>();
        
        for(int i = 0; i < 14; i++)
            deck.add(new Card("Tempura"));        
        for(int i = 0; i < 14; i++)
            deck.add(new Card("Sashimi"));       
        for(int i = 0; i < 14; i++)
            deck.add(new Card("Dumpling"));
        for(int i = 0; i < 6; i++)
            deck.add(new Card("SingleMakiRoll"));
        for(int i = 0; i < 12; i++)
            deck.add(new Card("DoubleMakiRoll"));
        for(int i = 0; i < 8; i++)
            deck.add(new Card("TripleMakiRoll"));
        for(int i = 0; i < 10; i++)
            deck.add(new Card("SalmonNigiri"));
        for(int i = 0; i < 5; i++)
            deck.add(new Card("SquidNigiri"));
        for(int i = 0; i < 5; i++)
            deck.add(new Card("EggNigiri"));
        for(int i = 0; i < 10; i++)
            deck.add(new Card("Pudding"));
        for(int i = 0; i < 6; i++)
            deck.add(new Card("Wasabi"));
        for(int i = 0; i < 4; i++)
            deck.add(new Card("Chopsticks"));
        
        size = 108;
        
        Collections.shuffle(deck);
    }
    
    public int getSize() { return size; }
    
    // deal a certain amount of cards to each player
    // 2 players -> 10 cards to each
    // 3 players -> 9 cards to each
    // 4 players -> 8 cards to each
    // 5 players -> 7 cards to each
    public ArrayList<Card> dealHand(int numberOfPlayers) {
        
        ArrayList<Card> hand = new ArrayList<>();
        int numberOfCardsToDeal = 12 - numberOfPlayers;
        
        for(int i = 0; i < numberOfCardsToDeal; i++) {
            hand.add(deck.pop());
            size--;
        }
        
        return hand;
    }

    // take a single card off the the deck
    public Card drawCard() { 
        
        size--;
        return deck.pop(); 
    }
    
    public boolean isEmpty() { return size == 0; }
    
    @Override
    public String toString() {
        
        // making a deep copy to prevent modifications to the original deck
        Stack<Card> deckCopy = new Stack<>();
        deckCopy.addAll(deck);
        
        StringBuilder sb = new StringBuilder();
        
        String deckSize = "Deck Size: " + Integer.toString(size) + "\n";
        sb.append(deckSize);
        
        for(int i = 0; i < size; i++) {
            
            String currentCard;
            
            if(! (i == (size - 1)))
                currentCard = deckCopy.pop() + "\n";
            
            else
                currentCard = deckCopy.pop().toString();
            
            sb.append(currentCard);
        }
        
        return sb.toString();
    }
}
