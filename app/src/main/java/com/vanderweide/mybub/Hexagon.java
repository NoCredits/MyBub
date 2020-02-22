package com.vanderweide.mybub;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.List;

public  class Hexagon extends GameObject {

    private static final long serialVersionUID = 1L;

    public static final int SIDES = 6;

    private Point[] points = new Point[SIDES];
    private Point center;
    private int rotation = 90;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }



    public Hexagon(GameSurface gameSurface, float radius, int x, int y, int color) {
        super(gameSurface,color,radius,x,y);

        this.center=new Point(x,y);
//        this.paint.setStyle(Paint.Style.FILL);
        //this.paint.setStyle(Paint.Style.STROKE);
        updatePoints();
    }

    public Hexagon(GameSurface gameSurface, int color) {
        //super(gameSurface,color,,0,0);
        super(gameSurface,color,(320/(Utils.cols-1))/1.95f,1,1);
        this.center=new Point(1,1);
//        this.paint.setStyle(Paint.Style.FILL);
        //this.paint.setStyle(Paint.Style.STROKE);
        updatePoints();
    }

    public Hexagon(GameSurface gameSurface, int color, boolean shooter) {
        super(gameSurface,color,(320/(Utils.cols-1))/1.95f,1,1);

        if (shooter){
            this.x=(int)(this.screenWidth /2  + radius );
            this.y=(int)(this.screenHeight );

        } else {

        }
        this.center=new Point(this.x,this.y);
        updatePoints();
    }


    public void setRadius(int radius) {
        this.radius =radius;

        updatePoints();
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;

        updatePoints();
    }



    public void setCenter(Point center) {
        this.center = center;

        updatePoints();
    }

    //alles weg ssss``

    public void setCenter(int x, int y) {
        setCenter(new Point(x, y));
    }

    private double findAngle(double fraction) {
        return fraction * Math.PI * 2 + Math.toRadians((rotation + 180) % 360);
    }

    private Point findPoint(double angle) {
        int x = (int) (center.x + Math.cos(angle) * radius);
        int y = (int) (center.y + Math.sin(angle) * radius);

        return new Point(x, y);
    }

    protected void updatePoints() {
        for (int p = 0; p < SIDES; p++) {
            double angle = findAngle((double) p / SIDES);
            Point point = findPoint(angle);
            points[p] = point;
        }
    }

    public void calcPos(GameObject foe) {

        if (this.getX() > foe.getX()) {//rechts
            if (this.getY() > foe.getY() -Utils.offsetY + foe.getRadius() / 2) { //onder
                this.gridPosX = foe.gridPosX + 1;
                this.gridPosY = foe.gridPosY + 1;
                if (this.gridPosY%2!=0) this.gridPosX--;
            } else if (this.getY() < foe.getY() -Utils.offsetY - foe.getRadius() / 2) { //boven
                this.gridPosX = foe.gridPosX + 1;
                this.gridPosY = foe.gridPosY - 1;
                if (this.gridPosY%2!=0) this.gridPosX--;
            } else { //midden
                this.gridPosX = foe.gridPosX + 1;
                this.gridPosY = foe.gridPosY;
            }
        } else {  //links
            if (this.getY() > foe.getY() -Utils.offsetY+ foe.getRadius() / 2) { //onder
                this.gridPosX = foe.gridPosX - 1;
                this.gridPosY = foe.gridPosY + 1;
                if (this.gridPosY%2==0) this.gridPosX++;
            } else if (this.getY() < foe.getY() -Utils.offsetY- foe.getRadius() / 2) { //boven
                this.gridPosX = foe.gridPosX - 1;
                this.gridPosY = foe.gridPosY - 1;
                if (this.gridPosY%2==0) this.gridPosX++;
            } else { //midden
                this.gridPosX = foe.gridPosX - 1;
                this.gridPosY = foe.gridPosY;
            }
        }
    }


    public GameObject collide(List<GameObject> gameObjectList){
        GameObject collidedFoe=null;

        for (GameObject foe:Utils.getLayerlist(this.getLayer(),gameObjectList)){
            if (this!=foe && this.isRendered() && foe.isRendered() && foe.collidable) {

                double distance = Math.sqrt(
                        ((this.x - foe.x) * (this.x - foe.x))
                                + ((this.y - foe.y +Utils.offsetY) * (this.y - foe.y+Utils.offsetY)));
                if (distance < this.radius + foe.radius -5) {
                    collidedFoe = foe;
                    calcPos(foe);
                    return collidedFoe; //balls have collided
                }
            }
        }
        return collidedFoe;
    }

    private void draw_connectors_BAK(Canvas canvas){
        Paint paint=new Paint();
        paint.setColor(getColor());
        paint.setStyle(this.paint.getStyle());
        canvas.drawCircle(getX()*scale,getY()*scale -(Utils.offsetY*scale), getRadius()*scale*.8f, paint);
        if (gridPosY%2==0) {
            if (gridPosX%3==0){
                canvas.drawLine(getX()*scale,getY()*scale-(Utils.offsetY*scale),getX()*scale+140,getY()*scale+140-(Utils.offsetY*scale),paint);
                canvas.drawLine(getX()*scale,getY()*scale-(Utils.offsetY*scale),getX()*scale-140,getY()*scale-(Utils.offsetY*scale),paint);
                canvas.drawLine(getX()*scale,getY()*scale-(Utils.offsetY*scale),getX()*scale+140,getY()*scale-140-(Utils.offsetY*scale),paint);

            }
        } else {
            //canvas.drawLine(getX()*scale,getY()*scale-(Utils.offsetY*scale),getX()*scale+140,getY()*scale+140-(Utils.offsetY*scale),paint);

        }
    }



    public void draw(Canvas canvas){
        // Store before changing.
        Paint paint=new Paint();
        paint.setColor(getColor());
        paint.setStyle(this.paint.getStyle());

        Paint paintLine=new Paint();
        paintLine.setColor(Color.DKGRAY);
        paintLine.setStrokeWidth(8);
        paintLine.setStyle(Paint.Style.STROKE);

        // path
        Path polyPath = new Path();
        polyPath.moveTo(points[0].x, points[0].y);
        int i, len;
        len = points.length;
        for (i = 0; i < len; i++) {
            polyPath.lineTo(points[i].x, points[i].y);
        }
        polyPath.lineTo(points[0].x, points[0].y);

      //  canvas.drawLine(0,0,100,100,paint);
        // draw
        //canvas.drawPath(polyPath, paint);
        if (rendered &&  !shooter){
//            canvas.drawCircle(getX()*scale,getY()*scale -(Utils.offsetY*scale), getRadius()*scale, paint);
            canvas.drawCircle(getDX(),getDY(), getRadius()*scale*.7f, paint);



            if (!shouldDrop){
                if (NE!=null) {
                    float dy=((NE.getY()-getY()));
                    float dx=((NE.getX()-getX()));
                    if (NE.color==color) paintLine.setColor(color);
                    else paintLine.setColor(Color.DKGRAY);
                   // canvas.drawLine(getDX(),getDY(),NE.getDX(),NE.getDY(),paintLine);
                    canvas.drawLine(getDX()+dx,getDY()+dy,NE.getDX()-dx,NE.getDY()-dy,paintLine);
                }
                if (E!=null)  {
                    float dy=((E.getY()-getY()));
                    float dx=((E.getX()-getX()));
                    if (E.color==color) paintLine.setColor(color);
                    else paintLine.setColor(Color.DKGRAY);
                    canvas.drawLine(getDX()+dx,getDY()+dy,E.getDX()-dx,E.getDY()-dy,paintLine);
                }
                if (SE!=null)  {
                    float dy=((SE.getY()-getY()));
                    float dx=((SE.getX()-getX()));
                    if (SE.color==color) paintLine.setColor(color);
                    else paintLine.setColor(Color.DKGRAY);
                    canvas.drawLine(getDX()+dx,getDY()+dy,SE.getDX()-dx,SE.getDY()-dy,paintLine);
                }
                if (NW!=null)  {
                    float dy=((NW.getY()-getY()));
                    float dx=((NW.getX()-getX()));
                    if (NW.color==color) paintLine.setColor(color);
                    else paintLine.setColor(Color.DKGRAY);
                    canvas.drawLine(getDX()+dx,getDY()+dy,NW.getDX()-dx,NW.getDY()-dy,paintLine);
                }
                if (W!=null)  {
                    float dy=((W.getY()-getY()));
                    float dx=((W.getX()-getX()));
                    if (W.color==color) paintLine.setColor(color);
                    else paintLine.setColor(Color.DKGRAY);
                    canvas.drawLine(getDX()+dx,getDY()+dy,W.getDX()-dx,W.getDY()-dy,paintLine);
                }
                if (SW!=null)  {
                    float dy=((SW.getY()-getY()));
                    float dx=((SW.getX()-getX()));
                    if (SW.color==color) paintLine.setColor(color);
                    else paintLine.setColor(Color.DKGRAY);
                    canvas.drawLine(getDX()+dx,getDY()+dy,SW.getDX()-dx,SW.getDY()-dy,paintLine);
                }
            }


        }
        else if (rendered) canvas.drawCircle(getX()*scale,getY()*scale, getRadius()*scale*.8f, paint);
        this.lastDrawNanoTime= System.nanoTime();

    }

    public void update()  {

        // Current time in nanoseconds
        long now = System.nanoTime();

        if (lastDrawNanoTime==-1) {
            lastDrawNanoTime= now;
        }
        // Change nanoseconds to milliseconds (1 nanosecond = 1000000 milliseconds).
        int deltaTime = (int) ((now - lastDrawNanoTime)/ 1000000 );

        if (this.velocity>0){
            // Distance moves
            float distance = velocity * deltaTime;

            double movingVectorLength = Math.sqrt(movingVectorX* movingVectorX + movingVectorY*movingVectorY);

            // Calculate the new position of the game character.
            this.x = x +  (int)(distance* movingVectorX / movingVectorLength);
            this.y = y +  (int)(distance* movingVectorY / movingVectorLength);


            if(this.x < this.getRadius() +5)  { //-1 ? even als test. zat een bug in
                this.x = (int) this.getRadius()+5;
                this.movingVectorX = - this.movingVectorX;
            } else if(this.x > this.screenWidth +this.getRadius()*2 -10)  { //+1? bug tracking
                this.x= (int) (this.screenWidth + this.getRadius()*2 -10);
                this.movingVectorX = - this.movingVectorX;
            }

            if(this.y < this.getRadius() )  {
                this.y = (int) this.getRadius();
                this.movingVectorY = - this.movingVectorY;
            }
            else if(shooter && this.y > this.screenHeight- this.getRadius())  {
                this.y= (int) (this.screenHeight- this.getRadius());
               this.movingVectorY = - this.movingVectorY ;
            }

        }

        //setRotation(getRotation()+1);
        //setRotation(20);
        //this.y-=(Utils.offsetY);
        this.center=new Point(this.x,this.y);
        updatePoints();
    }

    /*public void calculateScreenPosToGrid(int padding){
        this.gridPosX=(int)((this.getX()+this.radius)/(this.getRadius()*2+padding));
        this.gridPosY=(int)((this.getY()+radius)/(this.getRadius()*2-(this.radius/4)));
        this.gridPosX--;
        this.gridPosY--;
        setCenter(this.x,this.y);
        updatePoints();
    }
*/

    public void calculateGridPosToScreen(int padding){
        //int r=(this.gridPosY - this.radius/2) * 3;

        this.x=(int)((this.gridPosX+1)*(this.radius*2+padding)-this.radius+1);
        this.y=(int)((this.gridPosY+1)*(this.radius*2+padding-this.radius/4)-this.radius/4);

       // this.x=(int)((this.gridPosX)*(this.radius*2+padding)+this.radius);
       // this.y=(int)((this.gridPosY)*(this.radius*2+padding)-this.radius*2);
        if ((this.gridPosY % 2)!= 0){
            this.x+=(radius)+padding;
        }


        setCenter(this.x,this.y);
        updatePoints();
    }


}