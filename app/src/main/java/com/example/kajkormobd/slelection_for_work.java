package com.example.kajkormobd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.kajkormobd.support.LocalDatabase;

public class slelection_for_work extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private ImageButton back_button;
    private ToggleButton homecategory,shopcategory,freelancecategory, lastFalse;
    private TextView home_text,shop_text,freelance_text;
    private Button done_bt;
    private TextView work_name;
    private static FragmentManager fragmentManager;

    String sa;

    final Context context = this;





    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slelection_for_work);

        homecategory= (ToggleButton)findViewById(R.id.button4);
        shopcategory= (ToggleButton) findViewById(R.id.shop_button);
        freelancecategory= (ToggleButton)findViewById(R.id.button);
        work_name = findViewById(R.id.work);
        home_text= (TextView)findViewById(R.id.textView12);
        shop_text= (TextView)findViewById(R.id.textView13);
        freelance_text= (TextView)findViewById(R.id.textView11);
        done_bt = findViewById(R.id.done_bt);

        fragmentManager = getSupportFragmentManager();










        back_button =findViewById(R.id.back_button1);

        done_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sa = work_name.getText().toString();
                LocalDatabase.getInstance().setSelectedWork(sa);
                finish();



                /*

                Fragment argumentFragment = new Home_fragment();//Get Fragment Instance
                Bundle data = new Bundle();//Use bundle to pass data
                data.putString("data", sa);//put string, int, etc in bundle with a key value
                argumentFragment.setArguments(data);//Finally set argument bundle to fragment

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new Home_fragment)
                        .addToBackStack(null)
                        .commit();

                 */

            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        homecategory.setOnCheckedChangeListener(this);
        shopcategory.setOnCheckedChangeListener(this);
        freelancecategory.setOnCheckedChangeListener(this);

        homecategory.setChecked(true);





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


                        AlertDialog.Builder home_dialouge = new AlertDialog.Builder(context);
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


                        AlertDialog.Builder home_dialouge = new AlertDialog.Builder(context);
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


                        AlertDialog.Builder home_dialouge = new AlertDialog.Builder(context);
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
