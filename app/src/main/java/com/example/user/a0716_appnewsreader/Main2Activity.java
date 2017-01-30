package com.example.user.a0716_appnewsreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        WebView webVw = (WebView) findViewById(R.id.webView);
        webVw.loadUrl(getIntent().getStringExtra("httpValue"));
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==android.R.id.home){
            Intent it = new Intent(this.getApplicationContext(),MainActivity.class);
            Log.i("tu bylo","tu");
            startActivity(it);
            this.finish();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

}
