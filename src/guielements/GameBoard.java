package guielements;

import assets.AssetLoader;
import cards.Card;
import misc.Skittles;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;

public class GameBoard {
    
    private ArrayList<CardSlot> cardSlots;
    private int x, y;
    private double scale;
    private double blackMatWidth;
    private int cumulativeScore;
    private ArrayList<Color> colors;
    private int colorIterator;
    private boolean raveMode;
    
    public GameBoard(int x, int y, double scale) {
        
        this.x = x;
        this.y = y;
        this.scale = scale;
        
        this.cardSlots = new ArrayList<>();
        
        // creating 9 slots, makis get grouped together and wasabi can be placed on various nigiri
        int offsetX = 0;
        
        for(int i = 0; i < 9; i++) {
            
            cardSlots.add(new CardSlot(x + offsetX, y, scale));
            offsetX += (110 * scale);
        }
        
        this.blackMatWidth = offsetX;
        this.colors = new Skittles().tasteTheRainbow();
        colorIterator = 0;
    }
    
    // copy constructor
    public GameBoard(GameBoard copyGameBoard) {
        
        this.cardSlots = new ArrayList<>();
        for(int i = 0; i < copyGameBoard.cardSlots.size(); i++)
            this.cardSlots.add(new CardSlot(copyGameBoard.getCardSlot(i)));
        this.x = copyGameBoard.x;
        this.y = copyGameBoard.y;
        this.scale = copyGameBoard.scale;
        this.blackMatWidth = copyGameBoard.blackMatWidth;
        this.cumulativeScore = copyGameBoard.cumulativeScore;
        this.colors = new Skittles().tasteTheRainbow();
        this.colorIterator = copyGameBoard.colorIterator;
    }
    
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    
    public void setScale(double scale) { this.scale = scale; }
    
    public void setCumulativeScore(int cumulativeScore) { this.cumulativeScore = cumulativeScore; }
    
    public void setColor(Color color) {
        
        for(CardSlot cs : cardSlots)
            cs.setColor(color);
    }
    
    public void setColor(int i, Color color) { cardSlots.get(i).setColor(color); }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    public double getScale() { return scale; }
    
    public int getCumulativeScore() { return cumulativeScore; }
    
    public int getSize() { return cardSlots.size(); }
    
    public int getCardSlotIndex(String slotName) {
        
        for(int i = 0; i < cardSlots.size(); i++)
            if(cardSlots.get(i).getSlotName() != null && cardSlots.get(i).getSlotName().equals(slotName))
                return i;
        
        // slot name doesn't exist
        return -1;
    }
    
    private CardSlot getCardSlot(int i) { return cardSlots.get(i); }
    
    // the game would not work without this addition
    public void setRaveMode(boolean raveMode) { this.raveMode = raveMode; }
    
    public void setEffects(boolean effects) {
        
        for(CardSlot cs : cardSlots)
            cs.setBlinking(effects);
    }
    
    public void stopChopsticksEffects() {
        
        for(CardSlot cs : cardSlots)
            if(! cs.isEmpty() && cs.getSlotName().equals("Chopsticks"))
                cs.stopBlinkingChopsticks();
    }
    
    /*
        Clears all cards except for puddings off of the board.
    */
    public void clear() {
        
        cumulativeScore = calculateScore();   
        
        ArrayList<Card> puddings = new ArrayList<>();
        
        // remove all the puddings first before clearing
        for(int i = 0; i < cardSlots.size(); i++)
            if((! cardSlots.get(i).isEmpty()) && cardSlots.get(i).getSlotName().equals("Pudding")) {
                while(! cardSlots.get(i).isEmpty())
                    puddings.add(cardSlots.get(i).removeCard());
                break;
            }
        
        // clear all the slots now
        for(CardSlot cs : cardSlots)
            cs.clear();
        
        // if there were any puddings, place them in the last card slot
        while(! puddings.isEmpty())
            cardSlots.get(8).playCard(puddings.remove(0));
    }
    
    /*
        Returns whether the board contains any chopsticks to play and whether the mouse's
        location is within that particular card slot.
    */
    public boolean hasChopsticks(Point p) {
        
        for(CardSlot cs : cardSlots)
            // not allowed to play chopsticks if there is only 1 card left
            if(! cs.isEmpty() && cs.getSlotName().equals("Chopsticks"))
                return cs.contains(p);
        
        return false;
    }
    
