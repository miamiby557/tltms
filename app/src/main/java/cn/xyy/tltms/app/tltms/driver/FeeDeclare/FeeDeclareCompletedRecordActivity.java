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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

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

public class FeeDeclareCompletedRecordActivity extends BaseActivity implements SwipyRefreshLayout.OnRefreshListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.project_srl)
    SwipyRefreshLayout project_srl;
    @BindView(R.id.completedListView)
    SwipeMenuListView completedListView;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat def = new DecimalFormat("#.00");
    List<FeeRequestListDTO> completedDeclareRecordDtoList;
    CompletedFeeDeclareAdapter completedFeeDeclareAdapter;
    int currentPage = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_declare_completed_record);
        completedDeclareRecordDtoList = new ArrayList<>();
        completedFeeDeclareAdapter = new CompletedFeeDeclareAdapter();
        completedListView.setAdapter(completedFeeDeclareAdapter);
        project_srl.setOnRefreshListener(this);
        completedFeeDeclareLoad();
    }

    private void check(int position) {
        FeeRequestListDTO listDTO = completedDeclareRecordDtoList.get(position);
        Intent intent  = new Intent(FeeDeclareCompletedRecordActivity.this,FeeDeclareViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("dto",listDTO);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("费用申报历史列表");
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

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            project_srl.postDelayed(new Runnable() {

                @Override
                public void run() {

                    try {
                        currentPage = 0;
                        completedDeclareRecordDtoList = new ArrayList();
                        completedFeeDeclareAdapter.notifyDataSetChanged();
                        // 加载完后调用该方法
                        if(project_srl==null){
                            return;
                        }
                        project_srl.setRefreshing(false);
                        completedFeeDeclareLoad();
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
                        completedFeeDeclareLoad();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 2000);
        }
    }

    class CompletedFeeDeclareAdapter extends BaseAdapter{

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(FeeDeclareCompletedRecordActivity.this).inflate(R.layout.adapter_completed_fee_declare_record, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            final FeeRequestListDTO listDTO = completedDeclareRecordDtoList.get(position);
            holder.tsOrderNo.setText(listDTO.getTsOrderNo());

            if("CREATE".equalsIgnoreCase(listDTO.getStatus())){
                holder.status.setText("新建");
            }else if("VERITY".equalsIgnoreCase(listDTO.getStatus())){
                holder.status.setText("已确认");
            }else if("AGREE".equalsIgnoreCase(listDTO.getStatus())){
                holder.status.setText("已同意");
            }else if("REFUSE".equalsIgnoreCase(listDTO.getStatus())){
                holder.status.setText("已拒绝");
            }else if("CANCEL".equalsIgnoreCase(listDTO.getStatus())){
                holder.status.setText("已取消");
            }
            if(listDTO.getOrderDate()!=null){
                holder.startDate.setText(df.format(listDTO.getOrderDate()));
            }
            holder.originCity.setText("始:"+listDTO.getOriginCity());
            holder.destCity.setText("终:"+listDTO.getDestCity());
            holder.requestAmount.setText("申报:"+ NumberUtil.getValue(listDTO.getRequestAmount())+"元");
            holder.reviewAmount.setText("审批:"+NumberUtil.getValue(listDTO.getReviewAmount())+"元");
            holder.check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(FeeDeclareCompletedRecordActivity.this,FeeDeclareViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dto",listDTO);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return convertView;
        }
        class ViewHolder{
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
            public ViewHolder(View convertView){
                ButterKnife.bind(this,convertView);
            }
        }
    }

    private void completedFeeDeclareLoad(){
        Map<String, String> params = new HashMap<>();
        params.put("userId", TLApplication.getInstance().getSysUserListDTO().getUserId());
        params.put("page", currentPage+"");
        RetrofitFactory.getApiService().hasFinished(params).enqueue(new Callback<Result<List<FeeRequestListDTO>>>() {
            @Override
            public void onResponse(Call<Result<List<FeeRequestListDTO>>> call, Response<Result<List<FeeRequestListDTO>>> response) {
                try {
                    Result<List<FeeRequestListDTO>> result = response.body();
                    if(result!=null && result.isSuccess()){
                        if(result.getData()!=null && result.getData().size()>0){
                            completedDeclareRecordDtoList.addAll(result.getData());
                            completedFeeDeclareAdapter.notifyDataSetChanged();
                        }
                    }else{
                        Toast.makeText(FeeDeclareCompletedRecordActivity.this, result.getMessage()+"", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Result<List<FeeRequestListDTO>>> call, Throwable t) {
                Toast.makeText(FeeDeclareCompletedRecordActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
