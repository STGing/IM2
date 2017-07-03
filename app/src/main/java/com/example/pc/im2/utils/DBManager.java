package com.example.pc.im2.utils;

import android.content.Context;

import com.example.pc.im2.model.dao.ContactDAO;
import com.example.pc.im2.model.dao.InvitationDAO;
import com.example.pc.im2.model.db.DBHelper;

/**
 * 集中管理Contact 和 Invitation 2个数据库的Dao
 */

public class DBManager {

    private final DBHelper dbHelper;
    private final ContactDAO contactDAO;
    private final InvitationDAO invitationDAO;

    public DBManager(Context context, String name) {
        dbHelper = new DBHelper(context,name);
        contactDAO = new ContactDAO(dbHelper);
        invitationDAO = new InvitationDAO(dbHelper);
    }

    //设置get方法

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public ContactDAO getContactDAO() {
        return contactDAO;
    }

    public InvitationDAO getInvitationDAO() {
        return invitationDAO;
    }

    //设置关闭的方法
    public void close(){
        if (dbHelper != null){
            dbHelper.close();
        }
    }
}
