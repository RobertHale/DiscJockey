package com.hale.robert.discjockey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 3/7/2018.
 */

public class ScoreCard {

    private int numberOfHoles;
    private String courseName;
    private ArrayList<Hole> holes;

    public ScoreCard(int numHoles){
        this.numberOfHoles = numHoles;
        setupHoles();
    }

    public ScoreCard(int numHoles, String name){
        this.numberOfHoles = numHoles;
        this.courseName = name;
        setupHoles();
    }

    private void setupHoles() {
        this.holes = new ArrayList<>();
        for (int i = 0; i < this.numberOfHoles; i++){
            holes.add(new Hole(i+1));
        }
    }

    public void setUsers(List<User> users){
        for (User user: users) {
            for (Hole hole: this.holes) {
                hole.addUser(user);
            }
        }
    }

    public boolean setPars(List<Integer> pars){
        if (pars.size() != this.numberOfHoles){
            return false;
        }
        for (int i = 0; i < this.numberOfHoles; i++) {
            this.holes.get(i).setPar(pars.get(i));
        }
        return true;
    }

    public boolean setDistances(List<Integer> dists){
        if (dists.size() != this.numberOfHoles){
            return false;
        }
        for (int i = 0; i < this.numberOfHoles; i++) {
            this.holes.get(i).setDistance(dists.get(i));
        }
        return true;
    }
}
