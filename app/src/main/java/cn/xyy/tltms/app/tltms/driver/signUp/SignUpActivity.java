package cn.xyy.tltms.app.tltms.driver.signUp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.CommonEnum;
import cn.xyy.tltms.app.data.model.DeliveryTaskListDto;
import cn.xyy.tltms.app.data.model.OrderException;
import cn.xyy.tltms.app.data.model.TransportationOrderListDTO;
import cn.xyy.tltms.app.data.model.TransportationOrderSignDto;
import cn.xyy.tltms.app.data.remote.Result;
import cn.xyy.tltms.app.data.remote.RetrofitFactory;
import cn.xyy.tltms.app.tltms.BaseActivity;
import cn.xyy.tltms.app.tltms.TLApplication;
import cn.xyy.tltms.app.utils.Constant;
import cn.xyy.tltms.app.utils.DataUtils;
import cn.xyy.tltms.app.utils.DateUtil;
import cn.xyy.tltms.app.utils.FileUploadUtil;
import cn.xyy.tltms.app.utils.NumberUtil;
import cn.xyy.tltms.app.widget.DialogTool;
import cn.xyy.tltms.app.widget.OrderExceptionAdapter;
import cn.xyy.tltms.app.widget.PhotoListAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity implements OnDateSetListener {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvLdOrderNo)
    TextView tvLdOrderNo;
    @BindView(R.id.originCity)
    TextView originCity;
    @BindView(R.id.destCity)
    TextView destCity;
    @BindView(R.id.edtTotalWeight)
    TextView edtTotalWeight;
    @BindView(R.id.edtTotalPackageQty)
    TextView edtTotalPackageQty;
    @BindView(R.id.edtTotalVolume)
    TextView edtTotalVolume;
    @BindView(R.id.edtSignMan)
    EditText edtSignMan;
    @BindView(R.id.edtIdCard)
    EditText edtIdCard;
    @BindView(R.id.edtException)
    EditText edtException;
    @BindView(R.id.btnSignDate)
    Button btnSignDate;
    @BindView(R.id.edtRemark)
    EditText edtRemark;
    @BindView(R.id.cbException)
    AppCompatCheckBox cbException;
    @BindView(R.id.btnChoosePic)
    Button btnChoosePic;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.layoutException)
    LinearLayout layoutException;
    @BindView(R.id.spException)
    Spinner spException;
    private Dialog dialog;

    private List<MediaBean> mediaBeens;
    private PhotoListAdapter adapter;
    DeliveryTaskListDto dto;
    TransportationOrderListDTO listDTO;
    private List<OrderException> orderExceptions;
    private OrderExceptionAdapter orderExceptionAdapter;
    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText("签收");

        btnSignDate.setText(DateUtil.getDate(calendar.getTime()));
        Intent intent = getIntent();
        dto = (DeliveryTaskListDto)intent.getSerializableExtra("dto");
        mediaBeens = new ArrayList<>();
        adapter = new PhotoListAdapter();
        gridView.setAdapter(adapter);
        orderExceptions = DataUtils.getExceptionData();
        orderExceptionAdapter = new OrderExceptionAdapter(this, orderExceptions);
        spException.setAdapter(orderExceptionAdapter);
    }

    @Override
    protected void initData() {

        Map<String, String> params = new HashMap<>();
        params.put("userId", TLApplication.getInstance().getSysUserListDTO().getUserId());
        params.put("tsOrderNo", dto.getTsOrderNo());
        RetrofitFactory.getApiService().findByTsOrderNo(params).enqueue(new Callback<Result<TransportationOrderListDTO>>() {
            @Override
            public void onResponse(Call<Result<TransportationOrderListDTO>> call, Response<Result<TransportationOrderListDTO>> response) {
                Result<TransportationOrderListDTO> result = response.body();
                if(result!=null&&result.isSuccess()){
                    listDTO = result.getData();
                    if(listDTO!=null&&listDTO.getOrderAddressDTO()!=null){
                        edtSignMan.setText(listDTO.getOrderAddressDTO().getConsigneeMan());
                        originCity.setText("始:"+listDTO.getOriginCity());
                        destCity.setText("终:"+listDTO.getDestCity());
                    }
                }else{
                    Toast.makeText(SignUpActivity.this,"返回数据异常",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result<TransportationOrderListDTO>> call, Throwable t) {
                Toast.makeText(SignUpActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        showView();

    }

    private void showView() {
        try {
            tvLdOrderNo.setText(dto.getTsOrderNo());
            edtTotalPackageQty.setText(NumberUtil.getValue(dto.getLoadedPackageQty())+"箱");
            edtTotalWeight.setText(NumberUtil.getValue(dto.getLoadedWeight())+"公斤");
            edtTotalVolume.setText(NumberUtil.getValue(dto.getLoadedVolume())+"立方");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.btnChoosePic, R.id.btnSubmit, R.id.btnSignDate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnChoosePic:
                choosePic();
                break;
            case R.id.btnSubmit:
                if(edtSignMan.getText().toString().length()==0){
                    Toast.makeText(SignUpActivity.this,"签收人为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog = DialogTool.showLoadingDialog(SignUpActivity.this,"签收中...");
                submit();
                break;
            case R.id.btnSignDate:
                DialogTool.DatePickerDialog(this, DateUtil.getDate(btnSignDate.getText().toString())).show(getSupportFragmentManager(), "btnStartDate");
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

    @OnCheckedChanged(R.id.cbException)
    void onChecked(boolean checked) {
        if (checked) {
            layoutException.setVisibility(View.VISIBLE);
        } else {
            layoutException.setVisibility(View.GONE);
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

    private void submit() {

        btnSubmit.setClickable(false);
        TransportationOrderSignDto signDto = new TransportationOrderSignDto();
        signDto.setTsOrderNo(dto.getTsOrderNo());
        signDto.setTsOrderId(dto.getTsOrderId());
        signDto.setSignIdCardNo(edtIdCard.getText().toString().trim());
        signDto.setSignMan(edtSignMan.getText().toString().trim());
        signDto.setSignRemark(edtRemark.getText().toString().trim());
        signDto.setSignPackageQty(listDTO.getTotalPackageQty());
        signDto.setSignWeight(listDTO.getTotalWeight());
        signDto.setSignVolume(listDTO.getTotalVolume());
        signDto.setCreateUserId(TLApplication.getInstance().getSysUserListDTO().getUserId());
        signDto.setCreateUserName(TLApplication.getInstance().getSysUserListDTO().getFullName());
        if (cbException.isChecked()) {
            OrderException orderException = (OrderException) spException.getSelectedItem();
            CommonEnum.ExceptionCategory category = CommonEnum.ExceptionCategory.valueOf(orderException.getName());
            signDto.setExceptionCategory(category);
            signDto.setExceptionDesc(edtException.getText().toString());
        }
        Map<String, Object> params = new HashMap<>();
        params.put("signDto",signDto);
        params.put("ldOrderId",dto.getLdOrderId());
        params.put("taskId",dto.getTaskId());
        RetrofitFactory.getApiService().sign(params).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                Result result = response.body();

                if (result.isSuccess()) {
                    if(mediaBeens.size()>0){
                        uploadFiles();
                    }else{
                        btnSubmit.setClickable(true);
                        Toast.makeText(SignUpActivity.this, "签收成功", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    if(dialog!=null){
                        dialog.cancel();
                    }
                    btnSubmit.setClickable(true);
                    Toast.makeText(SignUpActivity.this, "签收失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                if(dialog!=null){
                    dialog.cancel();
                }
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void uploadFiles() {
        List<String> filePath = new ArrayList<>();
        for(MediaBean bean:mediaBeens){
            filePath.add(bean.getOriginalPath());
        }
        FileUploadUtil.uploadFiles(filePath, TLApplication.getInstance().FILEUPLOAD+"?tsOrderNo="+dto.getTsOrderNo()+"&userId="+TLApplication.getInstance().getSysUserListDTO().getUserId(), new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                Toast.makeText(SignUpActivity.this, "签收成功,上传图片成功", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                btnSubmit.setClickable(true);
                Log.e("ss",s+","+e.getMessage());
                Toast.makeText(SignUpActivity.this, "签收成功,上传图片失败", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        try {
            if (timePickerView.getTag().equals("btnStartDate")) {
                btnSignDate.setText(DateUtil.getDate(millseconds));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                convertView = LayoutInflater.from(SignUpActivity.this).inflate(R.layout.gf_adapter_photo_list_item, null);
                vh.mIvThumb = (ImageView) convertView.findViewById(R.id.iv_thumb);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            final MediaBean mediaBean = mediaBeens.get(position);

            Glide.with(SignUpActivity.this).load(mediaBean.getOriginalPath())
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
