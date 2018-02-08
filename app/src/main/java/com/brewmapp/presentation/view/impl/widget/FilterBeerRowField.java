package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.FilterBeerField;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.ModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseRelativeLayout;

/**
 * Created by nixus on 25.11.2017.
 */

public class FilterBeerRowField extends BaseRelativeLayout implements InteractiveModelView<FilterBeerField> {

    @BindView(R.id.filter_name)
    TextView filterTitle;
    @BindView(R.id.selected_filter) TextView selectedFilter;
    @BindView(R.id.icon_filter)
    ImageView icon;
    @BindView(R.id.clear_filter) ImageView clear_filter;

    private FilterBeerField model;
    private Listener listener;
    public FilterBeerRowField(Context context) {
        super(context);
    }

    public FilterBeerRowField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilterBeerRowField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FilterBeerRowField(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public FilterBeerField getModel() {
        return model;
    }

    public void setModel(FilterBeerField model) {
        this.model = model;
        this.filterTitle.setText(model.getTitle());
        this.selectedFilter.setTypeface(null, Typeface.BOLD_ITALIC);
        this.selectedFilter.setText(model.getSelectedFilter());
        this.icon.setImageResource(model.getIcon());
        setOnClickListener(view -> listener.onModelAction(FilterBeerField.CODE_CLICK_FILTER_START_SELECTION,model));
        clear_filter.setVisibility(model.getSelectedItemId()==null?GONE:VISIBLE);
        clear_filter.setOnClickListener(view -> listener.onModelAction(FilterBeerField.CODE_CLICK_FILTER_CLEAR,model));
    }

    @Override
    public void setListener(Listener listener) {
        this.listener=listener;
    }
}

