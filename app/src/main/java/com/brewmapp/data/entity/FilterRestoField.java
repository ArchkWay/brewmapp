package com.brewmapp.data.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.presentation.view.impl.widget.FilterRestoRowField;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.adapter.ModelViewHolder;

/**
 * Created by nlbochas on 28/10/2017.
 */

public class FilterRestoField extends AbstractFlexibleItem<ModelViewHolder<FilterRestoRowField>> {

    public static final int NAME = 0;
    public static final int TYPE = 1;
    public static final int BEER = 2;
    public static final int KITCHEN = 3;
    public static final int PRICE = 4;
    public static final int CITY = 5;
    public static final int METRO = 6;
    public static final int FEATURES = 7;

    public static final int REGION = 11;
    public static final int SELECTED_CITY = 12;

    public static final int CODE_CLICK_FILTER_START_SELECTION = 0;
    public static final int CODE_CLICK_FILTER_CLEAR = 1;
    public static final int CODE_CLICK_FILTER_ERROR = 3;

    private int id;
    private String selectedItemId;
    private int icon;
    private String title;
    private String selectedFilter;
    private boolean selected;


    public FilterRestoField(int id) {
        this.id = id;
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
        holder.view.setListener(((FlexibleModelAdapter) adapter).getListener());
        resetToDefault(holder.view.getContext());
        holder.view.setModel(this);
    }

    private void resetToDefault(Context context) {
        if(context!=null)
            switch (id){
                case NAME:
                    icon = R.drawable.ic_resto_name;
                    title = context.getString(R.string.search_resto_name);
                    if(selectedFilter==null)
                        selectedFilter = context.getString(R.string.search_resto_name_defailt);
                    break;
                case TYPE:
                    icon = R.drawable.ic_resto;
                    title = context.getString(R.string.search_resto_type);
                    if(selectedFilter==null)
                        selectedFilter = context.getString(R.string.search_resto_type_defailt);
                    break;
                case BEER:
                    icon = R.drawable.ic_beer;
                    title = context.getString(R.string.search_resto_beer);
                    if(selectedFilter==null)
                        selectedFilter = context.getString(R.string.search_resto_beer_defailt);
                    break;
                case KITCHEN:
                    icon = R.drawable.ic_kitchen;
                    title = context.getString(R.string.search_resto_kitchen);
                    if(selectedFilter==null)
                        selectedFilter = context.getString(R.string.search_resto_kitchen_defailt);
                    break;
                case PRICE:
                    icon = R.drawable.ic_price_range;
                    title = context.getString(R.string.search_resto_price);
                    if(selectedFilter==null)
                        selectedFilter = context.getString(R.string.search_resto_price_default);
                    break;
                case CITY:
                    icon = R.drawable.ic_city;
                    title = context.getString(R.string.search_resto_city);
                    if(selectedFilter==null)
                        selectedFilter = context.getString(R.string.search_resto_city_default);
                    break;
                case METRO:
                    icon = R.drawable.ic_metro;
                    title = context.getString(R.string.search_resto_metro);
                    if(selectedFilter==null)
                        selectedFilter = context.getString(R.string.search_resto_metro_default);
                    break;
                case FEATURES:
                    icon = R.drawable.ic_feature;
                    title = context.getString(R.string.search_resto_other);
                    if(selectedFilter==null)
                        selectedFilter = context.getString(R.string.search_resto_other_default);
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

    public static List<FilterRestoField> createDefault() {
        List<FilterRestoField> out = new ArrayList<>();
        out.add(new FilterRestoField(NAME));
        out.add(new FilterRestoField(BEER));
        out.add(new FilterRestoField(TYPE));
        out.add(new FilterRestoField(KITCHEN));
        out.add(new FilterRestoField(PRICE));
        out.add(new FilterRestoField(CITY));
        out.add(new FilterRestoField(METRO));
        out.add(new FilterRestoField(FEATURES));
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
        selectedItemId=null;
        selectedFilter=null;
    }

}
