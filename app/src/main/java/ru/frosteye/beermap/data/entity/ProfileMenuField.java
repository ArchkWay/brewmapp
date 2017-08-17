package ru.frosteye.beermap.data.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import ru.frosteye.beermap.R;
import ru.frosteye.beermap.presentation.view.impl.widget.ProfileMenuFieldRow;
import ru.frosteye.ovsa.presentation.adapter.ModelViewHolder;

/**
 * Created by ovcst on 02.08.2017.
 */

public class ProfileMenuField extends AbstractFlexibleItem<ModelViewHolder<ProfileMenuFieldRow>> {

    public static final int NEW_POST = 0;
    public static final int ADD_PHOTO = 1;
    public static final int FAVORITE_BEER = 2;
    public static final int FAVORITE_BARS = 3;
    public static final int MY_RATINGS = 4;

    private int id;
    private int icon;
    private String title;

    public ProfileMenuField(int id, int icon, String title) {
        this.id = id;
        this.icon = icon;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfileMenuField menuField = (ProfileMenuField) o;

        return id == menuField.id;

    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_profile_menu_field;
    }

    @Override
    public ModelViewHolder<ProfileMenuFieldRow> createViewHolder(FlexibleAdapter adapter,
                                                          LayoutInflater inflater,
                                                          ViewGroup parent) {
        return new ModelViewHolder<>(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ModelViewHolder<ProfileMenuFieldRow> holder,
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

    public static List<ProfileMenuField> createDefault(Context context) {
        List<ProfileMenuField> out = new ArrayList<>();
        out.add(new ProfileMenuField(NEW_POST, R.drawable.ic_new_post, context.getString(R.string.new_post)));
        out.add(new ProfileMenuField(ADD_PHOTO, R.drawable.ic_new_photo, context.getString(R.string.add_photo)));
        out.add(new ProfileMenuField(FAVORITE_BEER, R.drawable.ic_fav_bar, context.getString(R.string.fav_beer)));
        out.add(new ProfileMenuField(FAVORITE_BARS, R.drawable.ic_fav_bar, context.getString(R.string.fav_bars)));
        out.add(new ProfileMenuField(MY_RATINGS, R.drawable.ic_fav_bar, context.getString(R.string.my_ratings)));


        return out;
    }
}
