package com.aahho.anno;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.aahho.anno.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by souvikdas on 4/11/17.
 */

public class AnnoActiveUserActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userReference;
    private Users actor;
    private ArrayList<Users> onlineUsers;
    private RecyclerView recyclerView;
    private ImageView backPress;
    public static AnnoActiveUserActivity activeUserActivity;
    @Override
    protected void onCreate(Bundle savedBundleInstance){
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.online_fragments);
        activeUserActivity = this;
        actor = (Users) getIntent().getSerializableExtra("actor");
        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("users");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onlineUsers = new ArrayList<Users>();
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    Users u = snap.getValue(Users.class);
                    if((u.getStatus().equals("Online")) && !(u.getId().equals(actor.getId()))){
                        onlineUsers.add(u);
                    }
                }

                recyclerView = (RecyclerView) findViewById(R.id.online_user_rview);
                LinearLayoutManager lManager = new LinearLayoutManager(AnnoActiveUserActivity.activeUserActivity, LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(lManager);
                AnnoOnlineUserAdapter adapter = new AnnoOnlineUserAdapter(onlineUsers);
                recyclerView.setAdapter(adapter);
                adapter.SetOnItemClickListener(new AnnoOnlineUserAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(AnnoActiveUserActivity.activeUserActivity, AnnoChatScreenActivity.class));
                        intent.putExtra("actor", actor);
                        intent.putExtra("receiver", onlineUsers.get(position));
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        backPress = (ImageView) findViewById(R.id.back_press_active);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }
}
