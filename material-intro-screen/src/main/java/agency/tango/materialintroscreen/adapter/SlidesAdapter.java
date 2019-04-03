package agency.tango.materialintroscreen.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

import agency.tango.materialintroscreen.fragments.SlideFragmentBase;

public class SlidesAdapter extends FragmentStatePagerAdapter {

    private ArrayList<SlideFragmentBase> fragments = new ArrayList<>();

    public SlidesAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public SlideFragmentBase getItem(int position) {
        return fragments.get(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        SlideFragmentBase fragment = (SlideFragmentBase) super.instantiateItem(container, position);
        fragments.set(position, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addItem(SlideFragmentBase fragment) {
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
        SlideFragmentBase fragment = getItem(position);
        return !fragment.canMoveFurther() || fragment.hasNeededPermissionsToGrant();
    }
}
