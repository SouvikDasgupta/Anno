package com.aahho.anno;

import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aahho.anno.model.Channels;
import com.aahho.anno.utility.Utility;
import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;

/**
 * Created by souvikdas on 4/10/17.
 */

public class AnnoChatScreenListAdapter extends BaseAdapter {

    private Channels channel;
    private String actorId;

    public AnnoChatScreenListAdapter(final Channels channel, final String actorId){
        this.channel = channel;
        this.actorId = actorId;
    }
    @Override
    public int getCount() {
        return channel.getMessages().size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(AnnoChatScreenActivity.chatScreenContext);
//        view = inflater.inflate(R.layout.chat_screen_individual, null);
//        CardView cardView = (CardView) view.findViewById(R.id.card_text_chat);
//        TextView textView = (TextView) view.findViewById(R.id.message_text_chat);
//        textView.setText(channel.getMessages().get(i).getTextMessage());
//        TextView date = (TextView) view.findViewById(R.id.date);
//        date.setText(channel.getMessages().get(i).getMessageTime().getHours() + ":"+ channel.getMessages().get(i).getMessageTime().getMinutes());
////        date.setText(channel.getMessages().get(i).getMessageTime().getTime().);
//        if(channel.getMessages().get(i).getSenderId().equals(actorId)){
//            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            relativeParams.setMargins(220,0,50,0);
//            cardView.setCardBackgroundColor(AnnoChatScreenActivity.chatScreenContext.getResources().getColor(R.color.colorPrimary));
//            textView.setTextColor(AnnoChatScreenActivity.chatScreenContext.getResources().getColor(R.color.textPrimaryColor));
//            cardView.setLayoutParams(relativeParams);
//        }



        if(channel.getMessages().get(i).getSenderId().equals(actorId)){
            view = inflater.inflate(R.layout.reciever_fragment, null);
            EmojiTextView textMessage = (EmojiTextView) view.findViewById(R.id.text_message);
            textMessage.setText(channel.getMessages().get(i).getTextMessage());
            TextView date = (TextView) view.findViewById(R.id.date);
            date.setText(new Utility().dateUtility(channel.getMessages().get(i).getMessageTime()));
        }else{
            view = inflater.inflate(R.layout.sender_fragment, null);
            EmojiTextView textMessage = (EmojiTextView) view.findViewById(R.id.text_message);
            textMessage.setText(channel.getMessages().get(i).getTextMessage());
            TextView date = (TextView) view.findViewById(R.id.date);

            date.setText( new Utility().dateUtility(channel.getMessages().get(i).getMessageTime()));
        }
        return view;
    }
}
