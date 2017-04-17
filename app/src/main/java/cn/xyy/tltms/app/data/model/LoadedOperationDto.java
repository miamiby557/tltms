package cn.xyy.tltms.app.data.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dev on 2017/4/5.
 */

public class LoadedOperationDto implements Serializable {
    private String taskId;
    private String operationUserId;
    private String operationUserName;
    private String appUserId;
    private Integer loadedPackageQty;
    private BigDecimal loadedVolume;
    private BigDecimal loadedWeight;
    private Date operationTime;

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public Integer getLoadedPackageQty() {
        return loadedPackageQty;
    }

    public void setLoadedPackageQty(Integer loadedPackageQty) {
        this.loadedPackageQty = loadedPackageQty;
    }

    public BigDecimal getLoadedVolume() {
        return loadedVolume;
    }

    public void setLoadedVolume(BigDecimal loadedVolume) {
        this.loadedVolume = loadedVolume;
    }

    public BigDecimal getLoadedWeight() {
        return loadedWeight;
    }

    public void setLoadedWeight(BigDecimal loadedWeight) {
        this.loadedWeight = loadedWeight;
    }
}
