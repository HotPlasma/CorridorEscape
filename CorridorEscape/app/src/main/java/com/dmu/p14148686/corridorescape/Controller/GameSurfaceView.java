package com.dmu.p14148686.corridorescape.Controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dmu.p14148686.corridorescape.Model.Block;
import com.dmu.p14148686.corridorescape.Model.Globals;
import com.dmu.p14148686.corridorescape.R;

import java.util.Random;
import java.util.Vector;

/**
 * Created by P14148686 on 30/11/2015.
 */
public class GameSurfaceView extends SurfaceView implements Runnable {

    private final static int MAX_FPS = 60;                   // desired fps
    private final static int MAX_FRAME_SKIPS = 10;            // maximum number of frames to be skipped
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;  // the frame period

    public Context GameContext; // the context of the game

    SoundPool SoundFX; // A collection of all the sound effects in my game
    SoundPool.Builder SoundFXBuilder; // A builder of sound effects
    AudioAttributes Attributes; // Attributes needed to play soundeffects
    AudioAttributes.Builder AttributesBuilder;
    int [] SoundID = new int [2]; // A array allows me to pick which sound effect I wish to play


    boolean GameFinished = false; // Asks if the game is finished

    // Used for gameloop
    SurfaceHolder holder;
    private boolean ok = false;
    Thread t = null;

    // Used for drawing
    Paint paint = new Paint();

    // Used to insure accurate render on all phones regardless of screensize
    Point ScreenSize;
    float ScaleWidth;
    float ScaleHeight;

    float TempVelocity; // Holds the last velocity if the game is paused

    int CurrentCorridor = 1; // How many corridors should be drawn

    PointF velocity = new PointF(2, 0); // A vector holding the speed the player will travel at

    int iScore = 0; // Holds the score. Is passed to Leaderboard when game has ended

    private Handler hHandler;

    Vector<Block> Corridor = new Vector<>(); // A vector which holds the number of corridors.
    Block Cat; // The player
    Block Pause; // The pause icon at the top right part of the screen.
    Block UpDoor; // The door the cat starts at
    Block DownDoor; // The door the player must reach
    int iLevel = 2; // Which level the player is on. Starts at 2 so the player gets to jump down one corridor in the first level
    int CorridorCount = 1; // The number of corridors that need to be drawn
    boolean bScreenScrolled = false; // Has the screen been scrolled. Has player reached bottom of screen?
    boolean bGoingRight = true; // Is player facing right direction
    boolean bPaused = false; // Is the game paused



    private long beginTime;                                     // the time when the cycle began
    private long timeDiff;                                      // the time it took for the cycle to execute
    private int sleepTime;                                      // ms to sleep
    private int framesSkipped;                                  // number of frames being skipped


    public GameSurfaceView(Context context, Point screenS) {
        super(context);
        holder = getHolder();
        ScreenSize = screenS; // Get screensize of phone
        hHandler = new Handler();
        // Game will look the same on all phones
        ScaleWidth = (float) ScreenSize.x / 1080;
        ScaleHeight = (float) ScreenSize.y / 1920;

        // Create Cat and pause button in default starting location
        Cat = new Block(ScaleWidth * 150, ScaleHeight * 300, ScaleWidth * 100, (ScaleHeight * 500), Color.argb(0, 0, 0, 0), getContext(), R.drawable.catright);
        Pause = new Block(ScaleWidth * 150, ScaleHeight * 150, ScaleWidth * 900, ScaleHeight * 50, Color.argb(0, 0, 0, 0), getContext(), R.drawable.pause);
        // Set up the first corridor and doors
        SetUpCorridor(1);
        SetUpDoors(1);
        GameContext = context; // Assign context
        LoadInSoundFX(); // Load in the sound effects
    }


