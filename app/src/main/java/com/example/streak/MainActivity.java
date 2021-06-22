package com.example.streak;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    int streakNumber = 0;
    boolean today = false;
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    int strikes = 0;
    int strikeTotal = 0;
    int streakGoal = 0;
    ArrayList<String> responses = new ArrayList<String >(Arrays.asList("good job!","nice!","yay!", "good work!", "keep going!", "you can do it!"));

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        streakNumber = sharedPreferences.getInt("streak_number", 0);
        strikes = sharedPreferences.getInt("strikes", 0);
        today = sharedPreferences.getBoolean("today", false);
        strikeTotal = sharedPreferences.getInt("strike_total", 3);
        streakGoal = sharedPreferences.getInt("streak_goal", 30);
        ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setMax(streakGoal);
        String currentDate = LocalDate.now().toString();
        String previousDate = sharedPreferences.getString("date", LocalDate.now().toString());
        if (!currentDate.equals(previousDate)){
            if (today) {
                today = false;
            }
            else {
                strikes++;
                if (strikes==3){
                    streakNumber = 0;
                }
            }
        }

        TextView streakText = findViewById(R.id.streak_number);
        streakText.setText(Integer.toString(streakNumber));
        TextView strikeText = findViewById(R.id.strike_number);
        String text = strikes+"/"+strikeTotal;
        strikeText.setText(text);
        update();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onPause(){
        super.onPause();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("streak_number", streakNumber);
        editor.putInt("strikes", strikes);
        editor.putInt("strike_total", strikeTotal);
        editor.putBoolean("today", today);
        editor.putString("date", LocalDate.now().toString());
        editor.putInt("streak_goal", streakGoal);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean onTouchEvent(MotionEvent event) {
        TextView streakText = findViewById(R.id.streak_number);

        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        switch(action) {

            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                double deltaX = x2 - x1;
                if (deltaX < -MIN_DISTANCE)
                {
                    streakNumber++;

                }
                else if (deltaX > MIN_DISTANCE)
                {
                    if(streakNumber>0) {
                        streakNumber--;
                    }
                }
                else{
                    if(!today){
                        streakNumber++;
                        Toast.makeText(this, random(), Toast.LENGTH_LONG).show ();
                        today = true;
                    }
                    else if(today){
                        if(streakNumber>0) {
                            streakNumber--;
                        }
                        today = false;
                    }
                }
                streakText.setText(Integer.toString(streakNumber));
                update();
                break;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void update(){
        View backdrop = (View) findViewById(R.id.main);
        ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setProgress(streakNumber);
        if (streakNumber>=streakGoal){
            backdrop.setBackgroundColor(getColor(R.color.celebrate));
        }
        else if (today){
            backdrop.setBackgroundColor(getColor(R.color.green));
        }
        else if (!today){
            backdrop.setBackgroundColor(getColor(R.color.red));
        }


    }

    public String random(){
        if(streakNumber==(streakGoal/2)){
            return "halfway there!";
        }
        if(streakNumber==(1)){
            return "first day!";
        }
        if(streakNumber==(3)){
            return "three days in!";
        }
        if(streakNumber==(7)){
            return "a whole week!";
        }
        if(streakNumber==(streakGoal-7)){
            return "one more week!";
        }
        if(streakNumber==(streakGoal-3)){
            return "three more days!";
        }
        if(streakNumber==(streakGoal-1)){
            return "one more day!";
        }
        if (streakNumber==streakGoal){
            return "you made it, time to celebrate!";
        }

        return responses.get((int)(Math.random()*responses.size()));
    }
    }