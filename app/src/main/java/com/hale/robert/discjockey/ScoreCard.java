package com.hale.robert.discjockey;

import java.util.ArrayList;
import java.util.HashMap;
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

    ScoreCard(int numHoles, String name){
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

    public void setUsers(List<String> users){
        for (String user: users) {
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

    private Hole getHole(int position){
        if(position < this.holes.size()) {
            return this.holes.get(position);
        }
        return null;
    }

    public List<Hole> getHoles(){
        return this.holes;
    }

    public String getCourseName(){
        return this.courseName;
    }

    public HashMap<String, ArrayList<Integer>> getScoreMap(){
        HashMap<String, ArrayList<Integer>> map = new HashMap<>();
        Hole curHole = getHole(0);
        for(Hole.UserScore us : curHole.getUserScores()){
            ArrayList<Integer> score = new ArrayList<>();
            score.add(us.score - curHole.getPar());
            map.put(us.user, score);
        }
        for (int i = 1; i < this.holes.size(); i++) {
            curHole = getHole(i);
            for (Hole.UserScore us : curHole.getUserScores()){
                map.get(us.user).add(us.score - curHole.getPar());
            }
        }
        return map;
    }
}
