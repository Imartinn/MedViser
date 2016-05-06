package com.example.imart.medviser;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.imart.medviser.model.DBHandler;
import com.example.imart.medviser.model.adaptador_lista;

import java.util.ArrayList;

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

        cargarTomasHoy();
    }

    private void cargarTomasHoy() {
        DBHandler dbHandler = new DBHandler(this);
        Cursor c = dbHandler.getTomasDeHoy();

        String[][] listaDatos = new String[c.getCount()][c.getColumnCount()];

        for(int i = 0; i < c.getCount(); i++) {
            c.moveToNext();
            for (int j = 0; j < c.getColumnCount(); j++) {
                listaDatos[i][j] = c.getString(j+1);
            }
        }


        listaTomasHoy.setAdapter(new adaptador_lista(listaDatos));


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
