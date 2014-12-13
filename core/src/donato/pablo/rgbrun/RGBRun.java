package donato.pablo.rgbrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.physics.box2d.World;

public class RGBRun extends Game
{
    protected boolean over;

    public Preferences scores, options;

    public SpriteBatch batch;
    public BitmapFont font;
    public Skin uiskin;

    public OrthographicCamera guicam;

    public Platform bot, top;

    public MainMenuScreen mainMenuScreen;
    public GameScreen gameScreen;
    public GameOverScreen gameOverScreen;

    @Override
        public void create()
        {
            over = false;

            scores = Gdx.app.getPreferences("scores");
            options = Gdx.app.getPreferences("options");

            batch = new SpriteBatch();

            // Game font

            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = 128;
            font = generator.generateFont(parameter);
            generator.dispose();

            // Label styles

            LabelStyle labelStyle = new LabelStyle();
            labelStyle.font = font;

            LabelStyle labelLight = new LabelStyle(labelStyle);
            labelLight.fontColor = Color.WHITE;

            LabelStyle labelDark = new LabelStyle(labelStyle);
            labelDark.fontColor = Color.GRAY;

            // TextButton styles

            TextButtonStyle textButtonStyle = new TextButtonStyle();
            textButtonStyle.font = font;

            TextButtonStyle textButtonLight = new TextButtonStyle(textButtonStyle);
            textButtonLight.fontColor = Color.WHITE;
            textButtonLight.downFontColor = Consts.COLOR_FROM_STR("yellow");

            TextButtonStyle textButtonDark = new TextButtonStyle(textButtonStyle);
            textButtonDark.fontColor = Color.GRAY;

            TextButtonStyle textButtonDarkRedDown = new TextButtonStyle(textButtonDark);
            textButtonDarkRedDown.downFontColor = Consts.RGB.get(0);
            TextButtonStyle textButtonDarkGreenDown = new TextButtonStyle(textButtonDark);
            textButtonDarkGreenDown.downFontColor = Consts.RGB.get(1);
            TextButtonStyle textButtonDarkBlueDown = new TextButtonStyle(textButtonDark);
            textButtonDarkBlueDown.downFontColor = Consts.RGB.get(2);

            TextButtonStyle textButtonDarkRedChecked = new TextButtonStyle(textButtonDark);
            textButtonDarkRedChecked.checkedFontColor = Consts.RGB.get(0);
            TextButtonStyle textButtonDarkGreenChecked = new TextButtonStyle(textButtonDark);
            textButtonDarkGreenChecked.checkedFontColor = Consts.RGB.get(1);
            TextButtonStyle textButtonDarkBlueChecked = new TextButtonStyle(textButtonDark);
            textButtonDarkBlueChecked.checkedFontColor = Consts.RGB.get(2);

            TextButtonStyle textButtonOrange = new TextButtonStyle(textButtonStyle);
            textButtonOrange.fontColor = Color.ORANGE;

            // GUI skin

            uiskin = new Skin();
            uiskin.add("default", labelStyle);
            uiskin.add("light", labelLight);
            uiskin.add("dark", labelDark);
            uiskin.add("default", textButtonStyle);
            uiskin.add("light", textButtonLight);
            uiskin.add("dark", textButtonDark);
            uiskin.add("dark-red-down", textButtonDarkRedDown);
            uiskin.add("dark-green-down", textButtonDarkGreenDown);
            uiskin.add("dark-blue-down", textButtonDarkBlueDown);
            uiskin.add("dark-red-checked", textButtonDarkRedChecked);
            uiskin.add("dark-green-checked", textButtonDarkGreenChecked);
            uiskin.add("dark-blue-checked", textButtonDarkBlueChecked);
            uiskin.add("orange", textButtonOrange);

            // GUI camera

            guicam = new OrthographicCamera();
            guicam.setToOrtho(false, Consts.SCR_WIDTH, Consts.SCR_HEIGHT);

            // Declare bot and top for further use as platforms in Map or GUI elements in menus

            new World(new Vector2(0f,0f), false); // Loads Box2D libs to be able to create platforms before the map is created
            bot = new Platform(new Vector2(0, Consts.BOT),
                    new Vector2(Consts.WORLD_WIDTH,Consts.BOT_SIZE),
                    Consts.COLOR_FROM_STR("gray"));
            top = new Platform(new Vector2(0, Consts.TOP),
                    new Vector2(Consts.WORLD_WIDTH, Consts.TOP_SIZE),
                    Consts.COLOR_FROM_STR("gray"));

            // Show main menu screen

            mainMenuScreen = new MainMenuScreen(this);
            this.setScreen(mainMenuScreen);
        }

    @Override
        public void render()
        {
            super.render();

            guicam.update();

            if(over) {
                gameOverScreen = new GameOverScreen(this, gameScreen.getScore());
                this.setScreen(gameOverScreen);
                over = false;
            }
        }

    @Override
        public void dispose()
        {
            batch.dispose();
            font.dispose();
        }

    public void over()
    {
        over = true;
    }
}
