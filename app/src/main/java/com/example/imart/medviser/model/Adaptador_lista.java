package com.example.imart.medviser.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.imart.medviser.MainActivity;
import com.example.imart.medviser.R;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ignacio Martin on 5/05/16.
 */
public class Adaptador_lista extends BaseAdapter {

    private Context context;
    private String[][] listaDatos;
    private static LayoutInflater inflater=null;


    public Adaptador_lista(Context context, String[][] listaDatos) {
        this.context = context;
        this.listaDatos = listaDatos;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listaDatos.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listaDatos[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = inflater.inflate(R.layout.list_entrada, null);
        ((TextView)v.findViewById(R.id.txtMedNombre)).setText(listaDatos[position][1]);
        ((TextView)v.findViewById(R.id.txtHoraToma)).setText(listaDatos[position][2]);
        ((TextView)v.findViewById(R.id.txtDetallesToma)).setText(listaDatos[position][3]);
        ImageButton btnTomar = ((ImageButton)v.findViewById(R.id.btnTomar));
        //btnTomar.setImageBitmap(imageId);
        btnTomar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String date = "";

                Long miliDate = Long.parseLong(al.get(position).getDate()) * 1000;
                date = sdf.format(miliDate);



                Calendar calHoraToma = Calendar.getInstance();
                calHoraToma.set(calHoraToma.getTime().getYear(), calHoraToma.getTime().getMonth(), calHoraToma.getTime().getDay(),
                        Integer.parseInt(listaDatos[position][2].split(":")[0]), Integer.parseInt(listaDatos[position][2].split(":")[1]));

                Calendar calNow = Calendar.getInstance();
                calNow.getTime().compareTo(calHoraToma.getTime());

                DBHandler dbHandler = new DBHandler(context);
                dbHandler.insertarRegistro(null, listaDatos[position][0], calHoraToma.getTimeInMillis(), )
            }
        });

        return v;
    }
}
