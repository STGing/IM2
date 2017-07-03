package com.example.pc.im2.controller.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.widget.RadioGroup;

import com.example.pc.im2.R;
import com.example.pc.im2.base.BaseActivity;
import com.example.pc.im2.controller.fragment.ContactFragment;
import com.example.pc.im2.controller.fragment.ConversationFragment;
import com.example.pc.im2.controller.fragment.SettingFragment;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.main_radioGroup)
    RadioGroup mainRadioGroup;
    private Fragment fragment;

    @Override
    public void initListener() {

        //监听RadioGroup
        mainRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                //根据RadioButton切换
                switchFragment(checkedId);
            }
        });
    }

    /**
     * 根据ID切换Fragment
     * @param checkedId
     */
    private void switchFragment(int checkedId) {
        switch (checkedId) {
            case R.id.main_radioButton_conversation:
                fragment = new ConversationFragment();
                break;
            case R.id.main_radioButton_contact:
                fragment = new ContactFragment();
                break;
            case R.id.main_radioButton_setting:
                fragment = new SettingFragment();
                break;
            default:
        
                break;
        }

        //选出来对应的fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout,fragment).commit();
    }

    @Override
    public void initData() {
        //默认选择第一个页面
        switchFragment(R.id.main_radioButton_conversation);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

}
