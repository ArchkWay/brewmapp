package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.FilterBreweryField;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.ModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseRelativeLayout;

/**
 * Created by nixus on 07.12.2017.
 */

public class FilterBreweryRowField extends BaseRelativeLayout implements ModelView<FilterBreweryField> {

    @BindView(R.id.filter_name)
    TextView filterTitle;
    @BindView(R.id.selected_filter) TextView selectedFilter;
    @BindView(R.id.icon_filter)
    ImageView icon;

    private FilterBreweryField model;

    public FilterBreweryRowField(Context context) {
        super(context);
    }

    public FilterBreweryRowField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilterBreweryRowField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FilterBreweryRowField(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public FilterBreweryField getModel() {
        return model;
    }

    public void setModel(FilterBreweryField model) {
        this.model = model;
        this.filterTitle.setText(model.getTitle());
        this.selectedFilter.setTypeface(null, Typeface.BOLD_ITALIC);
        this.selectedFilter.setText(model.getSelectedFilter());
        this.icon.setImageResource(model.getIcon());
    }
}

