package cn.xyy.tltms.app.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by dev on 2017/4/13.
 */

public class TransportationOrderQuickCreateDto implements Serializable {
    private String appUserId;
    private String siteId;
    private String createUserId;
    private String clOrderNo;
    private String createUserName;
    private String projectId;
    private String originCityCode;
    private String destCityCode;
    private BigDecimal totalVolume;
    private BigDecimal totalWeight;
    private String consigneeMan;
    private String consigneePhone;

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getOriginCityCode() {
        return originCityCode;
    }

    public void setOriginCityCode(String originCityCode) {
        this.originCityCode = originCityCode;
    }

    public String getDestCityCode() {
        return destCityCode;
    }

    public void setDestCityCode(String destCityCode) {
        this.destCityCode = destCityCode;
    }

    public BigDecimal getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(BigDecimal totalVolume) {
        this.totalVolume = totalVolume;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getConsigneeMan() {
        return consigneeMan;
    }

    public void setConsigneeMan(String consigneeMan) {
        this.consigneeMan = consigneeMan;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public String getClOrderNo() {
        return clOrderNo;
    }

    public void setClOrderNo(String clOrderNo) {
        this.clOrderNo = clOrderNo;
    }
}
