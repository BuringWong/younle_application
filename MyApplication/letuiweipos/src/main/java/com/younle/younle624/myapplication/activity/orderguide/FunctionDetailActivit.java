package com.younle.younle624.myapplication.activity.orderguide;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.view.SelfLinearLayout;

public class FunctionDetailActivit extends Activity {
    private WebView webView;
    private NetWorks netWorks;
    private ImageView iv_jiazai_filure;
    private TextView tv_loading;
    private LinearLayout ll_cancel;
    private TextView tv_title;
    private SelfLinearLayout ll_loading;
    private final String url="http://tui.younle.com/Index/menDianPai.shtml";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_detail);
        initView();
        initWebView();
        webView.loadUrl(url);
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        webView = (WebView)findViewById(R.id.webView);
        iv_jiazai_filure = (ImageView)findViewById(R.id.iv_jiazai_filure);
        tv_loading = (TextView)findViewById(R.id.tv_loading);
        ll_cancel = (LinearLayout)findViewById(R.id.ll_cancel);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("门店派功能介绍");
        ll_loading = (SelfLinearLayout)findViewById(R.id.ll_loading);

    }
    /**
     * 初始化webView的一些设置
     */
    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);//支持缩放
        settings.setBuiltInZoomControls(true);  //启用内置缩放装置
        settings.setJavaScriptEnabled(true);    //启用JS脚本
        settings.setUseWideViewPort(true);// 这个很关键
        settings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtils.Log("页面加载完成了");
                super.onPageFinished(view, url);
                ll_loading.setVisibility(View.GONE);
            }
            //重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
