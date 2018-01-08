package com.aahho.anno;

import android.util.Log;

import com.aahho.anno.utility.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by souvikdas on 29/10/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SessionManager session;
    @Override
    public void onTokenRefresh(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Refreshed token: " + refreshedToken);
        session =  new SessionManager(getApplicationContext());
        String id = session.hasUserId();
        if(id!=null){
            sendRegistrationToServer(id, refreshedToken);
        }
        else{
            session.storeFcmToken(refreshedToken);
        }

    }

    public void sendRegistrationToServer(String id, String token){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");
        Map<String, Object> map = new HashMap<>();
        map.put("fcmToken", FirebaseInstanceId.getInstance().getToken());
        databaseReference.child(id).updateChildren(map);
    }
}
