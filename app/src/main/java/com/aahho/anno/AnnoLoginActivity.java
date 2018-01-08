package com.aahho.anno;

import android.content.ComponentName;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aahho.anno.model.Users;
import com.aahho.anno.utility.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class AnnoLoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button signIn;
    private TextView signUp;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Users user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anno_login);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        signIn = (Button) findViewById(R.id.signin);
        signUp = (TextView) findViewById(R.id.signup);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        //OnclickListener to navigate to signUp page from SignIn page
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(AnnoLoginActivity.this, AnnoRegisterActivity.class));
                startActivity(intent);
                finish();
            }
        });

        //OnclickListener to navigate to first screen to set anonymous name
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()){
                            user = snap.getValue(Users.class);
                            if(user.getUsername().equals(username.getText().toString().trim())
                                    && user.getPassword().equals(password.getText().toString().trim())){
                                SessionManager sessionManager = new SessionManager(getApplicationContext());
                                sessionManager.createLoginSession(user.getId(), user.getUsername());
                                Map<String, Object> map = new HashMap<>();
                                map.put("status", "Online");
                                map.put("fcmToken", sessionManager.getFcmToken());
                                databaseReference.child(user.getId()).updateChildren(map);
                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName(AnnoLoginActivity.this,AnnoUserMainActivity.class));
                                intent.putExtra("user", user);
                                startActivity(intent);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                finish();
                                break;
                            }else{
//                                Snackbar snackbar
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        });
    }
}
