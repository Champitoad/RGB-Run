package donato.pablo.rgbrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class RectEntity extends Entity
{
    protected FixtureDef fixtureDef;
    protected Fixture fixture;

    public RectEntity(BodyType bodyType, Vector2 pos, Vector2 size, Color color)
    {
        super(bodyType, pos, size, color);

        bodyDef.position.x += getWidth()/2;
        bodyDef.position.y += getHeight()/2;

        PolygonShape rect = new PolygonShape();
        rect.setAsBox(getWidth()/2, getHeight()/2);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = rect;
        fixtureDef.friction = 0;
    }

    @Override
        public void act(float delta)
        {
            super.act(delta);
            setPosition(getX()-getWidth()/2, getY()-getHeight()/2);
        }

    @Override
        public void draw(Batch batch, float parentAlpha)
        {
            renderer.setProjectionMatrix(batch.getProjectionMatrix());
            renderer.setTransformMatrix(batch.getTransformMatrix());
            renderer.translate(getX()*Consts.SCALE, getY()*Consts.SCALE, 0);
            renderer.begin(ShapeType.Filled);
            renderer.setColor(getColor());
            renderer.rect(0, 0, getWidth()*Consts.SCALE, getHeight()*Consts.SCALE);
            renderer.end();
        }

    @Override
        public void instanciate(World world)
        {
            super.instanciate(world);
            fixture = body.createFixture(fixtureDef);
        }
}
