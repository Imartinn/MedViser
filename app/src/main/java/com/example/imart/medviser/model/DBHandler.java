package com.example.imart.medviser.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by imart on 28/04/2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    private final String TABLE_MEDS = "meds";
    private final String TABLE_TOMAS = "tomas";
    private final String TABLE_REGISTROS = "registros";
    private final String TABLE_ESTADOS = "estados";

    private final String MEDS_COLUM_IDMED = "idMed";
    private final String MEDS_COLUM_NOMBRE = "nombre";
    private final String MEDS_COLUM_DETALLES = "detalles";
    private final String MEDS_COLUM_ACTIVO = "enActivo";

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

    private final String REG_COLUM_IDREG = "idReg";
    private final String REG_COLUM_IDMED = "idMed";
    private final String REG_COLUM_IDTOMA = "idToma";
    private final String REG_COLUM_HORATOMA = "horaToma";
    private final String REG_COLUM_FECHAREG = "fechaRegistro";
    private final String REG_COLUM_HORAREG = "horaRegistro";
    private final String REG_COLUM_ESTADO = "estadoToma";

    private final String ESTADO_COLUM_IDESTADO = "idEstado";
    private final String ESTADO_COLUM_NOMBRE = "nombreEstado";


    private final String DB_CREATE = "CREATE TABLE " + TABLE_MEDS+ " ( "+ MEDS_COLUM_IDMED +" integer primary key autoincrement not null, "+MEDS_COLUM_NOMBRE+" varchar(30) not null, " +
            MEDS_COLUM_DETALLES+" varchar(30) not null, "+MEDS_COLUM_ACTIVO+" bit not null ); CREATE TABLE "+TABLE_TOMAS+" ( "+TOMAS_COLUM_IDTOMA+" integer primary key autoincrement not null, "
            +TOMAS_COLUM_IDMED+" integer not null, "+TOMAS_COLUM_LUNES+" bit not null, "+TOMAS_COLUM_MARTES+" bit not null, "+TOMAS_COLUM_MIERCOLES+" bit not null, " +
            TOMAS_COLUM_JUEVES+" bit not null, "+TOMAS_COLUM_VIERNES+" bit not null, "+TOMAS_COLUM_SABADO+" bit not null, "+TOMAS_COLUM_DOMINGO+" bit not null, " +
            TOMAS_COLUM_DETALLES+" varchar(30) not null,"+TOMAS_COLUM_HORA+" time not null, foreign key ("+TOMAS_COLUM_IDMED+") references meds("+MEDS_COLUM_IDMED+"));" +
            "CREATE TABLE " + TABLE_REGISTROS + " ( "+ REG_COLUM_IDREG + " integer primary key not null, "+REG_COLUM_IDMED+" integer not null, "+
            REG_COLUM_IDTOMA + " int not null, " + REG_COLUM_HORATOMA + " varchar(5) not null, " + REG_COLUM_FECHAREG + " long not null, "/* + REG_COLUM_HORAREG + " time, " */+ REG_COLUM_ESTADO + " integer not null, foreign key (" + REG_COLUM_IDMED + ") references meds("+MEDS_COLUM_IDMED+"));" +
            " CREATE TABLE " + TABLE_ESTADOS + "( "+ ESTADO_COLUM_IDESTADO + " integer primary key not null, " + ESTADO_COLUM_NOMBRE + " varchar(20) not null);";

    private final String DB_DROP = "DROP TABLE IF EXISTS "+TABLE_MEDS+"; DROP TABLE IF EXISTS "+TABLE_TOMAS+";";


    public DBHandler(Context ctx){
        super(ctx, "meddb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] statements = DB_CREATE.split(";");
        for (String s : statements) {
            db.execSQL(s);
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DB_DROP);
        onCreate(db);
    }


    public long insertarMed(String nombre, String detalles, boolean activo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDS_COLUM_NOMBRE, nombre);
        contentValues.put(MEDS_COLUM_DETALLES, detalles);
        contentValues.put(MEDS_COLUM_ACTIVO, activo);
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

    public long insertarTomas(ObjToma objToma) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TOMAS_COLUM_IDMED, objToma.getIdMed());
        contentValues.put(TOMAS_COLUM_LUNES, objToma.isLunes());
        contentValues.put(TOMAS_COLUM_MARTES, objToma.isMartes());
        contentValues.put(TOMAS_COLUM_MIERCOLES, objToma.isMiercoles());
        contentValues.put(TOMAS_COLUM_JUEVES, objToma.isJueves());
        contentValues.put(TOMAS_COLUM_VIERNES, objToma.isViernes());
        contentValues.put(TOMAS_COLUM_SABADO, objToma.isSabado());
        contentValues.put(TOMAS_COLUM_DOMINGO, objToma.isDomingo());
        contentValues.put(TOMAS_COLUM_DETALLES, objToma.getDetalles());
        contentValues.put(TOMAS_COLUM_HORA, objToma.getHora());
        return db.insert(TABLE_TOMAS, null, contentValues);
    }

    public long insertarRegistro(int idToma, int idMed, String horaToma, long fechaReg,/* String horaRegistro,*/ int estado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REG_COLUM_IDMED, idMed);
        contentValues.put(REG_COLUM_IDTOMA, idToma);
        contentValues.put(REG_COLUM_HORATOMA, horaToma);
        contentValues.put(REG_COLUM_FECHAREG, fechaReg);
