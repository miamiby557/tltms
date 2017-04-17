package cn.xyy.tltms.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.lljjcoder.citypickerview.widget.CityPicker;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.DeliveryTaskListDto;
import cn.xyy.tltms.app.data.model.TransportationOrderListDTO;
import cn.xyy.tltms.app.tltms.TLApplication;
import cn.xyy.tltms.app.utils.DatePickDialogUtil;
import cn.xyy.tltms.app.utils.NumberUtil;
import cn.xyy.tltms.app.utils.PickerView;
import cn.xyy.tltms.app.utils.TimePickDialogUtil;

/**
 * Created by Xzy on 2016/6/27.
 */

public class DialogTool {

    static DecimalFormat def = new DecimalFormat("#.00");
    static DateFormat df1 = new SimpleDateFormat("yyyy年MM月dd日");

    static DateFormat df2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

    static DateFormat df3 = new SimpleDateFormat("HH:mm");

    public static TimePickerDialog timePickerDialog(OnDateSetListener listener, long currentTimeMillis) {
        TimePickerDialog dialog = new TimePickerDialog.Builder()
                .setCallBack(listener)
                .setCancelStringId(TLApplication.getInstance().getString(R.string.picker_cancel))
                .setSureStringId(TLApplication.getInstance().getString(R.string.picker_sure))
                .setTitleStringId(TLApplication.getInstance().getString(R.string.picker_title))
                .setCyclic(false)
                .setMinMillseconds(0)//System.currentTimeMillis()
                .setCurrentMillseconds(currentTimeMillis)
                .setThemeColor(ContextCompat.getColor(TLApplication.getInstance(), R.color.colorPrimaryDark))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(ContextCompat.getColor(TLApplication.getInstance(), R.color.timepicker_dialog_bg))
                .setWheelItemTextSelectorColor(ContextCompat.getColor(TLApplication.getInstance(), R.color.colorPrimary))
                .setWheelItemTextSize(12)
                .build();
        return dialog;
    }


    public static TimePickerDialog DatePickerDialog(OnDateSetListener listener, long currentTimeMillis) {
        TimePickerDialog dialog = new TimePickerDialog.Builder()
                .setCallBack(listener)
                .setCancelStringId(TLApplication.getInstance().getString(R.string.picker_cancel))
                .setSureStringId(TLApplication.getInstance().getString(R.string.picker_sure))
                .setTitleStringId(TLApplication.getInstance().getString(R.string.picker_title))
                .setCyclic(false)
                .setMinMillseconds(0)//System.currentTimeMillis()
                .setCurrentMillseconds(currentTimeMillis)
                .setThemeColor(ContextCompat.getColor(TLApplication.getInstance(), R.color.colorPrimaryDark))
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextNormalColor(ContextCompat.getColor(TLApplication.getInstance(), R.color.timepicker_dialog_bg))
                .setWheelItemTextSelectorColor(ContextCompat.getColor(TLApplication.getInstance(), R.color.colorPrimary))
                .setWheelItemTextSize(12)
                .build();
        return dialog;
    }

