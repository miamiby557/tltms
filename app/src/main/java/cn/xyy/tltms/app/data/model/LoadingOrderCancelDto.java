package cn.xyy.tltms.app.data.model;

import java.io.Serializable;

/**
 * Created by dev on 2017/4/14.
 */

public class LoadingOrderCancelDto implements Serializable {
    private String appUserId;
    private String userId;
    private String userName;
    private String ldOrderId;
    private String reason;

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLdOrderId() {
        return ldOrderId;
    }

    public void setLdOrderId(String ldOrderId) {
        this.ldOrderId = ldOrderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
