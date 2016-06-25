package com.dmu.p14148686.corridorescape.Controller;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dmu.p14148686.corridorescape.Model.Player;
import com.dmu.p14148686.corridorescape.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;


public class LeaderboardActivity extends AppCompatActivity {

    private Player[] scores = new Player[4]; // Holds values to compare to

    TextView title;

    TextView name1, name2, name3; // 3 Names
    TextView score1, score2, score3; // 3 Scores
    TextView NewScore; // Players score

    EditText NewName; // Allows player to enter his/her naem

    int Score; // To hold tranfered score

    Button okButton; // Button to accept name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        // Gets score from game
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Score = extras.getInt("Score");
        }
        for (int i = 0; i < scores.length; i++) {
            scores[i] = new Player("Empty", 0);
        }

        // Links data to text in xml file
        title = (TextView) this.findViewById(R.id.title);

        name1 = (TextView) this.findViewById(R.id.name1);
        name2 = (TextView) this.findViewById(R.id.name2);
        name3 = (TextView) this.findViewById(R.id.name3);

        score1 = (TextView) this.findViewById(R.id.score1);
        score2 = (TextView) this.findViewById(R.id.score2);
        score3 = (TextView) this.findViewById(R.id.score3);

        NewName = (EditText) this.findViewById(R.id.NewName);
        NewScore = (TextView) this.findViewById(R.id.NewScore);
        NewScore.setText(Integer.toString(Score));


        okButton = (Button) this.findViewById(R.id.Button);


        // Waits for button to be pressed
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScores();
                Close();
            }
        });

        loadFromFile();
        displayTable();

    }

    public void Close()
    {
        this.finish();
    }


    private void displayTable() // Displays table
    {


        name1.setText(scores[0].getName());
        name2.setText(scores[1].getName());
        name3.setText(scores[2].getName());

        score1.setText(Integer.toString(scores[0].getScore()));
        score2.setText(Integer.toString(scores[1].getScore()));
        score3.setText(Integer.toString(scores[2].getScore()));


    }

    private void updateScores() // Finds place for score and adds it to list
    {
        String name = NewName.getText().toString();
        int score = Integer.parseInt(NewScore.getText().toString());
        scores[3] = new Player(name, score);

        Arrays.sort(scores);

        saveToFile();

        displayTable();
    }


    // Loading and saving code
    private boolean loadFromFile() // Loads highscores from given datafile
    {

        File root = new File(getFilesDir(), "highScores");
        if (!root.exists()) {
            root.mkdirs();
        }
        File infile = new File(root, "highScores.txt");

        try {
            BufferedReader br = new BufferedReader(new FileReader(infile));
            String line;
            for(int i =0;i < 3;i++) {
                String name = "";
                int score = 0;
                if ((line = br.readLine()) != null) {
                    name = line;
                }
                if ((line = br.readLine()) != null) {
                    score = Integer.parseInt(line);
                }
                scores[i] = new Player(name, score);
            }

            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
