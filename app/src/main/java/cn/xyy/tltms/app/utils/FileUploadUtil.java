package cn.xyy.tltms.app.utils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.multipart.HttpMultipartMode;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by dev on 2017/4/8.
 */

public class FileUploadUtil {

    public static void uploadFiles(List<String> paths, String url, RequestCallBack requestCallBack) {
        RequestParams params = new RequestParams();
        MultipartEntity mMulti = new MultipartEntity(HttpMultipartMode.STRICT,
                null, Charset.forName("UTF-8"));
        for(String path:paths){
            mMulti.addPart("file", new FileBody(new File(path)));
        }
        params.setBodyEntity(mMulti);
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.POST, url, params, requestCallBack);
    }
}
