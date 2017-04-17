package cn.xyy.tltms.app.tltms.driver.dispatcher;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.citypickerview.widget.CityPicker;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.ProjectListDTO;
import cn.xyy.tltms.app.data.model.SysUserListDTO;
import cn.xyy.tltms.app.data.model.TransportationOrderListDTO;
import cn.xyy.tltms.app.data.model.TransportationOrderQuickCreateDto;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.TLApplication;
import cn.xyy.tltms.app.utils.DateUtil;
import cn.xyy.tltms.app.utils.NumberUtil;
import cn.xyy.tltms.app.widget.DialogTool;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 2017/3/28.
 */

public class QuickOrderCreateActivity extends BaseActivity{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.originCityCode)
    EditText originCityCode;
    @BindView(R.id.destCityCode)
    EditText destCityCode;
    @BindView(R.id.totalVolume)
    EditText totalVolume;
    @BindView(R.id.totalWeight)
    EditText totalWeight;
    @BindView(R.id.clOrderNo)
    TextView clOrderNo;
    @BindView(R.id.consigneeMan)
    EditText consigneeMan;
    @BindView(R.id.consigneePhone)
    EditText consigneePhone;
    @BindView(R.id.start)
    ImageView start;
    @BindView(R.id.end)
    ImageView end;
    List<ProjectListDTO> projectListDTOs;
    ProjectListAdapter projectListAdapter;
    CityPicker cityPicker;
    int currPosition = -1;
    private String province;
    private String city;
    private String district;
    private Dialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_order_create);

    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("快速开单");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.create:
                check();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void check() {
        if(originCityCode.getText().length()==0){
            Toast.makeText(QuickOrderCreateActivity.this, "始发城市为空", Toast.LENGTH_LONG).show();
            return;
        }
        if(destCityCode.getText().length()==0){
            Toast.makeText(QuickOrderCreateActivity.this, "目的城市为空", Toast.LENGTH_LONG).show();
            return;
        }
        if(clOrderNo.getText().toString().length()==0){
            Toast.makeText(QuickOrderCreateActivity.this, "客户单号为空", Toast.LENGTH_LONG).show();
            return;
        }
        if(consigneeMan.getText().length()==0){
            Toast.makeText(QuickOrderCreateActivity.this, "收货人为空", Toast.LENGTH_LONG).show();
            return;
        }
        if(consigneePhone.getText().length()==0){
            Toast.makeText(QuickOrderCreateActivity.this, "收货人电话为空", Toast.LENGTH_LONG).show();
            return;
        }
        if(totalVolume.getText().length()==0){
            Toast.makeText(QuickOrderCreateActivity.this, "体积为空", Toast.LENGTH_LONG).show();
            return;
        }
        if(totalWeight.getText().length()==0){
            Toast.makeText(QuickOrderCreateActivity.this, "重量为空", Toast.LENGTH_LONG).show();
            return;
        }
        if(currPosition<0){
            Toast.makeText(QuickOrderCreateActivity.this, "请选择项目", Toast.LENGTH_LONG).show();
            return;
        }
        quickCreate();
    }

    private void quickCreate() {
        dialog = DialogTool.showLoadingDialog(QuickOrderCreateActivity.this,"开单中...");
        TransportationOrderQuickCreateDto dto = new TransportationOrderQuickCreateDto();
        SysUserListDTO userListDTO = TLApplication.getInstance().getSysUserListDTO();
        dto.setAppUserId(userListDTO.getAppUserId());
        dto.setCreateUserName(userListDTO.getUsername());
        dto.setCreateUserId(userListDTO.getUserId());
        dto.setSiteId(userListDTO.getSiteId());
        dto.setOriginCityCode(originCityCode.getText().toString());
        dto.setDestCityCode(destCityCode.getText().toString());
        dto.setClOrderNo(clOrderNo.getText().toString());
        dto.setConsigneeMan(consigneeMan.getText().toString());
        dto.setConsigneePhone(consigneePhone.getText().toString());
        dto.setTotalVolume(new BigDecimal(totalVolume.getText().toString()));
        dto.setTotalWeight(new BigDecimal(totalWeight.getText().toString()));
        ProjectListDTO listDTO = projectListDTOs.get(currPosition);
        dto.setProjectId(listDTO.getProjectId());
        RetrofitFactory.getApiService().quickOrder(dto).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(dialog!=null){
                    dialog.cancel();
                }
                Result result = response.body();
                if(result!=null && result.isSuccess()){
                    Toast.makeText(QuickOrderCreateActivity.this, "快速开单成功", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(QuickOrderCreateActivity.this, "快速开单失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                if(dialog!=null){
                    dialog.cancel();
                }
                Toast.makeText(QuickOrderCreateActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick({R.id.start,R.id.end})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.end:
                cityPicker = DialogTool.showSelectAddress(QuickOrderCreateActivity.this);
                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                        //省份
                        province = citySelected[0];
                        //城市
                        city = citySelected[1];
                        //区县（如果设定了两级联动，那么该项返回空）
                        district = citySelected[2];
                        destCityCode.setText(province + city + district);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
            case R.id.start:
                cityPicker = DialogTool.showSelectAddress(QuickOrderCreateActivity.this);
                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                        //省份
                        province = citySelected[0];
                        //城市
                        city = citySelected[1];
                        //区县（如果设定了两级联动，那么该项返回空）
                        district = citySelected[2];
                        originCityCode.setText(province + city + district);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_quick_order, menu);
        return true;
    }


    @Override
    protected void initData() {
        projectListDTOs = new ArrayList<>();
        projectListAdapter = new ProjectListAdapter();
        listView.setAdapter(projectListAdapter);
        Map<String, String> params = new HashMap<>();
        params.put("appUserId", TLApplication.getInstance().getSysUserListDTO().getAppUserId());
        RetrofitFactory.getApiService().findProject(params).enqueue(new Callback<Result<List<ProjectListDTO>>>() {
            @Override
            public void onResponse(Call<Result<List<ProjectListDTO>>> call, Response<Result<List<ProjectListDTO>>> response) {
                Result<List<ProjectListDTO>> result = response.body();
                if(null != result && result.isSuccess()){
                    if(result.getData()==null || result.getData().size()==0){
                        Toast.makeText(QuickOrderCreateActivity.this, "没有记录", Toast.LENGTH_LONG).show();
                    }else{
                        projectListDTOs = result.getData();
                        projectListAdapter.notifyDataSetChanged();
                    }
                }else if(null != result && !result.isSuccess()){
                    Toast.makeText(QuickOrderCreateActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(QuickOrderCreateActivity.this, "数据返回异常", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result<List<ProjectListDTO>>> call, Throwable t) {
                Toast.makeText(QuickOrderCreateActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    class ProjectListAdapter extends BaseAdapter{
        Map<Integer,Boolean> state=new HashMap<>();
        @Override
        public int getCount() {
            return projectListDTOs.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(QuickOrderCreateActivity.this).inflate(R.layout.adapter_project_record, parent, false);
                holder = new ViewHolder(convertView);
                //设置标记
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            ProjectListDTO listDTO = projectListDTOs.get(position);
            holder.code.setText("编码:"+listDTO.getCode());
            holder.name.setText("名称:"+listDTO.getName());
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        // 重置，确保最多只有一项被选中
                        for (Integer key : state.keySet()) {
                            state.put(key, false);
                        }
                        state.put(position,true);
                        currPosition = position;
                        ProjectListAdapter.this.notifyDataSetChanged();
                    }
                }
            });
            boolean res = false;
            if(state.get(position)!=null && state.get(position)){
                res = true;
            }else {
                res = false;
            }
            holder.checkbox.setChecked(res);
            return convertView;
        }

        class ViewHolder{
            @BindView(R.id.code)
            TextView code;
            @BindView(R.id.name)
            TextView name;
            @BindView(R.id.checkbox)
            CheckBox checkbox;

            public ViewHolder(View view){
                ButterKnife.bind(this,view);
            }
        }
    }

}
