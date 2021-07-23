package com.motorbike.anqi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.my.ModifyAddressActivity;
import com.motorbike.anqi.bean.AddressBean;
import com.motorbike.anqi.view.CustomDialogView;

import java.util.List;

/**
 * Created by Administrator on 2018/4/25.
 */

public class SelectAddAdapter extends BaseAdapter {
    private Context context;
    private List<AddressBean> list;
    private Deladdress deladdress;
    private SetDefault setDefault;
    public SelectAddAdapter(Context context,Deladdress deladdress,SetDefault setDefault) {
        this.context = context;
        this.deladdress=deladdress;
        this.setDefault=setDefault;
    }

    public void setList(List<AddressBean> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.address_layout,parent,false);
            viewHolder.tvName=convertView.findViewById(R.id.tvName);
            viewHolder.tvPhone=convertView.findViewById(R.id.tvPhone);
            viewHolder.tvAddress=convertView.findViewById(R.id.tvAddress);
            viewHolder.tvEdit=convertView.findViewById(R.id.tvEdit);
            viewHolder.tvDel=convertView.findViewById(R.id.tvDel);
            viewHolder.llMoRen=convertView.findViewById(R.id.llMoRen);
            viewHolder.ivMoRen=convertView.findViewById(R.id.ivMoRen);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(list.get(position).getReceiver());
        viewHolder.tvPhone.setText(list.get(position).getPhone());
        viewHolder.tvAddress.setText(list.get(position).getAddress()+list.get(position).getAddressDetail());
        viewHolder.tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(list.get(position).getIdStr(),position);
            }
        });
        viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ModifyAddressActivity.class);
                intent.putExtra("name",list.get(position).getReceiver());
                intent.putExtra("phone",list.get(position).getPhone());
                intent.putExtra("address",list.get(position).getAddress());
                intent.putExtra("addressDetail",list.get(position).getAddressDetail());
                intent.putExtra("addressId",list.get(position).getIdStr());
                context.startActivity(intent);
            }
        });
        viewHolder.llMoRen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView tvName,tvPhone,tvAddress,tvEdit,tvDel;
        LinearLayout llMoRen;
        ImageView ivMoRen;
    }

    /**
     * 自定义dialog提示框
     *
     * @param
     */
    public void showAlertDialog(final String addressId, final int position)
    {
        final CustomDialogView dialog = new CustomDialogView(context);
        dialog.setTitle("提示信息");
        dialog.setMessage("确认删除该地址?");
        dialog.setYesOnclickListener("确定", new CustomDialogView.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                deladdress.delAddress(addressId,position);
                dialog.dismiss();

            }
        });
        dialog.setNoOnclickListener("取消", new CustomDialogView.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public interface Deladdress{
        void delAddress(String id,int position);
    }

    public interface SetDefault{
        void setDefault(String id,int position);
    }
}
