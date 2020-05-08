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
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    private SharedPreferencesUtil check;
    public static final MediaType FORM_CONTENT_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

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
        RequestBody body = new FormBody.Builder()
                .add("username", account)
                .add("email", email)
                .add("password", password)
                .add("name","读书")
                .build();
        //String cookie=CliniciansApplication.getOkhttpCookie();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
//        client.newCall(request).enqueue(callback);
        client.newCall(request).enqueue(callback);
    }

    //修改密码post请求
    public static void ChangePasswordWithOkHttp(String address, String old_password, String new_password, String renew_password, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("old_password", old_password)
                .add("new_password", new_password)
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
    public static void iconWithOkHttp(String address, String file, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
//        MediaType MEDIATYPE = MediaType.parse("image/jpeg; charset=utf-8");multipart/form-data
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "icon",
                        RequestBody.create(MediaType.parse("image/jpeg; charset=utf-8"), new File(file)))
                .build();

        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //post传email验证码
    public static void emailWithOkHttp(String address, String email_num, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("code",email_num)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //修改昵称
    public static void nicknameWithOkHttp(String address, String new_nickname, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("name",new_nickname)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //退出登录
    public static void signoutWithOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .build();
        client.newCall(request).enqueue(callback);
    }

    //忘记密码
    public static void findWithOkHttp(String address, String account, String email, String password, String repassword, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username", account)
                .add("email", email)
                .add("new_password", password)
                .add("re_password", repassword)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //我的书评get
    public static void mybookCommentWithOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .build();
        client.newCall(request).enqueue(callback);
    }

    //我的短评get
    public static void myshortCommentWithOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void file_submit(String url , String filepath,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        File file = new File(filepath);
        Log.i("zyr",filepath);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        //请求体
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
//                .addPart(Headers.of(
//                        "Content-Disposition",
//                        "form-data; name=\"filename\""),
//                        RequestBody.create(null, "lzr"))//这里是携带上传的其他数据
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"mFile\"; filename=\"" + "yyy.jpeg" + "\""), fileBody)
                .build();
        //请求的地址
        Request request = new Request.Builder()
                .url(url)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //上传头像获得url   POST
    public static void userIconWithOkHttp(String address,File file, okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file))
                .setType(MultipartBody.FORM)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .post(multipartBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    //个人中心想读已读在读get
    public static void WantReadWithOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .build();
        client.newCall(request).enqueue(callback);
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

    //退出登录
    public static void getAdapterWithOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .build();
        client.newCall(request).enqueue(callback);
    }

    //知乎日报
    public static void newsPaperWithOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .build();
        client.newCall(request).enqueue(callback);
    }

    //删除我的评论
    public static void delMyCommentWithOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .build();
        client.newCall(request).enqueue(callback);
    }

    //设置想读
    public static void changeStatusWithOkHttp(String address, String status, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("status",status)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
