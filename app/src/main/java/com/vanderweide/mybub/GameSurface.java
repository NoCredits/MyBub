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


        Utils.newDrop(gameList);

        Iterator<GameObject> iterator= gameList.iterator();

        while(iterator.hasNext()) {
            GameObject game = iterator.next();
            game.update();
            if (game.remove && game.getY()+Utils.offsetY>1000){
                iterator.remove();
            }
        }

        for (GameObject game:gameList) {
            //if (!zList.contains(game.getZ_index()))

        }
        GameObject foe=ammo.collide(gameList);
        if (foe!=null){ //collision with foe

            //ammo.calculateScreenPosToGrid(0);
            ammo.inGrid=true;
            ammo.calculateScreenPosToGrid(0);
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
            //Utils.checkNextCollide(x,y,color);
            ammo.shooter=false;
            ammo.collidable=true;
            ammo.calculateGridPosToScreen(0);

                if (Utils.falling){
                    ammo.remove=true;
                    ammo.inGrid = false;
                    ammo.collidable = false;
                    ammo.setMovingVectorY((int) (1000 + Utils.offsetY));
                    ammo.setVelocity(0.5f);

                }

           // ammo.calculateScreenPosToGrid(0);



/*            if (ammo.getColor()==foe.getColor()){
               // remove(foe);
               //remove(ammo);
                foe.inGrid=false;
                foe.collidable=false;
                foe.remove=true;
                foe.setMovingVectorY((int)(400+Utils.offsetY));
                foe.setVelocity(0.5f);
                ammo.inGrid=false;
                ammo.collidable=false;
                ammo.remove=true;
                ammo.setMovingVectorY((int)(400+Utils.offsetY));
                ammo.setVelocity(0.5f);
            }
*/
            //Hexagon hex=new Hexagon(this,15.6f,320/2,(int)(480),Color.BLUE);
           // Hexagon hex=new Hexagon(this,(320/11)/2,320/2,(int)(480),Color.BLUE);
            Hexagon hex=new Hexagon(this,Color.BLUE,true);
            hex.setLayer(Utils.randInt(0,5));
            hex.setColor(Utils.hexColor(hex.getLayer()));
            hex.setLayer(1);
            hex.shooter=true;
            gameList.add(hex);
            ammo=hex;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int x=  (int)(event.getX() /ammo.scale);
            int y = (int)(event.getY() /ammo.scale);

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



        Utils.setOffSetY(gameList);
        Utils.drawLayers(canvas,gameList);

    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {


        float radi=(320/11)/2;
        for (int r=0;r<gridRows;r++){
            for (int c=0;c<gridCols;c++) {

               // Hexagon hex=new Hexagon(this,30,Utils.randInt(25,this.getWidth()-25),Utils.randInt(25,this.getHeight()-25),Color.BLUE);
                //Hexagon hex=new Hexagon(this,30,Utils.randInt(25,320-25),Utils.randInt(25,480-25),Color.BLUE);
               // hex.setMovingVector(Utils.randInt(-10,10),Utils.randInt(-10,10));

               Hexagon hex=new Hexagon(this,Color.BLUE);
               //hex.setMovingVector(0,0);
                //hex.setVelocity(Utils.rand.nextFloat()/3);
                //hex.setRadius((this.getWidth()/(gridCols+1)/2));
                //hex.setRadius(((320-10)/(gridCols-1)/2));
                //hex.setRadius(15.6f);
                //hex.setRadius(Utils.randInt(30,70));
//                hex.setRadius(radi);
                hex.setZ_index(2);
                hex.setColor(Utils.hexColor(Utils.randInt(0,5)));
                hex.setLayer(1);
                int ran=Utils.randInt(0,3);
                    if (ran>1){
                        hex.setGridPosX(r);
                        hex.setGridPosY(c);
                        hex.setInGrid(true);
                        hex.collidable=true;
                        hex.calculateGridPosToScreen(0);

                       // grid.add(r,c,hex);
                        gameList.add(hex);
                    }
                  // hex.calculateGridPosToScreen(grid.getPadding());
            }
        }

        //create shooter
       // Hexagon hex=new Hexagon(this,(this.getWidth()/(gridCols+1)/2),this.getWidth()/2,this.getHeight()-50,Color.BLUE);
       // Hexagon hex=new Hexagon(this,15.6f,320/2,480-50,Color.BLUE);
//        Hexagon hex=new Hexagon(this,(320/11)/2,320,480,Color.BLUE);
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