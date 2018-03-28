package com.hale.robert.discjockey;

import android.os.AsyncTask;
import android.util.Log;

import com.hale.robert.discjockey.JsoupFetch.FetchCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CourseCreator {
    private final CreatorCallback callback;
    private int callBacks;

    public interface CreatorCallback {
        void CreationComplete(ArrayList<Integer> pars, ArrayList<Integer> dist);
        void CreationFailed();
    }

    public CourseCreator(CreatorCallback callback, String url){
        this.callback = callback;
        CreatorAsyncTask cat = new CreatorAsyncTask();
        cat.execute(url);
    }

    public class CreatorAsyncTask extends AsyncTask<String, Integer, Void>{

        @Override
        protected Void doInBackground(String... urls) {
            Document d = null;
            try {
                d = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                callback.CreationFailed();
                return null;
            }
            Element table = null;
            String url = null;
            if (urls[0].contains("course-location")) {
                if (d != null){
                    Element linkHolder = d.select("span").get(1);
                    Log.d(TAG, "doInBackground: span");
                    url = linkHolder.selectFirst("a").attr("abs:href");
                    Log.d(TAG, "doInBackground: should be layout: " + url);
                } else {
                    callback.CreationFailed();
                    return null;
                }
            } else {
                if (d != null) {
                    table = d.select("table").get(0);
                } else {
                    callback.CreationFailed();
                    return null;
                }
                if (table != null) {
                    url = table.selectFirst("tbody").selectFirst("tr").select("td").get(1).selectFirst("a").attr("abs:href");
                } else {
                    callback.CreationFailed();
                    return null;
                }
            }
            try {
                d = Jsoup.connect(url).get();
            } catch (IOException e) {
                callback.CreationFailed();
                return null;
            }
            Element list = d.selectFirst("ul");
            ArrayList<Integer> pars = new ArrayList<>();
            ArrayList<Integer> dist = new ArrayList<>();
            for(Element row : list.select("li")){
                String holeUrl = row.selectFirst("a").attr("abs:href");
                Log.d(TAG, "getting hole: " + row.selectFirst("a").text());
                Document hole;
                try {
                    hole = Jsoup.connect(holeUrl).get();
                } catch (IOException e) {
                    callback.CreationFailed();
                    return null;
                }
                Element t = hole.selectFirst("table");
                String par = t.selectFirst("tbody").select("tr").get(1).select("td").get(1).text();
                pars.add(Integer.parseInt(par));
                String distance = t.selectFirst("tbody").select("tr").get(2).select("td").get(1).text();
                if(distance.equals("N/A")){
                    dist.add(300);
                } else {
                    dist.add(Integer.parseInt(distance));
                }
                Log.d(TAG, "doInBackground: done " + par + " " + distance);
            }
            callback.CreationComplete(pars, dist);
            return null;
        }
    }

}
