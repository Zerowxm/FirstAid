package wxm.com.firstaid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.BoolRes;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import wxm.com.firstaid.module.User;
import wxm.com.firstaid.util.LruBitmapCache;
import wxm.com.firstaid.util.VolleyUtil;

/**
 * Created by Zero on 12/18/2015.
 */
public class MyApplication extends Application {

    public static final String TAG=MyApplication.class.getSimpleName();
    public static final String baseUrl=VolleyUtil.url_prefix;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private User user;
    private Boolean isLogined=false;

    private static MyApplication instnce;

    @Override
    public void onCreate() {
        super.onCreate();
        instnce=this;
        login();
    }

    public static synchronized MyApplication getInstnce(){
        return instnce;
    }

    public RequestQueue getmRequestQueue(){
        if (mRequestQueue ==null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getmImageLoader() {
        getmRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getmRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getmRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public boolean login(){
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String phone=sharedpreferences.getString("phone","");
        String password=sharedpreferences.getString("password","");
        String user_id=sharedpreferences.getString("user_id","");
        String name=sharedpreferences.getString("user_name","");
        String image=sharedpreferences.getString("image","");
        String sex=sharedpreferences.getString("sex","");
        String is_rescuer=sharedpreferences.getString("is_rescuer","");
        String age=sharedpreferences.getString("age","");
        String record=sharedpreferences.getString("records","");
        user=new User(is_rescuer,user_id,phone,password,name,image,sex,age);
        user.setRecord(record);
        Log.d("app",user.toString());
//        login("13194075172","1234");
        if (phone!=""&&password!=""){
            login(phone,password);
            isLogined = true;
        }else {
            Log.d("user","false");
            isLogined=false;
        }
        return isLogined;

    }

    public User getUser(){
        return user;
    }

    public Boolean getIsLogined(){
        return isLogined;
    }


    boolean login(final String phone, final String password){
        String url = VolleyUtil.url_prefix+"doLogin?phone="+phone+"&password="+password;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String status=String.valueOf(response.getInt("status"));
                            String msg=response.getString("msg");
                            String name=response.getString("username");
                            String image=response.getString("photo_url");
                            String user_id=response.getString("user_id");
                            String records=response.getString("disease_record");
                            Log.d("TEST",status);
                            if (onLoginSuccess(status))
                            {
                                user.setPassword(password);
                                user.setPhone(phone);
                                Log.d("user",user.toString());
                                isLogined=true;

                            }else {
                                Log.d("login","false");
                                isLogined=false;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstnce().addToRequestQueue(jsonObjReq);
        return isLogined;
    }

    public boolean onLoginSuccess(String result) {
        if ("1".equals(result)){
            return true;
        }else
            return false;
    }

}
