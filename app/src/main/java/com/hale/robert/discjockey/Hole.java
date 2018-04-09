package com.hale.robert.discjockey;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by robert on 3/7/2018.
 */

public class Hole implements Parcelable{

    private int number;
    private int par;
    private int distance;
    private ArrayList<UserScore> userScores;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.number);
        parcel.writeInt(this.par);
        parcel.writeInt(this.distance);
        parcel.writeInt(this.userScores.size());
        for (int i = 0; i < userScores.size(); i++) {
            parcel.writeString(this.userScores.get(i).user);
            parcel.writeInt(this.userScores.get(i).score);
        }
    }

    public static final Parcelable.Creator<Hole> CREATOR
            = new Parcelable.Creator<Hole>() {
        public Hole createFromParcel(Parcel in) {
            return new Hole(in);
        }

        public Hole[] newArray(int size) {
            return new Hole[size];
        }
    };

    private Hole(Parcel in) {
        this.number = in.readInt();
        this.par = in.readInt();
        this.distance = in.readInt();
        this.userScores = new ArrayList<>();
        int numUsers = in.readInt();
        for (int i = 0; i < numUsers; i++) {
            UserScore us = new UserScore(in.readString());
            us.score = in.readInt();
            this.userScores.add(us);
        }
    }

    public class UserScore {
        public int score;
        public String user;

        public UserScore(String user){
            this.user = user;
            this.score = 0;
        }
    }

    public Hole(int number){
        this.number = number;
        this.par = 3;
        this.distance = 300;
        this.userScores = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public int getPar() {
        return par;
    }

    public void setPar(int par){
        this.par = par;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance){
        this.distance = distance;
    }

    public boolean addUser(String user){
        for (int i = 0; i < userScores.size(); i++) {
            if(userScores.get(i).user.equals(user)){
                return false;
            }
        }
        //set default to par
        UserScore us = new UserScore(user);
        us.score = this.par;
        this.userScores.add(us);
        return true;
    }

    public List<UserScore> getUserScores() {
        return this.userScores;
    }

    public void setUserScores(ArrayList<UserScore> userScores) {
        this.userScores = userScores;
    }
}