    /* 
       Plays the chopsticks card which allows the player to play 2 cards this
       turn instead of 1. Afterwards, the chopsticks are put back into the
       player's hand.
    */
    public Card playChopsticks() {
        
        Card chopsticks = null;
        
        for(CardSlot cs : cardSlots)
            if(! cs.isEmpty() && cs.getSlotName().equals("Chopsticks"))
                chopsticks = cs.removeChopsticks();
        
        return chopsticks;
    }
    
    /*
        Checks to see if the mouse's location and the card's location intersect
        with the board.
    */
    public boolean intersects(Point p, Card c) {
        
        return getBounds().contains(p) && c.getBounds().intersects(getBounds());
    }
    
    /*
        Returns the area of the entire board.
    */
    private Rectangle getBounds() {
        
        return new Rectangle(x - (int) (10 * scale), y - (int) (10 * scale), 
            (int) (blackMatWidth + 10 * scale), (int) (300 * scale) + (int) (20 * scale));
    }
    
    /*
        Checks to see if the mouse's location and the card's location intersect
        with a card slot within the board.
    */
    public boolean intersectsCardSlot(Point p, Card c, int cardSlotIndex) {
        
        return cardSlots.get(cardSlotIndex).intersects(p, c);
    }
    
    /*
        Makes sure that the card being played gets placed where it's supposed to.
    */
    public boolean validCardSlot(Card c, int cardSlotIndex) {
        
        // if a player previously played a card of the same type and is attempting to
        // play the card elsewhere, prevent that from happening
        if(previouslyPlayedThisCard(c, cardSlotIndex)) {
            
            // tell the player that the card slot they're picking is invalid
            cardSlots.get(cardSlotIndex).setColor(Color.RED);
            resetAllExcept(cardSlotIndex);
            return false;
        }
        
        // there are no cards within this slot, we're good to go
        if(cardSlots.get(cardSlotIndex).isEmpty()) {
            
            // tell the player that this card is in a valid spot
            cardSlots.get(cardSlotIndex).setColor(Color.GREEN);
            resetAllExcept(cardSlotIndex);
            return true;
        }
        
        // makis check
        if(cardSlots.get(cardSlotIndex).getSlotName().equals("Makis")) {
            
            if(c.getName().equals("SingleMakiRoll") || c.getName().equals("DoubleMakiRoll") || 
                    c.getName().equals("TripleMakiRoll")) {
                    
                cardSlots.get(cardSlotIndex).setColor(Color.GREEN);
                resetAllExcept(cardSlotIndex);
                return true;
            }
        }
        
        // wasabi check (an egg nigiri, squid nigiri, or salmon nigiri gets played on a wasabi)
        if(cardSlots.get(cardSlotIndex).getSlotName().equals("Wasabi")) {
            
            if(c.getName().equals("EggNigiri") || c.getName().equals("SquidNigiri") ||
                    c.getName().equals("SalmonNigiri")) {
                
                cardSlots.get(cardSlotIndex).setColor(Color.GREEN);
                resetAllExcept(cardSlotIndex);
                return true;
            }
        }
        
        // nigiri check (a wasabi gets played on an egg nigiri, squid nigiri, or salmon nigiri)
        if(cardSlots.get(cardSlotIndex).getSlotName().equals("EggNigiri") ||
                cardSlots.get(cardSlotIndex).getSlotName().equals("SquidNigiri") ||
                cardSlots.get(cardSlotIndex).getSlotName().equals("SalmonNigiri")) {
            
            if(c.getName().equals("Wasabi")) {
                
                cardSlots.get(cardSlotIndex).setColor(Color.GREEN);
                resetAllExcept(cardSlotIndex);
                return true;
            }
        }
        
        // check to see if the names match
        if(cardSlots.get(cardSlotIndex).getSlotName().equals(c.getName())) {
        
            cardSlots.get(cardSlotIndex).setColor(Color.GREEN);
            resetAllExcept(cardSlotIndex);
            return true;
            
        } else {
            
            cardSlots.get(cardSlotIndex).setColor(Color.RED);
            resetAllExcept(cardSlotIndex);
            return false;
        }
    }
    
