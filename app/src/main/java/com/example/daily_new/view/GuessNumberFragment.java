package com.example.daily_new.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.daily_new.R;
import java.util.Random;

public class GuessNumberFragment extends Fragment {
    private EditText guessInput;
    private Button guessButton, clearButton;
    private TextView resultView, rangeView;
    private int randomNumber;
    private int minGuess = 1;
    private int maxGuess = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guess_number, container, false);

        // 初始化视图组件
        guessInput = view.findViewById(R.id.guess_input);
        guessButton = view.findViewById(R.id.guess_button);
        clearButton = view.findViewById(R.id.clear_button);
        resultView = view.findViewById(R.id.result_view);
        rangeView = view.findViewById(R.id.range_view);

        // 随机生成 1 到 100 之间的整数
        randomNumber = new Random().nextInt(100) + 1;

        // 按钮点击事件
        guessButton.setOnClickListener(v -> checkGuess());

        // 清空按钮点击事件
        clearButton.setOnClickListener(v -> clearGame());

        // 初始化区间显示
        updateRangeDisplay();

        return view;
    }

    // 检查用户猜测的数字
    private void checkGuess() {
        String guessText = guessInput.getText().toString();

        // 验证输入
        if (TextUtils.isEmpty(guessText)) {
            resultView.setText("请输入一个数字");
            return;
        }

        try {
            int guess = Integer.parseInt(guessText);

            // 判断猜测结果
            if (guess < randomNumber) {
                resultView.setText("你猜的数字太小了");
                minGuess = Math.max(minGuess, guess + 1); // 更新最小猜测范围
            } else if (guess > randomNumber) {
                resultView.setText("你猜的数字太大了");
                maxGuess = Math.min(maxGuess, guess - 1); // 更新最大猜测范围
            } else {
                resultView.setText("恭喜你，猜对了！");
            }

            // 更新区间显示
            updateRangeDisplay();

        } catch (NumberFormatException e) {
            resultView.setText("请输入有效的数字");
        }
    }

    // 更新当前区间显示
    private void updateRangeDisplay() {
        rangeView.setText(String.format("当前区间: %d 到 %d", minGuess, maxGuess));
    }

    // 清空游戏
    private void clearGame() {
        // 重新生成随机数
        randomNumber = new Random().nextInt(100) + 1;

        // 重置最小和最大区间
        minGuess = 1;
        maxGuess = 100;

        // 清空输入框和结果显示
        guessInput.setText("");
        resultView.setText("");

        // 更新区间显示
        updateRangeDisplay();
    }
}
