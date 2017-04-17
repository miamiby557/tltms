package cn.xyy.tltms.app.tltms.driver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.SysUserListDTO;
import cn.xyy.tltms.app.service.LbsService;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.TLApplication;
import cn.xyy.tltms.app.tltms.driver.TransportOrder.TransportOrderSelectActivity;
import cn.xyy.tltms.app.tltms.driver.dispatcher.DispatcherLoadingOrderRecordActivity;
import cn.xyy.tltms.app.tltms.driver.dispatcher.DispatcherOrderCreateActivity;
import cn.xyy.tltms.app.tltms.driver.dispatcher.QuickOrderCreateActivity;
import cn.xyy.tltms.app.tltms.driver.person.PersonalActivity;

/**
 * Created by dev on 2017/3/28.
 */

public class DispatcherActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.loadingOrderList)
    LinearLayout loadingOrderList;
    @BindView(R.id.createLdOrder)
    LinearLayout createLdOrder;
    @BindView(R.id.carLocation)
    LinearLayout carLocation;
    @BindView(R.id.findOrder)
    LinearLayout findOrder;
    @BindView(R.id.personal)
    LinearLayout personal;
    @BindView(R.id.quickOrder)
    LinearLayout quickOrder;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.vehicleNo)
    TextView vehicleNo;
    @BindView(R.id.company)
    TextView company;
    SysUserListDTO listDTO;
    ConnectivityManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disptcher);
        startService(new Intent(this, LbsService.class));
        try {
            manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            listDTO = TLApplication.getInstance().getSysUserListDTO();
            name.setText(listDTO.getFullName()+" (调度)");
            phone.setText(listDTO.getUsername());
            company.setText(listDTO.getAppUserName());
            vehicleNo.setText(listDTO.getVehicleNo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
    }

    @OnClick({R.id.loadingOrderList,R.id.carLocation,R.id.findOrder,R.id.personal,R.id.quickOrder,R.id.createLdOrder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loadingOrderList:
                openActivity(DispatcherLoadingOrderRecordActivity.class);
                break;
            case R.id.carLocation:
                Toast.makeText(getApplicationContext(), "正在开发...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.findOrder:
                openActivity(TransportOrderSelectActivity.class);
                break;
            case R.id.personal:
                TLApplication.getInstance().mainActivity = this;
                openActivity(PersonalActivity.class);
                break;
            case R.id.quickOrder:
                openActivity(QuickOrderCreateActivity.class);
                break;
            case R.id.createLdOrder:
                openActivity(DispatcherOrderCreateActivity.class);
                break;
        }
    }

    @Override
    protected void initData() {

    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
