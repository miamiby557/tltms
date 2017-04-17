package cn.xyy.tltms.app.tltms;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.SysUserListDTO;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.driver.DriverActivity;
import cn.xyy.tltms.app.tltms.driver.SettingActivity;
import cn.xyy.tltms.app.tltms.driver.DispatcherActivity;
import cn.xyy.tltms.app.widget.DialogTool;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.edtName)
    TextInputEditText edtName;

    @BindView(R.id.edtPsw)
    TextInputEditText edtPsw;

    @BindView(R.id.setServer)
    TextView setServer;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.autoLogin)
    CheckBox autoLogin;

    boolean bAutoogin = false;

    SharedPreferences preferences;

    Dialog roleDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getApplicationContext().getSharedPreferences("server", Context.MODE_PRIVATE);
        String url = preferences.getString("url","");
        String time = preferences.getString("time","");

        TLApplication.SERVER = url;
        TLApplication.FILEUPLOAD = url+"api/tsOrder/upload";
        try {
            TLApplication.SECOND = Integer.parseInt(time)*1000;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            TLApplication.SECOND = 10000;
        }
        Intent intent = getIntent();
        boolean auto = intent.getBooleanExtra("auto",true);
        String userName = sp.getString("userName","");
        String password = sp.getString("password","");
        boolean autoLo = sp.getBoolean("autoLogin",false);
        autoLogin.setChecked(autoLo);
        edtName.setText(userName);
        edtPsw.setText(password);
        if(auto && autoLo){
            login();
        }
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initData() {
    }

    @OnClick({R.id.btnLogin,R.id.setServer})
    void login(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                String name = edtName.getText().toString();
                if(name.length()==0){
                    Toast.makeText(LoginActivity.this,"请输入用户名" , Toast.LENGTH_LONG).show();
                    return;
                }
                String pwd = edtPsw.getText().toString();
                if(pwd.length()==0){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                    return;
                }
                String serverAddress = TLApplication.SERVER;
                if(serverAddress==null || serverAddress.length()==0){
                    Toast.makeText(LoginActivity.this, "请设置服务器地址", Toast.LENGTH_LONG).show();
                    return;
                }
                login();
                break;
            case R.id.setServer:
                openActivity(SettingActivity.class);
                break;
        }
    }

    private void login() {
        btnLogin.setText("登录中...");
        btnLogin.setClickable(false);
        final String name = edtName.getText().toString();
        final String psw = edtPsw.getText().toString();
        Map<String, String> params = new HashMap<>();
        params.put("username", name);
        params.put("password", psw);

        RetrofitFactory.getApiService().login(params).enqueue(new Callback<Result<SysUserListDTO>>() {
            @Override
            public void onResponse(Call<Result<SysUserListDTO>> call, Response<Result<SysUserListDTO>> response) {
                Result<SysUserListDTO> result = response.body();
                if (null != result && result.isSuccess()) {
                    if(result.getData().getRoleCodes()==null || result.getData().getRoleCodes().size()==0){
                        Toast.makeText(LoginActivity.this, "此用户未选定角色,请联系系统管理员", Toast.LENGTH_LONG).show();
                        btnLogin.setText("登 录");
                        btnLogin.setClickable(true);
                        return;
                    }
                    SysUserListDTO sysUserListDTO = result.getData();
                    TLApplication.getInstance().setUser(sysUserListDTO);
                    saveInfo(name,psw);
                    List<String> roleCodes = result.getData().getRoleCodes();
                    TLApplication.getInstance().setRoleCodes(roleCodes);
                    TLApplication.getInstance().SECOND = Integer.parseInt(sysUserListDTO.getTimeInterval());//传送坐标间隔时间
                    if(roleCodes.contains("ROLE_SJ")){
                        //司机
                        TLApplication.getInstance().type = "司机";
                        openActivity(DriverActivity.class);
                        finish();

                    }else if(roleCodes.contains("ROLE_DD")){
                        //调度
                        TLApplication.getInstance().type = "调度";
                        openActivity(DispatcherActivity.class);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "此用户没有角色", Toast.LENGTH_LONG).show();
                        btnLogin.setText("登 录");
                        btnLogin.setClickable(true);
                    }
                } else if (null != result && !result.isSuccess()) {
                    Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                    btnLogin.setText("登 录");
                    btnLogin.setClickable(true);
                } else {
                    Toast.makeText(LoginActivity.this, "登录异常", Toast.LENGTH_LONG).show();
                    btnLogin.setText("登 录");
                    btnLogin.setClickable(true);
                }
            }

            @Override
            public void onFailure(Call<Result<SysUserListDTO>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "登录失败，请检查服务器", Toast.LENGTH_LONG).show();
                btnLogin.setText("登 录");
                btnLogin.setClickable(true);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(roleDialog!=null){
            roleDialog.cancel();
        }
    }

    private void saveInfo(String userName, String password){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userName", userName);
        editor.putString("password",password);
        if(autoLogin.isChecked()){
            editor.putBoolean("autoLogin", true);
        }else{
            editor.putBoolean("autoLogin", false);
        }

        editor.commit();
    }


}
