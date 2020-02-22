package com.vanderweide.mybub;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Utils {

    public static float offsetY=0f;

    public static Random rand=new Random();

    public static GameObject[][] grid;

    public static int cols=12;
    public static int rows=11;

    public static boolean falling;
    public static int ballsFalling=0;
    public static int arrowFromX=0;
    public static int arrowFromY=0;
    public static int arrowToX=0;
    public static int arrowToY=0;
    public static boolean showArrow;
    public static int score=0;

    private static SparseArray<ArrayList<GameObject>> addToArray(SparseArray<ArrayList<GameObject>>  arr,Integer key, GameObject myObject) {
        ArrayList<GameObject> itemsList = arr.get(key);

        // if list does not exist create it
        if(itemsList == null) {
            itemsList = new ArrayList<>();
            itemsList.add(myObject);
            arr.put(key, itemsList);
        } else {
            // add if item is not already in list
            if(!itemsList.contains(myObject)) itemsList.add(myObject);
        }
        return arr;
    }

    private static SparseArray<ArrayList<GameObject>> getLayerArray(List<GameObject> gameObjectList){
        SparseArray<ArrayList<GameObject>> layerArray=new SparseArray<>();
        for (GameObject gameObject:gameObjectList) {
            layerArray=Utils.addToArray(layerArray,gameObject.getLayer(),gameObject);
        }
        return layerArray;
    }

    public static ArrayList<GameObject>  getLayerlist(int layer, List<GameObject> gameObjectList){
        SparseArray<ArrayList<GameObject>> layerArray=getLayerArray(gameObjectList);
        return layerArray.get(layer);
    }

    public static void drawLayers(Canvas canvas, List<GameObject> gameObjectList ){
        SparseArray<ArrayList<GameObject>> layerArray=getLayerArray(gameObjectList);

        for(int i = 0; i < layerArray.size(); i++) {
            int key = layerArray.keyAt(i);
            ArrayList<GameObject> gameList = layerArray.get(key);

            //sort list on z-order
            SparseArray<ArrayList<GameObject>> zList=new SparseArray<>();
            for (GameObject game: gameList) {
                zList=Utils.addToArray(zList,game.z_index,game);
            }

            //loop zlist and draw
            for(int o = 0; o < zList.size(); o++) {
                int oKey = zList.keyAt(o);
                ArrayList<GameObject> zOrderedList = zList.get(oKey);
                for (GameObject game: zOrderedList) {
                    game.draw(canvas);
                  //  if (Utils.collide(game,gameList))   return;
                }
            }
        }

       if (showArrow){
           Paint paint=new Paint();
           paint.setColor(Color.WHITE);
           paint.setStyle(Paint.Style.STROKE);
           paint.setStrokeWidth(5);
           canvas.drawLine(arrowFromX,arrowFromY,arrowToX,arrowToY,paint);
       }

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(50);
        canvas.drawText("score: "+score, 0, 2000, paint);
    }

    public static void setOffSetY(List<GameObject> gameObjectList){
        int lowest=0; //laagste bal (hoogste Y)
        offsetY=00;
        for (GameObject game: gameObjectList) {
            if (game.inGrid){
                if (game.getGridPosY()>lowest) lowest=game.getGridPosY();
                if (lowest>11) offsetY=(float) (((lowest-11)+1)*(game.radius*2-game.radius/4)-game.radius);
            }
        }
        for (GameObject game: gameObjectList) {
            if (game.inGrid){
                game.offsetY=-offsetY;
            }
        }
    }

    public static int randInt(int min, int max) {

        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static int hexColor(int index) {
        int color;
        switch (index) {
            case 0:
                color= Color.GREEN;
            break;
            case 1:
                color= Color.RED;
            break;
            case 2:
                color= Color.BLUE;
            break;
            case 3:
                color= Color.LTGRAY;
            break;
            case 4:
                color= Color.MAGENTA;
            break;
            case 5:
                color= Color.YELLOW;
            break;
            default: color=Color.WHITE;
            break;
        }
        return color;
    }

    public static GameObject exists(int x, int y,List<GameObject> gameObjectList){
        GameObject exists=null;
        for (GameObject game: gameObjectList) {
            if (game.getGridPosX()==x && game.getGridPosY()==y) {
                exists=game;
                break;
            }
        }
        return exists;
    }

    private static void attachNeighbours( ){
        int rowCount=400;
        int columnCount=Utils.cols;
        for(int r=0; r < rowCount; r++) {
            for(int c=0; c < columnCount; c++) {
                GameObject gameObject=grid[r][c];
                if (gameObject!=null){
                    if (r%2==0 ){ //even rij
                        gameObject.NE=neighbour(c,r-1); //rechtsboven
                        gameObject.E=neighbour(c+1,r); //rechts
                        gameObject.SE=neighbour(c,r+1);//rechtsonder
                        gameObject.SW=neighbour(c-1,r+1); //linksonder
                        gameObject.W=neighbour(c-1,r); //links
                        gameObject.NW=neighbour(c-1,r-1); //linksboven

                    } else { //oneven rij
                        gameObject.NE=neighbour(c+1,r-1); //rechtsboven
                        gameObject.E=neighbour(c+1,r); //rechts
                        gameObject.SE=neighbour(c+1,r+1);//rechtsonder
                        gameObject.SW=neighbour(c,r+1); //linksonder
                        gameObject.W=neighbour(c-1,r); //links
                        gameObject.NW=neighbour(c,r-1); //linksboven
                    }
                }
            }
        }
    }

    private static GameObject neighbour(int column,int row){
        if (column<0 || row<0 || column>Utils.cols-1 || row>400) return null;
        return grid[row][column];
    }

    public static void createArrayList(List<GameObject> gameObjectList){
        falling=false;
        ballsFalling=0;
        int rowCount=400;
        int columnCount=Utils.cols;
        grid = new GameObject[400][Utils.cols];
        for(int r=0; r < rowCount; r++) {
            for(int c=0; c < columnCount; c++) {
                grid[r][c]=null;
            }
        }
        for (GameObject gameObject:gameObjectList){
            if (gameObject.inGrid){
                gameObject.checked=false;
                gameObject.shouldDrop=false;
                grid[gameObject.getGridPosY()][gameObject.getGridPosX()]=gameObject;
            }
        }
        attachNeighbours();
    }

    public static void newDrop(List<GameObject> gameObjectList){
        int columnCount=Utils.cols;
        createArrayList(gameObjectList);

        for(int c=0; c < columnCount; c++) {
           checkNext(c,0);
        }
        Iterator<GameObject> iterator= gameObjectList.iterator();

        while(iterator.hasNext()) {
            GameObject game = iterator.next();
            if (!game.shooter) {
                if (!game.checked) { //vallen maar
                   game.inGrid=false;
                   game.collidable=false;
                   game.remove=true;
                   game.setMovingVectorY(2000);
                   game.setVelocity(0.8f);
                   //iterator.remove();
                }
            }
        }


    }

    private static void checkNext(int c,int r){

        if (c<0 || r<0 || c>Utils.cols-1 || r>400) return;

        if (grid[r][c] !=null)  {
            if (!grid[r][c].checked){
                grid[r][c].checked=true;
            }
            else return;
        } else return;

        if (r%2==0 ){ //even rij
            checkNext(c,r-1); //rechtsboven
            checkNext(c+1,r); //rechts
            checkNext(c,r+1);//rechtsonder
            checkNext(c-1,r+1); //linksonder
            checkNext(c-1,r); //links
            checkNext(c-1,r-1); //linksboven

        } else { //oneven rij
            checkNext(c+1,r-1); //rechtsboven
            checkNext(c+1,r); //rechts
            checkNext(c+1,r+1);//rechtsonder
            checkNext(c,r+1); //linksonder
            checkNext(c-1,r); //links
            checkNext(c,r-1); //linksboven
        }
    }

    public static void checkNextCollide(int c,int r,int color){

        if (c<0 || r<0 || c>Utils.cols-1 || r>400) return;

        //Log.i("c",String.valueOf(c));
        if (grid[r][c] !=null)  {
            if (grid[r][c].color==color){
                if (!grid[r][c].checked){
                    grid[r][c].checked=true;
                    if (!grid[r][c].shooter) {
                        grid[r][c].shouldDrop = true;
                        ballsFalling++;
                    }
                }
                else return;
            } else return;
        } else return;

        if (r%2==0 ){ //even rij
            checkNextCollide(c,r-1,color); //rechtsboven
            checkNextCollide(c+1,r,color); //rechts
            checkNextCollide(c,r+1,color);//rechtsonder
            checkNextCollide(c-1,r+1,color); //linksonder
            checkNextCollide(c-1,r,color); //links
            checkNextCollide(c-1,r-1,color); //linksboven

        } else { //oneven rij
            checkNextCollide(c+1,r-1,color); //rechtsboven
            checkNextCollide(c+1,r,color); //rechts
            checkNextCollide(c+1,r+1,color);//rechtsonder
            checkNextCollide(c,r+1,color); //linksonder
            checkNextCollide(c-1,r,color); //links
            checkNextCollide(c,r-1,color); //linksboven
        }
    }

    public static List<GameObject> createGrid(GameSurface gameSurface){
        List<GameObject> gameList=new ArrayList<>();
        score=0;
        int gridRows=rows;
        int gridCols=Utils.cols;
        for (int r=0;r<gridRows;r++){
            for (int c=0;c<gridCols;c++) {

                Hexagon hex=new Hexagon(gameSurface,Color.BLUE);
                hex.setZ_index(2);
                hex.setColor(Utils.hexColor(Utils.randInt(0,4)));
                hex.setLayer(1);
                int ran=Utils.randInt(0,3);
                if (ran>0){
                    hex.setGridPosX(c);
                     hex.setGridPosY(r);
                    hex.setInGrid(true);
                    hex.collidable=true;
                    hex.calculateGridPosToScreen(0);

                //    if ((r%2!=0 && c%2==0) || (r%2==0 ) )
                    if ((r%2!=0 && (c!=2 && c!=5 && c!=8 && c!=11 ))
                            || (r%2==0 ) && (c!=1 && c!=4 && c!=7 && c!=10) )
                        gameList.add(hex);
                }
            }
        }
        return gameList;

    }


}
