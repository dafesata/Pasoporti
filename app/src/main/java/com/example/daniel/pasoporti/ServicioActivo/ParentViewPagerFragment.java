package com.example.daniel.pasoporti.ServicioActivo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daniel.pasoporti.R;

/**
 * Created by Daniel on 12/4/2017.
 */

public class ParentViewPagerFragment extends Fragment {

    public static final String TAG = ParentViewPagerFragment.class.getName();

    public static ParentViewPagerFragment newInstance() {
        return new ParentViewPagerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_parent_viewpager, container, false);

        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        /** Important: Must use the child FragmentManager or you will see side effects. */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        return root;
    }



    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem: "+String.valueOf(position));
            switch (position){
                case 0:
                    return MapFragment.newInstance(0,"1/2");
                case 1:
                    return DetailsFragment.newInstance(1,"2/2");
                default:
                    return null;
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Map Fragment";
                case 1:
                    return "Details Fragment";
            }
            return null;
        }

    }

}