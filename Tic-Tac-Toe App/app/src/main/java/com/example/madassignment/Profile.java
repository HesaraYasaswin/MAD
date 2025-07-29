package com.example.madassignment;
import android.graphics.Bitmap;

public class Profile {

    private String name;
    private Bitmap avatar;
    private Bitmap xMarker;
    private Bitmap oMarker;
    private int win;
    private int draw;
    private int loss;

    // Constructors

    public Profile() { }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public Bitmap getXMarker() {
        return xMarker;
    }

    public void setXMarker(Bitmap xMarker) {
        this.xMarker = xMarker;
    }

    public Bitmap getOMarker() {
        return oMarker;
    }

    public void setOMarker(Bitmap oMarker) {
        this.oMarker = oMarker;
    }
    public int getWin() {
        return win;
    }
    public void setWin(int win) {
        this.win = win;
    }
    public int getDraw() {
        return draw;
    }
    public void setDraw(int draw) {
        this.draw = draw;
    }
    public int getLoss() {
        return loss;
    }
    public void setLoss(int loss) {
        this.loss = loss;
    }
}