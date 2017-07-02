package com.example.pc.im2.common;

import android.content.Context;

import com.example.pc.im2.model.bean.UserInfo;
import com.example.pc.im2.model.dao.AccountDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by PC on 2017/7/2.
 */

public class Model {

    private Context context;
    private AccountDAO accountDAO;//用户数据库操作

    /**
     * 设置单例模式
     */
    private Model() {}

    private static Model instance;

    public static Model getInstance(){
        if (instance == null) {
            synchronized (Model.class){
                if (instance == null){
                    instance = new Model();
                }
            }
        }
        return instance;
    }

    /**
     * 在程序开始的时候进行初始化
     * @param context
     */
    public void init(Context context){
        this.context = context;
        accountDAO = new AccountDAO(context);
    }

    /**
     * 设置线程池
     */
    private ExecutorService service = Executors.newCachedThreadPool();

    public ExecutorService getThreadPool(){
        return service;
    }

    /**
     * 登录成功以后保存用户数据
     */
    public void loginSuccess(UserInfo userInfo) {
        //添加用户
        accountDAO.addUser(userInfo);
    }

    /**
     * 获取用户的数据库操作者
     * @return
     */
    public AccountDAO getAccountDAO(){
        return accountDAO;
    }

}
