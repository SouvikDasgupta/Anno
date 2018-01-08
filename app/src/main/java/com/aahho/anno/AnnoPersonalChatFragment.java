//package com.aahho.anno;
//
//import android.content.ComponentName;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.aahho.anno.model.Channels;
//import com.aahho.anno.model.ChatList;
//import com.aahho.anno.model.Users;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
///**
// * Created by souvikdas on 26/9/17.
// */
//
//public class AnnoPersonalChatFragment extends Fragment {
//    private Users actor;
//    private FirebaseDatabase firebaseDatabase;
//    private DatabaseReference databaseReference;
//    private ArrayList<ChatList> displayChatList;
//    private RecyclerView recyclerView;
//    private View view;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater
//            , @Nullable ViewGroup container, @Nullable Bundle savedBundleInstance){
//        view = inflater.inflate(R.layout.chat_fragments, container, false);
//        actor = (Users)getArguments().getSerializable("user");
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference().child("channels");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                displayChatList = new ArrayList<>();
//                for(DataSnapshot snap : snapshot.getChildren()){
//                    Channels channel = snap.getValue(Channels.class);
//                    ArrayList<Users> invovledUserId = channel.getInvolved();
//                    if(invovledUserId.contains(actor.getId())){
//                        final ChatList chatList = new ChatList();
//                        String otherUserId = null;
//                        chatList.setLastMessage(channel.getMessages().get(channel.getMessages().size()-1).getTextMessage());
//                        for(int i = 0 ; i < invovledUserId.size(); i++){
//                            if(!(invovledUserId.get(i).equals(actor.getId()))){
//                                otherUserId = invovledUserId.get(i);
//                            }
//                        }
//                        firebaseDatabase.getReference().child("users").child(otherUserId).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot snapshot1) {
//                                Users otherUser = snapshot1.getValue(Users.class);
//                                chatList.setOtherUser(otherUser);
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError error) {
//
//                            }
//                        });
//                        displayChatList.add(chatList);
//                    }
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//
//            }
//        });
//        return view;
//    }
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser){
//        super.setUserVisibleHint(isVisibleToUser);
//        if(isVisibleToUser){
//
//            recyclerView = (RecyclerView) view.findViewById(R.id.chat_personal_rview);
//            LinearLayoutManager lManager = new LinearLayoutManager(AnnoMainScreenActivity.mainScreenActivity, LinearLayoutManager.VERTICAL,false);
//            recyclerView.setLayoutManager(lManager);
//            AnnoPersonalChatAdapter adapter = new AnnoPersonalChatAdapter(displayChatList);
//            recyclerView.setAdapter(adapter);
//            adapter.SetOnItemClickListener(new AnnoOnlineUserAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//
//                    Intent intent = new Intent();
//                    intent.setComponent(new ComponentName(AnnoMainScreenActivity.mainScreenActivity, AnnoChatScreenActivity.class));
//                    intent.putExtra("actor", actor);
//                    intent.putExtra("receiver", displayChatList.get(position).getOtherUser());
//                    startActivity(intent);
//                }
//            });
//
//        }
//    }
//}
