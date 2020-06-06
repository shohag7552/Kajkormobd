package com.example.kajkormobd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;


import java.util.Objects;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile_fragment extends Fragment {



    private FirebaseAuth mAuth;
    private TextView User_name,User_Address,User_phn;
    private CircleImageView User_profile;
    private DatabaseReference databaseReference;
    private String UserID ;



    private RecyclerView postItems;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference postref = db.collection("posts");

    private profile_post_adpter adpter;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_profile, container , false );

        Toolbar myToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(myToolbar);

        User_name = (TextView)view.findViewById(R.id.name_text);
        User_Address = (TextView)view.findViewById(R.id.address_textview);
        User_phn = (TextView)view.findViewById(R.id.phn_text);
        User_profile = (CircleImageView)view.findViewById(R.id.imageView2);
        postItems= (RecyclerView)view.findViewById(R.id.recycleview1);




        mAuth = FirebaseAuth.getInstance();
        UserID=mAuth.getCurrentUser().getUid();


        profile_recycle();


        if( mAuth.getCurrentUser() != null ){

            String userId = mAuth.getCurrentUser().getUid();
            db.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot doc ) {


                    Log.d("PROFILE", "onSuccess: " + doc );

                    if( doc.exists() ){
                        String name = doc.getString("name");
                        String mobile = doc.getString("mobile");
                        String image = doc.getString("image");
                        String address = doc.getString("address");

                        User_name.setText(name);
                        User_Address.setText(address);
                        User_phn.setText(mobile);
                        Picasso.get().load(image).fit().centerCrop().into(User_profile);
                    }
                    else{
                         Intent in = new Intent(getActivity(), worker_photo_name_activity.class);
                         startActivity(in);
                    }
                }
            });

        }
        else {
            Intent in = new Intent(getActivity(),LoginActivity.class);
            startActivity(in);
        }

        return view;
    }

    private void profile_recycle() {


        Query query = postref.orderBy("post_time", Query.Direction.DESCENDING).whereEqualTo("user_id",UserID);

        FirestoreRecyclerOptions<profile_post> prfile_posts_option = new FirestoreRecyclerOptions.Builder<profile_post>()
                .setQuery(query, profile_post.class)
                .build();


        if(adpter==null){
            adpter = new profile_post_adpter(prfile_posts_option);
            cd = new ConnectionDetector(getActivity());
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent){
                postItems.setHasFixedSize(true);
                postItems.setLayoutManager(new LinearLayoutManager(getActivity()));
                postItems.setAdapter(adpter);
            }else{
                Toast.makeText(getActivity(),"no Internet",Toast.LENGTH_LONG).show();
                postItems.setLayoutManager(new LinearLayoutManager(getActivity()));
                postItems.setAdapter(adpter);
                adpter.notifyDataSetChanged();

            }
        }else{
            postItems.setLayoutManager(new LinearLayoutManager(getActivity()));
            postItems.setAdapter(adpter);
            adpter.notifyDataSetChanged();

        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                adpter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(postItems);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.option_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();

        if (id == R.id.setting) {
            //do your function here
            Intent in = new Intent(getActivity(),worker_photo_name_activity_for_edit.class);
            startActivity(in);

            Toast.makeText(getActivity(), "Settings", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.Language) {
            //do your function here
            Toast.makeText(getActivity(), "Language", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.English) {
            //do your function here
            Toast.makeText(getActivity(), "English", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.Bangla) {
            //do your function here
            Toast.makeText(getActivity(), "Bangla", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.logout) {
            mAuth.signOut();
            Intent in = new Intent(getActivity(), Home.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(in);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        adpter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adpter != null) {
            adpter.stopListening();
        }
    }





}

