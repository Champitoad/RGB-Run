package donato.pablo.rgbrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen, EventListener
{
    protected final RGBRun game;

    protected Stage gui;
    protected Label titleLabel;
    protected TextButton backButton;

    protected Table mainLayout;

    public MenuScreen(final RGBRun game, String title, String backButtonText)
    {
        this.game = game;

        // Widgets

        titleLabel = new Label(title, game.uiskin, "light");
        titleLabel.setFontScale(0.8f);
        titleLabel.pack();
        Consts.MOVE(titleLabel, "title");

        backButton = new TextButton(backButtonText, game.uiskin, "light");
        backButton.getLabel().setFontScale(0.5f);
        backButton.pack();
        Consts.MOVE(backButton, "top_left");
        backButton.addListener(this);

        // Main layout

        mainLayout = new Table();
        mainLayout.setFillParent(true);

        // GUI

        gui = new Stage(new ScreenViewport(game.guicam));
        gui.addActor(titleLabel);
        gui.addActor(backButton);
        gui.addActor(mainLayout);
        Gdx.input.setInputProcessor(gui);
    }

    @Override
        public void render(float delta)
        {
            // White background
            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            // Bot and top in background
            game.bot.draw(game.batch, 1);
            game.top.draw(game.batch, 1);
            // GUI
            gui.draw();
        }

    // Handle buttons
    @Override
        public boolean handle(Event event)
        {
            return true;
        }

    @Override
        public void resize(int width, int height) {}
    @Override
        public void show()
        {
            Gdx.input.setInputProcessor(gui);
        }
    @Override
        public void hide() {}
    @Override
        public void pause() {}
    @Override
        public void resume() {}
    @Override
        public void dispose() {}
}
