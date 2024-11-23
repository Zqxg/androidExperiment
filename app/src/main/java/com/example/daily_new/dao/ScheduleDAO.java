package com.example.daily_new.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.daily_new.model.Schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    // 定义表名常量
    private static final String TABLE_NAME = "schedules";

    public ScheduleDAO(Context context) {
        dbHelper = new DBHelper(context);
        // 初始化数据库连接
        database = dbHelper.getWritableDatabase();
    }

    // 打开数据库连接
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // 关闭数据库连接
    public void close() {
        dbHelper.close();
    }

    // 插入日程
    public boolean insertSchedule(Schedule schedule) {
        ContentValues values = new ContentValues();
        values.put("title", schedule.getTitle());
        values.put("content", schedule.getContent());
        values.put("date", schedule.getDate());
        values.put("time", schedule.getTime());

        long result = database.insert(TABLE_NAME, null, values);
        return result != -1;  // 插入成功返回 true
    }

    // 更新日程方法，返回布尔值
    public boolean updateSchedule(Schedule schedule) {
        ContentValues values = new ContentValues();
        values.put("title", schedule.getTitle());
        values.put("content", schedule.getContent());
        values.put("date", schedule.getDate());
        values.put("time", schedule.getTime());

        // 执行更新操作并检查影响的行数
        int rowsAffected = database.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(schedule.getId())});
        return rowsAffected > 0;  // 如果影响了至少一行，表示更新成功
    }

    // 删除日程方法，返回布尔值
    public boolean deleteSchedule(int id) {
        // 删除操作
        int rowsAffected = database.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;  // 如果删除了至少一行，返回 true，否则返回 false
    }

    // 根据ID获取日程
    public Schedule getScheduleById(int scheduleId) {
        Cursor cursor = database.query(TABLE_NAME, null, "id = ?", new String[]{String.valueOf(scheduleId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Schedule schedule = cursorToSchedule(cursor);
            cursor.close();
            return schedule;
        }
        return null;
    }

    // 获取所有日程
    public List<Schedule> getAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    schedules.add(cursorToSchedule(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();  // 输出异常信息
        } finally {
            if (cursor != null) cursor.close();  // 确保关闭游标
        }
        return schedules;
    }

    // 将Cursor中的数据转换成Schedule对象
    private Schedule cursorToSchedule(Cursor cursor) {
        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
        @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
        @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
        @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
        @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));

        return new Schedule(id, title, content, date, time);
    }
}
