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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.CommonEnum;
import cn.xyy.tltms.app.data.model.DeliveryTaskCreatePo;
import cn.xyy.tltms.app.data.model.LoadingOrderCreatePo;
import cn.xyy.tltms.app.data.model.SysUserListDTO;
import cn.xyy.tltms.app.data.model.TransportationOrderListDTO;
import cn.xyy.tltms.app.data.model.VehicleListDTO;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.TLApplication;
import cn.xyy.tltms.app.utils.CalculateType;
import cn.xyy.tltms.app.utils.DataUtils;
import cn.xyy.tltms.app.utils.DateUtil;
import cn.xyy.tltms.app.utils.NumberUtil;
import cn.xyy.tltms.app.widget.DialogTool;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 2017/3/28.
 */

public class DispatcherBasicInfoActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.vehicleNo)
    TextView vehicleNo;
    @BindView(R.id.driver)
    TextView driver;
    @BindView(R.id.driverPhone)
    TextView driverPhone;
    @BindView(R.id.select_driver)
    Button select_driver;
    @BindView(R.id.startPlace)
    TextView startPlace;
    @BindView(R.id.endPlace)
    TextView endPlace;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.calculateTypes)
    Spinner calculateTypes;
    LoadingOrderCreatePo loadingOrderCreatePo;
    List<CalculateType> calculates = DataUtils.getCalculateTypes();
    private static int SELECT_DRIVER = 101;

    private Dialog dialog;
    @BindView(R.id.listView)
    ListView listView;
    List<TransportationOrderListDTO> selectDtos;
    TransportOrderSelectedAdapter transportOrderSelectedAdapter;
    CalculateAdapter calculateAdapter;
    String currCalculateType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher_basic_info);
        loadingOrderCreatePo = new LoadingOrderCreatePo();
        selectDtos = (List<TransportationOrderListDTO>) getIntent().getSerializableExtra("selectDtos");
        TransportationOrderListDTO listDTO = selectDtos.get(0);
        loadingOrderCreatePo.setOriginCode(listDTO.getOriginCityCode());
        loadingOrderCreatePo.setDestCode(listDTO.getDestCityCode());
        transportOrderSelectedAdapter = new TransportOrderSelectedAdapter();
        listView.setAdapter(transportOrderSelectedAdapter);
        calculateAdapter = new CalculateAdapter();
        calculateTypes.setAdapter(calculateAdapter);
        calculateTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CalculateType type = calculates.get(position);
                currCalculateType = type.getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        showView(listDTO);
    }

    private void showView(TransportationOrderListDTO listDTO) {
        startPlace.setText(listDTO.getOriginCity());
        endPlace.setText(listDTO.getDestCity());
        count.setText("("+selectDtos.size()+")");
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("填写基本信息");
    }

    @OnClick({R.id.select_driver})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.select_driver:
                intent = new Intent(DispatcherBasicInfoActivity.this,DispatcherSelectDriverActivity.class);
                startActivityForResult(intent,SELECT_DRIVER);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_create, menu);
        return true;
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
        if(vehicleNo.getText().toString().length()==0){
            Toast.makeText(DispatcherBasicInfoActivity.this, "请选择司机", Toast.LENGTH_LONG).show();
            return;
        }
        SysUserListDTO userListDTO = TLApplication.getInstance().getSysUserListDTO();
        loadingOrderCreatePo.setAppUserId(userListDTO.getAppUserId());
        loadingOrderCreatePo.setCreateUserId(userListDTO.getCreateUserId());
        loadingOrderCreatePo.setCreateUserName(userListDTO.getCreateUserName());
        loadingOrderCreatePo.setRemark(remark.getText().toString());
        loadingOrderCreatePo.setCreateUserName(userListDTO.getUsername());
        loadingOrderCreatePo.setCreateUserId(userListDTO.getUserId());
        loadingOrderCreatePo.setSiteId(userListDTO.getSiteId());
        loadingOrderCreatePo.setCalculateType(Enum.valueOf(CommonEnum.CalculateType.class,currCalculateType));
        List<DeliveryTaskCreatePo> taskCreatePos = new ArrayList<>();
        for(TransportationOrderListDTO listDTO:selectDtos){
            DeliveryTaskCreatePo po = new DeliveryTaskCreatePo();
            po.setTsOrderNo(listDTO.getTsOrderNo());
            po.setPackageQty(listDTO.getTotalPackageQty());
            po.setVolume(listDTO.getTotalVolume());
            po.setWeight(listDTO.getTotalWeight());
            taskCreatePos.add(po);
        }
        loadingOrderCreatePo.setDeliveryTaskCreatePos(taskCreatePos);

        dialog = DialogTool.showLoadingDialog(DispatcherBasicInfoActivity.this,"创建中...");
        RetrofitFactory.getApiService().dispatcherLoadingOrderCreate(loadingOrderCreatePo).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(dialog!=null){
                    dialog.cancel();
                }
                Result result = response.body();
                if(result!=null && result.isSuccess()){
                    Toast.makeText(DispatcherBasicInfoActivity.this, "创建成功", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(DispatcherBasicInfoActivity.this, "创建失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                if(dialog!=null){
                    dialog.cancel();
                }
                Toast.makeText(DispatcherBasicInfoActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void initData() {

    }

    class TransportOrderSelectedAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return selectDtos.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(DispatcherBasicInfoActivity.this).inflate(R.layout.adapter_transport_order_selected, parent, false);
                holder = new ViewHolder(convertView);
                //设置标记
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            final TransportationOrderListDTO listDTO = selectDtos.get(position);
            holder.ldOrderNo.setText(listDTO.getTsOrderNo());
            holder.company.setText(listDTO.getProjectName());
            holder.totalPackageQty.setText(NumberUtil.getValue(listDTO.getTotalPackageQty())+"箱");
            holder.startPlace.setText("始:"+listDTO.getOriginCity());
            holder.endPlace.setText("终:"+listDTO.getDestCity());
            holder.totalVolume.setText(NumberUtil.getValue(listDTO.getTotalVolume())+"立方");
            holder.totalWeight.setText(NumberUtil.getValue(listDTO.getTotalWeight())+"公斤");
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = DialogTool.showModifyDialog(DispatcherBasicInfoActivity.this, listDTO, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                EditText totalPackageQty = (EditText)dialog.findViewById(R.id.totalPackageQty);
                                EditText totalVolume = (EditText)dialog.findViewById(R.id.totalVolume);
                                EditText totalWeight = (EditText)dialog.findViewById(R.id.totalWeight);
                                if(totalPackageQty.getText().toString().length()==0){
                                    Toast.makeText(DispatcherBasicInfoActivity.this, "箱数为空", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if(totalVolume.getText().toString().length()==0){
                                    Toast.makeText(DispatcherBasicInfoActivity.this, "体积为空", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if(totalWeight.getText().toString().length()==0){
                                    Toast.makeText(DispatcherBasicInfoActivity.this, "重量为空", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                listDTO.setTotalPackageQty(new Integer(totalPackageQty.getText().toString()));
                                listDTO.setTotalVolume(new BigDecimal(totalVolume.getText().toString()));
                                listDTO.setTotalWeight(new BigDecimal(totalWeight.getText().toString()));
                                dialog.cancel();
                                Toast.makeText(DispatcherBasicInfoActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                                transportOrderSelectedAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                dialog.cancel();
                                Toast.makeText(DispatcherBasicInfoActivity.this, "修改失败", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            return convertView;
        }
        class  ViewHolder{
            @BindView(R.id.ldOrderNo)
            TextView ldOrderNo;
            @BindView(R.id.company)
            TextView company;
            @BindView(R.id.totalPackageQty)
            TextView totalPackageQty;
            @BindView(R.id.startPlace)
            TextView startPlace;
            @BindView(R.id.endPlace)
            TextView endPlace;
            @BindView(R.id.totalVolume)
            TextView totalVolume;
            @BindView(R.id.totalWeight)
            TextView totalWeight;
            @BindView(R.id.edit)
            Button edit;
            public ViewHolder(View view){
                ButterKnife.bind(this,view);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
                if(data!=null){
                    Bundle b=data.getExtras();
                    VehicleListDTO listDTO = (VehicleListDTO)b.getSerializable("listDto");
                    if(listDTO!=null){
                        vehicleNo.setText(listDTO.getVehicleNo());
                        driver.setText(listDTO.getDriver());
                        driverPhone.setText(listDTO.getDriverPhone());
                        loadingOrderCreatePo.setDriver(listDTO.getDriver());
                        loadingOrderCreatePo.setDriverPhone(listDTO.getDriverPhone());
                        loadingOrderCreatePo.setVehicleId(listDTO.getVehicleId());
                        loadingOrderCreatePo.setVehicleNo(listDTO.getVehicleNo());
                    }
                }

                break;
        }
    }

    class CalculateAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return calculates.size();
        }

        @Override
        public Object getItem(int position) {
            return calculates.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.adapter_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            CalculateType type = calculates.get(position);
            holder.name.setText(type.getName());
            return convertView;
        }
        class ViewHolder{
            @BindView(R.id.name)
            TextView name;
            public ViewHolder(View view){
                ButterKnife.bind(this,view);
            }
        }
    }
}
