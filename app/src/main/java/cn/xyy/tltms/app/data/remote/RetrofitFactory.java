package cn.xyy.tltms.app.data.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.xyy.tltms.app.tltms.TLApplication;
import cn.xyy.tltms.app.utils.Constant;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 2017/3/18.
 */

public class RetrofitFactory {

    //public static String baseUrl="http://simmy.f3322.net:9027/";
    //public static String baseUrl="http://192.168.1.95:9027/";

    public static String baseUrl= TLApplication.getInstance().SERVER;

    public static ApiService getApiService() {
        return ServiceFactory.apiService;
    }

    public static void reSetService(){
        ServiceFactory.apiService = getRetrofit().create(ApiService.class);
    }

    public static Retrofit getRetrofit() {
        return ServiceFactory.retrofit;
    }

    public static void reSetRetrofit() {
        ServiceFactory.setRetrofit();
    }


    private static class ServiceFactory {

        private static Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(Constant.OKHTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)//超时 60秒
                .readTimeout(Constant.OKHTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constant.OKHTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        String lglt = TLApplication.getInstance().getLgLt();
                        String[] arr = lglt.split("-");
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .addHeader("app_version",TLApplication.getInstance().getVersion())
                                .addHeader("latitude",arr[1]) //lg 经度 lt 纬度
                                .addHeader("longitude",arr[0]) //lg 经度 lt 纬度
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();
        public static void setRetrofit(){
            Log.e("baseUrl","b:"+baseUrl);
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
            apiService = retrofit.create(ApiService.class);
        }

        private static Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        private static ApiService apiService = retrofit.create(ApiService.class);

    }
}
