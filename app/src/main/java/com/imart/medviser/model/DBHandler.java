package com.imart.medviser.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by imart on 28/04/2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    public static final String TABLE_MEDS = "meds";
    public static final String TABLE_TOMAS = "tomas";
    public static final String TABLE_REGISTROS = "registros";
    public static final String TABLE_ESTADOS = "estados";

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
    private final String TOMAS_COLUM_ACTIVO = "enActivo";

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
            TOMAS_COLUM_DETALLES+" varchar(30) not null,"+TOMAS_COLUM_HORA+" time not null,"+TOMAS_COLUM_ACTIVO+" bit not null, foreign key ("+TOMAS_COLUM_IDMED+") references meds("+MEDS_COLUM_IDMED+"));" +
            "CREATE TABLE " + TABLE_REGISTROS + " ( "+ REG_COLUM_IDREG + " integer primary key not null, "+REG_COLUM_IDMED+" integer not null, "+
            REG_COLUM_IDTOMA + " int not null, " + REG_COLUM_HORATOMA + " time not null, " + REG_COLUM_FECHAREG + " long not null, "/* + REG_COLUM_HORAREG + " time, " */+ REG_COLUM_ESTADO + " integer not null, foreign key (" + REG_COLUM_IDMED + ") references meds("+MEDS_COLUM_IDMED+"));" +
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
        contentValues.put(MEDS_COLUM_ACTIVO, activo?1:0);
        long res = db.insert(TABLE_MEDS, null, contentValues);
        //db.close();
        return res;
    }

    public long insertarMed(int idMed, String nombre, String detalles, boolean activo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDS_COLUM_IDMED, idMed);
        contentValues.put(MEDS_COLUM_NOMBRE, nombre);
        contentValues.put(MEDS_COLUM_DETALLES, detalles);
        contentValues.put(MEDS_COLUM_ACTIVO, activo?1:0);
        long res = db.insert(TABLE_MEDS, null, contentValues);
        //db.close();
        return res;
    }

    public void leerMeds() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM meds", null);

        while(c.moveToNext()) {
            Log.d("MEDS", c.getString(0) + " : " + c.getString(1) + " : " + c.getString(2) + " : " + c.getString(3));
        }
    }

    public long actualizarMed(int idMed, String nombre, String detalles, boolean activo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDS_COLUM_NOMBRE, nombre);
        contentValues.put(MEDS_COLUM_DETALLES, detalles);
        contentValues.put(MEDS_COLUM_ACTIVO, activo?1:0);
        long res = db.update(TABLE_MEDS, contentValues, MEDS_COLUM_IDMED + " = " + idMed, null);
        //db.close();
        return res;
    }


    public long insertarToma(int idMed, boolean lunes, boolean martes, boolean miercoles, boolean jueves, boolean viernes,
                             boolean sabado, boolean domingo, String detalles, String hora, boolean activo) {
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
        contentValues.put(TOMAS_COLUM_ACTIVO, activo?1:0);
        long res = db.insert(TABLE_TOMAS, null, contentValues);
        //db.close();
        return res;
    }

    public long insertarToma(ObjToma objToma) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(objToma.getIdToma() != -1) {
            contentValues.put(TOMAS_COLUM_IDTOMA, objToma.getIdToma());
        }
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
        contentValues.put(TOMAS_COLUM_ACTIVO, objToma.isActivo()?1:0);
        long res = db.insert(TABLE_TOMAS, null, contentValues);
        //db.close();
        return res;
    }

    public long actualizarToma(ObjToma objToma) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TOMAS_COLUM_LUNES, objToma.isLunes());
        contentValues.put(TOMAS_COLUM_MARTES, objToma.isMartes());
        contentValues.put(TOMAS_COLUM_MIERCOLES, objToma.isMiercoles());
        contentValues.put(TOMAS_COLUM_JUEVES, objToma.isJueves());
        contentValues.put(TOMAS_COLUM_VIERNES, objToma.isViernes());
        contentValues.put(TOMAS_COLUM_SABADO, objToma.isSabado());
        contentValues.put(TOMAS_COLUM_DOMINGO, objToma.isDomingo());
        contentValues.put(TOMAS_COLUM_DETALLES, objToma.getDetalles());
        contentValues.put(TOMAS_COLUM_HORA, objToma.getHora());
        contentValues.put(TOMAS_COLUM_ACTIVO, objToma.isActivo()?1:0);
        long res = db.update(TABLE_TOMAS, contentValues, TOMAS_COLUM_IDTOMA + " = " + objToma.getIdToma(), null);
        //db.close();
        return res;
    }

    public void leerTomas() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM tomas", null);

        while(c.moveToNext()) {
            Log.d("TOMAS", c.getString(0) + " : " + c.getString(1) + " : " + c.getString(2) + " : " + c.getString(3) + " : " + c.getString(4)
                    + " : " + c.getString(5) + " : " + c.getString(6) + " : " + c.getString(7) + " : " + c.getString(8) + " : " + c.getString(9)
                    + " : " + c.getString(10) + " : " + c.getString(11));
        }
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
        Log.d("CONTENT", contentValues.toString());
        Log.d("RESULT", "" + res);
        //db.close();
        return res;
    }

    public void leerRegistros() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM registros", null);

        while(c.moveToNext()) {
            Log.d("REGISTROS", c.getString(0) + " : " + c.getString(1) + " : " + c.getString(2) + " : " + c.getString(3) + " : " + c.getString(4) + " : " + c.getString(5));
        }
    }

    public long insertarRegistro(ObjReg objReg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REG_COLUM_IDREG, objReg.getIdReg());
        contentValues.put(REG_COLUM_IDMED, objReg.getIdMed());
        contentValues.put(REG_COLUM_IDTOMA, objReg.getIdToma());
        contentValues.put(REG_COLUM_HORATOMA, objReg.getHoraToma());
        contentValues.put(REG_COLUM_FECHAREG, objReg.getFechaReg());
