package com.example.pc.im2.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.pc.im2.model.bean.InvitationInfo;
import com.example.pc.im2.model.bean.UserInfo;
import com.example.pc.im2.model.db.DBHelper;
import com.example.pc.im2.model.table.InvitationTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 对 Invitation 数据库的CRUD
 */

public class InvitationDAO {

    private final DBHelper dbHelper;

    public InvitationDAO(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * 添加邀请人员信息
     * @param invitationInfo
     */
    public void addInvitation(InvitationInfo invitationInfo){
        //1.校验数据
        if (invitationInfo == null){
            throw new NullPointerException("信息不能为空");
        }

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        //保存数据的字段和值
        ContentValues values = new ContentValues();
        values.put(InvitationTable.COL_REASON,invitationInfo.getReason());
        values.put(InvitationTable.COL_USER_HXID,invitationInfo.getUserInfo().getHxid());
        values.put(InvitationTable.COL_USER_NAME,invitationInfo.getUserInfo().getUsername());
        int ordinal = invitationInfo.getStatus().ordinal();//枚举转换成对应数字
        values.put(InvitationTable.COL_STATE,ordinal);
        database.replace(InvitationTable.TABLE_NAME,null,values);
    }

    /**
     * 获取所有邀请人员的信息
     * @return
     */
    public List<InvitationInfo> getInvitations(){
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        //执行的语句
        String sql = "select * from "+InvitationTable.TABLE_NAME;
        //查询
        Cursor cursor = database.rawQuery(sql, null);
        List<InvitationInfo> infoList = new ArrayList<>();//保存邀请信息的数组
        while (cursor.moveToNext()){
            InvitationInfo info = new InvitationInfo();
            info.setReason(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_REASON)));
            info.setStatus(int2InviteStatus(cursor.getInt(cursor.getColumnIndex(InvitationTable.COL_STATE))));

            UserInfo userInfo = new UserInfo();
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_HXID)));
            userInfo.setUsername(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_NAME)));

            info.setUserInfo(userInfo);
            //将invitationInfo添加到集合中
            infoList.add(info);
        }

        //关闭
        cursor.close();
        return infoList;
    }

    // 将int类型状态转换为邀请的状态（即将对应位置的数字转换成对应枚举常量）
    private InvitationInfo.InvitationStatus int2InviteStatus(int intStatus) {

        if (intStatus == InvitationInfo.InvitationStatus.NEW_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT.ordinal()) {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER.ordinal()) {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER;
        }
        return null;
    }


    /**
     * 根据环信ID，移除对应的邀请信息
     * @param hxId
     */
    public void removeInvitation(String hxId){
        //1.校验数据
        if (TextUtils.isEmpty(hxId)){
            throw new NullPointerException("ID不能为空");
        }

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        database.delete(InvitationTable.TABLE_NAME,InvitationTable.COL_USER_HXID+"=?",new String[]{hxId});
    }

    /**
     * 更新邀请状态的信息
     * @param invitationStatus
     * @param hxId
     */
    public void updateInvitationStatus(InvitationInfo.InvitationStatus invitationStatus, String hxId){
        //数据校验
        if (invitationStatus == null || TextUtils.isEmpty(hxId)){
            throw new NullPointerException("数据不能为空");
        }

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(InvitationTable.COL_STATE,invitationStatus.ordinal());
        database.update(InvitationTable.TABLE_NAME,values,InvitationTable.COL_USER_HXID+"=?",new String[]{hxId});
    }
}

