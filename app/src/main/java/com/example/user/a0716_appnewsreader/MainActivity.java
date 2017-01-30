package com.example.user.a0716_appnewsreader;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    static SQLiteDatabase articlesDatabase;
    protected ArrayList<String> listOfTittles;
    protected ArrayAdapter<String> listAdapter;
    protected Cursor c;
    protected ListView mainList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainList = (ListView) findViewById(R.id.listView);
        listOfTittles = new ArrayList<>();
        //Background (Back-end) SQL DB.
        articlesDatabase= this.openOrCreateDatabase("articlesDatabase",MODE_PRIVATE,null);
        articlesDatabase.execSQL("CREATE TABLE IF NOT EXISTS articles (storyID int(10),urlAddress VARCHAR, title VARCHAR)");
        // fill with data from other class.
        if(count()<=1){
            if(fillSQLDatabase()==true){
                fillListWithData();
            }
        }else {
           /* clearSQLDatabase(articlesDatabase);
            if(fillSQLDatabase()==true){
                fillListWithData();
            }*/
            fillListWithData();
        }

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                c = articlesDatabase.rawQuery("SELECT * FROM articles",null);
                int urlAddress = c.getColumnIndex("urlAddress");
                c.move(i+1);
                Intent it = new Intent(getApplicationContext(),Main2Activity.class);
                it.putExtra("httpValue",c.getString(urlAddress));
                startActivity(it);
            }
        });


    }

    public long count() {
        return DatabaseUtils.queryNumEntries(articlesDatabase,"articles");
    }

    protected Boolean fillSQLDatabase(){
        DownloadFromApiClass downloader = new DownloadFromApiClass();
        try {
            FillSQLDB fillSQLDB = new FillSQLDB();
           fillSQLDB.execute(downloader.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return true;
    }

    protected void clearSQLDatabase(SQLiteDatabase databaseName){
        databaseName.rawQuery("DROP TABLE articles",null);
    }

    public boolean fillListWithData(){
        if(listOfTittles.size()==0){
            c = articlesDatabase.rawQuery("SELECT * FROM articles",null);
            int storyID = c.getColumnIndex("storyID");
            int urlAddress = c.getColumnIndex("urlAddress");
            int title = c.getColumnIndex("title");
            c.moveToFirst();
            while(c.isLast()!=true){
                listOfTittles.add(c.getString(title));
                c.moveToNext();
            }
            listAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,listOfTittles);
            listAdapter.notifyDataSetChanged();
            mainList.setAdapter(listAdapter);
            return true;
        }else {
            return false;
        }
    }

}
