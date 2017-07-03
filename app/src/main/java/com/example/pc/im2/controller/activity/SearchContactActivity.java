package com.example.pc.im2.controller.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pc.im2.R;
import com.example.pc.im2.base.BaseActivity;
import com.example.pc.im2.common.Model;
import com.example.pc.im2.utils.UIUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;

public class SearchContactActivity extends BaseActivity {

    @BindView(R.id.invite_btn_search)
    Button inviteBtnSearch;
    @BindView(R.id.invite_et_search)
    EditText inviteEtSearch;
    @BindView(R.id.invite_tv_username)
    TextView inviteTvUsername;
    @BindView(R.id.invite_btn_add)
    Button inviteBtnAdd;
    @BindView(R.id.invite_ll_item)
    LinearLayout inviteLlItem;
    private String userName;

    @Override
    public void initListener() {
        super.initListener();

        //搜索按钮的点击事件
        inviteBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //1.获取搜索框中的数据
                userName = inviteEtSearch.getText().toString().trim();
                //2.校验数据
                if (TextUtils.isEmpty(userName)) {
                    UIUtils.showToast("用户名不能为空");
                    return;
                }
                //3.数据OK，就搜索
                Model.getInstance().getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {

                        //3.1首先到自己的服务器查询用户信息
                        if (getUser()){

                            //3.2查询到此人，显示搜索结果
                            UIUtils.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    inviteLlItem.setVisibility(View.VISIBLE);
                                    //设置姓名为搜索的姓名
                                    inviteTvUsername.setText(userName);
                                }
                            });

                        } else {
                            //3.3用户不存在
                            UIUtils.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    UIUtils.showToast("用户不存在");
                                }
                            });
                        }
                    }
                });

            }
        });


        //点击添加给用户为好友的事件
        inviteBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EMClient.getInstance().contactManager()
                            .addContact(userName,"搜索添加");

                    UIUtils.showToast("添加成功");

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    UIUtils.showToast(e.getMessage());
                }
            }
        });

    }

    /**
     * 这里模拟从自己服务器获取用户的信息
     * @return
     */
    private boolean getUser() {
        return true;
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_contact;
    }

}
