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
    public static final int CITY = 15;

    public static final int CODE_CLICK_FILTER_START_SELECTION = 0;
    public static final int CODE_CLICK_FILTER_CLEAR = 1;
    public static final int CODE_CLICK_FILTER_ERROR = 2;

    private int id;
    private String selectedItemId;
    private int icon;
    private String title;
    private String selectedFilter;
    private boolean selected;

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
        out.add(new FilterBeerField(CITY));
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
        setSelectedItemId(null);
    }

    private void resetToDefault(Context context) {
        if(context!=null)
            switch (id){
                case NAME:
                    icon = R.drawable.ic_resto_name;
                    title = context.getString(R.string.search_beer_name);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_beer_name_defailt);
                    break;
                case COUNTRY:
                    icon = R.drawable.ic_country;
                    title = context.getString(R.string.search_beer_country);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_beer_country_defailt);
                    break;
                case TYPE:
                    icon = R.drawable.ic_beer_type;
                    title = context.getString(R.string.search_beer_type);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_beer_type_defailt);
                    break;
                case BRAND:
                    icon = R.drawable.ic_brand;
                    title = context.getString(R.string.search_beer_brand);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_beer_brand_defailt);
                    break;
                case POWER:
                    icon = R.drawable.ic_power;
                    title = context.getString(R.string.search_beer_power);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_beer_power_defailt);
                    break;
                case BEER_FILTER:
                    icon = R.drawable.ic_filter_beer;
                    title = context.getString(R.string.search_beer_filter);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_beer_filter_defailt);
                    break;
                case DENSITY:
                    icon = R.drawable.ic_broj;
                    title = context.getString(R.string.search_beer_type_broj);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_beer_type_broj_defailt);
                    break;
                case IBU:
                    icon = R.drawable.ic_beer_ibu;
                    title = context.getString(R.string.search_beer_ibu);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_beer_ibu_defailt);
                    break;
                case BEER_PACK:
                    icon = R.drawable.ic_bottle_beer;
                    title = context.getString(R.string.search_beer_bootle);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_beer_bootle_defailt);
                    break;
                case COLOR:
                    icon = R.drawable.ic_color_beer;
                    title = context.getString(R.string.search_beer_color);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_beer_color_defailt);
                    break;
                case SMELL:
                    icon = R.drawable.ic_beer;
                    title = context.getString(R.string.search_beer_smell);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_beer_smell_defailt);
                    break;
                case TASTE:
                    icon = R.drawable.ic_taste;
                    title = context.getString(R.string.search_beer_taste);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_beer_taste_defailt);
                    break;
                case AFTER_TASTE:
                    icon = R.drawable.ic_beer_aftertaste;
                    title = context.getString(R.string.search_beer_after_taste);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_beer_after_taste_defailt);
                    break;
                case BREWERY:
                    icon = R.drawable.ic_brewery;
                    title = context.getString(R.string.search_beer_factory);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_beer_factory_defailt);
                    break;
                case PRICE_BEER:
                    icon = R.drawable.ic_price_range;
                    title = context.getString(R.string.search_beer_price);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_beer_price_defailt);
                    break;
                case CITY:
                    icon = R.drawable.ic_city;
                    title = context.getString(R.string.search_resto_city);
                    if(selectedItemId==null)
                        selectedFilter = context.getString(R.string.search_resto_city_default);
                    break;

            }

    }

}
