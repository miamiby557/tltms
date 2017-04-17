package cn.xyy.tltms.app.tltms.driver.loadingOrder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.DeliveryTaskListDto;
import cn.xyy.tltms.app.data.model.DeliveryTaskLogListDto;
import cn.xyy.tltms.app.data.model.LoadedOperationDto;
import cn.xyy.tltms.app.data.model.LoadingOrderEnum;
import cn.xyy.tltms.app.data.model.LoadingOrderListDto;
import cn.xyy.tltms.app.data.model.LoadingOrderOperationPo;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.TLApplication;
import cn.xyy.tltms.app.tltms.driver.FeeDeclare.FeeDeclareCreateActivity;
import cn.xyy.tltms.app.tltms.driver.signUp.SignUpActivity;
import cn.xyy.tltms.app.utils.DateUtil;
import cn.xyy.tltms.app.utils.NumberUtil;
import cn.xyy.tltms.app.widget.DialogTool;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingOrderViewActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvIdOrderNo)
    TextView tvIdOrderNo;
    @BindView(R.id.tvCreateTime)
    TextView tvCreateTime;
    @BindView(R.id.tvTotalPackageQty)
    TextView tvTotalPackageQty;
    @BindView(R.id.tvTotalVolume)
    TextView tvTotalVolume;
    @BindView(R.id.tvTotalWeight)
    TextView tvTotalWeight;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.listView)
    SwipeMenuListView listView;
    @BindView(R.id.logListView)
    ListView logListView;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.originCity)
    TextView originCity;
    @BindView(R.id.destCity)
    TextView destCity;
    LoadingOrderListDto loadingOrder;
    List<DeliveryTaskListDto> deliveryTaskListDtos;
    List<DeliveryTaskLogListDto> logListDtos;
    DeliveryTaskAdapter adapter;
    LogAdapter logAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_order);
        deliveryTaskListDtos = new ArrayList<>();
        logListDtos = new ArrayList<>();
        Intent intent = getIntent();
        loadingOrder = (LoadingOrderListDto)intent.getSerializableExtra("dto");
        adapter = new DeliveryTaskAdapter();
        listView.setAdapter(adapter);
        if(loadingOrder.getDeliveryTaskListDtoList()!=null){
            deliveryTaskListDtos = loadingOrder.getDeliveryTaskListDtoList();
            adapter.notifyDataSetChanged();
        }
        logAdapter = new LogAdapter();
        logListView.setAdapter(logAdapter);
        if(loadingOrder.getLogListDtos()!=null){
            logListDtos = loadingOrder.getLogListDtos();
            logAdapter.notifyDataSetChanged();
        }
        showView();
    }

    private void showView() {
        tvTitle.setText("配载单:"+loadingOrder.getLdOrderNo());
        tvIdOrderNo.setText(loadingOrder.getLdOrderNo());
        tvCreateTime.setText(DateUtil.getDate(loadingOrder.getCreateTime()));
        status.setText(loadingOrder.getStatus().getText());
        tvTotalPackageQty.setText(NumberUtil.getValue(loadingOrder.getTotalPackageQty()) + "箱");
        tvTotalVolume.setText(NumberUtil.getValue(loadingOrder.getTotalVolume()) + "立方");
        tvTotalWeight.setText(NumberUtil.getValue(loadingOrder.getTotalWeight()) + "公斤");
        originCity.setText("始:"+loadingOrder.getOriginCity()+"");
        destCity.setText("终:"+loadingOrder.getDestCity()+"");

    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    protected void initData() {

    }

    private void feeDeclare(int position) {
        DeliveryTaskListDto listDto = deliveryTaskListDtos.get(position);
        Intent intent = new Intent(LoadingOrderViewActivity.this,FeeDeclareCreateActivity.class);
        intent.putExtra("tsOrderNo",listDto.getTsOrderNo());
        startActivity(intent);
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

    }

    class DeliveryTaskAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return deliveryTaskListDtos.size();
        }

        @Override
        public Object getItem(int position) {
            return deliveryTaskListDtos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(LoadingOrderViewActivity.this).inflate(R.layout.delivery_task_record_layout, parent, false);
                holder = new ViewHolder();
                holder.tvIdOrderNo = (TextView) convertView.findViewById(R.id.tvIdOrderNo);
                holder.tvTatus = (TextView) convertView.findViewById(R.id.tvTatus);
                holder.tvlPackageQty = (TextView) convertView.findViewById(R.id.tvlPackageQty);
                holder.tvVolume = (TextView) convertView.findViewById(R.id.tvVolume);
                holder.tvWeight = (TextView) convertView.findViewById(R.id.tvWeight);
                holder.feeDeclare = (Button)convertView.findViewById(R.id.fee_declare);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final DeliveryTaskListDto deliveryTaskListDto = deliveryTaskListDtos.get(position);
            holder.tvIdOrderNo.setText(deliveryTaskListDto.getTsOrderNo());
            holder.tvTatus.setText(deliveryTaskListDto.getStatus().getText());
            holder.tvlPackageQty.setText(NumberUtil.getValue(deliveryTaskListDto.getPackageQty()) + "箱");
            holder.tvVolume.setText(NumberUtil.getValue(deliveryTaskListDto.getVolume()) + "立方");
            holder.tvWeight.setText(NumberUtil.getValue(deliveryTaskListDto.getWeight()) + "公斤");
            int days = DateUtil.daysBetween(deliveryTaskListDto.getFinishTime(),new Date());
            if(days>30){
                holder.feeDeclare.setVisibility(View.GONE);
            }
            holder.feeDeclare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoadingOrderViewActivity.this,FeeDeclareCreateActivity.class);
                    intent.putExtra("tsOrderNo",deliveryTaskListDto.getTsOrderNo());
                    startActivity(intent);
                }
            });
            return convertView;
        }
        class ViewHolder {
            private TextView tvIdOrderNo, tvTatus, tvlPackageQty, tvVolume, tvWeight;
            private Button feeDeclare;
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
                convertView = LayoutInflater.from(LoadingOrderViewActivity.this).inflate(R.layout.adapter_task_log, parent, false);
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
