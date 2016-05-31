package com.example.imart.medviser.controller;

import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import com.example.imart.medviser.R;
import com.example.imart.medviser.model.DBHandler;
import com.example.imart.medviser.model.ObjToma;

import java.util.ArrayList;
import java.util.Date;

public class NuevaMedActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private LinearLayout linToma = null;

    private EditText txtMedNombre, txtMedDetalle;
    private CheckBox chkLunes, chkMartes, chkMiercoles, chkJueves, chkViernes, chkSabado, chkDomingo;
    private Switch swActiva;
    private Button btnInsertar;
    private ArrayList<View> listaTomas;

    private int idMed = -1;

    private boolean edit;


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

        if(getIntent().getExtras() != null) {
            idMed = getIntent().getExtras().getInt("idMed");
            if (idMed > 0) {
                cargarMed();
            }
        }

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageButton btnNuevaHora = (ImageButton) this.findViewById(R.id.btnNuevaHora);
        ImageButton btnQuitarHora = (ImageButton) this.findViewById(R.id.btnQuitarHora);

        if (btnQuitarHora != null) {
            btnQuitarHora.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linToma.removeViewAt(linToma.getChildCount()-1); //La agregamos al contenedor de tomas
                    listaTomas.remove(listaTomas.size()-1); //Guardamos referencia en el arrayList
                }
            });
        }

        assert btnNuevaHora != null;
        btnNuevaHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View child = inflater.inflate(R.layout.lintomahora, null); //Generamos la linea
                final EditText hora = (EditText) child.findViewById(R.id.txtHoraToma);
                hora.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                hora.setText(selectedHour + ":" + selectedMinute);
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


        btnNuevaHora.callOnClick(); //Generamos la primera linea
        if (btnInsertar != null) {
            btnInsertar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guardarMed();
                }
            });
        }


    }

    private void cargarMed() {
        DBHandler dbHandler = new DBHandler(this);
        Cursor c = dbHandler.getEditMed(idMed);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(c.moveToNext()) {
            txtMedNombre.setText(c.getString(1));
            txtMedDetalle.setText(c.getString(2));
            Log.d("ACTIVA", c.getString(3));
            swActiva.setChecked(Boolean.parseBoolean(c.getString((3))));

            if(c.getString(6).equals("1")) {
                chkLunes.setChecked(true);
            }
            if(c.getString(7).equals("1")) {
                chkMartes.setChecked(true);
            }
            if(c.getString(8).equals("1")) {
                chkMiercoles.setChecked(true);
            }
            if(c.getString(9).equals("1")) {
                chkJueves.setChecked(true);
            }
            if(c.getString(10).equals("1")) {
                chkViernes.setChecked(true);
            }
            if(c.getString(11).equals("1")) {
                chkSabado.setChecked(true);
            }
            if(c.getString(12).equals("1")) {
                chkDomingo.setChecked(true);
            }

            do {
                //TODO: hacer update de tomas en vez de delete+insert
                View child = inflater.inflate(R.layout.lintomahora, null); //Generamos la linea
                EditText detalles = (EditText) child.findViewById(R.id.txtDetallesToma);
                detalles.setText(c.getString(13));
                final EditText hora = (EditText) child.findViewById(R.id.txtHoraToma);
                hora.setText(c.getString(14));
                hora.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                Date d = new Date();
                                d.setHours(selectedHour); d.setMinutes(selectedMinute);
                                hora.setText(d.getHours() + ":" + d.getMinutes());
                            }
                        }, 0, 0, true);//Yes 24 hour time
                        mTimePicker.setTitle(getString(R.string.titleSeleccionaHora));
                        mTimePicker.show();
                    }
                });
                linToma.addView(child); //La agregamos al contenedor de tomas
                listaTomas.add(child); //Guardamos referencia en el arrayList
            } while(c.moveToNext());
        }

    }

    private void guardarMed() {

        if(!comprobarCampos()) {
            Toast.makeText(this, "Debe indicar el nombre del medicamento, rellenar todas las horas de las tomas y seleccionar" +
                    " al menos un día de toma.", Toast.LENGTH_SHORT).show();
            return;
        }

        DBHandler dbHandler = new DBHandler(this);

        if(idMed > 0) {
            dbHandler.borrarMedYtomas(idMed);
        }

        long idMed = dbHandler.insertarMed(txtMedNombre.getText().toString(), txtMedDetalle.getText().toString(),
                swActiva.isChecked());

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
                Toast.makeText(NuevaMedActivity.this, "Insertados: " + insert, Toast.LENGTH_SHORT).show();
                finish();
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
