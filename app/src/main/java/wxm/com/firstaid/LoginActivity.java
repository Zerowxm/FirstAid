package wxm.com.firstaid;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wxm.com.firstaid.util.PrefUtils;
import wxm.com.firstaid.util.VolleyCallback;
import wxm.com.firstaid.util.VolleyUtil;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.to_login)
    Button login_btn;
    @BindView(R.id.forget_psw)
    TextView forget_psw;
    @BindView(R.id.to_sign)
    TextView sign_btn;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setToolbar();
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginActivity.this,SignActivity.class));
            }
        });
    }

    @BindView(R.id.phoneWrapper)
    TextInputLayout phoneWrapper;
    @BindView(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        upArrow.setColorFilter(ContextCompat.getColor(this , R.color.primary), PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(upArrow);
    }


    public boolean validate() {
        boolean valid = true;
        String phone = phoneWrapper.getEditText().getText().toString();
        String password = passwordWrapper.getEditText().getText().toString();

        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            phoneWrapper.setError("enter a valid phone address");
            valid = false;
        } else {
            phoneWrapper.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordWrapper.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordWrapper.setError(null);
        }

        return valid;
    }

    @OnClick(R.id.forget_psw)
    public void pw(){
        startActivity(new Intent(this,FindPW.class));
    }


    public void login() {
        Log.d(TAG, "Login");

//        if (!validate()) {
//            onLoginFailed("");
//            return;
//        }

        login_btn.setEnabled(false);

        progressDialog= new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("登錄中...");
        progressDialog.show();


        VolleyCallback callback=new VolleyCallback() {
            @Override
            public void onSuccess() {
//                progressDialog.dismiss();
                setResult(RESULT_OK, null);
                PrefUtils.markUse(LoginActivity.this);
                progressDialog.dismiss();
                finish();

            }
        };
        String password = passwordWrapper.getEditText().getText().toString();
        String phone=phoneWrapper.getEditText().getText().toString();
        // TODO: Implement your own authentication logic here.

        login(phone, password, callback);

    }

    public boolean onLoginSuccess(String result) {
        if ("1".equals(result)){
            return true;
        }else
            return false;
    }

    public void onLoginFailed(String msg) {
        Toast.makeText(getBaseContext(), "Login failed:"+msg, Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
        new AlertDialog.Builder(this)
                .setTitle("登錄失败")
                .setMessage("请重新登錄")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        login_btn.setEnabled(true);
    }


    void login(final String phone, final String password, final VolleyCallback callback){
        String url = VolleyUtil.url_prefix+"doLogin?phone="+phone+"&password="+password;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String status=response.getString("status");
                            String msg=response.getString("msg");

                            if (onLoginSuccess(status))
                            {
                                login_btn.setEnabled(true);
                                callback.onSuccess();
                                SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("phone",phone);
                                editor.putString("password",password);
                                String name=response.getString("username");
                                String image=response.getString("photo_url");
                                String user_id=response.getString("user_id");
                                String records=response.getString("disease_record");
                                editor.putString("user_id",user_id);
                                editor.putString("username",name);
                                editor.putString("image",image);
                                editor.putString("records",records);
                                editor.apply();
                                editor.commit();

                            }else {
                                onLoginFailed(msg);
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
