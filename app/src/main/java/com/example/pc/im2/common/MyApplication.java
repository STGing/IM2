package com.example.pc.im2.common;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

/**
 * Created by PC on 2017/7/2.
 */

public class MyApplication extends Application {


    private static Context context;
    private static int pid;
    private static Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        pid = android.os.Process.myPid();

        handler = new Handler();

        //初始化环信
        initHX();

        //初始化Model
        Model.getInstance().init(this);
    }

    public static Context getContext() {
        return context;
    }

    private void initHX() {
        //初始化环信sdk
        EMOptions options = new EMOptions();//配置一些功能
        //是否自动接受群组邀请
        options.setAutoAcceptGroupInvitation(false);
        //是否自动接受邀请
        options.setAcceptInvitationAlways(false);
        EaseUI.getInstance().init(this,options);
    }

    public static int getPid(){
        return pid;
    }

    public static Handler getHandler(){
        return handler;
    }
}
