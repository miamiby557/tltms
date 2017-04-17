package cn.xyy.tltms.app.tltms;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.service.LbsService;
import cn.xyy.tltms.app.tltms.driver.DriverActivity;
import cn.xyy.tltms.app.tltms.driver.DispatcherActivity;

public class MainActivity extends BaseActivity {


    @BindView(R.id.btnDriver)
    Button btnDriver;
    @BindView(R.id.btnDispatcher)
    Button btnDispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, LbsService.class));
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.btnDriver, R.id.btnDispatcher})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDriver:
                openActivity(DriverActivity.class);
                break;
            case R.id.btnDispatcher:
                openActivity(DispatcherActivity.class);
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