    /*
        Checks to see if the card being played has already been played. Need to
        prevent this from happening in order to keep the board organized and
        minimize wasted space.
    */
    private boolean previouslyPlayedThisCard(Card c, int cardSlotIndex) {
        
        for(int i = 0; i < cardSlots.size(); i++) {
            
            if(i == cardSlotIndex)
                continue;
            
            // make sure that any comparisons to names we make don't lead to null pointers
            if(cardSlots.get(i).getSlotName() != null) {
                
                // makis check
                if(cardSlots.get(i).getSlotName().equals("Makis"))
                    if(c.getName().equals("SingleMakiRoll") || c.getName().equals("DoubleMakiRoll") || 
                            c.getName().equals("TripleMakiRoll"))
                        return true;
                
                // previously played a card
                if(cardSlots.get(i).getSlotName().equals(c.getName()))
                    return true;
            }
        }
        
        return false;
    }
    
    public void formatBoard(int x, int y, double scale) {
        
        this.x = x;
        this.y = y;
        this.scale = scale;
        
        int offsetX = 0;
        
        for(CardSlot cs : cardSlots) {
            
            cs.formatCardSlot(x + offsetX, y, scale);
            offsetX += (110 * scale);            
        }
        
        this.blackMatWidth = offsetX;
    }
    
    public void reset() {
        
        for(CardSlot cs : cardSlots)
            cs.reset();
    }
    
    public void resetAllExcept(int cardSlotIndex) {
        
        for(int i = 0; i < cardSlots.size(); i++)
            if(i != cardSlotIndex)
                cardSlots.get(i).reset();
    }
        
    /*
        Allows the player to play a card.
    */
    public void playCard(Card c, int cardSlotIndex) {
        
        cardSlots.get(cardSlotIndex).playCard(c);
    }
    
    /*
        Allows the AI to play a card.
    */
    public void playCard(Card c) {
        
        int randomSlotIndex = new java.util.Random().nextInt(cardSlots.size());
        
        while(! validCardSlot(c, randomSlotIndex))
            randomSlotIndex = new java.util.Random().nextInt(cardSlots.size());
        
        cardSlots.get(randomSlotIndex).playCard(c);
    }
    
    /*
        Calculates the score of all sushi played on the game board. Everything is scored
        except for the maki and pudding bonus.
    */
    public int calculateScore() {
        
        int score = 0;
        
        for(CardSlot cs : cardSlots)
            score += cs.calculateScore();
        
        return score + cumulativeScore;
    }
    
    public int calculateAITestScore(Card card){
        
        int score = 0;
        
        // safe copy to work with
        ArrayList<CardSlot> cardSlotCopies = new ArrayList<>();
        
        for(int i = 0; i < cardSlots.size(); i++)
            cardSlotCopies.add(new CardSlot(cardSlots.get(i)));
        
        for(CardSlot cs : cardSlotCopies){
            if(cs.getSlotName() != null){
                if(cs.getSlotName().equals("Makis")){
                    if(card.getName().equals("SingleMakiRoll") || card.getName().equals("DoubleMakiRoll") || card.getName().equals("TripleMakiRoll")){
                        cs.addCard(card);
                        cs.calculateScore();
                        cs.removeCard(card);
                    }
                }
                else if(cs.getSlotName().equals(card.getName())){
                    //System.out.println("..");
                    cs.addCard(card);
                    cs.calculateScore();
                    cs.removeCard(card);
                }
            }
            score += cs.calculateScore();
        }
        return score;
    }
    
    // now returns a safe copy of all cards on the game board
    public ArrayList<Card> getBoardCards() {
        ArrayList<Card> cards = new ArrayList<>();
        for(CardSlot cs : cardSlots){
            for(Card card : cs.getCards()){
                cards.add(new Card(card));    
            }
        }
        return cards;
    }
    
    public int getMakiAmount() {
        
        for(CardSlot cs : cardSlots)
            if(! cs.isEmpty() && cs.getSlotName().equals("Makis"))
                return cs.getMakiAmount();
        
        return 0;
    }
    
    public int getPuddingAmount() {
        
        // even though the last slot is designated for puddings only, need to account for if the player played no puddings all game except for this round
        for(CardSlot cs : cardSlots)
            if(! cs.isEmpty() && cs.getSlotName().equals("Pudding"))
                return cs.size();
        
        return 0;
    }
    
    public void update() {
        
        colorIterator += 2;
        
        if(colorIterator >= colors.size() - 1)
            colorIterator = 0;
    }
    
    public void render(Graphics2D g2) {
             
        g2.drawImage(AssetLoader.blackBackground, x - (int) (10 * scale), y - (int) (10 * scale), 
            (int) (blackMatWidth + 10 * scale), (int) (300 * scale) + (int) (20 * scale), null);
 
        if(raveMode)
            for(CardSlot cs : cardSlots)
                cs.setColor(colors.get(colorIterator));
        
        for(CardSlot cs : cardSlots)
            cs.render(g2);
    }  
    
