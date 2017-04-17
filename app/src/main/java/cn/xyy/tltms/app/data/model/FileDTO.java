package cn.xyy.tltms.app.data.model;

import java.io.Serializable;

/**
 * Created by dev on 2017/4/7.
 */

public class FileDTO implements Serializable {
    private String version;
    private boolean mustUpdate;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isMustUpdate() {
        return mustUpdate;
    }

    public void setMustUpdate(boolean mustUpdate) {
        this.mustUpdate = mustUpdate;
    }
}
