package com.example.user.a0716_appnewsreader;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.x;

/**
 * Created by User on 15.01.2017.
 */

public class DownloadFromApiClass extends AsyncTask<String,Void,ArrayList<Long>> {
    ArrayList<Long> listOfArticles;
    @Override
    protected ArrayList<Long> doInBackground(String... urls) {

        try {
            URL urlAddress = new URL(urls[0]);
            HttpURLConnection connection =(HttpURLConnection) urlAddress.openConnection();
            InputStream iS = connection.getInputStream();
            InputStreamReader InputReader = new InputStreamReader(iS);
            BufferedReader bufferedReader = new BufferedReader(InputReader);
            listOfArticles = new ArrayList<>();
            while(bufferedReader.read()!=-1){
                String line = bufferedReader.readLine();
                line = line.replaceAll("\\[|\\]", "");
                line = line.replaceAll("\\s+","");
                String x[] = line.split(",");
                for(String m:x){
                    listOfArticles.add(Long.parseLong(m));
                }
                line = null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listOfArticles;
    }

    @Override
    protected void onPostExecute(ArrayList<Long> s) {
        super.onPostExecute(s);
    }
}
