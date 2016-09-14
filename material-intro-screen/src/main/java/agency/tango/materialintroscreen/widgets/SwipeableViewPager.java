package agency.tango.materialintroscreen.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import agency.tango.materialintroscreen.adapter.SlidesAdapter;
import agency.tango.materialintroscreen.listeners.ITouchEventListener;

public class SwipeableViewPager extends ViewPager {
    private float initialXValue;
    private SwipeDirection direction;

    List<ITouchEventListener> touchEventListeners = new ArrayList<>();

    public SwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.direction = SwipeDirection.all;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (ITouchEventListener eventListener : touchEventListeners) {
            eventListener.process();
        }

        if (IsSwipeAllowed(event)) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (IsSwipeAllowed(event)) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    @Override
    public SlidesAdapter getAdapter() {
        return (SlidesAdapter) super.getAdapter();
    }

    public SwipeableViewPager registerOnTouchEventListener(ITouchEventListener eventListener) {
        touchEventListeners.add(eventListener);
        return this;
    }

    public int getPreviousItem() {
        return this.getCurrentItem() - 1;
    }

    public void setAllowedSwipeDirection(SwipeDirection direction) {
        this.direction = direction;
    }

    private boolean IsSwipeAllowed(MotionEvent event) {
        if (direction == SwipeDirection.all) return true;

        if (direction == SwipeDirection.none) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            try {
                float diffX = event.getX() - initialXValue;
                if (diffX > 0 && direction == SwipeDirection.right) {
                    // swipe from left to right detected
                    return false;
                } else if (diffX < 0 && direction == SwipeDirection.left) {
                    // swipe from right to left detected
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return true;
    }

    public enum SwipeDirection {
        all, left, right, none
    }
}