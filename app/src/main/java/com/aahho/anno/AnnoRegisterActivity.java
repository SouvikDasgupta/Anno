package com.aahho.anno;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.aahho.anno.model.Users;
import com.aahho.anno.utility.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.tasks.Task;

import java.util.ArrayList;

/**
 * Created by souvikdas on 11/9/17.
 */

public class AnnoRegisterActivity extends AppCompatActivity {

    private TextView alreadyHaveAccount;
    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private Button signUp;
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;
    private FirebaseDatabase firebaseDatabase;
    private String userId;
    private Users user;
    private ArrayList<String> existingUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_register);
        alreadyHaveAccount = (TextView) findViewById(R.id.accountexist);
        username = (EditText) findViewById(R.id.reg_username);
        password = (EditText) findViewById(R.id.reg_password);
        confirmPassword = (EditText) findViewById(R.id.reg_confirmpassword);
        signUp = (Button) findViewById(R.id.reg_signup);

        //get instance of firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();

        //get reference to users node
        databaseReference = firebaseDatabase.getReference();
        userReference = databaseReference.child("users");

        //OnclickListener for already have an account to navigate back to Sign In page
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(AnnoRegisterActivity.this, AnnoLoginActivity.class));
                startActivity(intent);
                finish();
            }
        });

        //OnclickListener for Sign Up
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isUsernameValid = validateUsername();
                boolean isPasswordValid = validatePassword();
                if(isUsernameValid && isPasswordValid){
                    if(userId == null || userId.isEmpty()){
                        userId = userReference.push().getKey();
                    }
                    final Users user = new Users();
                    user.setId(userId);
                    user.setUsername(username.getText().toString().trim());
                    user.setPassword(password.getText().toString());
                    user.setStatus("Offline");
                    user.setFcmToken(new SessionManager(getApplicationContext()).getFcmToken());
                    Log.i("Username: " , user.getUsername());
                    Log.i("Size of list: " , String.valueOf(existingUsers.size()));
                    if(existingUsers.contains(user.getUsername())){
                        Toast.makeText(AnnoRegisterActivity.this, "Username alreay exists, choose a different username", Toast.LENGTH_SHORT).show();
                    }else {
                        userReference.child(userId).setValue(user);
                        userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                Toast.makeText(AnnoRegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                SessionManager session = new SessionManager(getApplicationContext());
                                session.createRegisterSession(user.getId(), user.getUsername());
                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName(AnnoRegisterActivity.this, AnnoMainScreenActivity.class));
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });



    }

    private boolean validatePassword() {
        boolean isPasswordValid = false;
        String pwd = password.getText().toString().trim();
        String cnfPwd = confirmPassword.getText().toString().trim();
        if(pwd != null && !pwd.isEmpty() && cnfPwd != null && !cnfPwd.isEmpty() && pwd.equals(cnfPwd)){
            isPasswordValid = true;
        }else {
            Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
        }
        return isPasswordValid;
    }

    private boolean validateUsername() {
        boolean isUsernameValid = false;
        String name = username.getText().toString().trim();
        if(name != null && !name.isEmpty()){
            isUsernameValid = true;
        }else{
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
        }
        return isUsernameValid;

    }

    @Override
    protected void onStart(){
        super.onStart();
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    existingUsers.add(snap.getValue(Users.class).getUsername());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }

}
