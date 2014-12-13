package donato.pablo.rgbrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MainMenuScreen extends MenuScreen
{
    protected TextButton startButton, hardcoreButton;

    public MainMenuScreen(final RGBRun game)
    {
        super(game, "RGB RUN", "QUIT");

        // Widgets

        startButton = new TextButton("START", game.uiskin, "dark-red-down");
        startButton.getLabel().setFontScale(1f);
        startButton.pack();
        startButton.addListener(this);

        hardcoreButton = new TextButton("HARDCORE", game.uiskin, "light");
        hardcoreButton.getLabel().setFontScale(0.50f);
        hardcoreButton.pack();
        Consts.MOVE(hardcoreButton, "top_right");
        hardcoreButton.addListener(this);

        updateGUI();

        // Layouts

        mainLayout.add(startButton);

        // GUI

        gui.addActor(hardcoreButton);
    }

    @Override
        public boolean handle(Event event)
        {
            // Start new game if start button pressed
            if(startButton.isChecked()) {
                game.gameScreen = new GameScreen(game);
                game.setScreen(game.gameScreen);
                startButton.toggle();
            }

            // Toggle hardcore mode if hardcore button pressed
            if(hardcoreButton.isChecked()) {
                game.options.putBoolean("hardcore", true);
            }
            else {
                game.options.putBoolean("hardcore", false);
            }

            // Quit game if back button pressed
            if(backButton.isChecked()) {
                Gdx.app.exit();
            }

            game.options.flush();
            updateGUI();

            return true;
        }

    @Override
        public void show()
        {
            super.show();
            updateGUI();
        }

    // Update GUI with options from preferences
    protected void updateGUI()
    {
        boolean hardcore = game.options.getBoolean("hardcore", false);
        if(hardcore) {
            hardcoreButton.setText("HARDCORE");
        }
        else {
            hardcoreButton.setText("NORMAL");
        }
        hardcoreButton.setChecked(hardcore);
    }
}
