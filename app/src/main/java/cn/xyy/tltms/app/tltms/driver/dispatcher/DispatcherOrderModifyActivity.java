package cn.xyy.tltms.app.tltms.driver.dispatcher;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.DeliveryTaskListDto;
import cn.xyy.tltms.app.data.model.LoadingOrderCancelDto;
import cn.xyy.tltms.app.data.model.LoadingOrderListDto;
import cn.xyy.tltms.app.data.model.LoadingOrderOperationPo;
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

public class DispatcherOrderModifyActivity extends BaseActivity{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ldOrderNo)
    TextView ldOrderNo;
    @BindView(R.id.totalPackageQty)
    TextView totalPackageQty;
    @BindView(R.id.totalVolume)
    TextView totalVolume;
    @BindView(R.id.totalWeight)
    TextView totalWeight;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.originCity)
    TextView originCity;
    @BindView(R.id.destCity)
    TextView destCity;
    @BindView(R.id.createTime)
    TextView createTime;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.add)
    Button add;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.delete)
    Button delete;
    Dialog dialog;
    List<DeliveryTaskListDto> deliveryTaskListDtos;

    DeliveryTaskAdapter deliveryTaskAdapter;
    LoadingOrderListDto loadingOrderListDto;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher_order_modify);
    }

    private void showView() {

        ldOrderNo.setText(loadingOrderListDto.getLdOrderNo());
        createTime.setText(DateUtil.getDate(loadingOrderListDto.getCreateTime()));
        status.setText(loadingOrderListDto.getStatus().getText());
        totalPackageQty.setText(NumberUtil.getValue(loadingOrderListDto.getTotalPackageQty())+"箱");
        totalVolume.setText(NumberUtil.getValue(loadingOrderListDto.getTotalVolume())+"立方");
        totalWeight.setText(NumberUtil.getValue(loadingOrderListDto.getTotalWeight())+"公斤");
        originCity.setText("始:"+loadingOrderListDto.getOriginCity());
        destCity.setText("终:"+loadingOrderListDto.getDestCity());
        deliveryTaskListDtos = new ArrayList<>();
        deliveryTaskAdapter = new DeliveryTaskAdapter();

        deliveryTaskListDtos = loadingOrderListDto.getDeliveryTaskListDtoList();
        count.setText("配载信息("+deliveryTaskListDtos.size()+")");
        listView.setAdapter(deliveryTaskAdapter);
        deliveryTaskAdapter.notifyDataSetChanged();
    }


    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("配载单修改");
        Intent intent = getIntent();
        loadingOrderListDto = (LoadingOrderListDto)intent.getSerializableExtra("dto");
        showView();
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
    protected void onResume() {
        super.onResume();
        deliveryTaskListDtos = new ArrayList<>();
        if(deliveryTaskAdapter==null){
            deliveryTaskAdapter = new DeliveryTaskAdapter();
            listView.setAdapter(deliveryTaskAdapter);
        }
        deliveryTaskAdapter.notifyDataSetChanged();
        reload();
    }

    @OnClick({R.id.add,R.id.delete})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.add:
                intent = new Intent(DispatcherOrderModifyActivity.this,DispatcherOrderAddTransportOrderActivity.class);
                intent.putExtra("ldOrderId",loadingOrderListDto.getLdOrderId());
                startActivity(intent);
                break;
            case R.id.delete:
                dialog = DialogTool.showReasonDialog(DispatcherOrderModifyActivity.this,new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        EditText reason = (EditText)dialog.findViewById(R.id.reason);
                        cancel(reason.getText().toString());
                    }
                });
                break;
        }
    }

    private void cancel(String reason) {
        dialog = DialogTool.showLoadingDialog(DispatcherOrderModifyActivity.this,"取消中...");
        LoadingOrderCancelDto dto = new LoadingOrderCancelDto();
        dto.setLdOrderId(loadingOrderListDto.getLdOrderId());
        dto.setAppUserId(TLApplication.getInstance().getSysUserListDTO().getAppUserId());
        dto.setUserId(TLApplication.getInstance().getSysUserListDTO().getUserId());
        dto.setUserName(TLApplication.getInstance().getSysUserListDTO().getUsername());
        dto.setReason(reason);
        RetrofitFactory.getApiService().cancel(dto).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(dialog!=null){
                    dialog.cancel();
                }
                Result result = response.body();
                if(result!=null && result.isSuccess()){
                    Toast.makeText(DispatcherOrderModifyActivity.this, "取消成功", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(DispatcherOrderModifyActivity.this, "取消失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(DispatcherOrderModifyActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void initData() {
        if(loadingOrderListDto!=null){
            reload();
        }
    }

    class DeliveryTaskAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return deliveryTaskListDtos.size();
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
                convertView = LayoutInflater.from(DispatcherOrderModifyActivity.this).inflate(R.layout.adapter_delivery_task_item, parent, false);
                holder = new ViewHolder(convertView);
                //设置标记
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            DeliveryTaskListDto listDto = deliveryTaskListDtos.get(position);
            holder.tsOrderNo.setText(listDto.getTsOrderNo());
            holder.status.setText(listDto.getStatus().getText());
            holder.packageQty.setText(NumberUtil.getValue(listDto.getPackageQty())+"箱");
            holder.volume.setText(NumberUtil.getValue(listDto.getVolume())+"立方");
            holder.weight.setText(NumberUtil.getValue(listDto.getWeight())+"公斤");
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDeliveryTask(position);
                }
            });
            return convertView;
        }

        class ViewHolder{
            @BindView(R.id.tsOrderNo)
            TextView tsOrderNo;
            @BindView(R.id.status)
            TextView status;
            @BindView(R.id.packageQty)
            TextView packageQty;
            @BindView(R.id.volume)
            TextView volume;
            @BindView(R.id.weight)
            TextView weight;
            @BindView(R.id.delete)
            Button delete;

            public ViewHolder(View view){
                ButterKnife.bind(this,view);
            }
        }
    }

    private void deleteDeliveryTask(int position) {
        dialog = DialogTool.showLoadingDialog(DispatcherOrderModifyActivity.this,"删除中");
        LoadingOrderOperationPo orderOperationPo = new LoadingOrderOperationPo();
        SysUserListDTO userListDTO = TLApplication.getInstance().getSysUserListDTO();
        orderOperationPo.setAppUserId(userListDTO.getAppUserId());
        orderOperationPo.setOperationTime(new Date());
        orderOperationPo.setOperationUserName(userListDTO.getUsername());
        orderOperationPo.setLdOrderId(loadingOrderListDto.getLdOrderId());
        orderOperationPo.setOperationUserId(userListDTO.getUserId());
        List<String> taskIds = new ArrayList<>();
        DeliveryTaskListDto listDto = deliveryTaskListDtos.get(position);
        taskIds.add(listDto.getTaskId());
        orderOperationPo.setTaskIds(taskIds);
        RetrofitFactory.getApiService().removeTsOrder(orderOperationPo).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(dialog!=null){
                    dialog.cancel();
                }
                Result result = response.body();
                if(result!=null && result.isSuccess()){
                    Toast.makeText(DispatcherOrderModifyActivity.this, "减单成功", Toast.LENGTH_LONG).show();
                    reload();
                }else{
                    Toast.makeText(DispatcherOrderModifyActivity.this, "减单失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                if(dialog!=null){
                    dialog.cancel();
                }
                Toast.makeText(DispatcherOrderModifyActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void reload() {
        Map<String, String> params = new HashMap<>();
        params.put("appUserId",TLApplication.getInstance().getSysUserListDTO().getAppUserId());
        params.put("ldOrderNo",loadingOrderListDto.getLdOrderNo());
        RetrofitFactory.getApiService().getByNo(params).enqueue(new Callback<Result<LoadingOrderListDto>>() {
            @Override
            public void onResponse(Call<Result<LoadingOrderListDto>> call, Response<Result<LoadingOrderListDto>> response) {
                Result<LoadingOrderListDto> result = response.body();
                if(result!=null && result.isSuccess()){
                    loadingOrderListDto = result.getData();
                    if(loadingOrderListDto!=null){
                        //showView();
                        deliveryTaskListDtos = loadingOrderListDto.getDeliveryTaskListDtoList();
                        if(deliveryTaskListDtos!=null){
                            deliveryTaskAdapter.notifyDataSetChanged();
                        }
                    }
                }else{
                    Toast.makeText(DispatcherOrderModifyActivity.this, "刷新页面失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result<LoadingOrderListDto>> call, Throwable t) {
                Toast.makeText(DispatcherOrderModifyActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
