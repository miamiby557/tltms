package cn.xyy.tltms.app.tltms.driver.dispatcher;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
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
import cn.xyy.tltms.app.data.model.LoadingOrderCancelDto;
import cn.xyy.tltms.app.data.model.LoadingOrderEnum;
import cn.xyy.tltms.app.data.model.LoadingOrderListDto;
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

public class DispatcherLoadingOrderRecordActivity extends BaseActivity implements SwipyRefreshLayout.OnRefreshListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.search_text)
    EditText search_text;
    @BindView(R.id.search)
    Button search;
    @BindView(R.id.project_srl)
    SwipyRefreshLayout project_srl;
    @BindView(R.id.listView)
    SwipeMenuListView listView;
    List<LoadingOrderListDto> orderListDtos;
    LoadingOrderAdapter loadingOrderAdapter;
    int currentPage = 0;
    Dialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disptcher_loading_order);

    }

    private void cancel(String reason,LoadingOrderListDto loadingOrderListDto) {
        dialog = DialogTool.showLoadingDialog(DispatcherLoadingOrderRecordActivity.this,"取消中...");
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
                    Toast.makeText(DispatcherLoadingOrderRecordActivity.this, "取消成功", Toast.LENGTH_LONG).show();
                    onResume();
                }else{
                    Toast.makeText(DispatcherLoadingOrderRecordActivity.this, "取消失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(DispatcherLoadingOrderRecordActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("配载单列表");
        project_srl.setOnRefreshListener(this);
        orderListDtos = new ArrayList<>();
        loadingOrderAdapter = new LoadingOrderAdapter();
        listView.setAdapter(loadingOrderAdapter);
    }

    @OnClick({R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                String text = search_text.getText().toString();
                if(text.length()==0){
                    Toast.makeText(DispatcherLoadingOrderRecordActivity.this, "输入搜索条件", Toast.LENGTH_LONG).show();
                    return;
                }
                initData();
                break;
        }
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
        currentPage = 0;
        orderListDtos = new ArrayList<>();
        loadingOrderAdapter.notifyDataSetChanged();
        initData();
    }

    @Override
    protected void initData() {
        String text = search_text.getText().toString();
        Map<String, String> params = new HashMap<>();
        params.put("appUserId", TLApplication.getInstance().getSysUserListDTO().getAppUserId());
        params.put("vehicleNo",text);
        params.put("page", currentPage+"");
        RetrofitFactory.getApiService().findActive(params).enqueue(new Callback<Result<List<LoadingOrderListDto>>>() {
            @Override
            public void onResponse(Call<Result<List<LoadingOrderListDto>>> call, Response<Result<List<LoadingOrderListDto>>> response) {
                Result<List<LoadingOrderListDto>> result =  response.body();
                try {
                    if (null != result && result.isSuccess()) {
                        if(result.getData()==null || result.getData().size()==0){
                            Toast.makeText(DispatcherLoadingOrderRecordActivity.this,"没有更多记录", Toast.LENGTH_LONG).show();
                            return;
                        }
                        orderListDtos.addAll(result.getData());
                        loadingOrderAdapter.notifyDataSetChanged();
                    } else if (null != result && !result.isSuccess()) {
                        Toast.makeText(DispatcherLoadingOrderRecordActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DispatcherLoadingOrderRecordActivity.this, "数据返回异常", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("findActive",e.getMessage());
                    Toast.makeText(DispatcherLoadingOrderRecordActivity.this, "解析数据出错", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result<List<LoadingOrderListDto>>> call, Throwable t) {
                Toast.makeText(DispatcherLoadingOrderRecordActivity.this, "连接服务器异常", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            project_srl.postDelayed(new Runnable() {

                @Override
                public void run() {

                    try {
                        currentPage = 0;
                        orderListDtos = new ArrayList();
                        loadingOrderAdapter.notifyDataSetChanged();
                        // 加载完后调用该方法
                        if(project_srl==null){
                            return;
                        }
                        project_srl.setRefreshing(false);
                        initData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 2000);
        }else {
            project_srl.postDelayed(new Runnable() {

                @Override
                public void run() {

                    try {
                        currentPage++;
                        // 加载完后调用该方法
                        if(project_srl==null){
                            return;
                        }
                        project_srl.setRefreshing(false);
                        initData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 2000);
        }
    }

    class LoadingOrderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return orderListDtos.size();
        }

        @Override
        public Object getItem(int position) {
            return orderListDtos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.ld_item_history, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final LoadingOrderListDto dto = orderListDtos.get(position);
            holder.loadingNumber.setText(dto.getLdOrderNo());
            holder.createTime.setText(DateUtil.getDate(dto.getCreateTime()));
            holder.vehicleNo.setText(dto.getVehicleNo());
            holder.driver.setText(dto.getDriver());
            holder.status.setText(dto.getStatus().getText());
            holder.totalPackageQty.setText(NumberUtil.getValue(dto.getTotalPackageQty()) + "箱");
            holder.totalVolume.setText(NumberUtil.getValue(dto.getTotalVolume()) + "立方");
            holder.totalWeight.setText(NumberUtil.getValue(dto.getTotalWeight()) + "公斤");
            /*if(dto.getStatus().getText().equalsIgnoreCase(LoadingOrderEnum.OrderStatus.CANCEL.getText())){
                holder.cancel.setVisibility(View.GONE);
            }*/
            /*if(dto.getStatus().getText().equalsIgnoreCase(LoadingOrderEnum.OrderStatus.INTRANSIT.getText())){
                holder.modify.setVisibility(View.GONE);
            }else{
                holder.modify.setVisibility(View.VISIBLE);
            }*/
            if(dto.getStatus().getText().equalsIgnoreCase(LoadingOrderEnum.OrderStatus.NEW.getText())|| dto.getStatus().getText().equalsIgnoreCase(LoadingOrderEnum.OrderStatus.ASSIGNED.getText())
                || dto.getStatus().getText().equalsIgnoreCase(LoadingOrderEnum.OrderStatus.ACCEPTED.getText())||dto.getStatus().getText().equalsIgnoreCase(LoadingOrderEnum.OrderStatus.LOADING.getText())
                    || dto.getStatus().getText().equalsIgnoreCase(LoadingOrderEnum.OrderStatus.LOADED.getText())){
                holder.cancel.setVisibility(View.VISIBLE);
                holder.modify.setVisibility(View.VISIBLE);
            }else{
                holder.cancel.setVisibility(View.GONE);
                holder.modify.setVisibility(View.GONE);
            }

            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dto.getStatus().getText().equalsIgnoreCase(LoadingOrderEnum.OrderStatus.CANCEL.getText())){
                        Toast.makeText(DispatcherLoadingOrderRecordActivity.this, "已经取消", Toast.LENGTH_LONG).show();
                    }else{
                        dialog = DialogTool.showReasonDialog(DispatcherLoadingOrderRecordActivity.this,new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                                EditText reason = (EditText)dialog.findViewById(R.id.reason);
                                cancel(reason.getText().toString(),dto);
                            }
                        });
                    }
                }
            });
            holder.modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DispatcherLoadingOrderRecordActivity.this,DispatcherOrderModifyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dto",dto);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            holder.check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DispatcherLoadingOrderRecordActivity.this,DispatcherOrderViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dto",dto);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.createTime)
        TextView createTime;
        @BindView(R.id.vehicleNo)
        TextView vehicleNo;
        @BindView(R.id.driver)
        TextView driver;
        @BindView(R.id.totalPackageQty)
        TextView totalPackageQty;
        @BindView(R.id.totalVolume)
        TextView totalVolume;
        @BindView(R.id.totalWeight)
        TextView totalWeight;
        @BindView(R.id.loadingNumber)
        TextView loadingNumber;
        @BindView(R.id.cancel)
        Button cancel;
        @BindView(R.id.modify)
        Button modify;
        @BindView(R.id.check)
        Button check;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
