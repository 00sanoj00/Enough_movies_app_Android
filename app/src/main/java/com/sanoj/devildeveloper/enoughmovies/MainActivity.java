package com.sanoj.devildeveloper.enoughmovies;

import static android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.monstertechno.adblocker.AdBlockerWebView;
import com.monstertechno.adblocker.util.AdBlocker;
import com.sanoj.devildeveloper.enoughmovies.Config.Config;
import com.sanoj.devildeveloper.enoughmovies.Utility.DownloadFileUtility;
import com.sanoj.devildeveloper.enoughmovies.Utility.IntentUtility;
import com.sanoj.devildeveloper.enoughmovies.Utility.PermissionUtility;
import com.sanoj.devildeveloper.enoughmovies.webview.AdvancedWebView;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import org.alfonz.utility.DownloadUtility;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements AdvancedWebView.Listener{

    private static final String TAG ="MovieDB";
    private AdvancedWebView webView;
    private Toolbar toolbar;
    private ProgressBar pbar;
    private View parentLayout;
    private Button mPlayBtn;
    private CircleRefreshLayout mRefreshLayout;
    private String mStrimingID;
    private AnimatorSet mAnimationSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        webView = (AdvancedWebView) findViewById(R.id.webView1);
        new AdBlockerWebView.init(this).initializeWebView(webView);
        pbar = findViewById(R.id.awv_progressBar);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mPlayBtn = findViewById(R.id.play_btn);



        /**loading animation*/
        mPlayBtn.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.pbtn));
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(mPlayBtn, "alpha", 1f, .3f);
        fadeOut.setDuration(1600);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(mPlayBtn, "alpha", .3f, 1f);
        fadeIn.setDuration(1600);
        mAnimationSet = new AnimatorSet();
        mAnimationSet.play(fadeIn).after(fadeOut);

        /**loading animation */

        /**statusbar colour */
        try {
            getWindow().getDecorView().setSystemUiVisibility(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS |
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } catch (Exception io) {
            Log.d(TAG, "onCreate: "+io);
        }
        /**statusbar colour */

        /**toolbar partget */
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Enough movies ::: By Sanoj");

        /**toolbar partget */
        /**Cookie manager setting*/
        try {
            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
        } catch (Exception u) {
            Log.d(TAG, "onCreate: ");
        }
        /**Cookie manager setting*/

        mRefreshLayout.setOnRefreshListener(
                new CircleRefreshLayout.OnCircleRefreshListener() {
                    @Override
                    public void refreshing() {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String webUrl = webView.getUrl();
                                if (!isNetworkAvailable()) {
                                    webView.loadUrl(Config.mErrorURL);
                                } else if (isNetworkAvailable()) {
                                    if (webUrl.equals(Config.mErrorURL)) {
                                        if (webView.canGoBack()) {
                                            webView.goBack();
                                        } else {
                                            webView.loadUrl(Config.mMainURL);
                                        }
                                    } else {
                                        webView.reload();
                                    }
                                }


                            }
                        }, 1500);

                    }

                    @Override
                    public void completeRefresh() {
                        //
                    }
                });

        /**your webview aria */
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setSaveFormData(true);
        webView.setCookiesEnabled(true);
        webView.getSettings().setAppCachePath(MainActivity.this.getCacheDir().getAbsolutePath());
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setBuiltInZoomControls(false);



        webView.setListener(MainActivity.this, this);
        webView.setGeolocationEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);


        webView.setWebChromeClient(new ChromeClient());
        webView.setWebViewClient(new MyWebViewClient());


        if (!isNetworkAvailable()) {
            webView.loadUrl(Config.mErrorURL);
        } else if (isNetworkAvailable()) {
            if (savedInstanceState == null)
            {
                webView.loadUrl(Config.mMainURL);
            }
            //webView.loadUrl(Config.mMainURL);
        }
        /**your webview aria */

        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(Config.mStrimingURl+mStrimingID);
            }
        });

    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        Log.d(TAG, "onPageStarted: ");
        mPlayBtn.setVisibility(View.GONE);
    }

    @Override
    public void onPageFinished(String url) {
        try {
            mRefreshLayout.finishRefreshing();

            String mainurl = url;
            String newString = mainurl.replace("/title/", "?title=");
            String fixString = newString.replace("/?","&");
            Uri uri = Uri.parse(fixString);
            String limit = uri.getQueryParameter("title");
            String chektital = limit.substring(0,2);

            if (chektital.equals("tt")){
                Log.d(TAG, "onPageFinished  "+limit);
                mStrimingID = limit+".html";
                mPlayBtn.setVisibility(View.VISIBLE);
            }else{
                Log.d(TAG, "***** this is not movies or tv show");
                mPlayBtn.setVisibility(View.GONE);
            }




        } catch (Exception o) {
            //fix
        }

        if (webView.getUrl().equals(Config.mStrimingURl+mStrimingID)){
            mPlayBtn.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        Log.d(TAG, "onPageError: ");
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
        if (PermissionUtility.checkPermissionWriteExternalStorage(MainActivity.this)) {
            Toast.makeText(MainActivity.this, R.string.main_downloading, Toast.LENGTH_LONG).show();
            try {
                DownloadUtility.downloadFile(MainActivity.this, url, DownloadFileUtility.getFileName(url));
            } catch (Exception o) {
                Snackbar.make(parentLayout, "Can only download HTTP/HTTPS \n blob Url Not Support", Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                        .show();
            }
        }
    }
    private boolean isLinkExternal(String url) {
        for (String rule : Config.LINKS_OPENED_IN_EXTERNAL_BROWSER) {
            if (url.contains(rule)) return true;
        }
        return false;
    }
    private boolean isLinkInternal(String url) {
        for (String rule : Config.LINKS_OPENED_IN_INTERNAL_WEBVIEW) {
            if (url.contains(rule)) return true;
        }
        return false;
    }
    @Override
    public void onExternalPageRequest(String url) {
        Log.d(TAG, "onExternalPageRequest: ");
    }
    private class ChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int progress) {

            pbar.setProgress(progress);
            if (progress == 100) {

                pbar.setVisibility(View.INVISIBLE);
            } else {
                pbar.setVisibility(View.VISIBLE);
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onPermissionRequest(final PermissionRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                request.grant(request.getResources());
            }
        }
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        ChromeClient() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
    private class MyWebViewClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

            return AdBlockerWebView.blockAds(view,url) ? AdBlocker.createEmptyResource() :
                    super.shouldInterceptRequest(view, url);

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (webView.getUrl().equals(Config.mStrimingURl+mStrimingID)){
                mPlayBtn.setVisibility(View.GONE);
            }
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (DownloadFileUtility.isDownloadableFile(url)) {
                if (PermissionUtility.checkPermissionWriteExternalStorage(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, R.string.main_downloading, Toast.LENGTH_LONG).show();
                    DownloadUtility.downloadFile(MainActivity.this, url, DownloadFileUtility.getFileName(url));
                    return true;
                }
                return true;
            } else if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                System.out.println(url);
                boolean external = isLinkExternal(url);
                boolean internal = isLinkInternal(url);
                if (!external && !internal) {
                    external = Config.OPEN_LINKS_IN_EXTERNAL_BROWSER;
                }

                if (external) {
                    IntentUtility.startWebActivity(MainActivity.this, url);
                    return true;
                } else {
                    return false;
                }
            } else if (url != null && url.startsWith("file://")) {
                return false;
            } else {
                return IntentUtility.startIntentActivity(MainActivity.this, url);
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mAnimationSet.start();
            if (url.equals(Config.mStrimingURl+mStrimingID)){
                toolbar.setTitle("This App Made in Sri lanka by Sanoj");
            }else if(url.equals(Config.mMainURL)){
                toolbar.setTitle("This App Made in Sri lanka by Sanoj");
            }else{
                toolbar.setTitle(view.getTitle()+" [ By Sanoj ]");
            }

            if (webView.getUrl().equals(Config.mStrimingURl+mStrimingID)){
                mPlayBtn.setVisibility(View.GONE);
            }

            super.onPageFinished(view, url);
        }
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            webView.loadUrl(Config.mErrorURL);
        }


    }
    private static long back_pressed;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        if (webView.getUrl().equals(Config.mStrimingURl+mStrimingID)){
                            if (back_pressed + 2000 > System.currentTimeMillis()){
                                webView.goBack();
                            }
                            else{
                                Toast.makeText(getBaseContext(), "Press once again to exit Media player", Toast.LENGTH_SHORT).show();
                                back_pressed = System.currentTimeMillis();
                            }
                        }else{
                            webView.goBack();
                        }

                    } else {
                        //exitdialog();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

}