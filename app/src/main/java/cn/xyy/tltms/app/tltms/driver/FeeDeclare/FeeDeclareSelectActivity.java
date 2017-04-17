package cn.xyy.tltms.app.tltms.driver.FeeDeclare;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.DeliveryTaskListDto;
import cn.xyy.tltms.app.data.model.LoadingOrderListDto;
import cn.xyy.tltms.app.data.model.TransportationOrderListDTO;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.TLApplication;
import cn.xyy.tltms.app.utils.NumberUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 2017/3/28.
 */

public class FeeDeclareSelectActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.edtLdOrderNo)
    EditText edtLdOrderNo;
    @BindView(R.id.search)
    Button search;
    @BindView(R.id.ldOrderNo)
    TextView ldOrderNo;
    @BindView(R.id.totalPackageQty)
    TextView totalPackageQty;
    @BindView(R.id.totalVolume)
    TextView totalVolume;
    @BindView(R.id.totalWeight)
    TextView totalWeight;
    @BindView(R.id.createTime)
    TextView createTime;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.originCity)
    TextView originCity;
    @BindView(R.id.destCity)
    TextView destCity;
    @BindView(R.id.listView)
    ListView listView;
    List<DeliveryTaskListDto> deliveryTaskListDtos;
    LoadingOrderListDto loadingOrderListDto;
    FeeDeclareAdapter feeDeclareAdapter;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_declare);
        deliveryTaskListDtos = new ArrayList<>();
        feeDeclareAdapter = new FeeDeclareAdapter();
        listView.setAdapter(feeDeclareAdapter);
        ldOrderNo.setText("输入配载单号查询");
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
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("费用申报");
    }

    private void search(){
        if(edtLdOrderNo.getText().toString().length()==0){
            Toast.makeText(FeeDeclareSelectActivity.this, "输入配载单号查询", Toast.LENGTH_LONG).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("ldOrderNo",edtLdOrderNo.getText().toString());
        params.put("appUserId",TLApplication.getInstance().getSysUserListDTO().getAppUserId());
        RetrofitFactory.getApiService().getByNo(params).enqueue(new Callback<Result<LoadingOrderListDto>>() {
            @Override
            public void onResponse(Call<Result<LoadingOrderListDto>> call, Response<Result<LoadingOrderListDto>> response) {
                Result<LoadingOrderListDto> result = response.body();
                if(result.isSuccess()){
                    loadingOrderListDto = result.getData();
                    if(loadingOrderListDto!=null){
                        showView();
                        deliveryTaskListDtos = loadingOrderListDto.getDeliveryTaskListDtoList();
                        if(deliveryTaskListDtos!=null && deliveryTaskListDtos.size()>0){
                            count.setText(deliveryTaskListDtos.size()+"");
                            feeDeclareAdapter.notifyDataSetChanged();
                        }
                    }else {
                        clearView();
                        Toast.makeText(FeeDeclareSelectActivity.this, "没有记录", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(FeeDeclareSelectActivity.this, result.getMessage()+"", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result<LoadingOrderListDto>> call, Throwable t) {

            }
        });
    }

    private void clearView() {
        ldOrderNo.setText("输入配载单号查询");
        totalPackageQty.setText("");
        totalVolume.setText("");
        totalWeight.setText("");
        createTime.setText("");
        originCity.setText("");
        destCity.setText("");
        deliveryTaskListDtos = new ArrayList();
        feeDeclareAdapter.notifyDataSetChanged();
        count.setText("0");
    }

    @Override
    protected void initData() {

    }

    private void showView() {
        if(loadingOrderListDto!=null){
            try {
                ldOrderNo.setText(loadingOrderListDto.getLdOrderNo());
                totalPackageQty.setText(loadingOrderListDto.getTotalPackageQty()+"箱");
                totalVolume.setText(NumberUtil.getValue(loadingOrderListDto.getTotalVolume())+"立方");
                totalWeight.setText(NumberUtil.getValue(loadingOrderListDto.getTotalWeight())+"公斤");
                createTime.setText(df.format(loadingOrderListDto.getCreateTime()));
                originCity.setText("始:"+loadingOrderListDto.getOriginCity());
                destCity.setText("终:"+loadingOrderListDto.getDestCity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                search();
                break;
        }
    }

    class FeeDeclareAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(FeeDeclareSelectActivity.this).inflate(R.layout.adapter_transport_order_fee_declare, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            DeliveryTaskListDto deliveryTaskListDto = deliveryTaskListDtos.get(position);
            holder.ldOrderNo.setText(deliveryTaskListDto.getTsOrderNo());
            holder.totalVolume.setText(NumberUtil.getValue(deliveryTaskListDto.getVolume())+"立方");
            holder.totalWeight.setText(NumberUtil.getValue(deliveryTaskListDto.getWeight())+"公斤");
            holder.packageQty.setText(NumberUtil.getValue(deliveryTaskListDto.getPackageQty())+"箱");
            holder.declare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeliveryTaskListDto taskListDto = deliveryTaskListDtos.get(position);
                    Intent intent = new Intent(FeeDeclareSelectActivity.this,FeeDeclareCreateActivity.class);
                    intent.putExtra("tsOrderNo",taskListDto.getTsOrderNo());
                    startActivity(intent);
                }
            });
            return convertView;
        }
        class ViewHolder{
            @BindView(R.id.ldOrderNo)
            TextView ldOrderNo;
            @BindView(R.id.totalVolume)
            TextView totalVolume;
            @BindView(R.id.packageQty)
            TextView packageQty;
            @BindView(R.id.totalWeight)
            TextView totalWeight;
            @BindView(R.id.declare)
            Button declare;
            public ViewHolder(View convertView){
                ButterKnife.bind(this,convertView);
            }
        }
    }
}
