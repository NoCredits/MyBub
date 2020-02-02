package com.vanderweide.mybub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;

    private final List<ChibiCharacter> chibiList = new ArrayList<ChibiCharacter>();
    private final List<Explosion> explosionList = new ArrayList<Explosion>();
    private final List<GameObject> gameList = new ArrayList<GameObject>();
    private final List<Integer> zList = new ArrayList<Integer>();
    private final int gridRows=11;
    private final int gridCols=11;
    private final GridObject grid=new GridObject(gridRows,gridRows);


    private static Random rand;


    public GameSurface(Context context)  {
        super(context);

        // Make Game Surface focusable so it can handle events.
        this.setFocusable(true);

        // Sét callback.
        this.getHolder().addCallback(this);
    }

    public void update()  {
        for(ChibiCharacter chibi: chibiList) {
            chibi.update();
        }
        for(Explosion explosion: this.explosionList)  {
            explosion.update();
        }

        for (GameObject game:gameList) {
            //if (!zList.contains(game.getZ_index()))
            game.update();
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

    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        //
        // In particular, do NOT do 'Random rand = new Random()' here or you
        // will get not very good / not very random results.
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int x=  (int)event.getX();
            int y = (int)event.getY();

            Iterator<ChibiCharacter> iterator= this.chibiList.iterator();

            while(iterator.hasNext()) {
                ChibiCharacter chibi = iterator.next();
                if( chibi.getX() < x && x < chibi.getX() + chibi.getWidth()
                        && chibi.getY() < y && y < chibi.getY()+ chibi.getHeight())  {
                    // Remove the current element from the iterator and the list.
                    iterator.remove();

                    // Create Explosion object.
                    Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.explosion);
                    Explosion explosion = new Explosion(this, bitmap,chibi.getX(),chibi.getY());

                    this.explosionList.add(explosion);
                }
            }


            for(ChibiCharacter chibi: chibiList) {
                int movingVectorX =x-  chibi.getX() ;
                int movingVectorY =y-  chibi.getY() ;
                chibi.setMovingVector(movingVectorX, movingVectorY);
            }
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);


        for(ChibiCharacter chibi: chibiList)  {
            chibi.draw(canvas);
        }

        for(Explosion explosion: this.explosionList)  {
            explosion.draw(canvas);
        }

        for (GameObject game:gameList) {
            game.draw(canvas);
        }

    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        rand=new Random();
        Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.chibi1);
        ChibiCharacter chibi1 = new ChibiCharacter(this,chibiBitmap1,100,50);

        Bitmap chibiBitmap2 = BitmapFactory.decodeResource(this.getResources(),R.drawable.chibi2);
        ChibiCharacter chibi2 = new ChibiCharacter(this,chibiBitmap2,300,150);
        this.chibiList.add(chibi1);
        this.chibiList.add(chibi2);

        this.getWidth();
        /*
        for (int i=0;i<20;i++){
            //this.bolList.add(new Bol(Color.RED,25,randInt(0,500),randInt(0,500)));
            Hexagon hex=new Hexagon(this,30,randInt(25,this.getWidth()-25),randInt(25,this.getHeight()-25),Color.BLUE);
            hex.setMovingVector(randInt(-10,10),randInt(-10,10));
//            hex.setColor(Color.rgb(randInt(0,255),randInt(0,255),randInt(0,255)));
            hex.setRadius(randInt(5,50));
            hex.setVelocity(rand.nextFloat());
            hex.setZ_index(randInt(0,4));
            switch (hex.getZ_index()){
                case 0: hex.setColor(Color.GREEN);
                    break;
                case 1: hex.setColor(Color.RED);
                break;
                case 2: hex.setColor(Color.BLUE);
                    break;
                case 3: hex.setColor(Color.WHITE);
                    break;
                case 4: hex.setColor(Color.YELLOW);
                    break;
            }

            Bol bol=new Bol(this,Color.RED,25,randInt(25,this.getWidth()-25),randInt(25,this.getHeight()-25));
            bol.setMovingVector(randInt(-10,10),randInt(-10,10));
//            bol.setColor(Color.rgb(randInt(0,255),randInt(0,255),randInt(0,255)));
            bol.setRadius(randInt(5,50));
            bol.setVelocity(rand.nextFloat());
            bol.setZ_index(randInt(0,4));
            switch (bol.getZ_index()){
                case 0: bol.setColor(Color.GREEN);
                    break;
                case 1: bol.setColor(Color.RED);
                    break;
                case 2: bol.setColor(Color.BLUE);
                    break;
                case 3: bol.setColor(Color.WHITE);
                    break;
                case 4: bol.setColor(Color.YELLOW);
                    break;
            }
            this.gameList.add(hex);
            this.gameList.add(bol);+
        }
        */


        for (int r=0;r<gridRows;r++){
            for (int c=0;c<gridCols;c++) {

                Hexagon hex=new Hexagon(this,30,randInt(25,this.getWidth()-25),randInt(25,this.getHeight()-25),Color.BLUE);
                //hex.setMovingVector(randInt(-10,10),randInt(-10,10));
                hex.setMovingVector(0,0);
//            hex.setColor(Color.rgb(randInt(0,255),randInt(0,255),randInt(0,255)));


                hex.setRadius((this.getWidth()/(gridCols+1)/2));
                hex.setVelocity(0);
                hex.setZ_index(randInt(0,4));
                switch (hex.getZ_index()){
                    case 0: hex.setColor(Color.GREEN);
                        break;
                    case 1: hex.setColor(Color.RED);
                        break;
                    case 2: hex.setColor(Color.BLUE);
                        break;
                    case 3: hex.setColor(Color.WHITE);
                        break;
                    case 4: hex.setColor(Color.YELLOW);
                        break;
                }
               if (randInt(0,2)>0){
                   gameList.add(hex);
                   grid.add(r,c,hex);
                   hex.calculateGridPosToScreen(grid.getPadding());

               }
            }
        }



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