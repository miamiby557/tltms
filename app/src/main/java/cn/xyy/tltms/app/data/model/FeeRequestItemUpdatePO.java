package cn.xyy.tltms.app.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by dev on 2017/4/5.
 */

public class FeeRequestItemUpdatePO implements Serializable {
    private String itemId;
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

    public String getFeeAccountCode() {
        return feeAccountCode;
    }

    public void setFeeAccountCode(String feeAccountCode) {
        this.feeAccountCode = feeAccountCode;
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

    public String getFeeAccountName() {
        return feeAccountName;
    }

    public void setFeeAccountName(String feeAccountName) {
        this.feeAccountName = feeAccountName;
    }
}
