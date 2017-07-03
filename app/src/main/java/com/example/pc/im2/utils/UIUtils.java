package com.example.pc.im2.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.pc.im2.common.MyApplication;

/**
 * Created by Administrator on 2017/6/20.
 */

public class UIUtils {

    
    /*
    * 根据ID,获取View
    * */
    public static View inflate(int id){
        return View.inflate(getContext(),id,null);
    }


    /*
    * 返回一个上下文
    * */
    public static Context getContext() {

        return MyApplication.getContext();
    }




    //与屏幕分辨率相关的
    public static int dp2px(int dp){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5);

    }

    public static int px2dp(int px){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);
    }

    public static void runOnUIThread(Runnable runnable) {

        //pid processid 进程id
        //tid threadid 线程id
        //注意：如果在主线程中运行 那么tid == pid
//        Log.d("process", "runOnUIThread: "
//                +"processid=="+MyApplication.getPid()
//                +"  threadid== "+android.os.Process.myTid()
//        );
        if(MyApplication.getPid() == android.os.Process.myTid()){
            runnable.run();
        }else{
            MyApplication.getHandler().post(runnable);
        }
    }


    public static void showToast(final String message){
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }



}
