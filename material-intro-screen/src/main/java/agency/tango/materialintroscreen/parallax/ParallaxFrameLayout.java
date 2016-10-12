package agency.tango.materialintroscreen.parallax;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import agency.tango.materialintroscreen.R;

public class ParallaxFrameLayout extends FrameLayout implements Parallaxable {

    public ParallaxFrameLayout(Context context) {
        super(context);
    }

    public ParallaxFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public void setOffset(@FloatRange(from = -1.0, to = 1.0) float offset) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            ParallaxFrameLayout.LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            if (layoutParams.parallaxFactor == 0)
                continue;
            child.setTranslationX(getWidth() * -offset * layoutParams.parallaxFactor);
        }
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {
        float parallaxFactor = 0f;

        LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ParallaxLayout_Layout);
            parallaxFactor = typedArray.getFloat(R.styleable.ParallaxLayout_Layout_layout_parallaxFactor, parallaxFactor);
            typedArray.recycle();
        }

        LayoutParams(int width, int height) {
            super(width, height);
        }

        @SuppressWarnings("unused")
        LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
        }

        LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        @SuppressWarnings("unused")
        LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }
}