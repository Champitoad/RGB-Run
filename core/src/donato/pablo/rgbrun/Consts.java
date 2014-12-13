package donato.pablo.rgbrun;

import java.util.Arrays;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Consts
{
    // Screen resolution (pixels)
    public static final float SCR_WIDTH = Gdx.graphics.getWidth();
    public static final float SCR_HEIGHT = Gdx.graphics.getHeight();

    // World dimensions (meters)
    public static final float WORLD_WIDTH = 32f;
    public static final float WORLD_HEIGHT = 18f;

    // Pixels/Meters scale factor 
    public static final float SCALE = 60f;

    // Bot and top properties (meters)
    public static final float BOT_SIZE = WORLD_HEIGHT/6f;
    public static final float BOT = 0;
    public static final float TOP_SIZE = WORLD_HEIGHT/6f;
    public static final float TOP = WORLD_HEIGHT-TOP_SIZE;

    // Difficulties
    public static final String EASY = "easy";
    public static final String MEDIUM = "medium";
    public static final String HARD = "hard";
    public static final String[] DIFFICULTIES_ARRAY =
    {EASY, MEDIUM, HARD};
    public static final ArrayList<String> DIFFICULTIES = new ArrayList<String>(Arrays.asList(DIFFICULTIES_ARRAY));

    // Colors lists
    public static final Color[] RGB_ARRAY = {Color.RED, new Color(0, 0.75f, 0, 1), Color.BLUE};
    public static final ArrayList<Color> RGB = new ArrayList<Color>(Arrays.asList(RGB_ARRAY));
    public static Color COLOR_FROM_STR(String colorStr)
    {
        switch(colorStr) {
            case "red" :
                return RGB.get(0);
            case "green" :
                return RGB.get(1);
            case "blue" :
                return RGB.get(2);
            case "gray" :
                return Color.GRAY;
            case "black" :
                return Color.BLACK;
            case "yellow" :
                return Color.YELLOW;
            default :
                return new Color(0, 0, 0, 0);
        }
    }

    // Widgets repositioning method
    public static void MOVE(Actor widget, String pos)
    {
        switch(pos) {
            case "title" :
                widget.setX(SCR_WIDTH/2-widget.getWidth()/2);
                widget.setY((TOP+TOP_SIZE/2)*SCALE-widget.getHeight()/2);
                break;
            case "top_left" :
                widget.setY((TOP+TOP_SIZE/2)*SCALE-widget.getHeight()/2);
                widget.setX(widget.getY()-TOP*SCALE);
                break;
            case "top_right" :
                widget.setY((TOP+TOP_SIZE/2)*SCALE-widget.getHeight()/2);
                widget.setX(SCR_WIDTH-(widget.getY()-TOP*SCALE)-widget.getWidth());
                break;
            case "bot_right" :
                widget.setY(BOT_SIZE/2*SCALE-widget.getHeight()/2);
                widget.setX(SCR_WIDTH-widget.getY()-widget.getWidth());
                break;
            default :
                return;
        }
    }

    // Fetch record key from current game options
    public static String FETCH_RECORD_KEY(RGBRun game)
    {
        String recordKey = "record-";
        if(game.options.getBoolean("hardcore", false)) {
            recordKey += "hardcore";
        }
        else {
            recordKey += "normal";
        }
        return recordKey;
    }
}
