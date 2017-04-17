package cn.xyy.tltms.app.tltms.driver.FeeDeclare;

import android.app.Dialog;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.FeeAccountListDTO;
import cn.xyy.tltms.app.data.model.FeeRequestCreatePO;
import cn.xyy.tltms.app.data.model.FeeRequestItemCreatePO;
import cn.xyy.tltms.app.data.model.FeeRequestItemListDTO;
import cn.xyy.tltms.app.data.model.FeeRequestItemUpdatePO;
import cn.xyy.tltms.app.data.model.FeeRequestListDTO;
import cn.xyy.tltms.app.data.model.FeeRequestUpdatePO;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.TLApplication;
import cn.xyy.tltms.app.utils.NumberUtil;
import cn.xyy.tltms.app.widget.DialogTool;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 2017/4/5.
 */

public class FeeDeclareModifyActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.type)
    Spinner type;
    @BindView(R.id.fee)
    EditText fee;
    @BindView(R.id.add)
    Button add;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.tips)
    TextView tips;
    List<FeeAccountListDTO> feeAccountListDTOs;
    FeeAccountListDTO currentItem;
    FeeRequestUpdatePO feeRequestUpdatePO;
    List<FeeRequestItemUpdatePO> itemUpdatePOs;
    FeeDeclareItemAdapter feeDeclareItemAdapter;
    FeeSubjectAdapter feeSubjectAdapter;
    private Dialog dialog;
    java.text.DecimalFormat def = new java.text.DecimalFormat("#.00");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_declare_modify);
        feeAccountListDTOs = new ArrayList<>();
        Intent intent = this.getIntent();
        if(intent!=null){
            FeeRequestListDTO dto = (FeeRequestListDTO)intent.getSerializableExtra("dto");
            feeRequestUpdatePO = new FeeRequestUpdatePO();
            feeRequestUpdatePO.setRequestId(dto.getRequestId());
            feeRequestUpdatePO.setRequestRemark(dto.getRequestRemark());
            feeRequestUpdatePO.setRequestAmount(dto.getRequestAmount());
            feeRequestUpdatePO.setReviewUserId(dto.getReviewUserId());
            feeRequestUpdatePO.setReviewUserName(dto.getReviewUserName());
            itemUpdatePOs = new ArrayList<>();
            if(dto.getItems()!=null && dto.getItems().size()>0){
                for(FeeRequestItemListDTO itemListDTO:dto.getItems()){
                    FeeRequestItemUpdatePO updatePO = new FeeRequestItemUpdatePO();
                    updatePO.setFeeAccountCode(itemListDTO.getFeeAccountCode());
                    updatePO.setFeeAccountName(itemListDTO.getFeeAccountName());
                    updatePO.setRequestAmount(itemListDTO.getRequestAmount());
                    itemUpdatePOs.add(updatePO);
                }
            }

        }
        getFeeAccountList();
        feeSubjectAdapter = new FeeSubjectAdapter();
        type.setAdapter(feeSubjectAdapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentItem = feeAccountListDTOs.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        feeDeclareItemAdapter = new FeeDeclareItemAdapter();
        listView.setAdapter(feeDeclareItemAdapter);
        feeDeclareItemAdapter.notifyDataSetChanged();
        showView();
    }

    private void showView() {
        BigDecimal count = new BigDecimal(0);
        if(itemUpdatePOs.size()>0){
            for(FeeRequestItemUpdatePO item:itemUpdatePOs){
                count = count.add(item.getRequestAmount());
            }
        }
        tips.setText("申报科目:"+itemUpdatePOs.size()+"条,总申报费:"+count+"元");
        remark.setText(feeRequestUpdatePO.getRequestRemark());
    }

    private void getFeeAccountList() {
        RetrofitFactory.getApiService().canApply().enqueue(new Callback<Result<List<FeeAccountListDTO>>>() {
            @Override
            public void onResponse(Call<Result<List<FeeAccountListDTO>>> call, Response<Result<List<FeeAccountListDTO>>> response) {
                Result<List<FeeAccountListDTO>> result = response.body();
                if(result.isSuccess()){
                    feeAccountListDTOs = result.getData();
                    feeSubjectAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Result<List<FeeAccountListDTO>>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.update:
                if(itemUpdatePOs.size()==0){
                    Toast.makeText(FeeDeclareModifyActivity.this, "请添加一条费用", Toast.LENGTH_SHORT).show();
                }else{
                    feeRequestUpdatePO.setRequestRemark(remark.getText().toString());
                    BigDecimal requestAmount = new BigDecimal(0.00);
                    feeRequestUpdatePO.setItems(itemUpdatePOs);
                    for(FeeRequestItemUpdatePO createPO:itemUpdatePOs){
                        requestAmount = requestAmount.add(createPO.getRequestAmount());
                    }
                    feeRequestUpdatePO.setRequestAmount(requestAmount);
                    dialog = DialogTool.showLoadingDialog(FeeDeclareModifyActivity.this,"更新中...");
                    updateFeeRequest();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateFeeRequest() {
        RetrofitFactory.getApiService().update(feeRequestUpdatePO).enqueue(new Callback<Result<String>>() {
            @Override
            public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                if(dialog!=null){
                    dialog.cancel();
                }
                Result<String> result = response.body();
                if(result.isSuccess()){
                    Toast.makeText(FeeDeclareModifyActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(FeeDeclareModifyActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result<String>> call, Throwable t) {
                if(dialog!=null){
                    dialog.cancel();
                }
                Toast.makeText(FeeDeclareModifyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_update, menu);
        return true;
    }

    @OnClick({R.id.add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                if(fee.getText().toString().length()==0){
                    Toast.makeText(FeeDeclareModifyActivity.this, "请输入费用", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    BigDecimal amount = new BigDecimal(fee.getText().toString());
                    FeeRequestItemUpdatePO itemCreatePO = new FeeRequestItemUpdatePO();
                    itemCreatePO.setFeeAccountCode(currentItem.getCode());
                    itemCreatePO.setFeeAccountName(currentItem.getName());
                    itemCreatePO.setRequestAmount(amount);
                    addToItem(itemCreatePO);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(FeeDeclareModifyActivity.this, "费用格式不对", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    class FeeSubjectAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return feeAccountListDTOs.size();
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
                convertView = LayoutInflater.from(FeeDeclareModifyActivity.this).inflate(R.layout.adapter_fee_subject_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            FeeAccountListDTO item = feeAccountListDTOs.get(position);
            holder.subjectName.setText(item.getName());
            return convertView;
        }
        class ViewHolder{
            @BindView(R.id.subjectName)
            TextView subjectName;
            public ViewHolder(View convertView){
                ButterKnife.bind(this,convertView);
            }
        }
    }

    private void addToItem(FeeRequestItemUpdatePO itemUpdatePO) {
        boolean exist = false;
        if(itemUpdatePOs.size()>0){
            for(FeeRequestItemUpdatePO item:itemUpdatePOs){
                if(itemUpdatePO.getFeeAccountCode().equalsIgnoreCase(item.getFeeAccountCode())){
                    exist = true;
                    item.setRequestAmount(itemUpdatePO.getRequestAmount());
                }
            }
        }
        if(!exist){
            itemUpdatePOs.add(itemUpdatePO);
        }
        BigDecimal count = new BigDecimal(0);
        if(itemUpdatePOs.size()>0){
            for(FeeRequestItemUpdatePO item:itemUpdatePOs){
                count = count.add(item.getRequestAmount());
            }
        }
        tips.setText("申报科目:"+itemUpdatePOs.size()+"条,总申报费:"+count+"元");
        feeDeclareItemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("费用申报修改");
    }

    @Override
    protected void initData() {

    }

    class FeeDeclareItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemUpdatePOs.size();
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
                convertView = LayoutInflater.from(FeeDeclareModifyActivity.this).inflate(R.layout.adapter_fee_declare_kemu_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            FeeRequestItemUpdatePO itemCreatePO = itemUpdatePOs.get(position);
            holder.kemu.setText(itemCreatePO.getFeeAccountName());
            holder.amount.setText(NumberUtil.getValue(itemCreatePO.getRequestAmount())+"元");
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(position);
                }
            });
            return convertView;
        }
        class ViewHolder{
            @BindView(R.id.kemu)
            TextView kemu;
            @BindView(R.id.amount)
            TextView amount;
            @BindView(R.id.delete)
            Button delete;
            public ViewHolder(View convertView){
                ButterKnife.bind(this,convertView);
            }
        }
    }

    private void remove(int position) {
        itemUpdatePOs.remove(position);
        feeDeclareItemAdapter.notifyDataSetChanged();
        BigDecimal count = new BigDecimal(0);
        if(itemUpdatePOs.size()>0){
            for(FeeRequestItemUpdatePO item:itemUpdatePOs){
                count = count.add(item.getRequestAmount());
            }
        }
        tips.setText("申报科目:"+itemUpdatePOs.size()+"条,总申报费:"+count+"元");
    }
}
