package donato.pablo.rgbrun;

import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.physics.box2d.World;

public class Pattern extends Group implements Cloneable
{
    protected String difficulty;
    protected int index;
    protected boolean adaptive;

    public Pattern(String difficulty, int index)
    {
        this.difficulty = difficulty;
        this.index = index;

        XmlReader reader = new XmlReader();
        Element root = null;
        try {
            root = reader.parse(Gdx.files.internal("patterns.xml"));
        } catch(IOException ioe) {}

        Element pattern = root.getChildByName(difficulty).getChild(index);

        adaptive = pattern.getBoolean("adaptive");
        setWidth(pattern.getFloat("width"));

        for(Element platform : pattern.getChildrenByName("platform")) {
            Vector2 pos = new Vector2();
            pos.x = Float.valueOf(platform.get("pos").split(";")[0]);
            pos.y = Float.valueOf(platform.get("pos").split(";")[1]);

            Vector2 size = new Vector2();
            size.x = Float.valueOf(platform.get("size").split(";")[0]);
            size.y = Float.valueOf(platform.get("size").split(";")[1]);

            Color color = Consts.COLOR_FROM_STR(platform.get("color"));

            addActor(new Platform(pos, size, color));
        }

        for(Element laser : pattern.getChildrenByName("laser")) {
            Vector2 p1 = new Vector2();
            p1.x = Float.valueOf(laser.get("p1").split(";")[0]);
            p1.y = Float.valueOf(laser.get("p1").split(";")[1]);

            Vector2 p2 = new Vector2();
            p2.x = Float.valueOf(laser.get("p2").split(";")[0]);
            p2.y = Float.valueOf(laser.get("p2").split(";")[1]);

            Color color = Consts.COLOR_FROM_STR(laser.get("color"));

            addActor(new Laser(p1, p2, color));
        }

        setX(0);
    }

    @Override
        public void act(float delta)
        {
            super.act(delta);

            ArrayList<Float> obstaclesX = new ArrayList<Float>();
            for(Actor obstacle : getChildren()) {
                obstaclesX.add(obstacle.getX());
            }
            Collections.sort(obstaclesX);
            setX(obstaclesX.get(0));
        }

    public void instanciate(World world)
    {
        for(Actor actor : getChildren()) {
            Entity obstacle = (Entity)actor;
            obstacle.bodyDef.position.x += getX();
            obstacle.bodyDef.position.y += Consts.BOT_SIZE;
            obstacle.instanciate(world);
        }
    }

    public void move(float vel)
    {
        for(Actor actor : getChildren()) {
            Entity obstacle = (Entity)actor;
            obstacle.move(vel);
        }
    }

    public Pattern clone()
    {
        return new Pattern(difficulty, index);
    }

    public void randomizeColors(Color charColor)
    {
        ArrayList<Color> rgb = new ArrayList<Color>(Arrays.asList(Consts.RGB_ARRAY));
        ArrayList<Color> colors = new ArrayList<Color>();

        if(adaptive) {
            colors = rgb;
            Collections.rotate(colors, -rgb.indexOf(charColor));
        }
        else {
            for(int i = 3 ; i > 0 ; i--) {
                Color color = rgb.get((int)(Math.random()*i));
                colors.add(color);
                rgb.remove(color);
            }
        }

        for(Actor obstacle : getChildren()) {
            if(obstacle.getColor().equals(Consts.RGB.get(0))) {
                obstacle.setColor(colors.get(0));
            }
            else if(obstacle.getColor().equals(Consts.RGB.get(1))) {
                obstacle.setColor(colors.get(1));
            }
            else if(obstacle.getColor().equals(Consts.RGB.get(2))) {
                obstacle.setColor(colors.get(2));
            }
        }
    }

    public void setAdaptive(boolean adaptive)
    {
        this.adaptive = adaptive;
    }
}
