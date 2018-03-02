package com.brewmapp.data.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.brewmapp.R;
import com.brewmapp.presentation.view.impl.widget.FilterBeerRowField;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.adapter.ModelViewHolder;

/**
 * Created by nixus on 25.11.2017.
 */

public class FilterBeerField extends AbstractFlexibleItem<ModelViewHolder<FilterBeerRowField>> {

    public static final int NAME = 0;
    public static final int COUNTRY = 1;
    public static final int TYPE = 2;
    public static final int BRAND = 3;
    public static final int POWER = 4;
    public static final int BEER_FILTER = 5;
    public static final int DENSITY = 6;
    public static final int IBU = 7;
    public static final int BEER_PACK = 8;
    public static final int COLOR = 9;
    public static final int SMELL = 10;
    public static final int TASTE = 11;
    public static final int AFTER_TASTE = 12;
    public static final int BREWERY = 13;
    public static final int PRICE_BEER = 14;

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

    public FilterBeerField(int id) {
        this.id = id;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterBeerField filterBeerField = (FilterBeerField) o;
        return id == filterBeerField.id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_beer_filter;
    }

    @Override
    public ModelViewHolder<FilterBeerRowField> createViewHolder(FlexibleAdapter adapter,
                                                                LayoutInflater inflater,
                                                                ViewGroup parent) {
        return new ModelViewHolder<>(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ModelViewHolder<FilterBeerRowField> holder,
                               int position, List payloads) {
        holder.view.setListener(((FlexibleModelAdapter) adapter).getListener());
        if(isDirty)
            resetToDefault(holder.view.getContext());
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

    public static List<FilterBeerField> createDefault() {
        List<FilterBeerField> out = new ArrayList<>();
        out.add(new FilterBeerField(NAME));
        out.add(new FilterBeerField(COUNTRY));
        out.add(new FilterBeerField(TYPE));
        out.add(new FilterBeerField(BRAND));
        out.add(new FilterBeerField(POWER));
        out.add(new FilterBeerField(BEER_FILTER));
        out.add(new FilterBeerField(DENSITY));
        out.add(new FilterBeerField(IBU));
        out.add(new FilterBeerField(BEER_PACK));
        out.add(new FilterBeerField(COLOR));
        out.add(new FilterBeerField(SMELL));
        out.add(new FilterBeerField(TASTE));
        out.add(new FilterBeerField(AFTER_TASTE));
        out.add(new FilterBeerField(BREWERY));
        out.add(new FilterBeerField(PRICE_BEER));
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

    private void resetToDefault(Context context) {
        isDirty =false;
        if(context!=null)
            switch (id){
                case NAME:
                    icon = R.drawable.ic_resto_name;
                    title = context.getString(R.string.search_beer_name);
                    selectedFilter = context.getString(R.string.search_beer_name_defailt);
                    selectedItemId=null;
                    break;
                case COUNTRY:
                    icon = R.drawable.ic_country;
                    title = context.getString(R.string.search_beer_country);
                    selectedFilter = context.getString(R.string.search_beer_country_defailt);
                    selectedItemId=null;
                    break;
                case TYPE:
                    icon = R.drawable.ic_beer_type;
                    title = context.getString(R.string.search_beer_type);
                    selectedFilter = context.getString(R.string.search_beer_type_defailt);
                    selectedItemId=null;
                    break;
                case BRAND:
                    icon = R.drawable.ic_brand;
                    title = context.getString(R.string.search_beer_brand);
                    selectedFilter = context.getString(R.string.search_beer_brand_defailt);
                    selectedItemId=null;
                    break;
                case POWER:
                    icon = R.drawable.ic_power;
                    title = context.getString(R.string.search_beer_power);
                    selectedFilter = context.getString(R.string.search_beer_power_defailt);
                    selectedItemId=null;
                    break;
                case BEER_FILTER:
                    icon = R.drawable.ic_filter_beer;
                    title = context.getString(R.string.search_beer_filter);
                    selectedFilter = context.getString(R.string.search_beer_filter_defailt);
                    selectedItemId=null;
                    break;
                case DENSITY:
                    icon = R.drawable.ic_broj;
                    title = context.getString(R.string.search_beer_type_broj);
                    selectedFilter = context.getString(R.string.search_beer_type_broj_defailt);
                    selectedItemId=null;
                    break;
                case IBU:
                    icon = R.drawable.ic_beer_ibu;
                    title = context.getString(R.string.search_beer_ibu);
                    selectedFilter = context.getString(R.string.search_beer_ibu_defailt);
                    selectedItemId=null;
                    break;
                case BEER_PACK:
                    icon = R.drawable.ic_bottle_beer;
                    title = context.getString(R.string.search_beer_bootle);
                    selectedFilter = context.getString(R.string.search_beer_bootle_defailt);
                    selectedItemId=null;
                    break;
                case COLOR:
                    icon = R.drawable.ic_color_beer;
                    title = context.getString(R.string.search_beer_color);
                    selectedFilter = context.getString(R.string.search_beer_color_defailt);
                    selectedItemId=null;
                    break;
                case SMELL:
                    icon = R.drawable.ic_beer;
                    title = context.getString(R.string.search_beer_smell);
                    selectedFilter = context.getString(R.string.search_beer_smell_defailt);
                    selectedItemId=null;
                    break;
                case TASTE:
                    icon = R.drawable.ic_taste;
                    title = context.getString(R.string.search_beer_taste);
                    selectedFilter = context.getString(R.string.search_beer_taste_defailt);
                    selectedItemId=null;
                    break;
                case AFTER_TASTE:
                    icon = R.drawable.ic_beer_aftertaste;
                    title = context.getString(R.string.search_beer_after_taste);
                    selectedFilter = context.getString(R.string.search_beer_after_taste_defailt);
                    selectedItemId=null;
                    break;
                case BREWERY:
                    icon = R.drawable.ic_brewery;
                    title = context.getString(R.string.search_beer_factory);
                    selectedFilter = context.getString(R.string.search_beer_factory_defailt);
                    selectedItemId=null;
                    break;
                case PRICE_BEER:
                    icon = R.drawable.ic_price_range;
                    title = context.getString(R.string.search_beer_price);
                    selectedFilter = context.getString(R.string.search_beer_price_defailt);
                    selectedItemId=null;
                    break;
            }

    }

}
