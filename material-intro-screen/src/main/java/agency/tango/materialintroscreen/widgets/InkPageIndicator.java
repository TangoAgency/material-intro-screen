package agency.tango.materialintroscreen.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.CustomViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import java.util.Arrays;

import agency.tango.materialintroscreen.R;

public class InkPageIndicator extends View implements CustomViewPager.OnPageChangeListener, View.OnAttachStateChangeListener {
    private static final int DEFAULT_DOT_SIZE = 8;
    private static final int DEFAULT_GAP = 12;
    private static final int DEFAULT_ANIM_DURATION = 400;
    private static final int DEFAULT_UNSELECTED_COLOUR = 0x80ffffff;
    private static final int DEFAULT_SELECTED_COLOUR = 0xffffffff;

    private static final float INVALID_FRACTION = -1f;
    private static final float MINIMAL_REVEAL = 0.00001f;
    private final Paint selectedPaint;
    private final Path unselectedDotPath;
    private final Path unselectedDotLeftPath;
    private final Path unselectedDotRightPath;
    private final RectF rectF;
    private final Interpolator interpolator;
    float endX1;
    float endY1;
    float endX2;
    float endY2;
    float controlX1;
    float controlY1;
    float controlX2;
    float controlY2;
    private int dotDiameter;
    private int gap;
    private long animDuration;
    private int unselectedColour;
    private float dotRadius;
    private float halfDotRadius;
    private long animHalfDuration;
    private float dotTopY;
    private float dotCenterY;
    private float dotBottomY;
    private SwipeableViewPager viewPager;
    private int pageCount;
    private int currentPage;
    private int previousPage;
    private float selectedDotX;
    private boolean selectedDotInPosition;
    private float[] dotCenterX;
    private float[] joiningFractions;
    private float retreatingJoinX1;
    private float retreatingJoinX2;
    private float[] dotRevealFractions;
    private boolean isAttachedToWindow;
    private boolean pageChanging;
    private Paint unselectedPaint;
    private Path combinedUnselectedPath;
    private ValueAnimator moveAnimation;
    private PendingRetreatAnimator retreatAnimation;
    private PendingRevealAnimator[] revealAnimations;

    public InkPageIndicator(Context context) {
        this(context, null, 0);
    }

    public InkPageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InkPageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final int density = (int) context.getResources().getDisplayMetrics().density;

