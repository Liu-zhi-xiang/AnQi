package com.motorbike.anqi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.my.UserCenterActivity;
import com.motorbike.anqi.bean.GoodslistBean;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/1/29.
 */

public class IntegralMallAdapter extends BaseAdapter {
    private Context context;
    private List<GoodslistBean> list;
    public IntegralMallAdapter(Context context) {
        this.context = context;
    }
    public void setList(List<GoodslistBean> list){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.integralmall_item_layout,parent,false);
            viewHolder.ivImage=convertView.findViewById(R.id.ivImage);
            viewHolder.tvName=convertView.findViewById(R.id.tvName);
            viewHolder.tvJiFen=convertView.findViewById(R.id.tvJiFen);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(list.get(position).getGoodsName());
        viewHolder.tvJiFen.setText(list.get(position).getPointNum());
        Picasso.with(context)
                .load(list.get(position).getGoodsImg())
                .config(Bitmap.Config.RGB_565)
                .centerCrop()
                .fit()
                .networkPolicy(NetworkPolicy.NO_STORE)
                .into(viewHolder.ivImage);
        return convertView;
    }
    public class ViewHolder{
        ImageView ivImage;
        TextView tvName,tvJiFen;
    }
}
