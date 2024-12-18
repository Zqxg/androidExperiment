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

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class IntegrationFragment extends Fragment {
    private EditText functionInput, lowerBoundInput, upperBoundInput;
    private Button calculateButton;
    private TextView resultView;

    private String stringResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_integration, container, false);

        // 初始化视图组件
        functionInput = view.findViewById(R.id.function_input);
        lowerBoundInput = view.findViewById(R.id.lower_bound_input);
        upperBoundInput = view.findViewById(R.id.upper_bound_input);
        calculateButton = view.findViewById(R.id.calculate_button);
        resultView = view.findViewById(R.id.result_view);

        // 按钮点击事件
        calculateButton.setOnClickListener(v -> calculateIntegration());

        return view;
    }

    private void calculateIntegration() {
        String function = functionInput.getText().toString();
        String lowerBound = lowerBoundInput.getText().toString();
        String upperBound = upperBoundInput.getText().toString();

        // 验证输入
        if (TextUtils.isEmpty(function) || TextUtils.isEmpty(lowerBound) || TextUtils.isEmpty(upperBound)) {
            resultView.setText("请输入完整的数据");
            return;
        }

        try {
            double a = Double.parseDouble(lowerBound);
            double b = Double.parseDouble(upperBound);

            // 调用变步长梯形法积分计算方法
            TrapezoidCalculation(function, a, b);
            resultView.setText(stringResult);
        } catch (NumberFormatException e) {
            resultView.setText("请输入有效的数字");
        }
    }

    // 变步长梯形法计算定积分
    private void TrapezoidCalculation(String function, double a, double b) {
        int m = 1, n = 0;
        double[] T = new double[100]; // 数组存储每次计算的结果

        // 初始梯形法计算
        T[n] = (b - a) * (f(function, a) + f(function, b)) / 2;

        do {
            double h = (b - a) / m;
            double s = 0.0;

            // 计算中间的函数值
            for (int k = 0; k < m; k++) {
                s += f(function, a + (k + 0.5) * h);  // 中点求值
            }

            // 更新梯形法的计算结果
            T[n + 1] = T[n] / 2 + h / 2 * s;

            m = 2 * m;  // 每次加倍分段数
            n++;        // 增加计算次数
        } while (Math.abs(T[n] - T[n - 1]) >= 0.0000001);  // 误差条件

        // 保存结果为字符串，方便显示
        stringResult = "n= " + n +
                "\nS= " + T[n] +
                "\n误差为 " + Math.abs(T[n] - T[n - 1]);
    }

    // 使用 Rhino 计算表达式的值
    private double f(String function, double x) {
        try {
            // 使用 Rhino JavaScript 引擎计算表达式
            Context context = Context.enter();
            Scriptable scope = context.initStandardObjects();

            // 替换表达式中的 'x^' 为 'Math.pow(x,' 和 '^' 为 Math.pow(x,
            String expression = function.replace("x^", "Math.pow(x,")
                    .replace("^", "Math.pow(x,")  // 将 '^' 转换为 Math.pow(x,
                    .replace("x", String.valueOf(x));  // 替换 'x' 为当前值

            // 计算表达式
            Object result = context.evaluateString(scope, expression + ")", "<string>", 1, null);

            // 将结果转换为 double 并返回
            return Double.parseDouble(result.toString());
        } catch (Exception e) {
            // 如果计算失败，返回 0 或抛出异常
            e.printStackTrace();
            return 0.0;
        } finally {
            Context.exit();
        }
    }
}
