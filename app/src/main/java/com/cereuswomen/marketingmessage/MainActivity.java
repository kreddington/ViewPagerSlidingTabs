
/*
 * Copyright (c) 2015. Cereus Women and Kim Reddington. All rights reserved worldwide.
 */

package com.cereuswomen.marketingmessage;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cereuswomen.marketingmessage.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private  static final String LOG_TAG = "MainActivity";

    private static final int FRAGMENT_TOP_CLIENTS = 0;
    private static final int FRAGMENT_TARGET_MARKET = 1;
    private static final int FRAGMENT_CHALLENGES = 2;
    private static final int FRAGMENT_RESULTS = 3;
    private static final int FRAGMENT_BIG_CHALLENGE = 4;
    private static final int FRAGMENT_BIG_RESULT = 5;
    private static final int FRAGMENT_MESSAGE = 6;

    private static final int BUTTON_TOP_CLIENTS = R.id.action_top_clients_next;
    private static final int BUTTON_TARGET_MARKET = R.id.action_target_market_next;
    private static final int BUTTON_CHALLENGES = R.id.action_challenges_next;
    private static final int BUTTON_RESULTS = R.id.action_results_next;
    private static final int BUTTON_BIG_CHALLENGE = R.id.action_big_challenge_next;
    private static final int BUTTON_BIG_RESULT = R.id.action_big_result_next;

    // variables used key-value pairs when saving/restoring the instance state.
    private static final String POSITION = "position";
    private static final String USER = "user";

    //private static final String TABS_ADAPTER = "tabsAdapter";
    private static final String FRAGMENT_LIST = "fragmentList";
    //private static final String SLIDING_TAB_LAYOUT = "slidingTabLayout";

    List<Fragment> mFragmentList = new ArrayList<Fragment>();
    SlidingTabLayout mSlidingTabLayout;
    TabsAdapter mTabsAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the View Pager elements to be used once user logs in.
        setContentView(R.layout.activity_main);
        mViewPager = new ViewPager(this);

        List<Fragment> fragments = addFragment(0);
        mTabsAdapter = new TabsAdapter(getSupportFragmentManager(), fragments);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mTabsAdapter);
        mSlidingTabLayout.setViewPager(mViewPager);

        if (savedInstanceState == null) {
            // User must log in before using app.
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);


        } else {
            //grab key-value pairs passed through savedInstanceState
            int position = savedInstanceState.getInt(POSITION);
            //TODO: once the user process is turned back on, then we can save the data entered and re-populate here
//            int user = savedInstanceState.getInt(USER);

            //populate any other fragments needed based on the step user is on
            int count = 1;
            while (count <= position) {
                addFragment(count);
                count++;
            }


            //NOTE: Data in fields of each fragment remain intact when rotating screen.
        }

        // The main screen that will be displayed after the user logins.



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(POSITION, mTabsAdapter.getCount()-1);
        //TODO: Once login process is turned back on, we need to save the user id and then repopulate in onCreate
//        savedInstanceState.putInt(USER, );

        // Save the view hierarchy state.
        super.onSaveInstanceState(savedInstanceState);

    }

        // Creates a list of fragments in the order that they will displayed in the ViewPager.
    private List<Fragment> addFragment(int position) {

        switch (position) {
            case FRAGMENT_TOP_CLIENTS:
                mFragmentList.add(TopClientsFragment.newInstance());
                break;
            case FRAGMENT_TARGET_MARKET:
                mFragmentList.add(TargetMarketFragment.newInstance());
                break;
            case FRAGMENT_CHALLENGES:
                mFragmentList.add(ChallengesFragment.newInstance());
                break;
            case FRAGMENT_RESULTS:
                mFragmentList.add(ResultsFragment.newInstance());
                break;
            case FRAGMENT_BIG_CHALLENGE:
                mFragmentList.add(BigChallengeFragment.newInstance());
                break;
            case FRAGMENT_BIG_RESULT:
                mFragmentList.add(BigResultFragment.newInstance());
                break;
            case FRAGMENT_MESSAGE:
                mFragmentList.add(MessageFragment.newInstance());
                break;
        }

        // If position is 0, then we don't need to update the tabs adapter, it is already initialized through TabsAdapter constructor.
        // Otherwise, we need to update the view.
        if (position > 0) {
            mTabsAdapter.updateFragments(position, mFragmentList);
        }

        return mFragmentList;
    }

//    private void analyzeNextSelection(int fragmentPosition, int position){
//        if (position < fragmentPosition) {
//            addFragment(fragmentPosition);
//        } else {
//            mTabsAdapter.setFragmentSelection(position);
//        }
//    }

    // Defined in each fragment's XML and called when the button is clicked to add the next fragment.
    public void onNextFragment(View view) {
        int count = mTabsAdapter.getCount();
        int position = count - 1;

        // Determine which NEXT button was pushed.
        // Hide the button, since user can just swipe right to go to the next view.
        // Determine which fragment should be created next.
        if (view == findViewById(BUTTON_TOP_CLIENTS)) {
            findViewById(BUTTON_TOP_CLIENTS).setVisibility(View.GONE);
            addFragment(FRAGMENT_TARGET_MARKET);
        } else if (view == findViewById(BUTTON_TARGET_MARKET)) {
            findViewById(BUTTON_TARGET_MARKET).setVisibility(View.GONE);
            addFragment(FRAGMENT_CHALLENGES);
        } else if (view == findViewById(BUTTON_CHALLENGES)) {
            findViewById(BUTTON_CHALLENGES).setVisibility(View.GONE);
            addFragment(FRAGMENT_RESULTS);
        } else if (view == findViewById(BUTTON_RESULTS)) {
            findViewById(BUTTON_RESULTS).setVisibility(View.GONE);
            addFragment(FRAGMENT_BIG_CHALLENGE);
        } else if (view == findViewById(BUTTON_BIG_CHALLENGE)) {
            findViewById(BUTTON_BIG_CHALLENGE).setVisibility(View.GONE);
            addFragment(FRAGMENT_BIG_RESULT);
        } else if (view == findViewById(BUTTON_BIG_RESULT)) {
            findViewById(BUTTON_BIG_RESULT).setVisibility(View.GONE);
            addFragment(FRAGMENT_MESSAGE);
        }
    }

    public void onSave(View view) {

    }

    class TabsAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public TabsAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Resources res = getResources();
            TypedArray icons = res.obtainTypedArray(R.array.icons);
            // TODO: replace the string array with icons (Drawable)
            // TODO: Create ability to set a selected icon
            String drawable = icons.getString(position);
            icons.recycle();
            return drawable;
        }

        public void updateFragments(int position, List<Fragment> fragments) {
            this.fragments = fragments;
            mSlidingTabLayout.populateTabStrip(position);
            setFragmentSelection(position);

        }

        public void setFragmentSelection(int position) {
            notifyDataSetChanged();
            mViewPager.setCurrentItem(position);
        }

    }
}
