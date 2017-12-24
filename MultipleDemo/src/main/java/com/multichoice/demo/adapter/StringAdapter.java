package com.multichoice.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multichoice.demo.R;

import java.util.List;

/**
 * Created by Administrator on 2017/12/24.
 */

public class StringAdapter extends BaseAdapterImpl<String> {
    public StringAdapter(Context context, List<String> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.simple_file_list_item, parent, false);
        }
        TextView view = (TextView) convertView.findViewById(R.id.tv_name);
        view.setText(getItem(position));
        return convertView;
    }
}
