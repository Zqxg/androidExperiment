package com.example.daily_new.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.daily_new.R;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 初始化控件
        ImageView profileImage = view.findViewById(R.id.profileImage);
        TextView profileName = view.findViewById(R.id.profileName);
        Button logoutButton = view.findViewById(R.id.logoutButton);

        // 设置示例用户名
        profileName.setText("示例用户");

        // 设置头像点击事件
        profileImage.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "你点击了头像！", Toast.LENGTH_SHORT).show();
        });

        // 设置退出登录按钮点击事件
        logoutButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "你已退出登录！", Toast.LENGTH_SHORT).show();
            // 跳转到登录页（示例）
//            Intent intent = new Intent(requireContext(), LoginActivity.class);
//            startActivity(intent);
//            requireActivity().finish();
        });

        return view;
    }
}
