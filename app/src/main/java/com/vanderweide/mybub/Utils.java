package com.vanderweide.mybub;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    public static float offsetY=15.6f;

    public static Random rand=new Random();

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
    }


    public static void calcPos(GameObject hero,GameObject foe) {

        if (hero.getX() > foe.getX()) {//rechts
            if (hero.getY() > foe.getY() + foe.getRadius() / 2) { //onder
                hero.gridPosX = foe.gridPosX + 1;
                hero.gridPosY = foe.gridPosY + 1;
                if (hero.gridPosY%2!=0) hero.gridPosX--;
            } else if (hero.getY() < foe.getY() - foe.getRadius() / 2) { //boven
                hero.gridPosX = foe.gridPosX + 1;
                hero.gridPosY = foe.gridPosY - 1;
                if (hero.gridPosY%2!=0) hero.gridPosX--;
            } else { //midden
                hero.gridPosX = foe.gridPosX + 1;
                hero.gridPosY = foe.gridPosY;
            }
        } else {  //links
            if (hero.getY() > foe.getY() + foe.getRadius() / 2) { //onder
                hero.gridPosX = foe.gridPosX - 1;
                hero.gridPosY = foe.gridPosY + 1;
                if (hero.gridPosY%2==0) hero.gridPosX++;
            } else if (hero.getY() < foe.getY() - foe.getRadius() / 2) { //boven
                hero.gridPosX = foe.gridPosX - 1;
                hero.gridPosY = foe.gridPosY - 1;
                if (hero.gridPosY%2==0) hero.gridPosX++;
            } else { //midden
                hero.gridPosX = foe.gridPosX - 1;
                hero.gridPosY = foe.gridPosY;
            }
        }
    }


    public static GameObject collide(GameObject hero, List<GameObject> gameObjectList){
        GameObject collidedFoe=null;

                for (GameObject foe:getLayerlist(hero.getLayer(),gameObjectList)){
                    if (hero!=foe && hero.isRendered() && foe.isRendered()) {
                        //  Log.i("collide hero layer",String.valueOf(hero.getLayer()));
                        // Log.i("collide",String.valueOf(Math.abs(hero.getX()-foe.getX())-foe.getRadius()));
//                        if (hero.x + hero.radius + foe.radius > foe.x
                        //                               && hero.x < foe.x + hero.radius + foe.radius
                        //                              && hero.y + hero.radius + foe.radius > foe.y
                        //                             && hero.y < foe.y + hero.radius + foe.radius)
                        //                    {
                        double distance = Math.sqrt(
                                ((hero.x - foe.x) * (hero.x - foe.x))
                                        + ((hero.y - foe.y) * (hero.y - foe.y)));
                        if (distance < hero.radius + foe.radius) {
                            collidedFoe = foe;
                            calcPos(hero,foe);
                           return collidedFoe; //balls have collided
                        }
                    }
                }
        return collidedFoe;
    }

    public static int randInt(int min, int max) {

        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static int hexColor(int index) {
        int color=Color.WHITE;
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
}
