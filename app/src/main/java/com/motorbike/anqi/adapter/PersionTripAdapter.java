package com.motorbike.anqi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.motorbike.anqi.R;

import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 * 他人主页-行程adapter
 */

public class PersionTripAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    public PersionTripAdapter(Context context) {
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.persiontrip_layout,parent,false);
        }else {

        }
        return convertView;
    }

    public class ViewHolder{

    }
}
