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

    private List<ChibiCharacter> chibiList = new ArrayList<ChibiCharacter>();
    private List<Explosion> explosionList = new ArrayList<Explosion>();
    private List<GameObject> gameList = new ArrayList<GameObject>();

    private int gridRows=11;
    private int gridCols=11;
    private GridObject grid=new GridObject(gridRows,gridRows);
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

        for (GameObject game:gameList) {
            //if (!zList.contains(game.getZ_index()))
            game.update();

        }
        GameObject foe=ammo.collide(gameList);
        if (foe!=null){ //collision with foe
            ammo.setVelocity(0);
            ammo.shooter=false;
           // ammo.calculateScreenPosToGrid(0);
            ammo.calculateGridPosToScreen(0);

            if (ammo.getColor()==foe.getColor()){
                remove(foe);
                remove(ammo);
            }

            Hexagon hex=new Hexagon(this,15.6f,320/2,(int)(480),Color.BLUE);
            hex.setLayer(Utils.randInt(0,5));
            hex.setColor(Utils.hexColor(hex.getLayer()));
            hex.setLayer(1);
            hex.shooter=true;
            gameList.add(hex);
            ammo=hex;
        }

        for(Explosion explosion: this.explosionList)  {
            explosion.update();
        }



        Iterator<Explosion> iterator= this.explosionList.iterator();
        while(iterator.hasNext())  {
            Explosion explosion = iterator.next();

            if(explosion.isFinish()) {
                // If explosion finish, Remove the current element from the iterator & list.
                iterator.remove();
                continue;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int x=  (int)(event.getX() /ammo.scaleX);
            int y = (int)(event.getY() /ammo.scaleY);

//                if( ammo.getX()-ammo.getRadius() < x && x < ammo.getX() + ammo.getRadius()
//                        && ammo.getY()-ammo.getRadius() < y && y < ammo.getY()+ ammo.getRadius())  {
//
//                }

            ammo.setVelocity(0.3f);
            Log.d("x y klikX klikY ",String.valueOf(ammo.getX())+" "+String.valueOf(ammo.getY())+" "+String.valueOf(x)+" "+String.valueOf(y));
            ammo.setMovingVectorX(x<ammo.getX()?x-ammo.getX():x-ammo.getX());
            ammo.setMovingVectorY(y<ammo.getY()?y-ammo.getY():y+ammo.getY());

            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);


        for(Explosion explosion: this.explosionList)  {
            explosion.draw(canvas);
        }

        Utils.setOffSetY(gameList);
        Utils.drawLayers(canvas,gameList);

    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //rand=new Random();
        Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.chibi1);
        ChibiCharacter chibi1 = new ChibiCharacter(this,chibiBitmap1,100,50);

        Bitmap chibiBitmap2 = BitmapFactory.decodeResource(this.getResources(),R.drawable.chibi2);
        ChibiCharacter chibi2 = new ChibiCharacter(this,chibiBitmap2,300,150);

        for (int r=0;r<gridRows;r++){
            for (int c=0;c<gridCols;c++) {

               // Hexagon hex=new Hexagon(this,30,Utils.randInt(25,this.getWidth()-25),Utils.randInt(25,this.getHeight()-25),Color.BLUE);
                Hexagon hex=new Hexagon(this,30,Utils.randInt(25,320-25),Utils.randInt(25,480-25),Color.BLUE);
               // hex.setMovingVector(Utils.randInt(-10,10),Utils.randInt(-10,10));
               hex.setMovingVector(0,0);
                //hex.setVelocity(Utils.rand.nextFloat()/3);
                //hex.setRadius((this.getWidth()/(gridCols+1)/2));
                //hex.setRadius(((320-10)/(gridCols-1)/2));
                hex.setRadius(15.6f);
                //hex.setRadius(Utils.randInt(30,70));
                hex.setZ_index(2);
                hex.setLayer(Utils.randInt(0,5));
                hex.setColor(Utils.hexColor(hex.getLayer()));
                hex.setLayer(1);
                int ran=Utils.randInt(0,3);
                    if (ran>2){
                        grid.add(r,c,hex);
                        gameList.add(hex);
                    }
                  // hex.calculateGridPosToScreen(grid.getPadding());
                hex.calculateGridPosToScreen(0);
            }
        }

        //create shooter
       // Hexagon hex=new Hexagon(this,(this.getWidth()/(gridCols+1)/2),this.getWidth()/2,this.getHeight()-50,Color.BLUE);
       // Hexagon hex=new Hexagon(this,15.6f,320/2,480-50,Color.BLUE);
        Hexagon hex=new Hexagon(this,15.6f,320,480,Color.BLUE);
        hex.setLayer(Utils.randInt(0,5));
        hex.setColor(Utils.hexColor(hex.getLayer()));
        hex.setLayer(1);
        gameList.add(hex);
        ammo=hex;

        chibi1.setVelocity(0.5f);
        chibi1.setMovingVector(Utils.randInt(-10,10),Utils.randInt(-10,10));
        chibi1.setLayer(3);
        chibi1.setZ_index(3);
       // gameList.add(chibi1);

        chibi2.setVelocity(Utils.rand.nextFloat());
        chibi2.setMovingVector(Utils.randInt(-10,10),Utils.randInt(-10,10));
        chibi2.setLayer(3);
        chibi2.setZ_index(3);
        //gameList.add(chibi2);


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