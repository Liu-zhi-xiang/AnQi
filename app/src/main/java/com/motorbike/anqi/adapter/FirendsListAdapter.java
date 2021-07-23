package com.motorbike.anqi.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.my.OtherInfoActivity;
import com.motorbike.anqi.activity.my.PersionDetailActivity;
import com.motorbike.anqi.activity.voice.AutofriendsListActivity;
import com.motorbike.anqi.bean.MemberListBean;
import com.motorbike.anqi.bean.OtherInfoBean;
import com.motorbike.anqi.fragment.OnlyUserFragment;
import com.motorbike.anqi.view.CircleImageView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class FirendsListAdapter extends BaseAdapter {
    private Context context;
    private List<MemberListBean> list;
    public FirendsListAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<MemberListBean> list){
        this.list=list;
    }
    @Override
    public int getCount() {
        if (list!=null)
            return list.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.firendslist_item_layout,parent,false);
            viewHolder.tvName=convertView.findViewById(R.id.tvName);
            viewHolder.tvLine=convertView.findViewById(R.id.tvLine);
            viewHolder.headImage=convertView.findViewById(R.id.headImage);
            viewHolder.llYou=convertView.findViewById(R.id.llYou);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        if (list!=null){
            viewHolder.tvName.setText(list.get(position).getNickname());
            Picasso.with(context)
                    .load(list.get(position).getHeaderImg())
                    .config(Bitmap.Config.RGB_565)
                    .centerCrop()
                    .fit()
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .into(viewHolder.headImage);
        }
        if (position==list.size()){
            viewHolder.tvLine.setVisibility(View.GONE);
        }
        viewHolder.llYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OtherInfoActivity.class);
                intent.putExtra("friendId",list.get(position).getFriendId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder{
        TextView tvName,tvLine;
        CircleImageView headImage;
        LinearLayout llYou;
    }
}
