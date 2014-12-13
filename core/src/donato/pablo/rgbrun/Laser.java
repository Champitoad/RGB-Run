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
import com.badlogic.gdx.physics.box2d.EdgeShape;

public class Laser extends Entity
{
    protected FixtureDef fixtureDef;
    protected Fixture fixture;

    protected Vector2 p1l, p2l; // Current local positions

    public Laser(Vector2 p1, Vector2 p2, Color color)
    {
        super(BodyType.KinematicBody, new Vector2(0, 0), new Vector2(0, 0), color);

        Vector2 pos = new Vector2();
        pos.x = (p1.x+p2.x)/2;
        pos.y = (p1.y+p2.y)/2;
        setPosition(pos.x, pos.y);

        Vector2 size = new Vector2();
        size.x = Math.abs(p2.x-p1.x);
        size.y = Math.abs(p2.y-p1.y);
        setSize(size.x, size.y);

        p1l = new Vector2();
        p1l.x = p1.x-getX();
        p1l.y = p1.y-getY();

        p2l = new Vector2();
        p2l.x = p2.x-getX();
        p2l.y = p2.y-getY();

        bodyDef.position.x = getX()-Consts.WORLD_WIDTH/2;
        bodyDef.position.y = getY()-Consts.WORLD_HEIGHT/2;

        EdgeShape edge = new EdgeShape();
        edge.set(p1l.x, p1l.y, p2l.x, p2l.y);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = edge;
    }

    @Override
        public void draw(Batch batch, float parentAlpha)
        {
            renderer.setProjectionMatrix(batch.getProjectionMatrix());
            renderer.setTransformMatrix(batch.getTransformMatrix());
            renderer.translate(getX()*Consts.SCALE, getY()*Consts.SCALE, 0);
            renderer.begin(ShapeType.Line);
            renderer.setColor(getColor());
            renderer.line(p1l.x*Consts.SCALE, p1l.y*Consts.SCALE, p2l.x*Consts.SCALE, p2l.y*Consts.SCALE);
            renderer.end();
        }

    @Override
        public void instanciate(World world)
        {
            super.instanciate(world);
            fixture = body.createFixture(fixtureDef);
        }
}
