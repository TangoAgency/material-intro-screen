package agency.tango.materialintroscreen.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

import agency.tango.materialintroscreen.LastEmptySlideFragment;
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

    /**
     * @return Returns count of fragments in adapter
     */
    @Override
    public int getCount() {
        return fragments.size();
    }

    /**
     * @return Returns count of fragments in adapter WITHOUT empty slide if is available
     */
    public int slidesCount() {
        if (isLastItemEmptySlide()) {
            return fragments.size() - 1;
        } else {
            return fragments.size();
        }
    }

    public void addItem(SlideFragment fragment) {
        fragments.add(slidesCount(), fragment);
        notifyDataSetChanged();
    }

    public void addEmptySlide(LastEmptySlideFragment fragment) {
        fragments.add(fragment);
        notifyDataSetChanged();
    }

    public int getLastItemPosition() {
        return slidesCount() - 1;
    }

    public boolean isLastSlide(int position) {
        return position == slidesCount() - 1;
    }

    public boolean shouldFinish(int position) {
        return position == slidesCount();
    }

    private boolean isLastItemEmptySlide() {
        return fragments.size() > 0 && fragments.get(fragments.size() - 1) instanceof LastEmptySlideFragment;
    }
}
