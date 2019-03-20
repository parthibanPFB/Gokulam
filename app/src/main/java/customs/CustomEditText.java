package customs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;


import real.estate.gokulam.R;


public class CustomEditText extends AppCompatEditText {

    private static final int DEFAULT_TYPE_FACE = 0;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        Typeface typeface;
        if (attrs == null) {
            typeface = getMatchingTypeface(DEFAULT_TYPE_FACE);
        } else {
            //get the attributes specified in attrs.xml using the name we included
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                    R.styleable.CustomTypeface, 0, 0);
            try {
                int type = typedArray.getInt(R.styleable.CustomTypeface_typeface, DEFAULT_TYPE_FACE);
                typeface = getMatchingTypeface(type);
            } finally {
                typedArray.recycle();
            }
        }
        setTypeface(typeface);
    }

    private Typeface getMatchingTypeface(int type) {
        Context context = getContext();
        switch (type) {
            case 0:
            default:
                return CustomFonts.getInstance().getRegularTypeFace(context);
            case 1:
                return CustomFonts.getInstance().getBoldTypeface(context);
            case 2:
                return CustomFonts.getInstance().getLightTypeFace(context);
            case 3:
                return CustomFonts.getInstance().getMediumTypeFace(context);
        }
    }
}
