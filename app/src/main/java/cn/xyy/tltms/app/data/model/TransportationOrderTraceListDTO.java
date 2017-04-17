package cn.xyy.tltms.app.data.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dev on 2017/4/14.
 */

public class TransportationOrderTraceListDTO implements Serializable {
    private String traceId;
    private String appUserId;
    private String tsOrderNo;
    private String tsOrderStatus;
    private Date traceTime;
    private String traceContent;
    private String traceUserId;
    private String traceUserName;
    private String traceSource;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getTsOrderNo() {
        return tsOrderNo;
    }

    public void setTsOrderNo(String tsOrderNo) {
        this.tsOrderNo = tsOrderNo;
    }

    public String getTsOrderStatus() {
        return tsOrderStatus;
    }

    public void setTsOrderStatus(String tsOrderStatus) {
        this.tsOrderStatus = tsOrderStatus;
    }

    public Date getTraceTime() {
        return traceTime;
    }

    public void setTraceTime(Date traceTime) {
        this.traceTime = traceTime;
    }

    public String getTraceContent() {
        return traceContent;
    }

    public void setTraceContent(String traceContent) {
        this.traceContent = traceContent;
    }

    public String getTraceUserId() {
        return traceUserId;
    }

    public void setTraceUserId(String traceUserId) {
        this.traceUserId = traceUserId;
    }

    public String getTraceUserName() {
        return traceUserName;
    }

    public void setTraceUserName(String traceUserName) {
        this.traceUserName = traceUserName;
    }

    public String getTraceSource() {
        return traceSource;
    }

    public void setTraceSource(String traceSource) {
        this.traceSource = traceSource;
    }
}
