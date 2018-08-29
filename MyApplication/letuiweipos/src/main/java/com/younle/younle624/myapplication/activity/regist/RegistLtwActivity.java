package com.younle.younle624.myapplication.activity.regist;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.younle.younle624.myapplication.R;

public class RegistLtwActivity extends Activity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_ltw);
        webView = (WebView)findViewById(R.id.wbview);
        initWebView();
        webView.loadUrl("http://tui.younle.com/");
//        webView.loadUrl("http://tui.younle.com/Index/user_register.shtml");
//        webView.loadUrl("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=47018152_dg&wd=webview%E7%9A%84%E4%BD%BF%E7%94%A8&oq=%E4%B9%90%E6%8E%A8%E5%BE%AE&rsv_pq=c545e2880000a89c&rsv_t=b5beViy6J9WP8SR3eKsReRkDtGvHT0VskNCGkyxImu00vfmLB3U0ZUHEpWvNAxWqQtI&rqlang=cn&rsv_enter=0&inputT=14286&rsv_sug3=365&rsv_sug1=295&rsv_sug7=100&bs=%E4%B9%90%E6%8E%A8%E5%BE%AE");
    }

    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);//支持缩放
        settings.setBuiltInZoomControls(true);  //启用内置缩放装置
        settings.setJavaScriptEnabled(true);    //启用JS脚本
        settings.setUseWideViewPort(true);// 这个很关键
        settings.setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient() {
            //当点击链接时,希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);  //加载新的url
                return true;    //返回true,代表事件已处理,事件流到此终止
            }
        });

        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        webView.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            //当WebView进度改变时更新窗口进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //Activity的进度范围在0到10000之间,所以这里要乘以100
                RegistLtwActivity.this.setProgress(newProgress * 100);
            }
        });
    }
}
