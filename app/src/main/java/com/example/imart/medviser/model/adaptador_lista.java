package com.example.imart.medviser.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imart.medviser.MainActivity;
import com.example.imart.medviser.R;

/**
 * Created by Ignacio Martin on 5/05/16.
 */
public class adaptador_lista extends BaseAdapter {

    private String nombreMed, horaMed, detallesToma;
    private Context context;
    private int[] imageId;
    private static LayoutInflater inflater=null;


    public adaptador_lista(String nombreMed, String horaMed, String detallesToma, Context context, int[] imageId) {
        this.nombreMed = nombreMed;
        this.horaMed = horaMed;
        this.detallesToma = detallesToma;
        this.context = context;
        this.imageId = imageId;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = inflater.inflate(R.layout.list_entrada, null);
        ((TextView)v.findViewById(R.id.txtMedNombre)).setText(nombreMed);
        ((TextView)v.findViewById(R.id.txtHoraToma)).setText(horaMed);
        ((TextView)v.findViewById(R.id.txtDetallesToma)).setText(detallesToma);
        ImageButton btnTomar = ((ImageButton)v.findViewById(R.id.btnTomar));
        btnTomar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }
}
