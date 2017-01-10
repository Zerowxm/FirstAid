package wxm.com.firstaid;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wxm.com.firstaid.adapter.RecyclerviewAdapter;
import wxm.com.firstaid.module.User;
import wxm.com.firstaid.util.MyBitmapFactory;
import wxm.com.firstaid.util.PrefUtils;
import wxm.com.firstaid.util.Utils;
import wxm.com.firstaid.util.VolleyUtil;

public class Settings extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private AlertDialog.Builder alertDialog;
    private View view;
    @BindView(R.id.user_age)
    TextView user_age;
    @BindView(R.id.user_gender)
    TextView user_gender;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.user_photo)
    ImageView user_photo;
    @BindView(R.id.switch_will)
    Switch switch_ill;

    User user=MyApplication.getInstnce().getUser();

    @BindView(R.id.toolbar_title)
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        textView.setText("修改設置");
        init();
        getInfo(MyApplication.getInstnce().getUser().getId());
        setResult(RESULT_OK, null);

    }
    @Override
    public void onBackPressed() {
        finish();
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    public static final int CHOOSE_PHOTO = 0x1;


    @OnClick({R.id.c_name,R.id.c_gender,R.id.c_age,R.id.c_photo})
    public void onClike(RelativeLayout imageView){
        switch (imageView.getId()){
            case R.id.c_age:
                alertDialog = new AlertDialog.Builder(this);
                view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                alertDialog.setView(view);
                final TextInputLayout addWrapper=(TextInputLayout) view.findViewById(R.id.addWrapper);
                addWrapper.getEditText().setHint("年龄");
                alertDialog.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String age=addWrapper.getEditText().getText().toString();
                        if (age.isEmpty()){
                            addWrapper.setError("empty");
                        }else {
                            addWrapper.setError(null);
                            user_age.setText(age);
                            user.setAge(age);
                        }

                    }
                });
                alertDialog.show();
                break;
            case R.id.c_name:
                alertDialog = new AlertDialog.Builder(this);
                view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                alertDialog.setView(view);
                final TextInputLayout addWrapper2=(TextInputLayout) view.findViewById(R.id.addWrapper);
                addWrapper2.getEditText().setHint("姓名");
                alertDialog.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name=addWrapper2.getEditText().getText().toString();
                        if (name.isEmpty()){
                            addWrapper2.setError("empty");
                        }else {
                            addWrapper2.setError(null);
                            Log.d("info",name);
                            user_name.setText(name);
                            user.setName(name);
                        }

                    }
                });
                alertDialog.show();
                break;
            case R.id.c_photo:
                startActivityForResult(new Intent(this,UpLoadImage.class),1);
                /*Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image*//*");
                Intent chooseImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                chooseImage.setType("image*//*");
                Intent chooserIntent = Intent.createChooser(photoPickerIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{chooseImage});
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(chooserIntent, CHOOSE_PHOTO);*/
                break;

            case R.id.c_gender:
                final List sex=new ArrayList();
                sex.add("1");
                sex.add("男");
                String sexCode;
                alertDialog = new AlertDialog.Builder(this);
                view = getLayoutInflater().inflate(R.layout.test, null);
                alertDialog.setView(view);
                final RadioGroup radioGroup=(RadioGroup) view.findViewById(R.id.rg);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int id) {
                        if (id==R.id.rb0){
                            sex.clear();
                            sex.add("1");
                            sex.add("男");
                        }
                        else if (id==R.id.rb1){
                            sex.clear();
                            sex.add("0");
                            sex.add("女");
                        }
                    }
                });
                alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user.setSex((String) sex.get(0));
                        user_gender.setText((String)sex.get(1));
                    }
                });
                alertDialog.show();
        }
    }
    private Bitmap bitmap=null;

    public void encode(){
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImgUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String icon = MyBitmapFactory.BitmapToString(bitmap);
        Log.d("image",icon);


    }

    private Uri selectedImgUri = null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);super.onActivityResult(requestCode, resultCode, data);
       /* if (resultCode == Activity.RESULT_OK) {
            Uri chosenImageUri = data.getData();
            selectedImgUri = chosenImageUri;
            Picasso.with(this).load(selectedImgUri).into(user_photo);
            MyApplication.getInstnce().getUser().setUrl(chosenImageUri);
        }*/
        if (resultCode==UpLoadImage.UPLOAD){
            String url="http://103.80.124.80/index.php/Home/Api/getPhotoUrl?user_id="+user.getId();
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Picasso.with(Settings.this).load(response.getString("photo_url"))
                                        .into(user_photo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub

                        }
                    });
            MyApplication.getInstnce().addToRequestQueue(jsObjRequest);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {
            saveInfo();
            Log.d("info","save");
            Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
        }else if (id==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveInfo(){
        final String rescuer;
        if (switch_ill.isChecked()){
            rescuer="1";
        }else {
            rescuer="0";
        }
        user.setIs_rescuer(rescuer);
        String url= null;
        String name=null;
        try {
            name= URLEncoder.encode(user_name.getText().toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url = VolleyUtil.url_prefix+"setInformation?user_id="+user.getId()
                    +"&username="+name+"&sex="
                    + Utils.getSexIndex(user_gender.getText().toString())
                    +"&age="+user_age.getText().toString()+"&is_Rescuer="+user.getIs_rescuer();


        Log.d("url",url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("add", response.toString());
                        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("user_name", user_name.getText().toString());
                        editor.putString("is_rescuer",rescuer);
                        editor.putString("sex",user_gender.getText().toString());
                        editor.putString("age",user_age.getText().toString());
                        editor.apply();

                    }


                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("add", "Error: " + error.getMessage());
            }
        }

        ){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString =
                            new String(response.data,"UTF-8");
                    Log.d("parse_bf",""+response.data);
                    Log.d("parse",jsonString);
                    Log.d("parse_header",HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        };

        MyApplication.getInstnce().addToRequestQueue(jsonObjReq);
    }

    public void init(){
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_name.setText(sharedpreferences.getString("user_name",""));
        user_age.setText(sharedpreferences.getString("age",""));
        user_gender.setText(sharedpreferences.getString("sex",""));
        Picasso.with(this).load(user.getImage()).into(user_photo);

    }

    public void getInfo(String id){
        String url=VolleyUtil.url_prefix+"getSetInformation?user_id="+id;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("info", response.toString());
                        try {
                            if("1".equals(response.getString("sex"))){
                                user_gender.setText("男");
                            }
                            else {
                                user_gender.setText("女");

                            }
                            user_age.setText(response.getString("age"));

                            user_name.setText(response.getString("username"));
                            Picasso.with(Settings.this).load(response.getString("photo_url").toString()).into(user_photo);
                            if ("1".equals(user.getIs_rescuer())){
                                switch_ill.setChecked(true);
                            }else {
                                switch_ill.setChecked(false);
                            }
                           } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("info", "Error: " + error.getMessage());
            }
        }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString =
                            new String(response.data,"UTF-8");
                    return Response.success(new JSONObject(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        };
        MyApplication.getInstnce().addToRequestQueue(jsonObjReq);
    }

    @OnClick(R.id.to_logout)
    public void logout(){
        new AlertDialog.Builder(this)
                .setTitle("退出")
                .setMessage("确认要退出吗？")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(PrefUtils.Logout(Settings.this)){
                            finish();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                }).setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                }

        })
                .show();


    }

}
