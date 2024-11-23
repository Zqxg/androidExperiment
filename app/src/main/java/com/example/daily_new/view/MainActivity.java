package com.example.daily_new.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daily_new.adapter.ScheduleAdapter;
import com.example.daily_new.R;
import com.example.daily_new.dao.ScheduleDAO;
import com.example.daily_new.model.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;
    private List<Schedule> scheduleList = new ArrayList<>();
    private FloatingActionButton addButton;
    private ScheduleDAO scheduleDAO;
    private Button btnCheckRetirement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        btnCheckRetirement = findViewById(R.id.btn_check_retirement);

        // 初始化 DAO 和 RecyclerView
        ScheduleDAO scheduleDAO = new ScheduleDAO(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 从数据库中获取日程列表
        scheduleList = scheduleDAO.getAllSchedules();

        // 检查数据库是否有数据
        if (scheduleList == null || scheduleList.isEmpty()) {
            // 如果没有数据，使用模拟数据
            loadSchedules();
            Toast.makeText(this, "没有日程，加载模拟数据", Toast.LENGTH_SHORT).show();
        } else {
            // 如果有数据，使用数据库数据
            scheduleAdapter = new ScheduleAdapter(scheduleList, this);
            recyclerView.setAdapter(scheduleAdapter);
        }

        addButton = findViewById(R.id.addButton);  // 获取“+”按钮

        // 点击“+”按钮跳转到添加日程页面
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddScheduleActivity.class);
            startActivity(intent);
        });

        // 设置 RecyclerView 的点击事件处理
        scheduleAdapter.setOnItemClickListener((position) -> {
            Schedule schedule = scheduleList.get(position);
            Intent intent = new Intent(MainActivity.this, EditScheduleActivity.class);
            intent.putExtra("scheduleId", schedule.getId());  // 传递日程ID
            startActivityForResult(intent, 1);  // 请求码1
        });

        // 设置“查询退休年龄”按钮的点击事件
        btnCheckRetirement.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RetirementActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            // 获取删除标志或更新后的日程
            boolean isDeleted = data.getBooleanExtra("isDeleted", false);
            Schedule updatedSchedule = (Schedule) data.getSerializableExtra("updatedSchedule");
            int deletedId = data.getIntExtra("deletedId", -1);  // 获取被删除的 ID

            if (isDeleted && deletedId != -1) {
                // 从列表中移除被删除的日程
                for (int i = 0; i < scheduleList.size(); i++) {
                    if (scheduleList.get(i).getId() == deletedId) {
                        scheduleList.remove(i);  // 移除对应项
                        break;
                    }
                }
                // 刷新 RecyclerView
                scheduleAdapter.notifyDataSetChanged();
                Toast.makeText(this, "日程已删除", Toast.LENGTH_SHORT).show();
            } else if (updatedSchedule != null) {
                // 如果是更新操作，更新日程
                for (int i = 0; i < scheduleList.size(); i++) {
                    if (scheduleList.get(i).getId() == updatedSchedule.getId()) {
                        scheduleList.set(i, updatedSchedule);  // 更新日程
                        break;
                    }
                }
                // 刷新 RecyclerView
                scheduleAdapter.notifyDataSetChanged();
            }
        }
    }


    // 模拟加载日程数据
    private void loadSchedules() {
        // 示例数据
        scheduleList.add(new Schedule(1, "模拟标题", "模拟内容", "2024-11-25", "09:00"));
        scheduleList.add(new Schedule(2, "示例日程", "示例内容", "2024-11-26", "14:00"));

        // 设置适配器
        scheduleAdapter = new ScheduleAdapter(scheduleList, this);
        recyclerView.setAdapter(scheduleAdapter);
    }
}
