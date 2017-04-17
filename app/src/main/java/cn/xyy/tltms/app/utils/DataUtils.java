package cn.xyy.tltms.app.utils;

import android.content.Context;
import android.content.pm.PackageInfo;

import java.util.ArrayList;
import java.util.List;

import cn.xyy.tltms.app.data.model.CommonEnum;
import cn.xyy.tltms.app.data.model.OrderException;


/**
 * Created by admin on 2017/3/19.
 */

public class DataUtils {



    public static List<CalculateType> getCalculateTypes(){
        List<CalculateType> calculateTypes = new ArrayList<>();
        calculateTypes.add(new CalculateType(CommonEnum.CalculateType.VOLUME.name(),CommonEnum.CalculateType.VOLUME.getText()));
        calculateTypes.add(new CalculateType(CommonEnum.CalculateType.WEIGHT.name(),CommonEnum.CalculateType.WEIGHT.getText()));
        calculateTypes.add(new CalculateType(CommonEnum.CalculateType.PACKAGE.name(),CommonEnum.CalculateType.PACKAGE.getText()));
        calculateTypes.add(new CalculateType(CommonEnum.CalculateType.CAR.name(),CommonEnum.CalculateType.CAR.getText()));
        calculateTypes.add(new CalculateType(CommonEnum.CalculateType.VOLUME_DISTANCE.name(),CommonEnum.CalculateType.VOLUME_DISTANCE.getText()));
        return calculateTypes;
    }

    public static List<OrderException> getExceptionData(){

        List<OrderException> orderExceptions = new ArrayList<>();
        orderExceptions.add(new OrderException("DS","丢失"));
        orderExceptions.add(new OrderException("THYW","提货延误"));
        orderExceptions.add(new OrderException("PSYW","配送延误"));
        orderExceptions.add(new OrderException("PS","破损"));
        orderExceptions.add(new OrderException("CH","串货"));
        orderExceptions.add(new OrderException("ZDSG","重大事故"));

        return orderExceptions;
    }

    public static String getVersionCode(Context context)//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

}

