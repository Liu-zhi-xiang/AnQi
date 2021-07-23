package com.motorbike.anqi.main.che;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.MediaBean;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;




public class GridTwoAdapter extends BaseAdapter {
	private List<MediaBean> mList;
	private LayoutInflater inflater;
	private int mScreenWidth;
	private Context context;
	private static  int maxSize=9;

	public GridTwoAdapter(Context context, int maxSize, List<MediaBean> mList) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.maxSize=9;
		this.mList = mList;
	}

	public void setmList(List<MediaBean> mList) {
		this.mList = mList;

	}

	public int getCount() {

		if (mList!=null){
			if(mList.size() == maxSize){
				return maxSize;
			}
			return (mList.size()+1 );
		}
			return 1;

	}

	public MediaBean getItem(int arg0) {
		return mList.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

			convertView = inflater.inflate(R.layout.item_published_grida,
					parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.item_grida_image);

			if (position < mList.size()){
				Log.e("Aaaaa", "apapter" + position);
				MediaBean mediaBean = mList.get(position);
				Uri path = null;
					path = mediaBean.getUri();
					if (path!=null) {
//				Picasso.with(context).invalidate(new File(path.toString()));
				Picasso.with(context)
						.load(path)
						.config(Bitmap.Config.RGB_565)
						.centerCrop()
						.fit()
						.networkPolicy(NetworkPolicy.NO_STORE)
						.into(holder.image);
					}

			}
		Log.e("aaaa","position=="+position+"size"+mList.size());
		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}


}