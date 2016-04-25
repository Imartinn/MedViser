package com.example.imart.medviser;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NuevaMed extends AppCompatActivity {

    LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_med);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final LinearLayout linToma = (LinearLayout) this.findViewById(R.id.linToma);
        ImageButton btnNuevaHora = (ImageButton) this.findViewById(R.id.btnNuevaHora);


        btnNuevaHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View child = inflater.inflate(R.layout.lintomahora, null);
                linToma.addView(child);
            }
        });

    }


}
