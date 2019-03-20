package customs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import real.estate.gokulam.R;


public class FontSpannableTextView extends TextView {

    public FontSpannableTextView(Context context) {
        super(context);
    }

    public FontSpannableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontSpannableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        /*if (isInEditMode()) {
            return;
        }*/
        String partialText = null;
        int partialTextColor = Integer.MIN_VALUE;

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontSpannableTextView);
            for (int i = 0; i < typedArray.getIndexCount(); i++) {
                int attr = typedArray.getIndex(i);
                switch (attr) {
                    case R.styleable.FontSpannableTextView_partial_text:
                        partialText = typedArray.getString(attr);
                        break;
                    case R.styleable.FontSpannableTextView_partial_text_color:
                        partialTextColor = typedArray.getColor(attr, Color.BLACK);
                        break;
                }
            }
            typedArray.recycle();
        }

        if (partialText != null && partialTextColor != Integer.MIN_VALUE) {
            String wholeText = getText().toString();
            Spannable spannable = new SpannableString(wholeText);
            spannable.setSpan(new ForegroundColorSpan(partialTextColor),
                    wholeText.indexOf(partialText),
                    wholeText.indexOf(partialText) + partialText.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setText(spannable);
        }
    }
}
