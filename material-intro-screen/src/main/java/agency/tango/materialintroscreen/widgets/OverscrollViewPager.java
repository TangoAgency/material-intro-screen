package agency.tango.materialintroscreen.widgets;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;

import agency.tango.materialintroscreen.R;

public class OverscrollViewPager extends OverscrollContainer<SwipeableViewPager> {

    public OverscrollViewPager(Context context) {
        this(context, null);
    }

    public OverscrollViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverscrollViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected boolean canOverscrollAtEnd() {
        SwipeableViewPager viewPager = getOverscrollView();
        PagerAdapter adapter = viewPager.getAdapter();
        if (null != adapter && adapter.getCount() > 0) {
            if (viewPager.getCurrentItem() == adapter.getCount() - 1) {
                return true;
            }
            return false;
        }

        return false;
    }


    @Override
    protected SwipeableViewPager createOverscrollView() {
        SwipeableViewPager swipeableViewPager = new SwipeableViewPager(getContext(), null);
        swipeableViewPager.setId(R.id.swipeable_view_pager);
        return swipeableViewPager;
    }

}
