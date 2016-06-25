package com.dmu.p14148686.corridorescape.Model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * Created by P14148686 on 02/11/2015.
 */
public class Block {

    // A block which can hold a sprite

   public float iWidth;
   public float iHeight;
   public float iXPos;
   public float iYPos;
   public int fColour;
   public Drawable Sprite;

    public Block(float Width, float Height, float xPosition, float yPosition, int Colour, Context context, int Sprite2) // Creates a Block with a sprite
    {
        Sprite = ContextCompat.getDrawable(context,Sprite2);
        iWidth = Width;
        iHeight = Height;
        iXPos = xPosition;
        iYPos = yPosition;
        fColour = Colour;

    }

    public void ChangeSprite(Context context, int NewSprite) // Allows changing of a sprite for a Block
    {
        Sprite =  ContextCompat.getDrawable(context, NewSprite);
    }

    public void MakeBlock(int iWidth, int iHeight, int XPosition, int YPosition, float Colour)
    {
        new Rect(XPosition,YPosition, XPosition + iWidth, YPosition + iHeight);
    }

    public void DrawRect(Paint p, Canvas c)
    {
        Sprite.setBounds((int)iXPos,(int)iYPos,(int)iXPos+(int)iWidth ,(int)iYPos+(int)iHeight);
        p.setColor(fColour);
        c.drawRect(iXPos, iYPos, iXPos + iWidth, iYPos + iHeight, p);
        Sprite.draw(c);
    }

    public float getiWidth()
    {
        return iWidth;
    }

    public float getiHeight()
    {
        return iHeight;
    }
    public float getiXPos()
    {
        return iXPos;
    }
    public float getiYPos()
    {
        return iYPos;
    }

    public void setiWidth(float newWidth)
    {
        iWidth = newWidth;
    }

    public void setiHeight(float newHeight)
    {
        iHeight = newHeight;
    }
    public void setiXPos(float newXPos)
    {
        iXPos = newXPos;
    }
    public void setiYPos(float newYPos)
    {
        iYPos = newYPos;
    }

}