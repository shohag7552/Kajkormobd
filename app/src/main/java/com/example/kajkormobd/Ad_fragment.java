package com.example.kajkormobd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Ad_fragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private ToggleButton homecategory,shopcategory,freelancecategory, lastFalse,negotiable;
    private TextView home_text , shop_text, freelance_text, work_name,work_experience;
    private EditText work_description, work_amount,work_phn;
    private AutoCompleteTextView searchView_location;
    private Button create_post_btn;
    private ProgressDialog Loadingbar;


    private FirebaseAuth mAuth;
    private DatabaseReference postref;
    private String userId;
    private String Uname,Uimage;



    private  String negotiable_conditon = "Not Negotiable";

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private String saveCurrentDate,saveCurrentTime,postRandomName;








    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container , false );

        Toolbar myToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(myToolbar);



        mAuth = FirebaseAuth.getInstance();

        userId = mAuth.getCurrentUser().getUid();
        postref = FirebaseDatabase.getInstance().getReference().child("posts");




        homecategory= (ToggleButton) view.findViewById(R.id.button4);
        shopcategory= (ToggleButton) view.findViewById(R.id.shop_button);
        freelancecategory= (ToggleButton) view.findViewById(R.id.button);
        negotiable= (ToggleButton) view.findViewById(R.id.nego);

        home_text= (TextView)view.findViewById(R.id.textView12);
        shop_text= (TextView)view.findViewById(R.id.textView13);
        freelance_text= (TextView)view.findViewById(R.id.textView11);
        work_name= (TextView)view.findViewById(R.id.work);
        work_experience= (TextView)view.findViewById(R.id.expo);


        work_description = (EditText)view.findViewById(R.id.descrip_edit);
        work_amount = (EditText)view.findViewById(R.id.amount_edit);
        work_phn = (EditText)view.findViewById(R.id.phn_edit1);

        create_post_btn = (Button)view.findViewById(R.id.create_post);

        Loadingbar = new ProgressDialog(view.getContext());

        searchView_location= (AutoCompleteTextView)view.findViewById(R.id.location_search);
        final CharSequence[] location = getResources().getStringArray(R.array.location);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(),android.R.layout.simple_list_item_1,location);
        searchView_location.setAdapter(adapter);


        db.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc ) {


                Log.d("PROFILE", "onSuccess: " + doc );

                if( doc.exists() ){
                    Uname = doc.getString("name");
                    Uimage = doc.getString("image");



                }
                else{
                    Toast.makeText(getActivity(), "name and image not work..", Toast.LENGTH_SHORT).show();
                }
            }
        });



        create_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostCreating();
            }
        });



        // work_name.setEnabled(false);





        homecategory.setOnCheckedChangeListener(this);
        shopcategory.setOnCheckedChangeListener(this);
        freelancecategory.setOnCheckedChangeListener(this);

        homecategory.setChecked(true);


        work_experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                work_experience_operation();
            }
        });


        negotiable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {


                    negotiable.setBackgroundDrawable(getResources().getDrawable(R.drawable.negotiable_pressed));
                    negotiable.setTextColor(getResources().getColor(R.color.white));

                    negotiable_conditon = "(Negotiable)";


                }
                else

                    {
                        negotiable.setBackgroundDrawable(getResources().getDrawable(R.drawable.negotiable_pressed));
                        negotiable.setTextColor(getResources().getColor(R.color.black));
                        negotiable_conditon = "(Not Negotiable)";

                    }
            }
        });






        return view;
    }private void saveUser( String postRandomName,  String workName , String userExperince , String userDescription, String userLocation,String userAmount,String userPhone ){

        // Create a new user with a first and last name
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("work_name",workName);
        postMap.put("work_experience",userExperince);
        postMap.put("work_description",userDescription);
        postMap.put("work_location",userLocation);
        postMap.put("work_amount",userAmount);
        postMap.put("negotiable_condition",negotiable_conditon);
        postMap.put("work_phone",userPhone);
        postMap.put("user_id",userId);
        postMap.put("post_time",postRandomName);
        postMap.put("user_name",Uname);
        postMap.put("user_image",Uimage);


        // Add a new document with a generated ID
        db.collection("posts")
                .add(postMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent intent = new Intent(getActivity(),Home.class);
                        startActivity(intent);
                        Loadingbar.dismiss();
                    }
                });
    }



    private void PostCreating()
    {


        Calendar callforDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(callforDate.getTime());

        Calendar callforTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(callforDate.getTime());

        postRandomName = saveCurrentDate +"// "+ saveCurrentTime;


        final String userId = mAuth.getCurrentUser().getUid();
        final String workName = work_name.getText().toString().trim();
        final String userExperince = work_experience.getText().toString().trim();
        final String userDescription = work_description.getText().toString().trim();
        final String userLocation = searchView_location.getText().toString().trim();
        final String userAmount = work_amount.getText().toString().trim();
        final String userPhone = work_phn.getText().toString().trim();





        if (TextUtils.isEmpty(workName)) {
            Toast.makeText(getActivity(), "Work name needed...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userExperince)){
            Toast.makeText(getActivity(), "Work Experience Needed...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userDescription)){
            Toast.makeText(getActivity(), "Description Needed....", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(userLocation)){
            Toast.makeText(getActivity(), "Location needed...", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(userAmount)){
            Toast.makeText(getActivity(), "Amount Needed....", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(userPhone)){
            Toast.makeText(getActivity(), "A phone Number needed", Toast.LENGTH_SHORT).show();

        }else {

            Loadingbar.setTitle("Creating Your Post: ");
            Loadingbar.setMessage("Please Wait...");
            Loadingbar.setCanceledOnTouchOutside(true);
            Loadingbar.show();


            saveUser(postRandomName,workName,userExperince,userDescription,userLocation,userAmount,userPhone);

            }





    }



    private void work_experience_operation() {

        final CharSequence[] testArray = getResources().getStringArray(R.array.experience);


        AlertDialog.Builder home_dialouge = new AlertDialog.Builder(getActivity());
        home_dialouge.setTitle("Select your Work Experience");
        home_dialouge.setSingleChoiceItems(testArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                work_experience.setText(testArray[which]);
                work_experience.setTextColor(getResources().getColor(R.color.black));
                dialog.dismiss();

            }
        });

        AlertDialog sDialog = home_dialouge.create();
        sDialog.show();





    }










    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!homecategory.isChecked()&&!shopcategory.isChecked()&&!freelancecategory.isChecked()) {
            lastFalse.setChecked(true);
            return;
        }

        if (isChecked){


            if (buttonView == homecategory) {
                work_name.setText("Tutor");

                //homecategory.setChecked(true);
                shopcategory.setChecked(false);
                freelancecategory.setChecked(false);
                homecategory.setBackgroundDrawable(getResources().getDrawable(R.drawable.pressed_button_chnage));
                shopcategory.setBackgroundDrawable(getResources().getDrawable(R.drawable.pressd_for_shop));
                freelancecategory.setBackgroundDrawable(getResources().getDrawable(R.drawable.pressed_for_freelancer));

                home_text.setTextColor(getResources().getColor(R.color.green));
                home_text.setTypeface(Typeface.DEFAULT_BOLD);

                shop_text.setTextColor(getResources().getColor(R.color.gray));
                freelance_text.setTextColor(getResources().getColor(R.color.gray));

                //work_name.setEnabled(true);

                work_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final CharSequence[] testArray = getResources().getStringArray(R.array.Household_work);


                        AlertDialog.Builder home_dialouge = new AlertDialog.Builder(getActivity());
                        home_dialouge.setTitle("Select home work");
                        home_dialouge.setSingleChoiceItems(testArray, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                work_name.setText(testArray[which]);
                                work_name.setTextColor(getResources().getColor(R.color.black));

                                dialog.dismiss();

                            }
                        });

                        AlertDialog sDialog = home_dialouge.create();
                        sDialog.show();



                    }
                });







            }
            if (buttonView == shopcategory) {
                //shopcategory.setChecked(true);
                work_name.setText("Shop Stuff");
                homecategory.setChecked(false);
                freelancecategory.setChecked(false);
                homecategory.setBackgroundDrawable(getResources().getDrawable(R.drawable.pressed_button_chnage));
                shopcategory.setBackgroundDrawable(getResources().getDrawable(R.drawable.pressd_for_shop));
                freelancecategory.setBackgroundDrawable(getResources().getDrawable(R.drawable.pressed_for_freelancer));

                shop_text.setTextColor(getResources().getColor(R.color.green));
                shop_text.setTypeface(Typeface.DEFAULT_BOLD);

                home_text.setTextColor(getResources().getColor(R.color.gray));
                freelance_text.setTextColor(getResources().getColor(R.color.gray));



                work_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final CharSequence[] testArray = getResources().getStringArray(R.array.Shop_Work);


                        AlertDialog.Builder home_dialouge = new AlertDialog.Builder(getActivity());
                        home_dialouge.setTitle("Select home work");
                        home_dialouge.setSingleChoiceItems(testArray, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                work_name.setText(testArray[which]);
                               work_name.setTextColor(getResources().getColor(R.color.black));

                                dialog.dismiss();

                            }
                        });

                        AlertDialog sDialog = home_dialouge.create();
                        sDialog.show();



                    }
                });

            }
            if (buttonView == freelancecategory) {
                work_name.setText("Data Entry");
                //freelancecategory.setChecked(true);
                shopcategory.setChecked(false);
                homecategory.setChecked(false);
                homecategory.setBackgroundDrawable(getResources().getDrawable(R.drawable.pressed_button_chnage));
                shopcategory.setBackgroundDrawable(getResources().getDrawable(R.drawable.pressd_for_shop));
                freelancecategory.setBackgroundDrawable(getResources().getDrawable(R.drawable.pressed_for_freelancer));

                freelance_text.setTextColor(getResources().getColor(R.color.green));
                freelance_text.setTypeface(Typeface.DEFAULT_BOLD);
                shop_text.setTextColor(getResources().getColor(R.color.gray));
                home_text.setTextColor(getResources().getColor(R.color.gray));




                work_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final CharSequence[] testArray = getResources().getStringArray(R.array.Freelance_Work);


                        AlertDialog.Builder home_dialouge = new AlertDialog.Builder(getActivity());
                        home_dialouge.setTitle("Select home work");
                        home_dialouge.setSingleChoiceItems(testArray, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                work_name.setText(testArray[which]);
                                work_name.setTextColor(getResources().getColor(R.color.black));

                                dialog.dismiss();

                            }
                        });

                        AlertDialog sDialog = home_dialouge.create();
                        sDialog.show();



                    }
                });

            }

        }
        else {
            lastFalse = (ToggleButton) buttonView;
        }
    }












}


