package wxm.com.firstaid.util;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import wxm.com.firstaid.MyApplication;

import static com.android.volley.Request.*;

/**
 * Created by Zero on 2/1/2016.
 */
public class VolleyUtil {
    private static final String TAG = "volley";
    public static final String url_prefix="http://103.80.124.80/index.php/Home/Api/";

    public static void getJsonObjectRequest(String url) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstnce().addToRequestQueue(jsonObjReq);

    }

    /*public static void getJsonArrayRequest(String url) {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        List<Test> tests = new Gson().fromJson(response.toString(), new TypeToken<List<Test>>() {
                        }.getType());
                        try {
                            Log.d(TAG, response.getJSONObject(0).getJSONArray("Things").toString());
                            List<Thing> things = new Gson().fromJson(response.getJSONObject(0).getJSONArray("Things").toString(),
                                    new TypeToken<List<Thing>>() {
                                    }.getType());

                            for (Thing thing : things) {
                                Log.d(TAG, " " + thing.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "1" + tests.get(0).toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstnce().addToRequestQueue(jsonObjReq);

    }*/

    /*public static void getStringRequest(String url) {
        StringRequest stringRequest = new StringRequest(Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        List<Test> tests = new Gson().fromJson(response, new TypeToken<List<Test>>() {
                        }.getType());
                        Log.d(TAG, "1 " + tests.get(0).toString());

                        Log.d(TAG, "2 " + tests.toString());
                        for (Test test : tests) {
                            Log.d(TAG, test.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });
        MyApplication.getInstnce().addToRequestQueue(stringRequest);
    }*/

    public static void dealWithArray() {

    }

    public static void login(String phone, String password) {
        String url = "http://alline.roadlogica.cn/sessions/create/jsonData=json?phone=" + phone +
                "&password=" + password;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstnce().addToRequestQueue(jsonObjReq);
    }

    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));

        char[] buffer = new char[1024];

        String jsonString = new String();

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();

        jsonString = sb.toString();

        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }
}
