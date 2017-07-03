package com.example.pc.im2.controller.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.pc.im2.R;
import com.example.pc.im2.controller.activity.SearchContactActivity;
import com.example.pc.im2.utils.UIUtils;
import com.hyphenate.easeui.ui.EaseContactListFragment;

/**
 * Created by PC on 2017/7/3.
 */

public class ContactFragment extends EaseContactListFragment {

    @Override
    protected void initView() {
        super.initView();
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
    }

    /**
     * 初始化ListView头布局
     */
    private void initHeadView() {
        //获取头部的View
        View headView = View.inflate(getActivity(), R.layout.head_view,null);

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
               UIUtils.showToast("邀请");
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
}
