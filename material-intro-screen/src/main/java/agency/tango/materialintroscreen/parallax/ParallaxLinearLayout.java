package agency.tango.materialintroscreen.parallax;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import agency.tango.materialintroscreen.R;

public class ParallaxLinearLayout extends LinearLayout implements Parallaxable
{
    @FloatRange(from = -1.0, to = 1.0)
    private float offset = 0;

    public ParallaxLinearLayout(Context context)
    {
        super(context);
    }

    public ParallaxLinearLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ParallaxLinearLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p)
    {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams()
    {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p)
    {
        return new LayoutParams(p);
    }

    @Override
    public void setOffset(@FloatRange(from = -1.0, to = 1.0) float offset)
    {
        this.offset = offset;
        for (int i = getChildCount() - 1; i >= 0; i--)
        {
            View child = getChildAt(i);
            ParallaxLinearLayout.LayoutParams p = (LayoutParams) child.getLayoutParams();
            if (p.parallaxFactor == 0) continue;
            child.setTranslationX(getWidth() * -offset * p.parallaxFactor);
        }
    }

    public static class LayoutParams extends LinearLayout.LayoutParams
    {
        float parallaxFactor = 0f;

        public LayoutParams(Context c, AttributeSet attrs)
        {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ParallaxLayout_Layout);
            parallaxFactor = a.getFloat(R.styleable.ParallaxLayout_Layout_layout_parallaxFactor, parallaxFactor);
            a.recycle();
        }

        public LayoutParams(int width, int height)
        {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity)
        {
            super(width, height, gravity);
        }

        public LayoutParams(ViewGroup.LayoutParams source)
        {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source)
        {
            super(source);
        }
    }
}