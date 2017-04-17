package cn.xyy.tltms.app.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by dev on 2017/4/1.
 */

public class FeeRequestItemCreatePO implements Serializable {

    private String feeAccountCode;
    private String codeName;
    private BigDecimal requestAmount;

    public String getFeeAccountCode() {
        return feeAccountCode;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
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
}
