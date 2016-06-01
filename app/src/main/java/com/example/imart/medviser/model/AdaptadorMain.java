package com.example.imart.medviser.model;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.imart.medviser.R;
import com.example.imart.medviser.controller.MainActivity;
import com.example.imart.medviser.view.objEntradaMain;

import java.util.Calendar;

/**
 * Created by Ignacio Martin on 5/05/16.
 */
public class AdaptadorMain extends BaseAdapter {

    private Context context;
    private objEntradaMain[] listaTomas;
    private static LayoutInflater inflater=null;


    public AdaptadorMain(Context context, objEntradaMain[] listaTomas) {
        this.context = context;
        this.listaTomas = listaTomas;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listaTomas.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listaTomas[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = inflater.inflate(R.layout.list_entrada, null);

        TextView txtMedNombre = (TextView)v.findViewById(R.id.lblNombreMed);
        TextView txtHoraToma = (TextView)v.findViewById(R.id.lblHoraToma);
        TextView txtDetallesToma = (TextView)v.findViewById(R.id.lblDetallesToma);

        txtMedNombre.setText(listaTomas[position].getNombreMed());
        txtHoraToma.setText(listaTomas[position].getHoraToma());
        txtDetallesToma.setText(listaTomas[position].getDetallesToma());
        ImageButton btnTomar = ((ImageButton)v.findViewById(R.id.btnTomar));

        if(listaTomas[position].getEstado() == 0) {
            btnTomar.setEnabled(false);
            btnTomar.setColorFilter(Color.GREEN);
        } else if(listaTomas[position].getEstado() == 1) {
            btnTomar.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnTomar.setImageDrawable(context.getDrawable(R.drawable.aprox));
            } else {
                btnTomar.setImageDrawable(context.getResources().getDrawable(R.drawable.aprox));
            }
        } else if(listaTomas[position].getEstado() == 2) {
            btnTomar.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnTomar.setImageDrawable(context.getDrawable(R.drawable.wrong));
            } else {
                btnTomar.setImageDrawable(context.getResources().getDrawable(R.drawable.wrong));
            }
        }

        btnTomar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calNow = Calendar.getInstance();
                int horaActual = calNow.get(Calendar.HOUR_OF_DAY);
                int minsActual = calNow.get(Calendar.MINUTE);

                int horaToma = Integer.parseInt(listaTomas[position].getHoraToma().split(":")[0]);
                int minsToma = Integer.parseInt(listaTomas[position].getHoraToma().split(":")[1]);

                Log.d("ACTUAL", "HA: " + horaActual);
                Log.d("ACTUAL", "MA: " + minsActual);

                Log.d("TOMA", "HT: " + horaToma);
                Log.d("TOMA", "HT: " + minsToma);

                int diferencia = 0;

                if(horaActual == horaToma) {
                    diferencia = Math.max(minsActual, minsToma) - Math.min(minsActual, minsToma);
                } else if (horaActual < horaToma) {
                    diferencia = 60 - minsActual;
                    diferencia += minsToma;
                    diferencia += ((horaToma - horaActual) * 60)-60;
                } else {
                    diferencia = 60 - minsToma;
                    diferencia += minsActual;
                    diferencia += ((horaActual - horaToma) * 60)-60;
                }

                int estado = 0;
                if(diferencia <= 30) {
                    estado = 0;
                } else if (diferencia <= 60) {
                    estado = 1;
                } else {
                    estado = 2;
                }

                DBHandler dbHandler = new DBHandler(context);
                long res = dbHandler.insertarRegistro(listaTomas[position].getIdToma(), listaTomas[position].getIdMed(), horaToma + ":" + minsToma,
                        calNow.getTimeInMillis(), estado);

                ((MainActivity)context).cargarTomasHoy();
            }
        });

        return v;
    }
}
