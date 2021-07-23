package com.motorbike.anqi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.ExchangeBean;
import com.motorbike.anqi.bean.RecordBean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/29.
 */

public class IntegralRecordAdapter extends BaseAdapter {
    private Context context;
    private List<ExchangeBean> list;

    public IntegralRecordAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<ExchangeBean> list) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.integralrecord_item_layout,parent,false);
            viewHolder.tvDate=convertView.findViewById(R.id.tvDate);
            viewHolder.tvJiFen=convertView.findViewById(R.id.tvJiFen);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tvDate.setText(list.get(position).getOrderCreateTime());
        viewHolder.tvJiFen.setText(list.get(position).getOrderPoint());
        return convertView;
    }
    public class ViewHolder{
        TextView tvDate,tvJiFen;
    }
}
