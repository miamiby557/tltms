package cn.xyy.tltms.app.tltms.driver.person;

import android.app.Activity;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.FileDTO;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.LoginActivity;
import cn.xyy.tltms.app.tltms.TLApplication;
import cn.xyy.tltms.app.update.ApkUpdateUtils;
import cn.xyy.tltms.app.utils.DataUtils;
import cn.xyy.tltms.app.widget.DialogTool;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.checkVersion)
    LinearLayout checkVersion;
    @BindView(R.id.updatePwd)
    LinearLayout updatePwd;
    @BindView(R.id.loginOut)
    LinearLayout loginOut;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.carNumber)
    TextView carNumber;
    @BindView(R.id.company)
    TextView company;
    @BindView(R.id.car)
    LinearLayout car;
    @BindView(R.id.car_view)
    View car_view;
    @BindView(R.id.version)
    TextView version;
    private Dialog dialog;
    ConnectivityManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_operation);
        if("调度".equalsIgnoreCase(TLApplication.getInstance().type)){
            car.setVisibility(View.GONE);
            car_view.setVisibility(View.GONE);
        }
        manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        name.setText(TLApplication.getInstance().getSysUserListDTO().getFullName()+"  ");
        type.setText(TLApplication.getInstance().type+"  ");
        phone.setText(TLApplication.getInstance().getSysUserListDTO().getUsername()+"  ");
        carNumber.setText(TLApplication.getInstance().getSysUserListDTO().getVehicleNo()+"  ");
        company.setText(TLApplication.getInstance().getSysUserListDTO().getAppUserName()+"  ");
        String currentCode = DataUtils.getVersionCode(getApplicationContext());
        version.setText(currentCode+"  ");
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null!=getSupportActionBar()){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("我的");
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

    @OnClick({R.id.updatePwd,R.id.checkVersion,R.id.loginOut})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updatePwd:
                openActivity(UpdatePwdActivity.class);
                break;
            case R.id.checkVersion:
                NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                if(wifi == NetworkInfo.State.CONNECTED||wifi== NetworkInfo.State.CONNECTING){
                    /*dialog = DialogTool.showUpdateDialog(PersonalActivity.this, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                            getVersion();
                        }
                    });*/
                    getVersion();
                }else{
                    Toast.makeText(getApplicationContext(), "请连接wifi进行下载更新...", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.loginOut:
                finish();
                Activity activity = TLApplication.getInstance().mainActivity;
                if(activity!=null){
                    activity.finish();
                }
                Intent intent = new Intent(PersonalActivity.this,LoginActivity.class);
                intent.putExtra("auto",false);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void initData() {

    }

    private void getVersion() {
        RetrofitFactory.getApiService().checkVersion().enqueue(new Callback<Result<FileDTO>>() {
            @Override
            public void onResponse(Call<Result<FileDTO>> call, Response<Result<FileDTO>> response) {
                Result<FileDTO> result = response.body();
                if(result!=null && result.isSuccess()){
                    FileDTO fileDTO = result.getData();
                    if(fileDTO!=null){
                        String versionCode = fileDTO.getVersion();
                        if(versionCode!=null){
                            //计算服务器版本号所有数字之和
                            String[] count = versionCode.split("\\.");
                            int newNumber = 0;
                            if(count.length>0){
                                for(String s:count){
                                    newNumber += Integer.parseInt(s);
                                }
                            }
                            String currentCode = DataUtils.getVersionCode(getApplicationContext());
                            //计算本地版本号所有数字之和
                            int oldNumber = 0;
                            if(currentCode!=null){
                                String[] strings = currentCode.split("\\.");
                                if(strings.length>0){
                                    for(String s:strings){
                                        oldNumber += Integer.parseInt(s);
                                    }
                                }
                            }
                            Log.e("version",versionCode+","+newNumber+","+currentCode+","+oldNumber);
                            if(oldNumber<newNumber){
                                dialog = DialogTool.showConfirmUpdateDialog(PersonalActivity.this, currentCode, versionCode, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        download();
                                        dialog.cancel();
                                    }
                                });

                            }else{
                                Toast.makeText(getApplicationContext(), "当前版本已经是最新版本", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "服务器版本号为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "解析返回数据出错", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result<FileDTO>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            startActivity(intent);
        }
    }

    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private boolean canDownloadState() {
        try {
            int state = this.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void download() {
        if (!canDownloadState()) {
            Toast.makeText(this, "下载服务不用,请您启用", Toast.LENGTH_SHORT).show();
            showDownloadSetting();
            return;
        }
        String url;
        url = TLApplication.getInstance().SERVER+"api/sysFile/downloadApp";
        ApkUpdateUtils.download(this, url, getResources().getString(R.string.app_name));
    }
}
