package cn.xyy.tltms.app.data.model;

import java.io.Serializable;

public class CommonEnum implements Serializable {

    public enum HandoverType  {
        TO_DOOR("送货上门"),
        UPSTAIRS("送货上楼"),
        SELF("自提"),;

        private String text;

        HandoverType(String text) {
            this.text = text;
        }


        public String getText() {
            return this.text;
        }
    }

    public enum ExceptionStatus {
        NEW("新建"),
        PROCESSING("处理中"),
        CLOSE("关闭");

        ExceptionStatus(String text) {
            this.text = text;
        }

        private String text;


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public enum ExceptionCategory{
        DS("丢失"),
        THYW("提货延误"),
        PSYW("配送延误"),
        PS("破损"),
        CH("串货"),
        ZDSG("重大事故");

        private String text;

        ExceptionCategory(String text) {
            this.text = text;
        }


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public enum ReceiptStatus {
        NOT_BACK("未回"),
        SENDING("已寄"),
        HAS_BACK("已回");

        private String text;

        ReceiptStatus(String text) {
            this.text = text;
        }


        public String getText() {
            return this.text;
        }
    }

    public enum ReceiptUploadStatus {
        NOT_UPLOAD("未上传"),
        HAS_UPLOADED("已上传");

        private String text;

        ReceiptUploadStatus(String text) {
            this.text = text;
        }


        public String getText() {
            return this.text;
        }
    }

    public enum ReceiptApprovalStatus  {
        UNAUDITED("未审核"),
        AUDITED("审核通过"),
        REFUSE("审核不通过");

        private String text;

        ReceiptApprovalStatus(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }
    }

    //自动分单类型
    public enum TsOrderAssignType {
        SDAUTOMATIC("审单分单"),
        JDAUTOMATIC("截单分单");

        TsOrderAssignType(String text) {
            this.text = text;
        }

        private String text;


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    //配载单位
    public enum LoadUnit  {
        VOLUME("体积"),
        WEIGHT("重量"),
        PACKAGE("件数");

        LoadUnit(String text) {
            this.text = text;
        }

        private String text;


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    //计费单位
    public enum CalculateType  {
        VOLUME("体积"),
        WEIGHT("重量"),
        PACKAGE("件数"),
        CAR("整车"),
        VOLUME_DISTANCE("体积公里");

        CalculateType(String text) {
            this.text = text;
        }

        private String text;


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    //结算周期
    public enum SettleCycle  {
        MONTHLY("月结"),
        COLLECT("到付");

        SettleCycle(String text) {
            this.text = text;
        }

        private String text;


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    //支付方式
    public enum PaymentType  {
        TRANSFER("转账"),
        CASH("现金"),
        OILCARD("油卡"),
        check("支票");

        PaymentType(String text) {
            this.text = text;
        }

        private String text;


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    //运输方式
    public enum TransportType {
        GLZC("公路整车"),
        GLLD("公路零担"),
        KY("空运"),
        TL("铁路"),
        KD("快递");

        TransportType(String text) {
            this.text = text;
        }

        private String text;


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    //订单运单类型
    public enum OrderCategory {
        CARLOAD("整车"),
        LTL("零担"),
        THROUGHTRAFFIC("联运"),
        EXPRESSAGE("快递");

        OrderCategory(String text) {
            this.text = text;
        }

        private String text;


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public enum GpsDeviceType  {
        GD("固定式"),
        BX("便携式");

        private String text;

        GpsDeviceType(String text) {
            this.text = text;
        }


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public enum GpsDeviceStatus  {
        ZC("正常"),
        SH("损坏");
        private String text;

        GpsDeviceStatus(String text) {
            this.text = text;
        }


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    //租赁方式
    public enum VehicleLeaseType{
        OWN("自有"),
        CONTRACT("合同"),
        TEMPORARY("临时");

        VehicleLeaseType(String text) {
            this.text = text;
        }

        private String text;


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    //车厢类型
    public enum VehicleCategory  {
        DISCHARGING("自卸"),
        KITE("飞翼"),
        SLAB("平板"),
        CARTON("箱式"),
        POT("罐式"),
        FENCE("栏板式"),
        WAREHOUSE("仓栏式"),
        REFRIGERATE("冷藏"),
        WEI_BAN("尾板"),
        OTHER_SPECIAL("其他特殊");

        private String text;

        VehicleCategory(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    //核载吨位
    public enum VehicleTonnage  {
        TWO("2吨"),
        THREE("3吨"),
        FIVE("5吨"),
        EIGHT("8吨"),
        TEN("10吨"),
        TWENTY("20吨"),
        TWENTY_FIVE("25吨"),
        THIRTY_FIVE("35吨"),
        FORTY("40吨"),
        FORTY_FIVE("45吨");

        VehicleTonnage(String text) {
            this.text = text;
        }

        private String text;


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    //运输方式
    public enum VehicleTransportType {
        CARLOAD("整车"),
        FREIGHT("零担");

        VehicleTransportType(String text) {
            this.text = text;
        }

        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    //车况等级 优秀、良好、一般、较差
    public enum VehicleSituationGrade{
        EXCELLENT("优秀"),
        WELL("良好"),
        COMMON("一般"),
        POOR("较差");

        VehicleSituationGrade(String text) {
            this.text = text;
        }

        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    //信用等级
    public enum VehicleCreditGrade {
        EXCELLENT("优秀"),
        WELL("良好"),
        COMMON("一般"),
        POOR("较差");

        VehicleCreditGrade(String text) {
            this.text = text;
        }

        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }


        public String toString() {
            return this.name();
        }
    }

    //信用等级
    public enum BusinessType{
        UNIT("事业部"),
        GROUP("群组");

        BusinessType(String text) {
            this.text = text;
        }

        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }


        public String toString() {
            return this.name();
        }
    }
}
