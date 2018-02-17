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
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.adapter.ModelViewHolder;

/**
 * Created by nixus on 07.12.2017.
 */

public class FilterBreweryField extends AbstractFlexibleItem<ModelViewHolder<FilterBreweryRowField>> {

    public static final int NAME = 0;
    public static final int COUNTRY = 1;
    public static final int BRAND = 2;
    public static final int TYPE_BEER = 3;
    public static final int CODE_CLICK_FILTER_START_SELECTION = 0;
    public static final int CODE_CLICK_FILTER_CLEAR = 1;
    public static final int CODE_CLICK_FILTER_ERROR = 2;

    private int id;
    private String selectedItemId;
    private int icon;
    private String title;
    private String selectedFilter;
    private boolean selected;

    private boolean isDirty =true;

    public FilterBreweryField(int id) {
        this.id = id;
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
        holder.view.setListener(((FlexibleModelAdapter) adapter).getListener());
        if(isDirty)
            resetToDefault(holder.view.getContext());
        holder.view.setModel(this);
    }

    private void resetToDefault(Context context) {
        isDirty =false;
        if(context!=null)
            switch (id) {
                case NAME:
                    icon = R.drawable.ic_resto_name;
                    title = context.getString(R.string.search_brewery_name);
                    selectedFilter = context.getString(R.string.search_brewery_name_defailt);
                    selectedItemId = null;
                    break;
                case COUNTRY:
                    icon = R.drawable.ic_country;
                    title = context.getString(R.string.search_beer_country);
                    selectedFilter = context.getString(R.string.search_beer_country_defailt);
                    selectedItemId = null;
                    break;
                case BRAND:
                    icon = R.drawable.ic_brand;
                    title = context.getString(R.string.search_beer_brand);
                    selectedFilter = context.getString(R.string.search_beer_brand_defailt);
                    selectedItemId = null;
                    break;
                case TYPE_BEER:
                    icon = R.drawable.ic_beer_type;
                    title = context.getString(R.string.search_beer_type);
                    selectedFilter = context.getString(R.string.search_beer_type_defailt);
                    selectedItemId = null;
                    break;
            }
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
        out.add(new FilterBreweryField(NAME));
        out.add(new FilterBreweryField(COUNTRY));
        out.add(new FilterBreweryField(BRAND));
        out.add(new FilterBreweryField(TYPE_BEER));
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

    public void clearFilter() {
        isDirty =true;
        setSelectedItemId(null);
    }
}
