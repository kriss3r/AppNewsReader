package com.example.user.a0716_appnewsreader;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by User on 17.01.2017.
 */

public class FillSQLDB extends AsyncTask<ArrayList<Long>,Void,Boolean> {
    ArrayList<JSONObject> array ;
    @Override
    protected Boolean doInBackground(ArrayList<Long>... arrayLists) {
        array = new ArrayList<>();
        Iterator<Long> it = arrayLists[0].listIterator();
        while(it.hasNext()){
            Long value = it.next();
            try {
                URL url = new URL("https://hacker-news.firebaseio.com/v0/item/"+String.valueOf(value)+".json?print=pretty\"");
                HttpURLConnection connection =(HttpURLConnection) url.openConnection();

                InputStream is = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                BufferedReader bReader = new BufferedReader(reader);
                String m ="";
                    m = bReader.readLine();
                JSONObject newObject = new JSONObject(m);
                array.add(newObject);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        for(int i=0;i<20;i++){
            try {
                JSONObject a = array.get(i);
                int id = a.getInt("id");
                String tit ="";
                tit = a.getString("title");
                String title = tit.replaceAll("\'","''");
                String url = "";
                url = a.getString("url");
                Log.i("insert ?",id+title+url);
                MainActivity.articlesDatabase.execSQL("INSERT INTO articles (storyID,urlAddress,title) VALUES("+id+",'"+url+"','"+title+"')");
                title ="";
                url ="";
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
    }
}
