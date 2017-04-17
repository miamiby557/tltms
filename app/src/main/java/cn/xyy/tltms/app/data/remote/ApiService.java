package cn.xyy.tltms.app.data.remote;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.xyy.tltms.app.data.model.FeeAccountListDTO;
import cn.xyy.tltms.app.data.model.FeeRequestCreatePO;
import cn.xyy.tltms.app.data.model.FeeRequestListDTO;
import cn.xyy.tltms.app.data.model.FeeRequestUpdatePO;
import cn.xyy.tltms.app.data.model.FileDTO;
import cn.xyy.tltms.app.data.model.LoadedOperationDto;
import cn.xyy.tltms.app.data.model.LoadingOrderAddTaskDto;
import cn.xyy.tltms.app.data.model.LoadingOrderCancelDto;
import cn.xyy.tltms.app.data.model.LoadingOrderCreatePo;
import cn.xyy.tltms.app.data.model.LoadingOrderListDto;
import cn.xyy.tltms.app.data.model.LoadingOrderLocationCreatePO;
import cn.xyy.tltms.app.data.model.LoadingOrderOperationPo;
import cn.xyy.tltms.app.data.model.ProjectListDTO;
import cn.xyy.tltms.app.data.model.SysUserListDTO;
import cn.xyy.tltms.app.data.model.TransportationOrderListDTO;
import cn.xyy.tltms.app.data.model.TransportationOrderQuickCreateDto;
import cn.xyy.tltms.app.data.model.TransportationOrderSignDto;
import cn.xyy.tltms.app.data.model.TransportationOrderTraceListDTO;
import cn.xyy.tltms.app.data.model.VehicleListDTO;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by admin on 2017/3/18.
 */

public interface ApiService {

    //1.按车辆查询配载单记录
    @POST("api/ldOrder/findByAssignedTo")
    Call<Result<List<LoadingOrderListDto>>> findByAssignedT(@Body Map<String, String> params);

    //1测试
    @POST("api/ldOrder")
    Call<Result<List<LoadingOrderListDto>>> ldOrder();

    @POST("api/ldOrder/findActiveByAssignedTo")
    Call<Result<List<LoadingOrderListDto>>> findActiveByAssignedTo(@Body Map<String, String> params);

    /*配载运单开始装车*/
    @POST("api/ldOrder/startLoading")
    Call<Result> startLoading(@Body LoadingOrderOperationPo params);

    /*配载运单完成装车*/
    @POST("api/ldOrder/loadFinish")
    Call<Result> loadFinish(@Body LoadingOrderOperationPo params);

    /*配载运单发车*/
    @POST("api/ldOrder/setOut")
    Call<Result> setOut(@Body LoadingOrderOperationPo params);

    /*配载运单到达*/
    @POST("api/ldOrder/arrive")
    Call<Result> setArrive(@Body LoadingOrderOperationPo params);

    /*配载运单预到达*/
    @POST("api/ldOrder/planArrive")
    Call<Result> preArrival(@Body LoadingOrderOperationPo params);

//    /*配载运单签收*/
//    @POST("api/ldOrder/setOut")
//    Call<Result> setSignUp(@Body LoadingOrderOperationPo params);

    /*配载运单完成卸货*/
    @POST("api/ldOrder/finish")
    Call<Result> completeUnloading(@Body LoadingOrderOperationPo params);

    /*按单号查询运单*/
    @POST("api/tsOrder/findByTsOrderNo")
    Call<Result<TransportationOrderListDTO>> findByTsOrderNo(@Body Map<String, String> params);

    /*配载运单签收*//*
    @POST("api/tsOrder/sign")
    Call<Result> sign(@Body TransportationOrderSignDto params);*/

    /*配载运单签收*/
    @POST("api/tsOrder/sign")
    Call<Result> sign(@Body Map<String, Object> params);

    /*发送app坐标*/
    @POST("api/ldOrder/location")
    Call<Result> sendLocation(@Body LoadingOrderLocationCreatePO LoadingOrderLocationCreatePO);

    /*登录*/
    @POST("api/login")
    Call<Result<SysUserListDTO>> login(@Body Map<String, String> params);