        final TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.InkPageIndicator, defStyle, 0);

        dotDiameter = typedArray.getDimensionPixelSize(R.styleable.InkPageIndicator_dotDiameter, DEFAULT_DOT_SIZE * density);
        dotRadius = dotDiameter / 2;
        halfDotRadius = dotRadius / 2;
        gap = typedArray.getDimensionPixelSize(R.styleable.InkPageIndicator_dotGap, DEFAULT_GAP * density);
        animDuration = (long) typedArray.getInteger(R.styleable.InkPageIndicator_animationDuration, DEFAULT_ANIM_DURATION);
        animHalfDuration = animDuration / 2;
        unselectedColour = typedArray.getColor(R.styleable.InkPageIndicator_pageIndicatorColor, DEFAULT_UNSELECTED_COLOUR);
        int selectedColour = typedArray.getColor(R.styleable.InkPageIndicator_currentPageIndicatorColor, DEFAULT_SELECTED_COLOUR);
        typedArray.recycle();

        unselectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        unselectedPaint.setColor(unselectedColour);
        selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedPaint.setColor(selectedColour);
        interpolator = new FastOutSlowInInterpolator();

        combinedUnselectedPath = new Path();
        unselectedDotPath = new Path();
        unselectedDotLeftPath = new Path();
        unselectedDotRightPath = new Path();
        rectF = new RectF();

        addOnAttachStateChangeListener(this);
    }

    private int getCount() {
        return viewPager.getAdapter().getCount();
    }

    public void setViewPager(final SwipeableViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        setPageCount(getCount());
        viewPager.getAdapter().registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                setPageCount(getCount());
            }
        });
        setCurrentPageImmediate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (isAttachedToWindow) {
            float fraction = positionOffset;
            int currentPosition = pageChanging ? previousPage : currentPage;
            int leftDotPosition = position;

            if (currentPosition != position) {
                fraction = 1f - positionOffset;

                if (fraction == 1f) {
                    leftDotPosition = Math.min(currentPosition, position);
                }
            }
            setJoiningFraction(leftDotPosition, fraction);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position < pageCount) {
            if (isAttachedToWindow) {
                setSelectedPage(position);
            } else {
                setCurrentPageImmediate();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void setPageCount(int pages) {
        if (pages > 0) {
            pageCount = pages;
            resetState();
            requestLayout();
        }
    }

    private void calculateDotPositions(int width) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = width - getPaddingRight();

        int requiredWidth = getRequiredWidth();
        float startLeft = left + ((right - left - requiredWidth) / 2) + dotRadius;

        dotCenterX = new float[pageCount];
        for (int i = 0; i < pageCount; i++) {
            dotCenterX[i] = startLeft + i * (dotDiameter + gap);
        }
        dotTopY = top;
        dotCenterY = top + dotRadius;
        dotBottomY = top + dotDiameter;

        setCurrentPageImmediate();
    }

    private void setCurrentPageImmediate() {
        if (viewPager != null) {
            currentPage = viewPager.getCurrentItem();
        } else {
            currentPage = 0;
        }
        if (isDotAnimationStarted()) {
            selectedDotX = dotCenterX[currentPage];
        }
    }

    private boolean isDotAnimationStarted() {
        return dotCenterX != null && dotCenterX.length > 0 && (moveAnimation == null || !moveAnimation.isStarted());
    }

    private void resetState() {
        joiningFractions = new float[pageCount - 1];
        Arrays.fill(joiningFractions, 0f);
        dotRevealFractions = new float[pageCount];
        Arrays.fill(dotRevealFractions, 0f);
        retreatingJoinX1 = INVALID_FRACTION;
        retreatingJoinX2 = INVALID_FRACTION;
        selectedDotInPosition = true;
    }

    @SuppressLint("SwitchIntDef")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredHeight = getDesiredHeight();
        int height;
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.EXACTLY:
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(desiredHeight, MeasureSpec.getSize(heightMeasureSpec));
                break;
            default:
                height = desiredHeight;
                break;
        }

        int desiredWidth = getDesiredWidth();
        int width;
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.EXACTLY:
                width = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(desiredWidth, MeasureSpec.getSize(widthMeasureSpec));
                break;
            default:
                width = desiredWidth;
                break;
        }
        setMeasuredDimension(width, height);
        calculateDotPositions(width);
    }

    private int getDesiredHeight() {
        return getPaddingTop() + dotDiameter + getPaddingBottom();
    }

    private int getRequiredWidth() {
        return pageCount * dotDiameter + (pageCount - 1) * gap;
    }

    private int getDesiredWidth() {
        return getPaddingLeft() + getRequiredWidth() + getPaddingRight();
    }

    @Override
    public void onViewAttachedToWindow(View view) {
        isAttachedToWindow = true;
    }

    @Override
    public void onViewDetachedFromWindow(View view) {
        isAttachedToWindow = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (viewPager == null || pageCount == 0) return;
        drawUnselected(canvas);
        drawSelected(canvas);
    }

    private void drawUnselected(Canvas canvas) {
        combinedUnselectedPath.rewind();

        for (int page = 0; page < pageCount; page++) {
            int nextXIndex;

            if (page == pageCount - 1) {
                nextXIndex = page;
            } else {
                nextXIndex = page + 1;
            }

            Path unselectedPath = getUnselectedPath(page,
                    dotCenterX[page],
                    dotCenterX[nextXIndex],
                    page == pageCount - 1 ? INVALID_FRACTION : joiningFractions[page],
                    dotRevealFractions[page]);

            unselectedPath.addPath(combinedUnselectedPath);
            combinedUnselectedPath.addPath(unselectedPath);
        }

        if (retreatingJoinX1 != INVALID_FRACTION) {
            Path retreatingJoinPath = getRetreatingJoinPath();
            combinedUnselectedPath.addPath(retreatingJoinPath);
        }

        canvas.drawPath(combinedUnselectedPath, unselectedPaint);
    }

    private Path getUnselectedPath(int page, float centerX, float nextCenterX, float joiningFraction, float dotRevealFraction) {
        unselectedDotPath.rewind();

        if (isDotNotJoining(page, joiningFraction, dotRevealFraction)) {
            unselectedDotPath.addCircle(dotCenterX[page], dotCenterY, dotRadius, Path.Direction.CW);
        }

        if (isDotJoining(joiningFraction)) {

            unselectedDotLeftPath.rewind();
            unselectedDotLeftPath.moveTo(centerX, dotBottomY);
            rectF.set(centerX - dotRadius, dotTopY, centerX + dotRadius, dotBottomY);
            unselectedDotLeftPath.arcTo(rectF, 90, 180, true);

            endX1 = centerX + dotRadius + (joiningFraction * gap);
            endY1 = dotCenterY;
            controlX1 = centerX + halfDotRadius;
            controlY1 = dotTopY;
            controlX2 = endX1;
            controlY2 = endY1 - halfDotRadius;
            unselectedDotLeftPath.cubicTo(controlX1, controlY1,
                    controlX2, controlY2,
                    endX1, endY1);

            endX2 = centerX;
            endY2 = dotBottomY;
            controlX1 = endX1;
            controlY1 = endY1 + halfDotRadius;
            controlX2 = centerX + halfDotRadius;
            controlY2 = dotBottomY;
            unselectedDotLeftPath.cubicTo(controlX1, controlY1,
                    controlX2, controlY2,
                    endX2, endY2);

            unselectedDotPath.addPath(unselectedDotLeftPath);

            unselectedDotRightPath.rewind();
            unselectedDotRightPath.moveTo(nextCenterX, dotBottomY);

            rectF.set(nextCenterX - dotRadius, dotTopY, nextCenterX + dotRadius, dotBottomY);
            unselectedDotRightPath.arcTo(rectF, 90, -180, true);

            endX1 = nextCenterX - dotRadius - (joiningFraction * gap);
            endY1 = dotCenterY;
            controlX1 = nextCenterX - halfDotRadius;
            controlY1 = dotTopY;
            controlX2 = endX1;
            controlY2 = endY1 - halfDotRadius;
            unselectedDotRightPath.cubicTo(controlX1, controlY1,
                    controlX2, controlY2,
                    endX1, endY1);

            endX2 = nextCenterX;
            endY2 = dotBottomY;
            controlX1 = endX1;
            controlY1 = endY1 + halfDotRadius;
            controlX2 = endX2 - halfDotRadius;
            controlY2 = dotBottomY;
            unselectedDotRightPath.cubicTo(controlX1, controlY1,
                    controlX2, controlY2,
                    endX2, endY2);
            unselectedDotPath.addPath(unselectedDotRightPath);
        }

        if (joiningFraction > 0.5f && joiningFraction < 1f && retreatingJoinX1 == INVALID_FRACTION) {
            float adjustedFraction = (joiningFraction - 0.2f) * 1.25f;

            unselectedDotPath.moveTo(centerX, dotBottomY);
            rectF.set(centerX - dotRadius, dotTopY, centerX + dotRadius, dotBottomY);
            unselectedDotPath.arcTo(rectF, 90, 180, true);

            endX1 = centerX + dotRadius + (gap / 2);
            endY1 = dotCenterY - (adjustedFraction * dotRadius);
            controlX1 = endX1 - (adjustedFraction * dotRadius);
            controlY1 = dotTopY;
            controlX2 = endX1 - ((1 - adjustedFraction) * dotRadius);
            controlY2 = endY1;
            unselectedDotPath.cubicTo(controlX1, controlY1,
                    controlX2, controlY2,
                    endX1, endY1);

            endX2 = nextCenterX;
            endY2 = dotTopY;
            controlX1 = endX1 + ((1 - adjustedFraction) * dotRadius);
            controlY1 = endY1;
            controlX2 = endX1 + (adjustedFraction * dotRadius);
            controlY2 = dotTopY;
            unselectedDotPath.cubicTo(controlX1, controlY1,
                    controlX2, controlY2,
                    endX2, endY2);

            rectF.set(nextCenterX - dotRadius, dotTopY, nextCenterX + dotRadius, dotBottomY);
            unselectedDotPath.arcTo(rectF, 270, 180, true);

            endY1 = dotCenterY + (adjustedFraction * dotRadius);
            controlX1 = endX1 + (adjustedFraction * dotRadius);
            controlY1 = dotBottomY;
            controlX2 = endX1 + ((1 - adjustedFraction) * dotRadius);
            controlY2 = endY1;
            unselectedDotPath.cubicTo(controlX1, controlY1,
                    controlX2, controlY2,
                    endX1, endY1);

            endX2 = centerX;
            endY2 = dotBottomY;
            controlX1 = endX1 - ((1 - adjustedFraction) * dotRadius);
            controlY1 = endY1;
            controlX2 = endX1 - (adjustedFraction * dotRadius);
            controlY2 = endY2;
            unselectedDotPath.cubicTo(controlX1, controlY1,
                    controlX2, controlY2,
                    endX2, endY2);
        }
        if (joiningFraction == 1 && retreatingJoinX1 == INVALID_FRACTION) {
            rectF.set(centerX - dotRadius, dotTopY, nextCenterX + dotRadius, dotBottomY);
            unselectedDotPath.addRoundRect(rectF, dotRadius, dotRadius, Path.Direction.CW);
        }

        if (dotRevealFraction > MINIMAL_REVEAL) {
            unselectedDotPath.addCircle(centerX, dotCenterY, dotRevealFraction * dotRadius,
                    Path.Direction.CW);
        }

        return unselectedDotPath;
    }

    private boolean isDotJoining(float joiningFraction) {
        return joiningFraction > 0f && joiningFraction <= 0.5f && retreatingJoinX1 == INVALID_FRACTION;
    }

    private boolean isDotNotJoining(int page, float joiningFraction, float dotRevealFraction) {
        return (joiningFraction == 0f || joiningFraction == INVALID_FRACTION)
                && dotRevealFraction == 0f
                && !(page == currentPage && selectedDotInPosition);
    }

    private Path getRetreatingJoinPath() {
        unselectedDotPath.rewind();
        rectF.set(retreatingJoinX1, dotTopY, retreatingJoinX2, dotBottomY);
        unselectedDotPath.addRoundRect(rectF, dotRadius, dotRadius, Path.Direction.CW);
        return unselectedDotPath;
    }

    private void drawSelected(Canvas canvas) {
        canvas.drawCircle(selectedDotX, dotCenterY, dotRadius, selectedPaint);
    }

    private void setSelectedPage(int now) {
        if (now == currentPage) {
            return;
        }

        pageChanging = true;
        previousPage = currentPage;
        currentPage = now;
        final int steps = Math.abs(now - previousPage);

        if (steps > 1) {
            if (now > previousPage) {
                for (int i = 0; i < steps; i++) {
                    setJoiningFraction(previousPage + i, 1f);
                }
            } else {
                for (int i = -1; i > -steps; i--) {
                    setJoiningFraction(previousPage + i, 1f);
                }
            }
        }

        moveAnimation = createMoveSelectedAnimator(dotCenterX[now], previousPage, now, steps);
        moveAnimation.start();
    }

    private ValueAnimator createMoveSelectedAnimator(
            final float moveTo, int was, int now, int steps) {
        ValueAnimator moveSelected = ValueAnimator.ofFloat(selectedDotX, moveTo);

        retreatAnimation = new PendingRetreatAnimator(was, now, steps,
                now > was ?
                        new RightwardStartPredicate(moveTo - ((moveTo - selectedDotX) * 0.25f)) :
                        new LeftwardStartPredicate(moveTo + ((selectedDotX - moveTo) * 0.25f)));
        retreatAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                resetState();
                pageChanging = false;
            }
        });
        moveSelected.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                selectedDotX = (Float) valueAnimator.getAnimatedValue();
                retreatAnimation.startIfNecessary(selectedDotX);
                ViewCompat.postInvalidateOnAnimation(InkPageIndicator.this);
            }
        });
        moveSelected.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                selectedDotInPosition = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                selectedDotInPosition = true;
            }
        });

        moveSelected.setStartDelay(selectedDotInPosition ? animDuration / 4L : 0L);
        moveSelected.setDuration(animDuration * 3L / 4L);
        moveSelected.setInterpolator(interpolator);
        return moveSelected;
    }

    private void setJoiningFraction(int leftDot, float fraction) {
        if (joiningFractions != null) {
            if (leftDot < joiningFractions.length) {
                joiningFractions[leftDot] = fraction;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }

    public void clearJoiningFractions() {
        Arrays.fill(joiningFractions, 0f);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void setDotRevealFraction(int dot, float fraction) {
        if (dot < dotRevealFractions.length) {
            dotRevealFractions[dot] = fraction;
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setPageIndicatorColor(int secondaryColor) {
        unselectedColour = secondaryColor;
        unselectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        unselectedPaint.setColor(unselectedColour);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPage = savedState.currentPage;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = currentPage;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int currentPage;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPage);
        }
    }

    public abstract class PendingStartAnimator extends ValueAnimator {
        boolean hasStarted;
        StartPredicate predicate;

        PendingStartAnimator(StartPredicate predicate) {
            super();
            this.predicate = predicate;
            hasStarted = false;
        }

        void startIfNecessary(float currentValue) {
            if (!hasStarted && predicate.shouldStart(currentValue)) {
                start();
                hasStarted = true;
            }
        }
    }

    public class PendingRetreatAnimator extends PendingStartAnimator {
        PendingRetreatAnimator(int was, int now, int steps, StartPredicate predicate) {
            super(predicate);
            setDuration(animHalfDuration);
            setInterpolator(interpolator);

            // work out the start/end values of the retreating join from the direction we're
            // travelling in.  Also look at the current selected dot position, i.e. we're moving on
            // before a prior anim has finished.
            final float initialX1 = now > was ? Math.min(dotCenterX[was], selectedDotX) - dotRadius
                    : dotCenterX[now] - dotRadius;
            final float finalX1 = now > was ? dotCenterX[now] - dotRadius
                    : dotCenterX[now] - dotRadius;
            final float initialX2 = now > was ? dotCenterX[now] + dotRadius
                    : Math.max(dotCenterX[was], selectedDotX) + dotRadius;
            final float finalX2 = now > was ? dotCenterX[now] + dotRadius
                    : dotCenterX[now] + dotRadius;

            revealAnimations = new PendingRevealAnimator[steps];
            // hold on to the indexes of the dots that will be hidden by the retreat so that
            // we can initialize their revealFraction's i.e. make sure they're hidden while the
            // reveal animation runs
            final int[] dotsToHide = new int[steps];
            if (initialX1 != finalX1) {
                setFloatValues(initialX1, finalX1);
                for (int i = 0; i < steps; i++) {
                    revealAnimations[i] = new PendingRevealAnimator(was + i,
                            new RightwardStartPredicate(dotCenterX[was + i]));
                    dotsToHide[i] = was + i;
                }
                addUpdateListener(new AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        retreatingJoinX1 = (Float) valueAnimator.getAnimatedValue();
                        ViewCompat.postInvalidateOnAnimation(InkPageIndicator.this);

                        for (PendingRevealAnimator pendingReveal : revealAnimations) {
                            pendingReveal.startIfNecessary(retreatingJoinX1);
                        }
                    }
                });
            } else {
                setFloatValues(initialX2, finalX2);
                for (int i = 0; i < steps; i++) {
                    revealAnimations[i] = new PendingRevealAnimator(was - i,
                            new LeftwardStartPredicate(dotCenterX[was - i]));
                    dotsToHide[i] = was - i;
                }
                addUpdateListener(new AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        retreatingJoinX2 = (Float) valueAnimator.getAnimatedValue();
                        ViewCompat.postInvalidateOnAnimation(InkPageIndicator.this);

                        for (PendingRevealAnimator pendingReveal : revealAnimations) {
                            pendingReveal.startIfNecessary(retreatingJoinX2);
                        }
                    }
                });
            }

            addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    clearJoiningFractions();

                    for (int dot : dotsToHide) {
                        setDotRevealFraction(dot, MINIMAL_REVEAL);
                    }
                    retreatingJoinX1 = initialX1;
                    retreatingJoinX2 = initialX2;
                    ViewCompat.postInvalidateOnAnimation(InkPageIndicator.this);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    retreatingJoinX1 = INVALID_FRACTION;
                    retreatingJoinX2 = INVALID_FRACTION;
                    ViewCompat.postInvalidateOnAnimation(InkPageIndicator.this);
                }
            });
        }
    }

    public class PendingRevealAnimator extends PendingStartAnimator {
        private int dot;

        PendingRevealAnimator(int dot, StartPredicate predicate) {
            super(predicate);
            setFloatValues(MINIMAL_REVEAL, 1f);
            this.dot = dot;
            setDuration(animHalfDuration);
            setInterpolator(interpolator);
            addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    setDotRevealFraction(PendingRevealAnimator.this.dot,
                            (Float) valueAnimator.getAnimatedValue());
                }
            });
            addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    setDotRevealFraction(PendingRevealAnimator.this.dot, 0f);
                    ViewCompat.postInvalidateOnAnimation(InkPageIndicator.this);
                }
            });
        }
    }

    public abstract class StartPredicate {
        float thresholdValue;

        StartPredicate(float thresholdValue) {
            this.thresholdValue = thresholdValue;
        }

        abstract boolean shouldStart(float currentValue);
    }

    public class RightwardStartPredicate extends StartPredicate {
        RightwardStartPredicate(float thresholdValue) {
            super(thresholdValue);
        }

        boolean shouldStart(float currentValue) {
            return currentValue > thresholdValue;
        }
    }

    public class LeftwardStartPredicate extends StartPredicate {
        LeftwardStartPredicate(float thresholdValue) {
            super(thresholdValue);
        }

        boolean shouldStart(float currentValue) {
            return currentValue < thresholdValue;
        }
    }
}