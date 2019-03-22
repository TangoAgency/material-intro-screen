package agency.tango.materialintroscreen.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import agency.tango.materialintroscreen.ISlideErrorHandler;
import agency.tango.materialintroscreen.adapter.SlidesAdapter;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.CustomViewPager;

@SuppressWarnings("PMD.SingularField")
public class SwipeableViewPager extends CustomViewPager {
    private float startPos = 0;
    private boolean swipingAllowed;
    private boolean alphaExitTransitionEnabled = false;
    private ISlideErrorHandler errorHandler;

    public SwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        swipingAllowed = true;
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent event) {
        switch (event.getActionMasked()) {
            case (MotionEvent.ACTION_DOWN):
                return super.onInterceptTouchEvent(event);
            case (MotionEvent.ACTION_MOVE):
                if (!swipingAllowed) {
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
        switch (event.getActionMasked()) {
            case (MotionEvent.ACTION_DOWN):
                startPos = event.getX();
                resolveSwipingRightAllowed();
                return super.onTouchEvent(event);
            case (MotionEvent.ACTION_MOVE):
                if (isSwipingNotAllowed(event)) {
                    errorHandler.handleError();
                    return true;
                }
                return super.onTouchEvent(event);
            case (MotionEvent.ACTION_UP):
                if (isSwipingNotAllowed(event)) {
                    scrollTo(this.mScroller.getCurrX(), 0);
                    onPageScrolled(getCurrentItem(), 0, 0);
                    errorHandler.handleError();
                    return true;
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

    @Override
    public boolean executeKeyEvent(@NonNull KeyEvent event) {
        return false;
    }

    public void registerSlideErrorHandler(ISlideErrorHandler handler) {
        errorHandler = handler;
    }

    public void moveToNextPage() {
        setCurrentItem(getCurrentItem() + 1, true);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    public int getPreviousItem() {
        return getCurrentItem() - 1;
    }

    public void setSwipingRightAllowed(boolean allowed) {
        swipingAllowed = allowed;
    }

    public void alphaExitTransitionEnabled(boolean alphaExitTransitionEnabled) {
        this.alphaExitTransitionEnabled = alphaExitTransitionEnabled;
    }

    public boolean alphaExitTransitionEnabled() {
        return alphaExitTransitionEnabled && swipingAllowed;
    }

    private boolean isSwipingNotAllowed(MotionEvent event) {
        return !swipingAllowed && startPos - event.getX() > 16;
    }

    private void resolveSwipingRightAllowed() {
        SlidesAdapter adapter = getAdapter();
        if (adapter == null) {
            return;
        }

        if (getAdapter().shouldLockSlide(getCurrentItem())) {
            setSwipingRightAllowed(false);
        } else {
            setSwipingRightAllowed(true);
        }
    }
}