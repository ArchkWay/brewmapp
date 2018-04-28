package com.brewmapp.data.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import com.brewmapp.R;
import com.brewmapp.presentation.view.impl.widget.CardOptionRow;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.adapter.ModelViewHolder;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.tool.DateTools;

import static ru.frosteye.ovsa.data.storage.ResourceHelper.getString;

/**
 * Created by ovcst on 02.08.2017.
 */

public class CardMenuField extends AbstractFlexibleItem<ModelViewHolder<CardOptionRow>>  {

    public static final int NEW_POST = 0;
    public static final int ADD_PHOTO = 1;
    public static final int FAVORITE_BEER = 2;
    public static final int FAVORITE_RESTO = 3;
    public static final int MY_RATINGS = 4;
    public static final int SUBSCRIBE = 5;
    public static final int MY_WORK = 8;
    public static final int MY_RESUME = 9;

    public static final int EVENT_DATE = 5;
    public static final int EVENT_LOCATION = 6;
    public static final int EVENT_SHARE = 7;
    public static final int EVENT_ALERT = 8;


    private int id;
    private int icon;
    private String title;
    private boolean extraSpaceBottom = false;
    private String externalId;

    public CardMenuField(int id, int icon, String title) {
        this.id = id;
        this.icon = icon;
        this.title = title;
    }

    public boolean isExtraSpaceBottom() {
        return extraSpaceBottom;
    }

    public CardMenuField setExtraSpaceBottom(boolean extraSpaceBottom) {
        this.extraSpaceBottom = extraSpaceBottom;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardMenuField menuField = (CardMenuField) o;

        return id == menuField.id;

    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_profile_menu_field;
    }

    @Override
    public ModelViewHolder<CardOptionRow> createViewHolder(FlexibleAdapter adapter,
                                                           LayoutInflater inflater,
                                                           ViewGroup parent) {
        return new ModelViewHolder<>(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ModelViewHolder<CardOptionRow> holder,
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

    public void setTitle(String title) {
        this.title = title;
    }

    public static List<IFlexible> createProfileViewItems(Context context) {
        List<CardMenuField> out = new ArrayList<>();
        out.add(new CardMenuField(SUBSCRIBE, R.drawable.ic_subscrible, context.getString(R.string.fav_subscrabe)));
        out.add(new CardMenuField(FAVORITE_BEER, R.drawable.ic_fav_beer, context.getString(R.string.fav_beer)));
        out.add(new CardMenuField(FAVORITE_RESTO, R.drawable.ic_fav_resto, context.getString(R.string.fav_bars)));
        out.add(new CardMenuField(MY_RATINGS, R.drawable.ic_my_elevation, context.getString(R.string.my_ratings)));
        out.add(new CardMenuField(MY_WORK, R.drawable.ic_my_work, context.getString(R.string.my_work)));
        out.add(new CardMenuField(MY_RESUME, R.drawable.ic_my_experiance, context.getString(R.string.my_resume)));

        return new ArrayList<>(out);
    }

    public static List<CardMenuField> createProfileItems(Context context) {
        List<CardMenuField> out = new ArrayList<>();
        out.add(new CardMenuField(NEW_POST, R.drawable.ic_new_post, context.getString(R.string.new_post)));
        out.add(new CardMenuField(ADD_PHOTO, R.drawable.ic_new_photo, context.getString(R.string.add_photo)));
        out.add(new CardMenuField(FAVORITE_BEER, R.drawable.ic_fav_beer, context.getString(R.string.fav_beer)));
        out.add(new CardMenuField(FAVORITE_RESTO, R.drawable.ic_fav_resto, context.getString(R.string.fav_bars)));
        out.add(new CardMenuField(MY_RATINGS, R.drawable.ic_my_elevation, context.getString(R.string.my_ratings)));


        return out;
    }

    public static List<CardMenuField> createEventItems(Event event, Context context) {
        List<CardMenuField> out = new ArrayList<>();
        out.add(new CardMenuField(EVENT_DATE, R.drawable.ic_calendar_gray, getString(R.string.pattern_event_start,
                DateTools.formatDottedDate(event.getDateFrom()), event.getTimeFrom())));
        out.add(new CardMenuField(EVENT_LOCATION, R.drawable.ic_loc_gray, event.getLocation().getFormattedAddress()));

        out.add(new CardMenuField(EVENT_SHARE, R.drawable.ic_share, getString(R.string.share)));
        out.add(new CardMenuField(EVENT_ALERT, R.drawable.ic_alert, getString(R.string.alert)));

        return out;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalId() {
        return externalId;
    }

}