    private void LoadInSoundFX() // Builds sound effects and loads them in for use
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) // So it works on lollypop and newer OS
        {
            AttributesBuilder = new AudioAttributes.Builder();
            AttributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
            AttributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
            Attributes = AttributesBuilder.build();
            SoundFXBuilder = new SoundPool.Builder();
            SoundFXBuilder.setAudioAttributes(Attributes);
            SoundFX = SoundFXBuilder.build();
        }
        else
        {
            SoundFX = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        }

        SoundID[0] = SoundFX.load(GameContext,R.raw.boing,1);
        SoundID[1] = SoundFX.load(GameContext,R.raw.failedsfx, 1);
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };


    public int getRandomInt(int iMin, int iMax) { //Takes two integers and returns a integer between them
        Random rand = new Random();

        int randomNum = rand.nextInt((iMax - iMin) + 1) + iMin;

        return randomNum;
    }

    public void SetUpCorridor(int iCorridorNum) // Sets up corridors. Amount depends on given parameter
    {

        float iCorridorYPos = 150;

        for (int i = 0; i < iCorridorNum; i++)
        {
            iCorridorYPos += 350; // All on 350 for for each corridor to ensure next corridor is drawn under player
        }
        // Draw corridor
        Corridor.add(new Block(ScaleWidth * 890, ScaleHeight * 300, ScaleWidth * 100, (ScaleHeight * iCorridorYPos), Color.argb(255, 255, 150, 76), getContext(), R.drawable.corridor));
        }


    public void SetUpDoors(int iCorridorNum) // Similar to corridor but draws doors on correct corridor
    {

        boolean DoorsColliding = false;
        float iCorridorYPos = (150) ;

        for (int i = 0; i < iCorridorNum; i++)
        {
            iCorridorYPos += (350);
        }

        // Creates a random X coordinate inside the corridor
        int UpDoorPos = getRandomInt( 100, 850);
        int DownDoorPos = getRandomInt(100, 850);

        // If doors overlap then try again
        if (!(DownDoorPos + 120 < UpDoorPos || DownDoorPos > UpDoorPos + 120)) {
            DoorsColliding = true;
            while (DoorsColliding) {
                UpDoorPos = getRandomInt(100, 850);
                DownDoorPos = getRandomInt(100, 850);
                if (DownDoorPos + 120 < UpDoorPos || DownDoorPos > UpDoorPos + 120) {
                    DoorsColliding = false;
                }
            }
        }
        // Draw Doors
        UpDoor = new Block(ScaleWidth * 100, ScaleHeight * 300, ScaleWidth * UpDoorPos, ScaleHeight * iCorridorYPos, Color.argb(255, 243, 118, 37), getContext(), R.drawable.updoor);

        DownDoor = new Block(ScaleWidth * 100, ScaleHeight * 300, ScaleWidth * DownDoorPos, ScaleHeight * iCorridorYPos, Color.argb(255, 255, 214, 143), getContext(), R.drawable.downdoor);

        Cat.iXPos = (ScaleWidth * UpDoorPos); // Player starts at updoor position

        // Ensures player is always traveling correct direction towards door
        if (UpDoor.iXPos < DownDoor.iXPos) {
            bGoingRight = true;
            if (bGoingRight) {
                if (velocity.x > 0)
                {
                    velocity.x += 0.5;
                }
                else
                {
                    velocity.x = -velocity.x;
                }
                Cat.ChangeSprite(getContext(), R.drawable.catright);
            }
        }
        else
        {
            bGoingRight = false;
            if (!bGoingRight)
            {
                if (velocity.x < 0)
                {
                    velocity.x -= 0.5;
                }
                else
                {
                    velocity.x = -velocity.x;
                }
                Cat.ChangeSprite(getContext(), R.drawable.catleft);
            }

        }
    }

    private void updateCanvas() {

        // Moves cat by velocity.x
        Cat.iXPos += (velocity.x * ScaleWidth);

        // If cat hits right side of corridor
        if (Cat.iXPos > (ScaleWidth * 850))
        {
            SoundFX.play(SoundID[1], Globals.SFX_VOLUME/100,Globals.SFX_VOLUME/100,0,0,1); // Death sound
            GameFinished = true; // End game
            velocity.x = 0;
            Cat.iXPos = (ScaleWidth * 849);
        }

        if (Cat.iXPos < (ScaleWidth * 100)) // If cat hits left side of corridor
        {
            SoundFX.play(SoundID[1],Globals.SFX_VOLUME/100,Globals.SFX_VOLUME/100,0,0,1); // Death sound
            GameFinished = true;
            velocity.x = 0;
            Cat.iXPos = (ScaleWidth * 101);
        }


    }

    protected void drawCanvas(Canvas canvas) {
        // Draw game
        canvas.drawARGB(255, 48, 163, 147);
        for (int i = 0; i < Corridor.size(); i++) // Draw all corridors
        {
            Corridor.get(i).DrawRect(paint, canvas);
        }

        Pause.DrawRect(paint, canvas); // Draw pause button

        // Draws score
        paint.setTextSize(160 * ScaleWidth);
        paint.setARGB(255, 186, 246, 236);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(iScore), ScreenSize.x / 2, 150 , paint);

        // Draw doors
        UpDoor.DrawRect(paint, canvas);
        DownDoor.DrawRect(paint, canvas);

        // Draw cat
        Cat.DrawRect(paint, canvas);
        hHandler.postDelayed(r, 10);

        // If paused draw paused text
        if (bPaused)
        {
            paint.setARGB(255, 186, 246, 236);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(160 * ScaleWidth);
            canvas.drawText("Paused", ScreenSize.x / 2, ScreenSize.y / 2, paint);
        }

        // If game ended draw game over text
        if (GameFinished)
        {
            paint.setARGB(255, 186, 246, 236);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(160 * ScaleWidth);
            canvas.drawText("Game Over", ScreenSize.x / 2, ScreenSize.y / 2, paint);
            paint.setTextSize(80  * ScaleWidth);
            canvas.drawText("Press anywhere to continue", ScreenSize.x / 2, ScreenSize.y / 2 + 100, paint);
        }

    }

    public void run() { // Ensures game runs same speed on all phones
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        while (ok) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            Canvas c = holder.lockCanvas();
            synchronized (holder) {
                beginTime = System.currentTimeMillis();
                framesSkipped = 0;  // resetting the frames skipped
                // update game state
                this.updateCanvas();
                // render state to the screen
                // draws the canvas on the panel
                this.drawCanvas(c);
                // calculate how long did the cycle take
                timeDiff = System.currentTimeMillis() - beginTime;
                // calculate sleep time
                sleepTime = (int) (FRAME_PERIOD - timeDiff);
                if (sleepTime > 0) {
                    // if sleepTime > 0 put to sleep for short period of time
                    try {
                        // send the thread to sleep for a short period
                        // very useful for battery saving
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                }

                //Log.d("Sleep Time", String.valueOf(sleepTime));
                //ADD THIS IF WE ARE DOING LOTS OF WORK
                //If sleeptime is greater than a frame length, skip a number of frames
                while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                    // we need to catch up
                    // update without rendering
                    this.updateCanvas();
                    // add frame period to check if in next frame
                    sleepTime += FRAME_PERIOD;
                    framesSkipped++;
                   /* Log.d("Skipping", "Skip");*/
                }
                holder.unlockCanvasAndPost(c);
            }
        }
    }

    public void pause() {
        SoundFX.release();
        ok = false;
        while (true) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
        t = null;
    }

    public void resume() {
        LoadInSoundFX();
        ok = true;
        t = new Thread(this);
        t.start();
    }

    public void pauseGameplay() // Allows pausing of gameplay
    {
        TempVelocity = velocity.x;
        velocity.x = 0;
        bPaused = true;
    }

    public void resumeGameplay() // Allows player to unpause gameplay
    {
        bPaused = false;
        velocity.x = TempVelocity;

    }

    public void pressUpdate(float X, float Y) { // Update on touch

        // If player has pressed pause button
        if (X >  ScaleWidth * 900 && Y < ScaleHeight * 200)
        {
            // Pause game
            if (!bPaused) {
                pauseGameplay();
            }
        }
        // If game already paused then resume
        else if (bPaused)
        {
            resumeGameplay();
        }

        else  if (Cat.iXPos + (ScaleWidth * 150) < DownDoor.iXPos || Cat.iXPos > DownDoor.iXPos + (ScaleHeight * 100)) // Not collided with Down Door
        {
            // Do nothing
        }
        else // Collided with Down door
        {
            Cat.iYPos += (ScaleHeight * 350); // Send to second floor position
            SoundFX.play(SoundID[0],Globals.SFX_VOLUME/100,Globals.SFX_VOLUME/100,0,0,1);
            if (Cat.iYPos == (ScaleHeight * 1900) ) // Cat has reached bottom of screen
            {
                for (int i = 0; i < Corridor.size(); i++) { // Move corridors up
                    Corridor.elementAt(i).iYPos -= (ScaleHeight * 350);
                }
                CurrentCorridor--;
                iScore++; // Increment score
                SetUpCorridor(CurrentCorridor);
                SetUpDoors(CurrentCorridor); // Set up the doors for the next level
                Cat.iYPos -= (ScaleHeight * 700); // Move cat up
                bScreenScrolled = true; // Screen has been scrolled
            }
            else
            {
                // Generic create next floor and doors
                CurrentCorridor++; // Set floor to second
                CorridorCount++;
                iScore++; // Increment score
                SetUpCorridor(CurrentCorridor);
                SetUpDoors(CurrentCorridor); // Set up the doors for the next level
            }

            // Ensures screen scrolling does not break scrolling
            if(bScreenScrolled == true)
            {
                bScreenScrolled = false;
                CorridorCount++;
            }
            // If number of corridors match level then go to next level
            if (iLevel == CorridorCount - 1)
            {
                Corridor.clear();
                iLevel++;
                CorridorCount = 1;
                CurrentCorridor = 1;
                Cat.iYPos =  (ScaleHeight * 500);
                SetUpCorridor(CurrentCorridor);
                SetUpDoors(CurrentCorridor); // Set up the doors for the next level
            }
        }

    }
}
