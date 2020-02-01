package com.vanderweide.mybub;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Bol extends GameObject {


    public Bol(GameSurface gameSurface,int color, int radius, int x, int y)  {
        super(gameSurface,color,radius,x,y);

    }

    public void draw(Canvas canvas){
        Paint paint=new Paint();
        paint.setColor(getColor());
        paint.setStyle(getPaint().getStyle());
        canvas.drawCircle(getX(),getY(), getRadius(), paint);
    //canvas.drawRect(getX(),getY(),getX()+radius,getY()+radius,paint);
        canvas.drawLine(getX(),getY(),getX()-(radius*2),getY()-(radius*2),paint);
        canvas.drawLine(getX(),getY(),getX()+(radius*2),getY()+(radius*2),paint);
        this.lastDrawNanoTime= System.nanoTime();

    }


    public void update()  {
        // Current time in nanoseconds
        long now = System.nanoTime();

        if (lastDrawNanoTime==-1) {
            lastDrawNanoTime= now;
        }
        // Change nanoseconds to milliseconds (1 nanosecond = 1000000 milliseconds).
        int deltaTime = (int) ((now - lastDrawNanoTime)/ 1000000 );

        // Distance moves
        float distance = velocity * deltaTime;

        double movingVectorLength = Math.sqrt(movingVectorX* movingVectorX + movingVectorY*movingVectorY);

        // Calculate the new position of the game character.
        this.x = x +  (int)(distance* movingVectorX / movingVectorLength);
        this.y = y +  (int)(distance* movingVectorY / movingVectorLength);

        // When the game's character touches the edge of the screen, then change direction

        if(this.x < this.getRadius() )  {
            this.x = this.getRadius();
            this.movingVectorX = - this.movingVectorX;
        } else if(this.x > this.gameSurface.getWidth() -this.getRadius())  {
            this.x= this.gameSurface.getWidth()-this.getRadius();
            this.movingVectorX = - this.movingVectorX;
        }

        if(this.y < this.getRadius() )  {
            this.y = this.getRadius();
            this.movingVectorY = - this.movingVectorY;
        } else if(this.y > this.gameSurface.getHeight()- this.getRadius())  {
            this.y= this.gameSurface.getHeight()- this.getRadius();
            this.movingVectorY = - this.movingVectorY ;
        }

    }


}
