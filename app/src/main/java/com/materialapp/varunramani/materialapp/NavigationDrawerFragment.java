package com.materialapp.varunramani.materialapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@l} subclass.
 */
public class NavigationDrawerFragment extends Fragment /*implements Adapter.ClickListener*/ {

    private RecyclerView recyclerView;
    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    public static final String KEY_FROM_SAVED_INSTANCE_STATE = "";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;

    private boolean mUserLEarnedDrawer;
    private boolean mFromSavedInstanceState;
    private Adapter adapter;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLEarnedDrawer = Boolean.parseBoolean(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        adapter = new Adapter(getActivity(), getData());
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void OnClick(View v, int position) {
                //Toast.makeText(getActivity(), "OnClick called " + position, Toast.LENGTH_LONG).show();
                if (position == 0) {
                    startActivity(new Intent(getActivity(), SubActivity.class));
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (position == 1) {// || position == 2 || position == 3) {
                    //Toast.makeText(getActivity(), "Cannot start Activity", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), TabActivityWithLibrary.class));
                }
            }

            @Override
            public void OnLongClick(View v, int position) {
                Toast.makeText(getActivity(), "OnLongClick called " + position, Toast.LENGTH_LONG).show();
            }
        }));
        return layout;
    }

    public static List<Information> getData() {
        List<Information> data = new ArrayList<>();
        int[] icons = {R.drawable.abc_btn_check_material, R.drawable.abc_btn_check_material, R.drawable.abc_btn_check_material, R.drawable.abc_btn_check_material};
        String[] titles = {"SubActivity", "Movies", "SubActivity3", "SubActivity4"};
        //can be changed to add more items by changing the number
        for (int i = 0; i < 4; i++) {
            Information current = new Information();
            current.iconId = icons[i % titles.length];
            current.strTitle = titles[i % titles.length];
            data.add(current);
        }
        return data;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        //for the hamburger icon
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLEarnedDrawer) {
                    mUserLEarnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLEarnedDrawer + "");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            /*@Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(slideOffset<0.6){
                    toolbar.setAlpha(1-slideOffset);
                }
            }*/
        };

        if (!mUserLEarnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();

    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);

    }

    /*@Override
    public void itemClicked(View view, int position) {
        *//*int id = view.getId();
        Toast.makeText(getActivity(), "ID is:" + id, Toast.LENGTH_LONG).show();*//*
        startActivity(new Intent(getActivity(), SubActivity.class));
    }*/

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {


        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            Log.d("VRR", "Constructer Called");
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    //Log.d("VRR", "onSingleTapUp " + e);
                    return true;/*super.onSingleTapUp(e)*/
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if (child != null && clickListener != null) {
                        clickListener.OnLongClick(child, recyclerView.getChildPosition(child));
                    }

                    //Log.d("VRR", "onLongPress " + e);
                    //super.onLongPress(e);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.OnClick(child, rv.getChildPosition(child));
            }

            //Log.d("VRR", "RecyclerTouchListener onInterceptTouchEvent called " + gestureDetector.onTouchEvent(e) +" " +e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.d("VRR", "RecyclerTouchListener onTouchEvent called " + e);
        }
    }

    public static interface ClickListener {
        public void OnClick(View v, int position);

        public void OnLongClick(View v, int position);
    }
}
