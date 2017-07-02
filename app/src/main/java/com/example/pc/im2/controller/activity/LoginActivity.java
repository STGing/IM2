package com.example.pc.im2.controller.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pc.im2.R;
import com.example.pc.im2.base.BaseActivity;
import com.example.pc.im2.common.Model;
import com.example.pc.im2.model.bean.UserInfo;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_et_username)
    EditText loginEtUsername;
    @BindView(R.id.login_et_password)
    EditText loginEtPassword;
    @BindView(R.id.login_btn_register)
    Button loginBtnRegister;
    @BindView(R.id.login_btn_login)
    Button loginBtnLogin;


    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }



    @OnClick({R.id.login_btn_register, R.id.login_btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn_register:
                register();//注册
                break;
            case R.id.login_btn_login:
                login();//登录
                break;
        }
    }

    /**
     * 注册账号
     */
    private void register() {
        final String username = loginEtUsername.getText().toString().trim();
        final String pwd = loginEtPassword.getText().toString().trim();

        //校验
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(username)){
            showToast("用户名或密码不能为空");
            return;
        }

        //分线程注册
        Model.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(username,pwd);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("注册成功");
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    /**
     * 登陆
     */
    private void login() {
        //获取输入的值
        final String pwd = loginEtPassword.getText().toString().trim();
        final String username = loginEtUsername.getText().toString().trim();

        //校验
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(username)){
            showToast("用户名或密码不能为空");
            return;
        }

        //分线程联网登陆
        Model.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                EMClient.getInstance().login(username, pwd, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        //登录成功 处理一些特殊的信息
                        String currentUser = EMClient.getInstance().getCurrentUser();

                        Model.getInstance().loginSuccess(new UserInfo(currentUser,currentUser));
                        //跳转界面
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("登录成功");
                            }
                        });
                        //结束当前页面
                        finish();
                    }

                    @Override
                    public void onError(int i, final String s) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast(s);
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }
}
