package cn.xyy.tltms.app.data.model;

import java.io.Serializable;

/**
 * Created by dev on 2017/4/5.
 */

public class FeeAccountListDTO implements Serializable {
    private String code;
    private String name;
    private String accountDesc;
    private String superCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountDesc() {
        return accountDesc;
    }

    public void setAccountDesc(String accountDesc) {
        this.accountDesc = accountDesc;
    }

    public String getSuperCode() {
        return superCode;
    }

    public void setSuperCode(String superCode) {
        this.superCode = superCode;
    }
}
