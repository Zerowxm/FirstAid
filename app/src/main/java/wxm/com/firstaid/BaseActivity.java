package wxm.com.firstaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import wxm.com.firstaid.util.PrefUtils;

/**
 * Created by Zero on 12/9/2016.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Log",""+PrefUtils.isFirstUse(this));
      /*  if (PrefUtils.isFirstUse(this)){
            Intent intent=new Intent(this,WelcActivity.class);
            finish();
            startActivity(intent);
        }
        else {
            MyApplication.getInstnce().login();
        }*/
    }
}
