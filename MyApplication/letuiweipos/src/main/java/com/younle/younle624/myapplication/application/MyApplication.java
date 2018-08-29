package com.younle.younle624.myapplication.application;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;

import com.igexin.sdk.PushManager;
import com.younle.younle624.myapplication.activity.LoginActivity;
import com.younle.younle624.myapplication.myservice.waimai.GTPushService;
import com.younle.younle624.myapplication.myservice.waimai.InitGTService;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.xutils.BuildConfig;
import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by 我是奋斗 on 2016/5/11.
 * 微信/e-mail:tt090423@126.com
 */
public class MyApplication extends Application implements Thread.UncaughtExceptionHandler{

    private static MyApplication instance;
    private DbManager.DaoConfig config;

    @Override
    public void onCreate() {
        super.onCreate();
        Configuration configuration = getResources().getConfiguration();
        configuration.keyboard=Configuration.KEYBOARD_NOKEYS;
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        /**
         * 个推推送
         */
        PushManager.getInstance().initialize(this, InitGTService.class);
        PushManager.getInstance().registerPushIntentService(this, GTPushService.class);

//        Thread.setDefaultUncaughtExceptionHandler(this);
        initDao();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(5000L, TimeUnit.MILLISECONDS)//单位是毫秒，链接超时时间
                .readTimeout(5000L, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false)
                .build();

        OkHttpUtils.initClient(okHttpClient);

    }

    private void initDao() {

        config = new DbManager.DaoConfig();
        //每一个config对象描述一个数据库，多个数据库就创建多个config对象
        //这里作为示例，我们只创建一个名字为dbName版本号为1的数据库
        config.setDbName("print_devices").setDbVersion(1);//设置数据库版本号
        //config.setDbDir(File file);
        // 该语句会将数据库文件保存在你想存储的地方
        //如果不设置则默认存储在应用程序目录下/database/dbName.db
        config.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                //用来监听数据库是否升级
                //如果当前数据库版本号比已存在的数据库版本号高则执行此片段
                //用途：软件升级之后在第一次运行时执行一些必要的初始化操作
            }
        });
        config.setTableCreateListener(new DbManager.TableCreateListener() {
            @Override
            public void onTableCreated(DbManager db, TableEntity table) {
            }
        });
        config.setAllowTransaction(true);
        //是否允许开启事务
    }

    public DbManager.DaoConfig getDaoConfig() {
        if (config  != null){
            return config;
        }else{
            initDao();
            return config;
        }
    }

//    public static void setInstance() {
//        if (instance != null){
//            MyApplication.instance = new MyApplication();
//        }
//    }

    /**
     * MyApplication
     * @return
     */
    public static MyApplication getInstance() {
        if (instance == null){
            MyApplication.instance = new MyApplication();
        }
        return instance;
    }
    /**
     * 捕获异常
     * @param thread
     * @param throwable
     */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

//        Utils.showToast(this,"很抱歉程序发出了异常，我们会及时解决，程序即将重启");
        Intent intent=new Intent(MyApplication.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        LogUtils.Log("异常：" + throwable.toString());
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
