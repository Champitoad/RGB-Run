package donato.pablo.rgbrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOverScreen implements Screen, EventListener
{
    protected final RGBRun game;

    protected Stage gui;
    protected Label gameOverLabel, scoreLabel, recordLabel;
    protected TextButton retryButton, menuButton;

    public GameOverScreen(final RGBRun game, int score)
    {
        this.game = game;

        String recordKey = Consts.FETCH_RECORD_KEY(game);
        int record = game.scores.getInteger(recordKey, 0);

        // Widgets

        gameOverLabel = new Label("GAME OVER", game.uiskin, "light");
        gameOverLabel.setFontScale(1f);
        gameOverLabel.pack();

        scoreLabel = new Label("SCORE : "+score, game.uiskin, "light");
        scoreLabel.setFontScale(0.5f);
        scoreLabel.pack();

        recordLabel = new Label("RECORD : "+record, game.uiskin, "light");
        recordLabel.setFontScale(0.5f);
        recordLabel.pack();

        // Write new record if necessary
        if(score > record) {
            game.scores.putInteger(recordKey, score);
            game.scores.flush();
            recordLabel.setText("NEW RECORD!");
            recordLabel.setColor(Consts.COLOR_FROM_STR("yellow"));
        }

        retryButton = new TextButton("RETRY", game.uiskin, "light");
        retryButton.getLabel().setFontScale(0.75f);
        retryButton.pack();
        retryButton.addListener(this);

        menuButton = new TextButton("MENU", game.uiskin, "light");
        menuButton.getLabel().setFontScale(0.75f);
        menuButton.pack();
        menuButton.addListener(this);

        // Layouts

        HorizontalGroup buttonsLayout = new HorizontalGroup();
        buttonsLayout.addActor(retryButton);
        buttonsLayout.addActor(menuButton);
        buttonsLayout.space(50);

        Table mainLayout = new Table();
        mainLayout.setFillParent(true);
        mainLayout.add(gameOverLabel).space(100);
        mainLayout.row();
        mainLayout.add(scoreLabel).space(10);
        mainLayout.row();
        mainLayout.add(recordLabel).space(10);
        mainLayout.row();
        mainLayout.add(buttonsLayout).space(100);

        // GUI

        gui = new Stage(new ScreenViewport(game.guicam));
        gui.addActor(mainLayout);
        Gdx.input.setInputProcessor(gui);
    }

    @Override
        public void render(float delta)
        {
            // Gray background
            float g = Consts.COLOR_FROM_STR("gray").r; 
            Gdx.gl.glClearColor(g, g, g, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            // GUI
            gui.draw();
        }

    // Handle buttons
    @Override
        public boolean handle(Event event)
        {
            // Launch new game if retry button pressed
            if(retryButton.isChecked()) {
                game.gameScreen = new GameScreen(game);
                game.setScreen(game.gameScreen);
            }
            // Show main menu screen if menu button pressed
            if(menuButton.isChecked()) {
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
}
