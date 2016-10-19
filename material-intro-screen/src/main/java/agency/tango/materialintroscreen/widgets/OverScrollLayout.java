package agency.tango.materialintroscreen.widgets;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;

import agency.tango.materialintroscreen.R;

public class OverScrollLayout extends OverScrollContainer {

    public OverScrollLayout(Context context) {
        this(context, null);
    }

    public OverScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverScrollLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected boolean canOverScrollAtEnd() {
        SwipeableViewPager viewPager = getOverScrollView();
        PagerAdapter adapter = viewPager.getAdapter();
        if (null != adapter && adapter.getCount() > 0) {
            if (viewPager.alphaExitTransitionEnabled() && viewPager.getCurrentItem() == adapter.getCount() - 1) {
                return true;
            }
            return false;
        }

        return false;
    }

    @Override
    protected SwipeableViewPager createOverScrollView() {
        SwipeableViewPager swipeableViewPager = new SwipeableViewPager(getContext(), null);
        swipeableViewPager.setId(R.id.swipeable_view_pager);
        return swipeableViewPager;
    }

}
