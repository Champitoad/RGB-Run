package donato.pablo.rgbrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Entity extends Actor
{
    protected ShapeRenderer renderer;

    public BodyDef bodyDef;
    protected Body body;

    public Entity(BodyType bodyType, Vector2 pos, Vector2 size, Color color)
    {
        renderer = new ShapeRenderer();

        setPosition(pos.x, pos.y);
        setSize(size.x, size.y);
        setColor(color);

        bodyDef = new BodyDef();
        bodyDef.position.x = getX()-Consts.WORLD_WIDTH/2;
        bodyDef.position.y = getY()-Consts.WORLD_HEIGHT/2;
        bodyDef.type = bodyType;
        bodyDef.fixedRotation = true;
    }

    @Override
        public void act(float delta)
        {
            super.act(delta);
            float x = body.getPosition().x+Consts.WORLD_WIDTH/2;
            float y = body.getPosition().y+Consts.WORLD_HEIGHT/2;
            setPosition(x, y);
        }

    public void instanciate(World world)
    {
        body = world.createBody(bodyDef);
        body.setUserData(this);
    }

    public void move(float vel)
    {
        body.setLinearVelocity(vel, 0);
    }
}
