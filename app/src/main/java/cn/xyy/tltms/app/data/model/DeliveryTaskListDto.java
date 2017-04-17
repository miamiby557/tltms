package cn.xyy.tltms.app.data.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DeliveryTaskListDto implements Serializable {
    private String taskId;
    private String ldOrderId;
    private String ldOrderNo;
    private String tsOrderId;
    private String tsOrderNo;
    private Integer packageQty;
    private BigDecimal volume;
    private BigDecimal weight;
    private Integer loadedPackageQty;
    private BigDecimal loadedVolume;
    private BigDecimal loadedWeight;
    private LoadingOrderEnum.DeliveryTaskStatus status;
    private Date startLoadTime;
    private Date endLoadTime;
    private Date startTime;
    private Date arriveTime;
    private Date finishTime;

    public DeliveryTaskListDto() {
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getLdOrderId() {
        return ldOrderId;
    }

    public void setLdOrderId(String ldOrderId) {
        this.ldOrderId = ldOrderId;
    }

    public String getLdOrderNo() {
        return ldOrderNo;
    }

    public void setLdOrderNo(String ldOrderNo) {
        this.ldOrderNo = ldOrderNo;
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

    public LoadingOrderEnum.DeliveryTaskStatus getStatus() {
        return status;
    }

    public void setStatus(LoadingOrderEnum.DeliveryTaskStatus status) {
        this.status = status;
    }

    public Date getStartLoadTime() {
        return startLoadTime;
    }

    public void setStartLoadTime(Date startLoadTime) {
        this.startLoadTime = startLoadTime;
    }

    public Date getEndLoadTime() {
        return endLoadTime;
    }

    public void setEndLoadTime(Date endLoadTime) {
        this.endLoadTime = endLoadTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getLoadedPackageQty() {
        return loadedPackageQty;
    }

    public void setLoadedPackageQty(Integer loadedPackageQty) {
        this.loadedPackageQty = loadedPackageQty;
    }

    public BigDecimal getLoadedVolume() {
        return loadedVolume;
    }

    public void setLoadedVolume(BigDecimal loadedVolume) {
        this.loadedVolume = loadedVolume;
    }

    public BigDecimal getLoadedWeight() {
        return loadedWeight;
    }

    public void setLoadedWeight(BigDecimal loadedWeight) {
        this.loadedWeight = loadedWeight;
    }
}
