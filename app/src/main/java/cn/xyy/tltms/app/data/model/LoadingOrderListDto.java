package cn.xyy.tltms.app.data.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class LoadingOrderListDto implements Serializable {
    private String ldOrderId;
    private String appUserId;
    private String siteId;
    private String ldOrderNo;
    private String vehicleId;
    private String vehicleNo;
    private String driver;
    private String driverPhone;
    private BigDecimal totalFee;
    private LoadingOrderEnum.OrderStatus status;
    private Integer totalPackageQty;
    private BigDecimal totalVolume;
    private BigDecimal totalWeight;
    private String remark;
    private String createUserId;
    private Date createTime;
    private String createUserName;
    private String modifyUserId;
    private Date lastModifyTime;
    private String modifyUserName;
    private String originCity;
    private String destCity;

    private List<DeliveryTaskListDto> deliveryTaskListDtoList;
    private List<DeliveryTaskLogListDto> logListDtos;

    public LoadingOrderListDto() {
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

    public String getLdOrderId() {
        return ldOrderId;
    }

    public void setLdOrderId(String ldOrderId) {
        this.ldOrderId = ldOrderId;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getLdOrderNo() {
        return ldOrderNo;
    }

    public void setLdOrderNo(String ldOrderNo) {
        this.ldOrderNo = ldOrderNo;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public LoadingOrderEnum.OrderStatus getStatus() {
        return status;
    }

    public void setStatus(LoadingOrderEnum.OrderStatus status) {
        this.status = status;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getModifyUserName() {
        return modifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
    }

    public List<DeliveryTaskListDto> getDeliveryTaskListDtoList() {
        return deliveryTaskListDtoList;
    }

    public void setDeliveryTaskListDtoList(List<DeliveryTaskListDto> deliveryTaskListDtoList) {
        this.deliveryTaskListDtoList = deliveryTaskListDtoList;
    }

    public List<DeliveryTaskLogListDto> getLogListDtos() {
        return logListDtos;
    }

    public void setLogListDtos(List<DeliveryTaskLogListDto> logListDtos) {
        this.logListDtos = logListDtos;
    }
}
