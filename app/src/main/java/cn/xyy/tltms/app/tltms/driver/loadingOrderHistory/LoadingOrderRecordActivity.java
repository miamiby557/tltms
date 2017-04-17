package cn.xyy.tltms.app.tltms.driver.loadingOrderHistory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.xyy.tltms.app.data.model.LoadingOrderListDto;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.TLApplication;
import cn.xyy.tltms.app.tltms.driver.loadingOrder.LoadingOrderActivity;
import cn.xyy.tltms.app.tltms.driver.loadingOrder.LoadingOrderViewActivity;
import cn.xyy.tltms.app.utils.DateUtil;
import cn.xyy.tltms.app.utils.NumberUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingOrderRecordActivity extends BaseActivity{

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.line)
    LinearLayout line;
    @BindView(R.id.loadMore)
    Button loadMore;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.startDate)
    TextView startDate;
    @BindView(R.id.totalFee)
    TextView totalFee;
    @BindView(R.id.totalPackageQty)
    TextView totalPackageQty;
    @BindView(R.id.totalVolume)
    TextView totalVolume;
    @BindView(R.id.totalWeight)
    TextView totalWeight;
    @BindView(R.id.originCity)
    TextView originCity;
    @BindView(R.id.destCity)
    TextView destCity;
    @BindView(R.id.loadingNumber)
    TextView loadingNumber;
    LoadingOrderListDto loadingOrder;
    List<LoadingOrderListDto> completedLoadingOrders;
    private LoadingOrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_order_record);

    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null!=getSupportActionBar()){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("配载单列表");
        completedLoadingOrders = new ArrayList<>();
        adapter = new LoadingOrderAdapter();
        listView.setAdapter(adapter);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loadingOrder==null){
                    Toast.makeText(LoadingOrderRecordActivity.this, "当前没有配载任务", Toast.LENGTH_SHORT).show();
                    return;
                }
                openActivity(LoadingOrderActivity.class);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkLoadingOrder(position);
            }
        });
    }

    private void checkLoadingOrder(int position) {
        LoadingOrderListDto loadingOrderListDto = completedLoadingOrders.get(position);
        Intent intent = new Intent(LoadingOrderRecordActivity.this, LoadingOrderViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("dto",loadingOrderListDto);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void initData() {
        getOrder();
        getCompletedLoadingOrder();
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            completedLoadingOrders = new ArrayList<>();
            getOrder();
            getCompletedLoadingOrder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.loadMore})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loadMore:
                openActivity(LoadMoreLdOrderRecordActivity.class);
                break;
        }
    }

    private void getOrder() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", TLApplication.getInstance().getSysUserListDTO().getUserId());
        RetrofitFactory.getApiService().findActiveByAssignedTo(params).enqueue(new Callback<Result<List<LoadingOrderListDto>>>() {
            @Override
            public void onResponse(Call<Result<List<LoadingOrderListDto>>> call, Response<Result<List<LoadingOrderListDto>>> response) {
                try {
                    List<LoadingOrderListDto> data = response.body().getData();
                    if(data==null|| data.size()==0){
                        loadingNumber.setText("当前没有配载任务");
                        status.setText("");
                        startDate.setText("");
                        totalFee.setText("");
                        totalPackageQty.setText("");
                        totalVolume.setText("");
                        totalWeight.setText("");
                        originCity.setText("");
                        destCity.setText("");
                    }else{
                        loadingOrder = data.get(0);
                        loadingNumber.setText(loadingOrder.getLdOrderNo());
                        status.setText(loadingOrder.getStatus().getText());
                        startDate.setText(DateUtil.getDate(loadingOrder.getCreateTime()));
                        totalFee.setText(NumberUtil.getValue(loadingOrder.getTotalFee())+"元");
                        totalPackageQty.setText(NumberUtil.getValue(loadingOrder.getTotalPackageQty()) + "箱");
                        totalVolume.setText(NumberUtil.getValue(loadingOrder.getTotalVolume()) + "立方");
                        totalWeight.setText(NumberUtil.getValue(loadingOrder.getTotalWeight()) + "公斤");
                        originCity.setText("始:"+loadingOrder.getOriginCity()+"");
                        destCity.setText("终:"+loadingOrder.getDestCity()+"");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("11111",e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Result<List<LoadingOrderListDto>>> call, Throwable t) {
                Toast.makeText(LoadingOrderRecordActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCompletedLoadingOrder() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", TLApplication.getInstance().getSysUserListDTO().getUserId());
        params.put("page", "0");
        RetrofitFactory.getApiService().findFinishedByAssignedTo(params).enqueue(new Callback<Result<List<LoadingOrderListDto>>>() {
            @Override
            public void onResponse(Call<Result<List<LoadingOrderListDto>>> call, Response<Result<List<LoadingOrderListDto>>> response) {
                try {
                    List<LoadingOrderListDto> data = response.body().getData();
                    if(data!=null && data.size()>0){
                        completedLoadingOrders = data;
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Result<List<LoadingOrderListDto>>> call, Throwable t) {

            }
        });
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

    class LoadingOrderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return completedLoadingOrders.size();
        }

        @Override
        public Object getItem(int position) {
            return completedLoadingOrders.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            try {
                if (null == convertView) {
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_history, parent, false);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                final LoadingOrderListDto dto = completedLoadingOrders.get(position);
                holder.loadingNumber.setText(dto.getLdOrderNo());
                holder.startDate.setText(DateUtil.getDate(dto.getCreateTime()));
                holder.totalFee.setText(NumberUtil.getValue(dto.getTotalFee())+"元");
                holder.status.setText(dto.getStatus().getText());
                holder.totalPackageQty.setText(NumberUtil.getValue(dto.getTotalPackageQty()) + "箱");
                holder.totalVolume.setText(NumberUtil.getValue(dto.getTotalVolume()) + "立方");
                holder.totalWeight.setText(NumberUtil.getValue(dto.getTotalWeight()) + "公斤");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    class ViewHolder {
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.startDate)
        TextView startDate;
        @BindView(R.id.totalFee)
        TextView totalFee;
        @BindView(R.id.totalPackageQty)
        TextView totalPackageQty;
        @BindView(R.id.totalVolume)
        TextView totalVolume;
        @BindView(R.id.totalWeight)
        TextView totalWeight;
        @BindView(R.id.loadingNumber)
        TextView loadingNumber;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }


}
