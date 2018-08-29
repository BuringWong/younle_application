package com.younle.younle624.myapplication.activity.takeoutfood;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.waimai.FinisTop;
import com.younle.younle624.myapplication.utils.ClicKUtils;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.SelfLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

public class BindMtActivity extends Activity implements NetWorks.OnNetCallBack, View.OnClickListener, SelfLinearLayout.ClickToReload {
    private WebView webView;
    private SelfLinearLayout ll_loading;
    private NetWorks netWorks;
    private ImageView iv_jiazai_filure;
    private ProgressBar pb_loading;
    private TextView tv_loading;
    /**
     * action=1是美团更换门店的标识，需要获取新的epoiid，然后在联网加载
     */
    private int action;
    private LinearLayout ll_cancel;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_mt);
        Utils.initToolBarColor(this, "#3f88ce");
        EventBus.getDefault().register(this);
        action = getIntent().getIntExtra("action", -1);
        netWorks = new NetWorks(this);
        initView();
        setListener();
        initWebView();
        getUrl();
    }
    private void initView() {
        webView = (WebView)findViewById(R.id.webView);
        ll_loading = (SelfLinearLayout)findViewById(R.id.ll_loading);
        iv_jiazai_filure = (ImageView)findViewById(R.id.iv_jiazai_filure);
        pb_loading = (ProgressBar)findViewById(R.id.pb_loading);
        tv_loading = (TextView)findViewById(R.id.tv_loading);
        ll_cancel = (LinearLayout)findViewById(R.id.ll_cancel);
        tv_title = (TextView)findViewById(R.id.tv_title);
        if(action==2) {
            tv_title.setText("绑定饿了么账户");
        }else {
            tv_title.setText("绑定美团账户");
        }
    }
    private void setListener() {
        ll_loading.setClickToReload(this);
        ll_cancel.setOnClickListener(this);
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
        settings.setDomStorageEnabled(true);
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

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
    }
    /**
     * 获取epoid
     */
    private void getUrl() {
        if(action==1) {
            netWorks.BindNewEpoiId(this,1);
        }else if(action==0) {
            netWorks.getEpoiBindAccount(this,0);
        }else if(action==2) {
            netWorks.getElmUrl(this,2);
        }
    }
    /**
     * 获取epoiid失败
     * @param e
     * @param flag
     */
    @Override
    public void onError(Exception e, int flag) {
        LogUtils.Log("获取epoiid异常：" + e.toString());
        netError();
    }

    private void netError() {
        pb_loading.setVisibility(View.GONE);
        tv_loading.setText("请检查网络后，点击屏幕重新加载！");
        iv_jiazai_filure.setVisibility(View.VISIBLE);
    }

    /**
     * 获取epoiid成功
     * @param response
     * @param flag
     */
    @Override
    public void onResonse(String response, int flag) {
        LogUtils.Log("response==" + response);

        try {
            JSONObject jsonObject=new JSONObject(response);
            int code = jsonObject.getInt("code");
            String data = jsonObject.getString("data");
            if(code==200) {
                String url="";
                if(action==2) {
                    url=data;
//                    LogUtils.Log("url=="+url);
                }else {
                    Constant.EPOIID=data;
                    ll_loading.setVisibility(View.GONE);
                    //启动美团绑定页面
                    url="https://open-erp.meituan.com/storemap" +
                            "?developerId=100316" +
                            "&businessId=2" +
                            "&ePoiId=" + Constant.EPOIID+
                            "&signKey=1gpty5so35bqc3b5" +
                            "&callbackUrl=http://waimai.younle.com/index/Api/mei_redirect";
                }
                LogUtils.Log("URL:"+url);
                webView.loadUrl(url);
            }else {
                netError();
            }
        } catch (JSONException e) {
            LogUtils.Log("e==" + e.toString());
            netError();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.ll_cancel) {
            finish();
        }
    }
    /**
     * 绑定成功
     */
    @Subscribe
    public void onEventMainThread(FinisTop finisTop) {
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void ClickToReload() {
        if(ClicKUtils.isFastDoubleClick()) {
            return;
        }else {
            getUrl();
        }
    }
}
