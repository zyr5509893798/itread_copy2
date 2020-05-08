package com.example.itread_copy2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.itread_copy2.Adapter.MyBookCommentsAdapter;
import com.example.itread_copy2.Adapter.MyShortCommentsAdapter;
import com.example.itread_copy2.Util.HttpUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

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

public class MyShortCommentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Map map;
    private RelativeLayout myshort_back;
    int list_begin = 0;
    private RefreshLayout refreshLayout;
    private MyShortCommentsAdapter mAdapter;


    List<Map<String, Object>> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_short_comments);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MyShortCommentsActivity.this, "点击了",Toast.LENGTH_SHORT).show();
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();  refreshlayout.finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据
//            }
//        });
        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshFooter(new ClassicsFooter(this).setFinishDuration(10));
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                myshortCommentLoadMoreWithOkHttp("http://47.102.46.161/user/comment_request");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView = findViewById(R.id. myshort_comment_recyclerview);
        myshort_back = findViewById(R.id.myshort_back);
        list_begin = 0;

        Toast.makeText(MyShortCommentsActivity.this, "list_bigin:"+String.valueOf(list_begin), Toast.LENGTH_SHORT).show();

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        mAdapter = new MyShortCommentsAdapter(MyShortCommentsActivity.this);
        recyclerView.setAdapter(mAdapter);


        myshort_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myshortCommentWithOkHttp("http://47.102.46.161/user/comment_request");

        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshFooter(new ClassicsFooter(this).setFinishDuration(10));
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                myshortCommentLoadMoreWithOkHttp("http://47.102.46.161/user/comment_request");
                Toast.makeText(MyShortCommentsActivity.this, "list_bigin loadmore:"+String.valueOf(list_begin), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void myshortCommentWithOkHttp(String address){
        HttpUtil.mybookCommentWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Log.i( "zyr", " mybookComment : error");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                Log.i("zyr","ShortCommentsResponseData:"+responseData);
                try{
                    Log.i("zyr", "shortcomment:list.size:"+list.size());
                    list.clear();
                    JSONObject object = new JSONObject(responseData);
                    JSONArray jsonArray = object.getJSONArray("short");
                    int this_end = 0;
                    for (int i = jsonArray.length() - 1 - list_begin; i >= 0; i--) {
                        this_end++;

                        if (this_end==10){
                            break;
                        }
                        list_begin++;
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                int news_id = jsonObject1.getInt("news_id");
//                        String status = jsonObject1.getString("status");
                        String content = jsonObject1.getString("content");  //头像
                        String score = jsonObject1.getString("score");
                        String time = jsonObject1.getString("create_time");
                        String book_num = jsonObject1.getString("num");
                        String book_name = jsonObject1.getString("title");
                        String book_photo = jsonObject1.getString("image");
                        String comment_id = jsonObject1.getString("id");
                        map = new HashMap();

//                        map.put("status", status);
                        map.put("content", content);
                        map.put("score", score);
                        map.put("time", time);
                        map.put("book_name", book_name);
                        map.put("book_num", book_num);
                        map.put("book_photo", book_photo);
                        map.put("comment_id", comment_id);

                        list.add(map);
                        Log.i("zyr", "shortcomment:list.size1111:"+list.size());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.i("zyr", "shortcomment:list.size:"+list.size());
//                            recyclerView.setLayoutManager(new LinearLayoutManager(MyShortCommentsActivity.this));//纵向
////                            recyclerView.setAdapter(new MyShortCommentsAdapter(MyShortCommentsActivity.this, list));
                            mAdapter.setData(list);
                            mAdapter.notifyDataSetChanged();
//                            recyclerView.setNestedScrollingEnabled(false);
                        }
                    });
                    refreshLayout.finishLoadMore();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i( "zyr", "LLLShortCommenta:"+responseData);
                    refreshLayout.finishLoadMore(false);
                }
            }//标签页
        });
    }

    public void myshortCommentLoadMoreWithOkHttp(String address){
        HttpUtil.mybookCommentWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Log.i( "zyr", " mybookComment : error");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                Log.i("zyr","ShortCommentsResponseData:"+responseData);
                try{
                    Log.i("zyr", "shortcomment:list.size:"+list.size());
                    JSONObject object = new JSONObject(responseData);
                    JSONArray jsonArray = object.getJSONArray("short");
                    int this_end = 0;
                    for (int i = jsonArray.length() - 1 - list_begin; i >= 0; i--) {
                        this_end++;
                        list_begin++;
                        if (this_end==10){
                            list_begin = list_begin - 1;
                            break;
                        }

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                int news_id = jsonObject1.getInt("news_id");
//                        String status = jsonObject1.getString("status");
                        String content = jsonObject1.getString("content");  //头像
                        String score = jsonObject1.getString("score");
                        String time = jsonObject1.getString("create_time");
                        String book_num = jsonObject1.getString("num");
                        String book_name = jsonObject1.getString("title");
                        String book_photo = jsonObject1.getString("image");
                        String comment_id = jsonObject1.getString("id");
                        map = new HashMap();

//                        map.put("status", status);
                        map.put("content", content);
                        map.put("score", score);
                        map.put("time", time);
                        map.put("book_name", book_name);
                        map.put("book_num", book_num);
                        map.put("book_photo", book_photo);
                        map.put("comment_id", comment_id);

                        list.add(map);
                        Log.i("zyr", "shortcomment:list.size1111:"+list.size());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.i("zyr", "shortcomment:list.size:"+list.size());
//                            recyclerView.setLayoutManager(new LinearLayoutManager(MyShortCommentsActivity.this));//纵向
//                            recyclerView.setAdapter(new MyShortCommentsAdapter(MyShortCommentsActivity.this, list));
                            mAdapter.setData(list);
                            mAdapter.notifyDataSetChanged();
//                            recyclerView.setNestedScrollingEnabled(false);
                        }
                    });
                    if (jsonArray.length() == list_begin){
                        refreshLayout.finishLoadMoreWithNoMoreData();
                        list_begin = 0;
                    }else {
                        refreshLayout.finishLoadMore();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i( "zyr", "LLLShortCommenta:"+responseData);
                    refreshLayout.finishLoadMore(false);
                }
            }//标签页
        });
    }


}
