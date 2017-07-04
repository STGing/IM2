package com.example.pc.im2.controller.activity;

import android.widget.ListView;

import com.example.pc.im2.R;
import com.example.pc.im2.base.BaseActivity;
import com.example.pc.im2.common.Model;
import com.example.pc.im2.controller.adapter.InvitationAdapter;
import com.example.pc.im2.model.bean.InvitationInfo;
import com.example.pc.im2.utils.SPUtils;
import com.example.pc.im2.utils.UIUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import butterknife.BindView;

public class InvitationDetailActivity extends BaseActivity {

    @BindView(R.id.lv_invite)
    ListView lvInvite;

    private InvitationAdapter adapter;

    @Override
    public void initData() {

        //设置小红点的状态
        SPUtils.put(this,SPUtils.NEW_INVITATION,false);

        //listView的设置
        adapter = new InvitationAdapter(this);
        lvInvite.setAdapter(adapter);

        //设置适配器数据
        setAdapterData();

        //适配器中2个按钮的监听事件
        adapter.setOnInvitationListener(new InvitationAdapter.OnInvitationListener() {
            @Override
            public void onAccept(final InvitationInfo info) {
                //点击了接受邀请按钮的回调
                //数据处理：网络/本地/内存页面
                Model.getInstance().getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            //1.网络通知环信：接受了好友申请
                            String hxid = info.getUserInfo().getHxid();//获取用户的环信ID
                            EMClient.getInstance().contactManager().acceptInvitation(hxid);

                            //2.本地，因为接受邀请，所以改变数据库中的状态
                            Model.getInstance().getDBManager().getInvitationDAO()
                                    .updateInvitationStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT, hxid);

                            //3.改变内存和网页(刷新数据)
                            UIUtils.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    setAdapterData();
                                }
                            });
                            //4.提示
                            UIUtils.showToast("添加成功");
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onDeny(final InvitationInfo info) {
                //点击了拒绝邀请按钮的回调
                //数据处理：网络/本地/内存页面
                Model.getInstance().getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            //1.网络通知环信：接受了好友申请
                            String hxid = info.getUserInfo().getHxid();//获取用户的环信ID
                            EMClient.getInstance().contactManager().declineInvitation(hxid);

                            //2.本地，因为拒绝邀请，所以删除掉数据库中保存的
                            Model.getInstance().getDBManager().getInvitationDAO()
                                    .removeInvitation(hxid);

                            //3.改变内存和网页(刷新数据)
                            UIUtils.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    setAdapterData();
                                }
                            });

                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /**
     * 设置适配器数据
     * 从本地数据库获取数据
     */
    private void setAdapterData() {

        //因为是数据查询操作，开线程
        Model.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //获取数据
               final List<InvitationInfo> invitations = Model.getInstance().getDBManager().getInvitationDAO()
                        .getInvitations();
                //校验
                if (invitations != null){
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.refreshData(invitations);
                        }
                    });

                }

            }
        });

    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_invitation_detail;
    }
}
