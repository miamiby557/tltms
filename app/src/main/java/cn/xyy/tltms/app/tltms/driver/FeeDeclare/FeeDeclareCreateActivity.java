package cn.xyy.tltms.app.tltms.driver.FeeDeclare;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.FeeAccountListDTO;
import cn.xyy.tltms.app.data.model.FeeRequestCreatePO;
import cn.xyy.tltms.app.data.model.FeeRequestItemCreatePO;
import cn.xyy.tltms.app.data.model.FeeRequestListDTO;
import cn.xyy.tltms.app.data.model.FeeSubjectItem;
import cn.xyy.tltms.app.data.model.TransportationOrderListDTO;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.TLApplication;
import cn.xyy.tltms.app.tltms.driver.signUp.SignUpActivity;
import cn.xyy.tltms.app.utils.FileUploadUtil;
import cn.xyy.tltms.app.widget.DialogTool;
import cn.xyy.tltms.app.widget.PhotoListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dev on 2017/3/28.
 */

public class FeeDeclareCreateActivity extends BaseActivity {
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
    @BindView(R.id.btnChoosePic)
    Button btnChoosePic;
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.tips)
    TextView tips;
    private Dialog dialog;
    List<FeeAccountListDTO> feeAccountListDTOs;
    FeeAccountListDTO currentItem;
    FeeRequestCreatePO feeRequestCreatePO;
    List<FeeRequestItemCreatePO> itemCreatePOs;
    FeeDeclareItemAdapter feeDeclareItemAdapter;
    FeeSubjectAdapter feeSubjectAdapter;
    private List<MediaBean> mediaBeens;
    private PhotoListAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_declare_item);
        mediaBeens = new ArrayList<>();
        adapter = new PhotoListAdapter();
        gridView.setAdapter(adapter);
        feeRequestCreatePO = new FeeRequestCreatePO();
        feeAccountListDTOs = new ArrayList<>();
        itemCreatePOs = new ArrayList<>();
        Intent intent = getIntent();
        if(intent!=null){
            String tsOrderNo = intent.getStringExtra("tsOrderNo");
            feeRequestCreatePO.setTsOrderNo(tsOrderNo);
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
            case R.id.create:
                if(itemCreatePOs.size()==0){
                    Toast.makeText(FeeDeclareCreateActivity.this, "请添加一条费用", Toast.LENGTH_SHORT).show();
                }else if(remark.getText().toString().length()==0){
                    Toast.makeText(FeeDeclareCreateActivity.this, "请输入申报备注", Toast.LENGTH_SHORT).show();
                }else{
                    feeRequestCreatePO.setItems(itemCreatePOs);
                    feeRequestCreatePO.setAppUserId(TLApplication.getInstance().getSysUserListDTO().getAppUserId());
                    feeRequestCreatePO.setReviewAppUserId(TLApplication.getInstance().getSysUserListDTO().getAppUserId());
                    feeRequestCreatePO.setRequestRemark(remark.getText().toString());
                    feeRequestCreatePO.setRequestUserId(TLApplication.getInstance().getSysUserListDTO().getUserId());
                    feeRequestCreatePO.setRequestUserName(TLApplication.getInstance().getSysUserListDTO().getUsername());
                    BigDecimal requestAmount = new BigDecimal(0.00);
                    for(FeeRequestItemCreatePO createPO:itemCreatePOs){
                        requestAmount = requestAmount.add(createPO.getRequestAmount());
                    }
                    feeRequestCreatePO.setRequestAmount(requestAmount);
                    dialog = DialogTool.showLoadingDialog(FeeDeclareCreateActivity.this,"创建中...");
                    createFeeRequest();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createFeeRequest() {
        RetrofitFactory.getApiService().apply(feeRequestCreatePO).enqueue(new Callback<Result<String>>() {
            @Override
            public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                if(dialog!=null){
                    dialog.cancel();
                }
                Result<String> result = response.body();
                if(result!=null && result.isSuccess()){
                    if(mediaBeens.size()>0){
                        uploadFile();
                    }else{
                        Toast.makeText(FeeDeclareCreateActivity.this, "申报成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    Toast.makeText(FeeDeclareCreateActivity.this, "申报失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result<String>> call, Throwable t) {
                if(dialog!=null){
                    dialog.cancel();
                }
                Toast.makeText(FeeDeclareCreateActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFile() {
        List<String> filePath = new ArrayList<>();
        for(MediaBean bean:mediaBeens){
            filePath.add(bean.getOriginalPath());
        }
        FileUploadUtil.uploadFiles(filePath, TLApplication.getInstance().SERVER+"api/feeRequest/upload"+"?tsOrderNo="+feeRequestCreatePO.getTsOrderNo()+"&userId="+TLApplication.getInstance().getSysUserListDTO().getUserId(), new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                Toast.makeText(FeeDeclareCreateActivity.this, "上传文件成功", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("ss",s+","+e.getMessage());
                Toast.makeText(FeeDeclareCreateActivity.this, "上传文件失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_create_fee, menu);
        return true;
    }

    @OnClick({R.id.add,R.id.btnChoosePic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                if(fee.getText().toString().length()==0){
                    Toast.makeText(FeeDeclareCreateActivity.this, "请输入费用", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    BigDecimal amount = new BigDecimal(fee.getText().toString());
                    FeeRequestItemCreatePO itemCreatePO = new FeeRequestItemCreatePO();
                    itemCreatePO.setFeeAccountCode(currentItem.getCode());
                    itemCreatePO.setCodeName(currentItem.getName());
                    itemCreatePO.setRequestAmount(amount);
                    addToItem(itemCreatePO);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(FeeDeclareCreateActivity.this, "费用格式不对", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnChoosePic:
                choosePic();
                break;
        }
    }

    private void choosePic() {
        RxGalleryFinal
                .with(this)
                .image()
                .selected(mediaBeens)
                .multiple()
                .maxSize(9)
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {
                    @Override
                    protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                        mediaBeens = new ArrayList();
                        mediaBeens.addAll(imageMultipleResultEvent.getResult());
                        adapter.notifyDataSetChanged();
                    }
                })
                .openGallery();

    }

    class FeeSubjectAdapter extends BaseAdapter{

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
                convertView = LayoutInflater.from(FeeDeclareCreateActivity.this).inflate(R.layout.adapter_fee_subject_item, parent, false);
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

    private void addToItem(FeeRequestItemCreatePO itemCreatePO) {
        boolean exist = false;
        if(itemCreatePOs.size()>0){
            for(FeeRequestItemCreatePO item:itemCreatePOs){
                if(itemCreatePO.getFeeAccountCode().equalsIgnoreCase(item.getFeeAccountCode())){
                    exist = true;
                    item.setRequestAmount(itemCreatePO.getRequestAmount());
                }
            }
        }
        if(!exist){
            itemCreatePOs.add(itemCreatePO);
        }
        BigDecimal count = new BigDecimal(0);
        if(itemCreatePOs.size()>0){
            for(FeeRequestItemCreatePO item:itemCreatePOs){
                count = count.add(item.getRequestAmount());
            }
        }
        tips.setText("申报科目:"+itemCreatePOs.size()+"条,总申报费:"+count+"元");
        feeDeclareItemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("费用申报创建");
    }

    @Override
    protected void initData() {

    }

    class FeeDeclareItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemCreatePOs.size();
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
                convertView = LayoutInflater.from(FeeDeclareCreateActivity.this).inflate(R.layout.adapter_fee_declare_kemu_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            FeeRequestItemCreatePO itemCreatePO = itemCreatePOs.get(position);
            holder.kemu.setText("费用科目:"+itemCreatePO.getCodeName());
            holder.amount.setText("申请费用:"+itemCreatePO.getRequestAmount()+"元");
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
        itemCreatePOs.remove(position);
        BigDecimal count = new BigDecimal(0);
        if(itemCreatePOs.size()>0){
            for(FeeRequestItemCreatePO item:itemCreatePOs){
                count = count.add(item.getRequestAmount());
            }
        }
        tips.setText("申报科目:"+itemCreatePOs.size()+"条,总申报费:"+count+"元");
        feeDeclareItemAdapter.notifyDataSetChanged();
    }

    class PhotoListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mediaBeens.size();
        }

        @Override
        public Object getItem(int position) {
            return mediaBeens.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (null == convertView) {
                vh = new ViewHolder();
                convertView = LayoutInflater.from(FeeDeclareCreateActivity.this).inflate(R.layout.gf_adapter_photo_list_item, null);
                vh.mIvThumb = (ImageView) convertView.findViewById(R.id.iv_thumb);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            final MediaBean mediaBean = mediaBeens.get(position);

            Glide.with(FeeDeclareCreateActivity.this).load(mediaBean.getOriginalPath())
                    .placeholder(R.drawable.ic_gf_default_photo)
                    .crossFade()
                    .animate(R.anim.gf_flip_horizontal_in)
                    .into(vh.mIvThumb);

            return convertView;
        }


        private class ViewHolder {
            private ImageView mIvThumb;
        }
    }
}
