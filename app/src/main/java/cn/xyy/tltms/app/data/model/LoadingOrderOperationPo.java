package cn.xyy.tltms.app.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LoadingOrderOperationPo implements Serializable {
    private String ldOrderId;
    private String operationUserId;
    private String operationUserName;
    private List<String> taskIds;
    private String appUserId;
    private Date operationTime;

    public String getLdOrderId() {
        return ldOrderId;
    }

    public void setLdOrderId(String ldOrderId) {
        this.ldOrderId = ldOrderId;
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

    public List<String> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<String> taskIds) {
        this.taskIds = taskIds;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }
}


