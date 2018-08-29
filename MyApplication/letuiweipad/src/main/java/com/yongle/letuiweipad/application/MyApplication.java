package com.yongle.letuiweipad.application;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yongle.letuiweipad.dao.DaoMaster;
import com.yongle.letuiweipad.dao.DaoSession;
import com.zhy.http.okhttp.OkHttpUtils;

import org.xutils.BuildConfig;
import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/9/15.
 */

public class MyApplication  extends Application{
    private static MyApplication instance;
    private DbManager.DaoConfig config;
    private DaoSession daoSession;
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        mContext=getApplicationContext();
        initOkhttp();
        initXutils();
        initDao();
        initGreenDao();
    }

    public static Context getContext() {
        return mContext;
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper openHelper=new DaoMaster.DevOpenHelper(this,"store_goods.db");
        SQLiteDatabase database = openHelper.getWritableDatabase();
        DaoMaster daoMaster=new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    private void initXutils() {
        x.Ext.init(this);
    }

    private void initOkhttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5000L, TimeUnit.MILLISECONDS)//单位是毫秒，链接超时时间
                .readTimeout(5000L, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
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

}
