package com.aahho.anno;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aahho.anno.model.Users;
import com.aahho.anno.utility.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by souvikdas on 26/9/17.
 */

public class AnnoMainScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private CharSequence [] titles = {"Active", "Chats"};
    private SlidingTabLayout slidingTabLayout;
    private ViewPager pager;
    private ViewPagerAdapter viewPagerAdapter;
    private Users actor;
    public static AnnoMainScreenActivity mainScreenActivity;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View navHeader;
    private NavigationView mDrawerList;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedBundleInstance){
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.activity_main_screen);
        mainScreenActivity = this;
        toolbar = (Toolbar) findViewById(R.id.toolbarmain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerList = (NavigationView)findViewById(R.id.navigation);
        navHeader = mDrawerList.getHeaderView(0);
        ImageView navImage = navHeader.findViewById(R.id.navhead);
//        navImage.setImageDrawable(getResources().getDrawable(R.drawable.prof));
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList.setNavigationItemSelectedListener(this);
        mDrawerList.setItemIconTintList(null);

        setupDrawer();
        sessionManager = new SessionManager(getApplicationContext());
        if(!sessionManager.isLoggedIn()){
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(AnnoMainScreenActivity.this, AnnoLoginActivity.class));
            startActivity(intent);
            finish();
        }else{
            final Map<String, String> userDetails = sessionManager.getUserDetails();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("users");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot childSnap = dataSnapshot.child(userDetails.get("userId"));
                    actor = childSnap.getValue(Users.class);
//                    mDrawerList.getMenu().getItem(R.id.group1);
                    Log.i("username", actor.getUsername());
                    Map<String, Object> map = new HashMap<>();
                    map.put("status", "Online");
                    map.put("fcmToken", sessionManager.getFcmToken());
                    databaseReference.child(actor.getId()).updateChildren(map);
                    slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slide_tab);
                    pager = (ViewPager) findViewById(R.id.pager);
                    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, actor);
                    pager.setAdapter(viewPagerAdapter);
                    slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                        @Override
                        public int getIndicatorColor(int position) {
                            return getResources().getColor(R.color.tabsScrollColor);
                        }
                    });
                    slidingTabLayout.setDistributeEvenly(true);
                    slidingTabLayout.setViewPager(pager);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(actor!=null){
            Map<String, Object> map = new HashMap<>();
            map.put("status", "Offline");
            databaseReference.child(actor.getId()).updateChildren(map);

        }

    }

    @Override
    public void onPause(){
        super.onPause();
        if(actor!=null){
            Map<String, Object> map = new HashMap<>();
            map.put("status", "Offline");
            databaseReference.child(actor.getId()).updateChildren(map);

        }
    }

    @Override
    public void onResume(){
        super.onResume();
        final Map<String, String> userDetails = sessionManager.getUserDetails();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot childSnap = dataSnapshot.child(userDetails.get("userId"));
                actor = childSnap.getValue(Users.class);
                Log.i("username", actor.getUsername());
                Map<String, Object> map = new HashMap<>();
                map.put("status", "Online");
                map.put("fcmToken", sessionManager.getFcmToken());
                databaseReference.child(actor.getId()).updateChildren(map);
                slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slide_tab);
                pager = (ViewPager) findViewById(R.id.pager);
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, actor);
                pager.setAdapter(viewPagerAdapter);
                slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                    @Override
                    public int getIndicatorColor(int position) {
                        return getResources().getColor(R.color.tabsScrollColor);
                    }
                });
                slidingTabLayout.setDistributeEvenly(true);
                slidingTabLayout.setViewPager(pager);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    @Override
//    public void onStart(){
//        super.onStart();
//        Map<String, Object> map = new HashMap<>();
//        map.put("status", "Online");
//        databaseReference.child(actor.getId()).updateChildren(map);
//    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.user){
            Intent profile = new Intent();
            profile.setComponent(new ComponentName(AnnoMainScreenActivity.this, AnnoProfileActivity.class));
            finish();
            startActivity(profile);

            overridePendingTransition(R.anim.enter, R.anim.exit);
        }else if(itemId == R.id.logout){
            sessionManager.logout();
            Intent loginIntent = new Intent(this, AnnoLoginActivity.class);
            startActivity(loginIntent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
         }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (mDrawerToggle.onOptionsItemSelected(menuItem)) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
//
//        return true;
//    }
}
