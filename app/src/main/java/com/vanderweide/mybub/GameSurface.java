package com.vanderweide.mybub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;

    private List<GameObject> gameList = new ArrayList<GameObject>();

    private int gridRows=11;
    private int gridCols=11;
    private GameObject ammo;

    public GameSurface(Context context)  {
        super(context);

        // Make Game Surface focusable so it can handle events.
        this.setFocusable(true);

        // Sét callback.
        this.getHolder().addCallback(this);
    }

    public void remove(GameObject gameObject){
        Iterator<GameObject> iterator= this.gameList.iterator();

        while(iterator.hasNext()) {
            if (iterator.next()==gameObject) iterator.remove();
        }
    }

    public void update()  {

        GameObject foe=ammo.collide(gameList);
        if (foe!=null){ //collision with foe

            ammo.inGrid=true;
            int x=ammo.gridPosX;
            int y=ammo.gridPosY;
            int color=ammo.color;
            ammo.setVelocity(0);
            Utils.createArrayList(gameList);
            if (y%2==0 ) { //even rij
                Utils.checkNextCollide(x, y - 1, color); //rechtsboven
                Utils.checkNextCollide(x + 1, y, color); //rechts
                Utils.checkNextCollide(x, y + 1, color);//rechtsonder
                Utils.checkNextCollide(x - 1, y + 1, color); //linksonder
                Utils.checkNextCollide(x - 1, y, color); //links
                Utils.checkNextCollide(x - 1, y - 1, color); //linksboven
            } else {
                Utils.checkNextCollide(x+1, y - 1, color); //rechtsboven
                Utils.checkNextCollide(x + 1, y, color); //rechts
                Utils.checkNextCollide(x+1, y + 1, color);//rechtsonder
                Utils.checkNextCollide(x , y + 1, color); //linksonder
                Utils.checkNextCollide(x , y, color); //links
                Utils.checkNextCollide(x , y - 1, color); //linksboven
            }
            ammo.shooter=false;
            ammo.collidable=true;
            ammo.calculateGridPosToScreen(0);

                if (Utils.ballsFalling>1){
                    ammo.shouldDrop=true;

                    for (GameObject gameObject:gameList){
                        if (gameObject.shouldDrop){
                            gameObject.inGrid = false;
                            gameObject.collidable = false;
                            gameObject.remove = true;
                            gameObject.setMovingVectorX((int) (Utils.randInt(-500,500)));
                            gameObject.setMovingVectorY((int) (1000 + Utils.offsetY));
                            gameObject.setVelocity(0.5f);
                        }
                    }
                }

            Hexagon hex=new Hexagon(this,Color.BLUE,true);
            hex.setLayer(Utils.randInt(0,5));
            hex.setColor(Utils.hexColor(hex.getLayer()));
            hex.setLayer(1);
            hex.shooter=true;
            gameList.add(hex);
            ammo=hex;
        }

        Utils.newDrop(gameList);

        Iterator<GameObject> iterator= gameList.iterator();

        while(iterator.hasNext()) {
            GameObject game = iterator.next();
            game.update();
            if (game.remove && game.getY()+Utils.offsetY>1000){
                iterator.remove();
            }
        }

        if (gameList.size()<=1){ //alle ballen zijn op
            gameList=Utils.createGrid(this);

            //create shooter
            Hexagon hex=new Hexagon(this,Color.BLUE,true);

            hex.setLayer(Utils.randInt(0,5));
            hex.setColor(Utils.hexColor(hex.getLayer()));
            hex.setLayer(1);

            gameList.add(hex);
            ammo=hex;
            ammo.shooter=true;

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //draw arrow
            if (ammo.getVelocity()==0){
                Utils.arrowFromX =  (int)(ammo.getX() *ammo.scale);
                Utils.arrowFromY = (int)(ammo.getY() *ammo.scale);
                Utils.arrowToX =  (int)(event.getX() );
                Utils.arrowToY = (int)(event.getY() );
                Utils.showArrow=true;

            }
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (ammo.getVelocity()==0 && Utils.showArrow) {
                Utils.arrowToX = (int) (event.getX());
                Utils.arrowToY = (int) (event.getY());
                return true;
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {


            Utils.showArrow=false;
            int x=  (int)(event.getX() /ammo.scale);
            int y = (int)(event.getY() /ammo.scale);

            if (y<ammo.getY() ){
                if (ammo.getVelocity()==0 || Utils.showArrow) {

                    ammo.setVelocity(0.4f);
                    Log.d("x y klikX klikY ", String.valueOf(ammo.getX()) + " " + String.valueOf(ammo.getY()) + " " + String.valueOf(x) + " " + String.valueOf(y));
                    ammo.setMovingVectorX(x < ammo.getX() ? x - ammo.getX() : x - ammo.getX());
                    ammo.setMovingVectorY(y < ammo.getY() ? y - ammo.getY() : y + ammo.getY());
                }
            }


            return true;
        }

        return false;
    }


    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);



        Utils.setOffSetY(gameList);
        Utils.drawLayers(canvas,gameList);

    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        gameList=Utils.createGrid(this);

        //create shooter
        Hexagon hex=new Hexagon(this,Color.BLUE,true);

        hex.setLayer(Utils.randInt(0,5));
        hex.setColor(Utils.hexColor(hex.getLayer()));
        hex.setLayer(1);

        gameList.add(hex);
        ammo=hex;
        ammo.shooter=true;


        this.gameThread = new GameThread(this,holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry= true;
        while(retry) {
            try {
                this.gameThread.setRunning(false);

                // Parent thread must wait until the end of GameThread.
                this.gameThread.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry= true;
        }
    }

}