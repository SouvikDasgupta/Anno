package com.aahho.anno;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aahho.anno.model.Upload;
import com.aahho.anno.model.UploadData;
import com.aahho.anno.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by souvikdas on 31/10/17.
 */

public class AnnoBasicDetailsActivity extends AppCompatActivity {
    private Users actor;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference userDatabaseRef;
    private ImageView imageView;
    private ImageView back;
    private ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedBundleInstance){
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.interested_page);
        imageView = (ImageView) findViewById(R.id.display_picture);
        back = (ImageView) findViewById(R.id.back_full_Pic);
        progress = (ProgressBar) findViewById(R.id.progressbar);
        actor = (Users) getIntent().getSerializableExtra("actor");
        firebaseDatabase = FirebaseDatabase.getInstance();
        userDatabaseRef = firebaseDatabase.getReference().child("users");
        userDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot childUser = dataSnapshot.child(actor.getId());
                final Users user = childUser.getValue(Users.class);
                databaseReference = firebaseDatabase.getReference().child("chat").child("uploads");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       if(user.getDpId()!=null){
                           DataSnapshot childSnap = dataSnapshot.child(user.getDpId());
                           Upload data = childSnap.getValue(Upload.class);
                           if(data.getWebViewLinks().getLarge()!=null){
                               Picasso.with(getApplicationContext()).load(data.getWebViewLinks().getLarge()).resize(200, 200).into(imageView);
                           }

                           progress.setVisibility(View.GONE);
                       }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

    }
}
