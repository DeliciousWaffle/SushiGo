package gamestates;

import assets.AssetLoader;
import guielements.Button;
import guielements.Text;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.awt.Point;

public class CreditsState extends GameState {
    
    private Button backButton;
    private Text[] credits;
    
    public CreditsState(GameStateManager gsm) {
        
        this.gameStateManager = gsm;
        backButton = new Button("Back", 70f, 50, 100);
        
        Font font = AssetLoader.jediFont;
        Color color = Color.YELLOW;
        
        // all strings are lowercase because this font is stupid
        credits = new Text[] {
            
            new Text("sushi go!", font, 180f, color, 200, 800),
            new Text("jake bussa alex dermer", font, 70f, color, 130, 900),
            new Text("game's creators", font, 100f, color, 120, 1200),
            new Text("gamewright", font, 70f, color, 400, 1300),
            new Text("images sources", font, 100f, color, 160, 1600),
            new Text("rules page:", font, 50f, color, 160, 1700),
            new Text("https://www.gamewright.com/gamewright/pdfs/Rules/SushiGoTM-RULES.pdf", font, 20f, color, 160, 1750),
            new Text("cards:", font, 50f, color, 160, 1850),
            new Text("http://fathergeek.com/sushi-go/", font, 20f, color, 160, 1900),
            new Text("icons:", font, 50f, color, 160, 2000),
            new Text("http://www.lummoxlabs.com/sushi-go-presskit", font, 20f, color, 160, 2050),
            new Text("fonts used", font, 100f, color, 340, 2350),
            new Text("primary:", font, 50f, color, 160, 2450),
            new Text("https://www.fontspace.com/astigmatic-one-eye-typographic-institute/shojumaru", font, 20f, color, 160, 2500),
            new Text("jedi:", font, 50f, color, 160, 2600),
            new Text("https://www.fontspace.com/boba-fonts/star-jedi", font, 20f, color, 160, 2650),
            new Text("other help", font, 100f, color, 340, 2950),
            new Text("game state manager:", font, 50f, color, 160, 3050),
            new Text("https://www.youtube.com/watch?v=a1fQbc0qa-k", font, 20f, color, 160, 3100),
            new Text("stack overflow:", font, 50f, color, 160, 3200),
            new Text("for pretty much everything else", font, 20f, color, 160, 3250)
        };
        
        for(Text t : credits)
            t.setYVel(-2);
    }

    @Override
    public void update() {
        
        backButton.update();
        
        for(Text t : credits)
            t.update();
    }

    @Override
    public void render(Graphics2D g2) {
        
        g2.drawImage(AssetLoader.starsBackground, 0, 0, 1280, 720, null);
        
        for(Text t : credits)
            t.render(g2);
        
        backButton.render(g2);
    }

    @Override
    public void handleMouseClicked(Point p) {
        
        if(backButton.contains(p)) {
            
            gameStateManager.resetCreditsState();
            gameStateManager.setCurrentState(GameStateManager.MAIN_MENU_STATE);
        }
    }
    
    @Override
    public void handleMouseReleased(Point p) {}

    @Override
    public void handleMouseMoved(Point p) {
        
        backButton.contains(p);
    }

    @Override
    public void handleMouseDragged(Point p) {}
}
