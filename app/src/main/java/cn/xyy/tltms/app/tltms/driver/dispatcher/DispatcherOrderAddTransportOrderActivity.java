package cn.xyy.tltms.app.tltms.driver.dispatcher;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;

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
import cn.xyy.tltms.app.data.model.DeliveryTaskCreatePo;
import cn.xyy.tltms.app.data.model.LoadingOrderAddTaskDto;
import cn.xyy.tltms.app.data.model.SysUserListDTO;
import cn.xyy.tltms.app.data.model.TransportationOrderListDTO;
import cn.xyy.tltms.app.data.model.TransportationOrderSummaryDTO;
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

public class DispatcherOrderAddTransportOrderActivity extends BaseActivity{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.edtLdOrderNo)
    EditText edtLdOrderNo;
    @BindView(R.id.search)
    Button search;
    @BindView(R.id.volume)
    TextView volume;
    @BindView(R.id.weight)
    TextView weight;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.add)
    Button add;
    List<TransportationOrderListDTO> transportationOrderListDTOs;
    List<TransportationOrderListDTO> selectDtos;
    String ldOrderId;
    Dialog dialog;

    TransportOrderAdapter transportOrderAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher_order_add_transport_order);

    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("配载单加单");
        Intent intent = getIntent();
        ldOrderId = intent.getStringExtra("ldOrderId");
        transportationOrderListDTOs = new ArrayList<>();
        selectDtos = new ArrayList<>();
        transportOrderAdapter = new TransportOrderAdapter();
        listView.setAdapter(transportOrderAdapter);
        transportOrderAdapter.notifyDataSetChanged();
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

    @OnClick({R.id.search,R.id.add})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.search:
                initData();
                break;
            case R.id.add:
                if(selectDtos.size()==0){
                    Toast.makeText(DispatcherOrderAddTransportOrderActivity.this, "至少选择一个运单", Toast.LENGTH_LONG).show();
                    return;
                }
                addTsOrder();
                break;
        }
    }

    private void addTsOrder() {
        LoadingOrderAddTaskDto addTaskDto = new LoadingOrderAddTaskDto();
        addTaskDto.setLdOrderId(ldOrderId);
        SysUserListDTO userListDTO = TLApplication.getInstance().getSysUserListDTO();
        addTaskDto.setOperationUserId(userListDTO.getUserId());
        addTaskDto.setOperationUserName(userListDTO.getUsername());
        addTaskDto.setAppUserId(userListDTO.getAppUserId());
        List<DeliveryTaskCreatePo> taskCreatePos = new ArrayList<>();
        for(TransportationOrderListDTO listDTO:selectDtos){
            DeliveryTaskCreatePo createPo = new DeliveryTaskCreatePo();
            createPo.setTsOrderNo(listDTO.getTsOrderNo());
            createPo.setPackageQty(listDTO.getTotalPackageQty());
            createPo.setVolume(listDTO.getTotalVolume());
            createPo.setWeight(listDTO.getTotalWeight());
            taskCreatePos.add(createPo);
        }
        addTaskDto.setTasks(taskCreatePos);
        dialog = DialogTool.showLoadingDialog(DispatcherOrderAddTransportOrderActivity.this,"加单中...");
        RetrofitFactory.getApiService().addTsOrder(addTaskDto).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();
                if(null != result && result.isSuccess()){
                    Toast.makeText(DispatcherOrderAddTransportOrderActivity.this, "加单成功", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(DispatcherOrderAddTransportOrderActivity.this, "加单失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(DispatcherOrderAddTransportOrderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void initData() {
        Map<String, String> params = new HashMap<>();
        params.put("tsOrderNo",edtLdOrderNo.getText().toString());
        params.put("appUserId", TLApplication.getInstance().getSysUserListDTO().getAppUserId());
        RetrofitFactory.getApiService().loadingOrderFindTransportOrder(params).enqueue(new Callback<Result<List<TransportationOrderListDTO>>>() {
            @Override
            public void onResponse(Call<Result<List<TransportationOrderListDTO>>> call, Response<Result<List<TransportationOrderListDTO>>> response) {
                Result<List<TransportationOrderListDTO>> result = response.body();
                if(null != result && result.isSuccess()){
                    transportationOrderListDTOs = result.getData();
                    transportOrderAdapter.notifyDataSetChanged();
                }else if (null != result && !result.isSuccess()) {
                    Toast.makeText(DispatcherOrderAddTransportOrderActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DispatcherOrderAddTransportOrderActivity.this, "数据返回异常", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result<List<TransportationOrderListDTO>>> call, Throwable t) {
                Toast.makeText(DispatcherOrderAddTransportOrderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    class TransportOrderAdapter extends BaseAdapter{

        Map<Integer,Boolean> state=new HashMap<>();
        @Override
        public int getCount() {
            return transportationOrderListDTOs.size();
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
                convertView = LayoutInflater.from(DispatcherOrderAddTransportOrderActivity.this).inflate(R.layout.adapter_transport_order_record, parent, false);
                holder = new ViewHolder(convertView);
                //设置标记
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            TransportationOrderListDTO listDTO = transportationOrderListDTOs.get(position);
            holder.tsOrderNo.setText(listDTO.getTsOrderNo());
            holder.consignorCity.setText("始:"+listDTO.getOrderAddressDTO().getConsignorCity());
            holder.consigneeCity.setText("终:"+listDTO.getOrderAddressDTO().getConsigneeCity());
            holder.projectName.setText(listDTO.getProjectName());
            holder.totalPackageQty.setText(NumberUtil.getValue(listDTO.getTotalPackageQty())+"箱");
            holder.totalVolume.setText(NumberUtil.getValue(listDTO.getTotalVolume())+"立方");
            holder.totalWeight.setText(NumberUtil.getValue(listDTO.getTotalWeight())+"公斤");
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked==true){
                        state.put(position, isChecked);
                        addToList(position);
                    }else{
                        state.remove(position);
                        removeFromList(position);
                    }
                }
            });
            if(state!=null&&state.containsKey(position)){
                holder.checkbox.setChecked(true);
            }else {
                holder.checkbox.setChecked(false);
            }
            return convertView;
        }

        class ViewHolder{
            @BindView(R.id.tsOrderNo)
            TextView tsOrderNo;
            @BindView(R.id.projectName)
            TextView projectName;
            @BindView(R.id.consignorCity)
            TextView consignorCity;
            @BindView(R.id.consigneeCity)
            TextView consigneeCity;
            @BindView(R.id.totalPackageQty)
            TextView totalPackageQty;
            @BindView(R.id.totalVolume)
            TextView totalVolume;
            @BindView(R.id.totalWeight)
            TextView totalWeight;
            @BindView(R.id.checkbox)
            CheckBox checkbox;

            public ViewHolder(View view){
                ButterKnife.bind(this,view);
            }
        }
    }

    private void removeFromList(int position) {
        selectDtos.remove(transportationOrderListDTOs.get(position));
        updateUI();
    }

    private void addToList(int position) {
        if(selectDtos.contains(transportationOrderListDTOs.get(position))){
            return;
        }
        selectDtos.add(transportationOrderListDTOs.get(position));
        updateUI();
    }

    private void updateUI(){
        if(selectDtos.size()>0){
            BigDecimal totalVolume = new BigDecimal(0.00);
            BigDecimal totalWeight = new BigDecimal(0.00);
            for(TransportationOrderListDTO dto:selectDtos){
                if(dto.getTotalVolume()!=null){
                    totalVolume = totalVolume.add(dto.getTotalVolume());
                }
                if(dto.getTotalWeight()!=null){
                    totalWeight = totalWeight.add(dto.getTotalWeight());
                }
            }
            volume.setText(NumberUtil.getValue(totalVolume));
            weight.setText(NumberUtil.getValue(totalWeight));
            number.setText(selectDtos.size()+"");
        }else{
            volume.setText(0+"");
            weight.setText(0+"");
            number.setText(0+"");
        }
    }

}
