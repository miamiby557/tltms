package cn.xyy.tltms.app.tltms.driver.baiduMap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.TLApplication;

import com.baidu.mapapi.map.MapView;
import com.baidu.trace.T;

/**
 * Created by dev on 2017/3/30.
 */

public class CarLocationActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.search_text)
    EditText search_text;
    @BindView(R.id.search)
    Button search;
    @BindView(R.id.bmapView)
    MapView bmapView;

    private TLApplication trackApp = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disptcher_car_location);
        trackApp = (TLApplication) getApplicationContext();
        trackApp.initBmap(bmapView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("车辆跟踪");
    }

    @Override
    protected void initData() {

    }
}
