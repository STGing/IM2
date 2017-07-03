package com.example.pc.im2.controller.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.pc.im2.R;
import com.example.pc.im2.base.BaseFragment;
import com.example.pc.im2.controller.activity.LoginActivity;
import com.example.pc.im2.utils.UIUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;

/**
 * Created by PC on 2017/7/3.
 */

public class SettingFragment extends BaseFragment {

    @BindView(R.id.setting_btn_exit)
    Button settingBtnExit;

    @Override
    public void initView() {

    }

    @Override
    public int getLayoutID() {
        return R.layout.setting_fragment;
    }

    @Override
    public void initData() {

        //1.获取当前登录用户的信息
        String currentUser = EMClient.getInstance().getCurrentUser();
        //2.设置退出按钮里面有用户信息
        settingBtnExit.setText("退出登陆（"+currentUser+")");

        //点击退出登录按钮
        settingBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                * 第一个参数 如果集成了GCM等第三方推送，方法里第一个参数需要设为true
                * */
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        //跳转到登陆界面
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        //结束当前页面
                        getActivity().finish();
                    }

                    @Override
                    public void onError(int i, String s) {
                        //登出失败
                        UIUtils.showToast(s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });


            }
        });
    }

}
