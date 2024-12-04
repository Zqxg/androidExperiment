package com.example.daily_new.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.daily_new.R;

public class CustomFragment extends Fragment {

    private EditText editTextHours;
    private Button buttonConvert;
    private TextView textViewResult;

    public CustomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom, container, false);

        // 初始化视图
        editTextHours = view.findViewById(R.id.editTextHours);
        buttonConvert = view.findViewById(R.id.buttonConvert);
        textViewResult = view.findViewById(R.id.textViewResult);

        // 设置按钮点击事件
        buttonConvert.setOnClickListener(v -> convertTimeAndTimestamp());

        return view;
    }

    // 进行时间换算和时间戳转换的逻辑
    private void convertTimeAndTimestamp() {
        String inputText = editTextHours.getText().toString();

        // 检查用户输入是否为空
        if (TextUtils.isEmpty(inputText)) {
            Toast.makeText(getContext(), "请输入小时数", Toast.LENGTH_SHORT).show();
            return;
        }

        // 转换输入的小时数
        try {
            double hours = Double.parseDouble(inputText);
            int minutes = (int) (hours * 60);
            int seconds = (int) (hours * 3600);

            // 计算时间戳：当前时间戳 + 转换后的秒数
            long currentTimestamp = System.currentTimeMillis() / 1000;  // 当前时间戳（秒）
            long timestampWithOffset = currentTimestamp + seconds;      // 当前时间戳加上输入的时间差

            // 显示结果
            textViewResult.setText(String.format("%s 小时 = %d 分钟 = %d 秒\n转换后的时间戳: %d", inputText, minutes, seconds, timestampWithOffset));

        } catch (NumberFormatException e) {
            // 输入格式错误
            Toast.makeText(getContext(), "请输入有效的数字", Toast.LENGTH_SHORT).show();
        }
    }
}
