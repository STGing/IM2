package com.example.pc.im2.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by PC on 2017/7/2.
 */

public class Model {

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
     * 设置线程池
     */
    private ExecutorService service = Executors.newCachedThreadPool();

    public ExecutorService getThreadPool(){
        return service;
    }

}
