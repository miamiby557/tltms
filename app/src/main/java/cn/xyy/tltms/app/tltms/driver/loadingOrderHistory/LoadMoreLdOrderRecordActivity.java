package cn.xyy.tltms.app.tltms.driver.loadingOrderHistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
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

public class LoadMoreLdOrderRecordActivity extends BaseActivity implements SwipyRefreshLayout.OnRefreshListener{

    @BindView(R.id.ldOrderNo)
    EditText ldOrderNo;
    @BindView(R.id.project_srl)
    SwipyRefreshLayout project_srl;
    @BindView(R.id.listView)
    ListView listView;
    LoadingOrderAdapter adapter;
    int currentPage = 0;
    List<LoadingOrderListDto> completedLoadingOrders;
    java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadmore_ld_order_record);
        project_srl.setOnRefreshListener(this);
    }

    @Override
    protected void initViews() {
        completedLoadingOrders = new ArrayList<>();
        adapter = new LoadingOrderAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkLoadingOrder(position);
            }
        });
    }

    @OnClick({R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                try {
                    completedLoadingOrders = new ArrayList();
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                initData();
                break;
        }
    }

    private void checkLoadingOrder(int position) {
        LoadingOrderListDto loadingOrderListDto = completedLoadingOrders.get(position);
        Intent intent = new Intent(LoadMoreLdOrderRecordActivity.this, LoadingOrderViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("dto",loadingOrderListDto);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void initData() {
        getCompletedLoadingOrder();
    }

    private void getCompletedLoadingOrder() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", TLApplication.getInstance().getSysUserListDTO().getUserId());
        params.put("page", currentPage+"");
        if(ldOrderNo.getText().toString().length()>0){
            params.put("ldOrderNo",ldOrderNo.getText().toString());
        }
        RetrofitFactory.getApiService().findFinishedByAssignedTo(params).enqueue(new Callback<Result<List<LoadingOrderListDto>>>() {
            @Override
            public void onResponse(Call<Result<List<LoadingOrderListDto>>> call, Response<Result<List<LoadingOrderListDto>>> response) {
                try {
                    List<LoadingOrderListDto> data = response.body().getData();
                    if(data!=null && data.size()>0){
                        completedLoadingOrders.addAll(data);
                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(LoadMoreLdOrderRecordActivity.this, "没有更多记录", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoadMoreLdOrderRecordActivity.this, "解析返回数据异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result<List<LoadingOrderListDto>>> call, Throwable t) {
                Toast.makeText(LoadMoreLdOrderRecordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                currentPage = 0;
                initData();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            project_srl.postDelayed(new Runnable() {

                @Override
                public void run() {

                    currentPage = 0;
                    completedLoadingOrders = new ArrayList();
                    // 加载完后调用该方法
                    if(project_srl==null){
                        return;
                    }
                    project_srl.setRefreshing(false);
                    initData();
                }
            }, 2000);
        }else {
            project_srl.postDelayed(new Runnable() {

                @Override
                public void run() {

                    currentPage++;
                    // 加载完后调用该方法
                    if(project_srl==null){
                        return;
                    }
                    project_srl.setRefreshing(false);
                    initData();
                }
            }, 2000);
        }
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
            if (null == convertView) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_history, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final LoadingOrderListDto dto = completedLoadingOrders.get(position);
            if(dto!=null){
                holder.loadingNumber.setText(dto.getLdOrderNo());
                holder.startDate.setText(DateUtil.getDate(dto.getCreateTime()));
                holder.status.setText(dto.getStatus().getText());
                holder.totalFee.setText(NumberUtil.getValue(dto.getTotalFee())+"元");
                holder.totalPackageQty.setText(dto.getTotalPackageQty() + "箱");
                holder.totalVolume.setText(df.format(dto.getTotalVolume()) + "立方");
                holder.totalWeight.setText(df.format(dto.getTotalWeight()) + "公斤");
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
