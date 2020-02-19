package com.vanderweide.mybub;

public class Point {
    public float x = 0;
    public float y = 0;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


}
