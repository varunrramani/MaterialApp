package com.materialapp.varunramani.materialapp;

import android.content.ComponentName;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import service.MyService;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;


public class TabActivityWithLibrary extends ActionBarActivity implements MaterialTabListener, FragmentBoxOffice.OnFragmentInteractionListener, View.OnClickListener {

    private static final int JOB_ID = 100;
    private Toolbar toolbar;

    //Used the library from https://github.com/neokree/MaterialTabs
    private MaterialTabHost materialTabHost;
    private ViewPager viewPager;

    public static final int MOVIES_SEARCH_RESULT = 0;
    public static final int MOVIES_HITS = 1;
    public static final int MOVIES_UPCOMING = 2;

    private static final String TAG_SORT_NAME = "sortName";
    private static final String TAG_SORT_DATE = "sortDate";
    private static final String TAG_SORT_RATINGS = "sortRatings";
    ViewPagerAdapter adapter;
    private NavigationDrawerFragment drawerFragment;

    private JobScheduler mJobScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mJobScheduler = JobScheduler.getInstance(this);
        constructJob();

        setContentView(R.layout.activity_tab_activity_with_library);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //To show the back button on the action bar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true); //To show the back button on the action bar

        //To add the navigation drawer
        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_drawer_layout);
        drawerFragment.setUp(R.id.fragment_drawer_layout,(DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        //from the library to create tabs.
        materialTabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        //View pager is for the content in the tab
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                materialTabHost.setSelectedNavigationItem(position);
            }
        });

        //Tabs with icons or text can be changed in the layout file and accordingly use one of the following for loops

        //To add tabs with text
        /*for (int i = 0; i < adapter.getCount(); i++) {
            materialTabHost.addTab(materialTabHost.newTab()
                    .setText(adapter.getPageTitle(i))
                    .setTabListener(this));
        }*/

        //To add tabs with icons
        for (int i = 0; i < adapter.getCount(); i++) {
            materialTabHost.addTab(materialTabHost.newTab()
                    .setIcon(adapter.getDrawable(i))
                    .setTabListener(this));
        }

        loadFab();

    }


    private void constructJob(){
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, MyService.class));
        builder.setPeriodic(2000)
        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
        .setPersisted(true);

        mJobScheduler.schedule(builder.build());
    }

    private void loadFab(){
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.sort);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView, new FloatingActionButton.LayoutParams(64,64, Gravity.CENTER))
                //.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_button_bg))
                .build();


        ImageView iconSortName = new ImageView(this);
        iconSortName.setImageResource(R.drawable.movie);

        ImageView iconSortDate = new ImageView(this);
        iconSortDate.setImageResource(R.drawable.film);

        ImageView iconSortRatings = new ImageView(this);
        iconSortRatings.setImageResource(R.drawable.star);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        //itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_sub_button_bg));

        SubActionButton btnSortName = itemBuilder.setContentView(iconSortName, new FloatingActionButton.LayoutParams(32,32, Gravity.CENTER))
                .build();
        //for Click listener
        btnSortName.setTag(TAG_SORT_NAME);
        SubActionButton btnSortDate = itemBuilder.setContentView(iconSortDate, new FloatingActionButton.LayoutParams(32,32, Gravity.CENTER))
                .build();

        //for Click Listener
        btnSortDate.setTag(TAG_SORT_DATE);
        SubActionButton btnSortRatings = itemBuilder.setContentView(iconSortRatings, new FloatingActionButton.LayoutParams(32,32, Gravity.CENTER))
                .build();
        //for Click Listener
        btnSortRatings.setTag(TAG_SORT_RATINGS);

        btnSortName.setOnClickListener(this);
        btnSortDate.setOnClickListener(this);
        btnSortRatings.setOnClickListener(this);

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(btnSortName)
                .addSubActionView(btnSortDate)
                .addSubActionView(btnSortRatings)
                .attachTo(actionButton)
                .build();
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab_activity_with_library, menu);
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
        } else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        viewPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        String TAG = v.getTag().toString();
        Fragment fragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        //Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
        if(fragment instanceof SortListener){

            if(TAG.equals(TAG_SORT_NAME)){
                //Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
                ((SortListener) fragment).onSortByName();

            } else if (TAG.equals(TAG_SORT_DATE)){
                ((SortListener) fragment).onSortByDate();

            } else if (TAG.equals(TAG_SORT_RATINGS)){
                ((SortListener) fragment).onSortByRatings();

            }
        }



    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {


        int icons[] = {R.drawable.star,
                R.drawable.movie,
                R.drawable.film};
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            //return MyFragment.getInstance(position);

            Fragment fragment = null;

            switch (position){
                case MOVIES_SEARCH_RESULT:
                    fragment = FragmentSearch.newInstance("", "");
                    break;
                case MOVIES_HITS:
                    fragment = FragmentBoxOffice.newInstance("", "");
                    break;
                case MOVIES_UPCOMING:
                    fragment = FragmentUpcoming.newInstance("", "");
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            //return 7;
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }

        private Drawable getDrawable(int position){
            return getResources().getDrawable(icons[position]);
        }
    }
}
