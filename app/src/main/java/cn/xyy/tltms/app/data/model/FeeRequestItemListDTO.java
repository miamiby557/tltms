package cn.xyy.tltms.app.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by dev on 2017/4/4.
 */

public class FeeRequestItemListDTO implements Serializable {
    private String itemId;
    private String requestId;
    private String feeAccountCode;
    private String feeAccountName;
    private BigDecimal requestAmount;
    private BigDecimal reviewAmount;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getFeeAccountCode() {
        return feeAccountCode;
    }

    public void setFeeAccountCode(String feeAccountCode) {
        this.feeAccountCode = feeAccountCode;
    }

    public String getFeeAccountName() {
        return feeAccountName;
    }

    public void setFeeAccountName(String feeAccountName) {
        this.feeAccountName = feeAccountName;
    }

    public BigDecimal getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(BigDecimal requestAmount) {
        this.requestAmount = requestAmount;
    }

    public BigDecimal getReviewAmount() {
        return reviewAmount;
    }

    public void setReviewAmount(BigDecimal reviewAmount) {
        this.reviewAmount = reviewAmount;
    }
}
