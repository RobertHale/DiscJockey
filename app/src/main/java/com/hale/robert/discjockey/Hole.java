package com.hale.robert.discjockey;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by robert on 3/7/2018.
 */

public class Hole {

    private int number;
    private int par;
    private int distance;
    private HashMap<User, Integer> userScore;

    public Hole(int number){
        this.number = number;
        this.par = 3;
        this.distance = 0;
        userScore = new HashMap<>();
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

    public boolean addUser(User user){
        if (this.userScore.containsKey(user)){
            return false;
        }
        //set default to par
        this.userScore.put(user, this.par);
        return true;
    }

    public HashMap<User, Integer> getUserScore() {
        return userScore;
    }

    public void setUserScore(HashMap<User, Integer> userScore) {
        this.userScore = userScore;
    }
}
