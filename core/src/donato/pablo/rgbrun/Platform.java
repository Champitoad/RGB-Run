package donato.pablo.rgbrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Platform extends RectEntity
{
    public Platform(Vector2 pos, Vector2 size, Color color)
    {
        super(BodyType.KinematicBody, pos, size, color);
    }
}
