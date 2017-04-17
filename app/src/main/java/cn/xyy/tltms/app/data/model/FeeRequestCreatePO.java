package cn.xyy.tltms.app.data.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by dev on 2017/4/1.
 */

public class FeeRequestCreatePO implements Serializable {
    private String appUserId;
    private String reviewAppUserId;
    private String tsOrderNo;
    private BigDecimal requestAmount;
    private String requestRemark;
    private String requestUserId;
    private String requestUserName;
    private String requestCategory = "LOADING";
    List<FeeRequestItemCreatePO> items;

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getReviewAppUserId() {
        return reviewAppUserId;
    }

    public void setReviewAppUserId(String reviewAppUserId) {
        this.reviewAppUserId = reviewAppUserId;
    }

    public String getTsOrderNo() {
        return tsOrderNo;
    }

    public void setTsOrderNo(String tsOrderNo) {
        this.tsOrderNo = tsOrderNo;
    }

    public BigDecimal getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(BigDecimal requestAmount) {
        this.requestAmount = requestAmount;
    }

    public String getRequestRemark() {
        return requestRemark;
    }

    public void setRequestRemark(String requestRemark) {
        this.requestRemark = requestRemark;
    }

    public String getRequestUserId() {
        return requestUserId;
    }

    public void setRequestUserId(String requestUserId) {
        this.requestUserId = requestUserId;
    }

    public String getRequestUserName() {
        return requestUserName;
    }

    public void setRequestUserName(String requestUserName) {
        this.requestUserName = requestUserName;
    }

    public String getRequestCategory() {
        return requestCategory;
    }

    public void setRequestCategory(String requestCategory) {
        this.requestCategory = requestCategory;
    }

    public List<FeeRequestItemCreatePO> getItems() {
        return items;
    }

    public void setItems(List<FeeRequestItemCreatePO> items) {
        this.items = items;
    }
}
