package com.imart.medviser.controller;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.imart.medviser.R;
import com.imart.medviser.model.DBHandler;
import com.imart.medviser.model.ObjMed;

public class ListMedsActivity extends AppCompatActivity {

    private ListView listMeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_meds);

        listMeds = (ListView) this.findViewById(R.id.listMeds);

        cargarLista();
    }

    private void cargarLista() {
        DBHandler dbHandler = new DBHandler(this);
        Cursor c = dbHandler.getMeds();
        final ObjMed[] objMed = new ObjMed[c.getCount()];
        String[] nombreMeds = new String[c.getCount()];

        int i = 0;
        while(c.moveToNext()) {
            objMed[i] = new ObjMed();
            objMed[i].setIdMed(Integer.parseInt(c.getString(0)));
            objMed[i].setNombre(c.getString(1));
            objMed[i].setDetalles(c.getString(2));
            objMed[i].setEnActivo(Boolean.parseBoolean(c.getString(3)));
            nombreMeds[i] = objMed[i].getNombre();
            i++;
        }
        listMeds.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, nombreMeds) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                convertView = super.getView(position, convertView, parent);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int idMed = objMed[position].getIdMed();
                        Intent i = new Intent(getApplicationContext(), NuevaMedActivity.class);
                        i.putExtra("idMed", idMed);
                        startActivity(i);
                    }
                });
                return convertView;
            }
        });
    }

    @Override
    protected void onResume() {
        cargarLista();
        super.onResume();
    }
}
