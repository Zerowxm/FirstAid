package wxm.com.firstaid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import wxm.com.firstaid.util.VolleyUtil;

public class FindPW extends AppCompatActivity {

    @BindView(R.id.codeWrapper)
    TextInputLayout codeWrapper;
    @BindView(R.id.phoneWrapper)
    TextInputLayout phoneWrapper;
    @BindView(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;
    @BindView(R.id.repasswordWrapper)
    TextInputLayout repasswordWrapper;
    @BindView(R.id.toolbar_title)
    TextView textView;

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
        textView.setText("重置密碼");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);
        ButterKnife.bind(this);
        setToolbar();
    }
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    String code="0";
    public void sendCode(String phone) {
        String url = VolleyUtil.url_prefix+"sendMsg?phone=" + phone;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            code=response.getString("verify_code");
                            Toast.makeText(FindPW.this,"验证码已发送",Toast.LENGTH_LONG);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @BindView(R.id.get_code)
    TextView getCode;
    boolean runningThree=false;
    @OnClick(R.id.get_code)
    public void getCode(){
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

    @OnClick(R.id.set_pw)
    public void set_pw(){
        validate();
    }

    void set_pw(final String phone, final String password){
        String url = VolleyUtil.url_prefix+"RetPassword?phone="+phone+"&password="+password;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status=response.getString("status");
                            String msg=response.getString("msg");
                            if ("1".equals(status)){
                                finish();
                            }
                            else {
                                Toast.makeText(FindPW.this,"重置失敗",Toast.LENGTH_LONG).show();
                            }
                            } catch (JSONException e1) {
                            e1.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MyApplication.getInstnce().addToRequestQueue(jsonObjReq);

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
            if (password.equals(repasswordWrapper.getEditText().getText().toString())){
                repasswordWrapper.setError(null);
            }else {
                repasswordWrapper.setError("兩次密碼不一致");
                valid=false;
            }
        }
        if (!code.endsWith(codeWrapper.getEditText().getText().toString())){
            valid=false;
        }else {
            codeWrapper.setError(null);
        }
        if (valid){
            set_pw(phone,password);
        }
        return valid;
    }

}
