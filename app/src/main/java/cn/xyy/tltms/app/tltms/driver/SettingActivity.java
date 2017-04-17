package cn.xyy.tltms.app.tltms.driver;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.TLApplication;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textUrl)
    EditText textUrl;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        preferences = getApplicationContext().getSharedPreferences("server", Context.MODE_PRIVATE);
        String url = preferences.getString("url","");
        textUrl.setText(url);
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
                if(textUrl.getText().toString().length()==0){
                    Toast.makeText(SettingActivity.this, "输入服务器ip地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("url", textUrl.getText().toString());
                editor.commit();
                TLApplication.getInstance().setServer(textUrl.getText().toString());
                TLApplication.getInstance().FILEUPLOAD = textUrl.getText().toString()+"api/tsOrder/upload";
                RetrofitFactory.baseUrl = textUrl.getText().toString();
                RetrofitFactory.reSetRetrofit();
                finish();
                Toast.makeText(SettingActivity.this, "请登录", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
