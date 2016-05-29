package com.example.imart.medviser.controller;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imart.medviser.R;
import com.example.imart.medviser.model.DBHandler;
import com.example.imart.medviser.model.adaptadorMain;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ListView listaTomasHoy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaTomasHoy = (ListView) this.findViewById(R.id.listaTomasHoy);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), NuevaMed.class);
                    startActivity(i);
                }
            });
        }

        String dbname = "mydb.db";
        File dbpath = this.getDatabasePath(dbname);
        Log.i("PATH", dbpath.getAbsolutePath());

        cargarTomasHoy();

    }

    private void cargarTomasHoy() {
        DBHandler dbHandler = new DBHandler(this);

        try {
            Cursor c = dbHandler.getTomasDeHoy();

            String[][] listaDatos = new String[c.getCount()][c.getColumnCount()];

            int i = 0;
            while(c.moveToNext()) {
                for (int j = 0; j < c.getColumnCount(); j++) {
                    listaDatos[i][j] = c.getString(j);
                }
                i++;
            }


            listaTomasHoy.setAdapter(new adaptadorMain(this, listaDatos));
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Cursor c = dbHandler.getTablas();
            String cadena = "";
            while(c.moveToNext()) {
                cadena += "\n";
                for (int j = 0; j < c.getColumnCount(); j++) {
                    cadena += c.getString(j);
                }
            }
            ((TextView)this.findViewById(R.id.txtDebug)).setText(cadena);
        }
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
}
