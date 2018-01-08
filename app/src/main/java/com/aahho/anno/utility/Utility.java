package com.aahho.anno.utility;

import android.util.Log;

import com.aahho.anno.model.Users;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by souvikdas on 11/10/17.
 */

public class Utility {

    private String[] months = new String[]{"jan", "feb", "mar", "apr", "may", "june", "july", "aug", "sept", "oct", "nov", "dec"};

    public String getId(Users actor, Users receiver){
        String id = null;
        if(actor.getUsername().compareTo(receiver.getUsername()) > 1){
            id = receiver.getId() + actor.getId();
        }else if(actor.getUsername().compareTo(receiver.getUsername()) < 0) {
            id = actor.getId() + receiver.getId();
        }
        return id;

    }


    public String dateUtility(Date date){
        String displayDate = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String minute = null;
        if(String.valueOf(calendar.get(Calendar.MINUTE)).length() < 2){
            minute = "0"+calendar.get(Calendar.MINUTE);
        }else{
            minute = String.valueOf(calendar.get(Calendar.MINUTE));
        }
        displayDate = String.valueOf(calendar.get(Calendar.DATE)) + " " + months[calendar.get(Calendar.MONTH)] +" "+ calendar.get(Calendar.YEAR) +
                " "+ calendar.get(Calendar.HOUR) + ":"+minute;
        Log.i("date" , displayDate);
        return displayDate;
    }
}
