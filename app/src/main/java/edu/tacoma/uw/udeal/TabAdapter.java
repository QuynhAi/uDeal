package edu.tacoma.uw.udeal;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mCartFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    TabAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position){
        return mCartFragments.get(position);
    }

    public void addFragment(Fragment fragment, String title){
        mCartFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int postion){
        return mFragmentTitles.get(postion);
    }

    @Override
    public int getCount() {
        return mCartFragments.size();
    }
}
