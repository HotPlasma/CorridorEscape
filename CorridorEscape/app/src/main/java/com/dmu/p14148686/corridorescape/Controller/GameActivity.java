package com.dmu.p14148686.corridorescape.Controller;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.dmu.p14148686.corridorescape.Model.Globals;
import com.dmu.p14148686.corridorescape.R;

/**
 * Created by P14148686 on 02/11/2015.
 */
public class GameActivity  extends AppCompatActivity {


    //The games gameview
   // private GameView gv;
    private GameSurfaceView gsv;
    public Point screenSize; // Screen size of phone

    MediaPlayer ThemeMusic; // Game music

    float xPos;
    float yPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init music

        ThemeMusic = MediaPlayer.create(this, R.raw.backingtrack);
        ThemeMusic.start();
        ThemeMusic.setLooping(true);
        ThemeMusic.setVolume(Globals.MUSIC_VOLUME / 100, Globals.MUSIC_VOLUME/100);

        // Removes unneeded operating system user interface items
        int mUIFlag = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        //Pass the GameView to setContentView
        //setContentView(R.layout.activity_main);

        // Gets size of screen
        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;

        screenSize = new Point(widthPixels,heightPixels);
       // gv = new GameView(this, screenSize);
        gsv = new GameSurfaceView(this, screenSize);
        setContentView(gsv);
    }

    public void GameEnded() // Ends game and passed score to Leaderboard acitivty
    {
        ThemeMusic.stop();

        Intent intentStart = new Intent(this, LeaderboardActivity.class);
        intentStart.putExtra("Score", gsv.iScore);
        startActivity(intentStart);


        this.finish();

    }

    @Override
    protected  void onPause()
    {
        super.onPause();
        gsv.pause();
        ThemeMusic.pause(); // Stops music when closing app
    }
    @Override
    protected  void onResume()
    {
        super.onResume();
        gsv.resume();
        ThemeMusic.start(); // Restarts music from same point if app resumed
    }

    public boolean onTouchEvent(MotionEvent event) // On screen being tapped
    {



        int eventaction = event.getAction();

        switch (eventaction)
        {
            case MotionEvent.ACTION_DOWN: // Screen pressed down
                xPos = event.getX();
                yPos = event.getY();
                gsv.pressUpdate(xPos,yPos);
                // If game finished bool is true end game
                if (gsv.GameFinished == true)
                {
                    GameEnded();
                }
                break;


            case MotionEvent.ACTION_UP: // Screen released
                break;
        }


        return true;
    }
}
