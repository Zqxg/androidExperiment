package com.example.daily_new.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daily_new.adapter.ScheduleAdapter;
import com.example.daily_new.R;
import com.example.daily_new.dao.ScheduleDAO;
import com.example.daily_new.model.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment {

    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;
    private List<Schedule> scheduleList = new ArrayList<>();
    private FloatingActionButton addButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_schedule, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        addButton = view.findViewById(R.id.addButton);

        // 初始化 DAO 和 RecyclerView
        ScheduleDAO scheduleDAO = new ScheduleDAO(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 从数据库中获取日程列表
        scheduleList = scheduleDAO.getAllSchedules();

        // 检查数据库是否有数据
        if (scheduleList == null || scheduleList.isEmpty()) {
            // 如果没有数据，使用模拟数据
            loadSchedules();
            Toast.makeText(requireContext(), "没有日程，加载模拟数据", Toast.LENGTH_SHORT).show();
        } else {
            // 如果有数据，使用数据库数据
            scheduleAdapter = new ScheduleAdapter(scheduleList, requireContext());
            recyclerView.setAdapter(scheduleAdapter);
        }

        // 点击“+”按钮跳转到添加日程页面
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddScheduleActivity.class);
            startActivity(intent);
        });

        // 设置 RecyclerView 的点击事件处理
        if (scheduleAdapter != null) {
            scheduleAdapter.setOnItemClickListener(position -> {
                Schedule schedule = scheduleList.get(position);
                Intent intent = new Intent(getActivity(), EditScheduleActivity.class);
                intent.putExtra("scheduleId", schedule.getId()); // 传递日程ID
                startActivityForResult(intent, 1); // 请求码1
            });
        }

        return view;
    }

    // 模拟加载日程数据
    private void loadSchedules() {
        scheduleList.add(new Schedule(1, "模拟标题", "模拟内容", "2024-11-25", "09:00"));
        scheduleList.add(new Schedule(2, "示例日程", "示例内容", "2024-11-26", "14:00"));

        // 设置适配器
        scheduleAdapter = new ScheduleAdapter(scheduleList, requireContext());
        recyclerView.setAdapter(scheduleAdapter);
    }
}
