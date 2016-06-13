package com.example.imart.medviser.model;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RestClient {

    private static final String BASE_URL = "http://localhost/medViser/";

    private static SyncHttpClient client = new SyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


    public void sincronizarConServidor(Context context) {

        DBHandler dbHandler = new DBHandler(context);

        int ultimoIdMedLocal = dbHandler.getUltimoId(DBHandler.TABLE_MEDS);
        RequestParams rp = new RequestParams();
        rp.add("user", Sesion.user);
        rp.add("pass", Sesion.pass);
        rp.add("wanted", DBHandler.TABLE_MEDS);

        get("http://localhost/medViser/askLastRow.php", rp, new JsonHttpResponseHandler("UTF-8") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject res = null;
                try {
                    res = response.getJSONObject("response");
                    int ultimoIdMedRemoto = Integer.parseInt(res.getString("last"));
                    Log.i("getDeviceNodeEvents", String.valueOf(res));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}