    public boolean hasUsableWasabi() {
        for(CardSlot cs : cardSlots){
            if(cs.hasUsableWasabi())
                return true;
        }
        return false;
    }
    // ----------------------------------- start of inner class CardSlot ------------------------------------------------
    
    // can have a max of 7 cards per a slot
    private class CardSlot {
        
        private String slotName;
        private ArrayList<Card> cards;
        private int x, y;
        private double scale;
        private Color color;
        private long startTime;
        private boolean blinking, blinkingChopsticks;
        
        public CardSlot(int x, int y, double scale) {
            
            this.cards = new ArrayList<>();
            this.x = x;
            this.y = y;
            this.scale = scale;
            this.color = Color.WHITE;
            this.startTime = System.currentTimeMillis();
            this.blinking = true;
            this.blinkingChopsticks = true;
        }
       
        // copy constructor
        public CardSlot(CardSlot copyCardSlot) {
            
            this.slotName = copyCardSlot.slotName;
            this.cards = new ArrayList<>();
            for(int i = 0; i < copyCardSlot.size(); i++)
                this.cards.add(new Card(copyCardSlot.getCards().get(i)));
            this.x = copyCardSlot.x;
            this.y = copyCardSlot.y;
            this.scale = copyCardSlot.scale;
            this.color = copyCardSlot.color;
            this.startTime = copyCardSlot.startTime;
            this.blinking = copyCardSlot.blinking;
            this.blinkingChopsticks = copyCardSlot.blinkingChopsticks;
        }
        
        public Card removeCard() {
            
            return cards.remove(0);
        }
        
        public void clear() {
            
            slotName = null;
            blinkingChopsticks = true;
            cards.clear();
        }
        
        public void setBlinking(boolean blinking) {
            this.blinking = blinking;
        }
        
        public void stopBlinkingChopsticks() {
            this.blinkingChopsticks = false;
        }
        
        /*
            Following removes a card from the slot. The only cards allowed to remove this
            way are wasabi and chopsticks.
        */
        public Card removeChopsticks() {
            
            // if this is the only card left, rename this slot to allow future cards to be played here
            if(cards.size() == 1)
                slotName = null;
            
            return cards.remove(cards.size() - 1);
        }
        
        public int size() {
            
            return cards.size();
        }
        
        public boolean contains(Point p) {
            
            return getBounds().contains(p);
        }
        
        /*
            Checks to see if this specific card slot intersects with the mouse's location
            and the card's location.
        */
        public boolean intersects(Point p, Card c) {
            
            return getBounds().contains(p) && c.getBounds().intersects(getBounds());
        }
                
        /*
            Returns whether there are cards within this card slot
        */
        public boolean isEmpty() {
            
            return cards.isEmpty();
        }
        
        public void setX(int x) { this.x = x; }
        public void setY(int y) { this.y = y; }
        
        public void setScale(double scale) { this.scale = scale; }
        
        public void setColor(Color color) { this.color = color; }
        
        public String getSlotName() { return slotName; }
        
        public Rectangle getBounds() { return new Rectangle(x, y, (int) (100 * scale), (int) (300 * scale)); }
        
        public void reset() {
            
            color = Color.WHITE;
            
            for(Card c : cards)
                c.setColor(Color.BLACK);
        }
        
        public void formatCardSlot(int x, int y, double scale) {
            
            this.x = x;
            this.y = y;
            this.scale = scale;
            
            int offsetY = 0;
            
            for(Card c : cards) {
                
                c.formatCard(x, y + offsetY, scale);
                offsetY += ((300 / 12) * scale);
            }
        }
        
        public void addCard(Card card) { cards.add(card); }
        
        public void removeCard(Card card) { cards.remove(card); }
        
        public void playCard(Card c) {
        
            positionCard(c);
            String cardName = c.getName();
            
            // makis check
            if(cardName.equals("SingleMakiRoll") || cardName.equals("DoubleMakiRoll") || cardName.equals("TripleMakiRoll")) {
                
                slotName = "Makis";
                cards.add(c);
                sortMakis();
            
            // wasabi check (wasabi played on nigiri, don't change the name of the nigiri)
            } else if(cardName.equals("Wasabi") && ! isEmpty()) {
                
                if(slotName.equals("EggNigiri") || slotName.equals("SquidNigiri") || slotName.equals("SalmonNigiri")) {
                 
                    cards.add(c);
                    sortNigiri();
                
                // playing a wasabi on a wasabi, without this, deletes the wasabi being played
                } else if(slotName.equals("Wasabi")) {
                    
                    cards.add(c);
                }
            
            // nigiri check (nigiri played on wasabi, change the slot name and sort)
            } else if((! isEmpty() && slotName.equals("Wasabi")) && (cardName.equals("EggNigiri") || cardName.equals("SquidNigiri") || cardName.equals("SalmonNigiri"))) {
                
                slotName = cardName;
                cards.add(c);
                sortNigiri();
                
            } else {
                
                slotName = cardName;
                cards.add(c);
            }
        }
        
