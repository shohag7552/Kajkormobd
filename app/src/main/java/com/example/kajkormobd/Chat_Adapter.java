package com.example.kajkormobd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_Adapter extends FirestoreRecyclerAdapter<chats, Chat_Adapter.ChatHolder> {


    public Chat_Adapter(@NonNull FirestoreRecyclerOptions<chats> options) {

        super(options);


    }
    @Override
    protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull chats model) {

        holder.name.setText(model.getUser_name());
        holder.work_name.setText(model.getWork_name());

        Picasso.get().load(model.getUser_image()).fit().centerCrop().into(holder.pic);
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recycler_item,parent,false);



        return new ChatHolder(view);
    }

    class ChatHolder extends RecyclerView.ViewHolder{

        TextView name,work_name;
        CircleImageView pic;
        Button cancel_btn,accept_btn;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.user_name);
            work_name = itemView.findViewById(R.id.work_catagory);

            pic = itemView.findViewById(R.id.circleImage);

            accept_btn = itemView.findViewById(R.id.accept_btn);
            cancel_btn = itemView.findViewById(R.id.cancel_btn);
        }
    }
}