//        contentValues.put(REG_COLUM_HORAREG, horaRegistro);
        contentValues.put(REG_COLUM_ESTADO, objReg.getEstado());
        long res = db.insert(TABLE_REGISTROS, null, contentValues);
        Log.d("RESULT", "" + res);
        //db.close();
        return res;
    }

    public long insertarEstado(int idEstado, String nombreEstado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ESTADO_COLUM_NOMBRE, nombreEstado);
        long res = db.insert(TABLE_ESTADOS, null, contentValues);
        //db.close();
        return res;
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
                + TOMAS_COLUM_IDMED + " = " + TABLE_MEDS + "." + MEDS_COLUM_IDMED + " WHERE " + dia + " = 1 AND "
                + TABLE_TOMAS + "." + TOMAS_COLUM_ACTIVO + " = 1 ORDER BY " + TOMAS_COLUM_HORA + " ASC;";

        Cursor res = db.rawQuery(query, null);
        //db.close();
        return res;
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
            String res = c.getString(0);
            //db.close();
            return res;
        }
        return null;
    }

    public Cursor getTablas() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM sqlite_master", null);
        //db.close();
        return res;
    }

    public Cursor getMeds() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_MEDS, null);
        //db.close();
        return res;
    }

    public Cursor getTomas() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_TOMAS, null);
        //db.close();
        return res;
    }

    public Cursor getRegistros() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_REGISTROS, null);
        //db.close();
        return res;
    }

    /**
     * Devuelve las meds a partir del idMed recibido
     * @param idMed
     * @return
     */
    public Cursor getMeds(int idMed) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_MEDS + " WHERE idMed > " + idMed, null);
        //db.close();
        return res;
    }

    public Cursor getEditMed(int idMed) {
        SQLiteDatabase db = this.getReadableDatabase();
        final String query = "SELECT * FROM " + TABLE_MEDS + " INNER JOIN " + TABLE_TOMAS + " ON " + TABLE_MEDS + "." + MEDS_COLUM_IDMED +
                " = " + TABLE_TOMAS + "." + TOMAS_COLUM_IDMED + " WHERE " + TABLE_MEDS + "." + MEDS_COLUM_IDMED + " = " + idMed;

        Cursor res = db.rawQuery(query, null);
        //db.close();
        return res;
    }

    public void borrarMedYtomas(int idMed) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDS, MEDS_COLUM_IDMED + " = " + idMed, null);
        db.delete(TABLE_TOMAS, TOMAS_COLUM_IDMED + " = " + idMed, null);
        //db.close();
    }

    public int getUltimoId(String tabla) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;

        switch (tabla) {
            case TABLE_MEDS:
                c = db.rawQuery("SELECT " + MEDS_COLUM_IDMED + " FROM " + TABLE_MEDS + " ORDER BY " + MEDS_COLUM_IDMED + " DESC LIMIT 1;",
                        null);
                break;
            case TABLE_TOMAS:
                c = db.rawQuery("SELECT " + TOMAS_COLUM_IDTOMA + " FROM " + TABLE_TOMAS + " ORDER BY " + TOMAS_COLUM_IDTOMA + " DESC LIMIT 1;",
                        null);
                break;
            case TABLE_REGISTROS:
                c = db.rawQuery("SELECT " + REG_COLUM_IDREG + " FROM " + TABLE_REGISTROS + " ORDER BY " + REG_COLUM_IDREG + " DESC LIMIT 1;",
                        null);
                break;
        }

        if(c.moveToNext()) {
            int res = Integer.parseInt(c.getString(0));
            //db.close();
            return res;

        }
        //db.close();
        return -1;
    }

    public void desactivarTomas(int idMed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TOMAS_COLUM_ACTIVO, 0);
        db.update(TABLE_TOMAS, contentValues, "idMed = " + idMed, null);
        //db.close();
    }

    public void limpiarBD() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDS, null, null);
        db.delete(TABLE_TOMAS, null, null);
        db.delete(TABLE_REGISTROS, null, null);
    }

    public int[] getIdTomas() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_TOMAS, new String[] {TOMAS_COLUM_IDTOMA}, null, null, null, null, null);
        int[] idTomas = new int[c.getCount()];
        int i=0;
        while (c.moveToNext()) {
            idTomas[i] = c.getInt(0);
            i++;
        }
        db.close();
        return idTomas;
    }

    public ObjToma getToma(long idToma) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TOMAS + " WHERE idToma = " + idToma, null);
        ObjToma objToma = null;

        if(c.moveToNext()) {
            objToma = new ObjToma();
            objToma.setIdToma(c.getInt(0));
            objToma.setIdMed(c.getInt(1));
            objToma.setLunes(c.getInt(2)==1);
            objToma.setMartes(c.getInt(3)==1);
            objToma.setMiercoles(c.getInt(4)==1);
            objToma.setJueves(c.getInt(5)==1);
            objToma.setViernes(c.getInt(6)==1);
            objToma.setSabado(c.getInt(7)==1);
            objToma.setDomingo(c.getInt(8)==1);
            objToma.setDetalles(c.getString(9));
            objToma.setHora(c.getString(10));
            objToma.setActivo(c.getInt(11)==1);
        }
        db.close();
        return objToma;
    }

    public ObjToma[] getTomasActivas() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TOMAS + " WHERE " + TOMAS_COLUM_ACTIVO + " = 1;", null);
        ObjToma[] tomas = new ObjToma[c.getCount()];

        int i = 0;
        while (c.moveToNext()) {
            tomas[i] = new ObjToma();
            tomas[i].setIdToma(c.getInt(0));
            tomas[i].setIdMed(c.getInt(1));
            tomas[i].setLunes(c.getInt(2)==1);
            tomas[i].setMartes(c.getInt(3)==1);
            tomas[i].setMiercoles(c.getInt(4)==1);
            tomas[i].setJueves(c.getInt(5)==1);
            tomas[i].setViernes(c.getInt(6)==1);
            tomas[i].setSabado(c.getInt(7)==1);
            tomas[i].setDomingo(c.getInt(8)==1);
            tomas[i].setDetalles(c.getString(9));
            tomas[i].setHora(c.getString(10));
            tomas[i].setActivo(c.getInt(11)==1);
            i++;
        }
        db.close();
        return tomas;
    }

    public ObjMed getMed(long idMed) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_MEDS + " WHERE idMed = " + idMed, null);
        ObjMed med = null;

        if(c.moveToNext()) {
            med = new ObjMed();
            med.setIdMed(c.getInt(0));
            med.setNombre(c.getString(1));
            med.setDetalles(c.getString(2));
            med.setEnActivo(c.getInt(3)==1);
        }
        db.close();
        return med;
    }
}


