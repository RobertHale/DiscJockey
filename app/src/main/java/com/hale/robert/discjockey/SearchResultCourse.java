package com.hale.robert.discjockey;

import java.net.URL;

public class SearchResultCourse {

    private String name;
    private URL url;

    public SearchResultCourse(String name, URL url){
        this.name = name;
        this.url = url;
    }

    public SearchResultCourse(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
