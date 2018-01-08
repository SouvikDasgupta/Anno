package com.aahho.anno;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aahho.anno.model.ChatList;
import com.aahho.anno.model.Upload;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by souvikdas on 19/9/17.
 */

public class AnnoPersonalChatAdapter extends RecyclerView.Adapter<AnnoPersonalChatAdapter.Holder> {
    private ArrayList<ChatList> chatList;
    public AnnoOnlineUserAdapter.OnItemClickListener mItemClickListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public AnnoPersonalChatAdapter(final ArrayList<ChatList> chatList){
        this.chatList = chatList;
    }

    @Override
    public AnnoPersonalChatAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(AnnoUserMainActivity.userMainActivity);
        View view = inflater.inflate(R.layout.chat_frag_individual, parent,false);
        AnnoPersonalChatAdapter.Holder holder = new AnnoPersonalChatAdapter.Holder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final AnnoPersonalChatAdapter.Holder holder, int position) {
        final ChatList list = chatList.get(position);
        holder.lastMessage.setText(list.getLastMessage());
        if(list.getOtherUser().getAnonymousName()!=null){
            holder.otherUsername.setText(list.getOtherUser().getAnonymousName());
        }else{
            holder.otherUsername.setText(list.getOtherUser().getUsername());
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("chat").child("uploads");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(list.getOtherUser().getDpId()!=null){
                    DataSnapshot childSnap = dataSnapshot.child(list.getOtherUser().getDpId());
                    Upload data = childSnap.getValue(Upload.class);
                    if(data.getWebViewLinks().getThumbnail()!=null){
                        Picasso.with(AnnoUserMainActivity.userMainActivity).load(data.getWebViewLinks().getThumbnail()).into(holder.otherChatUser);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        if(chatList.get(position).getOtherUser().getStatus().equals("Offline")){
//            holder.onlineDisplay.setBackground(AnnoMainScreenActivity.mainScreenActivity.getDrawable(R.drawable.offline));
//        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView otherUsername;
        TextView lastMessage;
        CircleImageView otherChatUser;
        public Holder(View itemView) {
            super(itemView);
            otherUsername  = (TextView) itemView.findViewById(R.id.personal_othername);
            lastMessage = (TextView) itemView.findViewById(R.id.last_message);
            otherChatUser = (CircleImageView) itemView.findViewById(R.id.online_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(view, getPosition());
                    }
                }
            });
        }
    }

    //click listener

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final AnnoOnlineUserAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
