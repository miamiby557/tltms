package cn.xyy.tltms.app.tltms.driver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.TLApplication;

public class LocationSendTimeActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textTime)
    EditText textTime;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_send);
        preferences = getApplicationContext().getSharedPreferences("server", Context.MODE_PRIVATE);
        String time = preferences.getString("time","");
        textTime.setText(time+"");
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null!=getSupportActionBar()){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.btnSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                if(textTime.getText().toString().length()==0 || Integer.parseInt(textTime.getText().toString())==0){
                    Toast.makeText(LocationSendTimeActivity.this, "输入大于0的时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("time", textTime.getText().toString());
                editor.commit();
                TLApplication.getInstance().setServer(textTime.getText().toString());
                finish();
                Toast.makeText(LocationSendTimeActivity.this, "注意:重新登录，时间才能生效", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
