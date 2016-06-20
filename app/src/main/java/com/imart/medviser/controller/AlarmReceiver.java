package com.imart.medviser.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.imart.medviser.model.DBHandler;
import com.imart.medviser.model.ObjToma;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            String detalles = bundle.getString("detalles");
            long idToma = bundle.getLong("idToma");

            DBHandler dbHandler = new DBHandler(context);
            ObjToma toma = dbHandler.getToma(idToma);
            Calendar cal = Calendar.getInstance();
            int dia = cal.get(Calendar.DAY_OF_WEEK);
            boolean activarAlarma = false;

            Calendar ahora = Calendar.getInstance();
            Calendar horaToma = Calendar.getInstance();
            horaToma.set(Calendar.HOUR_OF_DAY, Integer.parseInt(toma.getHora().split(":")[0]));
            horaToma.set(Calendar.MINUTE, Integer.parseInt(toma.getHora().split(":")[1]));
            if(horaToma.before(ahora)) { //Si es una alarma pasada
                Log.e("ALARMA", "ALARMA PASADA " + detalles);
                return;
            }
            Log.e("ALARMA", "ALARMA ACTUAL " + detalles);

            switch (dia) {
                case Calendar.MONDAY:
                    activarAlarma = toma.isLunes();
                    break;
                case Calendar.TUESDAY:
                    activarAlarma = toma.isMartes();
                    break;
                case Calendar.WEDNESDAY:
                    activarAlarma = toma.isMiercoles();
                    break;
                case Calendar.THURSDAY:
                    activarAlarma = toma.isJueves();
                    break;
                case Calendar.FRIDAY:
                    activarAlarma = toma.isViernes();
                    break;
                case Calendar.SATURDAY:
                    activarAlarma = toma.isSabado();
                    break;
                case Calendar.SUNDAY:
                    activarAlarma = toma.isDomingo();
                    break;
            }

            if(activarAlarma) {
                Intent i = new Intent(context, AlarmActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtras(bundle);
                context.startActivity(i);
            }

        } catch (Exception e) {
            Toast.makeText(context, "Hubo un error al lanzar la alarma ¡Pero es hora de tomar un medicamento!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }
    }
}
