package com.example.pc.im2.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pc.im2.model.table.AccountTable;

/**
 * Created by PC on 2017/7/2.
 */

public class AccountDB extends SQLiteOpenHelper {

    public AccountDB(Context context) {
        super(context, "account.db", null, 1);
    }

    /**
     * 用于创建数据库
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AccountTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
