package com.example.pc.im2.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.pc.im2.common.ContactValue;
import com.example.pc.im2.common.Model;
import com.example.pc.im2.model.bean.InvitationInfo;
import com.example.pc.im2.model.bean.UserInfo;
import com.example.pc.im2.utils.SPUtils;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

/**



/**
 * 全局监听环信的的一些回调
 */

public class GlobalLIstener {


    private final Context mContext;
    private final LocalBroadcastManager manager;

    public GlobalLIstener(Context context) {
        this.mContext = context;
        manager = LocalBroadcastManager.getInstance(context);
        EMClient.getInstance().contactManager().setContactListener(eContactListener);
    }

    EMContactListener eContactListener = new EMContactListener() {
        //收到好友邀请  别人加你
        @Override
        public void onContactInvited(String username, String reason) {

            //记录邀请信息
            InvitationInfo info = new InvitationInfo();
            info.setReason(reason);
            info.setUserInfo(new UserInfo(username,username));
            info.setStatus(InvitationInfo.InvitationStatus.NEW_INVITE);
            //有人邀请，保存邀请信息
            Model.getInstance()
                    .getDBManager()
                    .getInvitationDAO()
                    .addInvitation(info);

            //有人邀请，所以小红点提示消息，调用SPl来保存
            SPUtils.put(mContext,SPUtils.NEW_INVITATION,true);

            //发送广播通知
            manager.sendBroadcast(new Intent(ContactValue.NEW_INVITE_CHANGE));

        }

        //好友请求被同意  你加别人的时候 别人同意了
        @Override
        public void onContactAgreed(String username) {
            //添加邀请信息
            //记录邀请信息
            InvitationInfo info = new InvitationInfo();
            info.setReason("邀请被接受");
            info.setUserInfo(new UserInfo(username,username));
            info.setStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);
            //保存
            Model.getInstance()
                    .getDBManager()
                    .getInvitationDAO()
                    .addInvitation(info);

            //保存小红点信息
            SPUtils.put(mContext,SPUtils.NEW_INVITATION,true);

            //发送广播通知
            manager.sendBroadcast(new Intent(ContactValue.NEW_INVITE_CHANGE));
        }



        //被删除时回调此方法
        @Override
        public void onContactDeleted(String username) {

            //删除对方的邀请信息
            Model.getInstance().getDBManager().getInvitationDAO()
                    .removeInvitation(username);

            //删除用户的信息
            Model.getInstance().getDBManager().getContactDAO()
                    .deleteContactByHxId(username);

            //发送广播通知
            manager.sendBroadcast(new Intent(ContactValue.CONTACT_CHANGED));

        }


        //增加了联系人时回调此方法  当你同意添加好友
        @Override
        public void onContactAdded(String username) {
            //添加联系人
            Model.getInstance().getDBManager().getContactDAO()
                    .saveContact(new UserInfo(username,username),true );

            //发送广播通知
            manager.sendBroadcast(new Intent(ContactValue.CONTACT_CHANGED));
        }

        //好友请求被拒绝  你加别人 别人拒绝了
        @Override
        public void onContactRefused(String username) {

            //保存小红点
            SPUtils.put(mContext,SPUtils.NEW_INVITATION,true);
            //发送广播
            manager.sendBroadcast(new Intent(ContactValue.NEW_INVITE_CHANGE));
        }
    };
}
