package com.example.seniorproject.smartshopping.controller.fragment.shoppinglistfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.ShoppingListMap;
import com.example.seniorproject.smartshopping.view.specialview.SlidingTabLayout;


public class MoreShoppingListItemFragment extends Fragment {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private ViewPager viewPager;
    private ShoppingListMap shoppingListMap;
    private SlidingTabLayout slidingTabLayout;
    private int position;



    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public MoreShoppingListItemFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MoreShoppingListItemFragment newInstance(ShoppingListMap shoppingListMap, int position) {
        MoreShoppingListItemFragment fragment = new MoreShoppingListItemFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putParcelable("shoppingListMap", shoppingListMap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
        shoppingListMap = getArguments().getParcelable("shoppingListMap");
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_more_shopping_list_item, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int page) {
                switch (page){
                    case 0:
                        return MoreShoppingListItemUpdateFragment.newInstance(shoppingListMap);
                    case 1:
                        return MoreShoppingListItemOptimizeFragment.newInstance(shoppingListMap);
                    default:
                        return null;

                }
            }
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "Items";
                    case 1:
                        return "Optimize";
                    default:
                        return "";
                }
            }
        });

        slidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.slidingTabLayout);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/


    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

}
