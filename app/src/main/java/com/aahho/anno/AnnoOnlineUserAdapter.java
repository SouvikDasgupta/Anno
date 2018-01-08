package com.aahho.anno;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aahho.anno.model.Upload;
import com.aahho.anno.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by souvikdas on 18/9/17.
 */

public class AnnoOnlineUserAdapter extends RecyclerView.Adapter<AnnoOnlineUserAdapter.Holder> {
    private ArrayList<Users> onlineUsers;
    public OnItemClickListener mItemClickListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public AnnoOnlineUserAdapter(ArrayList<Users> onlineUsers){
        this.onlineUsers = onlineUsers;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(AnnoActiveUserActivity.activeUserActivity);
        View view = inflater.inflate(R.layout.online_frag_individual, parent, false);
        AnnoOnlineUserAdapter.Holder holder = new AnnoOnlineUserAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final Users user = onlineUsers.get(position);
        if(user.getAnonymousName()!=null){
            holder.onlineName.setText(user.getAnonymousName());
        }else{
            holder.onlineName.setText(user.getUsername());
        }
        if(user.getId()!=null){
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference().child("chat").child("uploads");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(user.getDpId()!=null){
                        DataSnapshot childSnap = dataSnapshot.child(user.getDpId());
                        Upload data = childSnap.getValue(Upload.class);
                        if(data.getWebViewLinks().getThumbnail()!=null){
                            Picasso.with(AnnoActiveUserActivity.activeUserActivity).load(data.getWebViewLinks().getThumbnail()).into(holder.circleImageView);

                        }

//                    Toast.makeText(AnnoMainScreenActivity.mainScreenActivity, data.getSelf_link(), Toast.LENGTH_SHORT).show();
//                    holder.circleImageView.setImageURI(Uri.parse(data.getSelf_link()));
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return onlineUsers.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private TextView onlineName;
        private CircleImageView circleImageView;
        public Holder(View itemView) {
            super(itemView);
            onlineName = (TextView) itemView.findViewById(R.id.online_name);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.online_image);

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

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
