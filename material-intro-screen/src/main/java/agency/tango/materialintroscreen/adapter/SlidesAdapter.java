package agency.tango.materialintroscreen.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import agency.tango.materialintroscreen.LastEmptySlideFragment;
import agency.tango.materialintroscreen.SlideFragment;

import java.util.ArrayList;

public class SlidesAdapter extends FragmentStatePagerAdapter
{
    private ArrayList<SlideFragment> fragments = new ArrayList<>();

    public SlidesAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }

    @Override
    public SlideFragment getItem(int position)
    {
        return fragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        SlideFragment fragment = (SlideFragment) super.instantiateItem(container, position);
        fragments.set(position, fragment);
        return fragment;
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    public int getCalculatedCount()
    {
        if (fragments.size() > 0 && fragments.get(fragments.size() - 1) instanceof LastEmptySlideFragment)
        {
            return fragments.size() - 1;
        }
        else
        {
            return fragments.size();
        }
    }

    public void addItem(SlideFragment fragment)
    {
        fragments.add(getCalculatedCount(), fragment);
        this.notifyDataSetChanged();
    }

    public void addEmptySlide(LastEmptySlideFragment fragment)
    {
        fragments.add(fragment);
        this.notifyDataSetChanged();
    }

    public int getLastItemPosition()
    {
        return getCalculatedCount() - 1;
    }

    public boolean isLastSlide(int position)
    {
        return position == getCalculatedCount() - 1;
    }

    public boolean shouldFinish(int position)
    {
        return position == getCalculatedCount();
    }
}
