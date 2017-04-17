package cn.xyy.tltms.app.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xyy.tltms.app.data.model.LoadingOrderListDto;
import cn.xyy.tltms.app.data.model.LoadingOrderLocationCreatePO;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.TLApplication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 2017/3/25.
 */

public class LbsService extends Service implements BDLocationListener {

    // 定位相关
    private LocationClient mLocClient;
    private SDKReceiver mReceiver;

    private LoadingOrderListDto loadingOrder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //先拿到在途的配载单，如果有在途的配载单，则调用百度sdk获取最新的经纬度，然后回传到服务器
        //如果没有在途配载单，则不需要调用百度sdk；
        getIntransitOrder();
    }

    private void getIntransitOrder() {
        Map<String, String> params = new HashMap<>();
        if(TLApplication.getInstance()!=null && TLApplication.getInstance().getSysUserListDTO()!=null){
            params.put("userId", TLApplication.getInstance().getSysUserListDTO().getUserId());
            params.put("startDate", "");
            params.put("endDate", "");
            RetrofitFactory.getApiService().findActiveByAssignedTo(params).enqueue(new Callback<Result<List<LoadingOrderListDto>>>() {
                @Override
                public void onResponse(Call<Result<List<LoadingOrderListDto>>> call, Response<Result<List<LoadingOrderListDto>>> response) {
                    try {
                        List<LoadingOrderListDto> data = response.body().getData();
                        if (!data.isEmpty()) {
                            //不确定集合里面有多少单，只好拿第一个
                            loadingOrder = data.get(0);
                            //有在途派车单，初始化并调用百度sdk拿经纬度
                            initLocation();
                        } else {
                            if (null != mLocClient && mLocClient.isStarted()) {
                                mLocClient.stop();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Result<List<LoadingOrderListDto>>> call, Throwable t) {
                    Toast.makeText(LbsService.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }


    }

    /**
     * 初始百度地图
     */
    private void initLocation() {
        mReceiver = new SDKReceiver();
        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        registerReceiver(mReceiver, iFilter);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(TLApplication.getInstance().SECOND);//可以设置服务器传送地址的时间
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /*接收百度地图传回来的经纬度*/
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        sendLcation(bdLocation);
        Log.d("BaiduSdk---------->>","BaiduSdk");
    }

    /*回传配载单经纬度*/
    private void sendLcation(BDLocation bdLocation) {
        double lat = bdLocation.getLatitude();
        double lng = bdLocation.getLongitude();
        LoadingOrderLocationCreatePO locationCreatePO = new LoadingOrderLocationCreatePO();
        locationCreatePO.setLat(new BigDecimal(lat));
        locationCreatePO.setLng(new BigDecimal(lng));
        locationCreatePO.setTime(new Date());
        locationCreatePO.setUserId(TLApplication.getInstance().getSysUserListDTO().getUserId());
        RetrofitFactory.getApiService().sendLocation(locationCreatePO).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                try {
                    Result result = response.body();
                    if (null != result && result.isSuccess()) {

                    } else {
                        if (null != mLocClient && mLocClient.isStarted()) {
                            mLocClient.stop();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            Log.d("", "action: " + s);

            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                Log.d("SDKReceiver>>>>>", "key 验证出错! 错误码 :" + intent.getIntExtra(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0) + " ; 请在 AndroidManifest.xml 文件中检查 key 设置");
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                Log.d("SDKReceive,success>>>>>", "key 验证成功! 功能可以正常使用");
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                Log.d("NetworkError>>>>", "网络出错");
            }
        }
    }


}
