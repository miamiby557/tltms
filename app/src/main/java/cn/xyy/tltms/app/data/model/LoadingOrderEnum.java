package cn.xyy.tltms.app.data.model;


public class LoadingOrderEnum {

    public enum DeliveryTaskStatus {
        WAITING("待装车"),
        LOADING("装车中"),
        LOADED("已装车"),
        INTRANSIT("在途"),
        PLAN_ARRIVE("预到达"),
        ARRIVE("到达"),
        FINISH("完成");

        private String text;

        DeliveryTaskStatus(String text) {
            this.text = text;
        }


        public String getText() {
            return text;
        }
    }

    public enum OrderStatus {
        NEW("新建"),
        ASSIGNED("已派单"),
        ACCEPTED("已接收"),
        LOADING("装车中"),
        LOADED("装车完成"),
        INTRANSIT("已发车"),
        ARRIVE("到达"),
        FINISH("完成"),
        CANCEL("取消");

        private String text;

        OrderStatus(String text) {
            this.text = text;
        }


        public String getText() {
            return text;
        }
    }
}
