package cn.xyy.tltms.app.tltms.driver.loadingOrder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.DeliveryTaskListDto;
import cn.xyy.tltms.app.data.model.LoadedOperationDto;
import cn.xyy.tltms.app.data.model.LoadingOrderEnum;
import cn.xyy.tltms.app.data.model.LoadingOrderListDto;
import cn.xyy.tltms.app.data.model.LoadingOrderOperationPo;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.TLApplication;
import cn.xyy.tltms.app.tltms.driver.FeeDeclare.FeeDeclareCreateActivity;
import cn.xyy.tltms.app.tltms.driver.loadingOrderHistory.LoadingOrderRecordActivity;
import cn.xyy.tltms.app.tltms.driver.signUp.SignUpActivity;
import cn.xyy.tltms.app.utils.Constant;
import cn.xyy.tltms.app.utils.DateUtil;
import cn.xyy.tltms.app.utils.NumberUtil;
import cn.xyy.tltms.app.widget.DialogTool;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingOrderActivity extends BaseActivity {

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
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.originCity)
    TextView originCity;
    @BindView(R.id.destCity)
    TextView destCity;
    LoadingOrderListDto loadingOrder;
    List<DeliveryTaskListDto> deliveryTaskListDtos;
    DeliveryTaskAdapter adapter;

    Dialog dialog;
    DateFormat df2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_order);
        deliveryTaskListDtos = new ArrayList<>();
        adapter = new DeliveryTaskAdapter();
        listView.setAdapter(adapter);
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
        getOrder();
    }

    private void feeDeclare(int position) {
        DeliveryTaskListDto listDto = deliveryTaskListDtos.get(position);
        Intent intent = new Intent(LoadingOrderActivity.this,FeeDeclareCreateActivity.class);
        intent.putExtra("tsOrderNo",listDto.getTsOrderNo());
        startActivity(intent);
    }

    private void getOrder() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", TLApplication.getInstance().getSysUserListDTO().getUserId());
        RetrofitFactory.getApiService().findActiveByAssignedTo(params).enqueue(new Callback<Result<List<LoadingOrderListDto>>>() {
            @Override
            public void onResponse(Call<Result<List<LoadingOrderListDto>>> call, Response<Result<List<LoadingOrderListDto>>> response) {
                List<LoadingOrderListDto> data = response.body().getData();
                if(data==null|| data.size()==0){
                    finish();
                }else{
                    loadingOrder = data.get(0);
                    tvTitle.setText("配载单:"+loadingOrder.getLdOrderNo());
                    tvIdOrderNo.setText(loadingOrder.getLdOrderNo());
                    tvCreateTime.setText(DateUtil.getDate(loadingOrder.getCreateTime()));
                    status.setText(loadingOrder.getStatus().getText());
                    tvTotalPackageQty.setText(NumberUtil.getValue(loadingOrder.getTotalPackageQty()) + "箱");
                    tvTotalVolume.setText(NumberUtil.getValue(loadingOrder.getTotalVolume()) + "立方");
                    tvTotalWeight.setText(NumberUtil.getValue(loadingOrder.getTotalWeight()) + "公斤");
                    originCity.setText("始:"+loadingOrder.getOriginCity()+"");
                    destCity.setText("终:"+loadingOrder.getDestCity()+"");
                    deliveryTaskListDtos = new ArrayList();
                    try {
                        if(loadingOrder.getDeliveryTaskListDtoList()!=null){
                            deliveryTaskListDtos.addAll(loadingOrder.getDeliveryTaskListDtoList());
                            count.setText(loadingOrder.getDeliveryTaskListDtoList().size()+"");
                            adapter.notifyDataSetChanged();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<List<LoadingOrderListDto>>> call, Throwable t) {
                Toast.makeText(LoadingOrderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void reload() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", TLApplication.getInstance().getSysUserListDTO().getUserId());
        RetrofitFactory.getApiService().findActiveByAssignedTo(params).enqueue(new Callback<Result<List<LoadingOrderListDto>>>() {
            @Override
            public void onResponse(Call<Result<List<LoadingOrderListDto>>> call, Response<Result<List<LoadingOrderListDto>>> response) {
                try {
                    List<LoadingOrderListDto> data = response.body().getData();
                    if(data==null|| data.size()==0){
                        //openActivity(LoadingOrderRecordActivity.class);
                    }else{
                        loadingOrder = data.get(0);
//                    tvTitle.setText(loadingOrder.getLdOrderId());
                        tvIdOrderNo.setText(loadingOrder.getLdOrderId());
                        tvCreateTime.setText(DateUtil.getDate(loadingOrder.getCreateTime()));
//                    tvTotalFee.setText(loadingOrder.getTotalFee().toString() + "元");
                        tvTotalPackageQty.setText(NumberUtil.getValue(loadingOrder.getTotalPackageQty()) + "箱");
                        tvTotalVolume.setText(NumberUtil.getValue(loadingOrder.getTotalVolume()) + "立方");
                        tvTotalWeight.setText(NumberUtil.getValue(loadingOrder.getTotalWeight()) + "公斤");
                        deliveryTaskListDtos = new ArrayList();
                        deliveryTaskListDtos.addAll(loadingOrder.getDeliveryTaskListDtoList());
                        adapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Result<List<LoadingOrderListDto>>> call, Throwable t) {
                Toast.makeText(LoadingOrderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
                convertView = LayoutInflater.from(LoadingOrderActivity.this).inflate(R.layout.delivery_task_layout, parent, false);
                holder = new ViewHolder();
                holder.tvIdOrderNo = (TextView) convertView.findViewById(R.id.tvIdOrderNo);
                holder.tvTatus = (TextView) convertView.findViewById(R.id.tvTatus);
                holder.tvlPackageQty = (TextView) convertView.findViewById(R.id.tvlPackageQty);
                holder.tvVolume = (TextView) convertView.findViewById(R.id.tvVolume);
                holder.tvWeight = (TextView) convertView.findViewById(R.id.tvWeight);
                holder.fee_declare = (Button) convertView.findViewById(R.id.fee_declare);
                holder.btn01 = (Button) convertView.findViewById(R.id.btn01);
                holder.btn02 = (Button) convertView.findViewById(R.id.btn02);
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
                holder.fee_declare.setVisibility(View.GONE);
            }
            holder.fee_declare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoadingOrderActivity.this,FeeDeclareCreateActivity.class);
                    intent.putExtra("tsOrderNo",deliveryTaskListDto.getTsOrderNo());
                    startActivity(intent);
                }
            });
            final String deliveryStatus = deliveryTaskListDto.getStatus().name();
            if (deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.WAITING.name())) {
                holder.btn01.setVisibility(View.GONE);
                holder.btn02.setText("开始装车");
            } else if (deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.LOADING.name())) {
                holder.btn01.setVisibility(View.GONE);
                holder.btn02.setText("装车完成");
            } else if (deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.LOADED.name())) {
                holder.btn01.setVisibility(View.GONE);
                holder.btn02.setText("发车");
            } else if (deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.INTRANSIT.name())) {
                holder.btn01.setVisibility(View.VISIBLE);
                holder.btn01.setText("预到达");
                holder.btn02.setText("到达");
            } else if (deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.PLAN_ARRIVE.name())) {
                holder.btn01.setVisibility(View.GONE);
                holder.btn02.setText("到达");
            }else if (deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.ARRIVE.name())) {
                holder.btn01.setVisibility(View.VISIBLE);
                holder.btn01.setText("完成卸货");
                holder.btn02.setText("签收");
            } else if (deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.FINISH.name())) {
                holder.btn01.setVisibility(View.INVISIBLE);
                holder.btn02.setText("签收");
            } else {
                holder.btn01.setVisibility(View.GONE);
                holder.btn02.setVisibility(View.GONE);
            }
            final String operateText1 = holder.btn01.getText().toString();
            final String operateText2 = holder.btn02.getText().toString();
            holder.btn01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = "";
                    if(deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.INTRANSIT.name())){
                        title = "预到达";
                    }else if(deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.ARRIVE.name())){
                        title = "完成卸货";
                    }
                    dialog = DialogTool.showConfirmCancelDialog(LoadingOrderActivity.this, Gravity.CENTER,operateText1, title, "确认","关闭", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                TextView date = (TextView)dialog.findViewById(R.id.date);
                                TextView time = (TextView)dialog.findViewById(R.id.time);
                                if(date.getText().toString().length()==0){
                                    Toast.makeText(LoadingOrderActivity.this, "日期为空", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if(time.getText().toString().length()==0){
                                    Toast.makeText(LoadingOrderActivity.this, "时间为空", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                Date operationTime = df2.parse(date.getText().toString()+" "+time.getText().toString());
                                if (deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.INTRANSIT.name())) {
                                    preArrival(deliveryTaskListDto,operationTime);
                                } else if (deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.ARRIVE.name())) {
                                    completeUnloading(deliveryTaskListDto,operationTime);
                                } else {
                                    //Nothing
                                }
                                dialog.cancel();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            if(deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.ARRIVE.name())){
                holder.btn02.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSignUp(deliveryTaskListDto);
                    }
                });

            }else if(deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.LOADING.name())){
                holder.btn02.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = DialogTool.showLoadCompletedDialog(LoadingOrderActivity.this, deliveryTaskListDto, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    EditText totalPackageQty = (EditText) dialog.findViewById(R.id.totalPackageQty);
                                    EditText totalVolume = (EditText) dialog.findViewById(R.id.totalVolume);
                                    EditText totalWeight = (EditText) dialog.findViewById(R.id.totalWeight);
                                    TextView date = (TextView)dialog.findViewById(R.id.date);
                                    TextView time = (TextView)dialog.findViewById(R.id.time);
                                    if(totalPackageQty.getText().toString().trim().length()==0 ||Integer.parseInt(totalPackageQty.getText().toString())==0){
                                        Toast.makeText(LoadingOrderActivity.this, "件数要大于0", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    if(totalVolume.getText().toString().length()==0){
                                        Toast.makeText(LoadingOrderActivity.this, "体积为空", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    if(totalWeight.getText().toString().length()==0){
                                        Toast.makeText(LoadingOrderActivity.this, "重量为空", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    if(date.getText().toString().length()==0){
                                        Toast.makeText(LoadingOrderActivity.this, "日期为空", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    if(time.getText().toString().length()==0){
                                        Toast.makeText(LoadingOrderActivity.this, "时间为空", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    Date operationTime = df2.parse(date.getText().toString()+" "+time.getText().toString());
                                    LoadedOperationDto dto = new LoadedOperationDto();
                                    dto.setOperationTime(operationTime);
                                    dto.setTaskId(deliveryTaskListDto.getTaskId());
                                    dto.setAppUserId(TLApplication.getInstance().getSysUserListDTO().getAppUserId());
                                    dto.setOperationUserId(TLApplication.getInstance().getSysUserListDTO().getUserId());
                                    dto.setOperationUserName(TLApplication.getInstance().getSysUserListDTO().getCreateUserName());
                                    dto.setLoadedPackageQty(Integer.parseInt(totalPackageQty.getText().toString()));
                                    dto.setLoadedVolume(new BigDecimal(totalVolume.getText().toString()));
                                    dto.setLoadedWeight(new BigDecimal(totalWeight.getText().toString()));
                                    dto.setOperationUserName(TLApplication.getInstance().getSysUserListDTO().getFullName());
                                    loadFinish(dto);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("tag",""+e.getMessage());
                                }
                            }
                        });
                    }
                });
            }else{
                holder.btn02.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = "";
                        if(deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.WAITING.name())){
                            title = "开始装车";
                        }else if(deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.LOADED.name())){
                            title = "开始发车";
                        }else if(deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.INTRANSIT.name())){
                            title = "运单到达";
                        }else if(deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.PLAN_ARRIVE.name())){
                            title = "预到达";
                        }
                        dialog = DialogTool.showConfirmCancelDialog(LoadingOrderActivity.this, Gravity.CENTER,operateText2,title, "确认","关闭", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    TextView date = (TextView)dialog.findViewById(R.id.date);
                                    TextView time = (TextView)dialog.findViewById(R.id.time);
                                    if(date.getText().toString().length()==0){
                                        Toast.makeText(LoadingOrderActivity.this, "日期为空", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    if(time.getText().toString().length()==0){
                                        Toast.makeText(LoadingOrderActivity.this, "时间为空", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    Date operationTime = df2.parse(date.getText().toString()+" "+time.getText().toString());
                                    if (deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.WAITING.name())) {
                                        startLoading(deliveryTaskListDto,operationTime);
                                    } else if (deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.LOADED.name())) {
                                        setOut(deliveryTaskListDto,operationTime);
                                    } else if (deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.INTRANSIT.name())) {
                                        setArrive(deliveryTaskListDto,operationTime);
                                    } else if (deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.PLAN_ARRIVE.name())) {
                                        setArrive(deliveryTaskListDto,operationTime);
                                    } else if (deliveryStatus.equals(LoadingOrderEnum.DeliveryTaskStatus.FINISH.name())) {
                                        //Nothing
                                    }
                                    dialog.cancel();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("DialogTool",e.getMessage());
                                }
                            }
                        });

                    }
                });
            }
            return convertView;
        }


    }

    private class ViewHolder {
        private TextView tvIdOrderNo, tvTatus, tvlPackageQty, tvVolume, tvWeight;
        private Button fee_declare,btn01, btn02;
    }




    /*开始装车*/
    private void startLoading(DeliveryTaskListDto dto,Date operationTime) {
        Log.d("startLoading", "startLoading");
        LoadingOrderOperationPo params = new LoadingOrderOperationPo();
        List<String> taskIds = new ArrayList<>();
        taskIds.add(dto.getTaskId());
        params.setLdOrderId(dto.getLdOrderId());
        params.setOperationUserId(TLApplication.getInstance().getSysUserListDTO().getUserId());
        params.setTaskIds(taskIds);
        params.setOperationTime(operationTime);
        params.setOperationUserName(TLApplication.getInstance().getSysUserListDTO().getFullName());
        RetrofitFactory.getApiService().startLoading(params).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                getOrder();
                Result result = response.body();
                if (result != null && result.isSuccess()) {
                    Toast.makeText(LoadingOrderActivity.this, "装车成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoadingOrderActivity.this, "装车失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(LoadingOrderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /*配载运单完成装车*/
    private void loadFinish(LoadedOperationDto dto) {
        Log.d("loadFinish", "loadFinish");
        RetrofitFactory.getApiService().loadFinish(dto).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                getOrder();
                Result result = response.body();
                if (result != null && result.isSuccess()) {
                    Toast.makeText(LoadingOrderActivity.this, "装车成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoadingOrderActivity.this, "装车失败", Toast.LENGTH_LONG).show();
                }
                if(dialog!=null){
                    dialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(LoadingOrderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                if(dialog!=null){
                    dialog.cancel();
                }
            }
        });
    }

    /*配载运单发车*/
    private void setOut(DeliveryTaskListDto dto,Date operationTime) {
        Log.d("setOut", "setOut");
        LoadingOrderOperationPo params = new LoadingOrderOperationPo();
        List<String> taskIds = new ArrayList<>();
        taskIds.add(dto.getTaskId());
        params.setLdOrderId(dto.getLdOrderId());
        params.setOperationUserId(TLApplication.getInstance().getSysUserListDTO().getUserId());
        params.setTaskIds(taskIds);
        params.setOperationTime(operationTime);
        params.setOperationUserName(TLApplication.getInstance().getSysUserListDTO().getFullName());
        RetrofitFactory.getApiService().setOut(params).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                getOrder();
                Result result = response.body();
                if (result != null && result.isSuccess()) {
                    Toast.makeText(LoadingOrderActivity.this, "发车成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoadingOrderActivity.this, "发车失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(LoadingOrderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /*配载运单到达*/
    private void setArrive(DeliveryTaskListDto dto,Date operationTime) {
        Log.d("setArrive", "setArrive");
        LoadingOrderOperationPo params = new LoadingOrderOperationPo();
        List<String> taskIds = new ArrayList<>();
        taskIds.add(dto.getTaskId());
        params.setLdOrderId(dto.getLdOrderId());
        params.setOperationUserId(TLApplication.getInstance().getSysUserListDTO().getUserId());
        params.setTaskIds(taskIds);
        params.setOperationTime(operationTime);
        params.setOperationUserName(TLApplication.getInstance().getSysUserListDTO().getFullName());
        RetrofitFactory.getApiService().setArrive(params).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                getOrder();
                Result result = response.body();
                if (result != null && result.isSuccess()) {
                    Toast.makeText(LoadingOrderActivity.this, "到达成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoadingOrderActivity.this, "到达失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(LoadingOrderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /*配载运单预到达*/
    private void preArrival(DeliveryTaskListDto dto,Date operationTime) {
        Log.d("preArrival", "preArrival");
        LoadingOrderOperationPo params = new LoadingOrderOperationPo();
        List<String> taskIds = new ArrayList<>();
        taskIds.add(dto.getTaskId());
        params.setLdOrderId(dto.getLdOrderId());
        params.setOperationUserId(TLApplication.getInstance().getSysUserListDTO().getUserId());
        params.setTaskIds(taskIds);
        params.setAppUserId(TLApplication.getInstance().getSysUserListDTO().getAppUserId());
        params.setOperationTime(operationTime);
        params.setOperationUserName(TLApplication.getInstance().getSysUserListDTO().getFullName());
        RetrofitFactory.getApiService().preArrival(params).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                getOrder();
                Result result = response.body();
                if (result != null && result.isSuccess()) {
                    Toast.makeText(LoadingOrderActivity.this, "预到达成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoadingOrderActivity.this, "预到达失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(LoadingOrderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /*配载运单签收*/
    private void setSignUp(DeliveryTaskListDto dto) {
        Log.d("setSignUp", "setSignUp");
        Intent i = new Intent(this, SignUpActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("dto",dto);
        i.putExtras(bundle);
        finish();
        startActivity(i);
    }

    /*配载运单完成卸货*/
    private void completeUnloading(DeliveryTaskListDto dto,Date operationTime) {
        Log.d("completeUnloading", "completeUnloading");
        LoadingOrderOperationPo params = new LoadingOrderOperationPo();
        List<String> taskIds = new ArrayList<>();
        taskIds.add(dto.getTaskId());
        params.setLdOrderId(dto.getLdOrderId());
        params.setOperationUserId(TLApplication.getInstance().getSysUserListDTO().getUserId());
        params.setOperationUserName(TLApplication.getInstance().getSysUserListDTO().getFullName());
        params.setTaskIds(taskIds);
        params.setOperationTime(operationTime);

        RetrofitFactory.getApiService().completeUnloading(params).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                getOrder();
                Result result = response.body();
                if (result != null && result.isSuccess()) {
                    Toast.makeText(LoadingOrderActivity.this, "卸货成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoadingOrderActivity.this, "卸货失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(LoadingOrderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
