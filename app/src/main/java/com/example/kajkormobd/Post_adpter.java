package com.example.kajkormobd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Post_adpter extends FirestoreRecyclerAdapter<Posts,Post_adpter.postHolder> {
    private  onItemClickListener listener;
    private String uid;
    private FirebaseAuth auth;
    private Context c;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String currentuser ="new";
    private String senderId;

    private DatabaseReference senderRef ;





    public Post_adpter(@NonNull FirestoreRecyclerOptions<Posts> options) {

        super(options);


    }

    @Override
    protected void onBindViewHolder(@NonNull final postHolder holder, final int position, @NonNull final Posts model) {


        final DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        String id = documentSnapshot.getString("user_id");


        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            auth = FirebaseAuth.getInstance();
            uid = auth.getCurrentUser().getUid();

            if (uid.equals(id)){
                RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
                param.height = 0;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                holder.itemView.setVisibility(View.VISIBLE);

            }else {
                holder.post_work_name.setText(model.getWork_name());
                holder.post_experience.setText(model.getWork_experience());
                holder.post_description.setText(model.getWork_description());
                holder.post_amount.setText(model.getWork_amount());
                holder.post_nego.setText(model.getNegotiable_condition());
                holder.post_phone.setText(model.getWork_phone());
                holder.post_location.setText(model.getWork_location());
                holder.post_time.setText(model.getPost_time());
                holder.user_name.setText(model.getUser_name());

                Picasso.get().load(model.getUser_image()).fit().centerCrop().into(holder.user_image);

            }

        }else {
            holder.post_work_name.setText(model.getWork_name());
            holder.post_experience.setText(model.getWork_experience());
            holder.post_description.setText(model.getWork_description());
            holder.post_amount.setText(model.getWork_amount());
            holder.post_nego.setText(model.getNegotiable_condition());
            holder.post_phone.setText(model.getWork_phone());
            holder.post_location.setText(model.getWork_location());
            holder.post_time.setText(model.getPost_time());
            holder.user_name.setText(model.getUser_name());

            Picasso.get().load(model.getUser_image()).fit().centerCrop().into(holder.user_image);

        }
        holder.favorite_post_tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                c= buttonView.getContext();

                Posts posts = documentSnapshot.toObject(Posts.class);


                if (isChecked){

                    holder.favorite_post_tb.setBackgroundDrawable(ContextCompat.getDrawable(c,R.drawable.fav_button_color_change));

                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        auth = FirebaseAuth.getInstance();
                        uid = auth.getCurrentUser().getUid();

                        Map<String, Object> favmap = new HashMap<>();
                        favmap.put("work_name",posts.work_name);
                        favmap.put("work_experience",posts.work_experience);
                        favmap.put("work_description",posts.work_description);
                        favmap.put("work_location",posts.work_location);
                        favmap.put("work_amount",posts.work_amount);
                        favmap.put("negotiable_condition",posts.negotiable_condition);
                        favmap.put("work_phone",posts.work_phone);
                        favmap.put("user_id",posts.user_id);
                        favmap.put("post_time",posts.post_time);
                        favmap.put("user_name",posts.user_name);
                        favmap.put("user_image",posts.user_image);
                        favmap.put("uid",uid);


                        // Add a new document with a generated ID
                        db.collection("favorite")
                                .add(favmap)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        Toast.makeText(c, "Successfully added", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        holder.favorite_post_tb.setEnabled(false);


                    }else {


                        Toast.makeText(c, "Create an Account First", Toast.LENGTH_SHORT).show();
                    }






                }
                else {

                    holder.favorite_post_tb.setEnabled(false);
                    holder.favorite_post_tb.setBackgroundDrawable(ContextCompat.getDrawable(c,R.drawable.fav_button_color_change));



                }
            }
        });


        Posts posts = documentSnapshot.toObject(Posts.class);
        final String userId = posts.user_id;




        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c= v.getContext();

                Posts posts = documentSnapshot.toObject(Posts.class);
                final String userName = posts.user_name;
                final String userImage = posts.user_image;


                senderRef = FirebaseDatabase.getInstance().getReference().child("Chat");


                //Toast.makeText(c, "success", Toast.LENGTH_SHORT).show();

                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                     senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();



                        holder.chat.setEnabled(false);


                        if (currentuser.equals("new")){


                            //-----send data into database---


                            senderRef.child(senderId).child(userId).child("request_type").setValue("sent")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                senderRef.child(userId).child(senderId).child("request_type").setValue("received")
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful())
                                                                {
                                                                    currentuser = "request_sent";

                                                                    Intent chatIntent = new Intent(c,ChatActivity.class);
                                                                    chatIntent.putExtra("user_id",userId);
                                                                    chatIntent.putExtra("user_name",userName);
                                                                    chatIntent.putExtra("user_image",userImage);
                                                                    c.startActivity(chatIntent);

                                                                }
                                                            }
                                                        });

                                            }
                                        }
                                    });

                            //----- set Contact-------

//                            DocumentSnapshot userref = db.collection("users").document(uid).get().getResult();
//                            Map<String,Object> senderMap = new HashMap<>();
//                            senderMap.put("name",userref.)
//
//                            contextRef.child(senderId).updateChildren()


                            //---- Send data into firestore database-----

                            Map<String, Object> chatMap = new HashMap<>();
                            chatMap.put("work_name", posts.work_name);
                            chatMap.put("post_user_id", posts.user_id);
                            chatMap.put("user_name", posts.user_name);
                            chatMap.put("user_image", posts.user_image);
                            chatMap.put("user_id", senderId);




                            db.collection("chats")
                                    .add(chatMap)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(c, "add to the chat list", Toast.LENGTH_SHORT).show();



                                        }
                                    });



                        }


                }
                else {
                    Toast.makeText(c, "need account.", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }









    @NonNull
    @Override
    public postHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_for_recycle2,parent,false);



        return new postHolder(view);
    }

    public void deleteItem(int position){

        getSnapshots().getSnapshot(position).getReference().delete();

    }






    class postHolder extends RecyclerView.ViewHolder{

        TextView post_location,post_work_name,post_experience,post_description,post_amount,post_nego,post_phone,post_time,user_name;
        CircleImageView user_image;
        ToggleButton favorite_post_tb;
        TextView chat;

        public postHolder(@NonNull final View itemView) {
            super(itemView);

            post_work_name = itemView.findViewById(R.id.textView15);
            post_experience = itemView.findViewById(R.id.textView16);
            post_location = itemView.findViewById(R.id.tv_location);
            post_description = itemView.findViewById(R.id.textView18);
            post_amount = itemView.findViewById(R.id.textView17);
            post_nego = itemView.findViewById(R.id.tv_nego);
            post_phone = itemView.findViewById(R.id.tv_phn);
            post_time = itemView.findViewById(R.id.time_date);
            user_name = itemView.findViewById(R.id.textView14);
            user_image = itemView.findViewById(R.id.circleImageView);
            favorite_post_tb = itemView.findViewById(R.id.fv_tb);
            chat = itemView.findViewById(R.id.chat);

            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);

                    }
                }
            });







        }



    }
    public interface onItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;

    }






    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}