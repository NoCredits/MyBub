package com.vanderweide.mybub;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class GameObject {

    int x;
    int y;
    int color;
    Paint paint;
    int radius;

    float velocity;
    int movingVectorX;
    int movingVectorY;
    long lastDrawNanoTime;
    GameSurface gameSurface;
    int layer;
    int z_index;

    boolean inGrid;
    int gridPosX;
    int gridPosY;


    private GameObject(){
        this.velocity = 0.5f;
        this.movingVectorX = 10;
        this.movingVectorY = 5;
        this.lastDrawNanoTime = -1;
        this.layer=0;
        this.z_index=0;
        this.inGrid=false;
    }


    GameObject(GameSurface gameSurface,int color, int radius, int x, int y)  {
        this();
        this.gameSurface=gameSurface;
        this.radius=radius;
        this.color=color;
        this.x= x;
        this.y= y;
        this.paint=new Paint();
        this.paint.setStyle(Paint.Style.FILL);
    }


    void setMovingVector(int movingVectorX, int movingVectorY)  {
        this.movingVectorX= movingVectorX;
        this.movingVectorY = movingVectorY;
    }


    int getX()  {
        return this.x;
    }

    int getY()  {
        return this.y;
    }

    int getRadius() {return this.radius;}

    int getColor() {return this.color;}

    public void setX(int x)  {
        this.x=x;
    }

    public void setY(int y)  {
        this.y=y;
    }

    public void setRadius(int radius) {this.radius=radius;}

    void setColor(int color) {this.color=color;}

    Paint getPaint() {  return this.paint;  }

    public void setPaint(Paint paint) {    this.paint = paint;    }


    public float getVelocity() {
        return velocity;
    }

    void setVelocity(float velocity) {
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

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    int getZ_index() {
        return z_index;
    }

    void setZ_index(int z_index) {
        this.z_index = z_index;
    }
    public boolean isInGrid() {
        return inGrid;
    }

    void setInGrid(boolean inGrid) {
        this.inGrid = inGrid;
    }

    public int getGridPosX() {
        return gridPosX;
    }

    void setGridPosX(int gridPosX) {
        this.gridPosX = gridPosX;
    }

    public int getGridPosY() {
        return gridPosY;
    }

    void setGridPosY(int gridPosY) {
        this.gridPosY = gridPosY;
    }



    public void draw(Canvas canvas){

    }

    public void update(){

    }

    public void addTogrid(GridObject grid, int row, int col){
        if (!this.inGrid){
            grid.add(row,col,this);
        } else grid.remove(row,col);
    }

    public void removeFromgrid(GridObject grid){
        if (this.inGrid){
            grid.remove(this.gridPosX,this.gridPosY);
        }
    }
    public void calculateGridPosToScreen(int padding){
        this.x= this.x % 2==0 ? this.gridPosX*(this.radius+padding):this.gridPosX*((this.radius/2)+padding);
        //this.x=this.gridPosX*(this.radius*padding);
        this.y=this.gridPosY*(this.radius+padding);

    }

}
