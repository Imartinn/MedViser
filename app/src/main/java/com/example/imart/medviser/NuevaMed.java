package com.example.imart.medviser;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.imart.medviser.model.DBHandler;
import com.example.imart.medviser.model.ObjToma;

import java.util.ArrayList;

public class NuevaMed extends AppCompatActivity {

    private LayoutInflater inflater;
    private LinearLayout linToma = null;

    private EditText txtMedNombre, txtMedDetalle;
    private CheckBox chkLunes, chkMartes, chkMiercoles, chkJueves, chkViernes, chkSabado, chkDomingo;
    private Switch swActiva;
    private Button btnInsertar;
    private ArrayList<View> listaTomas;


    @Override
    protected void onCreate(Bundle savedInstanceState) { //TODO: Añadir boton de eliminar toma
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_med);

        linToma = (LinearLayout) this.findViewById(R.id.linToma);
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
        btnInsertar = (Button) this.findViewById(R.id.btnInsertar);
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

        //btnNuevaHora.callOnClick(); //Generamos la primera linea
        if(btnInsertar != null) {
            btnInsertar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guardarMed();
                }
            });
        }
    }

    private void cargarMed() {

    }

    private void guardarMed() {

        if(!comprobarCampos()) {
            Toast.makeText(this, "Debe indicar el nombre del medicamento, rellenar todas las horas de las tomas y seleccionar" +
                    " al menos un día de toma.", Toast.LENGTH_SHORT).show();
            return;
        }

        DBHandler dbHandler = new DBHandler(this);

        long idMed = dbHandler.insertarMed(txtMedNombre.getText().toString(), txtMedDetalle.getText().toString(),
                swActiva.isActivated());

        if(idMed != -1) {

            for (View v : listaTomas) {
                ObjToma objToma = new ObjToma();
                objToma.setIdMed(idMed);
//                objToma.setLunes(((CheckBox) (v.findViewById(R.id.chkLunes))).isChecked());
//                objToma.setMartes(((CheckBox) (v.findViewById(R.id.chkMartes))).isChecked());
//                objToma.setMiercoles(((CheckBox) (v.findViewById(R.id.chkMiercoles))).isChecked());
//                objToma.setJueves(((CheckBox) (v.findViewById(R.id.chkJueves))).isChecked());
//                objToma.setViernes(((CheckBox) (v.findViewById(R.id.chkViernes))).isChecked());
//                objToma.setSabado(((CheckBox) (v.findViewById(R.id.chkSabado))).isChecked());
//                objToma.setDomingo(((CheckBox) (v.findViewById(R.id.chkDomingo))).isChecked());
//                objToma.setDetalles(((EditText) (v.findViewById(R.id.txtDetallesToma))).getText().toString());
                objToma.setLunes(chkLunes.isChecked());
                objToma.setMartes(chkMartes.isChecked());
                objToma.setMiercoles(chkMiercoles.isChecked());
                objToma.setJueves(chkJueves.isChecked());
                objToma.setViernes(chkViernes.isChecked());
                objToma.setSabado(chkSabado.isChecked());
                objToma.setDomingo(chkDomingo.isChecked());
                objToma.setDetalles(((EditText) (v.findViewById(R.id.txtDetallesToma))).getText().toString());
                objToma.setHora(((EditText) (v.findViewById(R.id.txtHoraToma))).getText().toString());
                long insert = dbHandler.insertarTomas(objToma);
                Toast.makeText(NuevaMed.this, "Insertados: " + insert, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private boolean comprobarCampos() {
        if(txtMedNombre.getText().length() < 1) {
            return false;
        }

        for(View v : listaTomas) {
            if(((EditText)(v).findViewById(R.id.txtHoraToma)).getText().length() < 1) {
                return false;
            }
        }

        if(!chkLunes.isChecked() && !chkMartes.isChecked() && !chkMiercoles.isChecked() && !chkJueves.isChecked()
                && !chkViernes.isChecked() && !chkSabado.isChecked() && !chkDomingo.isChecked()) {
            return false;
        }
        return true;
    }
}
