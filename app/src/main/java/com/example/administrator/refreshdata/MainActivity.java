package com.example.administrator.refreshdata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /*声明控件*/
    private Button btn_refresh_listView_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化控件
        btn_refresh_listView_data = (Button) findViewById(R.id.btn_refresh_listView_data);
        // 给控件添加点击监听事件
        btn_refresh_listView_data.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_refresh_listView_data:
                startActivity(new Intent(MainActivity.this, RefreshListViewDataActivity.class));
                break;
            default:
                break;
        }
    }
}
