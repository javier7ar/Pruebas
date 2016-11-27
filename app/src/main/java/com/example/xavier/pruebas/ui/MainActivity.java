package com.example.xavier.pruebas.ui;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.xavier.pruebas.R;
import com.example.xavier.pruebas.ui.take_sample.TakeSampleActivity;


public class MainActivity extends AppCompatActivity {
    protected TextView lb_main_titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lb_main_titulo = (TextView) findViewById(R.id.lb_main_titulo);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void tomarMuestra(View view){
        startTakeSampleActivity();
    }

    protected void startTakeSampleActivity() {
        Intent intent = new Intent(this, TakeSampleActivity.class);
        startActivity(intent);
    }

}
