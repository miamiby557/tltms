package cn.xyy.tltms.app.data.model;

import java.util.Date;

/**
 * Created by admin on 2017/3/18.
 */

public class SysUser {
    private String userId;
    private String appUserId;
    private String siteId;
    private String username;
    private String password;
    private String fullName;
    private String empNo;
    private String email;
    private String remark;
    private boolean activeStatus;
    private String lastLoginIp;
    private Date lastLoginTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
