package iitmad.com.a20425418.newsgateway.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Ashok Kumar - A20425418 on 11/21/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class DrawerPagerAdapter extends FragmentPagerAdapter
{
    private long baseId = 0;
    private List<Fragment> fragments;

    public DrawerPagerAdapter(FragmentManager fm,List<Fragment> tempFragmentList) {
        super(fm);
        fragments = tempFragmentList;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public long getItemId(int position) {
        // give an ID different from position when position has been changed
        return baseId + position;
    }

    /**
     * Notify that the position of a fragment has been changed.
     * Create a new ID for each position to force recreation of the fragment
     *
     * @param n number of items which have been changed
     */
    public void notifyChangeInPosition(int n) {
        // shift the ID returned by getItemId outside the range of all previous fragments
        baseId += getCount() + n;
    }
}
