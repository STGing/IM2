package com.example.pc.im2.common;

import android.content.Context;

import com.example.pc.im2.controller.GlobalLIstener;
import com.example.pc.im2.model.bean.UserInfo;
import com.example.pc.im2.model.dao.AccountDAO;
import com.example.pc.im2.utils.DBManager;
import com.example.pc.im2.utils.SPUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by PC on 2017/7/2.
 */

public class Model {

    private Context context;
    private AccountDAO accountDAO;//用户数据库操作
    private DBManager manager;
    private GlobalLIstener globalLIstener;

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
        //初始化全局监听
        globalLIstener = new GlobalLIstener(context);
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

        //如果Manager存在，就关闭
        if (manager != null)
            manager.close();

        //此时，初始化DBManager
        manager = new DBManager(context,userInfo.getUsername());
    }

    /**
     * 获取用户的数据库操作者
     * @return
     */
    public AccountDAO getAccountDAO(){
        return accountDAO;
    }

    /**
     * 获取 Contact 和 Invation 2个数据库的操作这
     */
    public DBManager getDBManager(){
        return manager;
    }
}
