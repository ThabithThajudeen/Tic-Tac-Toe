package com.example.mad_assignment1_final;

import android.graphics.drawable.Drawable;

public class Profile {

    String name = "FirstLast";
    int hiScore;
    Drawable avatarImage;
    int avatarArrayNumber;
    boolean activePlayer;

    public Profile() {
        name = "First Last";
        hiScore = 0;
        avatarImage = null;
        activePlayer = false;
    }

    public void setName(String pName) {
        this.name = pName;
    }
//    public void setHiScore(int hiScore) {
//        this.hiScore = hiScore;
//    }
    public void setAvatarImage(Drawable avatarImage) {
        this.avatarImage = avatarImage;
    }
//    public void setActivePlayer(boolean pActivePlayer) { this.activePlayer = pActivePlayer; }
    public void setAvatarArrayNumber(int pValue) { this.avatarArrayNumber = pValue; }

    public String getName() {
        return name;
    }
//    public int getHiScore() {
//        return hiScore;
//    }
    public Drawable getAvatarImage() {
        return avatarImage;
    }
//    public boolean getActivePlayer() {
//        return activePlayer;
//    }
    public int getAvatarArrayNumber() {
        return avatarArrayNumber;
    }
}