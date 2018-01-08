package com.aahho.anno;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.aahho.anno.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by souvikdas on 18/9/17.
 */

public class AnnoOnlineUserActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Users user;
    public static AnnoOnlineUserActivity onlineUserContext;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Users> onlineUsers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.online_fragments);
        onlineUserContext = this;
        user = (Users) getIntent().getSerializableExtra("user");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    Users u = snap.getValue(Users.class);
                    if((u.getStatus().equals("Online")) && !(u.getId().equals(user.getId()))){
                        onlineUsers.add(u);
                    }
                }

                recyclerView = (RecyclerView) findViewById(R.id.online_user_rview);
                LinearLayoutManager lManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(lManager);
                AnnoOnlineUserAdapter adapter = new AnnoOnlineUserAdapter(onlineUsers);
                recyclerView.setAdapter(adapter);
                adapter.SetOnItemClickListener(new AnnoOnlineUserAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(AnnoOnlineUserActivity.this, AnnoFinalChatScreenActivity.class));
                        intent.putExtra("actor", user);
                        intent.putExtra("receiver", onlineUsers.get(position));
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
