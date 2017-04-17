package cn.xyy.tltms.app.tltms.driver.TransportOrder;

import android.app.Dialog;
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

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.TransportOrderStatusDto;
import cn.xyy.tltms.app.data.model.TransportationOrderListDTO;
import cn.xyy.tltms.app.data.model.TransportationOrderTraceListDTO;
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
 * Created by dev on 2017/3/30.
 */

public class TransportOrderSelectActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.edtLdOrderNo)
    EditText edtLdOrderNo;
    @BindView(R.id.search)
    Button search;
    @BindView(R.id.tsOrderNo)
    TextView tsOrderNo;
    @BindView(R.id.projectName)
    TextView projectName;
    @BindView(R.id.orderDate)
    TextView orderDate;
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
    @BindView(R.id.listView)
    ListView listView;
    OrderStatusAdapter orderStatusAdapter;

    List<TransportationOrderTraceListDTO> traceListDTOs;
    TransportationOrderListDTO transportationOrderListDTO;
    Dialog dialog;

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_order_status);

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
        tvTitle.setText("运单查询");
        clearView();
        traceListDTOs = new ArrayList<>();
        orderStatusAdapter = new OrderStatusAdapter();
        listView.setAdapter(orderStatusAdapter);
        orderStatusAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                if(edtLdOrderNo.getText().toString().length()==0){
                    Toast.makeText(TransportOrderSelectActivity.this, "输入运单号查询", Toast.LENGTH_LONG).show();
                    return;
                }
                initData();
                break;
        }
    }

    private void findTraceInfoByTsOrder(String tsOrderNo){
        Map<String, String> params = new HashMap<>();
        params.put("tsOrderNo",tsOrderNo);
        params.put("appUserId", TLApplication.getInstance().getSysUserListDTO().getAppUserId());
        RetrofitFactory.getApiService().trackingInfoOfTsOrder(params).enqueue(new Callback<Result<List<TransportationOrderTraceListDTO>>>() {
            @Override
            public void onResponse(Call<Result<List<TransportationOrderTraceListDTO>>> call, Response<Result<List<TransportationOrderTraceListDTO>>> response) {
                Result<List<TransportationOrderTraceListDTO>> result = response.body();
                if(result!=null && result.isSuccess()){
                    traceListDTOs = result.getData();
                    if(traceListDTOs!=null){
                        orderStatusAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(TransportOrderSelectActivity.this, "未查到信息记录", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(TransportOrderSelectActivity.this, "未查到信息记录", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result<List<TransportationOrderTraceListDTO>>> call, Throwable t) {
                Toast.makeText(TransportOrderSelectActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void initData() {
        if(edtLdOrderNo.getText().toString().length()==0){
            return;
        }
        clearView();
        dialog = DialogTool.showLoadingDialog(TransportOrderSelectActivity.this,"查找中...");
        traceListDTOs = new ArrayList<>();
        orderStatusAdapter.notifyDataSetChanged();
        Map<String, String> params = new HashMap<>();
        params.put("tsOrderNo",edtLdOrderNo.getText().toString());
        params.put("userId",TLApplication.getInstance().getSysUserListDTO().getUserId());
        RetrofitFactory.getApiService().findByTsOrderNo(params).enqueue(new Callback<Result<TransportationOrderListDTO>>() {
            @Override
            public void onResponse(Call<Result<TransportationOrderListDTO>> call, Response<Result<TransportationOrderListDTO>> response) {
                if(dialog!=null){
                    dialog.cancel();
                }
                Result<TransportationOrderListDTO> result = response.body();
                if(result!=null && result.isSuccess()){
                    transportationOrderListDTO = result.getData();
                    if(transportationOrderListDTO!=null){
                        showView();
                        findTraceInfoByTsOrder(transportationOrderListDTO.getTsOrderNo());
                    }else{
                        Toast.makeText(TransportOrderSelectActivity.this, "未查到运单记录", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(TransportOrderSelectActivity.this, "未查到运单记录", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result<TransportationOrderListDTO>> call, Throwable t) {
                if(dialog!=null){
                    dialog.cancel();
                }
                Toast.makeText(TransportOrderSelectActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearView() {
        tsOrderNo.setText("输入运单查询");
        projectName.setText("");
        orderDate.setText("");
        consignorCity.setText("");
        consigneeCity.setText("");
        totalPackageQty.setText("");
        totalVolume.setText("");
        totalWeight.setText("");
    }

    private void showView() {
        tsOrderNo.setText(transportationOrderListDTO.getTsOrderNo());
        projectName.setText(transportationOrderListDTO.getProjectName());
        orderDate.setText(DateUtil.getDate(transportationOrderListDTO.getOrderDate()));
        consignorCity.setText("始:"+transportationOrderListDTO.getOrderAddressDTO().getConsignorCity());
        consigneeCity.setText("终:"+transportationOrderListDTO.getOrderAddressDTO().getConsigneeCity());
        totalPackageQty.setText(NumberUtil.getValue(transportationOrderListDTO.getTotalPackageQty())+"箱");
        totalVolume.setText(NumberUtil.getValue(transportationOrderListDTO.getTotalVolume())+"立方");
        totalWeight.setText(NumberUtil.getValue(transportationOrderListDTO.getTotalWeight())+"公斤");

    }

    class OrderStatusAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return traceListDTOs.size();
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
                convertView = LayoutInflater.from(TransportOrderSelectActivity.this).inflate(R.layout.adapter_transport_order_status, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            TransportationOrderTraceListDTO listDTO = traceListDTOs.get(position);
            holder.time.setText(df.format(listDTO.getTraceTime()));
            holder.status.setText(listDTO.getTraceContent());
            holder.traceUserName.setText(listDTO.getTraceUserName());
            return convertView;
        }

        class ViewHolder{
            @BindView(R.id.time)
            TextView time;
            @BindView(R.id.status)
            TextView status;
            @BindView(R.id.traceUserName)
            TextView traceUserName;
            public ViewHolder(View view){
                ButterKnife.bind(this,view);
            }
        }
    }
}
