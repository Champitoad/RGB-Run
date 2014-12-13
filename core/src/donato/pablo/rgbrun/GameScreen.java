package donato.pablo.rgbrun;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen extends InputAdapter implements Screen, EventListener
{
    protected final RGBRun game;

    protected float score;
    protected int record;
    protected boolean pause;

    protected Stage gui;
    protected Label scoreLabel, recordLabel;
    protected ImageButton pauseButton;
    protected TextButton exitButton;

    protected Map map;
    protected OrthographicCamera worldcam;
    protected ArrayList<Color> charColors;

    public GameScreen(final RGBRun game)
    {
        this.game = game;

        score = 0f;
        record = game.scores.getInteger(Consts.FETCH_RECORD_KEY(game), 0);
        pause = false;

        scoreLabel = new Label(""+(int)score, game.uiskin, "light");
        scoreLabel.setFontScale(0.5f);
        scoreLabel.pack();
        Consts.MOVE(scoreLabel, "title");

        recordLabel = new Label(""+record, game.uiskin, "light");
        recordLabel.setFontScale(0.45f);
        recordLabel.pack();
        recordLabel.setColor(Consts.COLOR_FROM_STR("yellow"));
        Consts.MOVE(recordLabel, "top_left");

        exitButton = new TextButton("EXIT", game.uiskin, "light");
        exitButton.getLabel().setFontScale(0.5f);
        exitButton.pack();
        Consts.MOVE(exitButton, "top_left");
        exitButton.setVisible(false);
        exitButton.addListener(this);

        SpriteDrawable pause = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("pause.png"))));
        SpriteDrawable play = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("play.png"))));
        SpriteDrawable pauseOver = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("pause_over.png"))));
        SpriteDrawable playOver = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("play_over.png"))));
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = pause;
        style.imageOver = pauseOver;
        style.imageChecked = play;
        style.imageCheckedOver = playOver;
        pauseButton = new ImageButton(style);
        pauseButton.pack();
        Consts.MOVE(pauseButton, "top_right");
        pauseButton.addListener(this);

        gui = new Stage(new ScreenViewport(game.guicam));
        gui.addActor(scoreLabel);
        gui.addActor(recordLabel);
        gui.addActor(exitButton);
        gui.addActor(pauseButton);

        worldcam = new OrthographicCamera();
        worldcam.setToOrtho(false, Consts.WORLD_WIDTH, Consts.WORLD_HEIGHT);

        map = new Map(new ScreenViewport(worldcam), game);

        charColors = new ArrayList<Color>(Arrays.asList(Consts.RGB_ARRAY));

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gui);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
        public void render(float delta)
        {
            if(!pause) {
                // Update score
                score += delta*12;
                scoreLabel.setText(""+(int)score);
                scoreLabel.pack();
                Consts.MOVE(scoreLabel, "title");
                if((int)score == record) {
                    scoreLabel.setColor(Consts.COLOR_FROM_STR("yellow"));
                }
                // Right part of the screen being touched : character jumps
                if(Gdx.input.isTouched()) {
                    if(Gdx.input.getY() >= Consts.TOP_SIZE*Consts.SCALE) {
                        if(Gdx.input.getX() >= Consts.SCR_WIDTH/2) {
                            map.character.jump(27);
                        }
                    }
                }
                // Physics
                map.act(delta);
            }

            /* Drawing */

            // White background
            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            // Lasers thickness in pixels
            Gdx.gl.glLineWidth(5);
            // Update world camera
            worldcam.update();
            game.batch.setProjectionMatrix(worldcam.combined);
            // GUI over map
            map.draw();
            gui.draw();
        }

    @Override
        public boolean touchDown(int x, int y, int pointer, int button)
        {
            // Do nothing if pause
            if(pause) {
                return true;
            }
            if(y >= Consts.TOP_SIZE*Consts.SCALE) {
                // Right part of the screen : character jumps
                if(x >= Consts.SCR_WIDTH/2) { 
                    map.character.jump(27);
                }
                // Left part of the screen : change character's color
                else { 
                    Collections.rotate(charColors, -1);
                    map.character.setColor(charColors.get(0));
                }
            }
            return true;
        }

    // Handle buttons
    @Override
        public boolean handle(Event event)
        {
            if(pauseButton.isChecked()) {
                pause = true;
                recordLabel.setVisible(false);
                exitButton.setVisible(true);
            }
            else {
                pause = false;
                exitButton.setVisible(false);
                recordLabel.setVisible(true);
            }
            if(exitButton.isChecked()) {
                game.setScreen(game.mainMenuScreen);
            }
            return true;
        }

    @Override
        public void resize(int width, int height) {}
    @Override
        public void show() {}
    @Override
        public void hide() {}
    @Override
        public void pause() {}
    @Override
        public void resume() {}
    @Override
        public void dispose() {}

    public int getScore()
    {
        return (int)score;
    }
}
