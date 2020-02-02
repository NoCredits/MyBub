package com.vanderweide.mybub;

import android.graphics.Canvas;

public class GridObject {

    GameObject[][] pos;
    private int rows;
    private int cols;

    private int padding;

    public GridObject(int rows, int cols){
        this.padding=0;
        this.pos=new GameObject[rows][cols];
    }


    public GameObject[][] getPos() {
        return this.pos;
    }

    public void setPos(GameObject[][] pos) {
        this.pos = pos;
    }

    public void add(int row, int col, GameObject gameObject){
        gameObject.setGridPosX(row);
        gameObject.setGridPosY(col);
        gameObject.setInGrid(true);
        this.pos[row][col]=gameObject;
    }

    public void remove(int row, int col){
        this.pos[row][col].setInGrid(false);
        this.pos[row][col]=null;
    }

    public void move(int fromRow, int fromCol, int toRow, int toCol, GameObject gameObject){
        remove(fromRow,fromCol);
        add(toRow,toCol,gameObject);
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return this.cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }


    public void draw(Canvas canvas){ //gaan we niet gebruiken normaliter
        for (int r=0; r<this.rows;r++){
            for (int c=0; c<this.cols;c++){
                if(this.pos[r][c]!=null){
                    this.pos[r][c].draw(canvas);
                }
            }
        }
    }

    public void calculateAllPosToScreen(){
        for (int r=0; r<this.rows;r++){
            for (int c=0; c<this.cols;c++){
                if(this.pos[r][c]!=null){
                    this.pos[r][c].calculateGridPosToScreen(this.padding);;
                }
            }
        }
    }


}
