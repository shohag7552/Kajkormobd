package com.example.kajkormobd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile_post_adpter extends FirestoreRecyclerAdapter<profile_post, profile_post_adpter.postHolder> {





    public profile_post_adpter(@NonNull FirestoreRecyclerOptions<profile_post> options) {

        super(options);


    }

    @Override
    protected void onBindViewHolder(@NonNull postHolder holder, int position, @NonNull profile_post model) {


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




    @NonNull
    @Override
    public postHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_post_item_for_recycle3,parent,false);



        return new postHolder(view);
    }

    public void deleteItem(int position){

        getSnapshots().getSnapshot(position).getReference().delete();

    }






    class postHolder extends RecyclerView.ViewHolder {

        TextView post_location, post_work_name, post_experience, post_description, post_amount, post_nego, post_phone, post_time, user_name;
        CircleImageView user_image;

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



        }


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