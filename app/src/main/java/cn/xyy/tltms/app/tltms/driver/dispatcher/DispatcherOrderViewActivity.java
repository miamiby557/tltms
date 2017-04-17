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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.DeliveryTaskListDto;
import cn.xyy.tltms.app.data.model.DeliveryTaskLogListDto;
import cn.xyy.tltms.app.data.model.LoadingOrderListDto;
import cn.xyy.tltms.app.data.model.TransportationOrderListDTO;
import cn.xyy.tltms.app.data.model.TransportationOrderSummaryDTO;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.utils.DateUtil;
import cn.xyy.tltms.app.utils.NumberUtil;
import cn.xyy.tltms.app.widget.DialogTool;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 2017/3/28.
 */

public class DispatcherOrderViewActivity extends BaseActivity{
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
    @BindView(R.id.logList)
    ListView logList;
    @BindView(R.id.count)
    TextView count;
    List<DeliveryTaskListDto> deliveryTaskListDtos;
    List<DeliveryTaskLogListDto> logListDtos;
    DeliveryTaskAdapter deliveryTaskAdapter;
    LogAdapter logAdapter;
    LoadingOrderListDto loadingOrderListDto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher_order_view);
        deliveryTaskListDtos = new ArrayList<>();
        logListDtos = new ArrayList<>();
        deliveryTaskAdapter = new DeliveryTaskAdapter();
        Intent intent = getIntent();
        loadingOrderListDto = (LoadingOrderListDto)intent.getSerializableExtra("dto");
        listView.setAdapter(deliveryTaskAdapter);
        logAdapter = new LogAdapter();
        logList.setAdapter(logAdapter);
        if(loadingOrderListDto.getDeliveryTaskListDtoList()!=null){
            deliveryTaskListDtos.addAll(loadingOrderListDto.getDeliveryTaskListDtoList());
            deliveryTaskAdapter.notifyDataSetChanged();
        }
        if(loadingOrderListDto.getLogListDtos()!=null){
            logListDtos = loadingOrderListDto.getLogListDtos();
            logAdapter.notifyDataSetChanged();
        }
        showView();
        count.setText("配载信息("+deliveryTaskListDtos.size()+")");
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
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("配载单查看");
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
    protected void initData() {
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
                convertView = LayoutInflater.from(DispatcherOrderViewActivity.this).inflate(R.layout.adapter_delivery_task_item_view, parent, false);
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

            public ViewHolder(View view){
                ButterKnife.bind(this,view);
            }
        }
    }
    class LogAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return logListDtos.size();
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
                convertView = LayoutInflater.from(DispatcherOrderViewActivity.this).inflate(R.layout.adapter_task_log, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            DeliveryTaskLogListDto logListDto = logListDtos.get(position);
            holder.tsOrderNo.setText(logListDto.getTsOrderNo());
            holder.operationStatus.setText(logListDto.getOperationStatus().getText());
            holder.operationTime.setText(DateUtil.getDate(logListDto.getOperationTime()));
            holder.operationUserName.setText("操作人:"+logListDto.getOperationUserName());
            return convertView;
        }
        class ViewHolder {
            @BindView(R.id.operationTime)
            TextView operationTime;
            @BindView(R.id.operationUserName)
            TextView operationUserName;
            @BindView(R.id.operationStatus)
            TextView operationStatus;
            @BindView(R.id.tsOrderNo)
            TextView tsOrderNo;
            public ViewHolder(View view){
                ButterKnife.bind(this,view);
            }
        }
    }


}
