package com.dmu.p14148686.corridorescape.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dmu.p14148686.corridorescape.Controller.GameActivity;
import com.dmu.p14148686.corridorescape.R;

/**
 * Created by P14148686 on 07/03/2016.
 */
public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //Toast.makeText(this, "OnCreate()", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(this, "onPause()", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "onResume()", Toast.LENGTH_LONG).show();
    }

    public void startGame(View view) { // Starts gameactivity
        Intent intentStart = new Intent(this, GameActivity.class);

        startActivity(intentStart);
    }

    public void startLeaderBoards(View view) { // Starts LeaderboardsActivity
        Intent intentStart = new Intent(this, LeaderboardActivity.class);

        startActivity(intentStart);
    }

    public void startHelp(View view) { // Starts Instructions activity
        Intent intentStart = new Intent(this, HelpActivity.class);

        startActivity(intentStart);
    }



    public void startSettings(View view) { // Starts settings acitivty
        Intent intentStart = new Intent(this, SettingsActivity.class);

        startActivity(intentStart);
    }
}
