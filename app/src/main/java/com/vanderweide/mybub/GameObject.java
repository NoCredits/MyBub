package com.vanderweide.mybub;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class GameObject {

    protected int x;
    protected int y;
    protected int color;
    protected Paint paint;
    protected int radius;

    protected float velocity;
    protected int movingVectorX;
    protected int movingVectorY;
    protected long lastDrawNanoTime;
    protected GameSurface gameSurface;

    public GameObject(){
        this.velocity = 0.5f;
        this.movingVectorX = 10;
        this.movingVectorY = 5;
        this.lastDrawNanoTime = -1;
    }

    public GameObject(GameSurface gameSurface,int color, int radius, int x, int y)  {
        this();
        this.gameSurface=gameSurface;
        this.radius=radius;
        this.color=color;
        this.x= x;
        this.y= y;
        this.paint=new Paint();
        this.paint.setStyle(Paint.Style.FILL);
    }
    public void setMovingVector(int movingVectorX, int movingVectorY)  {
        this.movingVectorX= movingVectorX;
        this.movingVectorY = movingVectorY;
    }


    public int getX()  {
        return this.x;
    }

    public int getY()  {
        return this.y;
    }

    public int getRadius() {return this.radius;}

    public int getColor() {return this.color;}

    public void setX(int x)  {
        this.x=x;
    }

    public void setY(int y)  {
        this.y=y;
    }

    public void setRadius(int radius) {this.radius=radius;}

    public void setColor(int color) {this.color=color;}

    public Paint getPaint() {  return this.paint;  }

    public void setPaint(Paint paint) {    this.paint = paint;    }


    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public int getMovingVectorX() {
        return movingVectorX;
    }

    public void setMovingVectorX(int movingVectorX) {
        this.movingVectorX = movingVectorX;
    }

    public int getMovingVectorY() {
        return movingVectorY;
    }

    public void setMovingVectorY(int movingVectorY) {
        this.movingVectorY = movingVectorY;
    }

    public long getLastDrawNanoTime() {
        return lastDrawNanoTime;
    }

    public void setLastDrawNanoTime(long lastDrawNanoTime) {
        this.lastDrawNanoTime = lastDrawNanoTime;
    }

    public GameSurface getGameSurface() {
        return gameSurface;
    }

    public void setGameSurface(GameSurface gameSurface) {
        this.gameSurface = gameSurface;
    }

    public void draw(Canvas canvas){

    }

    public void update(){

    }

}
