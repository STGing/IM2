package com.example.pc.im2.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.pc.im2.R;
import com.example.pc.im2.model.bean.InvitationInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PC on 2017/7/4.
 */

public class InvitationAdapter extends BaseAdapter {

    private final Context context;

    private List<InvitationInfo> list = new ArrayList<>();

    public InvitationAdapter(Context context) {
        this.context = context;
    }

    public void refreshData(List<InvitationInfo> list) {
        //数据校验
        if (list != null) {
            this.list.clear();
            this.list.addAll(list);
            this.notifyDataSetChanged();
        }

    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.invitation_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //处理数据
        final InvitationInfo info = list.get(position);
        //设置用户名
        viewHolder.tvInviteName.setText(info.getUserInfo().getUsername());
        //默认设置 接收 和 拒绝 按钮隐藏
        viewHolder.btInviteAccept.setVisibility(View.GONE);
        viewHolder.btInviteReject.setVisibility(View.GONE);

        //根据不同的状态来设置不同的样式
        InvitationInfo.InvitationStatus status = info.getStatus();

        switch (status) {
            case INVITE_ACCEPT://接受了的邀请
                //根据理由来判断
                if (info.getReason() == null){
                   //如果没有理由
                    viewHolder.tvInviteReason.setText("接受邀请");
                } else {
                    //如果有理由，显示
                    viewHolder.tvInviteReason.setText(info.getReason());
                }

                break;
            case NEW_INVITE://新邀请
                //根据理由来判断
                if (info.getReason() == null){
                    //如果没有理由
                    viewHolder.tvInviteReason.setText("新的邀请");
                } else {
                    //如果有理由，显示
                    viewHolder.tvInviteReason.setText(info.getReason());
                }

                //因为是新邀请，所以要显示接受和拒绝按钮
                viewHolder.btInviteAccept.setVisibility(View.VISIBLE);
                viewHolder.btInviteReject.setVisibility(View.VISIBLE);

                //2个按钮的点击事件

                //接受的事件
                viewHolder.btInviteAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onAccept(info);
                    }
                });

                //拒绝的事件
                viewHolder.btInviteReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onDeny(info);
                    }
                });

                break;
            case INVITE_ACCEPT_BY_PEER://邀请被接受

                //根据理由来判断
                if (info.getReason() == null){
                    //如果没有理由
                    viewHolder.tvInviteReason.setText("发送的邀请被接受");
                } else {
                    //如果有理由，显示
                    viewHolder.tvInviteReason.setText(info.getReason());
                }

                break;
            default:

                break;
        }


        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_invite_name)
        TextView tvInviteName;
        @BindView(R.id.tv_invite_reason)
        TextView tvInviteReason;
        @BindView(R.id.bt_invite_accept)
        Button btInviteAccept;
        @BindView(R.id.bt_invite_reject)
        Button btInviteReject;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 通过接口将数据暴露出去
     */
    //接口实现类
    private OnInvitationListener listener;
    //接口
    public interface OnInvitationListener{
        //接受邀请
        void onAccept(InvitationInfo info);
        //拒绝邀请
        void onDeny(InvitationInfo info);
    }
    //监听方法
    public void setOnInvitationListener(OnInvitationListener listener){
        this.listener = listener;
    }
}
