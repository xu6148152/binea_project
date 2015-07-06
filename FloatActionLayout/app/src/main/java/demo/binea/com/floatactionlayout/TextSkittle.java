package demo.binea.com.floatactionlayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Utility class for creating text skittle, exposes methods for modifying the look of the
 * text view, many more to come
 */
public class TextSkittle {

    private Context context;
    private TextSkittleContainer layout;
    private Skittle skittle;
    private TextView textView;

    /**
     * Use this constructor for convenient addition of the text skittle
     *
     * @param context
     * @param parent
     */
    public TextSkittle(Context context, ViewGroup parent) {
        this.context = context;
        layout = (TextSkittleContainer) LayoutInflater.from(context)
                .inflate(R.layout.text_action_skittle, parent, false);
        layout.setTag(context.getResources().getString(R.string.text_skittle_tag));
        skittle = (Skittle) layout.getChildAt(1);
        skittle.setClickability(false);
        textView = (TextView) layout.getChildAt(0);
    }

    /**
     * Used internally, Use the above constructor in your activities
     *
     * @param textSkittleLayout
     */
    TextSkittle(TextSkittleContainer textSkittleLayout) {

        layout = textSkittleLayout;
        textView = (TextView) textSkittleLayout.getChildAt(0);
        skittle = (Skittle) textSkittleLayout.getChildAt(1);
    }

    TextSkittleContainer getTextSkittle() {
        return layout;
    }

    Skittle getSkittle() {
        return skittle;
    }

    public void setIcon(Drawable icon) {
        skittle.setImageDrawable(icon);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setTextColor(@ColorRes int color) {
        textView.setTextColor(context.getResources().getColor(color));
    }

    public void setTextBackgroundColor(@ColorRes int color) {
        textView.setBackgroundColor(context.getResources().getColor(color));
    }

    public void setTextBackground(Drawable background) {
        textView.setBackgroundDrawable(background);
    }

    public void setSkittleColor(@ColorRes int color) {

        skittle.setBackgroundTintList(Utils.getColorStateList(color, context));

    }

    public String getText() {
        return textView.getText().toString();
    }

    public int getPosition() {
        return layout.getPosition();
    }

    public void setPosition(int position) {
        layout.setPosition(position);
    }
}