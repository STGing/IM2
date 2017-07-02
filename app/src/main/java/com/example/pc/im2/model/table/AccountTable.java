package com.example.pc.im2.model.table;

/**
 * 存放数据库的语句
 * 1.建立表的语句。
 */

public class AccountTable {
    public static final String TABLE_NAME = "account";
    public static final String COL_USERNAME = "username";
    public static final String COL_HXID = "hxid";
    public static final String COL_PHOTO = "photo";
    public static final String COL_NICK = "nick";

    public static final String CREATE_TABLE = "create table "+TABLE_NAME+"("
            +COL_HXID+" text primary key, "
            +COL_USERNAME +" text, "
            +COL_PHOTO +" text, "
            +COL_NICK +" text)";

}