        private void positionCard(Card c) {
            
            c.setX(x);
            int offsetY = 0;
            
            for(int i = 0; i < cards.size(); i++)
                offsetY += ((300 / 12) * scale);
            
            c.setY(y + offsetY);
            
            c.setColor(Color.BLACK);
        }
        
        private void repositionCards() {
            
            int offsetY = 0;
            
            for(Card c : cards) {
                
                c.setY(y + offsetY);
                offsetY += ((300 / 12) * scale);
            }  
        }
        
        public void render(Graphics2D g2) {
            
            g2.setColor(color);
            g2.drawRect(x - (int) (2 * scale), y - (int) (2 * scale), (int) ((100 + 2) * scale), (int) ((300 + 2) * scale));
            
            // following is to make wasabi and chopsticks "blink" to indicate that they can be interacted with
            if(System.currentTimeMillis() - startTime >= 250)
                for(Card c : cards)
                    c.setColor(Color.BLACK);
            
            if(System.currentTimeMillis() - startTime >= 500 && blinking) {
                // contains a wasabi ***
                if(! isEmpty()) {
                    if(slotName.equals("Wasabi"))
                        for(Card c : cards)
                            c.setColor(Color.MAGENTA);
                    else if(slotName.equals("Chopsticks") && blinkingChopsticks)
                        for(Card c : cards)
                            c.setColor(Color.MAGENTA);
                    else if(slotName.equals("EggNigiri") || slotName.equals("SalmonNigiri") || slotName.equals("SquidNigiri"))
                        if(hasUsableWasabi())
                            highlightUsableWasabi();
                }     
                    
                startTime = System.currentTimeMillis();
            }
            
            for(Card c : cards)
                c.render(g2);
        }
        
        /*
            Returns whether or not there are more wasabi in a nigiri slot than nigiri.
            This indicates that if the player has the 
        */
        private boolean hasUsableWasabi() {
            
            int numNigiri = 0;
            int numWasabi = 0;
            
            for(Card c : cards) {
                
                if(c.getName().equals("Wasabi"))
                    numWasabi++;
                else
                    numNigiri++;
            }
            
            return numWasabi > numNigiri;
        }
        
        private void highlightUsableWasabi() {
            
            int numNigiri = 0;
            int numWasabi = 0;
            
            for(Card c : cards) {
                
                if(c.getName().equals("Wasabi"))
                    numWasabi++;
                else
                    numNigiri++;
            }
                        
            for(int i = cards.size() - 1; i > numNigiri; i--)
                cards.get(i).setColor(Color.MAGENTA);
        }
        
        /*
            Sort the maki rolls with largest in the back, smallest in the front. This is not
            set up in the best way possible.
        */
        private void sortMakis() {
                        
            Stack<Card> singleMakiRolls = new Stack<>();
            Stack<Card> doubleMakiRolls = new Stack<>();
            Stack<Card> tripleMakiRolls = new Stack<>();
            
            for(int i = 0; i < cards.size(); i++)
                if(cards.get(i).getName().equals("SingleMakiRoll"))
                    singleMakiRolls.push(new Card(cards.get(i)));

           for(int i = 0; i < cards.size(); i++)
                if(cards.get(i).getName().equals("DoubleMakiRoll"))
                   doubleMakiRolls.push(new Card(cards.get(i)));

            for(int i = 0; i < cards.size(); i++)
               if(cards.get(i).getName().equals("TripleMakiRoll"))
                    tripleMakiRolls.push(new Card(cards.get(i)));
            
            cards = new ArrayList<>();
            
            while(! tripleMakiRolls.isEmpty())
                cards.add(tripleMakiRolls.pop());
            
            while(! doubleMakiRolls.isEmpty()) 
                cards.add(doubleMakiRolls.pop());
            
            while(! singleMakiRolls.isEmpty())
                cards.add(singleMakiRolls.pop());
            
            repositionCards();
        }
        
