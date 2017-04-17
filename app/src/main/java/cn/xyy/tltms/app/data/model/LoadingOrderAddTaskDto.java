package cn.xyy.tltms.app.data.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dev on 2017/4/14.
 */

public class LoadingOrderAddTaskDto implements Serializable {
    private String ldOrderId;
    private List<DeliveryTaskCreatePo> tasks;
    private String operationUserId;
    private String operationUserName;
    private String appUserId;

    public String getLdOrderId() {
        return ldOrderId;
    }

    public void setLdOrderId(String ldOrderId) {
        this.ldOrderId = ldOrderId;
    }

    public List<DeliveryTaskCreatePo> getTasks() {
        return tasks;
    }

    public void setTasks(List<DeliveryTaskCreatePo> tasks) {
        this.tasks = tasks;
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
}
