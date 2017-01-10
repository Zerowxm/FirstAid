package wxm.com.firstaid.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import wxm.com.firstaid.MyApplication;
import wxm.com.firstaid.R;

import static wxm.com.firstaid.Settings.CHOOSE_PHOTO;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                Intent chooseImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                chooseImage.setType("image/*");
                Intent chooserIntent = Intent.createChooser(photoPickerIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{chooseImage});
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(chooserIntent, CHOOSE_PHOTO);
            }
        });
    }

    Bitmap bitmap=null;
    Uri selectedImgUri=null;

    public void encode(){
        try {

            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImgUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String icon = MyBitmapFactory.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
//        String icon = MyBitmapFactory.BitmapToString(bitmap);
        Log.d("image",icon);
       send(icon );

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri chosenImageUri = data.getData();
            selectedImgUri = chosenImageUri;
            encode();
        }
    }


    public void send(final String icon){
        String URL = VolleyUtil.url_prefix+"pic_modify";
        String url2=VolleyUtil.url_prefix+"pic_modify2";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_id", "4");
                params.put("smeta", icon);

                return params;
            }
        };
        StringRequest postRequest2 = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_id", "4");
                params.put("smeta", icon);

                return params;
            }
        };
        MyApplication.getInstnce().addToRequestQueue(postRequest);
        MyApplication.getInstnce().addToRequestQueue(postRequest2);
    }

}