        /*
            Sorts the nigiri such that there is a single wasabi behind a nigiri card.
        */
        private void sortNigiri() {
            
            Stack<Card> wasabi = new Stack<>();
            Stack<Card> nigiri = new Stack<>();
            
            // pushing on a copy of both the wasabi and nigiri
            for(int i = 0; i < cards.size(); i++) {
                
                if(cards.get(i).getName().equals("Wasabi"))
                    wasabi.push(new Card(cards.get(i)));
                else
                    nigiri.push(new Card(cards.get(i)));
            }
            
            // sorting the wasabi and nigiri cards
            cards = new ArrayList<>();
            
            while(true) {
                
                if(! wasabi.isEmpty())
                    cards.add(wasabi.pop());
                
                if(! nigiri.isEmpty())
                    cards.add(nigiri.pop());
                
                if(wasabi.isEmpty() && nigiri.isEmpty())
                    break;
            }
            
            repositionCards();
        }
        
        public int calculateScore() {
            
            int score = 0;
            
            if(cards.isEmpty())
                return score;
            
            switch(slotName) {
                case "Tempura":
                    score += scoreTempura();
                    break;
                case "Sashimi":
                    score += scoreSashimi();
                    break;
                case "Dumpling":
                    score += scoreDumplings();
                    break;
                case "EggNigiri":
                    score += scoreNigiri();
                    break;
                case "SquidNigiri":
                    score += scoreNigiri();
                    break;
                case "SalmonNigiri":
                    score += scoreNigiri();
                    break;
            }
            
            return score;
        }
        
        public int getMakiAmount() {
            int makiAmount = 0;
            for(Card c : cards) {
                if(c.getName().equals("SingleMakiRoll"))
                    makiAmount++;
                else if(c.getName().equals("DoubleMakiRoll"))
                    makiAmount += 2;
                else if(c.getName().equals("TripleMakiRoll"))
                    makiAmount += 3;
            }
            return makiAmount;   
        }
        
        // each set of 2 tempura scores 5 points
        public int scoreTempura() {
            
            int tempuraAmount = cards.size();
            tempuraAmount /= 2;
            
            int score = tempuraAmount * 5;
            return score;
        }
        
        // each set of 3 sashimi scores 10 points
        public int scoreSashimi() {
            
            int sashimiAmount = cards.size();
            sashimiAmount /= 3;
            
            int score = sashimiAmount * 10;
            return score;
        }
        
        // # dumplings: 1 2 3  4 5+
        // points:      1 3 6 10 15
        public int scoreDumplings() {
            
            int dumplings = cards.size();
            
            switch(dumplings) {
                case 0:
                    return 0;
                case 1:
                    return 1;
                case 2:
                    return 3;
                case 3:
                    return 6;
                case 4:
                    return 10;
                case 5:
                    return 15;
                // > 5 dumplings
                default:
                    return 15;  
            }
        }
        
        // 1 point per egg nigiri, if dipped in wasabi, each is 3 points
        // 2 points per salmon nigiri, if dipped in wasabi, each is 6 points
        // 3 points per squid nigiri, if dipped in wasabi, each is 9 points
        public int scoreNigiri() {
           
            int nigiris = 0;
            int wasabis = 0;
            int score = 0;
            
            for(Card c : cards) {
                
                if(c.getName().equals("EggNigiri") || c.getName().equals("SquidNigiri") || c.getName().equals("SalmonNigiri"))
                    nigiris++;
                else if(c.getName().equals("Wasabi"))
                    wasabis++;
            }
            
            // goofy way of pairing up nigiri and wasabi to score big points
            while(nigiris > 0 && wasabis > 0) {
                switch (slotName) {
                    case "EggNigiri":
                        score += 3;
                        break;
                    case "SalmonNigiri":
                        score += 6;
                        break;
                    case "SquidNigiri":
                        score += 9;
                        break;
                }
                
                nigiris--;
                wasabis--;
            }
            
            // add the remaining nigiri to the score
            switch(slotName) {
                case "EggNigiri":
                    score += nigiris;
                    break;
                case "SalmonNigiri":
                    score += (nigiris * 2);
                    break;
                case "SquidNigiri":
                    score += (nigiris * 3);
                    break;
            }
            
            return score;
        }
        
        public ArrayList<Card> getCards(){
            return cards;
        }
        
        @Override
        public String toString() {
            
            return cards.toString();
        }
    }
}