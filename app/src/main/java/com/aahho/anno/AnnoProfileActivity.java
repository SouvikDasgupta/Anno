package com.aahho.anno;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aahho.anno.api.ServiceAPI;
import com.aahho.anno.model.Upload;
import com.aahho.anno.model.UploadResponse;
import com.aahho.anno.model.Users;
import com.aahho.anno.utility.RetrofitClient;
import com.aahho.anno.utility.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aahho.anno.AnnoChatScreenActivity.chatScreenContext;

/**
 * Created by souvikdas on 27/10/17.
 */

public class AnnoProfileActivity extends AppCompatActivity {
    private TextView edit;
    private int RESULT_SUCCESS = 200;
    private int STORAGE_PERMISSION_CODE = 201;
    private CircleImageView image;
    private SessionManager session;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Users actor;
    private ImageView backPress;
    private boolean changePasswordValue = false;
    private TextView userNameProfile;
    private Button updateBasicDetails;
    private EditText anonymusName;
    private EditText interestedIn;
    private LinearLayout layout;
    private AnnoProfileActivity profileActivity;
    private EditText currentPassword;
    private EditText newPassword;
    private EditText confirmPassword;
    private Button changePassword;
    private CardView changePasswordCard;
    private CardView inner;
    private ImageView imageView;
    private Button updatePassword;
    @Override
    public void onCreate(Bundle savedBundleInstance){
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.profile_details);
        profileActivity = this;
        layout = (LinearLayout) findViewById(R.id.root_layout);
        image = (CircleImageView) findViewById(R.id.profile_pic);
        backPress = (ImageView) findViewById(R.id.back_press_details);
        userNameProfile = (TextView) findViewById(R.id.profile_username);
        anonymusName = (EditText) findViewById(R.id.anonymus_name);
        interestedIn = (EditText) findViewById(R.id.interested_in);
        updateBasicDetails = (Button) findViewById(R.id.update_basic_details);
        currentPassword = (EditText) findViewById(R.id.current_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        changePasswordCard = (CardView) findViewById(R.id.change_password_card);
        inner = (CardView) findViewById(R.id.innercard);
        imageView = (ImageView) findViewById(R.id.dropimage);
        updatePassword = (Button) findViewById(R.id.changepassword);
        session = new SessionManager(getApplicationContext());
        final Map<String, String> userDetails = session.getUserDetails();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot childSnap = dataSnapshot.child(userDetails.get("userId"));
                actor = childSnap.getValue(Users.class);
                userNameProfile.setText(actor.getUsername());
                String anonymusNameActor = actor.getAnonymousName();
                if(anonymusNameActor != null){
                    anonymusName.setText(anonymusNameActor);
                }
                ArrayList<String> intIn = actor.getInterestedIn();
                if(intIn!= null && !intIn.isEmpty()){
                    StringBuilder s = new StringBuilder();
                    for(String str: intIn){
                        s.append(str +",");
                    }
                    interestedIn.setText(s.substring(0, s.length()-1));
                }

                if(actor.getDpId()!=null){
                    DatabaseReference uploadRef = firebaseDatabase.getReference().child("chat").child("uploads");
                    uploadRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DataSnapshot uSnap = dataSnapshot.child(actor.getDpId());
                            Upload uploadData = uSnap.getValue(Upload.class);
                            if(uploadData.getWebViewLinks().getThumbnail()!=null){
                                Picasso.with(getApplicationContext()).load(uploadData.getWebViewLinks().getThumbnail()).resize(100, 100).into(image);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        edit = (TextView) findViewById(R.id.uploadimage);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isReadStorageAllowed()){
                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(galleryIntent,RESULT_SUCCESS);
                }else{
                    requestStoragePermission();
                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(galleryIntent,RESULT_SUCCESS);
                }

            }
        });

