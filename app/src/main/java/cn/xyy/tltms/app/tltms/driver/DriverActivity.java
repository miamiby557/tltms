package cn.xyy.tltms.app.tltms.driver;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.FileDTO;
import cn.xyy.tltms.app.data.model.SysUserListDTO;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.service.LbsService;
import cn.xyy.tltms.app.slipeMenu.SlidingMenu;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.LoginActivity;
import cn.xyy.tltms.app.tltms.TLApplication;
import cn.xyy.tltms.app.tltms.driver.FeeDeclare.FeeDeclareRecordActivity;
import cn.xyy.tltms.app.tltms.driver.loadingOrderHistory.LoadingOrderRecordActivity;
import cn.xyy.tltms.app.tltms.driver.person.PersonalActivity;
import cn.xyy.tltms.app.update.ApkUpdateUtils;
import cn.xyy.tltms.app.utils.DataUtils;
import cn.xyy.tltms.app.widget.DialogTool;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnLoadingOrder)
    LinearLayout btnLoadingOrder;
    @BindView(R.id.btnExpensesDeclare)
    LinearLayout btnExpensesDeclare;
    @BindView(R.id.personal)
    LinearLayout personal;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        startService(new Intent(this, LbsService.class));
        try {
            manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            listDTO = TLApplication.getInstance().getSysUserListDTO();
            name.setText(listDTO.getFullName()+" (司机)");
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


    @Override
    protected void initData() {

    }

    @OnClick({R.id.btnLoadingOrder,R.id.btnExpensesDeclare,R.id.personal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoadingOrder:
                openActivity(LoadingOrderRecordActivity.class);
                break;
            case R.id.btnExpensesDeclare:
                //费用申报
                openActivity(FeeDeclareRecordActivity.class);
                break;
            case R.id.personal:
                TLApplication.getInstance().mainActivity = this;
                openActivity(PersonalActivity.class);
                break;
        }
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
