package cn.xyy.tltms.app.utils;

import java.math.BigDecimal;

/**
 * Created by dev on 2017/4/6.
 */

public class NumberUtil {
    static java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

    public static String getValue(BigDecimal bigDecimal){
        if(bigDecimal==null || bigDecimal.floatValue()==0){
            return "0";
        }
        String dfString = df.format(bigDecimal);
        return dfString;
    }

    public static String getValue(Integer integer){
        if(integer==null || integer.intValue()==0){
            return "0";
        }
        return integer+"";
    }
}
