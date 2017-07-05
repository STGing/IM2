package com.example.pc.im2.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.pc.im2.R;
import com.example.pc.im2.common.ContactValue;
import com.example.pc.im2.common.Model;
import com.example.pc.im2.controller.activity.ChatActivity;
import com.example.pc.im2.controller.activity.InvitationDetailActivity;
import com.example.pc.im2.controller.activity.SearchContactActivity;
import com.example.pc.im2.model.bean.UserInfo;
import com.example.pc.im2.utils.SPUtils;
import com.example.pc.im2.utils.UIUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PC on 2017/7/3.
 */

public class ContactFragment extends EaseContactListFragment {

    private ImageView redView;
    private LocalBroadcastManager manager;

    //接受到邀请的广播
    private BroadcastReceiver invitationReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //接收到广播，判断是否显示小红点
            isShowRedView();
        }
    };
    //接收到联系人发生了变化
    private BroadcastReceiver contactReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //添加了好友，或者删除了好友的时候
            //刷新本地数据
            refreshLocalContact();
        }
    };
    private List<UserInfo> contacts;//本地联系人列表

    @Override
    protected void initView() {
        super.initView();

        //点击联系人跳转到会话界面
        setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {

                //点击联系人，跳转，将用户数据传递
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,user.getUsername());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setUpView() {
        super.setUpView();

        //初始化ListView的头布局
        initHeadView();

        //设置标题栏右侧的加号
        titleBar.setRightImageResource(R.drawable.ease_blue_add);

        //设置点击加号的事件监听
        titleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击之后，跳转到添加好友的页面
                startActivity(new Intent(getActivity(),SearchContactActivity.class));
            }
        });

        //广播接收
        manager = LocalBroadcastManager.getInstance(getActivity());
        //注册广播接收器(接受有好友邀请消息的广播)
        manager.registerReceiver(invitationReciver,new IntentFilter(ContactValue.NEW_INVITE_CHANGE));
        //注册广播（接受到联系人发生了改变的广播）
        manager.registerReceiver(contactReciver,new IntentFilter(ContactValue.CONTACT_CHANGED));


        //显示联系人
        showContactList();

        //长按联系人，删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //因为position为0 的位置是headView，所以要判断
                if (position == 0){
                    return false;
                }

                showDelDialog(position);

                return true;
            }
        });

    }

    /**
     * 当长按联系人时，弹出一个确认框
     * @param position
     */
    private void showDelDialog(final int position) {
        new AlertDialog.Builder(getActivity())
                .setMessage("确定要删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除联系人
                        deleteContact(position);
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    //删除本地联系人
    private void deleteContact(final int position) {
        //开启线程
        Model.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    //1.网络
                    UserInfo userInfo = contacts.get(position - 1);
                    EMClient.getInstance().contactManager().deleteContact(userInfo.getHxid());

                    //2.本地
                    Model.getInstance().getDBManager().getContactDAO()
                            .deleteContactByHxId(userInfo.getHxid());
                    //3.内存页面
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshLocalContact();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 显示当前账号的联系人列表
     */
    private void showContactList() {
        //判断是否是第一次登陆，如果是第一次，从服务器获取联系人列表
        Boolean isGetDataForServer = (Boolean) SPUtils.get(getActivity(), "isGetDataForServer", false);
        if (isGetDataForServer){
            //从本地获取数据
            refreshLocalContact();
        } else {
            //从服务器获取数据并刷新
            refreshDataFromServer();
        }
    }

    /**
     * 从服务器获取数据
     */
    private void refreshDataFromServer() {

        //耗时操作，子线程
        Model.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //1.网络获取
                    List<String> contacts =
                            EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //Log.e("TAG","contacts=-=="+contacts);

                    //校验数据
                    if (contacts != null && contacts.size() > 0){
                        //2.本地保存
                        List<UserInfo> userinfos = new ArrayList<UserInfo>();
                        for (String contac:contacts) {
                            UserInfo userinfo = new UserInfo(contac,contac);
                            userinfos.add(userinfo);
                        }
                        //Log.e("TAG","userinfos==="+userinfos);

                        Model.getInstance().getDBManager().getContactDAO()
                                .saveContacts(userinfos,true);

                        //3.刷新内存和页面
                        UIUtils.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshLocalContact();
                            }
                        });

                        //4.从服务器获取过数据了，所以下次从本地获取
                        SPUtils.put(getActivity(),"isGetDataForServer",true);
                    }


                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //判断是否显示小红点
        isShowRedView();
    }

    /**
     * 判断当前用户是否有邀请，及是否显示小红点
     */
    private void isShowRedView() {
        //从数据库获取
        Boolean isShow = (Boolean) SPUtils.get(getActivity(), SPUtils.NEW_INVITATION, false);
        //是否显示小红点
        redView.setVisibility(isShow?View.VISIBLE:View.GONE);
    }

    /**
     * 初始化ListView头布局
     */
    private void initHeadView() {
        //获取头部的View
        View headView = View.inflate(getActivity(), R.layout.head_view,null);

        //获取头部View的小红点
        redView = (ImageView) headView.findViewById(R.id.contact_head_invitation_iv_redPoint);

        //将头部的View加入到ListView
        listView.addHeaderView(headView);

        //邀请好友的线性布局
        LinearLayout invitation = (LinearLayout) headView.findViewById(R.id.contact_head_invitation_ll);
        //群组的线性布局
        LinearLayout group = (LinearLayout) headView.findViewById(R.id.contact_head_group_ll);

        //上面2个布局的点击事件
        //点击邀请
        invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //UIUtils.showToast("邀请");
                //点击之后，跳转到好友邀请详情页面
                startActivity(new Intent(getActivity(), InvitationDetailActivity.class));
            }
        });

        //点击群组
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showToast("群组");
            }
        });

    }

    /**
     * 刷新本地联系人的数据
     */
    private void refreshLocalContact() {
        //1.从数据库获取
        contacts = Model.getInstance().getDBManager().getContactDAO().getContacts();
        //2.校验
        if (contacts != null){
            //3.准备数据
            Map<String, EaseUser> map = new HashMap<>();

            //转换数据
            for (UserInfo info : contacts) {
                map.put(info.getHxid(), new EaseUser(info.getUsername()));
            }
            //4.环信设置数据
            setContactsMap(map);
            getContactList();
            contactListLayout.refresh();//刷新
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //解除广播
        manager.unregisterReceiver(invitationReciver);
        manager.unregisterReceiver(contactReciver);
    }
}
