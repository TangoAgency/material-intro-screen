package agency.tango.materialintroscreen.widgets;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import agency.tango.materialintroscreen.SlideFragment;
import agency.tango.materialintroscreen.adapter.SlidesAdapter;

public class SwipeableViewPager extends ViewPager {
    private boolean swipingAllowed;
    float startPos = 0;
    private int currentIt;

    public SwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        swipingAllowed = true;

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                SlideFragment fragment = getAdapter().getItem(getCurrentItem());
                if (!fragment.canMoveFurther() || fragment.hasNeededPermissionsToGrant()) {
                    setSwipingRightAllowed(false);
                } else {
                    setSwipingRightAllowed(true);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                return super.onInterceptTouchEvent(event);
            case (MotionEvent.ACTION_MOVE):
                if (!swipingAllowed && startPos - event.getX() > 0) {
                    return false;
                }
                return super.onInterceptTouchEvent(event);
            case (MotionEvent.ACTION_UP):
                if (!swipingAllowed) {
                    return false;
                }
                return super.onInterceptTouchEvent(event);
            default:
                return super.onInterceptTouchEvent(event);
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                startPos = event.getX();
                currentIt = getCurrentItem();
                return super.onTouchEvent(event);
            case (MotionEvent.ACTION_MOVE):
                if (!swipingAllowed && startPos - event.getX() > 0) {

                    return true;
                }
                return super.onTouchEvent(event);
            case (MotionEvent.ACTION_UP):
                if (!swipingAllowed && startPos - event.getX() > 0) {

                    //setCurrentItem(currentIt, true);
                    if(currentIt != getCurrentItem())
                    {
                        if(getAdapter().getItem(currentIt).canMoveFurther() == false)
                        {
                            setCurrentItem(currentIt);
                        }
                    }
                    //return true;
                }
                startPos = 0;
                return super.onTouchEvent(event);
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    public SlidesAdapter getAdapter() {
        return (SlidesAdapter) super.getAdapter();
    }

    public int getPreviousItem() {
        return getCurrentItem() - 1;
    }

    public void setSwipingRightAllowed(boolean allowed) {
        swipingAllowed = allowed;
    }
}