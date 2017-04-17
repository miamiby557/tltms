package cn.xyy.tltms.app.data.model;

import java.io.Serializable;

/**
 * Created by dev on 2017/4/13.
 */

public class ProjectListDTO implements Serializable {
    private String projectId;                   //项目ID
    private String appUserId;                   //app用户ID
    private String code;                        //项目编码
    private String name;                        //项目名称
    private String namePinyinAll;               //名称拼音全拼
    private String namePinyinHead;              //名称拼音头子母
    private String contractNo;                  //合同号

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

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

    public String getNamePinyinAll() {
        return namePinyinAll;
    }

    public void setNamePinyinAll(String namePinyinAll) {
        this.namePinyinAll = namePinyinAll;
    }

    public String getNamePinyinHead() {
        return namePinyinHead;
    }

    public void setNamePinyinHead(String namePinyinHead) {
        this.namePinyinHead = namePinyinHead;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
}
