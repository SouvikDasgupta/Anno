package com.aahho.anno;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aahho.anno.model.Channels;
import com.aahho.anno.model.ChatList;
import com.aahho.anno.model.Users;
import com.aahho.anno.utility.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

/**
 * Created by souvikdas on 3/11/17.
 */

public class AnnoUserMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Users actor;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference channelReference;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View navHeader;
    private NavigationView mDrawerList;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SessionManager sessionManager;
    public static AnnoUserMainActivity userMainActivity;
    private RecyclerView recylerView;
    private ArrayList<ChatList> displayChatList;
    private String otherUserId;
    private FloatingActionButton getActiveUser;
    private ChatList chatList;
    private ImageView search;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedBundleInstance){
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.user_main_screen);
        userMainActivity = this;
        firebaseDatabase = FirebaseDatabase.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbarmain);
        getActiveUser = (FloatingActionButton) findViewById(R.id.fab);
//        search = (ImageView) findViewById(R.id.search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerList = (NavigationView)findViewById(R.id.navigation);
        navHeader = mDrawerList.getHeaderView(0);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList.setNavigationItemSelectedListener(userMainActivity);
        mDrawerList.setItemIconTintList(null);
        setupDrawer();
        sessionManager = new SessionManager(getApplicationContext());
        if(!sessionManager.isLoggedIn()){
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(AnnoUserMainActivity.this, AnnoLoginActivity.class));
            startActivity(intent);
            finish();
        }else{
            final Map<String, String> userDetails = sessionManager.getUserDetails();

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
                    fetchList();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



        getActiveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnnoUserMainActivity.this, AnnoActiveUserActivity.class);
                intent.putExtra("actor", actor);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onSearchRequested();
//            }
//        });
    }


    @Override
    public boolean onSearchRequested(){
        return super.onSearchRequested();
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
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (mDrawerToggle.onOptionsItemSelected(menuItem)) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void initiateRecylerView(){
        recylerView = (RecyclerView) findViewById(R.id.user_main_rview);
        LinearLayoutManager lManager = new LinearLayoutManager(AnnoUserMainActivity.userMainActivity, LinearLayoutManager.VERTICAL,false);
        recylerView.setLayoutManager(lManager);
        AnnoPersonalChatAdapter adapter = new AnnoPersonalChatAdapter(displayChatList);
        recylerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(new AnnoOnlineUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent();
                intent.setComponent(new ComponentName(AnnoUserMainActivity.userMainActivity, AnnoChatScreenActivity.class));
                intent.putExtra("actor", actor);
                intent.putExtra("receiver", displayChatList.get(position).getOtherUser());
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    private void fetchList(){
        channelReference = firebaseDatabase.getReference().child("channels");
        Query queryData = channelReference.orderByChild("lastMessageTime");

        queryData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                displayChatList = new ArrayList<ChatList>();
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    final Channels channel = childSnapshot.getValue(Channels.class);
                    ArrayList<Users> invovledUser = channel.getInvolved();
                    Log.i("Size of involved user", String.valueOf(invovledUser.size()));
                    for(int i = 0; i < invovledUser.size(); i++){
                        if(invovledUser.get(i).getId().equals(actor.getId())){
                            chatList = new ChatList();
                            chatList.setLastMessage(channel.getMessages().get(channel.getMessages().size()-1).getTextMessage());
                            if( i == 0){
                                chatList.setOtherUser(invovledUser.get(1));
                            }else{
                                chatList.setOtherUser(invovledUser.get(0));
                            }

                            displayChatList.add(chatList);
                        }
                    }
                    Collections.reverse(displayChatList);

                }
                initiateRecylerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
