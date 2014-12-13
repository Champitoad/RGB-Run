package donato.pablo.rgbrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;

public class Character extends RectEntity
{
    protected boolean canJump;
    protected int jumpTimeout;

    protected FixtureDef groundSensorDef;
    protected Fixture groundSensor;

    public Character()
    {
        super(
            BodyType.DynamicBody,
            new Vector2(Consts.WORLD_WIDTH/4f, Consts.BOT+Consts.BOT_SIZE),
            new Vector2(1.5f, 1.5f),
            Consts.RGB.get(0)
        );

        canJump = true;
        jumpTimeout = 10;

        EdgeShape botEdge = new EdgeShape();
        float w = getWidth()/2;
        botEdge.set(-w, -w, w, -w);

        groundSensorDef = new FixtureDef();
        groundSensorDef.shape = botEdge;
        groundSensorDef.isSensor = true;
    }

    @Override
        public void act(float delta)
        {
            super.act(delta);
            if(jumpTimeout > 0) {
                jumpTimeout -= 1;
            }
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }

    @Override
        public void instanciate(World world)
        {
            super.instanciate(world);
            groundSensor = body.createFixture(groundSensorDef);
        }

    public void jump(float vel)
    {
        if(canJump) {
            body.setLinearVelocity(0, vel);
            canJump = false;
            jumpTimeout = 10;
        }
    }

    public int getJumpTimeout()
    {
        return jumpTimeout;
    }

    public boolean getCanJump()
    {
        return canJump;
    }

    public void setCanJump(boolean canJump)
    {
        this.canJump = canJump;
    }
}
