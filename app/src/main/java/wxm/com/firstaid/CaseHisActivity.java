package wxm.com.firstaid;

import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import wxm.com.firstaid.adapter.RecyclerviewAdapter;
import wxm.com.firstaid.module.Record;
import wxm.com.firstaid.util.DividerItemDecoration;
import wxm.com.firstaid.util.VolleyUtil;

public class CaseHisActivity extends AppCompatActivity {
    @BindView(R.id.recyclerview_list_slow_i)
    RecyclerView slow_list;
    @BindView(R.id.recyclerview_list_fast_i)
    RecyclerView fast_list;
    @BindView(R.id.add_fast_ill)
    ImageView add_f_ill;
    @BindView(R.id.add_slow_ill)
    ImageView add_s_ill;
    @BindView(R.id.toolbar_title)
    TextView title;
    private List<Record> records_slow;
    private List<Record> records_fast;
    private static String TAG="Case";
    private String user_id=MyApplication.getInstnce().getUser().getId();
    private void init() {

        String url1 = VolleyUtil.url_prefix+"ChronicList?user_id="+user_id;
        String url2 = VolleyUtil.url_prefix+"SuddenIllList?user_id="+user_id;
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(url1,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "response:" + response.toString());
                            Log.d(TAG, response.toString());
                            records_slow = new Gson().fromJson(response.toString(),
                                    new TypeToken<List<Record>>() {
                                    }.getType());
//                            setupRecyclerView(recyclerView);
                        adapter2= new RecyclerviewAdapter(true,records_slow);
                        slow_list.setAdapter(adapter2);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    // HTTP Status Code: 401 Unauthorized
                    Log.d(TAG, "Error: " + error.getMessage() + "network" + error.networkResponse.toString() +
                            "statusCode" + error.networkResponse.statusCode);
                }
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstnce().addToRequestQueue(jsonObjReq,TAG);

        JsonArrayRequest jsonObjReq2 = new JsonArrayRequest(url2,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "response:" + response.toString());
                        Log.d(TAG, response.toString());
                        records_fast = new Gson().fromJson(response.toString(),
                                new TypeToken<List<Record>>() {
                                }.getType());
//                            setupRecyclerView(recyclerView);
//                        setListView();
                        adapter1 = new RecyclerviewAdapter(false,records_fast);
                        fast_list.setAdapter(adapter1);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    // HTTP Status Code: 401 Unauthorized
                    Log.d(TAG, "Error: " + error.getMessage() + "network" + error.networkResponse.toString() +
                            "statusCode" + error.networkResponse.statusCode);
                }
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "Error: " + error.getMessage());
            }
        });

        MyApplication.getInstnce().addToRequestQueue(jsonObjReq2,TAG);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_his);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
        title.setText("病史記錄");
        final boolean is_slow_ill=true;
        add_s_ill.setClickable(true);
        add_f_ill.setClickable(true);
        add_f_ill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog(!is_slow_ill);
            }
        });

        add_s_ill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog(is_slow_ill);
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fast_list.setLayoutManager(linearLayoutManager);
        fast_list.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
        fast_list.setItemAnimator(new FadeInLeftAnimator());


        slow_list.setLayoutManager(new LinearLayoutManager(this));
        slow_list.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
        slow_list.setItemAnimator(new FadeInLeftAnimator());

        init();


    }

    RecyclerviewAdapter adapter1;
    RecyclerviewAdapter adapter2;
    public void setListView(){


    }

    private AlertDialog.Builder alertDialog;
    private View view;
    private void initDialog(final boolean ill) {
        alertDialog = new AlertDialog.Builder(this);
        view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        alertDialog.setView(view);
        final TextInputLayout addWrapper=(TextInputLayout) view.findViewById(R.id.addWrapper);
        addWrapper.getEditText().setHint("添加疾病");
        alertDialog.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String illName=addWrapper.getEditText().getText().toString();
                if (illName.isEmpty()){
                    addWrapper.setError("empty");
                }else {
                    addWrapper.setError(null);
                    if (ill){
//                        records_slow.add(new Record(illName));
                        add_ill(ill,user_id,illName);
                        adapter2.addItem(0,illName);
//                        adapter1.notifyItemInserted(0);
//                        slow_list.scrollToPosition(0);
//                        adapter1.notifyItemInserted(records_slow.size() - 1);
//                        adapter1.notifyDataSetChanged();
                    }
                    else {
                        add_ill(ill,user_id,illName);
                        adapter1.addItem(0,illName);
//                        fast_list.scrollToPosition(0);
//                        adapter2.notifyItemChanged(records_fast.size()-1);
//                        adapter2.notifyDataSetChanged();
                    }
                }

            }
        });
        alertDialog.show();

    }

    public void add_ill(boolean is_slow,String id,String content){
        try {
            content= URLEncoder.encode(content, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url;
        if (is_slow){
         url    = VolleyUtil.url_prefix+"addChronic?user_id="+id+"&content="+content;

        }else {
             url = VolleyUtil.url_prefix+"addSuddenIll?user_id="+id+"&content="+content;

        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("add", response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("add", "Error: " + error.getMessage());
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