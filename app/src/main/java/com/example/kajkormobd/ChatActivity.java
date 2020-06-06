package com.example.kajkormobd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String messageReceiverId,messageReceiverName,messageReceiverImage,messageSenderId;

    private Toolbar chat_tool_bar;
    private CircleImageView chat_profile_image;
    private TextView chat_profile_name;

    private RecyclerView usersMessageList;

    private ImageView send_message;
    private EditText write_message;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        mAuth = FirebaseAuth.getInstance();
        messageSenderId = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        messageReceiverId = getIntent().getExtras().get("user_id").toString();
        messageReceiverName= getIntent().getExtras().get("user_name").toString();
        messageReceiverImage= getIntent().getExtras().get("user_image").toString();


        Initializ();

        chat_profile_name.setText(messageReceiverName);
        Picasso.get().load(messageReceiverImage).fit().centerCrop().into(chat_profile_image);


        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage();
            }
        });
    }



    private void Initializ() {

        chat_tool_bar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(chat_tool_bar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = layoutInflater.inflate(R.layout.chat_bar,null);
        actionBar.setCustomView(action_bar_view);

        chat_profile_image = findViewById(R.id.chat_profile_image);
        chat_profile_name = findViewById(R.id.profile_name);

        write_message = findViewById(R.id.write_msg);
        send_message = findViewById(R.id.send_msg);

        usersMessageList = findViewById(R.id.messages_list);

        messageAdapter = new MessageAdapter(messagesList);

        linearLayoutManager = new LinearLayoutManager(this);
        usersMessageList.setLayoutManager(linearLayoutManager);
        usersMessageList.setAdapter(messageAdapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        rootRef.child("Messages").child(messageSenderId).child(messageReceiverId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Messages messages = dataSnapshot.getValue(Messages.class);

                messagesList.add(messages);
                messageAdapter.notifyDataSetChanged();

                usersMessageList.smoothScrollToPosition(usersMessageList.getAdapter().getItemCount());


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendMessage()
    {
        String messageText = write_message.getText().toString();

        if (TextUtils.isEmpty(messageText))
        {
            Toast.makeText(this, "First write your message", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String messageSenderRef = "Messages/" + messageSenderId + "/" + messageReceiverId;
            String messageReceiverRef = "Messages/" + messageReceiverId + "/" + messageSenderId;


            DatabaseReference userMessageKeyRef = rootRef.child("Messages")
                    .child(messageSenderId).child(messageReceiverId).push();

            String messagePushId = userMessageKeyRef.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message",messageText);
            messageMap.put("type","text");
            messageMap.put("from",messageSenderId);


            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushId,messageMap);
            messageBodyDetails.put(messageReceiverRef + "/" + messagePushId,messageMap);


            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ChatActivity.this, "Message not sent", Toast.LENGTH_SHORT).show();
                    }
                    write_message.setText("");
                }
            });

        }
    }
}
