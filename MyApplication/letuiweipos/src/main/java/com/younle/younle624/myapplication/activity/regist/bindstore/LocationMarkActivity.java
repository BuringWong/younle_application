package com.younle.younle624.myapplication.activity.regist.bindstore;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.SearchParam;
import com.tencent.lbssearch.object.result.SearchResultObject;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.map.geolocation.TencentPoi;
import com.tencent.mapsdk.raster.model.Circle;
import com.tencent.mapsdk.raster.model.CircleOptions;
import com.tencent.mapsdk.raster.model.GeoPoint;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.tencentmap.mapsdk.map.MapController;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.adapter.AroundAdapter;
import com.younle.younle624.myapplication.adapter.PoiAdapter;
import com.younle.younle624.myapplication.adapter.SearchAdapter;
import com.younle.younle624.myapplication.constant.Tencent;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.AroundPoi;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.SelfMapView;
import com.younle.younle624.myapplication.view.XListView;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 标记店铺位置的界面，腾讯地图
 */
public class LocationMarkActivity extends Activity {
    private TextView tv_title;
    private Button btn_setting_next;
    private Button btn_search;
    private EditText et_search_address;
    private LinearLayout ll_loading;
    private RelativeLayout rl_search;
    private ImageView iv_center;
    private ListView reference_list;
    private XListView xlv_search;
    private SelfMapView tecent_map;
    private MapController controller;
    private static final String AROUND = "around";
    private static final String SEARCH = "search";
    private TencentLocationRequest request;
    private TencentLocationManager tencentLocationManager;
    private TencentLocationListener locationListener;
    private String currentcity;
    private final Location mCenter = new Location("");
    private List<TencentPoi> poiList;
    private AroundAdapter aroundAdapter;
    private int poiIndex=1;
    private EventBus eventBus;
    private NetUtils netUtils;
    private PoiAdapter poiAdapter;
    private String inputAdd;
    private SearchParam param;
    private TencentSearch tencentSearch;
    private SearchResultObject searchResultObject;
    private SearchAdapter searchAdapter;
    private int pager_index=1;
    private boolean everSearch=false;
    private boolean loadMore=false;
    private boolean isFromNet=false;
    private List<AroundPoi.ResultBean.PoisBean> netPois;
    private List<SearchResultObject.SearchResultData> searchResultDatas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_mark);
        eventBus=EventBus.getDefault();
        eventBus.register(this);

        //1.初始化头部导航栏
        selfTitle();
        //2.初始化视图
        initView();
        //3.xlistview的基本配置
        initXlistView();
        //4.初始化腾讯地图
        initTencentMap();
        //5.初始化适配器
        initAdapter();
        //6.开始定位
        getMyLocation();
        //7.设置监听
        setListener();
    }
    /**
     * xlistview的配置
     */
    private void initXlistView() {
        xlv_search.setPullRefreshEnable(false);
        xlv_search.setPullLoadEnable(true);
        xlv_search.setAutoLoadEnable(false);
        xlv_search.setXListViewListener(new Xlistener());
//        xlv_search.setRefreshTime(getTime());
//        mAdapter = new ArrayAdapter<String>(this, R.layout.vw_list_item, items);
//        mListView.setAdapter(mAdapter);
    }

    /**
     * 下拉加载更多， 上拉刷新
     */
    class Xlistener implements XListView.IXListViewListener{

        @Override
        public void onRefresh() {

        }

        @Override
        public void onLoadMore() {
            loadMore=true;
            pager_index++;
            param.page_index(pager_index);
            tencentSearch.search(param, new SearchResponListener());
        }
    }


    private void setListener() {
        //搜索的点击监听
        btn_search.setOnClickListener(new SearchListener());
        //周边列表的点击
        reference_list.setOnItemClickListener(new ReferListItenListener());
        //搜索列表的item的点击监听
        xlv_search.setOnItemClickListener(new SearchItemOnClickListener());
        //地图拖动的监听
        tecent_map.setOnDragEvent(new SelfMapView.OnDragEvent() {
            @Override
            public void doSomeThing() {
                startAnimation();
                LatLng mapCenter = tecent_map.getMapCenter();
                reLocation(mapCenter);

            }
        });
        //位置拾取完成的button的监听
        btn_setting_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LocationMarkActivity.this, BindStoreSuccessActivity.class));
            }
        });
    }

    /**
     * 搜索列表的item的点击监听
     * 1.相对布局gone，
     * 2.获取经纬度值，联网请求poi
     * 3.设置更新referlist
     *
     */
    class SearchItemOnClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            rl_search.setVisibility(View.GONE);
            double lat=searchResultDatas.get(position-1).location.lat;
            double lng = searchResultDatas.get(position-1).location.lng;
            //移动地图中心点
            GeoPoint oflalo = Utils.oflalo(lat, lng);
            animateTo(oflalo,lat,lng);
            String url= UrlConstance.LBaseUrl+lat+","+lng+"&key="+ Tencent.KEY+"&get_poi="+poiIndex;

            netUtils = new NetUtils(LocationMarkActivity.this, AroundPoi.class, url);
        }
    }
    /**
     * 周边列表的item点击监听
     */
    class ReferListItenListener implements AdapterView.OnItemClickListener{
        //position从1开始
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            double lat;
            double lng;
            //1.改变对勾位置
            if(!isFromNet) {
                aroundAdapter.setPostiont(position - 1);
                aroundAdapter.notifyDataSetChanged();
                lat=poiList.get(position-1).getLatitude();
                lng=poiList.get(position-1).getLongitude();
            }else {
                poiAdapter.setPosition(position-1);
                poiAdapter.notifyDataSetChanged();
                lat=netPois.get(position-1).getLocation().getLat();
                lng=netPois.get(position-1).getLocation().getLng();
            }
            GeoPoint oflalo = Utils.oflalo(lat, lng);

            //2.移动地图中心
            animateTo(oflalo,lat,lng);
        }
    }

    /**
     * 搜搜索框的点击监听
     * 1.第一次点击搜索，成功后正常加载显示
     * 2.再次输入点击搜索，setdata，先隐藏lv,onsuccess时显示
     * 3.加载更多时，调用adddata
     *
     */
    class SearchListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //获取输入的内容
            inputAdd = et_search_address.getText().toString();
            if(!TextUtils.isEmpty(inputAdd)) {
                searchInput(inputAdd);
            }else {
                Utils.showToast(LocationMarkActivity.this, "请输入要搜索的地址的关键词！");
                return ;
            }
            //1.显示loading,显示整体
            if(xlv_search.getVisibility()==View.VISIBLE) {
                xlv_search.setVisibility(View.GONE);
            }
            rl_search.setVisibility(View.VISIBLE);
            ll_loading.setVisibility(View.VISIBLE);

        }
    }
    /**
     * 位置搜索
     * @param inputAdd
     */
    private void searchInput(String inputAdd) {

        SearchParam.Region r = new SearchParam.Region().poi(currentcity);
        param = new SearchParam().keyword(inputAdd).boundary(r);
        tencentSearch.search(param,new SearchResponListener());

    }

    /**
     * 关键字搜索的回调
     */
    class SearchResponListener implements HttpResponseListener{



        @Override
        public void onSuccess(int i, Header[] headers, BaseObject baseObject) {
            //1.loading消失，listview显示
            ll_loading.setVisibility(View.GONE);
            xlv_search.setVisibility(View.VISIBLE);
            searchResultObject = (SearchResultObject)baseObject;
            searchResultDatas = searchResultObject.data;

            if(!loadMore) {
                if(!everSearch) {//来自第一次搜索,
                    searchAdapter.setData(searchResultDatas);
                    xlv_search.setAdapter(searchAdapter);
                    everSearch=true;
                }else if(everSearch) {//第2+次点击搜索
                    searchAdapter.setData(searchResultDatas);
                    xlv_search.setAdapter(searchAdapter);
                }
            }else {//加载更多
                loadMore=false;
                searchAdapter.addData(searchResultDatas);
                searchAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
            //2.loading消失，提示
            ll_loading.setVisibility(View.GONE);
            Utils.showToast(LocationMarkActivity.this, "检索失败，请重新输入！");
        }
    }

    /**
     * 拖动后重新定位
     * @param mapCenter
     */
    private void reLocation(final LatLng mapCenter) {
        Utils.showToast(LocationMarkActivity.this, "重新定位中");

        float latitude = (float) mapCenter.getLatitude();
        float longitude = (float) mapCenter.getLongitude();
        String url= UrlConstance.LBaseUrl+latitude+","+longitude+"&key="+ Tencent.KEY+"&get_poi="+poiIndex;

        netUtils=new NetUtils(this,AroundPoi.class,url);

    }

    @Subscribe
    public void onEventMainThread(AroundPoi aroundPoi) {
        //拖动后重新定位周边poi
        LogUtils.Log("onEventMainThread");
        netPois = aroundPoi.getResult().getPois();
        poiAdapter.setData(netPois);
        reference_list.setAdapter(poiAdapter);
        //标志当前referlist的数据是通过联网请求得到的
        isFromNet=true;
    }



    private void initAdapter() {

        aroundAdapter=new AroundAdapter(this);
        poiAdapter=new PoiAdapter(this);
        searchAdapter=new SearchAdapter(this);
    }

    private void selfTitle() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("新增门店");
    }
    /**
     * 腾讯地图的初始化配置
     */
    private void initTencentMap() {
        controller = tecent_map.getController();
        controller.setZoom(16);
        locationListener=new MyTencentLocationListener();
        tencentSearch = new TencentSearch(this);
    }
    private void initView() {
        tecent_map = (SelfMapView)findViewById(R.id.tecent_map);

        btn_setting_next = (Button)findViewById(R.id.btn_setting_next);
        btn_search = (Button)findViewById(R.id.btn_search);
        et_search_address = (EditText)findViewById(R.id.et_search_address);
        ll_loading = (LinearLayout)findViewById(R.id.ll_loading);
        rl_search = (RelativeLayout)findViewById(R.id.rl_search);
        iv_center = (ImageView)findViewById(R.id.iv_center);


        reference_list = (XListView)findViewById(R.id.reference_list);
        reference_list.setTag(AROUND);
        xlv_search = (XListView)findViewById(R.id.xlv_search);
        xlv_search.setTag(SEARCH);

    }
    /**
     * 定位
     */
    private void getMyLocation() {
        Utils.showToast(this, "定位中...");

        //1.请求参数
        request = TencentLocationRequest.create();
        request.setAllowCache(true);
        request.setInterval(0);
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_POI);
        //2.获取位置
        tencentLocationManager = TencentLocationManager.getInstance(this);
        tencentLocationManager.requestLocationUpdates(request, locationListener);
    }

    /**
     * 首次进入定位的监听
     */
    class MyTencentLocationListener implements TencentLocationListener{

        @Override
        public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
            //移除定位监听
            tencentLocationManager.removeUpdates(this);
            currentcity = tencentLocation.getCity();
            //移动定位到地图中心
            GeoPoint geoPoint=Utils.of(tencentLocation);
            animateTo(geoPoint, tencentLocation.getLatitude(), tencentLocation.getLongitude());

            //获取周边信息
            poiList = tencentLocation.getPoiList();
            aroundAdapter.setData(poiList);
            reference_list.setAdapter(aroundAdapter);

            setOverly(tencentLocation.getLongitude(),tencentLocation.getLatitude());
        }

        @Override
        public void onStatusUpdate(String s, int i, String s1) {

        }
    }

    /**
     * 移动到指定位置/定位位置
     * @param oflalo
     * @param lat
     * @param lng
     */
    private void animateTo(GeoPoint oflalo, double lat, double lng) {

        if(oflalo!=null) {
            controller.animateTo(oflalo);
            // 修改 mapview 中心点
            controller.setCenter(oflalo);
        }
        // 注意一定要更新当前位置 mCenter
        updatePosition(lat,lng);


    }
    private void updatePosition(double lat, double lng) {
//        LatLng c = tecent_map.getMapCenter();
//        double lat = c.getLatitude();
//        double lng = c.getLongitude();

        mCenter.setLatitude(lat);
        mCenter.setLongitude(lng);
        //指针动画
        startAnimation();
    }

    /**
     * 地图中央指针动画
     */
    private void startAnimation() {
        Animation animation=new TranslateAnimation(0,0,0,-40);
        animation.setDuration(1000);
        iv_center.startAnimation(animation);
    }

    /**
     * 定位位置的标注
     * @param longitude
     * @param latitude
     */
    private void setOverly(double longitude, double latitude) {
        LatLng latLng1 = new LatLng(latitude,longitude);
        CircleOptions circleOp = new CircleOptions();
        circleOp.center(latLng1);
        circleOp.radius(50);
        circleOp.strokeColor(0xff0000ff);
        circleOp.strokeWidth(5);
        circleOp.fillColor(0xff00ff00);
        Circle circle = tecent_map.addCircle(circleOp);
    }
}
