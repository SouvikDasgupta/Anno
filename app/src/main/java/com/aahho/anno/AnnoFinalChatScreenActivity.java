package com.aahho.anno;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aahho.anno.model.Channels;
import com.aahho.anno.model.Message;
import com.aahho.anno.model.Users;
import com.aahho.anno.utility.Utility;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by souvikdas on 10/10/17.
 */

public class AnnoFinalChatScreenActivity extends AppCompatActivity {
    private Users actor;
    private Users receiver;
    private ImageView backPress;
    private TextView receiverName;
    private TextView receiverStatus;
    private ListView listView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference channelsRefrence;
    private Utility utility;
    private AnnoChatScreenListAdapter adapter;
    private ImageView send;
    private ImageView emoji;
    private EmojiEditText textMessage;
    private String expectedChannelId;
    public static AnnoFinalChatScreenActivity finalChatScreenActivity;

    protected void onCreate(Bundle savedBundleInstance){
        super.onCreate(savedBundleInstance);
        EmojiManager.install(new GoogleEmojiProvider());
        setContentView(R.layout.chat_screen);
        actor = (Users) getIntent().getSerializableExtra("actor");
        receiver = (Users) getIntent().getSerializableExtra("receiver");
        backPress =(ImageView) findViewById(R.id.back_press);
        receiverName = (TextView) findViewById(R.id.chat_receiver_name);
        receiverStatus = (TextView) findViewById(R.id.chat_receiver_status);
        listView = (ListView) findViewById(R.id.chat_screen_lview);
        textMessage = (EmojiEditText) findViewById(R.id.textmessage);
        send = (ImageView) findViewById(R.id.send);
        emoji = (ImageView) findViewById(R.id.emoji);
        receiverName.setText(receiver.getUsername());
        receiverStatus.setText(receiver.getStatus());
        utility = new Utility();
        finalChatScreenActivity = this;
        expectedChannelId = utility.getId(actor, receiver);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        channelsRefrence = firebaseDatabase.getReference().child("channels");
        channelsRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    if(dataSnapshot.hasChild(expectedChannelId)){
                        initiateListView(dataSnapshot.child(expectedChannelId).getValue(Channels.class), actor.getId());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO
            }
        });

        //Sending a message onClick send
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Message message = new Message();
                message.setSenderId(actor.getId());
                message.setReceiverId(receiver.getId());
                message.setTextMessage(textMessage.getText().toString().trim());
                channelsRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(expectedChannelId)){
                            Channels channel = dataSnapshot.child(expectedChannelId).getValue(Channels.class);
                            ArrayList<Message> messageList = channel.getMessages();
                            messageList.add(message);
                            Map<String, Object> map = new HashMap<>();
                            map.put("messages", messageList);
                            channelsRefrence.child(expectedChannelId).updateChildren(map);
                            initiateListView(dataSnapshot.child(expectedChannelId).getValue(Channels.class), actor.getId());
                        }else{
                            final Channels channel = new Channels();
                            channel.setChannelId(expectedChannelId);
                            ArrayList<Message> messages = channel.getMessages();
                            messages.add(message);
                            channel.setMessages(messages);
                            ArrayList<Users> involved = channel.getInvolved();
                            involved.add(actor);
                            involved.add(receiver);
                            channel.setInvolved(involved);
                            channel.setInitiator(actor.getId());
                            channelsRefrence.child(channel.getChannelId()).setValue(channel);
                            initiateListView(dataSnapshot.child(expectedChannelId).getValue(Channels.class), actor.getId());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

//        channelsRefrence.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Channels tempChannel = (Channels) dataSnapshot.child(expectedChannelId).getValue();
//                initiateListView(tempChannel, actor.getId());
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Channels tempChannel = (Channels) dataSnapshot.child(expectedChannelId).getValue();
//                initiateListView(tempChannel, actor.getId());
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    public void initiateListView(Channels channel, final String actorId){
        adapter = new AnnoChatScreenListAdapter(channel, actorId);
        listView.setAdapter(adapter);
        listView.setSelection(listView.getAdapter().getCount()-1);

    }
}
