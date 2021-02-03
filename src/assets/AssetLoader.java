package assets;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

public class AssetLoader {
    
    public static BufferedImage tempura, sashimi, dumpling, singleMakiRoll, doubleMakiRoll, tripleMakiRoll, salmonNigiri, squidNigiri, eggNigiri, pudding, wasabi, chopsticks;
    public static BufferedImage menuBackground, starsBackground, tableBackground, carpetBackground, blackBackground;
    public static BufferedImage[] rulesPages;
    public static BufferedImage quickGameSummary;
    public static BufferedImage dumplingIcon, makiRollIcon, eggNigiriIcon, salmonNigiriIcon, squidNigiriIcon, sashimiIcon, tempuraIcon, wasabiIcon, chopsticksIcon, puddingIcon, arrowIconLeft, arrowIconRight;
    public static Font font, jediFont;
    
    public AssetLoader() {
        
        try {
            
            tempura = getImage("tempura.jpg");
            sashimi = getImage("sashimi.jpg");
            dumpling = getImage("dumpling.jpg");
            singleMakiRoll = getImage("singlemakiroll.jpg");
            doubleMakiRoll = getImage("doublemakiroll.jpg");
            tripleMakiRoll = getImage("triplemakiroll.jpg");
            salmonNigiri = getImage("salmonnigiri.jpg");
            squidNigiri = getImage("squidnigiri.jpg");
            eggNigiri = getImage("eggnigiri.jpg");
            pudding = getImage("pudding.jpg");
            wasabi = getImage("wasabi.jpg");
            chopsticks = getImage("chopsticks.jpg");
            
            menuBackground = getImage("menubackground.jpg");
            starsBackground = getImage("starsbackground.png");
            tableBackground = getImage("tablebackground.png");
            carpetBackground = getImage("carpetbackground.png");
            blackBackground = getImage("blackbackground.jpg");
            
            rulesPages = new BufferedImage[6];
            for(int i = 0; i < rulesPages.length; i++)
                rulesPages[i] = getImage("rulespage" + i + ".PNG");
            quickGameSummary = getImage("quickgamesummary.PNG");
            
            dumplingIcon = getImage("dumplingicon.png");
            makiRollIcon = getImage("makirollicon.png");
            eggNigiriIcon = getImage("eggnigiriicon.png");
            salmonNigiriIcon = getImage("salmonnigiriicon.png");
            squidNigiriIcon = getImage("squidnigiriicon.png");
            sashimiIcon = getImage("sashimiicon.png");
            tempuraIcon = getImage("tempuraicon.png");
            wasabiIcon = getImage("wasabiicon.png");
            chopsticksIcon = getImage("chopsticksicon.png");
            puddingIcon = getImage("puddingicon.png");
            arrowIconLeft = getImage("arrowiconleft.png");
            arrowIconRight = getImage("arrowiconright.png");
            
        } catch(Exception e) {
            
            e.printStackTrace();
            System.exit(-1);
        
        } try {
            
            font = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("assets/shojumarufont.ttf"));
            jediFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("assets/jedifont.ttf"));
            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            
            ge.registerFont(font);
            ge.registerFont(jediFont);
            
        } catch(Exception e) {
            
            e.printStackTrace();
            System.exit(-1);
        }
    }   
    
    private BufferedImage getImage(String filename) throws Exception {
        
        return ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/" + filename));
    }
}