    /*调度页面配载单查找*/
    @POST("api/ldOrder/findActive")
    Call<Result<List<LoadingOrderListDto>>> findActive(@Body Map<String, String> params);

    /*创建配载单->查找托运单*/
    @POST("api/tsOrder/findListByTsOrderNo")
    Call<Result<List<TransportationOrderListDTO>>> loadingOrderFindTransportOrder(@Body Map<String, String> params);

    /*查找司机*/
    @POST("api/vehicle/findDriver")
    Call<Result<List<VehicleListDTO>>> findDriver(@Body Map<String, String> params);

    /*查找项目*/
    @POST("api/project/find")
    Call<Result<List<ProjectListDTO>>> findProject(@Body Map<String, String> params);

    /*快速开单*/
    @POST("api/tsOrder/create")
    Call<Result> quickOrder(@Body TransportationOrderQuickCreateDto dto);

    /*创建配载单*/
    @POST("api/ldOrder/create")
    Call<Result> dispatcherLoadingOrderCreate(@Body LoadingOrderCreatePo po);

    /*通过配载单号查询运单列表*/
    @POST("api/ldOrder/getByNo")
    Call<Result<LoadingOrderListDto>> getByNo(@Body Map<String, String> params);

    /*查找运单跟踪信息*/
    @POST("api/tsOrder/trackingInfoOfTsOrder")
    Call<Result<List<TransportationOrderTraceListDTO>>> trackingInfoOfTsOrder(@Body Map<String, String> params);

    /*配载单减单*/
    @POST("api/ldOrder/removeTsOrder")
    Call<Result> removeTsOrder(@Body LoadingOrderOperationPo orderOperationPo);

    /*配载单加单*/
    @POST("api/ldOrder/addTsOrder")
    Call<Result> addTsOrder(@Body LoadingOrderAddTaskDto addTaskDto);

    /*取消单加单*/
    @POST("api/ldOrder/cancel")
    Call<Result> cancel(@Body LoadingOrderCancelDto cancelDto);

    /*装车完成*/
    @POST("api/ldOrder/loadFinish")
    Call<Result> loadFinish(@Body LoadedOperationDto operationDto);

    /**
     * 拿到已经完成的配载单
     */
    @POST("api/ldOrder/findFinishedByAssignedTo")
    Call<Result<List<LoadingOrderListDto>>> findFinishedByAssignedTo(@Body Map<String, String> params);

    /*新建或者待确认的费用申报*/
    @POST("/api/feeRequest/unFinished")
    Call<Result<List<FeeRequestListDTO>>> unFinished(@Body Map<String, String> params);

    /*新建的费用申报*/
    @POST("/api/feeRequest/findCreate")
    Call<Result<List<FeeRequestListDTO>>> findCreate(@Body Map<String, String> params);

    /*待确认的费用申报*/
    @POST("/api/feeRequest/findVerity")
    Call<Result<List<FeeRequestListDTO>>> findVerity(@Body Map<String, String> params);

    /*已经完成的费用申报*/
    @POST("/api/feeRequest/hasFinished")
    Call<Result<List<FeeRequestListDTO>>> hasFinished(@Body Map<String, String> params);

    /*新建费用申报*/
    @POST("/api/feeRequest/apply")
    Call<Result<String>> apply(@Body FeeRequestCreatePO createPO);

    /*确认费用申报*/
    @POST("/api/feeRequest/verifyApply")
    Call<Result<String>> verifyApply(@Body Map<String, String> params);

    /*更新费用申报*/
    @POST("/api/feeRequest/update")
    Call<Result<String>> update(@Body FeeRequestUpdatePO updatePO);

    /**上传图片*/
    @POST("/api/tsOrder/uploadFiles")
    @Multipart
    Call<Result<String>> uploadFiles(@PartMap Map<String, RequestBody> partMap);

    /*取消费用申报*/
    @POST("/api/feeRequest/cancel")
    Call<Result<String>> cancel(@Body Map<String, String> params);

    /*费用科目*/
    @GET("/api/feeAccount/canApply")
    Call<Result<List<FeeAccountListDTO>>> canApply();

    /*版本号比较*/
    @GET("/api/sysFile/getVersion")
    Call<Result<FileDTO>> checkVersion();
}
