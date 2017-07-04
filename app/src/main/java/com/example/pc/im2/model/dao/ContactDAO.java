package com.example.pc.im2.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.pc.im2.model.bean.UserInfo;
import com.example.pc.im2.model.db.DBHelper;
import com.example.pc.im2.model.table.ContactTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 对Contact数据库的CRUD
 */

public class ContactDAO {

    private final DBHelper dbHelper;

    public ContactDAO(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    /**
     * 获取所有联系人信息
     */
    public List<UserInfo> getContacts(){
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        // 1 表示 是当前用户的联系人
        String sql = "select * from " + ContactTable.TABLE_NAME+" where "
                +ContactTable.COL_IS_CONTACT + "=1";
        Cursor cursor = database.rawQuery(sql, null);
        List<UserInfo> userInfos = new ArrayList<>();
        while (cursor.moveToNext()){
            UserInfo user = new UserInfo();
            user.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_NICK)));
            user.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_PHOTO)));
            user.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_HXID)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_NAME)));

            //添加集合
            userInfos.add(user);
        }

        cursor.close();
        return userInfos;
    }

    // 通过环信id获取联系人单个信息
    public UserInfo getContactByHx(String hxId){
        //校验数据
        if (TextUtils.isEmpty(hxId)){
            throw new NullPointerException("ID不能为空");
        }

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select * from "+ContactTable.TABLE_NAME
                + " where "+ContactTable.COL_USER_HXID+"=?";
        Cursor cursor = database.rawQuery(sql, new String[]{hxId});
        UserInfo user = new UserInfo();
        if (cursor.moveToNext()){
            user.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_HXID)));
            user.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_NICK)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_NAME)));
            user.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_PHOTO)));
        }
        cursor.close();
        return user;
    }

    /**
     * // 通过环信id集合获取用户联系人集合
     * @param hxIds
     * @return
     */
    public List<UserInfo> getContactsByHx(List<String> hxIds){
        //校验数据
        if (hxIds == null || hxIds.size() == 0){
            throw new NullPointerException("ID集合不能为空");
        }

        List<UserInfo> list = new ArrayList<>();

        for (int i = 0; i < hxIds.size(); i++) {
            UserInfo user = getContactByHx(hxIds.get(i));
            if (user != null)
                list.add(user);
        }
        return list;
    }

    /**
     * 保存单个联系人
     * @param user
     * @param isMyContact
     */
    public void saveContact(UserInfo user, boolean isMyContact){
        //校验数据
        if (user == null){
            throw new NullPointerException("数据不能为空");
        }

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        ContentValues value = new ContentValues();
        value.put(ContactTable.COL_USER_HXID,user.getHxid());
        value.put(ContactTable.COL_USER_NAME,user.getUsername());
        value.put(ContactTable.COL_USER_NICK,user.getNick());
        value.put(ContactTable.COL_USER_PHOTO,user.getPhoto());
        value.put(ContactTable.COL_IS_CONTACT,isMyContact?1:0);
        database.replace(ContactTable.TABLE_NAME,null,value);
    }

    /**
     * 保存联系人集合信息
     */
    public void saveContacts(List<UserInfo> contacts, boolean isMyContact){
        //数据校验
        if (contacts == null || contacts.size() == 0){
            throw new NullPointerException("数据不能为空");
        }

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        for (int i = 0; i < contacts.size(); i++) {
            saveContact(contacts.get(i),isMyContact);
        }
    }

    /**
     * 删除单个联系人
     */
    public void deleteContactByHxId(String hxId){
        //校验数据
        if (TextUtils.isEmpty(hxId)){
            throw new NullPointerException("数据不能为空");
        }

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        database.delete(ContactTable.TABLE_NAME,ContactTable.COL_USER_HXID+"=?",new String[]{hxId});
    }
}
