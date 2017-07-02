package com.example.pc.im2.controller.activity;

import android.content.Intent;
import android.os.CountDownTimer;

import com.example.pc.im2.R;
import com.example.pc.im2.base.BaseActivity;
import com.example.pc.im2.common.Model;
import com.hyphenate.chat.EMClient;

public class SplashActivity extends BaseActivity {

    private CountDownTimer countDownTimer;

    @Override
    public void initListener() {


    }

    /**
     * 切换要进入的页面
     */
    private void selectChageActivity() {

        Model.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //是否登录过环信服务器
                boolean loggedInBefore = EMClient.getInstance().isLoggedInBefore();
                if (loggedInBefore){
                    //登录过
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }else{
                    //没有登录过
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }
            }
        });
    }

    @Override
    public void initData() {

        //第一个参数是倒计时的总时长，倒计时时间间隔
        //倒计时结束
        countDownTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                //倒计时结束
                selectChageActivity();
            }
        }.start();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消倒计时
        countDownTimer.cancel();
    }
}
