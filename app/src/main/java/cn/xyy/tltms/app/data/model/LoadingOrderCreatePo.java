package cn.xyy.tltms.app.data.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dev on 2017/4/13.
 */

public class LoadingOrderCreatePo implements Serializable {
    private String appUserId;
    private String siteId;
    private String vehicleId;
    private String vehicleNo;
    private Double maxLoadVolume;
    private Double maxLoadWeight;
    private String driver;
    private String driverPhone;
    private String remark;
    private String createUserId;
    private String createUserName;
    private CommonEnum.CalculateType calculateType;
    private String originCode;
    private String destCode;
    private List<DeliveryTaskCreatePo> deliveryTaskCreatePos;

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

    public Double getMaxLoadVolume() {
        return maxLoadVolume;
    }

    public void setMaxLoadVolume(Double maxLoadVolume) {
        this.maxLoadVolume = maxLoadVolume;
    }

    public Double getMaxLoadWeight() {
        return maxLoadWeight;
    }

    public void setMaxLoadWeight(Double maxLoadWeight) {
        this.maxLoadWeight = maxLoadWeight;
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

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getDestCode() {
        return destCode;
    }

    public void setDestCode(String destCode) {
        this.destCode = destCode;
    }

    public List<DeliveryTaskCreatePo> getDeliveryTaskCreatePos() {
        return deliveryTaskCreatePos;
    }

    public void setDeliveryTaskCreatePos(List<DeliveryTaskCreatePo> deliveryTaskCreatePos) {
        this.deliveryTaskCreatePos = deliveryTaskCreatePos;
    }

    public CommonEnum.CalculateType getCalculateType() {
        return calculateType;
    }

    public void setCalculateType(CommonEnum.CalculateType calculateType) {
        this.calculateType = calculateType;
    }
}
