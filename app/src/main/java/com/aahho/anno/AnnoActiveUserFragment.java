package com.aahho.anno;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aahho.anno.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.aahho.anno.R.drawable.user;

/**
 * Created by souvikdas on 26/9/17.
 */

public class AnnoActiveUserFragment extends Fragment {
    private Users actor;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Users> onlineUsers;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater
            , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.online_fragments, container, false);
        actor = (Users) getArguments().getSerializable("user");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                onlineUsers = new ArrayList<>();
                for(DataSnapshot snap : snapshot.getChildren()){
                    Users u = snap.getValue(Users.class);
                    if((u.getStatus().equals("Online")) && !(u.getId().equals(actor.getId()))){
                        onlineUsers.add(u);
                    }
                }

                recyclerView = (RecyclerView) view.findViewById(R.id.online_user_rview);
                LinearLayoutManager lManager = new LinearLayoutManager(AnnoMainScreenActivity.mainScreenActivity, LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(lManager);
                AnnoOnlineUserAdapter adapter = new AnnoOnlineUserAdapter(onlineUsers);
                recyclerView.setAdapter(adapter);
                adapter.SetOnItemClickListener(new AnnoOnlineUserAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(AnnoMainScreenActivity.mainScreenActivity, AnnoChatScreenActivity.class));
                        intent.putExtra("actor", actor);
                        intent.putExtra("receiver", onlineUsers.get(position));
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        return view;
    }
}
