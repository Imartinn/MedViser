package com.imart.medviser.model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class RestClient {

    private static Context ctx;

    private static final String BASE_URL = "http://192.168.1.250/medViser/";

    private static SyncHttpClient client = new SyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, StringEntity entity, AsyncHttpResponseHandler responseHandler) {
        client.post(ctx, getAbsoluteUrl(url), entity, "application/json", responseHandler);
        //client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


    public static void bajarDelServer(Context context) {
        Sesion.loadSavedLogin(context);

        DBHandler dbHandler = new DBHandler(context);
        ctx = context;

        Log.d("RECIBIENDO", "ROWS");
        try {
            pedirRows(DBHandler.TABLE_MEDS);
            Thread.sleep(1000);
            pedirRows(DBHandler.TABLE_TOMAS);
            Thread.sleep(1000);
            pedirRows(DBHandler.TABLE_REGISTROS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void subirAlServer(Context context) {
        Sesion.loadSavedLogin(context);

        DBHandler dbHandler = new DBHandler(context);
        ctx = context;

        final int ultimoIdRegLocal = dbHandler.getUltimoId(DBHandler.TABLE_REGISTROS);
        JSONObject jo = new JSONObject();
        try {
            jo.put("user", Sesion.user);
            jo.put("pass", Sesion.pass);
            jo.put("wanted", DBHandler.TABLE_REGISTROS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity entity = null;
        try {
            entity = new StringEntity(jo.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        post("askLastRow.php", entity, new JsonHttpResponseHandler("UTF-8") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("RESPONSES", response.toString());

                try {
                    int ultimoIdRegRemoto = Integer.parseInt(response.getString("last"));

                    if (ultimoIdRegLocal > ultimoIdRegRemoto) {
                        Log.d("ENVIANDO", "ROWS");
                        try {
                            enviarRows(DBHandler.TABLE_MEDS);
                            Thread.sleep(1000);
                            enviarRows(DBHandler.TABLE_TOMAS);
                            Thread.sleep(1000);
                            enviarRows(DBHandler.TABLE_REGISTROS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("getDeviceNodeEvents", String.valueOf(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("RESPONSEF", statusCode + " :::: " + responseString);
            }
        });
    }


    /**
     * Contacta con el servidor para comprobar que DB está mas actualizada, la local o la remota y
     * actúa en consecuencia enviando o pidiendo registros
     * @param context
     */
//    public static void sincronizarConServidor(Context context) {
//        DBHandler dbHandler = new DBHandler(context);
//        ctx = context;
//
//        final int ultimoIdMedLocal = dbHandler.getUltimoId(DBHandler.TABLE_MEDS);
//        JSONObject jo = new JSONObject();
//        try {
//            jo.put("user", Sesion.user);
//            jo.put("pass", Sesion.pass);
//            jo.put("wanted", DBHandler.TABLE_MEDS);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        StringEntity entity = null;
//        try {
//            entity = new StringEntity(jo.toString());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//
//        post("askLastRow.php", entity, new JsonHttpResponseHandler("UTF-8") {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.d("RESPONSES", response.toString());
//
//                try {
//                    int ultimoIdMedRemoto = Integer.parseInt(response.getString("last"));
//
//                    if (ultimoIdMedLocal > ultimoIdMedRemoto) {
//                        Log.d("ENVIANDO", "ROWS");
//                        try {
//                            enviarRows(DBHandler.TABLE_MEDS);
//                            Thread.sleep(5000);
//                            enviarRows(DBHandler.TABLE_TOMAS);
//                            Thread.sleep(5000);
//                            enviarRows(DBHandler.TABLE_REGISTROS);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    } else if (ultimoIdMedLocal > -1 && ultimoIdMedLocal < ultimoIdMedRemoto){
//                        Log.d("RECIBIENDO", "ROWS");
//                        try {
//                            pedirRows(DBHandler.TABLE_MEDS);
//                            Thread.sleep(5000);
//                            pedirRows(DBHandler.TABLE_TOMAS);
//                            Thread.sleep(5000);
//                            pedirRows(DBHandler.TABLE_REGISTROS);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    Log.i("getDeviceNodeEvents", String.valueOf(response));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                Log.d("RESPONSEF", statusCode + " :::: " + responseString);
//            }
//        });
//    }


    /**
     * Envia los registros de la tabla objetivo a partir del id pasado como parametro
     * @param table tabla de la que sacar los registros
     */
    private static void enviarRows(String table) {
        DBHandler dbHandler = new DBHandler(ctx);

        if(table.equals(DBHandler.TABLE_MEDS)) {
            Cursor c = dbHandler.getMeds();
            ObjMed[] meds = new ObjMed[c.getCount()];
            JSONArray Jmeds = new JSONArray();
            int i = 0;
            while (c.moveToNext()) {
                meds[i] = new ObjMed();
                JSONObject jo = new JSONObject();

                meds[i].setIdMed(Integer.valueOf(c.getString(0)));
                meds[i].setNombre(c.getString(1));
                meds[i].setDetalles(c.getString(2));
                meds[i].setEnActivo(c.getInt(3)==1);

                try {
                    jo.put("idMed", meds[i].getIdMed());
                    jo.put("nombre", meds[i].getNombre());
                    jo.put("detalles", meds[i].getDetalles());
                    jo.put("enActivo", meds[i].isEnActivo()?1:0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Jmeds.put(jo);
            }

            if(meds.length > 0) {
                JSONObject jo1 = new JSONObject();
                try {
                    jo1.put("user", Sesion.user);
                    jo1.put("pass", Sesion.pass);
                    jo1.put("wanted", table);
                    jo1.put("data", Jmeds.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("DATA", Jmeds.toString());

                StringEntity entity = null;
                try {
                    entity = new StringEntity(jo1.toString());
                    Log.d("SENDING", jo1.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                post("insertRows.php", entity, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("RESULT INSERT MEDSS", new String(responseBody));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("RESULT INSERT MEDSF", new String(responseBody));
                    }
                });
            }
        } else if(table.equals(DBHandler.TABLE_TOMAS)) {
            Cursor c = dbHandler.getTomas();
            ObjToma[] tomas = new ObjToma[c.getCount()];
            JSONArray Jmeds = new JSONArray();
            int i = 0;
            while (c.moveToNext()) {
                tomas[i] = new ObjToma();
                JSONObject jo = new JSONObject();

                tomas[i].setIdToma(Integer.valueOf(c.getString(0)));
                tomas[i].setIdMed(Integer.valueOf(c.getString(1)));
                tomas[i].setLunes(c.getInt(2) == 1);
                tomas[i].setMartes(c.getInt(3) == 1);
                tomas[i].setMiercoles(c.getInt(4) == 1);
                tomas[i].setJueves(c.getInt(5) == 1);
                tomas[i].setViernes(c.getInt(6) == 1);
                tomas[i].setSabado(c.getInt(7) == 1);
                tomas[i].setDomingo(c.getInt(8) == 1);
                tomas[i].setDetalles(c.getString(9));
                tomas[i].setHora(c.getString(10));
                tomas[i].setActivo(c.getInt(11) == 1);

                try {
                    jo.put("idToma", tomas[i].getIdToma());
                    jo.put("idMed", tomas[i].getIdMed());
                    jo.put("lunes", tomas[i].isLunes()?1:0);
                    jo.put("martes", tomas[i].isMartes()?1:0);
                    jo.put("miercoles", tomas[i].isMiercoles()?1:0);
                    jo.put("jueves", tomas[i].isJueves()?1:0);
                    jo.put("viernes", tomas[i].isViernes()?1:0);
                    jo.put("sabado", tomas[i].isSabado()?1:0);
                    jo.put("domingo", tomas[i].isDomingo()?1:0);
                    jo.put("detalles", tomas[i].getDetalles());
                    jo.put("hora", tomas[i].getHora());
                    jo.put("enActivo", tomas[i].isActivo()?1:0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Jmeds.put(jo);
            }

            if(tomas.length > 0) {
                JSONObject jo1 = new JSONObject();
                try {
                    jo1.put("user", Sesion.user);
                    jo1.put("pass", Sesion.pass);
                    jo1.put("wanted", table);
                    jo1.put("data", Jmeds.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("DATA", Jmeds.toString());

                StringEntity entity = null;
                try {
                    entity = new StringEntity(jo1.toString());
                    Log.d("SENDING", jo1.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                post("insertRows.php", entity, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("RESULT INSERT TOMASS", new String(responseBody));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("RESULT INSERT TOMASF", new String(responseBody));
                    }
                });
            }
        } else if(table.equals(DBHandler.TABLE_REGISTROS)) {
            Cursor c = dbHandler.getRegistros();
            ObjReg[] regs = new ObjReg[c.getCount()];
            JSONArray Jmeds = new JSONArray();
            int i = 0;
            while (c.moveToNext()) {
                regs[i] = new ObjReg();
                JSONObject jo = new JSONObject();

                regs[i].setIdReg(Integer.valueOf(c.getString(0)));
                regs[i].setIdMed(Integer.valueOf(c.getString(1)));
                regs[i].setIdToma(Integer.valueOf(c.getString(2)));
                regs[i].setHoraToma(c.getString(3));
                regs[i].setFechaReg(Long.parseLong(c.getString(4)));
                regs[i].setEstado(Integer.parseInt(c.getString(5)));

                try {
                    jo.put("idReg", regs[i].getIdReg());
                    jo.put("idMed", regs[i].getIdMed());
                    jo.put("idToma", regs[i].getIdToma());
                    jo.put("horaToma", regs[i].getHoraToma());
                    jo.put("fechaRegistro", regs[i].getFechaReg());
                    jo.put("estadoToma", regs[i].getEstado());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Jmeds.put(jo);
            }

            if (regs.length > 0) {
                JSONObject jo1 = new JSONObject();
                try {
                    jo1.put("user", Sesion.user);
                    jo1.put("pass", Sesion.pass);
                    jo1.put("wanted", table);
                    jo1.put("data", Jmeds.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("DATA", Jmeds.toString());

                StringEntity entity = null;
                try {
                    entity = new StringEntity(jo1.toString());
                    Log.d("SENDING", jo1.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                post("insertRows.php", entity, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("RESULT INSERT REGSS", new String(responseBody));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("RESULT INSERT REGSF", new String(responseBody));
                    }
                });
            }
        }
    }

    /**
     * Pide los registros de la tabla objetivo a partir del id pasado como parametro
     * @param table tabla de la que pedir los registros
     */
    private static void pedirRows(String table) {

         if(table.equals(DBHandler.TABLE_MEDS)) {
            JSONObject jo1 = new JSONObject();
            try {
                jo1.put("user", Sesion.user);
                jo1.put("pass", Sesion.pass);
                jo1.put("wanted", table);
                jo1.put("last", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            StringEntity entity = null;
            try {
                entity = new StringEntity(jo1.toString());
                Log.d("SENDING", jo1.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            post("getLastRows.php", entity, new JsonHttpResponseHandler("UTF-8") {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("MEDS OBJECT RECIBIDASS", response.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("MEDS ARRAY RECIBIDASS", response.toString());
                    ObjMed[] meds = new ObjMed[response.length()];
                    for(int i = 0; i < response.length(); i++) {
                        meds[i] = new ObjMed();
                        try {
                            JSONObject jo = response.getJSONObject(i);
                            meds[i].setIdMed(jo.getInt("idMed"));
                            meds[i].setNombre(jo.getString("nombre"));
                            meds[i].setDetalles(jo.getString("detalles"));
                            meds[i].setEnActivo(jo.getInt("enActivo")==1?true:false);
//                             meds[i].setNombre(response.getString(2));
//                             meds[i].setDetalles(response.getString(3));
//                             meds[i].setEnActivo(Boolean.parseBoolean(response.getString(3)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    for(ObjMed med : meds) {
                        DBHandler dbHandler = new DBHandler(ctx);
                        dbHandler.insertarMed(med.getIdMed(), med.getNombre(), med.getDetalles(), med.isEnActivo());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("MEDS ARRAY RECIBIDASF", responseString);
                }
            });
        } else if(table.equals(DBHandler.TABLE_TOMAS)) {
             JSONObject jo1 = new JSONObject();
             try {
                 jo1.put("user", Sesion.user);
                 jo1.put("pass", Sesion.pass);
                 jo1.put("wanted", table);
                 jo1.put("last", 0);
             } catch (JSONException e) {
                 e.printStackTrace();
             }

             StringEntity entity = null;
             try {
                 entity = new StringEntity(jo1.toString());
                 Log.d("SENDING", jo1.toString());
             } catch (UnsupportedEncodingException e) {
                 e.printStackTrace();
             }
             entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

             post("getLastRows.php", entity, new JsonHttpResponseHandler("UTF-8") {
                 @Override
                 public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                     Log.d("TOMAS OBJECT RECIBIDASS", response.toString());
                 }

                 @Override
                 public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                     Log.d("TOMAS ARRAY RECIBIDASS", response.toString());
                     ObjToma[] tomas = new ObjToma[response.length()];
                     for(int i = 0; i < response.length(); i++) {
                         tomas[i] = new ObjToma();
                         try {
                             JSONObject jo = response.getJSONObject(i);
                             tomas[i].setIdToma(jo.getInt("idToma"));
                             tomas[i].setIdMed(jo.getInt("idMed"));
                             tomas[i].setLunes(jo.getInt("lunes") == 1);
                             tomas[i].setMartes(jo.getInt("martes") == 1);
                             tomas[i].setMiercoles(jo.getInt("miercoles") == 1);
                             tomas[i].setJueves(jo.getInt("jueves") == 1);
                             tomas[i].setViernes(jo.getInt("viernes") == 1);
                             tomas[i].setSabado(jo.getInt("sabado") == 1);
                             tomas[i].setDomingo(jo.getInt("domingo") == 1);
                             tomas[i].setDetalles(jo.getString("detalles"));
                             tomas[i].setHora(jo.getString("hora").substring(0,4));
                             tomas[i].setActivo(jo.getInt("enActivo") == 1);
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                     for(ObjToma toma : tomas) {
                         DBHandler dbHandler = new DBHandler(ctx);
                         dbHandler.insertarToma(toma);
                     }
                 }

                 @Override
                 public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                     Log.d("TOMAS ARRAY RECIBIDASF", responseString);
                 }
             });
         } else if(table.equals(DBHandler.TABLE_REGISTROS)) {
             JSONObject jo1 = new JSONObject();
             try {
                 jo1.put("user", Sesion.user);
                 jo1.put("pass", Sesion.pass);
                 jo1.put("wanted", table);
                 jo1.put("last", 0);
             } catch (JSONException e) {
                 e.printStackTrace();
             }

             StringEntity entity = null;
             try {
                 entity = new StringEntity(jo1.toString());
                 Log.d("SENDING", jo1.toString());
             } catch (UnsupportedEncodingException e) {
                 e.printStackTrace();
             }
             entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

             post("getLastRows.php", entity, new JsonHttpResponseHandler("UTF-8") {
                 @Override
                 public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                     Log.d("REGS OBJECT RECIBIDASS", response.toString());
                 }

                 @Override
                 public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                     Log.d("REGS ARRAY RECIBIDASS", response.toString());
                     ObjReg[] regs = new ObjReg[response.length()];
                     for(int i = 0; i < response.length(); i++) {
                         regs[i] = new ObjReg();
                         try {
                             JSONObject jo = response.getJSONObject(i);
                             regs[i].setIdReg(jo.getInt("idReg"));
                             regs[i].setIdMed(jo.getInt("idMed"));
                             regs[i].setIdToma(jo.getInt("idToma"));
                             regs[i].setHoraToma(jo.getString("horaToma"));
                             regs[i].setFechaReg(jo.getLong("fechaRegistro"));
                             regs[i].setEstado(jo.getInt("estadoToma"));
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                     for(ObjReg reg : regs) {
                         DBHandler dbHandler = new DBHandler(ctx);
                         dbHandler.insertarRegistro(reg);
                     }

                 }

                 @Override
                 public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                     Log.d("REGS ARRAY RECIBIDASF", responseString);
                 }
             });
         }
    }
}

