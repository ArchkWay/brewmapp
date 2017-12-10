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
import ru.frosteye.ovsa.presentation.adapter.ModelViewHolder;

/**
 * Created by nixus on 25.11.2017.
 */

public class FilterBeerField extends AbstractFlexibleItem<ModelViewHolder<FilterBeerRowField>> {

    public static final int NAME = 0;
    public static final int COUNTRY = 1;
    public static final int TYPE = 2;
    public static final int BRAND = 3;
    public static final int PLACE = 4;
    public static final int POWER = 5;
    public static final int BEER_FILTER = 6;
    public static final int DENSITY = 7;
    public static final int IBU = 8;
    public static final int BEER_PACK = 9;
    public static final int COLOR = 10;
    public static final int SMELL = 11;
    public static final int TASTE = 12;
    public static final int AFTER_TASTE = 13;
    public static final int BREWERY = 14;
    public static final int PRICE_BEER = 15;

    private int id;
    private String selectedItemId;
    private int icon;
    private String title;
    private String selectedFilter;
    private boolean selected;

    public FilterBeerField(int id, int icon, String title, String selectedFilter) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.selectedFilter = selectedFilter;
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

    public static List<FilterBeerField> createDefault(Context context) {
        List<FilterBeerField> out = new ArrayList<>();
        out.add(new FilterBeerField(NAME, R.drawable.ic_resto_name, context.getString(R.string.search_beer_name), "Любое"));
        out.add(new FilterBeerField(COUNTRY, R.drawable.ic_country, context.getString(R.string.search_beer_country), "Не имеет значения"));
        out.add(new FilterBeerField(TYPE, R.drawable.ic_beer_type, context.getString(R.string.search_beer_type), "Любой"));
        out.add(new FilterBeerField(BRAND, R.drawable.ic_brand, context.getString(R.string.search_beer_brand), "Любой"));
        out.add(new FilterBeerField(PLACE, R.drawable.ic_city, context.getString(R.string.search_beer_place), "Не имеет значения"));
        out.add(new FilterBeerField(POWER, R.drawable.ic_power, context.getString(R.string.search_beer_power), "Любая"));
        out.add(new FilterBeerField(BEER_FILTER, R.drawable.ic_filter_beer, context.getString(R.string.search_beer_filter), "Все виды"));
        out.add(new FilterBeerField(DENSITY, R.drawable.ic_broj, context.getString(R.string.search_beer_type_broj), "Любой"));
        out.add(new FilterBeerField(IBU, R.drawable.ic_beer_ibu, context.getString(R.string.search_beer_ibu), "Любой"));
        out.add(new FilterBeerField(BEER_PACK, R.drawable.ic_bottle_beer, context.getString(R.string.search_beer_bootle), "Любой"));
        out.add(new FilterBeerField(COLOR, R.drawable.ic_color_beer, context.getString(R.string.search_beer_color), "Любой"));
        out.add(new FilterBeerField(SMELL, R.drawable.ic_beer, context.getString(R.string.search_beer_smell), "Любой"));
        out.add(new FilterBeerField(TASTE, R.drawable.ic_taste, context.getString(R.string.search_beer_taste), "Любой"));
        out.add(new FilterBeerField(AFTER_TASTE, R.drawable.ic_beer_aftertaste, context.getString(R.string.search_beer_after_taste), "Любое"));
        out.add(new FilterBeerField(BREWERY, R.drawable.ic_brewery, context.getString(R.string.search_beer_factory), "Не имеет значения"));
        out.add(new FilterBeerField(PRICE_BEER, R.drawable.ic_price_range, context.getString(R.string.search_beer_price), "Любая"));
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
