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
import cn.xyy.tltms.app.data.model.FeeRequestItemListDTO;
import cn.xyy.tltms.app.data.model.FeeRequestItemUpdatePO;
import cn.xyy.tltms.app.data.model.FeeRequestListDTO;
import cn.xyy.tltms.app.data.model.FeeRequestUpdatePO;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.utils.NumberUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 2017/4/5.
 */

public class FeeDeclareViewActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.review)
    EditText review;
    @BindView(R.id.requestAmount)
    TextView requestAmount;
    @BindView(R.id.reviewAmount)
    TextView reviewAmount;
    List<FeeAccountListDTO> feeAccountListDTOs;
    FeeRequestUpdatePO feeRequestUpdatePO;
    List<FeeRequestItemUpdatePO> itemUpdatePOs;
    FeeDeclareItemAdapter feeDeclareItemAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_declare_view);
        feeAccountListDTOs = new ArrayList<>();
        Intent intent = this.getIntent();
        if(intent!=null){
            FeeRequestListDTO dto = (FeeRequestListDTO)intent.getSerializableExtra("dto");
            feeRequestUpdatePO = new FeeRequestUpdatePO();
            feeRequestUpdatePO.setRequestId(dto.getRequestId());
            feeRequestUpdatePO.setRequestRemark(dto.getRequestRemark());
            feeRequestUpdatePO.setReviewRemark(dto.getReviewRemark());
            feeRequestUpdatePO.setRequestAmount(dto.getRequestAmount());
            feeRequestUpdatePO.setReviewAmount(dto.getReviewAmount());
            feeRequestUpdatePO.setReviewUserId(dto.getReviewUserId());
            feeRequestUpdatePO.setReviewUserName(dto.getReviewUserName());
            itemUpdatePOs = new ArrayList<>();
            if(dto.getItems()!=null && dto.getItems().size()>0){
                for(FeeRequestItemListDTO itemListDTO:dto.getItems()){
                    FeeRequestItemUpdatePO updatePO = new FeeRequestItemUpdatePO();
                    updatePO.setFeeAccountCode(itemListDTO.getFeeAccountCode());
                    updatePO.setFeeAccountName(itemListDTO.getFeeAccountName());
                    updatePO.setRequestAmount(itemListDTO.getRequestAmount());
                    updatePO.setReviewAmount(itemListDTO.getReviewAmount());
                    itemUpdatePOs.add(updatePO);
                }
            }

        }
        feeDeclareItemAdapter = new FeeDeclareItemAdapter();
        listView.setAdapter(feeDeclareItemAdapter);
        feeDeclareItemAdapter.notifyDataSetChanged();
        showView();
    }

    private void showView() {
        try {
            remark.setText(feeRequestUpdatePO.getRequestRemark());
            review.setText(feeRequestUpdatePO.getReviewRemark());
            requestAmount.setText("申报费:"+NumberUtil.getValue(feeRequestUpdatePO.getRequestAmount())+"元");
            reviewAmount.setText("审批费:"+NumberUtil.getValue(feeRequestUpdatePO.getReviewAmount())+"元");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.update:
                if(itemUpdatePOs.size()==0){
                    Toast.makeText(FeeDeclareViewActivity.this, "请输入费用", Toast.LENGTH_SHORT).show();
                }else{
                    feeRequestUpdatePO.setRequestRemark(remark.getText().toString());
                    BigDecimal requestAmount = new BigDecimal(0.00);
                    feeRequestUpdatePO.setItems(itemUpdatePOs);
                    for(FeeRequestItemUpdatePO createPO:itemUpdatePOs){
                        requestAmount = requestAmount.add(createPO.getRequestAmount());
                    }
                    feeRequestUpdatePO.setRequestAmount(requestAmount);
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
                Result<String> result = response.body();
                if(result.isSuccess()){
                    Toast.makeText(FeeDeclareViewActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(FeeDeclareViewActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result<String>> call, Throwable t) {
                Toast.makeText(FeeDeclareViewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("费用申报详情");
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
                convertView = LayoutInflater.from(FeeDeclareViewActivity.this).inflate(R.layout.adapter_fee_declare_item_view, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            FeeRequestItemUpdatePO itemCreatePO = itemUpdatePOs.get(position);
            holder.kemu.setText(itemCreatePO.getFeeAccountName());
            holder.requestAmount.setText(NumberUtil.getValue(itemCreatePO.getRequestAmount())+"元");
            holder.reviewAmount.setText(NumberUtil.getValue(itemCreatePO.getReviewAmount())+"元");
            return convertView;
        }
        class ViewHolder{
            @BindView(R.id.kemu)
            TextView kemu;
            @BindView(R.id.requestAmount)
            TextView requestAmount;
            @BindView(R.id.reviewAmount)
            TextView reviewAmount;
            public ViewHolder(View convertView){
                ButterKnife.bind(this,convertView);
            }
        }
    }

    private void remove(int position) {
        itemUpdatePOs.remove(position);
        feeDeclareItemAdapter.notifyDataSetChanged();
    }
}
