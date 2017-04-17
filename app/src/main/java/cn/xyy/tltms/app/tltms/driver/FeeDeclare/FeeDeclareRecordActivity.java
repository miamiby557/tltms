package cn.xyy.tltms.app.tltms.driver.FeeDeclare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

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
import cn.xyy.tltms.app.data.model.FeeRequestListDTO;
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

public class FeeDeclareRecordActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.createListView)
    ListView createListView;
    @BindView(R.id.create_count)
    TextView create_count;
    @BindView(R.id.verityListView)
    ListView verityListView;
    @BindView(R.id.verity_count)
    TextView verity_count;
    @BindView(R.id.completedListView)
    ListView completedListView;
    @BindView(R.id.loadMore)
    Button loadMore;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat def = new DecimalFormat("#.00");
    List<FeeRequestListDTO> createDeclareRecordDtoList;
    List<FeeRequestListDTO> verityDeclareRecordDtoList;
    List<FeeRequestListDTO> completedDeclareRecordDtoList;
    CreateFeeDeclareAdapter createFeeDeclareAdapter;
    VerityFeeDeclareAdapter verityFeeDeclareAdapter;
    CompletedFeeDeclareAdapter completedFeeDeclareAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_declare_record);
        createDeclareRecordDtoList = new ArrayList<>();
        verityDeclareRecordDtoList = new ArrayList<>();
        completedDeclareRecordDtoList = new ArrayList<>();
        createFeeDeclareAdapter = new CreateFeeDeclareAdapter();
        verityFeeDeclareAdapter = new VerityFeeDeclareAdapter();
        completedFeeDeclareAdapter = new CompletedFeeDeclareAdapter();
        createListView.setAdapter(createFeeDeclareAdapter);
        verityListView.setAdapter(verityFeeDeclareAdapter);
        completedListView.setAdapter(completedFeeDeclareAdapter);
        createFeeDeclareLoad();
        verityFeeDeclareLoad();
        completedFeeDeclareLoad();
        //getListHight(completedListView);
    }

    public void getListHight(ListView listView) {
        Adapter adapter = listView.getAdapter();
        int hight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View view = adapter.getView(i, null, listView);
            view.measure(0, 0);
            hight += view.getMeasuredHeight();
        }
        int sh = listView.getDividerHeight() * (adapter.getCount() - 1);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = sh + hight;
        listView.setLayoutParams(params);
    }

    private void checkCompleted(int position) {
        FeeRequestListDTO listDTO = completedDeclareRecordDtoList.get(position);
        Intent intent = new Intent(FeeDeclareRecordActivity.this, FeeDeclareViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("dto", listDTO);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void checkVerity(int position) {
        FeeRequestListDTO listDTO = verityDeclareRecordDtoList.get(position);
        Intent intent = new Intent(FeeDeclareRecordActivity.this, FeeDeclareViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("dto", listDTO);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void update(int position) {
        FeeRequestListDTO dto = createDeclareRecordDtoList.get(position);
        if ("VERITY".equalsIgnoreCase(dto.getStatus())) {
            Toast.makeText(FeeDeclareRecordActivity.this, "已确认的申报不可修改", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(FeeDeclareRecordActivity.this, FeeDeclareModifyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("dto", dto);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void cancel(int position) {
        FeeRequestListDTO dto = createDeclareRecordDtoList.get(position);
        Map<String, String> params = new HashMap<>();
        params.put("requestId", dto.getRequestId());
        RetrofitFactory.getApiService().cancel(params).enqueue(new Callback<Result<String>>() {
            @Override
            public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                Result<String> result = response.body();
                if (result.isSuccess()) {
                    Toast.makeText(FeeDeclareRecordActivity.this, "取消成功", Toast.LENGTH_LONG).show();
                    reload();
                } else {
                    Toast.makeText(FeeDeclareRecordActivity.this, "不可取消", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result<String>> call, Throwable t) {
                Toast.makeText(FeeDeclareRecordActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void reload() {
        try {
            createDeclareRecordDtoList = new ArrayList();
            verityDeclareRecordDtoList = new ArrayList();
            completedDeclareRecordDtoList = new ArrayList();
            createFeeDeclareAdapter.notifyDataSetChanged();
            verityFeeDeclareAdapter.notifyDataSetChanged();
            completedFeeDeclareAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
        createFeeDeclareLoad();
        verityFeeDeclareLoad();
        completedFeeDeclareLoad();
    }

    private void confirm(int position) {
        FeeRequestListDTO dto = createDeclareRecordDtoList.get(position);
        if ("VERITY".equalsIgnoreCase(dto.getStatus())) {
            Toast.makeText(FeeDeclareRecordActivity.this, "已经确认", Toast.LENGTH_LONG).show();
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("requestId", dto.getRequestId());
            RetrofitFactory.getApiService().verifyApply(params).enqueue(new Callback<Result<String>>() {
                @Override
                public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                    Result<String> result = response.body();
                    if (result.isSuccess()) {
                        Toast.makeText(FeeDeclareRecordActivity.this, "确认成功", Toast.LENGTH_LONG).show();
                        reload();
                    } else {
                        Toast.makeText(FeeDeclareRecordActivity.this, "确认失败", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Result<String>> call, Throwable t) {
                    Toast.makeText(FeeDeclareRecordActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("费用申报列表");
    }

    @OnClick({R.id.loadMore})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loadMore:
                openActivity(FeeDeclareCompletedRecordActivity.class);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.fee_declare:
                openActivity(FeeDeclareSelectActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {

    }

    class CreateFeeDeclareAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return createDeclareRecordDtoList.size();
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
                convertView = LayoutInflater.from(FeeDeclareRecordActivity.this).inflate(R.layout.adapter_fee_declare_create, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            FeeRequestListDTO listDTO = createDeclareRecordDtoList.get(position);
            try {
                holder.tsOrderNo.setText(listDTO.getTsOrderNo());
                if ("CREATE".equalsIgnoreCase(listDTO.getStatus())) {
                    holder.status.setText("待确认");
                } else if ("VERITY".equalsIgnoreCase(listDTO.getStatus())) {
                    holder.status.setText("已确认");
                } else if ("AGREE".equalsIgnoreCase(listDTO.getStatus())) {
                    holder.status.setText("已同意");
                } else if ("REFUSE".equalsIgnoreCase(listDTO.getStatus())) {
                    holder.status.setText("已拒绝");
                } else if ("CANCEL".equalsIgnoreCase(listDTO.getStatus())) {
                    holder.status.setText("已取消");
                }
                if (listDTO.getOrderDate() != null) {
                    holder.startDate.setText(df.format(listDTO.getOrderDate()));
                }

                holder.startPlace.setText(listDTO.getDestCity());
                holder.totalFee.setText(NumberUtil.getValue(listDTO.getRequestAmount()) + "元");
                holder.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel(position);
                    }
                });
                holder.modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        update(position);
                    }
                });
                holder.confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirm(position);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tsOrderNo)
            TextView tsOrderNo;
            @BindView(R.id.startDate)
            TextView startDate;
            @BindView(R.id.status)
            TextView status;
            @BindView(R.id.startPlace)
            TextView startPlace;
            @BindView(R.id.totalFee)
            TextView totalFee;
            @BindView(R.id.cancel)
            Button cancel;
            @BindView(R.id.modify)
            Button modify;
            @BindView(R.id.confirm)
            Button confirm;

            public ViewHolder(View convertView) {
                ButterKnife.bind(this, convertView);
            }
        }
    }

    class VerityFeeDeclareAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return verityDeclareRecordDtoList.size();
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
                convertView = LayoutInflater.from(FeeDeclareRecordActivity.this).inflate(R.layout.adapter_fee_declare_verity, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            FeeRequestListDTO listDTO = verityDeclareRecordDtoList.get(position);
            try {
                holder.tsOrderNo.setText(listDTO.getTsOrderNo());
                if ("CREATE".equalsIgnoreCase(listDTO.getStatus())) {
                    holder.status.setText("新建");
                } else if ("VERITY".equalsIgnoreCase(listDTO.getStatus())) {
                    holder.status.setText("待审批");
                } else if ("AGREE".equalsIgnoreCase(listDTO.getStatus())) {
                    holder.status.setText("已同意");
                } else if ("REFUSE".equalsIgnoreCase(listDTO.getStatus())) {
                    holder.status.setText("已拒绝");
                } else if ("CANCEL".equalsIgnoreCase(listDTO.getStatus())) {
                    holder.status.setText("已取消");
                }
                if (listDTO.getOrderDate() != null) {
                    holder.startDate.setText(df.format(listDTO.getOrderDate()));
                }

                holder.startPlace.setText(listDTO.getDestCity());
                holder.totalFee.setText(NumberUtil.getValue(listDTO.getRequestAmount()) + "元");
                holder.check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkVerity(position);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tsOrderNo)
            TextView tsOrderNo;
            @BindView(R.id.startDate)
            TextView startDate;
            @BindView(R.id.status)
            TextView status;
            @BindView(R.id.startPlace)
            TextView startPlace;
            @BindView(R.id.totalFee)
            TextView totalFee;
            @BindView(R.id.check)
            Button check;

            public ViewHolder(View convertView) {
                ButterKnife.bind(this, convertView);
            }
        }
    }

    class CompletedFeeDeclareAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return completedDeclareRecordDtoList.size();
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
                convertView = LayoutInflater.from(FeeDeclareRecordActivity.this).inflate(R.layout.adapter_completed_fee_declare_record, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final FeeRequestListDTO listDTO = completedDeclareRecordDtoList.get(position);
            holder.tsOrderNo.setText(listDTO.getTsOrderNo());

            if ("CREATE".equalsIgnoreCase(listDTO.getStatus())) {
                holder.status.setText("新建");
            } else if ("VERITY".equalsIgnoreCase(listDTO.getStatus())) {
                holder.status.setText("已确认");
            } else if ("AGREE".equalsIgnoreCase(listDTO.getStatus())) {
                holder.status.setText("已同意");
            } else if ("REFUSE".equalsIgnoreCase(listDTO.getStatus())) {
                holder.status.setText("已拒绝");
            } else if ("CANCEL".equalsIgnoreCase(listDTO.getStatus())) {
                holder.status.setText("已取消");
            }
            if (listDTO.getOrderDate() != null) {
                holder.startDate.setText(df.format(listDTO.getOrderDate()));
            }
            holder.originCity.setText("始:" + listDTO.getOriginCity());
            holder.destCity.setText("终:" + listDTO.getDestCity());
            holder.requestAmount.setText("申报:" + NumberUtil.getValue(listDTO.getRequestAmount()) + "元");
            holder.reviewAmount.setText("审批:" + NumberUtil.getValue(listDTO.getReviewAmount()) + "元");
            holder.check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkCompleted(position);
                }
            });
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tsOrderNo)
            TextView tsOrderNo;
            @BindView(R.id.startDate)
            TextView startDate;
            @BindView(R.id.status)
            TextView status;
            @BindView(R.id.originCity)
            TextView originCity;
            @BindView(R.id.destCity)
            TextView destCity;
            @BindView(R.id.requestAmount)
            TextView requestAmount;
            @BindView(R.id.reviewAmount)
            TextView reviewAmount;
            @BindView(R.id.check)
            Button check;

            public ViewHolder(View convertView) {
                ButterKnife.bind(this, convertView);
            }
        }
    }

    private void createFeeDeclareLoad() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", TLApplication.getInstance().getSysUserListDTO().getUserId());
        params.put("appUserId", TLApplication.getInstance().getSysUserListDTO().getAppUserId());
        RetrofitFactory.getApiService().findCreate(params).enqueue(new Callback<Result<List<FeeRequestListDTO>>>() {
            @Override
            public void onResponse(Call<Result<List<FeeRequestListDTO>>> call, Response<Result<List<FeeRequestListDTO>>> response) {
                Result<List<FeeRequestListDTO>> result = response.body();
                if (result.isSuccess()) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        create_count.setText(result.getData().size() + "");
                        createDeclareRecordDtoList = result.getData();
                        createFeeDeclareAdapter.notifyDataSetChanged();
                    } else {
                        create_count.setText("0");
                    }
                } else {
                    Toast.makeText(FeeDeclareRecordActivity.this, result.getMessage() + "", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result<List<FeeRequestListDTO>>> call, Throwable t) {
                Toast.makeText(FeeDeclareRecordActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void verityFeeDeclareLoad() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", TLApplication.getInstance().getSysUserListDTO().getUserId());
        params.put("appUserId", TLApplication.getInstance().getSysUserListDTO().getAppUserId());
        RetrofitFactory.getApiService().findVerity(params).enqueue(new Callback<Result<List<FeeRequestListDTO>>>() {
            @Override
            public void onResponse(Call<Result<List<FeeRequestListDTO>>> call, Response<Result<List<FeeRequestListDTO>>> response) {
                try {
                    Result<List<FeeRequestListDTO>> result = response.body();
                    if (result != null && result.isSuccess()) {
                        if (result.getData() != null && result.getData().size() > 0) {
                            verity_count.setText(result.getData().size() + "");
                            verityDeclareRecordDtoList = result.getData();
                            verityFeeDeclareAdapter.notifyDataSetChanged();
                        } else {
                            verity_count.setText("0");
                        }
                    } else {
                        Toast.makeText(FeeDeclareRecordActivity.this, result.getMessage() + "", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Result<List<FeeRequestListDTO>>> call, Throwable t) {
                Toast.makeText(FeeDeclareRecordActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }

    private void completedFeeDeclareLoad() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", TLApplication.getInstance().getSysUserListDTO().getUserId());
        params.put("page", "0");
        RetrofitFactory.getApiService().hasFinished(params).enqueue(new Callback<Result<List<FeeRequestListDTO>>>() {
            @Override
            public void onResponse(Call<Result<List<FeeRequestListDTO>>> call, Response<Result<List<FeeRequestListDTO>>> response) {
                Result<List<FeeRequestListDTO>> result = response.body();
                if (result != null && result.isSuccess()) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        completedDeclareRecordDtoList = result.getData();
                        completedFeeDeclareAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(FeeDeclareRecordActivity.this, result.getMessage() + "", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result<List<FeeRequestListDTO>>> call, Throwable t) {
                Toast.makeText(FeeDeclareRecordActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
