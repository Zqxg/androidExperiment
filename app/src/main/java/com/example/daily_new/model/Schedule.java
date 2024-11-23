package com.example.daily_new.model;

import java.io.Serializable;
import java.util.Date;

public class Schedule implements Serializable {
    private int id;
    private String title;
    private String content;
    private String date;
    private String time;

    // 用于插入的构造函数
    public Schedule(String title, String content, String date, String time) {
        this.title = title;
        this.content = content;
        this.date = date;  // 存储日期
        this.time = time;  // 存储时间
    }

    // 用于从数据库获取数据的构造函数
    public Schedule(int id, String title, String content, String date, String time) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
    }

    // 获取提醒时间，格式化日期和时间
    public String getReminderTime() {
        return date + " " + time;  // 可以根据需要调整格式，例如使用 SimpleDateFormat 格式化
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}