//        contentValues.put(REG_COLUM_HORAREG, horaRegistro);
        contentValues.put(REG_COLUM_ESTADO, estado);
        long res = db.insert(TABLE_REGISTROS, null, contentValues);
        Log.d("RESULT", "" + res);
        return res;
    }

    public long insertarEstado(int idEstado, String nombreEstado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ESTADO_COLUM_NOMBRE, nombreEstado);
        return db.insert(TABLE_ESTADOS, null, contentValues);
    }

    public Cursor getTomasDeHoy() {
        SQLiteDatabase db = this.getReadableDatabase();
        String dia = null;

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                dia = TOMAS_COLUM_LUNES;
                break;
            case Calendar.TUESDAY:
                dia = TOMAS_COLUM_MARTES;
                break;
            case Calendar.WEDNESDAY:
                dia = TOMAS_COLUM_MIERCOLES;
                break;
            case Calendar.THURSDAY:
                dia = TOMAS_COLUM_JUEVES;
                break;
            case Calendar.FRIDAY:
                dia = TOMAS_COLUM_VIERNES;
                break;
            case Calendar.SATURDAY:
                dia = TOMAS_COLUM_SABADO;
                break;
            case Calendar.SUNDAY:
                dia = TOMAS_COLUM_DOMINGO;
                break;
        }

        final String query = "SELECT " + TABLE_TOMAS + "." + TOMAS_COLUM_IDMED + "," + TABLE_TOMAS +
                "." + TOMAS_COLUM_IDTOMA + "," + MEDS_COLUM_NOMBRE + ","
                + TOMAS_COLUM_HORA + "," + TABLE_TOMAS + "." + TOMAS_COLUM_DETALLES +
                " FROM " + TABLE_TOMAS + " INNER JOIN " + TABLE_MEDS + " ON " + TABLE_TOMAS + "."
                + TOMAS_COLUM_IDMED + " = " + TABLE_MEDS + "." + MEDS_COLUM_IDMED + " WHERE " + dia + " = 1";

        return db.rawQuery(query, null);
    }

    public String isTomada(int idToma) {
        SQLiteDatabase db = this.getReadableDatabase();

//        final String sq = "SELECT * FROM " + TABLE_REGISTROS + ";";
//        Cursor c = db.rawQuery(sq, null);
//        String res= "";
//        while (c.moveToNext()) {
//            res += c.getString(0) + ";" + c.getString(1) + ";" + c.getString(2) + ";" + c.getString(3) + ";" +
//                    c.getString(4) + ";" + c.getString(5) + "\n";
//        }
//        Log.d("REGS", res);
        final String query = "SELECT " + REG_COLUM_ESTADO + " FROM " + TABLE_REGISTROS + " WHERE date(" + REG_COLUM_FECHAREG + "/1000, 'unixepoch'," +
                " 'localtime') = date('now') AND " + REG_COLUM_IDTOMA + " = " + idToma + ";";
        //final String query = "SELECT fechaRegistro, date(fechaRegistro/1000, 'unixepoch', 'localtime') FROM registros WHERE idReg = 1";
        Cursor c = db.rawQuery(query, null);
        Log.d("DEBUG", "FILAS:" + c.getCount());
        if(c.moveToNext()) {
            Log.d("DEBUG", "RES:" + c.getString(0));
            return c.getString(0);
        }
        return null;
    }

    public Cursor getTablas() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM sqlite_master", null);
    }

    public Cursor getMeds() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MEDS, null);
    }

    public Cursor getEditMed(int idMed) {
        SQLiteDatabase db = this.getReadableDatabase();
        final String query = "SELECT * FROM " + TABLE_MEDS + " INNER JOIN " + TABLE_TOMAS + " ON " + TABLE_MEDS + "." + MEDS_COLUM_IDMED +
                " = " + TABLE_TOMAS + "." + TOMAS_COLUM_IDMED + " WHERE " + TABLE_MEDS + "." + MEDS_COLUM_IDMED + " = " + idMed;

        return db.rawQuery(query, null);
    }

    public void borrarMedYtomas(int idMed) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDS, MEDS_COLUM_IDMED + " = " + idMed, null);
        db.delete(TABLE_TOMAS, TOMAS_COLUM_IDMED + " = " + idMed, null);
    }

}
