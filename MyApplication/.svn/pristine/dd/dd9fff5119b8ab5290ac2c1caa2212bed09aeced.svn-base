package com.younle.younle624.myapplication.activity.regist.bindstore;

import android.app.Activity;
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
import com.younle.younle624.myapplication.adapter.XlistAdapter;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.SelfMapView;
import com.younle.younle624.myapplication.view.XListView;

import org.apache.http.Header;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 标记店铺位置的activity
 */
public class MarkLocationActivity extends Activity implements View.OnClickListener, TencentLocationListener, HttpResponseListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {
    private static final String AROUND = "around";
    private static final String SEARCH = "search";
    private SelfMapView tecent_map;
    private ListView reference_list;
    private Button btn_setting_next;
    private final Location mCenter = new Location("");
    private MapController controller;
    private ImageView iv_center;
    private Button btn_search;
    private TextView tv_title;
    private XlistAdapter xadapter;
    private LinearLayout ll_loading;
    private int pager_index=1;

    private List<SearchResultObject.SearchResultData> data=new ArrayList<>();;


    /**
     * 收索相关展示的listview
     */
    private XListView xlv_search;
    /**
     * 搜索地址的输入
     */
    private EditText et_search_address;
//    private LocationHelper mLocationHelper;
    private List<TencentPoi> poiList;
//    private AroundAdapter aroundAdapter;
    private SearchResultObject searchResultObject;
    private RelativeLayout rl_search;
    private String currentcity;
    private SearchParam param;
    private TencentSearch tencentSearch;
    private TencentLocationManager tencentLocationManager;
    private TencentLocationRequest request;
    private boolean isNear=false;
    private String inputAdd;
    private List<SearchResultObject.SearchResultData> nearData;
    private boolean isNearData=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_location);
        tecent_map = (SelfMapView)findViewById(R.id.tecent_map);
        tencentSearch = new TencentSearch(this);
        xadapter=new XlistAdapter(this);

//        mLocationHelper=new LocationHelper(this);


        initTencentMap();
        initView();
        setListener();
        initXlistView();
        selfTitle();
