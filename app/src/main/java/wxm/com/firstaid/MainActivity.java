package wxm.com.firstaid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wxm.com.firstaid.module.User;
import wxm.com.firstaid.util.LocationUtils;
import wxm.com.firstaid.util.PrefUtils;
import wxm.com.firstaid.util.VolleyUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PrefUtils.isFirstUse(this)){
            Intent intent=new Intent(this,WelcActivity.class);
            finish();
            startActivity(intent);
        }
        else {
            MyApplication.getInstnce().login();
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            user=MyApplication.getInstnce().getUser();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            NavigationView navigationViewBottom = (NavigationView) findViewById(R.id.navigation_drawer_bottom);
            navigationViewBottom.setNavigationItemSelectedListener(this);

            getInfo(user.getId());

        }


//        setLoc();

    }



    private void setupFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, SaveFragment.newInstance(user.getName(), user.getId())).commitAllowingStateLoss();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        item.setChecked(false);

        if (id == R.id.nav_ills) {
            startActivity(new Intent(this, CaseHisActivity.class));
            item.setChecked(false);
            item.setCheckable(false);
            // Handle the camera action
        } else if (id == R.id.nav_settings) {
            startActivityForResult(new Intent(this, Settings.class),1);
            item.setChecked(false);
            item.setCheckable(false);
        } else if (id == R.id.nav_save) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, HelpFragment.newInstance(user.getId())).commitAllowingStateLoss();

        } else if (id == R.id.nav_help) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, SaveFragment.newInstance(user.getName(), user.getId())).commitAllowingStateLoss();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    CircleImageView user_image;
    TextView name;

    private void setupNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        user_image = (CircleImageView) header.findViewById(R.id.user_photo);
        name = (TextView) header.findViewById(R.id.name);
        TextView ills=(TextView)header.findViewById(R.id.ills);
        Log.d("info_nav",user.toString());
        Picasso.with(this).load(user.getImage()).into(user_image);
        name.setText(user.getName());
        ills.setText(user.getRecord());
    }

    public void getInfo(String id){
        String url=VolleyUtil.url_prefix+"getInfoByUserId?user_id="+id;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("info_get", response.toString());
                        try {
                            MyApplication.getInstnce().getUser().setId(response.getString("user_id").toString());
                            MyApplication.getInstnce().getUser().setImage(response.getString("photo_url").toString());
                            MyApplication.getInstnce().getUser().setName(response.getString("username"));
                            MyApplication.getInstnce().getUser().setRecord(response.getString("disease_record").toString());
                            setupNavigationView();
                            setupFragment();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("info", "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstnce().addToRequestQueue(jsonObjReq);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("log","resume");
        String url=VolleyUtil.url_prefix+"getInfoByUserId?user_id="+user.getId();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("info_get", response.toString());
                        try {
                            MyApplication.getInstnce().getUser().setId(response.getString("user_id").toString());
                            MyApplication.getInstnce().getUser().setImage(response.getString("photo_url").toString());
                            MyApplication.getInstnce().getUser().setName(response.getString("username").toString());
                            MyApplication.getInstnce().getUser().setRecord(response.getString("disease_record").toString());
                            setupNavigationView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("info", "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstnce().addToRequestQueue(jsonObjReq);

        Fragment current= getSupportFragmentManager().findFragmentById(R.id.content);
        if (current instanceof SaveFragment){
        }
        /*View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment instanceof SaveFragment){
            ((SaveFragment)fragment).textView_save.setText(user.getName());
            ((SaveFragment)fragment).textView.setText(user.getName());
        }

    }

}
