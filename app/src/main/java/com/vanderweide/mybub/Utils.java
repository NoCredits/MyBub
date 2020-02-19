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

    public static boolean falling;
    public static int ballsFalling=0;
    public static int arrowFromX=0;
    public static int arrowFromY=0;
    public static int arrowToX=0;
    public static int arrowToY=0;
    public static boolean showArrow;

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
           paint.setStyle(Paint.Style.FILL);
           canvas.drawLine(arrowFromX,arrowFromY,arrowToX,arrowToY,paint);
       }
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
                color= Color.WHITE;
            break;
            case 4:
                color= Color.YELLOW;
            break;
            case 5:
                color= Color.MAGENTA;
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

    public static void createArrayList(List<GameObject> gameObjectList){
        falling=false;
        ballsFalling=0;
        int rowCount=400;
        int columnCount=11;
        grid = new GameObject[400][11];
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
    }

    public static void newDrop(List<GameObject> gameObjectList){
        int columnCount=11;
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
                   game.setMovingVectorY(5000);
                   game.setVelocity(0.4f);
                   //iterator.remove();
                }
            }
        }


    }

    private static void checkNext(int x,int y){

        if (x<0 || y<0 || x>10 || y>400) return;

        if (grid[y][x] !=null)  {
            if (!grid[y][x].checked){
                grid[y][x].checked=true;
            }
            else return;
        } else return;

        if (y%2==0 ){ //even rij
            checkNext(x,y-1); //rechtsboven
            checkNext(x+1,y); //rechts
            checkNext(x,y+1);//rechtsonder
            checkNext(x-1,y+1); //linksonder
            checkNext(x-1,y); //links
            checkNext(x-1,y-1); //linksboven

        } else { //oneven rij
            checkNext(x+1,y-1); //rechtsboven
            checkNext(x+1,y); //rechts
            checkNext(x+1,y+1);//rechtsonder
            checkNext(x,y+1); //linksonder
            checkNext(x-1,y); //links
            checkNext(x,y-1); //linksboven
        }
    }

    public static void checkNextCollide(int x,int y,int color){

        if (x<0 || y<0 || x>10 || y>400) return;

        //Log.i("x",String.valueOf(x));
        if (grid[y][x] !=null)  {
            if (grid[y][x].color==color){
                if (!grid[y][x].checked){
                    grid[y][x].checked=true;
                    if (!grid[y][x].shooter) {
                        grid[y][x].shouldDrop = true;
                        ballsFalling++;
                    }
                }
                else return;
            } else return;
        } else return;

        if (y%2==0 ){ //even rij
            checkNextCollide(x,y-1,color); //rechtsboven
            checkNextCollide(x+1,y,color); //rechts
            checkNextCollide(x,y+1,color);//rechtsonder
            checkNextCollide(x-1,y+1,color); //linksonder
            checkNextCollide(x-1,y,color); //links
            checkNextCollide(x-1,y-1,color); //linksboven

        } else { //oneven rij
            checkNextCollide(x+1,y-1,color); //rechtsboven
            checkNextCollide(x+1,y,color); //rechts
            checkNextCollide(x+1,y+1,color);//rechtsonder
            checkNextCollide(x,y+1,color); //linksonder
            checkNextCollide(x-1,y,color); //links
            checkNextCollide(x,y-1,color); //linksboven
        }
    }

    public static List<GameObject> createGrid(GameSurface gameSurface){
        List<GameObject> gameList=new ArrayList<>();
        int gridRows=11;
        int gridCols=11;
        for (int r=0;r<gridRows;r++){
            for (int c=0;c<gridCols;c++) {

                Hexagon hex=new Hexagon(gameSurface,Color.BLUE);
                hex.setZ_index(2);
                hex.setColor(Utils.hexColor(Utils.randInt(0,5)));
                hex.setLayer(1);
                int ran=Utils.randInt(0,3);
                if (ran>0){
                    hex.setGridPosX(r);
                    hex.setGridPosY(c);
                    hex.setInGrid(true);
                    hex.collidable=true;
                    hex.calculateGridPosToScreen(0);

                    gameList.add(hex);
                }
            }
        }
        return gameList;

    }

}
