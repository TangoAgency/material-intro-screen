package agency.tango.materialintroscreen;

import android.animation.ArgbEvaluator;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import agency.tango.materialintroscreen.adapter.SlidesAdapter;
import agency.tango.materialintroscreen.animations.ViewTranslationWrapper;
import agency.tango.materialintroscreen.animations.wrappers.BackButtonTranslationWrapper;
import agency.tango.materialintroscreen.animations.wrappers.NextButtonTranslationWrapper;
import agency.tango.materialintroscreen.animations.wrappers.PageIndicatorTranslationWrapper;
import agency.tango.materialintroscreen.animations.wrappers.SkipButtonTranslationWrapper;
import agency.tango.materialintroscreen.animations.wrappers.ViewPagerTranslationWrapper;
import agency.tango.materialintroscreen.listeners.IPageScrolledListener;
import agency.tango.materialintroscreen.listeners.IPageSelectedListener;
import agency.tango.materialintroscreen.listeners.MessageButtonBehaviourOnPageSelected;
import agency.tango.materialintroscreen.listeners.ViewBehavioursOnPageChangeListener;
import agency.tango.materialintroscreen.listeners.clickListeners.FinishScreenClickListener;
import agency.tango.materialintroscreen.listeners.clickListeners.NextSlideClickListener;
import agency.tango.materialintroscreen.listeners.clickListeners.PermissionNotGrantedClickListener;
import agency.tango.materialintroscreen.listeners.scrollListeners.ParallaxScrollListener;
import agency.tango.materialintroscreen.widgets.InkPageIndicator;
import agency.tango.materialintroscreen.widgets.SwipeableViewPager;

import static android.view.View.GONE;

public abstract class MaterialIntroActivity extends AppCompatActivity {
    private SwipeableViewPager viewPager;
    private InkPageIndicator inkIndicator;
    private SlidesAdapter adapter;
    private ImageButton backButton;
    private ImageButton skipButton;
    private ImageButton nextButton;
    private CoordinatorLayout coordinatorLayout;
    private Button messageButton;
    private LinearLayout navigationView;

    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private ViewTranslationWrapper nextButtonTranslationWrapper;
    private MessageButtonBehaviourOnPageSelected messageButtonBehaviourOnPageSelected;

