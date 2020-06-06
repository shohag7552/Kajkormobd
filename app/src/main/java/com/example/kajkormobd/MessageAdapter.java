package com.example.kajkormobd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> userMessagesList;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    Context c;

    public MessageAdapter(List<Messages> userMessagesList)
    {
        this.userMessagesList = userMessagesList;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView senderMessage,receiverMessage;
        public CircleImageView receiverImage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessage = itemView.findViewById(R.id.sender_message_text);
            receiverMessage = itemView.findViewById(R.id.receiver_msg_text);
            receiverImage = itemView.findViewById(R.id.message_profile_image);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_message_layout,parent,false);
        mAuth = FirebaseAuth.getInstance();
        return new  MessageViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {





        String messageSenderId = mAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(position);

        String fromUserId = messages.getFrom();
        String fromMessageType = messages.getType();

//        usersRef = FirebaseDatabase.getInstance().getReference().child("Messages").child(fromUserId);
//        usersRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.hasChild(""))
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        if (fromMessageType.equals("text")){

            holder.receiverMessage.setVisibility(View.INVISIBLE);
            holder.receiverImage.setVisibility(View.INVISIBLE);
            holder.senderMessage.setVisibility(View.INVISIBLE);

            if (fromUserId.equals(messageSenderId)){
                holder.senderMessage.setVisibility(View.VISIBLE);
                holder.senderMessage.setBackgroundResource(R.drawable.sender_msg_layout);
                holder.senderMessage.setText(messages.getMessage());


            }



            else
            {

                holder.receiverImage.setVisibility(View.VISIBLE);
                holder.receiverMessage.setVisibility(View.VISIBLE);

                holder.receiverMessage.setBackgroundResource(R.drawable.receiver_msg_layout);
                holder.receiverMessage.setText(messages.getMessage());
            }
        }
    }




    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }


}
