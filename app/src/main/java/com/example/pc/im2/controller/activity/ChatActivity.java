package com.example.pc.im2.controller.activity;

import android.widget.FrameLayout;

import com.example.pc.im2.R;
import com.example.pc.im2.base.BaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;

import butterknife.BindView;

public class ChatActivity extends BaseActivity {

    @BindView(R.id.chat_fl)
    FrameLayout chatFl;

    @Override
    public void initData() {
        EaseChatFragment chatFragment = new EaseChatFragment();
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.chat_fl,chatFragment).commit();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }


}
