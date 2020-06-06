package com.example.kajkormobd;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class worker_photo_name_activity extends AppCompatActivity {

    private EditText User_phn, User_name,User_address;
    String Provided_Phn;
    private CircleImageView User_Photo;
    private Button Create_profile_button;
    private static final int Gallery_Pick = 1;
    private Uri ImageUri;
    private StorageReference imagestorageRef;
    private DatabaseReference databaseReference;
    private String downloadImageUrl;
    private ProgressDialog Loadingbar;
    private FirebaseAuth mAuth;
    private Uri resultUri;

    public static final String TAG = "USER_PHOTO_NAME";

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userref = db.collection("users");





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_photo_name_activity);

        User_phn = findViewById(R.id.editText2);
        User_name = findViewById(R.id.editText);
        User_address = findViewById(R.id.editText4);
        User_Photo = findViewById(R.id.imageView2);
        Create_profile_button =findViewById(R.id.nextbutton);
        Loadingbar = new  ProgressDialog(this);


        mAuth =FirebaseAuth.getInstance();
        imagestorageRef = FirebaseStorage.getInstance().getReference().child("Pofile Images");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()){

                    final String UserId = mAuth.getCurrentUser().getUid();

                    db.collection("users").document(UserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot doc ) {


                            Log.d("PROFILE", "onSuccess: " + doc );

                            if( doc.exists() ){
                                String name = doc.getString("name");
                                String mobile = doc.getString("mobile");
                                String image = doc.getString("image");
                                String address = doc.getString("address");

                                User_name.setText(name);
                                User_address.setText(address);
                                User_phn.setText(mobile);
                                Picasso.get().load(image).fit().centerCrop().into(User_Photo);
                            }
                            else{

                                Toast.makeText(worker_photo_name_activity.this, "New User",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });






                    Intent intent = new Intent(worker_photo_name_activity.this,Home.class);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(worker_photo_name_activity.this, "must take user info", Toast.LENGTH_SHORT).show();
                }

            }
        });






        Provided_Phn = getIntent().getStringExtra("phoneNumber");
        User_phn.setText(Provided_Phn);

        User_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Open_gallary();
            }
        });

        Create_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Validate_Profile_data();
            }
        });

    }
    private void Open_gallary() {

        Intent gallaryintent =new Intent();
        gallaryintent.setAction(Intent.ACTION_GET_CONTENT);
        gallaryintent.setType("image/*");
        startActivityForResult(gallaryintent, Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();


            /*CropImage.activity(ImageUri).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);*/
            Picasso.get().load(ImageUri).into(User_Photo);



        }
        /*if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (requestCode == RESULT_OK) {
                resultUri = result.getUri();
                User_Photo.setImageURI(resultUri);


            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error =result.getError();
                Toast.makeText(this,"Crop image problem:" + error,Toast.LENGTH_SHORT).show();
            }

        }*/

    }

    private void saveUser( String userId,  String name , String mobile , String address, String imageUrl ){

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("name", name );
        user.put("mobile", mobile );
        user.put("address", address );
        user.put("image", imageUrl );

        // Add a new document with a generated ID
        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent intent = new Intent(worker_photo_name_activity.this,Home.class);
                    startActivity(intent);
                }
            });
    }

    private void Validate_Profile_data() {

        final String Userid = mAuth.getCurrentUser().getUid();
        final String UserName= User_name.getText().toString().trim();
        final String User_Mobile = User_phn.getText().toString().trim();
        final String UserAddress = User_address.getText().toString().trim();

        Loadingbar.setTitle("Adding Profile");
        Loadingbar.setMessage("Please Wait");
        Loadingbar.setCanceledOnTouchOutside(false);
        Loadingbar.show();


        if (ImageUri == null){
            Toast.makeText(this,"Photo required...",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(UserName)){
            Toast.makeText(this,"Name required...",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(User_Mobile)){
            Toast.makeText(this,"Mobile Number required...",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(UserAddress)){
            Toast.makeText(this,"Address required...",Toast.LENGTH_SHORT).show();
        } else{

            final StorageReference filepath = imagestorageRef.child(ImageUri.getLastPathSegment() + Userid +".jpg");
            filepath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                        saveUser( Userid, UserName , User_Mobile , UserAddress , uri.toString() );
                        }
                    });
                }
            });
        }

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth = FirebaseAuth.getInstance();
//        if (mAuth.getCurrentUser() != null) {
//
//            Intent in = new Intent(worker_photo_name_activity.this, Home.class);
//            startActivity(in);
//        }
//
//    }
}
