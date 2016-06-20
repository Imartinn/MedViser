package com.imart.medviser.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.imart.medviser.R;
import com.imart.medviser.model.AdaptadorMain;
import com.imart.medviser.model.DBHandler;
import com.imart.medviser.model.ObjToma;
import com.imart.medviser.view.objEntradaMain;

import java.io.File;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listaTomasHoy;
    private AdaptadorMain adapter;
    private ImageButton btnRefrescar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaTomasHoy = (ListView) this.findViewById(R.id.listaTomasHoy);
        btnRefrescar = (ImageButton) this.findViewById(R.id.btnRefrescar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), NuevaMedActivity.class);
                    startActivity(i);
                }
            });
        }
        //TODO:Fragments
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String dbname = "mydb.db";
        File dbpath = this.getDatabasePath(dbname);
        Log.i("PATH", dbpath.getAbsolutePath());

        cargarTomasHoy();

        btnRefrescar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarTomasHoy();
            }
        });

    }

    public void cargarTomasHoy() {
        setearAlarmas();
        DBHandler dbHandler = new DBHandler(this);
        dbHandler.leerMeds();
        try {
            Cursor c = dbHandler.getTomasDeHoy();
            objEntradaMain[] listaTomas = new objEntradaMain[c.getCount()];

            int i = 0;
            while(c.moveToNext()) {
                listaTomas[i] = new objEntradaMain();
                listaTomas[i].setIdMed(Integer.parseInt(c.getString(0)));
                listaTomas[i].setIdToma(Integer.parseInt(c.getString(1)));
                listaTomas[i].setNombreMed(c.getString(2));
                listaTomas[i].setHoraToma(c.getString(3));
                listaTomas[i].setDetallesToma(c.getString(4));
                listaTomas[i].setEstado(buscarEstado(listaTomas[i].getIdToma()));
                i++;
            }

            adapter = new AdaptadorMain(this, listaTomas);
            adapter.notifyDataSetChanged();
            listaTomasHoy.setAdapter(adapter);
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
        }
    }

    private void setearAlarmas() {
        desactivarAlarmas();
        DBHandler dbHandler = new DBHandler(this);
        ObjToma[] tomas = dbHandler.getTomasActivas();

        for(ObjToma toma : tomas) {
            //Seteamos el AlarmManager para que salte todos los dias a la hora definida por la toma, el
            //receiver se encarga de diferenciar los dias elegidos.
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(toma.getHora().split(":")[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(toma.getHora().split(":")[1]));
            calendar.set(Calendar.SECOND, 0);
            Calendar ahora = Calendar.getInstance();
            if(calendar.before(ahora)) { //Si es una alarma pasada
                continue;
            }
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("detalles", toma.getDetalles());
            intent.putExtra("idToma", toma.getIdToma());
            PendingIntent pi = PendingIntent.getBroadcast(this, (int)toma.getIdToma(),
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            if(Build.VERSION.SDK_INT > 18) {
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
            } else {
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pi);
            }

        }
    }

    private int buscarEstado(int idToma) {
        DBHandler dbHandler = new DBHandler(this);

        String res = dbHandler.isTomada(idToma);

        if(res != null) {
            return Integer.parseInt(res);
        }
        return -1;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nuevaMed) {
            Intent i = new Intent(getApplicationContext(), NuevaMedActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_listaMeds) {
            Intent i = new Intent(getApplicationContext(), ListMedsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_login) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void desactivarAlarmas() {
        DBHandler dbHandler = new DBHandler(this);
        int[] listaIdTomas = dbHandler.getIdTomas();
        for(int id : listaIdTomas) {
            PendingIntent pi = PendingIntent.getBroadcast(this, id,
                    new Intent(this, AlarmReceiver.class), PendingIntent.FLAG_CANCEL_CURRENT);
        }
    }

    @Override
    protected void onResume() {
        cargarTomasHoy();
        super.onResume();
    }
}
