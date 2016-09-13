package agency.tango.materialintroscreen.listeners.clickListeners;

import android.support.v4.view.ViewPager;
import android.view.View;

public class NextSlideClickListener implements View.OnClickListener {
    private ViewPager viewPager;

    public NextSlideClickListener(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onClick(View v) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }
}