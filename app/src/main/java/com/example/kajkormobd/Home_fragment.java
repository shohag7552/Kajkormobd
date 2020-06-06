package com.example.kajkormobd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kajkormobd.support.LocalDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;public class Home_fragment extends Fragment{

    public Home_fragment() {
    }

    ConnectionDetector cd;
    Boolean isInternetPresent = false;





    private RecyclerView postItems;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference postref = db.collection("posts");

    private Post_adpter adpter;

    private AutoCompleteTextView searchView_location;
    private TextView work_category_tv;
    private Button search_recycle;

    SwipeRefreshLayout swipeRefreshLayout;






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container , false );


        work_category_tv = view.findViewById(R.id.textView8);
        search_recycle= view.findViewById(R.id.button7);




        //Bundle bundle = this.getArguments();
        //if (bundle != null) {
        if(LocalDatabase.getInstance().getSelectedWork() != null) {

            //String getArgument = getArguments().getString("data");
            work_category_tv.setText(LocalDatabase.getInstance().getSelectedWork());//set string over textview


        }
        else {
            work_category_tv.setText("Select Work");
        }








        work_category_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getActivity(),slelection_for_work.class);
                startActivity(in);

            }
        });

        postItems = view.findViewById(R.id.recycleview1);


        searchView_location= (AutoCompleteTextView)view.findViewById(R.id.textView9);
        final CharSequence[] location = getResources().getStringArray(R.array.location);
        final ArrayAdapter<CharSequence> adapter1 = new ArrayAdapter<CharSequence>(getActivity(),android.R.layout.simple_list_item_1,location);
        searchView_location.setAdapter(adapter1);




        setupRecycleView_defalt();



        search_recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setupRecycleView();
            }
        });



        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adpter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

//
//        adpter.setOnItemClickListener(new Post_adpter.onItemClickListener() {
//            @Override
//            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//
//
//
//
//                Posts posts = documentSnapshot.toObject(Posts.class);
//
//
//
//
//
//                if (){
//
//                    Toast.makeText(getContext(), "yes", Toast.LENGTH_SHORT).show();
//
//
//                    getView().setBackgroundDrawable(getResources().getDrawable(R.drawable.fav_button_color_change));
//
//
//
//                }else{
//                    getView().setBackgroundDrawable(getResources().getDrawable(R.drawable.fav_button_color_change));
//                    Toast.makeText(getContext(), "no", Toast.LENGTH_SHORT).show();
//
//
//
//                }
//
//
//
//
//
//
//            }
//        });
//
        return view;




    }

    private void setupRecycleView_defalt() {


        Query query = postref.orderBy("post_time", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Posts> posts_option = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(query, Posts.class)
                .build();






        if(adpter==null){
            adpter = new Post_adpter(posts_option);
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













    }







    private void setupRecycleView() {


        String a = work_category_tv.getText().toString();
        String b = searchView_location.getText().toString();


        if (!a.equals("Select Work") && !b.equals("All Over Bangladesh")){

            Toast.makeText(getActivity(), a+b, Toast.LENGTH_SHORT).show();


            Query query = postref.orderBy("post_time", Query.Direction.DESCENDING).whereEqualTo("work_name",a).whereEqualTo("work_location",b);

            FirestoreRecyclerOptions<Posts> posts_option = new FirestoreRecyclerOptions.Builder<Posts>()
                    .setQuery(query, Posts.class)
                    .build();


            adpter = new Post_adpter(posts_option);
            adpter.startListening();

            postItems.setHasFixedSize(true);
            postItems.setAdapter(adpter);
            postItems.setLayoutManager(new LinearLayoutManager(getActivity()));
            adpter.notifyDataSetChanged();


        } else if (!a.equals("Select Work") && b.equals("All Over Bangladesh")) {

            Toast.makeText(getActivity(), a+b, Toast.LENGTH_SHORT).show();


            Query query = postref.orderBy("post_time", Query.Direction.DESCENDING).whereEqualTo("work_name",a);

            FirestoreRecyclerOptions<Posts> posts_option = new FirestoreRecyclerOptions.Builder<Posts>()
                    .setQuery(query, Posts.class)
                    .build();


            adpter = new Post_adpter(posts_option);
            adpter.startListening();

            postItems.setHasFixedSize(true);
            postItems.setAdapter(adpter);
            postItems.setLayoutManager(new LinearLayoutManager(getActivity()));
            adpter.notifyDataSetChanged();



        }

        else {
            setupRecycleView_defalt();
            adpter.startListening();
        }




    }

    @Override
    public void onResume() {
        super.onResume();
        String selectedWork = LocalDatabase.getInstance().getSelectedWork();
        if(selectedWork != null) {

            work_category_tv.setText(LocalDatabase.getInstance().getSelectedWork());
            Log.d("SelectedWork", selectedWork);
        }else {
            Log.d("SelectedWork", "selework is null");
        }


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


