package agency.tango.materialintroscreen.widgets;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

import agency.tango.materialintroscreen.listeners.IFinishListener;

public abstract class OverScrollContainer extends RelativeLayout {
    private SwipeableViewPager swipeableViewPager = null;
    private boolean mIsBeingDragged = false;
    private float mMotionBeginX = 0;
    private float positionOffset = 0;
    private int mTouchSlop;
    private IFinishListener finishListener;

    abstract protected boolean canOverScrollAtEnd();

    abstract protected SwipeableViewPager createOverScrollView();

    public OverScrollContainer(Context context) {
        this(context, null);
    }

    public OverScrollContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverScrollContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        swipeableViewPager = createOverScrollView();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        addView(swipeableViewPager, layoutParams);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public SwipeableViewPager getOverScrollView() {
        return swipeableViewPager;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            mMotionBeginX = event.getX();
            mIsBeingDragged = false;
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (!mIsBeingDragged) {
                float scrollDirectionDiff = event.getX() - mMotionBeginX;

                if (Math.abs(scrollDirectionDiff) > mTouchSlop) {
                    if (canOverScrollAtEnd() && scrollDirectionDiff < 0f) {
                        mIsBeingDragged = true;
                    }
                }
            }
        }

        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float moveOffset = event.getX() - mMotionBeginX;

        if (action == MotionEvent.ACTION_MOVE) {
            moveOverScrollView(moveOffset);
        } else if (action == MotionEvent.ACTION_UP) {
            if (positionOffset > 0.5f) {
                finishOverScrollViewWithAnimation(moveOffset);
            } else {
                resetOverScrollViewWithAnimation(moveOffset);
            }
            mIsBeingDragged = false;
        }

        return true;
    }

    public void registerFinishListener(IFinishListener listener) {
        finishListener = listener;
    }

    private void moveOverScrollView(float currentX) {
        if (currentX <= 0f) {
            scrollTo(-(int) currentX, 0);

            positionOffset = ((100f * getScrollX()) / getWidth()) / 100f;
            swipeableViewPager.onPageScrolled(swipeableViewPager.getAdapter().getLastItemPosition(), positionOffset, 0);

            if (positionOffset == 1f) {
                finishListener.doOnFinish();
            }
        }
    }

    private void resetOverScrollViewWithAnimation(final float currentX) {
        post(new SmoothScrollRunnable((int) currentX, 0, 300, new AccelerateInterpolator()));
    }

    private void finishOverScrollViewWithAnimation(float currentX) {
        post(new SmoothScrollRunnable((int) currentX, -getWidth(), 300, new AccelerateInterpolator()));
    }

    final class SmoothScrollRunnable implements Runnable {
        private final Interpolator interpolator;
        private final int scrollToPosition;
        private final int scrollFromPosition;
        private final long duration;

        private long startTime = -1;
        private int currentPosition = -1;

        SmoothScrollRunnable(int fromPosition, int toPosition, long duration, Interpolator scrollAnimationInterpolator) {
            scrollFromPosition = fromPosition;
            scrollToPosition = toPosition;
            interpolator = scrollAnimationInterpolator;
            this.duration = duration;
        }

        @Override
        public void run() {
            if (startTime == -1) {
                startTime = System.currentTimeMillis();
            } else {
                long normalizedTime = (1000 * (System.currentTimeMillis() - startTime)) / duration;
                normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

                final int deltaY = Math.round((scrollFromPosition - scrollToPosition)
                        * interpolator.getInterpolation(normalizedTime / 1000f));
                currentPosition = scrollFromPosition - deltaY;

                moveOverScrollView(currentPosition);
            }

            if (scrollToPosition != currentPosition) {
                ViewCompat.postOnAnimation(OverScrollContainer.this, this);
            }
        }
    }
}