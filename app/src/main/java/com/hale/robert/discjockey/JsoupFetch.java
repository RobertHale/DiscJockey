package com.hale.robert.discjockey;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class JsoupFetch{
    public interface FetchCallback {
        void fetchStart();
        void fetchComplete(Document d);
        void fetchFailed(IOException e);
    }

    private FetchCallback fetchCallback = null;
    private Context context = null;

    JsoupFetch(FetchCallback fetchCallback, Context context, String url) {
        this.fetchCallback = fetchCallback;
        this.context = context;
        JsoupAsyncTask jat = new JsoupAsyncTask();
        jat.execute(url);
    }

    public class JsoupAsyncTask extends AsyncTask<String, Integer, Void> {

        private Document document;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fetchCallback.fetchStart();
        }

        @Override
        protected Void doInBackground(String... urls) {
            try {
                this.document = Jsoup.connect(urls[0]).get();
                fetchCallback.fetchComplete(document);
            } catch (IOException e) {
                fetchCallback.fetchFailed(e);
            }
            return null;
        }
    }
}
