package com.example.pc.im2.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.pc.im2.model.bean.UserInfo;
import com.example.pc.im2.model.db.AccountDB;
import com.example.pc.im2.model.table.AccountTable;

/**
 * 关于用户信息的数据库
 * 1.增加用户数据
 */

public class AccountDAO {

    private final AccountDB accountDB;

    public AccountDAO(Context context){
        accountDB = new AccountDB(context);
    }

    /**
     * 增加用户
     */
    public void addUser(UserInfo user){

        //校验
        if (user == null){
            throw new NullPointerException("userInfo不能为空");
        }

        //数据操作
        SQLiteDatabase database = accountDB.getWritableDatabase();

        //添加数据
        ContentValues value = new ContentValues();
        value.put(AccountTable.COL_HXID,user.getHxid());
        value.put(AccountTable.COL_NICK,user.getNick());
        value.put(AccountTable.COL_PHOTO,user.getPhoto());
        value.put(AccountTable.COL_USERNAME,user.getUsername());

        //保存
        database.replace(AccountTable.TABLE_NAME,null, value);
    }

    /**
     * 根据环信ID，获取数据库对应的用户
     */
    public UserInfo getUserInfo(String hxid){
        //校验
        if (TextUtils.isEmpty(hxid)){
            return null;
        }

        SQLiteDatabase database = accountDB.getWritableDatabase();

        //查询字段
        String sql = "select * from "+AccountTable.TABLE_NAME
                +" where "+AccountTable.COL_HXID+"=?";
        //使用语句查询
        Cursor cursor = database.rawQuery(sql, new String[]{hxid});
        //新建一个用户，用于保存信息
        UserInfo userInfo = new UserInfo();
        if (cursor.moveToNext()){
            //查询信息
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(AccountTable.COL_HXID)));
            userInfo.setUsername(cursor.getString(cursor.getColumnIndex(AccountTable.COL_USERNAME)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(AccountTable.COL_PHOTO)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(AccountTable.COL_NICK)));
        }
        cursor.close();//记着关闭游标

        //返回
        return userInfo;
    }

}
