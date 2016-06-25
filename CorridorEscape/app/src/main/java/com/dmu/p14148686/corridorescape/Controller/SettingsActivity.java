package com.dmu.p14148686.corridorescape.Controller;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.dmu.p14148686.corridorescape.Model.Globals;
import com.dmu.p14148686.corridorescape.Model.Player;
import com.dmu.p14148686.corridorescape.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



public class SettingsActivity extends Activity {

    Player[] scores;
    private SeekBar Music = null;
    private SeekBar SFX = null;
    private AudioManager audioManager = null;
    Button Button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Allows management of audio in settings

        audioManager=(AudioManager)getSystemService(this.AUDIO_SERVICE);

        Music = (SeekBar)findViewById(R.id.seekBar);
        SFX = (SeekBar)findViewById(R.id.seekBar2);

        Music.setProgress((int)Globals.MUSIC_VOLUME); // Sets progress of seek bar to global music volume
        SFX.setProgress((int)Globals.SFX_VOLUME); // Sets progress of seek bar to global sfx volume

        initBar(Music, AudioManager.STREAM_MUSIC, 1);
        initBar(SFX, AudioManager.STREAM_MUSIC, 2);



        Button1 = (Button) this.findViewById(R.id.button);
        scores = new Player[3];
        // Set font

        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetScoreboard();
                // saveToFile();
            }
        });


    }

    private void initBar(SeekBar bar, final int stream, int soundID) // Initialises both seek bars in settings
    {
        if (soundID == 1)
        {
            bar.setProgress((int) Globals.MUSIC_VOLUME);

            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onProgressChanged(SeekBar bar, int progress, boolean fromUser)
                {
                    Globals.MUSIC_VOLUME = progress;
                }

                @Override
                public void onStartTrackingTouch(SeekBar bar) {

            }
                @Override
                public void onStopTrackingTouch(SeekBar bar) {

            }

            });
        }
        else  if (soundID == 2)
        {
            bar.setProgress((int) Globals.SFX_VOLUME);

            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                    Globals.SFX_VOLUME = progress;
                }

                @Override
                public void onStartTrackingTouch(SeekBar bar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar bar) {

                }
            });
        }
    }


    public void ResetScoreboard() // Resets the scoreboard to default values when button pressed
    {
        Toast.makeText(getApplicationContext(), "Leaderboard Reset.",
                Toast.LENGTH_SHORT).show();
        for (int i = 0; i < scores.length; i++)
        {
            scores[i] = new Player("Empty", 0);
        }
        saveToFile();
    }



    private boolean saveToFile()
    {
        // Construct string
        String strToWrite = "";
        for(int i =0;i < 3;i++)
        {
            strToWrite += scores[i].getName() + "\n";
            strToWrite += Integer.toString(scores[i].getScore()) + "\n";
        }

        // Save it to a file
        try
        {
            File root = new File(getFilesDir(), "highScores");
            if (!root.exists()) {
                root.mkdirs();
            }
            File outfile = new File(root, "highScores.txt");
            FileWriter writer = new FileWriter(outfile);
            writer.append(strToWrite);
            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
