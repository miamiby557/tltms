package cn.xyy.tltms.app.data.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dev on 2017/4/15.
 */

public class DeliveryTaskLogListDto implements Serializable {
    private String logId;
    private String taskId;
    private Date operationTime;
    private String operationUserId;
    private String operationUserName;
    private LoadingOrderEnum.DeliveryTaskStatus operationStatus;
    private String appVersion;
    private BigDecimal lng;
    private BigDecimal lat;
    private String tsOrderNo;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationUserId() {
        return operationUserId;
    }

    public void setOperationUserId(String operationUserId) {
        this.operationUserId = operationUserId;
    }

    public String getOperationUserName() {
        return operationUserName;
    }

    public void setOperationUserName(String operationUserName) {
        this.operationUserName = operationUserName;
    }

    public LoadingOrderEnum.DeliveryTaskStatus getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(LoadingOrderEnum.DeliveryTaskStatus operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public String getTsOrderNo() {
        return tsOrderNo;
    }

    public void setTsOrderNo(String tsOrderNo) {
        this.tsOrderNo = tsOrderNo;
    }
}
