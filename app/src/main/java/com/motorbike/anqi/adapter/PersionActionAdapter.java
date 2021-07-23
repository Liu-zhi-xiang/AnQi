package com.motorbike.anqi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.motorbike.anqi.R;

import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 * 他人主页-动态adapter
 */

public class PersionActionAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public PersionActionAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<String> list) {
        this.list = list;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.persionaction_layout,parent,false);
            viewHolder.rlBackGround=convertView.findViewById(R.id.rlBackGround);
            viewHolder.tvName=convertView.findViewById(R.id.tvName);
            viewHolder.tvDate=convertView.findViewById(R.id.tvDate);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }


        return convertView;
    }
    public  class ViewHolder{
        RelativeLayout rlBackGround;
        TextView tvName,tvDate;
    }
}
