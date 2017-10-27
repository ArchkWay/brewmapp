package com.brewmapp.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.FilteredTitle;

import java.util.List;

public class FilterAdapter extends ArrayAdapter<FilteredTitle> {

    private Context context;
    private List<FilteredTitle> filterList;

    public FilterAdapter(Context context, List<FilteredTitle> filterList) {
        super(context, R.layout.list_filter_layout, filterList);
        this.context = context;
        this.filterList = filterList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilteredTitle title = filterList.get(position);
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            LayoutInflater inf = LayoutInflater.from(context);
            convertView = inf.inflate(R.layout.list_filter_layout, parent, false);
            holder.filterTitle = (TextView) convertView.findViewById(R.id.title_filter);
            holder.imgChecked = (ImageView) convertView.findViewById(R.id.img_checked);
            holder.lytFilter = (LinearLayout) convertView.findViewById(R.id.lyt_filter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (title.isSelected()) {
            holder.imgChecked.setVisibility(View.VISIBLE);
        } else {
            holder.imgChecked.setVisibility(View.INVISIBLE);
        }

        holder.filterTitle.setText(title.getTitle());
        return convertView;
    }

    private static class ViewHolder {
        TextView filterTitle;
        ImageView imgChecked;
        LinearLayout lytFilter;
    }
}