package com.motorbike.anqi.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.my.ModifyAddressActivity;
import com.motorbike.anqi.activity.trip.TripDetailActivity;
import com.motorbike.anqi.bean.AddressBean;
import com.motorbike.anqi.bean.RidingDataListBean;
import com.motorbike.anqi.view.CircleImageView;
import com.motorbike.anqi.view.CustomDialogView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/4/25.
 */

public class TripDetailOwnAdapter extends BaseAdapter {
    private Context context;
    private List<RidingDataListBean> list;
    private String type;
    public TripDetailOwnAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<RidingDataListBean> list) {
        this.list = list;
    }

    public void setType(String type) {
        this.type = type;
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
            convertView= LayoutInflater.from(context).inflate(R.layout.tripdetail_two_layout_item,parent,false);
            viewHolder.tvJiSu=convertView.findViewById(R.id.tvJiSu);
            viewHolder.tvJunSu=convertView.findViewById(R.id.tvJunSu);
            viewHolder.tvJiaSu=convertView.findViewById(R.id.tvJiaSu);
            viewHolder.tvJiaoDu=convertView.findViewById(R.id.tvJiaoDu);
            viewHolder.headImage=convertView.findViewById(R.id.headImage);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        if (list!=null){
            viewHolder.tvJiSu.setText(list.get(position).getExtreme());
            viewHolder.tvJunSu.setText(list.get(position).getAvgSpeed());
            viewHolder.tvJiaSu.setText(list.get(position).getAccelerate100km());
            viewHolder.tvJiaoDu.setText(list.get(position).getBend());
            if (!TextUtils.isEmpty(type)){
                if (type.equals("1")){
                    viewHolder.headImage.setVisibility(View.GONE);
                }else {
                    viewHolder.headImage.setVisibility(View.VISIBLE);
                }
            }
            if (!TextUtils.isEmpty(list.get(position).getHeaderImg())){
                Picasso.with(context)
                        .load(list.get(position).getHeaderImg())
                        .config(Bitmap.Config.RGB_565)
                        .centerCrop()
                        .fit()
                        .networkPolicy(NetworkPolicy.NO_STORE)
                        .into(viewHolder.headImage);
            }
        }

        return convertView;
    }
    class ViewHolder{
        TextView tvJiSu,tvJunSu,tvJiaSu,tvJiaoDu;
        LinearLayout llMoRen;
        CircleImageView headImage;
    }


}
