package com.hale.robert.discjockey;

/**
 * Created by robert on 3/7/2018.
 */

public class User {

    private String name;
    private int id;

    public User(String name, int id){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
