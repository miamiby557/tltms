package cn.xyy.tltms.app.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by dev on 2017/4/13.
 */

public class DeliveryTaskCreatePo implements Serializable {
    private String tsOrderNo;
    private Integer packageQty;
    private BigDecimal volume;
    private BigDecimal weight;

    public String getTsOrderNo() {
        return tsOrderNo;
    }

    public void setTsOrderNo(String tsOrderNo) {
        this.tsOrderNo = tsOrderNo;
    }

    public Integer getPackageQty() {
        return packageQty;
    }

    public void setPackageQty(Integer packageQty) {
        this.packageQty = packageQty;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
