package com.example.imart.medviser.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by imart on 28/04/2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    private final String DB_CREATE = "CREATE TABLE meds ( id integer primary key not null, nombre varchar(30) not null, " +
            "detalles varchar(30) not null ); CREATE TABLE tomas ( idToma integer primary key not null, " +
            "idMed integer not null, lunes bit not null, martes bit not null, miercoles bit not null, " +
            "jueves bit not null, viernes bit not null, sabado bit not null, domingo bit not null, " +
            "detalles varchar(30) not null, hora time not null, foreign key (idMed) references meds(id));";

    private final String DB_DROP = "DROP TABLE IF EXISTS meds; DROP TABLE IF EXISTS tomas;";

    private final String TABLE_MEDS = "meds";
    private final String TABLE_TOMAS = "tomas";

    private final String MEDS_COLUM_ID = "id";
    private final String MEDS_COLUM_NOMBRE = "nombre";
    private final String MEDS_COLUM_DETALLES = "detalles";

    private final String TOMAS_COLUM_IDTOMA = "idToma";
    private final String TOMAS_COLUM_IDMED = "idMed";
    private final String TOMAS_COLUM_LUNES = "lunes";
    private final String TOMAS_COLUM_MARTES = "martes";
    private final String TOMAS_COLUM_MIERCOLES = "miercoles";
    private final String TOMAS_COLUM_JUEVES = "jueves";
    private final String TOMAS_COLUM_VIERNES = "viernes";
    private final String TOMAS_COLUM_SABADO = "sabado";
    private final String TOMAS_COLUM_DOMINGO = "domingo";
    private final String TOMAS_COLUM_DETALLES = "detalles";
    private final String TOMAS_COLUM_HORA = "hora";


    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DB_DROP);
        onCreate(sqLiteDatabase);
    }

    public long insertarMed(String nombre, String detalles) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDS_COLUM_NOMBRE, nombre);
        contentValues.put(MEDS_COLUM_DETALLES, detalles);
        return db.insert(TABLE_MEDS, null, contentValues);
    }

    public long insertarTomas(int idMed, boolean lunes, boolean martes, boolean miercoles, boolean jueves, boolean viernes,
                              boolean sabado, boolean domingo, String detalles, String hora) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TOMAS_COLUM_IDMED, idMed);
        contentValues.put(TOMAS_COLUM_LUNES, lunes);
        contentValues.put(TOMAS_COLUM_MARTES, martes);
        contentValues.put(TOMAS_COLUM_MIERCOLES, miercoles);
        contentValues.put(TOMAS_COLUM_JUEVES, jueves);
        contentValues.put(TOMAS_COLUM_VIERNES, viernes);
        contentValues.put(TOMAS_COLUM_SABADO, sabado);
        contentValues.put(TOMAS_COLUM_DOMINGO, domingo);
        contentValues.put(TOMAS_COLUM_DETALLES, detalles);
        contentValues.put(TOMAS_COLUM_HORA, hora);
        return db.insert(TABLE_TOMAS, null, contentValues);
    }
}
