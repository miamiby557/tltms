package cn.xyy.tltms.app.tltms;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.LocationMode;
import com.baidu.trace.Trace;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.xyy.tltms.app.data.model.SysUserListDTO;


/**
 * Created by admin on 2017/3/18.
 */

public class TLApplication extends Application {
    private static TLApplication INSTANCE;
    public static String FILEUPLOAD;
    private SysUserListDTO sysUserListDTO;

    public static String SERVER;

    private Context mContext = null;

    private List<String> roleCodes;

    public String type;

    public Activity mainActivity;

    /**
     * 发送定位的时间，默认10秒
     */
    public static int SECOND = 10000;

    /**
     * 轨迹服务
     */
    private Trace trace = null;

    /**
     * 轨迹服务客户端
     */
    private LBSTraceClient client = null;

    /**
     * 鹰眼服务ID，开发者创建的鹰眼服务对应的服务ID
     */
    private int serviceId = 10000;

    /**
     * entity标识
     */
    private String entityName = "myTrace";

    /**
     * 轨迹服务类型（0 : 不建立socket长连接， 1 : 建立socket长连接但不上传位置数据，2 : 建立socket长连接并上传位置数据）
     */
    private int traceType = 2;

    private MapView bmapView = null;

    private BaiduMap mBaiduMap = null;

    private TrackHandler mHandler = null;

    private double latitude = 0.0;//纬度
    private double longitude = 0.0;//经度

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        mContext = getApplicationContext();

        /*SDKInitializer.initialize(mContext);

        // 初始化轨迹服务客户端
        client = new LBSTraceClient(mContext);

        // 初始化轨迹服务
        trace = new Trace(mContext, serviceId, entityName, traceType);

        // 设置定位模式
        client.setLocationMode(LocationMode.High_Accuracy);

        mHandler = new TrackHandler(this);*/
    }

    public static TLApplication

    getInstance() {
        return INSTANCE;
    }

    public void setUser(SysUserListDTO sysUserListDTO) {
        this.sysUserListDTO = sysUserListDTO;
    }

    public SysUserListDTO getSysUserListDTO() {
        return this.sysUserListDTO;
    }

    public void initBmap(MapView bmapView) {
        this.bmapView = bmapView;
        this.mBaiduMap = bmapView.getMap();
        this.bmapView.showZoomControls(false);
    }

    public void setServer(String url) {
        SERVER = url;
    }

    public String getVersion() {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getLgLt() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return "0-0";
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }else{
                LocationListener locationListener = new LocationListener() {

                    // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    // Provider被enable时触发此函数，比如GPS被打开
                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    // Provider被disable时触发此函数，比如GPS被关闭
                    @Override
                    public void onProviderDisabled(String provider) {

                    }

                    //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                    @Override
                    public void onLocationChanged(Location location) {
                        if (location != null) {
                            Log.e("Map", "Location changed : Lat: "
                                    + location.getLatitude() + " Lng: "
                                    + location.getLongitude());
                        }
                    }
                };
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(location != null){
                    latitude = location.getLatitude(); //经度
                    longitude = location.getLongitude(); //纬度
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return longitude+"-"+latitude;
    }


    static class TrackHandler extends Handler {
        WeakReference<TLApplication> trackApp;

        TrackHandler(TLApplication trackApplication) {
            trackApp = new WeakReference<>(trackApplication);
        }

        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(trackApp.get().mContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
        }
    }

    public Context getmContext() {
        return mContext;
    }

    public Trace getTrace() {
        return trace;
    }

    public LBSTraceClient getClient() {
        return client;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getEntityName() {
        return entityName;
    }

    public Handler getmHandler() {
        return mHandler;
    }

    public MapView getBmapView() {
        return bmapView;
    }

    public BaiduMap getmBaiduMap() {
        return mBaiduMap;
    }

    public List<String> getRoleCodes() {
        return roleCodes;
    }

    public void setRoleCodes(List<String> roleCodes) {
        this.roleCodes = roleCodes;
    }
}
