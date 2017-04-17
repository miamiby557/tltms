package cn.xyy.tltms.app.data.remote;

import java.io.Serializable;

/**
 * Created by admin on 2017/3/18.
 */

public class Result<T> implements Serializable {
    private boolean success = true;
    private T data;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
            return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
