package com.motorbike.anqi.activity.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.baidu.location.Poi;
import com.baidu.mapapi.search.core.PoiInfo;
import com.motorbike.anqi.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class SearchResultAdapter extends BaseAdapter {

    private List<PoiInfo> data;
    private Context context;

    private int selectedPosition = -1;

    public SearchResultAdapter(Context context){
        this.context = context;
        data = new ArrayList<>();
    }

    public void setData(List<PoiInfo> data) {
        this.data = data;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @Override
    public int getCount() {
        if (data!=null){
            return data.size();
        }else {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_holder_result, parent, false);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bindView(position);

        return convertView;
    }


    class ViewHolder {
        TextView textTitle;
        TextView textSubTitle;
        ImageView imageCheck;

        public ViewHolder(View view)
        {
            textTitle = (TextView) view.findViewById(R.id.text_title);
            textSubTitle = (TextView) view.findViewById(R.id.text_title_sub);
            imageCheck = (ImageView) view.findViewById(R.id.image_check);
        }

        public void bindView(int position) {
            if (position >= data.size())
                return;

            PoiInfo poiItem = data.get(position);

            textTitle.setText(poiItem.name);
            textSubTitle.setText(poiItem.address);

            imageCheck.setVisibility(position == selectedPosition ? View.VISIBLE : View.INVISIBLE);
//            textSubTitle.setVisibility((position == 0 && poiItem.uid.equals("regeo")) ? View.GONE : View.VISIBLE);
        }
    }
}
