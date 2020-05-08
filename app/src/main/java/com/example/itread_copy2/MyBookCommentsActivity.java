package com.example.itread_copy2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.itread_copy2.Adapter.MyBookCommentsAdapter;
import com.example.itread_copy2.Util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyBookCommentsActivity extends AppCompatActivity {



    private RecyclerView recyclerView;
    private Map map;
    private RelativeLayout mybook_back;

    List<Map<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_book_comments);
    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView = findViewById(R.id. mybook_comment_recyclerview);
        mybook_back = findViewById(R.id.mybook_back);

        mybook_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        mybookCommentWithOkHttp("http://47.102.46.161/user/comment_request");
        mybookCommentWithOkHttp("http://news-at.zhihu.com/api/3/stories/latest");
    }

    public void mybookCommentWithOkHttp(String address){
//        HttpUtil.mybookCommentWithOkHttp(address, new Callback() {
        HttpUtil.newsPaperWithOkHttp(address, new Callback() {

                @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Log.i( "zyr", " mybookComment : error");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                try{
                    list.clear();
                    JSONObject object = new JSONObject(responseData);
//                    JSONArray jsonArray = object.getJSONArray("long");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
////                int news_id = jsonObject1.getInt("news_id");
////                        String status = jsonObject1.getString("status");
//                        String content = jsonObject1.getString("content");  //头像
//                        String score = jsonObject1.getString("score");
//                        String time = jsonObject1.getString("create_time");
//                        String book_num = jsonObject1.getString("num");
//                        String book_name = jsonObject1.getString("title");
//                        String book_photo = jsonObject1.getString("image");
//                        String comment_title  = jsonObject1.getString("long_title");
//                        map = new HashMap();
//
////                        map.put("status", status);
//                        map.put("content", content);
//                        map.put("score", score);
//                        map.put("time", time);
//                        map.put("book_name", book_name);
//                        map.put("book_num", book_num);
//                        map.put("book_photo", book_photo);
//                        map.put("comment_title", comment_title);
//
//                        list.add(map);

                    JSONArray jsonArray = object.getJSONArray("stories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                int news_id = jsonObject1.getInt("news_id");
                        String url = jsonObject1.getString("url");
                        JSONArray jsonArray3 = jsonObject1.getJSONArray("images");
                        String images = jsonArray3.get(0).toString();
//                String images = jsonObject1.getString("images");
                        String title = jsonObject1.getString("title");
                        String hint = jsonObject1.getString("hint");
                        String news_id = jsonObject1.getString("id");


                        map = new HashMap();

//                map.put("news_id",news_id);
                        map.put("url", url);
                        map.put("images", images);
                        map.put("title", title);
                        map.put("hint", hint);
                        map.put("id", news_id);
                        map.put("cheak", "1");
                        map.put("item", "0");

                        list.add(map);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                recyclerView.setLayoutManager(new LinearLayoutManager(MyBookCommentsActivity.this));//纵向
                                recyclerView.setAdapter(new MyBookCommentsAdapter(MyBookCommentsActivity.this, list));
                                recyclerView.setNestedScrollingEnabled(false);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i( "zyr", "LLL"+responseData);
                }
            }//标签页
        });
    }

    //新建Handler对象。
    @SuppressLint("HandlerLeak")
    final
    Handler mHandler = new Handler() {

        //handleMessage为处理消息的方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //                tv.setText(msg.arg1 + "");
            switch (msg.what){
                case 1:
//                    MyBookCommentsAdapter.notifyDataSetChanged();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
            if (true) {
            }
        }
    };

}
