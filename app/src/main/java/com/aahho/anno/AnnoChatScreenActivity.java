package com.aahho.anno;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aahho.anno.api.ServiceAPI;
import com.aahho.anno.model.Channels;
import com.aahho.anno.model.Message;
import com.aahho.anno.model.NotificationRequest;
import com.aahho.anno.model.NotificationRequestData;
import com.aahho.anno.model.NotificationResponse;
import com.aahho.anno.model.Upload;
import com.aahho.anno.model.Users;
import com.aahho.anno.utility.RetrofitClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by souvikdas on 21/9/17.
 */

public class AnnoChatScreenActivity extends AppCompatActivity {
    private TextView receiverName;
    private TextView receiverStatus;
    private Users actor;
    private Users receiver;
    private ImageView backPress;
    private ImageView send;
    private ImageView emoji;
    private ImageView display;
    private EmojiEditText textMessage;
    public static AnnoChatScreenActivity chatScreenContext;
    private ListView listView;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseDatabase firebaseDatabase1;
    private DatabaseReference channelsRefrence;
    private DatabaseReference databaseReference;
    private String channelId;
    private AnnoChatScreenListAdapter adapter;
    private DatabaseReference userReference;
    private static int count = 0;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        EmojiManager.install(new GoogleEmojiProvider());
        setContentView(R.layout.chat_screen);
        chatScreenContext = this;
        actor = (Users) getIntent().getSerializableExtra("actor");
        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference().child("users");
        Map<String, Object> map = new HashMap<>();
        map.put("status", "Online");
        userReference.child(actor.getId()).updateChildren(map);
        receiver = (Users) getIntent().getSerializableExtra("receiver");
        backPress =(ImageView) findViewById(R.id.back_press);
        receiverName = (TextView) findViewById(R.id.chat_receiver_name);
        receiverStatus = (TextView) findViewById(R.id.chat_receiver_status);
        listView = (ListView) findViewById(R.id.chat_screen_lview);
        display = (ImageView) findViewById(R.id.chat_image);
        if(receiver.getAnonymousName()!=null){
            receiverName.setText(receiver.getAnonymousName());
        }else{
            receiverName.setText(receiver.getUsername());
        }
        if(receiver.getDpId()!=null){
            firebaseDatabase1 = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase1.getReference().child("chat").child("uploads");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(receiver.getDpId()!=null){
                        DataSnapshot childSnap = dataSnapshot.child(receiver.getDpId());
                        Upload data = childSnap.getValue(Upload.class);
                        if(data.getWebViewLinks().getThumbnail()!=null){
                            Picasso.with(AnnoChatScreenActivity.chatScreenContext).load(data.getWebViewLinks().getThumbnail()).into(display);

                        }
//                    Picasso.with(AnnoChatScreenActivity.chatScreenContext).load(data.getSelf_link()).into(display);
//                    Toast.makeText(AnnoMainScreenActivity.mainScreenActivity, data.getSelf_link(), Toast.LENGTH_SHORT).show();
//                        display.setImageURI(Uri.parse(data.getSelf_link()));

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        receiverStatus.setText(receiver.getStatus());
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        channelsRefrence = firebaseDatabase.getReference().child("channels");

        channelsRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    String tChannelId;
                    if(snapshot.hasChild(actor.getId() + "@_" + receiver.getId())){
                        tChannelId = actor.getId() + "@_" + receiver.getId();
                        initiateListView(snapshot.child(tChannelId).getValue(Channels.class), actor.getId());
                    }else if(snapshot.hasChild(receiver.getId() + "@_" + actor.getId())){
                        tChannelId = receiver.getId() + "@_" + actor.getId();
                        initiateListView(snapshot.child(tChannelId).getValue(Channels.class), actor.getId());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        textMessage = (EmojiEditText) findViewById(R.id.textmessage);
        send = (ImageView) findViewById(R.id.send);
        emoji = (ImageView) findViewById(R.id.emoji);
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(keyBoardEmo){
//                    emoji.setImageDrawable(getResources().getDrawable(R.drawable.emoji));
//                    InputMethodManager imm = (InputMethodManager)
//                            getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                    keyBoardEmo = false;
//
//                }else {
                final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(getWindow()
                        .getDecorView().findViewById(android.R.id.content)).build(textMessage);
                emojiPopup.toggle(); // Toggles visibility of the Popup.

//                }

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Retrofit client = new  RetrofitClient().createRetrofitClient();
                final ServiceAPI serviceApi = client.create(ServiceAPI.class);
                final Message message = new Message();
                message.setSenderId(actor.getId());
                message.setReceiverId(receiver.getId());
                message.setTextMessage(textMessage.getText().toString().trim());
                final NotificationRequest notification = new NotificationRequest();
                NotificationRequestData data = new NotificationRequestData();
                data.setMessageTitle(actor.getUsername());
                data.setMessageTitle(message.getTextMessage());
                notification.setData(null);
                ArrayList<String> deviceIds = new ArrayList<String>();
                deviceIds.add(receiver.getFcmToken());
                notification.setDeviceIds(deviceIds);
                notification.setMessageBody(message.getTextMessage());
                notification.setMessageTitle(actor.getUsername());
                firebaseDatabase = FirebaseDatabase.getInstance();
                channelsRefrence = firebaseDatabase.getReference().child("channels");
                channelsRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            final String tempChannelId;
                            if((snapshot.hasChild(actor.getId() +"@_" + receiver.getId()))){
                                tempChannelId = actor.getId() + "@_" + receiver.getId();
                                final DataSnapshot snap = snapshot.child(tempChannelId);
                                Channels c = snap.getValue(Channels.class);
                                ArrayList<Message> cMessages = c.getMessages();
                                cMessages.add(message);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("messages", cMessages);
                                map.put("lastMessageTime", System.currentTimeMillis());
                                channelsRefrence.child(c.getChannelId()).updateChildren(map);
                                Call<NotificationResponse> notificationResponse = serviceApi.sendNotification(notification);
                                notificationResponse.enqueue(new Callback<NotificationResponse>() {
                                    @Override
                                    public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                                        if(response.isSuccessful()){
                                            Log.i("message sent", "success");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<NotificationResponse> call, Throwable t) {

                                    }
                                });
                                channelsRefrence.child(c.getChannelId()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        Channels tempChannel = snapshot.getValue(Channels.class);
                                        initiateListView(tempChannel, actor.getId());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {

                                    }
                                });
                            }else if(snapshot.hasChild(receiver.getId()+ "@_" + actor.getId())){
                                tempChannelId = receiver.getId()+ "@_" + actor.getId();
                                DataSnapshot snap = snapshot.child(tempChannelId);
                                Channels c = snap.getValue(Channels.class);
                                ArrayList<Message> cMessages = c.getMessages();
                                cMessages.add(message);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("messages", cMessages);
                                map.put("lastMessageTime", System.currentTimeMillis());
                                channelsRefrence.child(c.getChannelId()).updateChildren(map);
                                Call<NotificationResponse> notificationResponse = serviceApi.sendNotification(notification);
                                notificationResponse.enqueue(new Callback<NotificationResponse>() {
                                    @Override
                                    public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                                        if(response.isSuccessful()){
                                            Log.i("message sent", "success");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<NotificationResponse> call, Throwable t) {

                                    }
                                });
                                channelsRefrence.child(c.getChannelId()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        Channels tempChannel = snapshot.getValue(Channels.class);
                                        initiateListView(tempChannel, actor.getId());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {

                                    }
                                });
                            }else {
                                final Channels channel = new Channels();
                                channel.setChannelId(actor.getId() + "@_" + receiver.getId());
                                ArrayList<Message> messages = channel.getMessages();
                                messages.add(message);
                                channel.setMessages(messages);
                                ArrayList<Users> involved = channel.getInvolved();
                                involved.add(actor);
                                involved.add(receiver);
                                channel.setInvolved(involved);
                                channel.setInitiator(actor.getId());
                                channel.setLastMessageTime(System.currentTimeMillis());
                                channelsRefrence.child(channel.getChannelId()).setValue(channel);
                                Call<NotificationResponse> notificationResponse = serviceApi.sendNotification(notification);
                                notificationResponse.enqueue(new Callback<NotificationResponse>() {
                                    @Override
                                    public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                                        if(response.isSuccessful()){
                                            Log.i("message sent", "success");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<NotificationResponse> call, Throwable t) {

                                    }
                                });
                                channelsRefrence.child(channel.getChannelId()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        Channels tempChannel = snapshot.getValue(Channels.class);
                                        initiateListView(tempChannel, actor.getId());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {

                                    }
                                });
                            }
                        }else{
                            final Channels channel = new Channels();
                            channel.setChannelId(actor.getId() + "@_" + receiver.getId());
                            ArrayList<Message> messages = channel.getMessages();
                            messages.add(message);
                            channel.setMessages(messages);
                            ArrayList<Users> involved = channel.getInvolved();
                            involved.add(actor);
                            involved.add(receiver);
                            channel.setInvolved(involved);
                            channel.setInitiator(actor.getId());
                            channel.setLastMessageTime(System.currentTimeMillis());
                            channelsRefrence.child(channel.getChannelId()).setValue(channel);
                            Call<NotificationResponse> notificationResponse = serviceApi.sendNotification(notification);
                            notificationResponse.enqueue(new Callback<NotificationResponse>() {
                                @Override
                                public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                                    if(response.isSuccessful()){
                                        Log.i("message sent", "success");
                                    }
                                }

                                @Override
                                public void onFailure(Call<NotificationResponse> call, Throwable t) {

                                }
                            });
                            channelsRefrence.child(channel.getChannelId()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    Channels tempChannel = snapshot.getValue(Channels.class);
                                    initiateListView(tempChannel, actor.getId());
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
                textMessage.setText("");
//                InputMethodManager inputManager =
//                        (InputMethodManager) chatScreenContext.
//                                getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.hideSoftInputFromWindow(
//                        chatScreenContext.getCurrentFocus().getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        //Child Event Listener

    }

    public void initiateListView(Channels channel, final String actorId){
        adapter = new AnnoChatScreenListAdapter(channel, actorId);
        listView.setAdapter(adapter);
        listView.setSelection(listView.getAdapter().getCount()-1);

    }

    public void sendNotification(Message message){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.message)
                        .setContentTitle("Test")
                        .setContentText(message.getTextMessage());


// Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(++count, mBuilder.build());


    }
}
