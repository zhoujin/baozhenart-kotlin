package com.markjin.artmall.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.markjin.artmall.BaseActivity;
import com.markjin.artmall.R;
import com.markjin.artmall.utils.StringUtil;
import com.markjin.artmall.view.Loadlayout;


/**
 * web view Activity(需要传Bundle web_url参数)
 */
public class WebviewActivity extends BaseActivity implements View.OnClickListener {

    private WebView webView;
    private String url;
    private String web_extras;
    private TextView tv_top_middle;

    private Loadlayout loadlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
    }


    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    private void initView() {
        loadlayout = new Loadlayout(this, R.style.loadingDialog);
        url = getIntent().getExtras().getString("web_url", "");
        if (StringUtil.isEmpty(url)) {
            url = "http://www.baozhenart.com/";
        } else if (url.startsWith("www.")) {
            url = "http://" + url;
        }

        findViewById(R.id.iv_left).setOnClickListener(this);
        tv_top_middle = findViewById(R.id.tv_title);
        webView = findViewById(R.id.webview);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setUserAgentString(settings.getUserAgentString() + "bzapp");
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
        }
        if (!StringUtil.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            webView.getClass().getMethod("onResume").invoke(webView, (Object[]) null);
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_left) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finishActivity(this);
            }
        }
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (loadlayout != null) {
                loadlayout.show();
            }
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (loadlayout != null && loadlayout.isShowing()) {
                loadlayout.dismiss();
            }
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (!StringUtil.isEmpty(title)) {
                tv_top_middle.setText(title);
            }
            super.onReceivedTitle(view, title);

        }
    }
}