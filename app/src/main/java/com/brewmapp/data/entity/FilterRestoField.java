package com.brewmapp.data.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.brewmapp.R;
import com.brewmapp.presentation.view.impl.widget.FilterRestoRowField;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import ru.frosteye.ovsa.presentation.adapter.ModelViewHolder;

/**
 * Created by nlbochas on 28/10/2017.
 */

public class FilterRestoField extends AbstractFlexibleItem<ModelViewHolder<FilterRestoRowField>> {

    public static final int NAME = 0;
    public static final int TYPE = 1;
    public static final int BEER = 2;
    public static final int KITCHEN = 3;
    public static final int AVERAGE_BILL = 4;
    public static final int CITY = 5;
    public static final int METRO = 6;
    public static final int OTHER = 7;

    private int id;
    private String selectedItemId;
    private int icon;
    private String title;
    private String selectedFilter;
    private boolean selected;

    public FilterRestoField(int id, int icon, String title, String selectedFilter) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.selectedFilter = selectedFilter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterRestoField filterRestoField = (FilterRestoField) o;

        return id == filterRestoField.id;

    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_resto_filter;
    }

    @Override
    public ModelViewHolder<FilterRestoRowField> createViewHolder(FlexibleAdapter adapter,
                                                                 LayoutInflater inflater,
                                                                 ViewGroup parent) {
        return new ModelViewHolder<>(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ModelViewHolder<FilterRestoRowField> holder,
                               int position, List payloads) {
        holder.view.setModel(this);
    }

    public static void unselectAll(List<MenuField> fields) {
        for(MenuField field: fields) {
            field.setSelected(false);
        }
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getId() {
        return id;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public void setSelectedFilter(String selectedFilter) {
        this.selectedFilter = selectedFilter;
    }

    public static List<FilterRestoField> createDefault(Context context) {
        List<FilterRestoField> out = new ArrayList<>();
        out.add(new FilterRestoField(NAME, R.drawable.ic_resto_name, context.getString(R.string.search_resto_name), "Любое"));
        out.add(new FilterRestoField(TYPE, R.drawable.ic_resto, context.getString(R.string.search_resto_type), "Любой"));
        out.add(new FilterRestoField(BEER, R.drawable.ic_beer, context.getString(R.string.search_resto_beer), "Любое"));
        out.add(new FilterRestoField(KITCHEN, R.drawable.ic_kitchen, context.getString(R.string.search_resto_kitchen), "Любая"));
        out.add(new FilterRestoField(AVERAGE_BILL, R.drawable.ic_price_range, context.getString(R.string.search_resto_price), "Не имеет значения"));
        out.add(new FilterRestoField(CITY, R.drawable.ic_city, context.getString(R.string.search_resto_city), "Москва"));
        out.add(new FilterRestoField(METRO, R.drawable.ic_metro, context.getString(R.string.search_resto_metro), "Не имеет значения"));
        out.add(new FilterRestoField(OTHER, R.drawable.ic_feature, context.getString(R.string.search_resto_other), "Не имеют значения"));
        return out;
    }

    public String getSelectedFilter() {
        return selectedFilter;
    }

    public String getSelectedItemId() {
        return selectedItemId;
    }

    public void setSelectedItemId(String selectedItemId) {
        this.selectedItemId = selectedItemId;
    }
}