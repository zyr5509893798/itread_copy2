package com.example.itread_copy2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.itread_copy2.Util.HttpUtil;
import com.example.itread_copy2.Util.SharedPreferencesUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText login_username;
    private EditText login_password;
    private Button login_loginbtn;
    private SharedPreferencesUtil check;
    private String result;
    private String username;
    private String result_password = "0";
    private String demo = "0";
    private Map map;
    private String code;

    List<Map<String, Object>> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        check = SharedPreferencesUtil.getInstance(getApplicationContext());
        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        login_loginbtn = findViewById(R.id.login_loginbtn);

//        login_loginbtn.setOnClickListener((View.OnClickListener) this);
        login_loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                String loginAddress="http://www.tooltool.club/vip/demo?username=123456&password=123456";
//                String loginAddress="http://www.njggpt.com/image.jsp?t=";
//                String loginAddress="http://www.njggpt.com/dologin.action?pwd=123456&dhhm=18652830666&sign=2065";
                //49.233.166.246/user/login
                String loginAddress="http://49.233.166.246/user/login";
                String loginAccount = login_username.getText().toString();
                String loginPassword = login_password.getText().toString();
                if (TextUtils.isEmpty(loginAccount)){
                    Toast.makeText(LoginActivity.this,"请输入用户名", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(loginPassword)){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }else {
                    loginWithOkHttp(loginAddress, loginAccount, loginPassword);
                }
            }
        });

        //防空格回车
        setEditTextInputSpace(login_username);
        setEditTextInputSpace(login_password);

        //控制最大长度
        int maxLengthUserName =16;
        int maxLengthPassword = 18;
        InputFilter[] fArray =new InputFilter[1];
        fArray[0]=new  InputFilter.LengthFilter(maxLengthUserName);
        login_username.setFilters(fArray);
        InputFilter[] fArray1 =new InputFilter[1];
        fArray1[0]=new  InputFilter.LengthFilter(maxLengthPassword);
        login_password.setFilters(fArray1);

    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.login_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }


    //实现登录
    public void loginWithOkHttp(String address, final String account, final String password){
        HttpUtil.loginWithOkHttp(address,account,password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Log.i( "zyr", " name : error");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                String header = response.header("set-cookie");
                String JSESSIONID=header.substring(0, 43);
                Log.i("zyr","0");
                Log.i("zyr","login_jsessionid:"+JSESSIONID);
                check.setCookie(true);//设置已获得cookie
                check.saveCookie(JSESSIONID);//保存获得的cookie
                try{
                    JSONObject object = new JSONObject(responseData);
                    result = object.getString("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i( "zyr", "LLL"+responseData);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("登陆成功")){
                            check.setLogin(true);  //设置登录状态为已登录
                            check.setAccountId(account);  //添加账户信息
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }//标签页
        });
    }

    //实现反登录
    public void loginOffOkHttp(String address, final String account, final String password) {
        HttpUtil.loginOffOkHttp(address, account, password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Log.i("zyr", " name : off_error");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                String header = response.header("set-cookie");
                Log.i("zyr","login_responseData:"+responseData);
                Log.i("zyr", "login_header:"+header);
                String JSESSIONID=header.substring(0, 43);//非常重要，获得到从0到第43位字符
                Log.i("zyr","login_jsessionid:"+JSESSIONID);
                check.setCookie(true);//设置已获得cookie
                check.saveCookie(JSESSIONID);//保存获得的cookie
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("zyr", "LLL" + responseData);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        if (result.equals("表单提交成功")) {
//                            int i = list.size();
//                            Log.i("zyr", " i=" + i);
////                            result_password = list.get(0).get("title").toString();
//                            //Toast.makeText(LoginActivity.this,"登录成功"+"result="+result+"username="+username+"password="+result_password+"demo"+demo,Toast.LENGTH_LONG).show();
//                            check.setLogin(true);  //设置登录状态为已登录
//                            check.setAccountId(account);  //添加账户信息
                        Toast.makeText(LoginActivity.this,"code = "+code,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, NewBookActivity.class);
                            startActivity(intent);
//                        } else {
//                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
            }//标签页
        });
    }

    //同步get请求
    private void getSync() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request1;
                Request request2;
                try {
                    request1=new Request.Builder()  //请求对象，设置的参数详细要看源码解释
                            .url("http://www.njggpt.com/dologin.action?pwd=123456&dhhm=18652830666&sign=2065")    //这里的url参数值是自己访问的api
                            .build();
                    Response response = null;   //建立一个响应对象，一开始置为null
                    Call call = client.newCall(request1); //开始申请，发送网络请求。
                    response = call.execute();
                    if (response.isSuccessful()) {
                        showResponseIconNickname(response.toString());
                    } else {
                        Log.d("Fail","get请求失败");
                    }//下一个url
                    request2=new Request.Builder()  //请求对象，设置的参数详细要看源码解释
                            .url("http://www.njggpt.com/dologin.action?pwd=123456&dhhm=18652830666&sign=2065")    //这里的url参数值是自己访问的api
                            .build();
                    Response response2 = null;   //建立一个响应对象，一开始置为null
                    Call call2 = client.newCall(request2); //开始申请，发送网络请求。
                    response2 = call2.execute();
                    if (response2.isSuccessful()) {
                        showResponseIconNickname(response2.toString());
                    } else {
                        Log.d("Fail","get请求失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //get获取头像，昵称
    public void showResponseIconNickname(final String string) {

        try {
            JSONObject jsonObject = new JSONObject(string);
//            JSONObject jsonObject1 = jsonObject.getJSONObject("user");
//            home_icon_url = jsonObject1.getString("nickname");
//            //String home_icon_url = jsonObject.getString("title");
//            final String home_nickname_string = jsonObject1.getString("icon");
//            //String home_nickname_string = jsonObject.getString("hint");
            Log.i("zyr","map");
           String home_icon_url = "https://pic2.zhimg.com/v2-1cb35baecebc516b4481334eac4c2e65.jpg";
            final String home_nickname_string = "拉拉啊了";
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //防止空格回车
    public static void setEditTextInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }
}
