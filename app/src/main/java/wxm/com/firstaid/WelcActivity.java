package wxm.com.firstaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcActivity extends AppCompatActivity {
    @BindView(R.id.to_login)
    TextView login;
    @BindView(R.id.to_sign)
    TextView sign;
    private static int LOGIN=0x1;
    private static int SIGN=0x2;
    public static WelcActivity instance=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welc);
        ButterKnife.bind(this);
        instance = this;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(WelcActivity.this,LoginActivity.class),LOGIN);
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(WelcActivity.this,SignActivity.class),SIGN);
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("result", "drawer");
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                this.finish();
                startActivity(new Intent(this,MainActivity.class));
            }

    }
}