//        basicDetailsCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!basicDetailsOpen){
//                    ImageView arrow = (ImageView) findViewById(R.id.dropimage);
//                    arrow.setImageDrawable(getResources().getDrawable(R.drawable.dropup));
//                    CardView innerCard = (CardView) findViewById(R.id.basic_details_inner_card);
//                    innerCard.setVisibility(View.VISIBLE);
//                    basicDetailsOpen = true;
//                }else{
//                    ImageView arrow = (ImageView) findViewById(R.id.dropimage);
//                    arrow.setImageDrawable(getResources().getDrawable(R.drawable.dropdown));
//                    CardView innerCard = (CardView) findViewById(R.id.basic_details_inner_card);
//                    innerCard.setVisibility(View.GONE);
//                    basicDetailsOpen =false;
//
//                }
//
//            }
//        });

        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AnnoMainScreenActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        updateBasicDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> interestedArraylist = null;
                String[] interestedList = interestedIn.getText().toString().split(",");
                if(interestedList.length > 0){
                    interestedArraylist = new ArrayList<String>(Arrays.asList(interestedList));
                }
                Map<String, Object> map = new HashMap<>();
                map.put("anonymousName", anonymusName.getText().toString());
                map.put("interestedIn", interestedArraylist);
                databaseReference.child(actor.getId()).updateChildren(map);
                InputMethodManager inputManager =
                        (InputMethodManager) profileActivity.
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        profileActivity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                Snackbar snackbar = Snackbar
                        .make(layout, "Successfully updated basic details", Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AnnoBasicDetailsActivity.class);
                intent.putExtra("actor", actor);
                startActivity(intent);
            }
        });

        changePasswordCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!changePasswordValue){
                    inner.setVisibility(View.VISIBLE);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.dropup));
                    changePasswordValue = true;
                }else{
                    inner.setVisibility(View.GONE);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.dropdown));
                    changePasswordValue = false;

                }
            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!actor.getPassword().equals(currentPassword.getText().toString())){
                    Toast.makeText(profileActivity, "Current password entered is wrong", Toast.LENGTH_SHORT).show();
                    currentPassword.setText("");
                    newPassword.setText("");
                    confirmPassword.setText("");
                }else{
                    if(!newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                        Toast.makeText(profileActivity, "New password and Confirmed password did not matched", Toast.LENGTH_SHORT).show();
                        currentPassword.setText("");
                        newPassword.setText("");
                        confirmPassword.setText("");
                    }else{
                        Map<String, Object> map = new HashMap<>();
                        map.put("password", newPassword.getText().toString());
                        databaseReference.child(actor.getId()).updateChildren(map);
                        Snackbar snackbar = Snackbar
                                .make(layout, "Password updated successfully, please Login again", Snackbar.LENGTH_LONG);

                        snackbar.show();
                        currentPassword.setText("");
                        newPassword.setText("");
                        confirmPassword.setText("");
                        session.logout();
                        Intent intent = new Intent(getApplicationContext(), AnnoLoginActivity.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);

                    }
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_SUCCESS && resultCode == RESULT_OK){
            if(data != null){
                Log.i("data", data.getData().getPath());
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imagePath = cursor.getString(columnIndex);
                    if(imagePath.startsWith("file://")){
                        imagePath = imagePath.substring(7);
                    }
                    Log.i("imagePath", imagePath);
                    Picasso.with(this).load(data.getData()).resize(40, 40).into(image);
                    uploadImage(imagePath);
                }
            }else{

            }
        }

    }

    private void uploadImage(String uri) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Uploading Image..");
        progress.show();

        ServiceAPI serviceApi = new RetrofitClient().createRetrofitClient().create(ServiceAPI.class);

        //Taking File from file path
        final File file = new File(uri);

        Log.i("File" , String.valueOf(file.exists()));

      //create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);


        MultipartBody.Part body =
                MultipartBody.Part.createFormData("files[]", file.getName(), requestFile);

        Call<UploadResponse> callUploadResponse = serviceApi.uploadImage(body);

        callUploadResponse.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                progress.dismiss();
                if(response.isSuccessful()){
                    if(response.body().getNotification().getResponseCode() == 200){
                        Map<String, Object> map = new HashMap<>();
                        map.put("dpId", response.body().getData().get(0).getId());
                        databaseReference.child(actor.getId()).updateChildren(map);
                    }else{
                        Toast.makeText(AnnoProfileActivity.this,"Inside inner else" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.i("response error", response.raw().toString());
                    Toast.makeText(AnnoProfileActivity.this, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                progress.dismiss();
//                Log.i("error" , t.getMessage());
                Toast.makeText(AnnoProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

//        ImageView image = (ImageView) findViewById(R.id.imagetest);
//        image.setImageURI(uri);
    }

    //Check For Permission
    //Permission are granted dynamically from Android 6 and above

    public void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //Todo
        }

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults){

        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }

    }

    private boolean isReadStorageAllowed(){
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

}
