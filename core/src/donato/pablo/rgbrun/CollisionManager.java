package donato.pablo.rgbrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionManager implements ContactListener
{
    protected RGBRun game;

    protected Character character;
    protected Fixture charFixture;
    protected Entity obstacle;

    public CollisionManager(RGBRun game)
    {
        this.game = game;
    }

    // Gets character and obstacle references and returns true, or false if none of the colliding entities is a character
    protected boolean updateFromContact(Contact contact)
    {
        Entity entityA = (Entity)contact.getFixtureA().getBody().getUserData();
        Entity entityB = (Entity)contact.getFixtureB().getBody().getUserData();

        if(entityA instanceof Character) {
            character = (Character)entityA;
            charFixture = contact.getFixtureA();
            obstacle = entityB;
        }
        else if(entityB instanceof Character) {
            character = (Character)entityB;
            charFixture = contact.getFixtureB();
            obstacle = entityA;
        }
        else {
            return false;
        }

        return true;
    }

    @Override
        public void beginContact(Contact contact)
        {
            if(!updateFromContact(contact) || !charFixture.isSensor()) {
                return;
            }

            // Reset jump if character is touching ground
            if(character.getJumpTimeout() == 0) {
                character.setCanJump(true);
            }
        }

    @Override
        public void endContact(Contact contact) {}

    @Override
        public void preSolve(Contact contact, Manifold oldManifold)
        {
            if(!updateFromContact(contact)) {
                return;
            }

            // Color-based collision management
            if(obstacle instanceof Platform) {
                if(obstacle.getColor().equals(Consts.COLOR_FROM_STR("black"))) {
                    game.over();
                }
                else if(!character.getColor().equals(obstacle.getColor()) && Consts.RGB.contains(obstacle.getColor())) {
                    contact.setEnabled(false);
                }
            }
            else if(obstacle instanceof Laser) {
                if(character.getColor().equals(obstacle.getColor())) {
                    contact.setEnabled(false);
                }
                else {
                    game.over();
                }
            }
        }

    @Override
        public void postSolve(Contact contact, ContactImpulse contactImpulse) {}
}
