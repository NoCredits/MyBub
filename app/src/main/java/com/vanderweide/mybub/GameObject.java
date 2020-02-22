package com.vanderweide.mybub;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

public abstract class GameObject {

    int x;
    int y;

    public float getDX() {
        return  x*scale+offsetX*scale;
    }

    public float getDY() {
        return  y*scale+offsetY*scale;
    }


    int color;
    Paint paint;
    float radius;

    float velocity;
    int movingVectorX;
    int movingVectorY;
    long lastDrawNanoTime;
    GameSurface gameSurface;
    int screenWidth;
    int screenHeight;
    float scaleX;
    float scaleY;
    float scale;
    float offsetX;
    float offsetY;

    int layer;
    int z_index;
    boolean rendered;
    boolean touchable;
    boolean collidable;
    public boolean shooter=false;
    boolean inGrid;
    int gridPosX;
    int gridPosY;
    boolean checked;
    boolean remove;
    boolean shouldDrop;

    GameObject NE,E,SE,SW,W,NW;

    Bitmap image;
    int rowCount;
    int colCount;

    int WIDTH;
    int HEIGHT;

    int width;
    int height;


    private GameObject(){
        this.velocity = 0f;
        this.movingVectorX = 0;
        this.movingVectorY = 0;
        this.lastDrawNanoTime = -1;
        this.layer=0;
        this.z_index=0;
        this.inGrid=false;
        this.rendered=true;
        this.touchable=false;
        this.collidable=false;
        this.screenWidth=320;
        this.screenHeight=480;
        offsetX=0;
        offsetY=0;
    }


    GameObject(GameSurface gameSurface,int color, float radius, int x, int y)  {
        this();
        this.gameSurface=gameSurface;
        this.scale=this.getGameSurface().getWidth()/this.screenWidth;
        this.scaleX=this.getGameSurface().getWidth()/this.screenWidth;
        this.scaleY=this.getGameSurface().getHeight()/this.screenHeight;
        this.radius=radius;
        this.color=color;
        this.x= x;
        this.y= y;
        this.paint=new Paint();
        this.paint.setStyle(Paint.Style.FILL);

    }

    public GameObject collide(List<GameObject> gameObjectList){ return null;};

    public GameObject(GameSurface gameSurface,Bitmap image, int rowCount, int colCount, int x, int y)  {

        this();
        this.gameSurface=gameSurface;
        this.image = image;
        this.rowCount= rowCount;
        this.colCount= colCount;

        this.x= x;
        this.y= y;

        this.WIDTH = image.getWidth();
        this.HEIGHT = image.getHeight();

        this.width = this.WIDTH/ colCount;
        this.height= this.HEIGHT/ rowCount;
    }

    Bitmap createSubImageAt(int row, int col)  {
        // createBitmap(bitmap, x, y, width, height).
        Bitmap subImage = Bitmap.createBitmap(image, col* width, row* height ,width,height);
        return subImage;
    }


    void setMovingVector(int movingVectorX, int movingVectorY)  {
        this.movingVectorX= movingVectorX;
        this.movingVectorY = movingVectorY;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }


    int getX()  {
        return this.x;
    }

    int getY()  {
        return this.y;
    }

    float getRadius() {return this.radius;}

    int getColor() {return this.color;}

    public void setX(int x)  {
        this.x=x;
    }

    public void setY(int y)  {
        this.y=y;
    }

    public void setRadius(float radius) {this.radius=radius;}

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

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public boolean isTouchable() {
        return touchable;
    }

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public void draw(Canvas canvas){

    }

    public void update(){

    }

    public void calculateScreenPosToGrid(int padding){
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
        this.x= this.x % 2==0 ? (int) (this.gridPosX*(this.radius+padding)):(int)(this.gridPosX*((this.radius/2)+padding));
        //this.x=this.gridPosX*(this.radius*padding);
        this.y=(int)(this.gridPosY*(this.radius+padding));

    }

}
