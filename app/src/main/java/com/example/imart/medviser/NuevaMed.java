package com.example.imart.medviser;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;

import com.example.imart.medviser.model.DBHandler;
import com.example.imart.medviser.model.ObjToma;

import java.util.ArrayList;

public class NuevaMed extends AppCompatActivity {

    private LayoutInflater inflater;
    private final LinearLayout linToma = (LinearLayout) this.findViewById(R.id.linToma);

    private EditText txtMedNombre, txtMedDetalle;
    private CheckBox chkLunes, chkMartes, chkMiercoles, chkJueves, chkViernes, chkSabado, chkDomingo;
    private Switch swActiva;
    private ArrayList<View> listaTomas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_med);

        txtMedNombre = (EditText) this.findViewById(R.id.txtMedNombre);
        txtMedDetalle = (EditText) this.findViewById(R.id.txtMedDetalle);
        chkLunes = (CheckBox) this.findViewById(R.id.chkLunes);
        chkMartes = (CheckBox) this.findViewById(R.id.chkMartes);
        chkMiercoles = (CheckBox) this.findViewById(R.id.chkMiercoles);
        chkJueves = (CheckBox) this.findViewById(R.id.chkJueves);
        chkViernes = (CheckBox) this.findViewById(R.id.chkViernes);
        chkSabado = (CheckBox) this.findViewById(R.id.chkSabado);
        chkDomingo = (CheckBox) this.findViewById(R.id.chkDomingo);
        swActiva = (Switch) this.findViewById(R.id.swActiva);
        listaTomas = new ArrayList<View>();

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageButton btnNuevaHora = (ImageButton) this.findViewById(R.id.btnNuevaHora);

        if(getIntent().getExtras() != null) {
            cargarMed();
        }

        assert btnNuevaHora != null;
        btnNuevaHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View child = inflater.inflate(R.layout.lintomahora, null); //Generamos la linea
                final EditText hora = (EditText)child.findViewById(R.id.txtHoraToma);
                hora.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                hora.setText( selectedHour + ":" + selectedMinute);
                            }
                        }, 0, 0, true);//Yes 24 hour time
                        mTimePicker.setTitle(getString(R.string.titleSeleccionaHora));
                        mTimePicker.show();
                    }
                });
                linToma.addView(child); //La agregamos al contenedor de tomas
                listaTomas.add(child); //Guardamos referencia en el arrayList
            }
        });

        btnNuevaHora.callOnClick(); //Generamos una linea
    }

    private void cargarMed() {

    }

    private void guardarMed() { //TODO: Comprobar campos
        DBHandler dbHandler = new DBHandler(this);
        dbHandler.getWritableDatabase();

        long idMed = dbHandler.insertarMed(txtMedNombre.getText().toString(), txtMedDetalle.getText().toString(),
                swActiva.isActivated());

        if(idMed != -1) {

            for (View v : listaTomas) {
                ObjToma objToma = new ObjToma();
                objToma.setIdMed(idMed);
                objToma.setLunes(((CheckBox) (v.findViewById(R.id.chkLunes))).isChecked());
                objToma.setMartes(((CheckBox) (v.findViewById(R.id.chkMartes))).isChecked());
                objToma.setMiercoles(((CheckBox) (v.findViewById(R.id.chkMiercoles))).isChecked());
                objToma.setJueves(((CheckBox) (v.findViewById(R.id.chkJueves))).isChecked());
                objToma.setViernes(((CheckBox) (v.findViewById(R.id.chkViernes))).isChecked());
                objToma.setSabado(((CheckBox) (v.findViewById(R.id.chkSabado))).isChecked());
                objToma.setDomingo(((CheckBox) (v.findViewById(R.id.chkDomingo))).isChecked());
                objToma.setDetalles(((EditText) (v.findViewById(R.id.txtDetallesToma))).getText().toString());
                objToma.setHora(((EditText) (v.findViewById(R.id.txtHoraToma))).getText().toString());
                dbHandler.insertarTomas(objToma);
            }

        }
    }
}
