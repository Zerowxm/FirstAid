package wxm.com.firstaid;


import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wxm.com.firstaid.util.TipHelper;
import wxm.com.firstaid.util.VolleyUtil;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaveFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String own_url;
    private String mParam2;


    public SaveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SaveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SaveFragment newInstance(String name,String param1) {
        SaveFragment fragment = new SaveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2,name);
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    private String other_url;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            own_url = getArguments().getString(ARG_PARAM1);
            mParam2=getArguments().getString(ARG_PARAM2);
            other_url=VolleyUtil.url_prefix+"raiseOtherIndex?user_id="+ own_url;
            own_url = VolleyUtil.url_prefix+"raiseOwnIndex?user_id="+ own_url;
        }
    }
    @BindView(R.id.toolbar_title)
    TextView textView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar_save)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar_save)
    Toolbar toolbar_save;
    @BindView(R.id.toolbar_save_title)
    TextView textView_save;
    protected void setToolBar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        toolbar.setTitle("");
        textView.setText(mParam2);
        textView_save.setText(mParam2);
        activity.setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ActionBarDrawerToggle toggle1 = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar_save, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle1);
        toggle1.syncState();
    }
    @BindView(R.id.webview)
    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_save, container, false);
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.white));
        }

        setToolBar();
        setSeekBar();
        setImageButton();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setLoadWithOverviewMode(true);

        webView.setScrollbarFadingEnabled(true);
        setWebView();


        webView.getSettings().setGeolocationDatabasePath( getActivity().getFilesDir().getPath() );
        webView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        startWebView(own_url);

        TipHelper.createAlarm(getActivity());

        return v;
    }
    @BindView(R.id.save_type)
    SeekBar seekBar;

    public void setSeekBar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress()<50){
                    seekBar.setProgress(0);
                    if (!webView.getUrl().equals(own_url)){
                        webView.loadUrl(own_url);

                    }
                }else {
                    seekBar.setProgress(100);
                    if (!webView.getUrl().equals(other_url)){
                        webView.loadUrl(other_url);
                    }
                }
            }
        });
    }

    public void setWebView(){
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
        Log.d("size",""+width+" "+height);
        if (width>1200){
            webView.setInitialScale(310);
        }else if(width > 1000){
            webView.setInitialScale(290);
        }
        else if(width > 800){
            webView.setInitialScale(250);
        }
        else if(width > 650)
        {
            this.webView.setInitialScale(180);
        }else if(width > 520)
        {
            this.webView.setInitialScale(160);
        }else if(width > 450)
        {
            this.webView.setInitialScale(140);
        }else if(width > 300)
        {
            this.webView.setInitialScale(120);
        }else
        {
            this.webView.setInitialScale(100);
        }

    }

    @BindView(R.id.listen)
    ImageButton imageButton;
    @BindView(R.id.save_panel)
    LinearLayout relativeLayout;

    public void setImageButton(){
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!TipHelper.is_long){
//                    TipHelper.is_long=true;
//                    TipHelper.flag= Notification.FLAG_INSISTENT;
//                }else {
//                    TipHelper.is_long=false;
//                    TipHelper.flag=Notification.FLAG_AUTO_CANCEL;
//                }
//                TipHelper.PlaySound(getActivity());
                if (!TipHelper.mMediaPlayer.isPlaying()){
                    TipHelper.startAlarm();
                }else {
                    TipHelper.stopAlarm();
                }

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    /*    if (TipHelper.flag==Notification.FLAG_INSISTENT){
            TipHelper.flag=Notification.FLAG_AUTO_CANCEL;
            TipHelper.PlaySound(getActivity());
        }*/
        TipHelper.stopAlarm();


    }

    ProgressDialog progressDialog;
    @BindView(R.id.appbar_red)
    AppBarLayout appBarLayout_red;
    private void startWebView(String url) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("URL",url);
                progressDialog.show();
                if (url.contains("orderStatus")){
                    appBarLayout.setVisibility(View.INVISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                    appBarLayout_red.setVisibility(View.VISIBLE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getActivity().getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.primary_dark));
                    }


                }else {
                    appBarLayout.setVisibility(View.VISIBLE);
                    appBarLayout_red.setVisibility(View.INVISIBLE);
//                    TipHelper.flag=Notification.FLAG_AUTO_CANCEL;
//                    TipHelper.PlaySound(getActivity());
                    TipHelper.stopAlarm();
                    relativeLayout.setVisibility(View.VISIBLE);
                    imageButton.setVisibility(View.GONE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getActivity().getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.white));
                    }
                }
                view.loadUrl(url);

               /* webView.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK
                                && event.getAction() == MotionEvent.ACTION_UP
                                && webView.canGoBack()) {
                            handler.sendEmptyMessage(1);
                            return true;
                        }

                        return false;
                    }
                });*/
                return true;
            }

            private Handler handler = new Handler(){
                @Override
                public void handleMessage(Message message) {
                    switch (message.what) {
                        case 1:{
                            webViewGoBack();
                        }break;
                    }
                }
            };

            private void webViewGoBack(){
                webView.goBack();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    view.getSettings().setUseWideViewPort(true);


                }

                if (url.contains("orderStatus")){
                    relativeLayout.setVisibility(View.GONE);
                    imageButton.setVisibility(View.VISIBLE);

                }else {
                    relativeLayout.setVisibility(View.VISIBLE);
                    imageButton.setVisibility(View.GONE);
                }
            }

        });
        webView.loadUrl(url);
    }




    @Override
    public void onPrepareOptionsMenu(final Menu menu) {

        super.onPrepareOptionsMenu(menu);

        menu.clear();//This removes all menu items (no need to know the id of each of them)
    }

}
