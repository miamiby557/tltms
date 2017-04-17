package cn.xyy.tltms.app.tltms.driver.dispatcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import cn.xyy.tltms.app.data.model.TransportationOrderListDTO;
import cn.xyy.tltms.app.data.model.VehicleListDTO;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.TLApplication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 2017/3/28.
 */

public class DispatcherSelectDriverActivity extends BaseActivity implements SwipyRefreshLayout.OnRefreshListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.carNumber)
    EditText carNumber;
    @BindView(R.id.search)
    Button search;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.project_srl)
    SwipyRefreshLayout project_srl;
    VehicleAdapter vehicleAdapter;
    List<VehicleListDTO> vehicleListDTOs;
    int currentPage = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher_select_driver);
        vehicleListDTOs = new ArrayList<>();
        project_srl.setOnRefreshListener(this);
        vehicleAdapter = new VehicleAdapter();
        listView.setAdapter(vehicleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VehicleListDTO listDTO = vehicleListDTOs.get(position);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("listDto",listDTO);
                intent.putExtras(bundle);
                setResult(101,intent);
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("选择司机");
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

    @OnClick({R.id.search})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search:
                vehicleListDTOs = new ArrayList<>();
                vehicleAdapter.notifyDataSetChanged();
                if(carNumber.getText().length()==0){
                    Toast.makeText(DispatcherSelectDriverActivity.this, "请输入车牌号", Toast.LENGTH_LONG).show();
                }else{
                    initData();
                }
                break;
        }
    }

    @Override
    protected void initData() {
        Map<String, String> params = new HashMap<>();
        params.put("vehicleNo",carNumber.getText().toString());
        params.put("appUserId", TLApplication.getInstance().getSysUserListDTO().getAppUserId());
        params.put("page",currentPage+"");
        RetrofitFactory.getApiService().findDriver(params).enqueue(new Callback<Result<List<VehicleListDTO>>>() {
            @Override
            public void onResponse(Call<Result<List<VehicleListDTO>>> call, Response<Result<List<VehicleListDTO>>> response) {
                Result<List<VehicleListDTO>> result = response.body();
                if(result!=null){
                    if(result.getData()!=null && result.getData().size()>0){
                        vehicleListDTOs.addAll(result.getData());
                        vehicleAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(DispatcherSelectDriverActivity.this, "没有更多数据", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(DispatcherSelectDriverActivity.this, "解析数据异常", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result<List<VehicleListDTO>>> call, Throwable t) {
                Toast.makeText(DispatcherSelectDriverActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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
                        vehicleListDTOs = new ArrayList();
                        vehicleAdapter.notifyDataSetChanged();
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

    class VehicleAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return vehicleListDTOs.size();
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
                convertView = LayoutInflater.from(DispatcherSelectDriverActivity.this).inflate(R.layout.adapter_driver, parent, false);
                holder = new ViewHolder(convertView);
                //设置标记
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            VehicleListDTO listDTO = vehicleListDTOs.get(position);
            holder.carNumber.setText(""+listDTO.getVehicleNo());
            holder.driverName.setText(""+listDTO.getDriver());
            holder.driverPhone.setText(""+listDTO.getDriverPhone());
            return convertView;
        }
        class  ViewHolder{
            @BindView(R.id.carNumber)
            TextView carNumber;
            @BindView(R.id.driverName)
            TextView driverName;
            @BindView(R.id.driverPhone)
            TextView driverPhone;

            public ViewHolder(View view){
                ButterKnife.bind(this,view);
            }
        }
    }
}
