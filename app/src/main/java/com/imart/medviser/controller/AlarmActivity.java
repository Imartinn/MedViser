package com.imart.medviser.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.imart.medviser.model.DBHandler;
import com.imart.medviser.model.ObjMed;
import com.imart.medviser.model.ObjToma;

/**
 * Created by imart on 19/06/2016.
 */
public class AlarmActivity extends Activity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(android.R.color.transparent));
        context = this;

        Bundle bundle = getIntent().getExtras();
        String detalles = bundle.getString("detalles");
        long idToma = bundle.getLong("idToma");

        DBHandler dbHandler = new DBHandler(this);
        ObjToma toma = dbHandler.getToma(idToma);
        ObjMed med = dbHandler.getMed(toma.getIdMed());

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(notification == null)
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        final MediaPlayer mp = MediaPlayer.create(this, notification);
        mp.start();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("HORA DE TOMAR " + med.getNombre()).setCancelable(
                false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mp.stop();
                        mp.release();
                        dialog.cancel();
                        Intent i = new Intent(context, MainActivity.class);
                        context.startActivity(i);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
