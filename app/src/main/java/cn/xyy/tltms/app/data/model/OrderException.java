package cn.xyy.tltms.app.data.model;

/**
 * Created by admin on 2017/3/19.
 */

public class OrderException {
    private String value;
    private String name;

    public OrderException() {
    }

    public OrderException(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
