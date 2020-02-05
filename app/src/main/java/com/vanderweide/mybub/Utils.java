package com.vanderweide.mybub;

import android.graphics.Canvas;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

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
                    if (Utils.collide(game,gameList))   return;
                }
            }
        }
    }

    public static boolean collide(GameObject hero, List<GameObject> gameObjectList){
        boolean collisionDetected = false;

                for (GameObject foe:getLayerlist(hero.getLayer(),gameObjectList)){
                    if (hero!=foe && hero.isRendered() && foe.isRendered()){
                      //  Log.i("collide hero layer",String.valueOf(hero.getLayer()));
                       // Log.i("collide",String.valueOf(Math.abs(hero.getX()-foe.getX())-foe.getRadius()));
                        if ((Math.abs(hero.getX()-foe.getX())-foe.getRadius()*2<0)
                                && (Math.abs(hero.getY()-foe.getY())-foe.getRadius()*2<0) ) {
                            // we have a basic hit
                            Log.i("collide","true");
                            hero.setMovingVector(-hero.getMovingVectorX(),-hero.getMovingVectorY());
                            //hero.setMovingVector(foe.getMovingVectorX(),foe.getMovingVectorY());
                            //hero.setVelocity(foe.getVelocity());
                        }
                    }
                }
        return collisionDetected;
    }

    public static int randInt(int min, int max) {

        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}
