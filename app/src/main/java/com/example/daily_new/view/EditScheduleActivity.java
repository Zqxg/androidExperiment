package com.example.daily_new.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daily_new.R;
import com.example.daily_new.model.Schedule;
import com.example.daily_new.dao.ScheduleDAO;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditScheduleActivity extends AppCompatActivity {

    private EditText titleEditText, contentEditText;
    private Button setDateButton, setTimeButton, saveButton, deleteButton;
    private ScheduleDAO scheduleDAO;
    private Schedule schedule;
    private int scheduleId; // 当前编辑的日程ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        setDateButton = findViewById(R.id.setDateButton);
        setTimeButton = findViewById(R.id.setTimeButton);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);

        scheduleDAO = new ScheduleDAO(this);
        Calendar selectedDateTime = Calendar.getInstance();

        // 设置删除按钮的点击事件
        deleteButton.setOnClickListener(v -> deleteSchedule());

        // 从 Intent 中获取传递的 scheduleId
        Intent intent = getIntent();
        scheduleId = intent.getIntExtra("scheduleId", -1);  // 获取传递的 scheduleId

        Log.d("EditScheduleActivity", "传递的 scheduleId: " + scheduleId);  // 输出日志检查

        if (scheduleId != -1) {
            // 获取数据并显示
            schedule = scheduleDAO.getScheduleById(scheduleId);
            if (schedule != null) {
                titleEditText.setText(schedule.getTitle());
                contentEditText.setText(schedule.getContent());
                setDateButton.setText(schedule.getDate());  // 设置日期
                setTimeButton.setText(schedule.getTime());  // 设置时间
            } else {
                Toast.makeText(this, "未找到该日程", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "无效的日程ID", Toast.LENGTH_SHORT).show();
        }

        // 设置日期修改按钮的点击事件
        setDateButton.setOnClickListener(v -> {
            Calendar currentDate = Calendar.getInstance();
            new DatePickerDialog(EditScheduleActivity.this, (view, year, month, dayOfMonth) -> {
                selectedDateTime.set(year, month, dayOfMonth);
                // 更新日期字段
                setDateButton.setText(String.format("%d-%d-%d", year, month + 1, dayOfMonth));
            }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();
        });

        // 设置时间修改按钮的点击事件
        setTimeButton.setOnClickListener(v -> {
            Calendar currentTime = Calendar.getInstance();
            new TimePickerDialog(EditScheduleActivity.this, (view, hourOfDay, minute) -> {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedDateTime.set(Calendar.MINUTE, minute);
                // 更新时间字段
                setTimeButton.setText(String.format("%02d:%02d", hourOfDay, minute));
            }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), true).show();
        });

        // 修改按钮的点击事件
        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String content = contentEditText.getText().toString();

            if (schedule != null) {  // 确保 schedule 对象不为 null
                if (!title.isEmpty() && !content.isEmpty() && selectedDateTime != null) {
                    // 获取更新后的日期和时间
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                    String formattedDate = dateFormat.format(selectedDateTime.getTime());
                    String formattedTime = timeFormat.format(selectedDateTime.getTime());

                    // 更新 Schedule 对象
                    schedule.setTitle(title);
                    schedule.setContent(content);
                    schedule.setDate(formattedDate);
                    schedule.setTime(formattedTime);

                    // 更新数据库中的日程
                    boolean isUpdated = scheduleDAO.updateSchedule(schedule);

                    if (isUpdated) {
                        // 更新成功后，将修改后的日程作为结果返回
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedSchedule", schedule);  // 直接传递 Schedule 对象
                        setResult(RESULT_OK, resultIntent);  // 返回结果给 MainActivity
                        Toast.makeText(EditScheduleActivity.this, "日程已更新", Toast.LENGTH_SHORT).show();
                        finish();  // 返回主界面
                    } else {
                        Toast.makeText(EditScheduleActivity.this, "更新失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditScheduleActivity.this, "请输入标题、内容和时间", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EditScheduleActivity.this, "无法加载日程，可能该日程不存在", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteSchedule() {
        boolean isDeleted = scheduleDAO.deleteSchedule(scheduleId);

        if (isDeleted) {
            Toast.makeText(this, "日程已删除", Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("isDeleted", true);  // 设置标志表示已删除
            resultIntent.putExtra("deletedId", scheduleId);  // 返回被删除的日程 ID
            setResult(RESULT_OK, resultIntent);  // 返回结果给 MainActivity
            finish();  // 结束当前页面，返回 MainActivity
        } else {
            Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }


}
