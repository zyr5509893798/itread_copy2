package com.example.itread_copy2.Util;

import android.util.Log;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    private SharedPreferencesUtil check;

    public static String attachHttpGetParams(String url, LinkedHashMap<String, String> params) {

        Iterator<String> keys = params.keySet().iterator();
        Iterator<String> values = params.values().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("?");

        for (int i = 0; i < params.size(); i++) {
            String value = null;
            try {
                value = URLEncoder.encode(values.next(), "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            stringBuffer.append(keys.next() + "=" + value);
            if (i != params.size() - 1) {
                stringBuffer.append("&");
            }
        }

        return url + stringBuffer.toString();
    }

    //登录
    public static void loginWithOkHttp(String address, String account, String password, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username",account)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
    //注册
    public static void registerWithOkHttp(String address, String account, String email, String password, String repassword, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("username", account)
                .add("email", email)
                .add("password", password)
                .add("repassword", repassword)
                .build();
        //String cookie=CliniciansApplication.getOkhttpCookie();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
//        client.newCall(request).enqueue(callback);
        Call call = client.newCall(request);
        //5.请求加入调度,重写回调方法
        call.enqueue(callback);
    }

    //修改密码post请求
    public static void ChangePasswordWithOkHttp(String address, String old_password, String new_password, String renew_password, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("old_password", old_password)
                .add("new_password", new_password)
                .add("re_password", renew_password)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .post(body)
                .build();
//        client.newCall(request).enqueue(callback);
        Call call = client.newCall(request);
        //5.请求加入调度,重写回调方法
        call.enqueue(callback);
    }

   //反登录get
    public static void loginOffOkHttp(String address, String account, String password, okhttp3.Callback callback){
        Request request;
        OkHttpClient client = new OkHttpClient();
        if (SharedPreferencesUtil.isCookie()){
            Log.i("zyr", "true，成功");
            request = new Request.Builder()
                    .url(address)
                    .header("Cookie",SharedPreferencesUtil.getCookie())
//                .post(body)
                    .build();
            Log.i("zyr","getCookie的值="+SharedPreferencesUtil.getCookie());
        } else {
            request = new Request.Builder()
                    .url(address)
                    .build();
        }
//        request = new Request.Builder()
//                .url(address)
//                .build();
//        RequestBody body = new FormBody.Builder()
//                .add("username",account)
//                .add("password",password)
//                .build();

        client.newCall(request).enqueue(callback);
    }

    //home页get头像昵称
    public static void homeNameOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .build();
        client.newCall(request).enqueue(callback);
    }


    //post传照片文件
    public static void PassPhotoWithOkHttp(String address, File file, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType MEDIATYPE = MediaType.parse("image/jpeg; charset=utf-8");
        RequestBody body = RequestBody.create(MEDIATYPE, file);
        Request request = new Request.Builder()
                .url(address)
                .post(RequestBody.create(MEDIATYPE,file))
                .build();
        client.newCall(request).enqueue(callback);
    }

    private static final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                    cookieStore.put(httpUrl.host(), list);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                    List<Cookie> cookies = cookieStore.get(httpUrl.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            })
            .build();

}
