package cn.xyy.tltms.app.data.model;

import java.io.Serializable;

/**
 * Created by dev on 2017/4/13.
 */

public class VehicleListDTO implements Serializable {
    private String vehicleId;                             //ID
    private String appUserId;                             //app用户ID
    private String code;                                  //车辆编号
    private String vehicleNo;
    private String ownerName;                           //车主姓名
    private String ownerPhone;                         //车主电话
    private String ownerIdCardNo;                     //车主身份证号码
    private String driver;                             //司机
    private String driverPhone;
    private String appUserName;
    private String createUserName;
    private String modifyUserName;
    private boolean loadStatus;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerIdCardNo() {
        return ownerIdCardNo;
    }

    public void setOwnerIdCardNo(String ownerIdCardNo) {
        this.ownerIdCardNo = ownerIdCardNo;
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

    public String getAppUserName() {
        return appUserName;
    }

    public void setAppUserName(String appUserName) {
        this.appUserName = appUserName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getModifyUserName() {
        return modifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
    }

    public boolean isLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(boolean loadStatus) {
        this.loadStatus = loadStatus;
    }
}
