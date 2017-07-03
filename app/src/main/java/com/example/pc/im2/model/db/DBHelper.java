package com.example.pc.im2.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pc.im2.model.table.ContactTable;
import com.example.pc.im2.model.table.InvitationTable;

/**
 * 每个用户对应各自的一个数据库
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建立数据库
        //1.联系人的
        db.execSQL(ContactTable.CREATE_TABLE);
        //2.邀请人的
        db.execSQL(InvitationTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
