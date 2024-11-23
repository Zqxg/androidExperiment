package com.example.daily_new.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daily_new.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RetirementActivity extends AppCompatActivity {

    private EditText etBirthDate;
    private Button btnDatePicker, btnCalculate;
    private TextView tvRetirementDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retirement);

        // 初始化控件
        etBirthDate = findViewById(R.id.et_birth_date);
        btnDatePicker = findViewById(R.id.btn_date_picker);
        btnCalculate = findViewById(R.id.btn_calculate);
        tvRetirementDate = findViewById(R.id.tv_retirement_date);

        // 设置日期选择按钮的点击事件
        btnDatePicker.setOnClickListener(v -> showDatePickerDialog());

        // 设置查询退休时间按钮的点击事件
        btnCalculate.setOnClickListener(v -> calculateRetirementDate());
    }

    /**
     * 显示日期选择对话框
     */
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    // 设置选择的日期到输入框
                    String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    etBirthDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * 计算退休时间
     */
    private void calculateRetirementDate() {
        String birthDateStr = etBirthDate.getText().toString().trim();
        if (TextUtils.isEmpty(birthDateStr)) {
            Toast.makeText(this, "请输入或选择出生日期", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 解析出生日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate = sdf.parse(birthDateStr);

            // 根据性别假设：男性60岁退休，女性55岁退休（此处可以扩展为动态选择性别）
            int retirementAge = 60;

            // 计算退休日期
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(birthDate);
            calendar.add(Calendar.YEAR, retirementAge);
            Date retirementDate = calendar.getTime();

            // 显示退休日期
            String retirementDateStr = sdf.format(retirementDate);
            tvRetirementDate.setText("退休时间为 " + retirementDateStr);

        } catch (ParseException e) {
            Toast.makeText(this, "日期格式错误，请输入正确格式 (YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
        }
    }
}
