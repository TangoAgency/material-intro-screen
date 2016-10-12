package agency.tango.materialintroscreen.listeners;

import agency.tango.materialintroscreen.SlideFragment;
import agency.tango.materialintroscreen.adapter.SlidesAdapter;
import agency.tango.materialintroscreen.widgets.SwipeableViewPager;

public class SwipeStateTouchListener implements ITouchEventListener {

    private final SwipeableViewPager viewPager;
    private final SlidesAdapter adapter;

    public SwipeStateTouchListener(SwipeableViewPager viewPager, SlidesAdapter adapter) {
        this.viewPager = viewPager;
        this.adapter = adapter;
    }

    @Override
    public void process() {
        SlideFragment fragment = adapter.getItem(viewPager.getCurrentItem());
        if (!fragment.canMoveFurther() || fragment.hasNeededPermissionsToGrant()) {
            viewPager.setAllowedSwipeDirection(SwipeableViewPager.SwipeDirection.left);
        } else {
            viewPager.setAllowedSwipeDirection(SwipeableViewPager.SwipeDirection.all);
        }
    }
}
