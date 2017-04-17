package cn.xyy.tltms.app.data.model;

import java.io.Serializable;


public class TransportationOrderSummaryDTO implements Serializable{
    private String tsOrderId;                          //运单ID
    private Integer totalItemQty;                      //总数量
    private Integer totalPackageQty;                   //总件数
    private double totalVolume;                        //总体积
    private double totalWeight;                        //总重量
    private double totalGoodsWorth;                    //总货值


    public String getTsOrderId() {
        return tsOrderId;
    }

    public void setTsOrderId(String tsOrderId) {
        this.tsOrderId = tsOrderId;
    }

    public Integer getTotalItemQty() {
        return totalItemQty;
    }

    public void setTotalItemQty(Integer totalItemQty) {
        this.totalItemQty = totalItemQty;
    }

    public Integer getTotalPackageQty() {
        return totalPackageQty;
    }

    public void setTotalPackageQty(Integer totalPackageQty) {
        this.totalPackageQty = totalPackageQty;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public double getTotalGoodsWorth() {
        return totalGoodsWorth;
    }

    public void setTotalGoodsWorth(double totalGoodsWorth) {
        this.totalGoodsWorth = totalGoodsWorth;
    }
}
