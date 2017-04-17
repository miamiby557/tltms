package cn.xyy.tltms.app.utils;

/**
 * Created by admin on 2017/3/18.
 */

public class Constant {
    public static final String ORDER_NO = "orderNo";
    public static final String APP_USER_ID = "orderNo";

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";
    public static final String TIME_FORMAT = "HH:mm:ss";
    //    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //DeliveryTaskStatus

    public static String DELIVERY_TASK_STATUS_WAITING = "WAITING";
    public static String DELIVERY_TASK_STATUS_LOADING = "LOADING";
    public static String DELIVERY_TASK_STATUS_LOADED = "LOADED";
    public static String DELIVERY_TASK_STATUS_INTRANSIT = "INTRANSIT";
    public static String DELIVERY_TASK_STATUS_ARRIVE = "ARRIVE";
    public static String DELIVERY_TASK_STATUS_FINISH = "FINISH";

    public static String DELIVERY_TASK_STATUS_WAITING_STR = "待装车";
    public static String DELIVERY_TASK_STATUS_LOADING_STR = "装车中";
    public static String DELIVERY_TASK_STATUS_LOADED_STR = "已装车";
    public static String DELIVERY_TASK_STATUS_INTRANSIT_STR = "在途";
    public static String DELIVERY_TASK_STATUS_ARRIVE_STR = "到达";
    public static String DELIVERY_TASK_STATUS_FINISH_STR = "完成";


    public static String ORDER_STATUS_NEW = "NEW";
    public static String ORDER_STATUS_LOADING = "LOADING";
    public static String ORDER_STATUS_LOADED = "LOADED";
    public static String ORDER_STATUS_INTRANSIT = "INTRANSIT";
    public static String ORDER_STATUS_ARRIVE = "ARRIVE";
    public static String ORDER_STATUS_FINISH = "FINISH";
    public static String ORDER_STATUS_CANCEL = "CANCEL";

    public static String ORDER_STATUS_NEW_STR = "新建";
    public static String ORDER_STATUS_LOADING_STR = "装车中";
    public static String ORDER_STATUS_LOADED_STR = "装车完成";
    public static String ORDER_STATUS_INTRANSIT_STR = "在途";
    public static String ORDER_STATUS_ARRIVE_STR = "到达";
    public static String ORDER_STATUS_FINISH_STR = "完成";
    public static String ORDER_STATUS_CANCEL_STR = "取消";


    public static long OKHTTP_CONNECT_TIMEOUT = 60;
    public static long OKHTTP_READ_TIMEOUT = 60;
    public static long OKHTTP_WRITE_TIMEOUT = 60;

    //登录人身份
    public static String ROLE_KF = "客服";
    public static String ROLE_SJ = "司机";
    public static String ROLE_YZ = "运作";
    public static String ROLE_DD = "调度";
    public static String ROLE_CW = "财务";
    public static String ROLE_ADMIN = "管理员";
}
