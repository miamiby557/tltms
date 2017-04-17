package cn.xyy.tltms.app.tltms.driver.dispatcher;

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
import cn.xyy.tltms.app.data.model.TransportationOrderListDTO;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.TLApplication;
import cn.xyy.tltms.app.utils.DateUtil;
import cn.xyy.tltms.app.utils.NumberUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 2017/3/28.
 */

public class DispatcherOrderCreateActivity extends BaseActivity{
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
    @BindView(R.id.next)
    Button next;
    List<TransportationOrderListDTO> transportationOrderListDTOs;
    List<TransportationOrderListDTO> selectDtos;

    DispatcherOrderCreateAdapter dispatcherOrderCreateAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher_order_create);
        transportationOrderListDTOs = new ArrayList<>();
        selectDtos = new ArrayList<>();
        dispatcherOrderCreateAdapter = new DispatcherOrderCreateAdapter();
        listView.setAdapter(dispatcherOrderCreateAdapter);
        dispatcherOrderCreateAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("配载单创建");
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

    @OnClick({R.id.search,R.id.next})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.search:
                initData();
                break;
            case R.id.next:
                if(selectDtos.size()==0){
                    Toast.makeText(DispatcherOrderCreateActivity.this, "至少选择一条运单", Toast.LENGTH_LONG).show();
                    return;
                }
                intent = new Intent(DispatcherOrderCreateActivity.this,DispatcherBasicInfoActivity.class);
                intent.putExtra("selectDtos",(Serializable) selectDtos);
                startActivity(intent);
                break;
        }
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
                    if(result.getData()==null || result.getData().size()==0){
                        Toast.makeText(DispatcherOrderCreateActivity.this, "没有记录", Toast.LENGTH_LONG).show();
                    }else{
                        transportationOrderListDTOs = result.getData();
                        dispatcherOrderCreateAdapter.notifyDataSetChanged();
                    }
                }else if (null != result && !result.isSuccess()) {
                    Toast.makeText(DispatcherOrderCreateActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DispatcherOrderCreateActivity.this, "数据返回异常", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result<List<TransportationOrderListDTO>>> call, Throwable t) {
                Toast.makeText(DispatcherOrderCreateActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    class DispatcherOrderCreateAdapter extends BaseAdapter{

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
                convertView = LayoutInflater.from(DispatcherOrderCreateActivity.this).inflate(R.layout.adapter_transport_order_record, parent, false);
                holder = new ViewHolder(convertView);
                //设置标记
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            TransportationOrderListDTO listDTO = transportationOrderListDTOs.get(position);
            holder.tsOrderNo.setText(listDTO.getTsOrderNo());
            holder.projectName.setText(listDTO.getProjectName());
            holder.consignorCity.setText("始:"+getValue(listDTO.getOriginCity()));
            holder.consigneeCity.setText("终:"+getValue(listDTO.getDestCity()));
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

    private String getValue(String value){
        if(value==null){
            return "";
        }else{
            return value;
        }
    }

    private void addToList(int position) {
        TransportationOrderListDTO listDTO = transportationOrderListDTOs.get(position);
        boolean exist = false;
        for(TransportationOrderListDTO dto:selectDtos){
            if(dto.getTsOrderNo().equalsIgnoreCase(listDTO.getTsOrderNo())){
                exist = true;
                break;
            }
        }
        if(!exist){
            selectDtos.add(listDTO);
            updateUI();
        }


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
            volume.setText("0");
            weight.setText("0");
            number.setText("0");
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        dispatcherOrderCreateAdapter.state.clear();
        initData();
    }
}
