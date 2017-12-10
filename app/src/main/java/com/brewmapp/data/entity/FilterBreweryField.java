package com.brewmapp.data.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.brewmapp.R;
import com.brewmapp.presentation.view.impl.widget.FilterBreweryRowField;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import ru.frosteye.ovsa.presentation.adapter.ModelViewHolder;

/**
 * Created by nixus on 07.12.2017.
 */

public class FilterBreweryField extends AbstractFlexibleItem<ModelViewHolder<FilterBreweryRowField>> {

    public static final int NAME = 0;
    public static final int COUNTRY = 1;
    public static final int BRAND = 2;
    public static final int TYPE_BEER = 3;

    private int id;
    private String selectedItemId;
    private int icon;
    private String title;
    private String selectedFilter;
    private boolean selected;

    public FilterBreweryField(int id, int icon, String title, String selectedFilter) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.selectedFilter = selectedFilter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterBreweryField filterBreweryField = (FilterBreweryField) o;
        return id == filterBreweryField.id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_brewery_filtered;
    }

    @Override
    public ModelViewHolder<FilterBreweryRowField> createViewHolder(FlexibleAdapter adapter,
                                                                LayoutInflater inflater,
                                                                ViewGroup parent) {
        return new ModelViewHolder<>(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ModelViewHolder<FilterBreweryRowField> holder,
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

    public static List<FilterBreweryField> createDefault(Context context) {
        List<FilterBreweryField> out = new ArrayList<>();
        out.add(new FilterBreweryField(NAME, R.drawable.ic_resto_name, context.getString(R.string.search_brewery_name), "Любое"));
        out.add(new FilterBreweryField(COUNTRY, R.drawable.ic_country, context.getString(R.string.search_beer_country), "Любая"));
        out.add(new FilterBreweryField(BRAND, R.drawable.ic_brand, context.getString(R.string.search_beer_brand), "Любой"));
        out.add(new FilterBreweryField(TYPE_BEER, R.drawable.ic_beer_type, context.getString(R.string.search_beer_type), "Любой"));
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
