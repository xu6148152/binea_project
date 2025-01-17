package demo.binea.com.floatactionlayout;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Add this as the root view of your layouts
 */
@SuppressWarnings("ALL") public class SkittleLayout extends CoordinatorLayout
        implements View.OnClickListener, Animator.AnimatorListener {

    SkittleContainer skittleContainer;
    FloatingActionButton skittleMain;
    Boolean animatable;
    Integer flag = 0, color;
    List<Float> yList = new ArrayList<Float>();
    private RectF clickRect = new RectF();
    private RectF kittleRect = new RectF();

    public SkittleLayout(Context context) {
        super(context);
    }

    public SkittleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SkittleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override public void addView(View child) {
        super.addView(child);
        if (getChildCount() != 1 && !(child instanceof Snackbar.SnackbarLayout)) {
            addSkittleOnTop();
        }
    }

    @Override public void addView(View child, int index) {
        super.addView(child, index);
        if (getChildCount() != 1 && !(child instanceof Snackbar.SnackbarLayout)) {
            addSkittleOnTop();
        }
    }

    @Override public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        if (getChildCount() != 1) {
            addSkittleOnTop();
        }
    }

    @Override public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        if (getChildCount() != 1) {
            addSkittleOnTop();
        }
    }

    /**
     * Utility method called after addView to ensure the skittle container is on top
     */
    private void addSkittleOnTop() {

        Drawable drawable = skittleMain.getDrawable();

        removeView(skittleContainer);
        addViewInLayout(skittleContainer, -1, skittleContainer.getLayoutParams());
        skittleMain = (FloatingActionButton) skittleContainer.findViewById(R.id.skittle_main);

        setMainSkittleColor();
        if (drawable != null) {
            skittleMain.setImageDrawable(drawable);
        }
        skittleMain.setOnClickListener(this);
    }

    private void init(AttributeSet attrs) {

        //Add the main FloatingActionButton by default
        skittleContainer = (SkittleContainer) LayoutInflater.from(getContext())
                .inflate(R.layout.skittle_container, this, false);
        addView(skittleContainer);

        skittleMain = (FloatingActionButton) skittleContainer.findViewById(R.id.skittle_main);
        skittleMain.setOnClickListener(this);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SkittleLayout);

        try {
            color = array.getResourceId(R.styleable.SkittleLayout_mainSkittleColor,
                    Utils.fetchAccentColor(getContext()));

            setMainSkittleColor();
            Drawable drawable = array.getDrawable(R.styleable.SkittleLayout_mainSkittleIcon);
            if (drawable != null) {
                skittleMain.setImageDrawable(drawable);
            }
        } finally {
            array.recycle();
        }
    }

    private void setMainSkittleColor() {
        skittleMain.setBackgroundTintList(Utils.getColorStateList(color, getContext()));
    }

    void setMainSkittleColor(int color) {
        this.color = color;
        skittleMain.setBackgroundTintList(Utils.getColorStateList(color, getContext()));
    }

    void setMainSkittleAnimatable(boolean animatable) {
        this.animatable = animatable;
    }

    boolean getMainSkittleAnimatable() {
        return animatable;
    }

    void addFab(View fab) {
        //Add all button for at the same index for laying out the skittles vertically

        if (fab.getTag().equals("miniSkittle")) {
            fab.setTag("Skittle " + skittleContainer.getChildCount());
        }
        skittleContainer.addView(fab, 0);
    }

    public SkittleContainer getSkittleContainer() {
        return skittleContainer;
    }

    @Override public void onClick(View v) {

        toggleKittle();
    }

    private void toggleKittle() {
        int COUNT = skittleContainer.getChildCount();
        if (getMainSkittleAnimatable()) {
            toggleMainSkittle();
        }

        if (flag == 0) {
            openKittle();
        } else if (flag == 1) {
            closeKittle();
        }
    }

    private void openKittle() {
        int COUNT = skittleContainer.getChildCount();
        for (int i = 0; i < COUNT; i++) {
            View child = skittleContainer.getChildAt(i);
            if (child.getId() != R.id.skittle_main) {
                if (yList.size() != COUNT - 1) {
                    yList.add(child.getY());
                }

                child.setVisibility(VISIBLE);
                toggleSkittleClick(child, true);
                toggleSkittles(child, i);
            }
        }
        flag = 1;
    }

    private void closeKittle() {
        int COUNT = skittleContainer.getChildCount();
        for (int i = 0; i < COUNT; i++) {
            View child = skittleContainer.getChildAt(i);
            if (child.getId() != R.id.skittle_main) {
                child.setVisibility(VISIBLE);
                toggleSkittleClick(child, true);
                toggleSkittles(child, i);
            }
        }
        flag = 0;
    }

    private void toggleMainSkittle() {
        ObjectAnimator animator =
                ObjectAnimator.ofFloat(skittleMain, "rotation", 0f, 45f).setDuration(200);

        if (flag == 1) {
            animator.setFloatValues(45f, 0f);
        }
        animator.start();
    }

    private void toggleSkittles(View child, int index) {

        int duration = 200;
        FastOutLinearInInterpolator interpolator = new FastOutLinearInInterpolator();

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(child,
                PropertyValuesHolder.ofFloat("Y", child.getY() + child.getMeasuredHeight() / 2,
                        child.getY()), PropertyValuesHolder.ofFloat("alpha", 0, 1))
                .setDuration(duration);
        animator.setInterpolator(interpolator);

        if (flag == 0) {
            animator.setStartDelay((skittleContainer.getChildCount() - index) * 15);
            Log.d("Skittle Layout", "Animation");
            animator.start();
        } else {
            animator.setStartDelay(index * 15);
            animator.addListener(this);
            animator.reverse();
        }
    }

    /**
     * Used to set wether the skittle should be clickable or not
     */
    private void toggleSkittleClick(View v, boolean clickability) {

        if (v.getTag().equals(getResources().getString(R.string.text_skittle_tag))) {
            ((TextSkittleContainer) v).getSkittle().setClickability(clickability);
        } else {
            ((Skittle) v).setClickability(clickability);
        }
    }

    @Override public void onAnimationStart(Animator animation) {

    }

    @Override public void onAnimationEnd(Animator animation) {
        View child;
        //To place views at initial coordinates
        for (int i = 0; i < skittleContainer.getChildCount(); i++) {
            child = skittleContainer.getChildAt(i);
            if (child.getId() != R.id.skittle_main) {
                child.setAlpha(0f);
                child.setY(yList.get(i));
                toggleSkittleClick(child, false);
                child.setVisibility(GONE);
            }
        }
    }

    @Override public void onAnimationCancel(Animator animation) {

    }

    @Override public void onAnimationRepeat(Animator animation) {

    }

    private float downX = 0;
    private float downY = 0;

    @Override public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                kittleRect.set(skittleContainer.getLeft(), skittleContainer.getTop(),
                        skittleContainer.getRight(), skittleContainer.getBottom());
                clickRect.set(downX - 20, downY - 20, downX + 20, downY + 20);
                break;
            case MotionEvent.ACTION_UP:
                final float upX = ev.getX();
                final float upY = ev.getY();
                if (clickRect.contains(upX, upY) && !kittleRect.contains(upX, upY)) {
                    handleClick();
                    return true;
                }

        }
        return super.dispatchTouchEvent(ev);
    }

    private void handleClick() {
        if(flag == 1) {
            closeKittle();
        }
    }
}