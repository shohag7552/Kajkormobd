package com.example.kajkormobd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class favorite_fragment extends Fragment {
    private RecyclerView postItems;
    private FirebaseFirestore db1 = FirebaseFirestore.getInstance();
    private CollectionReference favref = db1.collection("favorite");



    private fav_post_adpter adpter;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    private FirebaseAuth mAuth;
    private String uid;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);


        postItems =view.findViewById(R.id.recycleview1);
        mAuth = FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();


        Query query1 = favref.whereEqualTo("uid",uid);

        FirestoreRecyclerOptions<favorite_Posts> fav_post = new FirestoreRecyclerOptions.Builder<favorite_Posts>()
                .setQuery(query1, favorite_Posts.class)
                .build();


        if(adpter==null){
            adpter = new fav_post_adpter(fav_post);
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



        return view;
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

