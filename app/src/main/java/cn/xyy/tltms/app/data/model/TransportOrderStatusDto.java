package cn.xyy.tltms.app.data.model;

import java.util.Date;

/**
 * Created by dev on 2017/3/30.
 */

public class TransportOrderStatusDto {
    private Date date;
    private String status;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
