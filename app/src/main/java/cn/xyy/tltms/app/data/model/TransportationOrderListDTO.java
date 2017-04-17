package cn.xyy.tltms.app.data.model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by LH 2446059046@qq.com on 2017/3/9.
 */

public class TransportationOrderListDTO implements Serializable {

    private String tsOrderId;                   //运单ID
    private String appUserId;                   //app用户ID
    private String projectId;                   //项目ID
    private String projectName;
    private String siteId;                      //站点ID
    private String clientId;                    //客户ID
    private String clOrderNo;                   //客户订单号
    private String tsOrderNo;                   //运单号
    private Date orderDate;                //运单日期
    private String originCityCode;                           //始发地代码
    private String originCity;
    private String destCityCode;                            //目的地代码
    private String destCity;
    private CommonEnum.OrderCategory orderType;
    private CommonEnum.LoadUnit loadingUnit;                         //配载单位
    private CommonEnum.TransportType transportType;                        //运输方式
    private CommonEnum.HandoverType handoverType;                         //交接方式：送货上门、送货上楼、自提
    private CommonEnum.CalculateType calculateType;
    private String specialRequire;              //特殊要求
//    private TransportationOrderEnum.OrderStatus status;                      //状态：已接单、已配载、已装车、在途、到达、签收、取消',
    private String remark;                      //备注
    private String createUserId;                //创建人ID
    private String createUserName;              //创建人
    private Date createTime;           //创建时间
    private String modifyUserId;                //修改人ID
    private String modifyUserName;                       //修改人
    private Date lastModifyDate;                //最后修改时间
    private String goodsCategory;
    private String goodsDesc;
    private boolean assignedStatus;


    private Integer totalPackageQty;                   //总件数
    private BigDecimal totalVolume;                        //总体积
    private BigDecimal totalWeight;


//    private List<TransportationOrderGoodsDTO> orderGoodsDTOs;

    private TransportationOrderSummaryDTO orderSummaryDTO;

    private TransportationOrderAddressDTO orderAddressDTO;

//    private TransportationOrderAddressDTO orderAddressDTO;


    public String getTsOrderId() {
        return tsOrderId;
    }

    public void setTsOrderId(String tsOrderId) {
        this.tsOrderId = tsOrderId;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClOrderNo() {
        return clOrderNo;
    }

    public void setClOrderNo(String clOrderNo) {
        this.clOrderNo = clOrderNo;
    }

    public String getTsOrderNo() {
        return tsOrderNo;
    }

    public void setTsOrderNo(String tsOrderNo) {
        this.tsOrderNo = tsOrderNo;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOriginCityCode() {
        return originCityCode;
    }

    public void setOriginCityCode(String originCityCode) {
        this.originCityCode = originCityCode;
    }

    public String getDestCityCode() {
        return destCityCode;
    }

    public void setDestCityCode(String destCityCode) {
        this.destCityCode = destCityCode;
    }

    public CommonEnum.OrderCategory getOrderType() {
        return orderType;
    }

    public void setOrderType(CommonEnum.OrderCategory orderType) {
        this.orderType = orderType;
    }

    public CommonEnum.LoadUnit getLoadingUnit() {
        return loadingUnit;
    }

    public void setLoadingUnit(CommonEnum.LoadUnit loadingUnit) {
        this.loadingUnit = loadingUnit;
    }

    public CommonEnum.TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(CommonEnum.TransportType transportType) {
        this.transportType = transportType;
    }

    public CommonEnum.HandoverType getHandoverType() {
        return handoverType;
    }

    public void setHandoverType(CommonEnum.HandoverType handoverType) {
        this.handoverType = handoverType;
    }

    public CommonEnum.CalculateType getCalculateType() {
        return calculateType;
    }

    public void setCalculateType(CommonEnum.CalculateType calculateType) {
        this.calculateType = calculateType;
    }

    public String getSpecialRequire() {
        return specialRequire;
    }

    public void setSpecialRequire(String specialRequire) {
        this.specialRequire = specialRequire;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public String getModifyUserName() {
        return modifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(String goodsCategory) {
        this.goodsCategory = goodsCategory;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public boolean isAssignedStatus() {
        return assignedStatus;
    }

    public void setAssignedStatus(boolean assignedStatus) {
        this.assignedStatus = assignedStatus;
    }

    public TransportationOrderSummaryDTO getOrderSummaryDTO() {
        return orderSummaryDTO;
    }

    public void setOrderSummaryDTO(TransportationOrderSummaryDTO orderSummaryDTO) {
        this.orderSummaryDTO = orderSummaryDTO;
    }

    public Integer getTotalPackageQty() {
        return totalPackageQty;
    }

    public void setTotalPackageQty(Integer totalPackageQty) {
        this.totalPackageQty = totalPackageQty;
    }

    public BigDecimal getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(BigDecimal totalVolume) {
        this.totalVolume = totalVolume;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public TransportationOrderAddressDTO getOrderAddressDTO() {
        return orderAddressDTO;
    }

    public void setOrderAddressDTO(TransportationOrderAddressDTO orderAddressDTO) {
        this.orderAddressDTO = orderAddressDTO;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getDestCity() {
        return destCity;
    }

    public void setDestCity(String destCity) {
        this.destCity = destCity;
    }
}