//        doMyLoc();
        getMyLocation();

    }

    private void setListener() {
        btn_search.setOnClickListener(this);
        xlv_search.setOnItemClickListener(this);
        reference_list.setOnItemClickListener(this);
        tecent_map.setOnDragEvent(new SelfMapView.OnDragEvent() {
            @Override
            public void doSomeThing() {
                startAnimation();
                LatLng mapCenter = tecent_map.getMapCenter();
                reLocation(mapCenter);

            }
        });
    }

    private void reLocation(final LatLng mapCenter) {
    Utils.showToast(MarkLocationActivity.this, "重新定位中");

    float latitude = (float) mapCenter.getLatitude();
    float longitude = (float) mapCenter.getLongitude();
    String url="http://apis.map.qq.com/ws/geocoder/v1/?location="+latitude+","+longitude+"&key=DTPBZ-GZCHF-GGGJL-NFJSE-KGISO-BIF3W&get_poi=1";
        RequestParams entity=new RequestParams(url);
        x.http().get(entity, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        
        
//                com.tencent.lbssearch.object.Location location=
//                        new com.tencent.lbssearch.object.Location(latitude,longitude);
//                isNear=true;
//
//               searchNear(location);

    }

    /**
     * xlistview的配置
     */
    private void initXlistView() {
        xlv_search.setPullRefreshEnable(false);
        xlv_search.setPullLoadEnable(true);
        xlv_search.setAutoLoadEnable(false);
        xlv_search.setXListViewListener(this);
//        xlv_search.setRefreshTime(getTime());
//        mAdapter = new ArrayAdapter<String>(this, R.layout.vw_list_item, items);
//        mListView.setAdapter(mAdapter);
    }

    /**
     * 腾讯地图的初始化配置
     */
    private void initTencentMap() {
        controller = tecent_map.getController();
        controller.setZoom(16);
//        aroundAdapter=new AroundAdapter();

//        Marker marker = tecent_map.addMarker(new MarkerOptions()
//                .anchor(0.5f, 0.5f)
//
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_indictor))
//                .draggable(true));
//        marker.showInfoWindow();// 设置默认显示一个infowinfow
    }

    private void selfTitle() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("新增门店");
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
        tencentLocationManager.requestLocationUpdates(request, MarkLocationActivity.this);


    }

    private void initView() {

        btn_setting_next = (Button)findViewById(R.id.btn_setting_next);
        btn_search = (Button)findViewById(R.id.btn_search);
        et_search_address = (EditText)findViewById(R.id.et_search_address);
        ll_loading = (LinearLayout)findViewById(R.id.ll_loading);
        rl_search = (RelativeLayout)findViewById(R.id.rl_search);
        iv_center = (ImageView)findViewById(R.id.iv_center);


        reference_list = (ListView)findViewById(R.id.reference_list);
        reference_list.setTag(AROUND);
        xlv_search = (XListView)findViewById(R.id.xlv_search);
        xlv_search.setTag(SEARCH);

    }

    private void animateTo(TencentLocation location, GeoPoint oflalo) {
        if (location == null&&oflalo==null) {
            return;
        }
        if(location!=null) {
//            controller.animateTo();
//            // 修改 mapview 中心点
//            controller.setCenter(oflalo);
        }else if(oflalo!=null) {
            controller.animateTo(oflalo);
            // 修改 mapview 中心点
            controller.setCenter(oflalo);
        }
        // 注意一定要更新当前位置 mCenter
        updatePosition();

    }
    private void updatePosition() {
        LatLng c = tecent_map.getMapCenter();
        double lat = c.getLatitude();
        double lng = c.getLongitude();
//        double lat = c.getLatitudeE6() / 1E6;
//        double lng = c.getLongitudeE6() / 1E6;

        mCenter.setLatitude(lat);
        mCenter.setLongitude(lng);
        //指针动画
        startAnimation();
    }

    /**
     * 位置搜索的监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        //1.获取edittext的输入
        rl_search.setVisibility(View.VISIBLE);
        ll_loading.setVisibility(View.VISIBLE);
        inputAdd = et_search_address.getText().toString();
        if(!TextUtils.isEmpty(inputAdd)) {
            searchInput(inputAdd);
        }

    }

    /**
     * 位置搜索
     * @param inputAdd
     */
    private void searchInput(String inputAdd) {

        SearchParam.Region r = new SearchParam.Region().poi(currentcity);
        param = new SearchParam().keyword(inputAdd).boundary(r);

        tencentSearch.search(param, this);

    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        //移除定位监听
        tencentLocationManager.removeUpdates(this);
        currentcity = tencentLocation.getCity();
        //移动定位到地图中心
        GeoPoint geoPoint=Utils.of(tencentLocation);
        animateTo(null, geoPoint);

        //获取周边信息
        poiList = tencentLocation.getPoiList();
        xadapter.setData(poiList,1);
        reference_list.setAdapter(xadapter);

        setOverly(tencentLocation.getLongitude(),tencentLocation.getLatitude());


    }

    private void setOverly(double longitude, double latitude) {
        LatLng latLng1 = new LatLng(latitude,longitude);
        CircleOptions circleOp = new CircleOptions();
        circleOp.center(latLng1);
        circleOp.radius(50);
        circleOp.strokeColor(0xff0000ff);
        circleOp.strokeWidth(5);
        circleOp.fillColor(0xff00ff00);
        Circle circle = tecent_map.addCircle(circleOp);

////        Drawable mark=this.getDrawable(R.drawable.current_location);
////        GeoPoint geoPoint = Utils.oflalo(latitude, longitude);
////
////        OverlayItem overlayItem=new OverlayItem(geoPoint,"","");
////        overlayItem.setMarker(mark);
//        GeoPoint p1 = Utils.oflalo(latitude,longitude);
////        GeoPoint p2 = new GeoPoint((int)(38.5 * 1E6), (int)(114.955 * 1E6));
////        LatLng SHANGHAI = new LatLng(31.238068, 121.501654);// 上海市经纬度
//        Drawable marker = getDrawable(current_location);
//
////        OverlayItem oiFixed = new OverlayItem(p1, "标注1", "不可拖拽");
//
////        oiFixed.setDragable(false);
//        OverlayItem oiDrag = new OverlayItem(p1,"","");
//        oiDrag.setMarker(marker);
//
////        mapView.add(oiFixed);
//        tecent_map.getOverlay().add(marker);
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    /**
     * 腾讯地图位置搜索的回调
     * @param i
     * @param headers
     * @param baseObject
     */
    @Override
    public void onSuccess(int i, Header[] headers, BaseObject baseObject) {
        searchResultObject = (SearchResultObject)baseObject;
        LogUtils.Log("onSuccess");
        if(isNear) {//收索周边
            isNear=false;
            rl_search.setVisibility(View.GONE);
            nearData = searchResultObject.data;
            xadapter.setNearData(nearData,3);
            isNearData=true;
            reference_list.setAdapter(xadapter);
            rl_search.setVisibility(View.GONE);
            ll_loading.setVisibility(View.GONE);
            xlv_search.setVisibility(View.GONE);

        }


        ll_loading.setVisibility(View.GONE);
        xlv_search.setVisibility(View.VISIBLE);

        xadapter.addData(searchResultObject.data, 2);
        if(pager_index==1) {
            xlv_search.setAdapter(xadapter);
            data.addAll(searchResultObject.data);
        }else {
            data.addAll(searchResultObject.data);
            xadapter.notifyDataSetChanged();
        }



    }

    @Override
    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
        xlv_search.stopLoadMore();
        Utils.showToast(this,"当前网络有问题，请检查网络后重试！");
        LogUtils.Log("onFailure");
    }

    /**
     * xlistview下拉刷新和上拉加载更多的回调
     */
    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        //1.再次请求腾讯地图服务器parm中添加pager_index,
        pager_index++;
        param.page_index(pager_index);
        tencentSearch.search(param, this);

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getTag().toString()) {
            case  AROUND:
                double latitude;
                double longitude;
                if(isNearData) {
                     latitude= nearData.get(position).location.lat;
                     longitude = nearData.get(position).location.lng;
                }else {
                     longitude = poiList.get(position).getLongitude();
                     latitude = poiList.get(position).getLatitude();

                }
                GeoPoint oflalo = Utils.oflalo(latitude, longitude);
                animateTo(null,oflalo);
                break;
            case SEARCH:

                longitude=data.get(position).location.lng;
                latitude=data.get(position).location.lat;
                oflalo = Utils.oflalo(latitude, longitude);
                animateTo(null,oflalo);

                searchNear(data.get(position).location);

//                xadapter.setData(poiList,1);
//                xadapter.notifyDataSetChanged();


                break;
        }
    }

    private void startAnimation() {
        Animation animation=new TranslateAnimation(0,0,0,-40);
        animation.setDuration(1000);
        iv_center.startAnimation(animation);
    }

    private void searchNear(com.tencent.lbssearch.object.Location location) {
        SearchParam.Nearby nearBy = new SearchParam.Nearby().point(location);
        nearBy.r(1000);

        param = new SearchParam().keyword(" ").boundary(nearBy);
//      SearchParam object = new SearchParam().keyword("").boundary(nearBy);
        isNear=true;
        tencentSearch.search(param, this);


    }

    @Override
    protected void onDestroy() {
        tecent_map.onDestroy();

        super.onDestroy();
    }
    @Override
    protected void onPause() {
        tecent_map.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        tecent_map.onResume();
        super.onResume();
    }

    @Override
    protected void onStop() {
        tecent_map.onStop();
        super.onStop();
    }
}
