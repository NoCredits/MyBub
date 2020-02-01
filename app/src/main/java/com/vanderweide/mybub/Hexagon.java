package com.vanderweide.mybub;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public  class Hexagon extends GameObject {

    private static final long serialVersionUID = 1L;

    public static final int SIDES = 6;

    private Point[] points = new Point[SIDES];
    private Point center;
    private int rotation = 90;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public Hexagon(GameSurface gameSurface, int radius, int x, int y, int color) {
        super(gameSurface,color,radius,x,y);

        this.center=new Point(x,y);
//        this.paint.setStyle(Paint.Style.FILL);
        //this.paint.setStyle(Paint.Style.STROKE);
        updatePoints();
    }


    public void setRadius(int radius) {
        this.radius = radius;

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

    public void draw(Canvas canvas){
        // Store before changing.
        Paint paint=new Paint();
        paint.setColor(getColor());
        paint.setStyle(this.paint.getStyle());

        // path
        Path polyPath = new Path();
        polyPath.moveTo(points[0].x, points[0].y);
        int i, len;
        len = points.length;
        for (i = 0; i < len; i++) {
            polyPath.lineTo(points[i].x, points[i].y);
        }
        polyPath.lineTo(points[0].x, points[0].y);

        // draw
        canvas.drawPath(polyPath, paint);
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

        // Distance moves
        float distance = velocity * deltaTime;

        double movingVectorLength = Math.sqrt(movingVectorX* movingVectorX + movingVectorY*movingVectorY);

        // Calculate the new position of the game character.
        this.x = x +  (int)(distance* movingVectorX / movingVectorLength);
        this.y = y +  (int)(distance* movingVectorY / movingVectorLength);

        // When the game's character touches the edge of the screen, then change direction

        if(this.x < this.getRadius() )  {
            this.x = this.getRadius();
            this.movingVectorX = - this.movingVectorX;
        } else if(this.x > this.gameSurface.getWidth() -this.getRadius())  {
            this.x= this.gameSurface.getWidth()-this.getRadius();
            this.movingVectorX = - this.movingVectorX;
        }

        if(this.y < this.getRadius() )  {
            this.y = this.getRadius();
            this.movingVectorY = - this.movingVectorY;
        } else if(this.y > this.gameSurface.getHeight()- this.getRadius())  {
            this.y= this.gameSurface.getHeight()- this.getRadius();
            this.movingVectorY = - this.movingVectorY ;
        }

        setRotation(getRotation()+1);
        this.center=new Point(this.x,this.y);
        updatePoints();
    }

}