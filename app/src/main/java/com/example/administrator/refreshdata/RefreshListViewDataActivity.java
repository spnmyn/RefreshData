package com.example.administrator.refreshdata;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RefreshListViewDataActivity extends Activity implements AbsListView.OnScrollListener {
    /*声明控件*/
    ListView listView;
    /*创建ListView页脚布局*/
    LinearLayout loadingLayout;
    /*创建线程对象*/
    private Thread thread;
    /*创建适配器对象*/
    private ListViewAdapter adapter = new ListViewAdapter();

    /**
     * 设置布局显示属性
     */
    private LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    /**
     * 设置布局显示最大化属性
     */
    private LinearLayout.LayoutParams FLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.FILL_PARENT,
            LinearLayout.LayoutParams.FILL_PARENT);

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_list_view_data);
        // 布局、数据并显示
        init();
    }

    /*
    * 布局、数据并显示
    * */
    private void init() {
        // 创建线性布局
        LinearLayout linearLayout = new LinearLayout(this);
        // 设置水平方向
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        // 创建进度条
        ProgressBar progressBar = new ProgressBar(this);
        // 进度条显示位置
        progressBar.setPadding(0, 0, 15, 0);
        // 把进度条加入linearLayout中
        linearLayout.addView(progressBar, mLayoutParams);
        // 创建文本
        TextView textView = new TextView(this);
        textView.setText("加载中...");
        textView.setGravity(Gravity.CENTER_VERTICAL);
        // 把文本加入linearLayout中
        linearLayout.addView(textView, FLayoutParams);
        // 设置linearLayout重力方向
        linearLayout.setGravity(Gravity.CENTER);

        // 设置ListView页脚Layout
        loadingLayout = new LinearLayout(this);
        loadingLayout.addView(linearLayout, mLayoutParams);
        loadingLayout.setGravity(Gravity.CENTER);

        // 初始化控件
        listView = findViewById(R.id.listView);
        /*
        * 添加脚页显示
        * 注意：不能为listView.addFooterView(linearLayout);
        * */
        listView.addFooterView(loadingLayout);
        // ListView与适配器关联
        listView.setAdapter(adapter);
        // 给ListView添加滚动监听事件
        listView.setOnScrollListener(this);
    }

    /*
    * 适配数据
    * */
    class ListViewAdapter extends BaseAdapter {
        int count = 10;

        public int getCount() {
            return count;
        }

        public Object getItem(int pos) {
            return pos;
        }

        public long getItemId(int pos) {
            return pos;
        }

        public View getView(int pos, View v, ViewGroup p) {
            TextView textView;
            if (v == null) {
                textView = new TextView(RefreshListViewDataActivity.this);
            } else {
                textView = (TextView) v;
            }
            textView.setText("ListItem " + pos);
            textView.setHeight(60);
            textView.setTextSize(20f);
            textView.setGravity(Gravity.CENTER);
            return textView;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            // 开启线程下载数据
            if (thread == null || !thread.isAlive()) {
                thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            // 这里放网络数据请求方法，我在这里用线程休眠5秒方法来处理
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                };
                thread.start();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 1:
                    if (adapter.count <= 40) {
                        adapter.count += 10;
                        int currentPage = adapter.count / 10;
                        Toast.makeText(getApplicationContext(), "第" + currentPage + "页", Toast.LENGTH_SHORT).show();
                    } else {
                        listView.removeFooterView(loadingLayout);
                        Toast.makeText(RefreshListViewDataActivity.this, "停止刷新", Toast.LENGTH_SHORT).show();
                    }
                    // 刷新适配器
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
}
