package com.example.kajkormobd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class chat_fragment extends Fragment {

    private RecyclerView chatList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference postref = db.collection("chats");

    private Chat_Adapter chatAdapter;

//    ConnectionDetector cd;
//    Boolean isInternetPresent = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_chat, container , false );

        chatList= view.findViewById(R.id.chat_recycler);

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = postref.whereEqualTo("user_id",id);

        FirestoreRecyclerOptions<chats> option = new FirestoreRecyclerOptions.Builder<chats>()
                .setQuery(query, chats.class)
                .build();

        chatAdapter = new Chat_Adapter(option);
        chatAdapter.startListening();

        chatList.setHasFixedSize(true);
        chatList.setAdapter(chatAdapter);
        chatList.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (chatAdapter != null) {
            chatAdapter.stopListening();
        }
    }


}

