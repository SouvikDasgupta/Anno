package com.aahho.anno;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aahho.anno.model.Channels;
import com.aahho.anno.model.Message;
import com.vanniktech.emoji.EmojiTextView;
import com.vanniktech.emoji.emoji.Emoji;

import static android.view.View.LAYOUT_DIRECTION_LTR;
import static android.view.View.LAYOUT_DIRECTION_RTL;

/**
 * Created by souvikdas on 22/9/17.
 */

public class AnnoChatScreenAdapter extends RecyclerView.Adapter<AnnoChatScreenAdapter.Holder> {

    private Channels channel;
    private String actorId;
    public AnnoChatScreenAdapter(final Channels channel, final String actorId){
        this.channel = channel;
        this.actorId = actorId;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(AnnoChatScreenActivity.chatScreenContext);
        View view = inflater.inflate(R.layout.chat_screen_individual, parent, false);
        AnnoChatScreenAdapter.Holder holder = new AnnoChatScreenAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Message msg = channel.getMessages().get(position);
        holder.messageText.setText(msg.getTextMessage());
        String messageTime = String.valueOf(msg.getMessageTime().getTime());
//        holder.messageDateTime.setText(messageTime);
        if(msg.getSenderId().equals(actorId)){
            holder.card.setCardBackgroundColor(AnnoChatScreenActivity.chatScreenContext.getResources().getColor(R.color.colorPrimary));
            holder.messageText.setTextColor(AnnoChatScreenActivity.chatScreenContext.getResources().getColor(R.color.textPrimaryColor));
        }
    }

    @Override
    public int getItemCount() {
        return channel.getMessages().size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        EmojiTextView messageText;
        CardView card;
        RelativeLayout relativeLayout;
        TextView messageDateTime;
        public Holder(View itemView) {
            super(itemView);
            messageText = (EmojiTextView) itemView.findViewById(R.id.message_text_chat);
//            card = (CardView) itemView.findViewById(R.id.card_text_chat);
//            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout);
//            messageDateTime = (TextView) itemView.findViewById(R.id.message_date);
        }
    }
}
