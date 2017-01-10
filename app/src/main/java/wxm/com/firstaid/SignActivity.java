package wxm.com.firstaid;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import wxm.com.firstaid.util.PrefUtils;
import wxm.com.firstaid.util.VolleyUtil;

public class SignActivity extends AppCompatActivity {
    @BindView(R.id.usernameWrapper)
    TextInputLayout usernameWrapper;
    @BindView(R.id.phoneWrapper)
    TextInputLayout phoneWrapper;
    @BindView(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;
    @BindView(R.id.codeWrapper)
    TextInputLayout codeWrapper;
    @BindView(R.id.sign_btn)
    Button sign_btn;
    @BindView(R.id.to_login)
    TextView toLogin;
    @BindView(R.id.get_code)
    TextView getCode;
    private static final String TAG = "SignupActivity";
    private String code="0";

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        upArrow.setColorFilter(ContextCompat.getColor(this , R.color.primary), PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(upArrow);    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);
        setToolbar();
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                WelcActivity.instance.startActivityForResult(new Intent(SignActivity.this,LoginActivity.class),1);
            }
        });
            getCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone=phoneWrapper.getEditText().getText().toString();
                    if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
                        phoneWrapper.setError("enter a valid phone address");
                    } else {
                        phoneWrapper.setError(null);
                        sendCode(phone);
                        getCode.setEnabled(false);
                        if (runningThree) {
                        } else {
                            downTimer.start();
                        }
                    }
                }
            });

        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // STUB
                hideKeyboard();
               signup();
            }
        });
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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
        if (!code.endsWith(codeWrapper.getEditText().getText().toString())){
            valid=false;
        }else {
            codeWrapper.setError(null);
        }

        return valid;
    }
    ProgressDialog progressDialog;

    public void signUp(String name, final String phone, final String password) {
        try {
            name= URLEncoder.encode(name, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url=VolleyUtil.url_prefix+"doRegister?username="+name+"&phone="+phone
                +"&password="+password;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String state=response.getString("status");
                            String msg=response.getString("msg");
                            if ("1".equals(state)){
                                onSignupSuccess();
                                SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("phone",phone);
                                editor.putString("password",password);
                                editor.apply();
                            }else {
                                onSignupFailed(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MyApplication.getInstnce().addToRequestQueue(jsonObjReq);
    }


    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed("");
            return;
        }

        progressDialog= new ProgressDialog(SignActivity.this);
        progressDialog.setMessage("注冊中...");
        progressDialog.show();

        String username = usernameWrapper.getEditText().getText().toString();
        String password = passwordWrapper.getEditText().getText().toString();

        String phone = phoneWrapper.getEditText().getText().toString();

        // TODO: Implement your own signup logic here.

        signUp(username, phone, password);

    }
    public void onSignupSuccess() {
        sign_btn.setEnabled(true);
        setResult(RESULT_OK, null);
        progressDialog.dismiss();
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onSignupFailed(String msg) {
        Toast.makeText(getBaseContext(), "Sign failed:"+msg, Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
        new AlertDialog.Builder(this)
                .setTitle("注冊失败")
                .setMessage("请重新注冊")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
        sign_btn.setEnabled(true);
    }

    boolean runningThree=false;
    public void sendCode(String phone) {
        String url = VolleyUtil.url_prefix+"sendMsg?phone=" + phone;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, response.toString());
                            code=response.getString("verify_code");
                            Toast.makeText(SignActivity.this,"验证码已发送",Toast.LENGTH_LONG).show();
//                            getCode.setClickable(false);

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

    //use of Handler
    //messge info
    private static final int UPDATE_MY_TV = 1;
    Message message = null;

    //Handler UI modification
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case UPDATE_MY_TV:
                    String currentTime = (String)msg.obj;
                    getCode.setText("Current Time: " + currentTime);
                    break;
            }
        }
    };


    private CountDownTimer downTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long l) {
            runningThree = true;
            getCode.setText((l / 1000) + "秒");
        }

        @Override
        public void onFinish() {
            runningThree = false;
            getCode.setText("重新发送");
            getCode.setEnabled(true);
        }
    };
}
