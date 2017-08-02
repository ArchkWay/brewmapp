package ru.frosteye.beermap.data.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import ru.frosteye.beermap.R;
import ru.frosteye.beermap.presentation.view.impl.widget.MenuFieldRow;
import ru.frosteye.ovsa.presentation.adapter.ModelViewHolder;

/**
 * Created by ovcst on 02.08.2017.
 */

public class MenuField extends AbstractFlexibleItem<ModelViewHolder<MenuFieldRow>> {

    public static final int EVENTS = 0;
    public static final int MESSAGES = 1;
    public static final int SEARCH = 2;
    public static final int MEETINGS = 3;
    public static final int MAP = 4;
    public static final int FRIENDS = 5;
    public static final int GROUPS = 6;
    public static final int GAMES = 7;
    public static final int FAVORITES = 8;
    public static final int SETTINGS = 9;

    private int id;
    private int icon;
    private String title;

    public MenuField(int id, int icon, String title) {
        this.id = id;
        this.icon = icon;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuField menuField = (MenuField) o;

        return id == menuField.id;

    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_menu_field;
    }

    @Override
    public ModelViewHolder<MenuFieldRow> createViewHolder(FlexibleAdapter adapter,
                                                          LayoutInflater inflater,
                                                          ViewGroup parent) {
        return new ModelViewHolder<>(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ModelViewHolder<MenuFieldRow> holder,
                               int position, List payloads) {
        holder.view.setModel(this);
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

    public static List<MenuField> createDefault(Context context) {
        List<MenuField> out = new ArrayList<>();
        out.add(new MenuField(EVENTS, R.drawable.ic_menu_events, context.getString(R.string.events)));
        out.add(new MenuField(EVENTS, R.drawable.ic_menu_messages, context.getString(R.string.messages)));
        out.add(new MenuField(EVENTS, R.drawable.ic_menu_search, context.getString(R.string.serach)));
        out.add(new MenuField(EVENTS, R.drawable.ic_menu_meetings, context.getString(R.string.meetings)));
        out.add(new MenuField(EVENTS, R.drawable.ic_menu_map, context.getString(R.string.map)));
        out.add(new MenuField(EVENTS, R.drawable.ic_menu_friends, context.getString(R.string.friends)));
        out.add(new MenuField(EVENTS, R.drawable.ic_menu_groups, context.getString(R.string.groups)));
        out.add(new MenuField(EVENTS, R.drawable.ic_menu_games, context.getString(R.string.games)));
        out.add(new MenuField(EVENTS, R.drawable.ic_menu_favorites, context.getString(R.string.favorites)));
        out.add(new MenuField(EVENTS, R.drawable.ic_menu_settings, context.getString(R.string.settings)));


        return out;
    }
}