    public static Dialog showConfirmCancelDialog(final Activity activity,
                                                 int gravity, String title,String operateText, String confirmText,
                                                 String cancelText, View.OnClickListener confirm) {
        final Dialog mDialog = showCommonDialog(activity,
                R.layout.dialog_confirm_tsorder, gravity);
        TextView common_dialog_cancel_txt = (TextView) mDialog
                .findViewById(R.id.common_dialog_cancel_txt);
        TextView common_dialog_confirm_txt = (TextView) mDialog
                .findViewById(R.id.common_dialog_confirm_txt);
        TextView common_dialog_title_txt = (TextView) mDialog
                .findViewById(R.id.common_dialog_title_txt);
        TextView operate = (TextView)mDialog.findViewById(R.id.operate);
        operate.setText(title+"时间");
        final TextView date = (TextView)mDialog.findViewById(R.id.date);
        final TextView time = (TextView)mDialog.findViewById(R.id.time);
        final String dfString1 = df1.format(new Date());
        final String dfString2 = df2.format(new Date());
        date.setText(dfString1);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickDialogUtil dateTimePicKDialog = new DatePickDialogUtil(
                        activity, dfString1);
                dateTimePicKDialog.dateTimePicKDialog(date);
            }
        });
        final String tString = df3.format(new Date());
        time.setText(tString);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickDialogUtil timePickDialogUtil = new TimePickDialogUtil(activity,dfString2);
                timePickDialogUtil.dateTimePicKDialog(time);
            }
        });
        common_dialog_title_txt.setTextColor(activity.getResources().getColor(
                R.color.black));
        common_dialog_title_txt.setText(title);
        common_dialog_cancel_txt.setText(cancelText);
        common_dialog_confirm_txt.setText(confirmText);
        common_dialog_confirm_txt.setText(confirmText);
        common_dialog_cancel_txt.setText(cancelText);
        common_dialog_cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        common_dialog_confirm_txt.setOnClickListener(confirm);
        mDialog.show();
        return mDialog;
    }

    public static Dialog showLoadCompletedDialog(final Activity activity, DeliveryTaskListDto deliveryTaskListDto
                                                 , View.OnClickListener confirm) {
        final Dialog mDialog = showCommonDialog(activity,
                R.layout.dialog_loadcompleted, Gravity.CENTER);
        TextView common_dialog_cancel_txt = (TextView) mDialog
                .findViewById(R.id.common_dialog_cancel_txt);
        TextView common_dialog_confirm_txt = (TextView) mDialog
                .findViewById(R.id.common_dialog_confirm_txt);
        final TextView date = (TextView)mDialog.findViewById(R.id.date);
        final TextView time = (TextView)mDialog.findViewById(R.id.time);
        final String dfString1 = df1.format(new Date());
        final String dfString2 = df2.format(new Date());
        date.setText(dfString1);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickDialogUtil dateTimePicKDialog = new DatePickDialogUtil(
                        activity, dfString1);
                dateTimePicKDialog.dateTimePicKDialog(date);
            }
        });
        final String tString = df3.format(new Date());
        time.setText(tString);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickDialogUtil timePickDialogUtil = new TimePickDialogUtil(activity,dfString2);
                timePickDialogUtil.dateTimePicKDialog(time);
            }
        });
        EditText totalPackageQty = (EditText)mDialog.findViewById(R.id.totalPackageQty);
        EditText totalVolume = (EditText)mDialog.findViewById(R.id.totalVolume);
        EditText totalWeight = (EditText)mDialog.findViewById(R.id.totalWeight);
        TextView preTotalPackageQty = (TextView)mDialog.findViewById(R.id.preTotalPackageQty);
        TextView preTotalVolume = (TextView)mDialog.findViewById(R.id.preTotalVolume);
        TextView preTotalWeight = (TextView)mDialog.findViewById(R.id.preTotalWeight);
        preTotalPackageQty.setText(NumberUtil.getValue(deliveryTaskListDto.getPackageQty()));
        preTotalVolume.setText(NumberUtil.getValue(deliveryTaskListDto.getVolume()));
        preTotalWeight.setText(NumberUtil.getValue(deliveryTaskListDto.getWeight()));
        totalPackageQty.setText(NumberUtil.getValue(deliveryTaskListDto.getPackageQty()));
        totalVolume.setText(NumberUtil.getValue(deliveryTaskListDto.getVolume()));
        totalWeight.setText(NumberUtil.getValue(deliveryTaskListDto.getWeight()));
        common_dialog_cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        common_dialog_confirm_txt.setOnClickListener(confirm);
        mDialog.show();
        return mDialog;
    }

    /**
     * 公用弹窗对话框
     *
     * @param activity
     * @param resId
     * @param gravity  位置
     * @return
     */

    public static Dialog showCommonDialog(Activity activity, int resId,
                                          int gravity) {
        final Dialog mDialog = new Dialog(activity, R.style.common_dialog_style);
        mDialog.setContentView(resId);
        WindowManager m = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay();
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = d.getWidth();
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(gravity);
        mDialog.setCancelable(true);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                mDialog.dismiss();
            }
        });
        mDialog.setCancelable(false);
        return mDialog;
    }

    public static Dialog showUpdateDialog(Activity activity, View.OnClickListener listener) {
        final Dialog mDialog = showCommonDialog(activity, R.layout.dialog_operation, Gravity.BOTTOM);
        TextView version = (TextView) mDialog.findViewById(R.id.version);
        TextView select_photo_cancel_txt = (TextView) mDialog.findViewById(R.id.select_photo_cancel_txt);
        version.setOnClickListener(listener);
        select_photo_cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        return mDialog;
    }

    public static Dialog showLoadingDialog(Activity activity,String text) {
        final Dialog mDialog = showCommonDialog(activity, R.layout.dialog_loading, Gravity.CENTER);
        TextView tText = (TextView) mDialog.findViewById(R.id.text);
        tText.setText(text);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        return mDialog;
    }

    public static Dialog showConfirmUpdateDialog(Activity activity,String loVersion,String seVersion,View.OnClickListener listener) {
        final Dialog mDialog = showCommonDialog(activity, R.layout.dialog_version, Gravity.CENTER);
        TextView localVersion = (TextView) mDialog.findViewById(R.id.localVersion);
        TextView serverVersion = (TextView) mDialog.findViewById(R.id.serverVersion);
        localVersion.setText(loVersion);
        serverVersion.setText(seVersion);
        TextView common_dialog_cancel_txt = (TextView) mDialog.findViewById(R.id.common_dialog_cancel_txt);
        TextView common_dialog_confirm_txt = (TextView) mDialog.findViewById(R.id.common_dialog_confirm_txt);
        common_dialog_confirm_txt.setOnClickListener(listener);
        common_dialog_cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        return mDialog;
    }

    public static Dialog selectRoleDialog(Activity activity, View.OnClickListener listener1,View.OnClickListener listener2) {
        final Dialog mDialog = showCommonDialog(activity, R.layout.dialog_select_role, Gravity.CENTER);
        TextView driver = (TextView) mDialog.findViewById(R.id.driver);
        TextView dispatcher = (TextView) mDialog.findViewById(R.id.dispatcher);
        driver.setOnClickListener(listener1);
        dispatcher.setOnClickListener(listener2);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        return mDialog;
    }

    public static Dialog showReasonDialog(Activity activity, View.OnClickListener listener){
        final Dialog mDialog = showCommonDialog(activity, R.layout.dialog_reason, Gravity.CENTER);
        EditText reason = (EditText) mDialog.findViewById(R.id.reason);
        TextView common_dialog_cancel_txt = (TextView) mDialog.findViewById(R.id.common_dialog_cancel_txt);
        TextView common_dialog_confirm_txt = (TextView) mDialog.findViewById(R.id.common_dialog_confirm_txt);
        mDialog.setCanceledOnTouchOutside(true);
        common_dialog_cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        common_dialog_confirm_txt.setOnClickListener(listener);
        mDialog.show();
        return mDialog;
    }
    //修改运单信息
    public static Dialog showModifyDialog(Activity activity, TransportationOrderListDTO listDTO,View.OnClickListener listener){
        final Dialog mDialog = showCommonDialog(activity, R.layout.dialog_modify_task, Gravity.CENTER);
        EditText totalPackageQty = (EditText)mDialog.findViewById(R.id.totalPackageQty);
        EditText totalVolume = (EditText)mDialog.findViewById(R.id.totalVolume);
        EditText totalWeight = (EditText)mDialog.findViewById(R.id.totalWeight);
        totalPackageQty.setText(NumberUtil.getValue(listDTO.getTotalPackageQty()));
        totalVolume.setText(NumberUtil.getValue(listDTO.getTotalVolume()));
        totalWeight.setText(NumberUtil.getValue(listDTO.getTotalWeight()));
        TextView common_dialog_cancel_txt = (TextView) mDialog.findViewById(R.id.common_dialog_cancel_txt);
        TextView common_dialog_confirm_txt = (TextView) mDialog.findViewById(R.id.common_dialog_confirm_txt);
        mDialog.setCanceledOnTouchOutside(true);
        common_dialog_cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        common_dialog_confirm_txt.setOnClickListener(listener);
        mDialog.show();
        return mDialog;
    }

    public static CityPicker showSelectAddress(Activity activity) {
        CityPicker cityPicker = new CityPicker.Builder(activity)
                .textSize(20)
                .title("地址选择")
                //.backgroundPop(0xa0000000)
                .titleBackgroundColor("#378AF3")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("广东省")
                .city("广州市")
                .district("天河区")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();
        return cityPicker;
    }
}