    private View.OnClickListener permissionNotGrantedClickListener;
    private View.OnClickListener finishScreenClickListener;
    private View.OnClickListener nextSlideClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_material_intro);
        viewPager = (SwipeableViewPager) findViewById(R.id.view_pager_slides);
        inkIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        backButton = (ImageButton) findViewById(R.id.button_back);
        nextButton = (ImageButton) findViewById(R.id.button_next);
        skipButton = (ImageButton) findViewById(R.id.button_skip);
        messageButton = (Button) findViewById(R.id.button_message);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout_slide);
        navigationView = (LinearLayout) findViewById(R.id.navigation_view);

        adapter = new SlidesAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        inkIndicator.setViewPager(viewPager);

        nextButton.setOnClickListener(new NextSlideClickListener(viewPager));
        nextButtonTranslationWrapper = new NextButtonTranslationWrapper(nextButton);
        initOnPageChangeListeners();

        permissionNotGrantedClickListener = new PermissionNotGrantedClickListener(this, nextButtonTranslationWrapper);
        finishScreenClickListener = new FinishScreenClickListener(this);
        nextSlideClickListener = new NextSlideClickListener(viewPager);

        setBackButtonVisible();

        viewPager.post(new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                messageButtonBehaviourOnPageSelected.pageSelected(currentItem);
                nextButtonBehaviour(currentItem, adapter.getItem(currentItem));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        SlideFragment fragment = adapter.getItem(viewPager.getCurrentItem());
        boolean hasPermissionToGrant = fragment.hasNeededPermissionsToGrant();
        if (hasPermissionToGrant == false) {
            viewPager.setAllowedSwipeDirection(SwipeableViewPager.SwipeDirection.all);
            nextButtonBehaviour(viewPager.getCurrentItem(), fragment);
            messageButtonBehaviourOnPageSelected.pageSelected(viewPager.getCurrentItem());
        } else {
            showPermissionsNotGrantedError();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            finish();
        } else {
            viewPager.setCurrentItem(viewPager.getPreviousItem());
        }
    }

    public void showPermissionsNotGrantedError() {
        showError(getString(R.string.please_grant_permissions));
    }

    public void showError(String error) {
        Snackbar.make(coordinatorLayout, error, Snackbar.LENGTH_SHORT).setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                navigationView.setTranslationY(0f);
                super.onDismissed(snackbar, event);
            }
        }).show();
    }

    private void initOnPageChangeListeners() {
        messageButtonBehaviourOnPageSelected = new MessageButtonBehaviourOnPageSelected(messageButton, adapter);

        viewPager.addOnPageChangeListener(new ViewBehavioursOnPageChangeListener(adapter)
                .registerViewTranslationWrapper(nextButtonTranslationWrapper)
                .registerViewTranslationWrapper(new BackButtonTranslationWrapper(backButton))
                .registerViewTranslationWrapper(new PageIndicatorTranslationWrapper(inkIndicator))
                .registerViewTranslationWrapper(new ViewPagerTranslationWrapper(viewPager))
                .registerViewTranslationWrapper(new SkipButtonTranslationWrapper(skipButton))

                .registerOnPageScrolled(new IPageScrolledListener() {
                    @Override
                    public void pageScrolled(final int position, float offset) {
                        viewPager.post(new Runnable() {
                            @Override
                            public void run() {
                                if (adapter.getItem(position).hasNeededPermissionsToGrant() || adapter.getItem(position).canPass() == false) {
                                    viewPager.setCurrentItem(position);
                                    inkIndicator.clearJoiningFractions();
                                }
                                nextButtonBehaviour(position, adapter.getItem(position));
                            }
                        });
                    }
                })
                .registerOnPageScrolled(new ColorTransitionScrollListener())
                .registerOnPageScrolled(new ParallaxScrollListener(adapter))

                .registerPageSelectedListener(messageButtonBehaviourOnPageSelected)
                .registerPageSelectedListener(new IPageSelectedListener() {
                    @Override
                    public void pageSelected(int position) {
                        if (adapter.shouldFinish(position)) {
                            finish();
                        }
                    }
                }));
    }

    private void nextButtonBehaviour(int position, final SlideFragment fragment) {
        if (fragment.hasNeededPermissionsToGrant()) {
            nextButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_next));
            nextButton.setOnClickListener(permissionNotGrantedClickListener);
            viewPager.setAllowedSwipeDirection(SwipeableViewPager.SwipeDirection.left);
        } else if (fragment.canPass() == false) {
            nextButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_next));
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nextButtonTranslationWrapper.error();
                    showError(fragment.passingErrorMessage());
                }
            });
            viewPager.setAllowedSwipeDirection(SwipeableViewPager.SwipeDirection.left);
        } else if (adapter.isLastSlide(position)) {
            nextButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_finish));
            nextButton.setOnClickListener(finishScreenClickListener);
        } else {
            nextButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_next));
            nextButton.setOnClickListener(nextSlideClickListener);
            viewPager.setAllowedSwipeDirection(SwipeableViewPager.SwipeDirection.all);
        }
    }

    private Integer getPrimaryColor(int position, float positionOffset) {
        return (Integer) argbEvaluator.evaluate(positionOffset, adapter.getItem(position).primaryColor(), adapter.getItem(position + 1).primaryColor());
    }

    private Integer getSecondaryColor(int position, float positionOffset) {
        return (Integer) argbEvaluator.evaluate(positionOffset, adapter.getItem(position).secondaryColor(), adapter.getItem(position + 1).secondaryColor());
    }

    /**
     * Add SlideFragment to IntroScreen
     *
     * @param slideFragment Fragment to add
     */
    public void addSlide(SlideFragment slideFragment) {
        adapter.addItem(slideFragment);
    }

    /**
     * Set skip button instead of back button
     */
    @SuppressWarnings("unused")
    public void setSkipButtonVisible() {
        backButton.setVisibility(GONE);

        skipButton.setVisibility(View.VISIBLE);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int position = viewPager.getCurrentItem(); position < adapter.getCalculatedCount(); position++) {
                    if (adapter.getItem(position).canPass() == false) {
                        viewPager.setCurrentItem(position);
                        showError(adapter.getItem(position).passingErrorMessage());
                        return;
                    }
                }
                viewPager.setCurrentItem(adapter.getLastItemPosition());
            }
        });
    }

    /**
     * Set back button visible
     */
    public void setBackButtonVisible() {
        skipButton.setVisibility(GONE);

        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getPreviousItem());
            }
        });
    }

    /**
     * Hides any back button
     */
    @SuppressWarnings("unused")
    public void hideBackButton() {
        backButton.setVisibility(View.INVISIBLE);
        skipButton.setVisibility(View.GONE);
    }

    /**
     * Set if last screen should be able to exit with alpha transition
     */
    @SuppressWarnings("unused")
    public void enableLastSlideAlphaExitTransition(boolean enableAlphaExitTransition) {
        adapter.addEmptySlide(new LastEmptySlideFragment());
    }

    private class ColorTransitionScrollListener implements IPageScrolledListener {
        @Override
        public void pageScrolled(int position, float offset) {
            if (position < adapter.getCount() - 1) {
                setViewsColor(position, offset);
            } else if (adapter.getCount() == 1) {
                viewPager.setBackgroundColor(adapter.getItem(position).primaryColor());
                messageButton.setTextColor(adapter.getItem(position).primaryColor());

                tintButtons(ColorStateList.valueOf(adapter.getItem(position).secondaryColor()));
            }
        }

        private void setViewsColor(int position, float offset) {
            int primaryColor = getPrimaryColor(position, offset);
            viewPager.setBackgroundColor(primaryColor);
            messageButton.setTextColor(primaryColor);

            int secondaryColor = getSecondaryColor(position, offset);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(secondaryColor);
            }
            inkIndicator.setPageIndicatorColor(secondaryColor);

            tintButtons(ColorStateList.valueOf(secondaryColor));
        }

        private void tintButtons(ColorStateList color) {
            ViewCompat.setBackgroundTintList(nextButton, color);
            ViewCompat.setBackgroundTintList(backButton, color);
            ViewCompat.setBackgroundTintList(skipButton, color);
        }
    }
}