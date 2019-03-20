package customs;

import android.content.Context;
import android.graphics.Typeface;

public class CustomFonts {
    private Typeface regular = null;
    private Typeface bold = null;
    private Typeface light = null;
    private Typeface medium = null;

    private static CustomFonts customFonts = new CustomFonts();

    public static CustomFonts getInstance() {
        return customFonts;
    }

    private CustomFonts() {
    }

    public Typeface getRegularTypeFace(Context context) {
        if (regular == null) {
            regular = Typeface.createFromAsset(context.getAssets(),  Fonts.REGULAR);
        }
        return regular;
    }

    public Typeface getBoldTypeface(Context context) {
        if (bold == null) {
            bold = Typeface.createFromAsset(context.getAssets(),  Fonts.BOLD);
        }
        return bold;
    }

    public Typeface getLightTypeFace(Context context) {
        if (light == null) {
            light = Typeface.createFromAsset(context.getAssets(),  Fonts.LIGHT);
        }
        return light;
    }

    public Typeface getMediumTypeFace(Context context) {
        if (medium == null) {
            medium = Typeface.createFromAsset(context.getAssets(),  Fonts.MEDIUM);
        }
        return medium;
    }
}
