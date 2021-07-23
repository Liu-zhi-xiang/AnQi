package com.motorbike.anqi.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.BrandBean;
import com.motorbike.anqi.bean.UserCarBean;
import com.motorbike.anqi.main.che.AddMotorcycleTypeActivity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/23.
 */

public class BrandAdapter extends BaseAdapter {
    private List<BrandBean> list;
    private Context context;

    public BrandAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<BrandBean> list) {
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
        ViewHoder hoder;
        if (convertView==null){
            hoder=new ViewHoder();
            convertView= LayoutInflater.from(context).inflate(R.layout.popu_list_item, parent, false);
            hoder.popu_list_item=convertView.findViewById(R.id.popu_list_item);
            hoder.add=convertView.findViewById(R.id.add);
            convertView.setTag(hoder);
        }else {
            hoder= (ViewHoder) convertView.getTag();
        }
        hoder.popu_list_item.setText(list.get(position).getCarBrand());


        return convertView;
    }
    class ViewHoder{
        TextView popu_list_item,add;
    }
}
