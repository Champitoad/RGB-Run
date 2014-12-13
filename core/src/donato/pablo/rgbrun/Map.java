package donato.pablo.rgbrun;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.physics.box2d.World;

public class Map extends Stage
{
    protected final RGBRun game;

    protected World world;
    public Character character;

    protected ArrayList<Pattern> patterns;
    protected HashMap<String, ArrayList<Pattern>> patternsDefinitions;
    protected int patternsDone;

    protected String difficulty;

    public Map(Viewport viewport, RGBRun game)
    {
        super(viewport, game.batch);
        this.game = game;

        difficulty = Consts.EASY;

        world = new World(new Vector2(0, -100), true);
        world.setContactListener(new CollisionManager(game));

        character = new Character();

        game.bot.instanciate(world);
        game.top.instanciate(world);
        character.instanciate(world);

        addActor(character);
        addActor(game.bot);
        addActor(game.top);

        // Patterns definitions

        patterns = new ArrayList<Pattern>();
        patternsDefinitions = new HashMap<String, ArrayList<Pattern>>();
        patternsDefinitions.put(Consts.EASY, new ArrayList<Pattern>());
        patternsDefinitions.put(Consts.MEDIUM, new ArrayList<Pattern>());
        patternsDefinitions.put(Consts.HARD, new ArrayList<Pattern>());
        patternsDone = 0;

        XmlReader reader = new XmlReader();
        Element root = null;
        try {
            root = reader.parse(Gdx.files.internal("patterns.xml"));
        } catch(IOException ioe) {}

        // Easy
        for(int index = 0 ; index < root.getChildByName(Consts.EASY).getChildCount(); index++) {
            patternsDefinitions.get(Consts.EASY).add(new Pattern(Consts.EASY, index));
        }
        // Medium
        for(int index = 0 ; index < root.getChildByName(Consts.MEDIUM).getChildCount(); index++) {
            patternsDefinitions.get(Consts.MEDIUM).add(new Pattern(Consts.MEDIUM, index));
        }
        // Hard
        for(int index = 0 ; index < root.getChildByName(Consts.HARD).getChildCount(); index++) {
            patternsDefinitions.get(Consts.HARD).add(new Pattern(Consts.HARD, index));
        }

        patterns.add(randomPattern(Consts.WORLD_WIDTH/2, difficulty));
    }

    @Override
        public void act(float delta)
        {
            super.act(delta);

            // Scrolling
            for(Pattern pattern : patterns) {
                pattern.move(-12);
                pattern.act(delta);
            }

            // Random generation
            Pattern firstPattern = patterns.get(0);
            Pattern lastPattern = patterns.get(patterns.size()-1);
            if(firstPattern.getX() <= -firstPattern.getWidth()) {
                patterns.remove(firstPattern);
            }
            if(lastPattern.getX() <= Consts.WORLD_WIDTH-lastPattern.getWidth()) {
                Pattern newPattern = randomPattern(Consts.WORLD_WIDTH, difficulty);
                patterns.add(newPattern);
                patternsDone++;
            }

            // Difficulty progression 
            if(patternsDone == 10) {
                difficulty = Consts.MEDIUM;
            }
            if(patternsDone == 20) {
                difficulty = Consts.HARD;
            }

            // Game over if character out of screen
            if(character.getX() < 0) {
                game.over();
            }

            // Physics simulation
            world.step(1/60f, 8, 3);
        }

    @Override
        public void draw()
        {
            for(Pattern pattern : patterns) {
                pattern.draw(game.batch, 1);
            }
            super.draw();
        }

    public Pattern randomPattern(float x, String difficulty)
    {
        int randIndex = (int)(Math.random()*patternsDefinitions.get(difficulty).size());
        Pattern pattern = patternsDefinitions.get(difficulty).get(randIndex).clone();

        if(game.options.getBoolean("hardcore", false)) {
            pattern.setAdaptive(false);
        }

        pattern.randomizeColors(character.getColor());
        pattern.setX(x);
        pattern.instanciate(world);
        pattern.act(1f/60f);

        return pattern;
    }
}
