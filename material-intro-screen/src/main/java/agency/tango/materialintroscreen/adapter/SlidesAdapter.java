package agency.tango.materialintroscreen.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

import agency.tango.materialintroscreen.SlideFragment;

public class SlidesAdapter extends FragmentStatePagerAdapter {
    private ArrayList<SlideFragment> fragments = new ArrayList<>();

    public SlidesAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public SlideFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        SlideFragment fragment = (SlideFragment) super.instantiateItem(container, position);
        fragments.set(position, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addItem(SlideFragment fragment) {
        fragments.add(getCount(), fragment);
        notifyDataSetChanged();
    }

    public int getLastItemPosition() {
        return getCount() - 1;
    }

    public boolean isLastSlide(int position) {
        return position == getCount() - 1;
    }

    public boolean shouldFinish(int position) {
        return position == getCount() && getItem(getCount() - 1).canMoveFurther();
    }

    public boolean shouldLockSlide(int position) {
        SlideFragment fragment = getItem(position);
        return !fragment.canMoveFurther() || fragment.hasNeededPermissionsToGrant();
    }
}
