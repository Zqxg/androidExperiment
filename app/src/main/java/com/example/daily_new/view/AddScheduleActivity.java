package com.example.daily_new.view;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daily_new.R;
import com.example.daily_new.dao.ScheduleDAO;
import com.example.daily_new.model.Schedule;
import com.example.daily_new.controllers.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddScheduleActivity extends AppCompatActivity {

    private EditText titleEditText, contentEditText;
    private Button setDateButton, setTimeButton, saveButton;
    private Calendar selectedDateTime;
    private ScheduleDAO scheduleDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        setDateButton = findViewById(R.id.setDateButton);
        setTimeButton = findViewById(R.id.setTimeButton);
        saveButton = findViewById(R.id.saveButton);

        scheduleDAO = new ScheduleDAO(this);  // 初始化数据库操作类
        selectedDateTime = Calendar.getInstance();

        // 设置日期选择
        setDateButton.setOnClickListener(v -> {
            Calendar currentDate = Calendar.getInstance();
            new DatePickerDialog(AddScheduleActivity.this, (view, year, month, dayOfMonth) -> {
                selectedDateTime.set(year, month, dayOfMonth);
                // 更新按钮文本为选中的日期
                setDateButton.setText(String.format("%d-%d-%d", year, month + 1, dayOfMonth));
            }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();
        });

        // 设置时间选择
        setTimeButton.setOnClickListener(v -> {
            Calendar currentTime = Calendar.getInstance();
            new TimePickerDialog(AddScheduleActivity.this, (view, hourOfDay, minute) -> {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedDateTime.set(Calendar.MINUTE, minute);
                // 更新按钮文本为选中的时间
                setTimeButton.setText(String.format("%02d:%02d", hourOfDay, minute));
            }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), true).show();
        });


        // 保存日程
        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String content = contentEditText.getText().toString();

            if (!title.isEmpty() && !content.isEmpty() && selectedDateTime != null) {
                // 格式化日期和时间
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                String formattedDate = dateFormat.format(selectedDateTime.getTime());  // 获取日期
                String formattedTime = timeFormat.format(selectedDateTime.getTime());  // 获取时间

                // 创建 Schedule 对象，并传递日期和时间
                Schedule schedule = new Schedule(title, content, formattedDate, formattedTime);

                // 保存日程到数据库
                boolean isInserted = scheduleDAO.insertSchedule(schedule);

                if (isInserted) {
                    // 设置提醒
                    setReminder(selectedDateTime);
                    Toast.makeText(this, "日程已保存", Toast.LENGTH_SHORT).show();
                    finish();  // 返回主界面
                } else {
                    Toast.makeText(this, "保存失败，请重试", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "请输入标题、内容和时间", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setReminder(Calendar calendar) {
        // 设置闹钟提醒
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // 设置闹钟为精确时间
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
