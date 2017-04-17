package cn.xyy.tltms.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.lang.ref.SoftReference;

/**
 * Created by Alpha on 2016/10/31
 */

public class GpsService extends Service implements BDLocationListener {

    private static long TIME = 1000L;
    // 定位相关
    private LocationClient mLocClient;
    private GpsHandler handler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        handler = new GpsHandler(this);

    }

    /**
     * 定位SDK监听函数
     */
    @Override
    public void onReceiveLocation(BDLocation location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        handler.sendEmptyMessageDelayed(0, TIME);
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        super.onDestroy();
    }

    private static final class GpsHandler extends Handler {

        private SoftReference<GpsService> reference;

        public GpsHandler(GpsService gpsService) {
            this.reference = new SoftReference<>(gpsService);
        }

        @Override
        public void handleMessage(Message msg) {
            GpsService gpsService = reference.get();
            if (gpsService != null) {
                super.handleMessage(msg);
                sendEmptyMessageDelayed(0, TIME);
                postLatLng();

            }
        }
    }


    private static void postLatLng() {

    }
}
