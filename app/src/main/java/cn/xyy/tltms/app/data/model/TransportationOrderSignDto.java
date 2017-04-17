package cn.xyy.tltms.app.data.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class TransportationOrderSignDto implements Serializable {
    private String signId;
    private String tsOrderId;
    private String tsOrderNo;
    private Date orderDate;
    private String signMan;
    private Date signTime;
    private String signIdCardNo;
    private BigDecimal signWeight;
    private BigDecimal signVolume;
    private Integer signPackageQty;
    private String signRemark;
    private Date createTime;
    private String createUserId;
    private String createUserName;
    //如有异常，记录异常
    private String exceptionDesc;//异常描述
    private CommonEnum.ExceptionCategory exceptionCategory;//异常类型


    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getTsOrderId() {
        return tsOrderId;
    }

    public void setTsOrderId(String tsOrderId) {
        this.tsOrderId = tsOrderId;
    }

    public String getTsOrderNo() {
        return tsOrderNo;
    }

    public void setTsOrderNo(String tsOrderNo) {
        this.tsOrderNo = tsOrderNo;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getSignMan() {
        return signMan;
    }

    public void setSignMan(String signMan) {
        this.signMan = signMan;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getSignIdCardNo() {
        return signIdCardNo;
    }

    public void setSignIdCardNo(String signIdCardNo) {
        this.signIdCardNo = signIdCardNo;
    }

    public BigDecimal getSignWeight() {
        return signWeight;
    }

    public void setSignWeight(BigDecimal signWeight) {
        this.signWeight = signWeight;
    }

    public BigDecimal getSignVolume() {
        return signVolume;
    }

    public void setSignVolume(BigDecimal signVolume) {
        this.signVolume = signVolume;
    }

    public Integer getSignPackageQty() {
        return signPackageQty;
    }

    public void setSignPackageQty(Integer signPackageQty) {
        this.signPackageQty = signPackageQty;
    }

    public String getSignRemark() {
        return signRemark;
    }

    public void setSignRemark(String signRemark) {
        this.signRemark = signRemark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }


    public String getExceptionDesc() {
        return exceptionDesc;
    }

    public void setExceptionDesc(String exceptionDesc) {
        this.exceptionDesc = exceptionDesc;
    }

    public CommonEnum.ExceptionCategory getExceptionCategory() {
        return exceptionCategory;
    }

    public void setExceptionCategory(CommonEnum.ExceptionCategory exceptionCategory) {
        this.exceptionCategory = exceptionCategory;
    }
}
