package agency.tango.materialintroscreen.parallax;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.FloatRange;
import androidx.percentlayout.widget.PercentRelativeLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import agency.tango.materialintroscreen.R;

public class ParallaxPercentRelativeLayout extends PercentRelativeLayout implements Parallaxable {
    public ParallaxPercentRelativeLayout(Context context) {
        super(context);
    }

    public ParallaxPercentRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxPercentRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
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
    protected PercentRelativeLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public void setOffset(@FloatRange(from = -1.0, to = 1.0) float offset) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            LayoutParams p = (LayoutParams) child.getLayoutParams();
            if (p.parallaxFactor == 0)
                continue;
            child.setTranslationX(getWidth() * -offset * p.parallaxFactor);
        }
    }

    public static class LayoutParams extends PercentRelativeLayout.LayoutParams {
        float parallaxFactor = 0f;

        @SuppressLint("CustomViewStyleable")
        LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.mis_ParallaxLayout_Layout);
            parallaxFactor = typedArray.getFloat(R.styleable.mis_ParallaxLayout_Layout_mis_layout_parallaxFactor, parallaxFactor);
            typedArray.recycle();
        }

        LayoutParams(int width, int height) {
            super(width, height);
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
